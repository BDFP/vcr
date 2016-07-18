package io.github.shakdwipeea.vcr.data;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * @author Akash Shakdwipeea
 *         Created on 7/18/16
 */
public interface GoApiService {
    @GET("/playlist/{url}")
    Call<List<BillboardSong>> getBillboardSong(@Path("url") String playlistUrl);

    @GET("/playlist")
    Call<List<Playlist>> getPlayList();
}
