package com.jeanpier.canicat.data.network.responses;

import androidx.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

public class PostPetResponse {
    @SerializedName("id")
    private String id;

    public PostPetResponse(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @NonNull
    @Override
    public String toString() {
        return "PostPetResponse{" +
                "id='" + id + '\'' +
                '}';
    }
}
