package io.github.shakdwipeea.vcr.play;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import java.io.File;

import io.github.shakdwipeea.vcr.R;
import io.github.shakdwipeea.vcr.search.HomeActivity;

public class PlayActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getFiles();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(view.getContext(), HomeActivity.class);
                startActivity(i);

            }
        });
    }

    private void getFiles() {
        File direct = new File(Environment.getExternalStorageDirectory()
                + HomeActivity.MUSIC_DIR);

        File[] songs = direct.listFiles();

        if (songs.length != 0) {
            for (int i = 0; i < songs.length; i++) {
                String fileName = songs[i].getName();
                System.out.println(fileName);
            }
        }

    }

}
