package com.jeanpier.canicat.data.network;

import android.util.Log;

import androidx.annotation.NonNull;

import com.jeanpier.canicat.data.model.Pet;

import java.util.List;
import java.util.UUID;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PetService {
    private static final String TAG = "PetService";
    private PetApiClient api;
    private List<Pet> pets;

    public PetService(PetApiClient api) {
        this.api = api;
    }

    public List<Pet> getPetsByUserId(UUID userId) {
        Call<List<Pet>> call = api.getPetsByUserId(userId);
        call.enqueue(new Callback<List<Pet>>() {
            @Override
            public void onResponse(@NonNull Call<List<Pet>> call, @NonNull Response<List<Pet>> response) {
                List<Pet> body = response.body();
                if (body == null) return;
                pets = body;
            }

            @Override
            public void onFailure(@NonNull Call<List<Pet>> call, @NonNull Throwable t) {
                Log.d(TAG, "onFailure: " + t.getMessage());
            }
        });
        return pets;
    }
}
