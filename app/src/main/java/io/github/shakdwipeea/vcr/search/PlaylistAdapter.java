package io.github.shakdwipeea.vcr.search;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.github.shakdwipeea.vcr.R;
import io.github.shakdwipeea.vcr.data.Playlist;

/**
 * @author Akash Shakdwipeea
 *         Created on 7/18/16
 */
public class PlaylistAdapter extends RecyclerView.Adapter<PlaylistAdapter.ViewHolder>{
    private List<Playlist> playlists;

    private ViewHolder.Callback callback;

    public PlaylistAdapter(ViewHolder.Callback callback) {
        this.callback = callback;
        this.playlists = new ArrayList<>();
    }

    public void setPlaylists(List<Playlist> playlists) {
        this.playlists.clear();
        this.playlists.addAll(playlists);
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.playlist_card, parent, false);

        ViewHolder vh = new ViewHolder(v);

        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final Playlist playlist = playlists.get(position);
        holder.playlistName.setText(playlist.getName());

        holder.playlistName.setText(playlist.getName());
        holder.searchPlaylist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                callback.onSearch(playlist.getUrl());
            }
        });
    }

    @Override
    public int getItemCount() {
        return playlists.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.playlist_name) TextView playlistName;
        @BindView(R.id.search_playlist) Button searchPlaylist;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public interface Callback {
            void onSearch(String url);
        }
    }
}
