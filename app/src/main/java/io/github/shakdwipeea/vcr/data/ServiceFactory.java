package io.github.shakdwipeea.vcr.data;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by akash on 7/3/16.
 */
public class ServiceFactory {
    private  static ApiService apiService;

    public static String BASE_URL = "http://torpedo.servegame.com:3000";

    public static ApiService getInstance() {
        if (apiService == null) {
            Retrofit build = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            apiService = build.create(ApiService.class);

        }

        return apiService;
    }
}
