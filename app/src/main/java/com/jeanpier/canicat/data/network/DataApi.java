package com.jeanpier.canicat.data.network;

import com.jeanpier.canicat.config.Routes;
import com.jeanpier.canicat.data.model.Pet;
import com.jeanpier.canicat.data.model.VaccineRecord;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface DataApi {
    @GET("/api" + Routes.USERS_PATH + "/{userId}" + Routes.PETS_PATH)
    Call<List<Pet>> getPetsByUserId(@Path("userId") String userId);

    @POST("/api" + Routes.VACCINE_PATH)
    Call<VaccineRecord> saveVaccineRecord(@Body VaccineRecord vaccineRecord);

}
