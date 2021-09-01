package com.jeanpier.canicat.ui.records.vaccine.viewmodels;

import android.app.Application;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jeanpier.canicat.R;
import com.jeanpier.canicat.data.model.Vaccine;
import com.jeanpier.canicat.data.network.VaccineService;
import com.jeanpier.canicat.data.network.responses.ErrorResponse;
import com.jeanpier.canicat.util.AlertUtil;

import java.lang.reflect.Type;
import java.util.Collections;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class VaccineViewModel extends AndroidViewModel {

    private static final String TAG = "VaccineViewModel";
    private final MutableLiveData<Integer> loading = new MutableLiveData<>();
    private final VaccineService vaccineService = new VaccineService();
    private final MutableLiveData<String> petId = new MutableLiveData<>();
    public MutableLiveData<List<Vaccine>> vaccines;
    private final Gson gson = new Gson();
    private final Type errorType = new TypeToken<ErrorResponse>() {
    }.getType();

    public VaccineViewModel(@NonNull Application application) {
        super(application);
    }

    public LiveData<List<Vaccine>> getVaccines() {
        if (vaccines == null) {
            vaccines = new MutableLiveData<>();
            loadVaccines();
        }
        return vaccines;
    }

    public LiveData<String> getPetId() {
        return petId;
    }

    public void setPetId(String petId) {
        this.petId.setValue(petId);
    }

    public void clearPetId() {
        petId.setValue("");
    }

    public LiveData<Integer> isLoading() {
        return loading;
    }

    public void loadVaccines() {
        loading.postValue(View.VISIBLE);
        String currentPetId = petId.getValue();
        vaccineService.getVaccineByPetId(currentPetId).enqueue(new Callback<List<Vaccine>>() {
            @Override
            public void onResponse(@NonNull Call<List<Vaccine>> call, @NonNull Response<List<Vaccine>> response) {
                loading.postValue(View.GONE);
                if (response.isSuccessful()) {
                    List<Vaccine> petVaccines = response.body() != null ?
                            response.body() : Collections.emptyList();
                    vaccines.postValue(petVaccines);
                } else {
                    if (response.errorBody() == null) {
                        AlertUtil.showErrorAlert(
                                getApplication().getString(R.string.get_vaccines_error), getApplication());
                        return;
                    }
                    ErrorResponse errorResponse = gson.fromJson(response.errorBody().charStream(), errorType);
                    AlertUtil.showErrorAlert(errorResponse.getError(), getApplication());
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<Vaccine>> call, @NonNull Throwable t) {
                loading.postValue(View.GONE);
                AlertUtil.showGenericErrorAlert(getApplication());
                t.printStackTrace();
            }
        });
    }

}
