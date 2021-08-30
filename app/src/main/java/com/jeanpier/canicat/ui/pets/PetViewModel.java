package com.jeanpier.canicat.ui.pets;

import android.app.Application;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.jeanpier.canicat.R;
import com.jeanpier.canicat.core.RetrofitHelper;
import com.jeanpier.canicat.data.model.Pet;
import com.jeanpier.canicat.data.network.PetService;
import com.jeanpier.canicat.util.ToastUtil;

import java.util.Collections;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PetViewModel extends AndroidViewModel {

    private static final String TAG = "PetViewModel";
    private final MutableLiveData<Integer> isLoading = new MutableLiveData<>();
    private final PetService petService = new PetService();
    private MutableLiveData<List<Pet>> pets;

    public PetViewModel(@NonNull Application application) {
        super(application);
    }

    public LiveData<List<Pet>> getPets() {
        if (pets == null) {
            pets = new MutableLiveData<>();
            loadPets();
        }
        return pets;
    }

    public LiveData<Integer> getIsLoading() {
        return isLoading;
    }

    private void loadPets() {
        isLoading.postValue(View.VISIBLE);
        Call<List<Pet>> call = petService.getPetsByUserId("21e0a26e-01c1-409d-9b19-f6393019aa9b");
        call.enqueue(new Callback<List<Pet>>() {
            @Override
            public void onResponse(@NonNull Call<List<Pet>> call, @NonNull Response<List<Pet>> response) {
                if (response.isSuccessful()) {
                    isLoading.postValue(View.GONE);
                    List<Pet> userPets = response.body() != null ?
                            response.body() : Collections.emptyList();
                    Log.d(TAG, "onResponse: Pets cargados con éxito: " + userPets.size());
                    pets.postValue(userPets);
                } else {
                    ToastUtil.show(getApplication(), getApplication().getString(R.string.get_pets_error));
                    Log.d(TAG, "onResponse: Ha ocurrido un error");
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<Pet>> call, @NonNull Throwable t) {
                ToastUtil.show(getApplication(), getApplication().getString(R.string.get_pets_failed));
                Log.d(TAG, "onFailure: " + t.getMessage());
            }
        });
    }
}