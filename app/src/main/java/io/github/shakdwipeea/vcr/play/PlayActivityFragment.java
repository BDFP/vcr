package io.github.shakdwipeea.vcr.play;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import io.github.shakdwipeea.vcr.R;

/**
 * A placeholder fragment containing a simple view.
 */
public class PlayActivityFragment extends Fragment {

    public PlayActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_play, container, false);
    }
}
