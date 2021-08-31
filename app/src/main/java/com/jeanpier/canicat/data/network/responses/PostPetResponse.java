package com.jeanpier.canicat.data.network.responses;

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

    @Override
    public String toString() {
        return "SuccessPostPet{" +
                "id='" + id + '\'' +
                '}';
    }
}
