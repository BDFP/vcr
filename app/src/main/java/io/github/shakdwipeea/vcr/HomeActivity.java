package io.github.shakdwipeea.vcr;

import android.app.DownloadManager;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnTextChanged;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeActivity extends AppCompatActivity implements View.OnKeyListener, Callback<Song>, View.OnClickListener {
    @BindView(R.id.search_query) EditText searchQuery;
    @BindView(R.id.progress_bar) ProgressBar progressBar;

    @BindView(R.id.result_card) CardView resultCard;
    @BindView(R.id.song_name) TextView songName;
    @BindView(R.id.download_button) Button downloadBtn;

    Song song;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ButterKnife.bind(this);

        searchQuery.setOnKeyListener(this);
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
        if (id == R.id.action_settings) {
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
        songName.setText(getSongName(song.getUrl()));
        downloadBtn.setOnClickListener(this);
        resultCard.setVisibility(View.VISIBLE);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.download_button:
                downloadSong();
                break;
            default:break;
        }
    }

    private String getSongName(String songUrl) {
        return songUrl.split("/")[2];
    }

    private void downloadSong() {
        DownloadManager dm = (DownloadManager) getSystemService(DOWNLOAD_SERVICE);
        Uri source = Uri.parse(ServiceFactory.BASE_URL)
                .buildUpon()
                .appendPath(song.getUrl())
                .build();

        DownloadManager.Request request = new DownloadManager.Request(source);
        request.setTitle(getSongName(song.getUrl()));
        request.setDescription("Downloading song from VCR");
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        request.allowScanningByMediaScanner();

        dm.enqueue(request);
    }
}
