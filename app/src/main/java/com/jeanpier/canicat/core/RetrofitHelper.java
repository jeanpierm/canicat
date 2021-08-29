package com.jeanpier.canicat.core;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitHelper {

    // Para ver tu ip local ejecuta el comando "ipconfig" en cmd/powershell. (IPv4)
    private static final String IP_LOCAL = "192.168.1.36";
    private static final String PORT = "3000";
    public static final String BASE_URI = "http://" + IP_LOCAL + ":" + PORT;
    public static final String PETS_PATH = "pets";
    public static final String USERS_PATH = "users";
    public static final String VACCINE_PATH = "vaccine-records";

    private static Retrofit retrofit;

    public static Retrofit getHttpClient() {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URI + "/api/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }
}
