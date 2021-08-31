package com.jeanpier.canicat.data.network;

import com.jeanpier.canicat.core.RetrofitHelper;
import com.jeanpier.canicat.data.model.User;
import com.jeanpier.canicat.data.network.responses.PostUserResponse;

import retrofit2.Call;

public class UserService {

    private final DataApi dataApi = RetrofitHelper.getHttpClient().create(DataApi.class);

    public Call<PostUserResponse> create(User user) {
        return dataApi.createUser(user);
    }
}
