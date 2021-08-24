package com.jeanpier.canicat.core;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitHelper {
    public static final String BASE_URI = "https://localhost/3000/api/canicat/";
    public static final String PETS_PATH = "/pets";
    private static Retrofit retrofit;

    public static Retrofit getHttpClient() {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URI)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }
}
