package io.github.shakdwipeea.vcr.search;

import android.Manifest;
import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.github.shakdwipeea.vcr.R;
import io.github.shakdwipeea.vcr.data.ServiceFactory;
import io.github.shakdwipeea.vcr.data.Song;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeActivity extends AppCompatActivity implements View.OnKeyListener, Callback<Song>, View.OnClickListener {
    private static final int MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE = 100;

    @BindView(R.id.search_query) EditText searchQuery;
    @BindView(R.id.progress_bar) ProgressBar progressBar;

    @BindView(R.id.result_card) CardView resultCard;
    @BindView(R.id.song_name) TextView songName;
    @BindView(R.id.download_button) Button downloadBtn;

    InterstitialAd mInterstitialAd;

    @BindView(R.id.adView) AdView adView;

    Song song;

    public static final String MUSIC_DIR = "/vcr_music";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ButterKnife.bind(this);

        requestNewBanner();

        mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId("ca-app-pub-6145835042223031/9285928909");

        mInterstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdClosed() {
                requestNewInterstitial();
            }
        });

        requestNewInterstitial();

        searchQuery.setOnKeyListener(this);
    }

    private void requestNewInterstitial() {
        AdRequest adRequest = new AdRequest.Builder()
                .addTestDevice("B6B257EC8BE4173AE786D0B71F1E0C52")
                .build();

        mInterstitialAd.loadAd(adRequest);
    }

    private void requestNewBanner() {
        AdRequest adRequest = new AdRequest.Builder()
                .addTestDevice("B6B257EC8BE4173AE786D0B71F1E0C52")
                .build();

        adView.loadAd(adRequest);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_rate) {
            try {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + this.getPackageName())));
            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(this, "Could not open play Store", Toast.LENGTH_SHORT)
                        .show();
            }
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * Handle key actions
     *
     * @param view View where action took place
     * @param keyCode key pressed
     * @param keyEvent key action
     * @return true if handled
     */
    @Override
    public boolean onKey(View view, int keyCode, KeyEvent keyEvent) {

        if (keyEvent.getAction() == KeyEvent.ACTION_DOWN) {
            switch (keyCode) {
                case KeyEvent.KEYCODE_DPAD_CENTER:
                case KeyEvent.KEYCODE_ENTER:
                    searchSong(searchQuery.getText().toString());
                    return true;
                default: break;
            }
        }
        return false;
    }

    /**
     * Call the API to get downloadable URL
     *
     * @param query Song to search for
     */
    void searchSong(String query) {
        View v = this.getCurrentFocus();
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(v.getWindowToken(), 0);

        progressBar.setVisibility(View.VISIBLE);
        ServiceFactory.getInstance()
                .getSongMetadata(query)
                .enqueue(this);
    }

    /**
     * On HTTP Success Response
     *
     * @param call
     * @param response
     */
    @Override
    public void onResponse(Call<Song> call, Response<Song> response) {
        progressBar.setVisibility(View.GONE);

        song = response.body();
        displaySongDetails();
    }

    /**
     * On HTTP Error Response
     *
     * @param call
     * @param t
     */
    @Override
    public void onFailure(Call<Song> call, Throwable t) {
        progressBar.setVisibility(View.GONE);

        Toast.makeText(this, t.getMessage(), Toast.LENGTH_SHORT)
                .show();
    }


    private void displaySongDetails() {
        songName.setText(getSongName());
        downloadBtn.setOnClickListener(this);
        resultCard.setVisibility(View.VISIBLE);
        downloadBtn.setEnabled(true);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.download_button:
                downloadBtn.setEnabled(false);
                downloadSong();
                showAd();
                break;
            default:break;
        }
    }

    private void showAd() {
        if (mInterstitialAd.isLoaded()) {
            mInterstitialAd.show();
        }
    }

    private String getSongName() {
        if (song != null) {
            String[] strings = song.getUrl().split("/");
            return strings[strings.length - 1];
        }
        Toast.makeText(this, "Song not found", Toast.LENGTH_SHORT)
                .show();
        return null;
    }

    private void downloadSong() {

        if (!checkPerms()) {
            return;
        }

        File direct = new File(Environment.getExternalStorageDirectory()
                + MUSIC_DIR);

        if (!direct.exists()) {
            direct.mkdirs();
        }

        DownloadManager dm = (DownloadManager) getSystemService(DOWNLOAD_SERVICE);
        try {
            URL url = new URL(song.getUrl());
            URI uri = new URI(url.getProtocol(), url.getUserInfo(), url.getHost(), url.getPort(),
                    url.getPath(), url.getQuery(), url.getRef());
            url = uri.toURL();

            DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url.toString()));
            request.setTitle(getSongName());
            request.setDescription("Downloading song from VCR");
            request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
            request.allowScanningByMediaScanner();
            request.setDestinationInExternalPublicDir(MUSIC_DIR, getSongName());

            dm.enqueue(request);

            Toast.makeText(this, "Download will start shortly", Toast.LENGTH_SHORT)
                    .show();
        } catch (MalformedURLException | URISyntaxException e) {
            Toast.makeText(this, "Bad Url", Toast.LENGTH_SHORT).show();
        }


    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                    downloadSong();

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    Toast.makeText(this, "No permissions", Toast.LENGTH_SHORT)
                            .show();
                }
                return;

            }
        }
    }

    private boolean checkPerms() {
        int permissionCheck = ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE)) {

                // Show an expanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

            } else {

                // No explanation needed, we can request the permission.

                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE);

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }

            return false;
        } else {
            return true;
        }
    }
}
