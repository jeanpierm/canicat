package com.jeanpier.canicat.core;

import com.jeanpier.canicat.config.Routes;
import com.jeanpier.canicat.data.network.DataApi;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitHelper {

    private static Retrofit retrofit;

    public static Retrofit getHttpClient() {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(Routes.BASE_URI)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }
}
