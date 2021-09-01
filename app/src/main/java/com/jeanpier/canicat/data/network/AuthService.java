package com.jeanpier.canicat.data.network;

import com.jeanpier.canicat.core.RetrofitHelper;
import com.jeanpier.canicat.data.network.responses.LoginResponse;

import retrofit2.Call;

public class AuthService {
    private final DataApi dataApi = RetrofitHelper.getHttpClient().create(DataApi.class);

    public Call<LoginResponse> login(String email, String password) {
        return dataApi.login(email, password);
    }
}
