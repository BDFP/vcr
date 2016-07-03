package io.github.shakdwipeea.vcr;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * Created by akash on 7/3/16.
 */
public interface ApiService {
    @GET("/download/{name}")
    Call<Song> getSongMetadata(@Path("name") String songName);
}
