package com.jeanpier.canicat.data.network;

import com.jeanpier.canicat.core.RetrofitHelper;
import com.jeanpier.canicat.data.model.Vaccine;

import java.util.List;

import retrofit2.Call;

public class VaccineService {

    private final DataApi dataApi = RetrofitHelper.getHttpClient().create(DataApi.class);

    public Call<Vaccine> saveVaccineRecord(Vaccine vaccine){
        return dataApi.saveVaccineRecord(vaccine);
    }

    public Call<List<Vaccine>> getVaccineByPetId(String petId){
        return dataApi.getVaccineByPetId(petId);
    }

    public Call<Void> deleteVaccine(String vaccineId){
        return dataApi.deleteVaccine(vaccineId);
    }

    public Call<Void> updateVaccine(String vaccineId,  Vaccine vaccine){
        return dataApi.updateVaccineById(vaccineId, vaccine);
    }

}
