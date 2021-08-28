package com.jeanpier.canicat.data.network;

import com.jeanpier.canicat.core.RetrofitHelper;
import com.jeanpier.canicat.data.model.Pet;

import java.util.List;
import java.util.UUID;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface DataApi {

    @GET(RetrofitHelper.USERS_PATH + "/{userId}/" + RetrofitHelper.PETS_PATH)
    Call<List<Pet>> getPetsByUserId(@Path("userId") String userId);
}
