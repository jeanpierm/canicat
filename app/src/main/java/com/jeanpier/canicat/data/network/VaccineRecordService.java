package com.jeanpier.canicat.data.network;

import com.jeanpier.canicat.core.RetrofitHelper;
import com.jeanpier.canicat.data.model.VaccineRecord;

import java.util.List;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.http.Body;

public class VaccineRecordService {

    private final Retrofit retrofit = RetrofitHelper.getHttpClient();

    public Call<VaccineRecord> saveVaccineRecord(VaccineRecord vaccineRecord){
        return retrofit.create(DataApi.class).saveVaccineRecord(vaccineRecord);
    }

    public Call<List<VaccineRecord>> getVaccineByPetId(String petId){
        return retrofit.create(DataApi.class).getVaccineByPetId(petId);
    }

    public Call<Void> deleteVaccine(String vaccineId){
        return retrofit.create(DataApi.class).deleteVaccine(vaccineId);
    }

}
