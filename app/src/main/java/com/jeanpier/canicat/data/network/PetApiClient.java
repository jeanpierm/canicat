package com.jeanpier.canicat.data.network;

import com.jeanpier.canicat.core.RetrofitHelper;
import com.jeanpier.canicat.data.model.Pet;

import java.util.List;
import java.util.UUID;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface PetApiClient {

    @GET(RetrofitHelper.BASE_URI + RetrofitHelper.PETS_PATH + "/{userId}")
    Call<List<Pet>> getPetsByUserId(@Path("userId") UUID userId);
}
