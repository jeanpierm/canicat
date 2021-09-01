package com.jeanpier.canicat.ui.records.vaccine;

import android.app.Application;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.jeanpier.canicat.R;
import com.jeanpier.canicat.data.model.VaccineRecord;
import com.jeanpier.canicat.data.network.VaccineRecordService;
import com.jeanpier.canicat.util.ToastUtil;

import java.util.Collections;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class VaccineViewModel extends AndroidViewModel {
    private static final String TAG = "VaccineViewModel";
    private final MutableLiveData<Integer> loading = new MutableLiveData<>();
    private final VaccineRecordService vaccineRecordService =  new VaccineRecordService();
    private final MutableLiveData<String> uid = new MutableLiveData<>();
    public MutableLiveData<List<VaccineRecord>> vaccines;

    public VaccineViewModel(@NonNull Application application) {
        super(application);
    }

    public LiveData<List<VaccineRecord>> getVaccines() {
        if (vaccines == null) {
            vaccines = new MutableLiveData<>();
            loadVaccines();
        }
        return vaccines;
    }

    public void loadVaccines(){
        loading.postValue(View.VISIBLE);
        String petId = uid.getValue();

        Call<List<VaccineRecord>> call = vaccineRecordService.getVaccineByPetId("f8b2762e-624d-4275-abf4-88d9a5a6dac6");
        call.enqueue(new Callback<List<VaccineRecord>>() {
            @Override
            public void onResponse(Call<List<VaccineRecord>> call, Response<List<VaccineRecord>> response) {
                if(response.isSuccessful()){
                    loading.postValue(View.GONE);
                    List<VaccineRecord> petVaccines = response.body() != null ?
                            response.body() : Collections.emptyList();

                    Log.d(TAG, "onResponse: Vaccines cargados con éxito: " + petVaccines.size());
                    vaccines.postValue(petVaccines);
                }else{
                    ToastUtil.show(getApplication(), getApplication().getString(R.string.get_vaccines_error));
                    Log.d(TAG, "onResponse: Ha ocurrido un error");
                }
            }

            @Override
            public void onFailure(Call<List<VaccineRecord>> call, Throwable t) {
                ToastUtil.show(getApplication(), getApplication().getString(R.string.get_vaccines_fail));
                Log.d(TAG, "onFailure: " + t.getMessage());
                t.printStackTrace();
            }
        });
    }

    public LiveData<String> getUID() {
        return uid;
    }
    public void setUID(String petId) {
        uid.setValue(petId);
    }
    public LiveData<Integer> isLoading() {
        return loading;
    }

}
