package com.jeanpier.canicat.data.network;

import com.jeanpier.canicat.config.Routes;
import com.jeanpier.canicat.data.model.Pet;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface DataApi {
    @GET("/api" + Routes.USERS_PATH + "/{userId}" + Routes.PETS_PATH)
    Call<List<Pet>> getPetsByUserId(@Path("userId") String userId);
}
