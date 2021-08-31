package com.jeanpier.canicat.data.network;

import com.jeanpier.canicat.core.RetrofitHelper;
import com.jeanpier.canicat.data.model.Pet;
import com.jeanpier.canicat.data.network.responses.PostPetResponse;

import java.util.List;

import retrofit2.Call;

public class PetService {
    private final DataApi dataApi = RetrofitHelper.getHttpClient().create(DataApi.class);

    public Call<List<Pet>> getByUserId(String userId) {
        return dataApi.getPetsByUserId(userId);
    }

    public Call<Void> updateById(String petId, Pet pet) {
        return dataApi.updatePetById(petId, pet);
    }

    public Call<PostPetResponse> create(Pet pet) {
        return dataApi.createPet(pet);
    }

    public Call<Void> deleteById(String id) {
        return dataApi.deletePetById(id);
    }
}
