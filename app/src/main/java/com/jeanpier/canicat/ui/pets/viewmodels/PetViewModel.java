package com.jeanpier.canicat.ui.pets.viewmodels;

import android.app.Application;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.jeanpier.canicat.R;
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
    private final MutableLiveData<Integer> loading = new MutableLiveData<>();
    private final PetService petService = new PetService();
    private final MutableLiveData<String> uid = new MutableLiveData<>();
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

    public LiveData<String> getUID() {
        return uid;
    }

    public void setUID(String userId) {
        uid.setValue(userId);
    }

    public LiveData<Integer> isLoading() {
        return loading;
    }

    public void loadPets() {
        loading.postValue(View.VISIBLE);
        String userId = uid.getValue();
        Call<List<Pet>> call = petService.getByUserId(userId);
        call.enqueue(new Callback<List<Pet>>() {
            @Override
            public void onResponse(@NonNull Call<List<Pet>> call, @NonNull Response<List<Pet>> response) {
                if (response.isSuccessful()) {
                    loading.postValue(View.GONE);
                    List<Pet> userPets = response.body() != null ?
                            response.body() : Collections.emptyList();
                    Log.d(TAG, "onResponse: Pets cargados con éxito: " + userPets.size());
                    pets.postValue(userPets);
                } else {
                    ToastUtil.show(getApplication(), getApplication().getString(R.string.get_pets_error));
                    Log.d(TAG, "onResponse: Ha ocurrido un error" + response.errorBody());
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<Pet>> call, @NonNull Throwable t) {
                ToastUtil.show(getApplication(), getApplication().getString(R.string.get_pets_failed));
                Log.d(TAG, "onFailure: " + t.getMessage());
                t.printStackTrace();
            }
        });
    }
}