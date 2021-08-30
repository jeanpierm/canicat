package com.jeanpier.canicat.data.network;

import com.jeanpier.canicat.config.Routes;
import com.jeanpier.canicat.data.model.Pet;
import com.jeanpier.canicat.data.network.responses.PostPetResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface DataApi {

    @GET(Routes.API_APTH + Routes.USERS_PATH + "/{userId}" + Routes.PETS_PATH)
    Call<List<Pet>> getPetsByUserId(@Path("userId") String userId);

    @POST(Routes.API_APTH + Routes.PETS_PATH)
    Call<PostPetResponse> createPet(@Body Pet pet);

    @PATCH(Routes.API_APTH + Routes.PETS_PATH + "/{petId}")
    Call<Void> updatePetById(@Path("petId") String petId, @Body Pet pet);

    @DELETE(Routes.API_APTH + Routes.PETS_PATH + "/{petId}")
    Call<Void> deletePetById(@Path("petId") String id);
}
