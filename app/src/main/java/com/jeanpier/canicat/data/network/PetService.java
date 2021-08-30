package com.jeanpier.canicat.data.network;

import com.jeanpier.canicat.core.RetrofitHelper;
import com.jeanpier.canicat.data.model.Pet;

import java.util.List;

import retrofit2.Call;
import retrofit2.Retrofit;

public class PetService {
    private final Retrofit retrofit = RetrofitHelper.getHttpClient();

    public Call<List<Pet>> getPetsByUserId(String userId) {
        return retrofit.create(DataApi.class).getPetsByUserId(userId);
    }
}
