package com.jeanpier.canicat.data.network;

import com.jeanpier.canicat.core.RetrofitHelper;
import com.jeanpier.canicat.data.model.VaccineRecord;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.http.Body;

public class VaccineRecordService {

    private final Retrofit retrofit = RetrofitHelper.getHttpClient();

    public Call<VaccineRecord> saveVaccineRecord(VaccineRecord vaccineRecord){
        return retrofit.create(DataApi.class).saveVaccineRecord(vaccineRecord);
    }

}
