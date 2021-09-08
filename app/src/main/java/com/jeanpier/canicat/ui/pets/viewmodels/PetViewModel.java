package com.jeanpier.canicat.ui.pets.viewmodels;

import android.app.Application;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jeanpier.canicat.R;
import com.jeanpier.canicat.data.model.Pet;
import com.jeanpier.canicat.data.network.PetService;
import com.jeanpier.canicat.data.network.responses.ErrorResponse;
import com.jeanpier.canicat.util.AlertUtil;

import java.lang.reflect.Type;
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
    private final Gson gson = new Gson();
    private final Type errorType = new TypeToken<ErrorResponse>() {
    }.getType();


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

    public void clearPets() {
        pets = null;
    }

    public LiveData<String> getUID() {
        return uid;
    }

    public void setUID(String userId) {
        uid.setValue(userId);
    }

    public void clearUID() {
        uid.setValue(null);
    }

    public LiveData<Integer> isLoading() {
        return loading;
    }

    public void loadPets() {
        if (pets == null) pets = new MutableLiveData<>();
        loading.postValue(View.VISIBLE);
        String userId = uid.getValue();
        petService.getByUserId(userId).enqueue(new Callback<List<Pet>>() {
            @Override
            public void onResponse(@NonNull Call<List<Pet>> call, @NonNull Response<List<Pet>> response) {
                loading.postValue(View.GONE);
                if (response.isSuccessful()) {
                    List<Pet> userPets = response.body() != null ?
                            response.body() : Collections.emptyList();
                    pets.postValue(userPets);
                } else {
                    if (response.errorBody() == null) {
                        AlertUtil.showErrorAlert(
                                getApplication().getString(R.string.get_pets_error), getApplication());
                        return;
                    }
                    ErrorResponse errorResponse = gson.fromJson(response.errorBody().charStream(), errorType);
                    AlertUtil.showErrorAlert(errorResponse.getError(), getApplication());
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<Pet>> call, @NonNull Throwable t) {
                loading.postValue(View.GONE);
                AlertUtil.showGenericErrorAlert(getApplication());
                t.printStackTrace();
            }
        });
    }
}