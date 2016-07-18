package io.github.shakdwipeea.vcr.data;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by akash on 7/3/16.
 */
public class ServiceFactory {
    private  static ApiService apiService;
    private static GoApiService goApiService;

    public static String BASE_URL = "http://torpedo.servegame.com";

    public static ApiService getInstance() {

        if (apiService == null) {
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL + ":3000")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            apiService = retrofit.create(ApiService.class);
        }

        return apiService;
    }

    public static GoApiService getGoService() {
        if (goApiService == null) {
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL + ":8000")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            goApiService = retrofit.create(GoApiService.class);
        }

        return goApiService;
    }
}
