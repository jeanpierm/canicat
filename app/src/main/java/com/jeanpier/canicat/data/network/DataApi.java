package com.jeanpier.canicat.data.network;

import com.jeanpier.canicat.config.Routes;
import com.jeanpier.canicat.data.model.Pet;
import com.jeanpier.canicat.data.model.User;
import com.jeanpier.canicat.data.network.responses.LoginResponse;
import com.jeanpier.canicat.data.network.responses.PostPetResponse;
import com.jeanpier.canicat.data.network.responses.PostUserResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface DataApi {

    @GET(Routes.API_APTH + Routes.USERS_PATH + "/{userId}" + Routes.PETS_PATH)
    Call<List<Pet>> getPetsByUserId(@Path("userId") String userId);

    @POST(Routes.API_APTH + Routes.PETS_PATH)
    Call<PostPetResponse> createPet(@Body Pet pet);

    @POST(Routes.API_APTH + Routes.USERS_PATH)
    Call<PostUserResponse> createUser(@Body User user);

    @PATCH(Routes.API_APTH + Routes.PETS_PATH + "/{petId}")
    Call<Void> updatePetById(@Path("petId") String petId, @Body Pet pet);

    @DELETE(Routes.API_APTH + Routes.PETS_PATH + "/{petId}")
    Call<Void> deletePetById(@Path("petId") String id);

    @POST(Routes.API_APTH + Routes.LOGIN_PATH)
    @FormUrlEncoded
    Call<LoginResponse> login(@Field("email") String email, @Field("password") String password);
}
