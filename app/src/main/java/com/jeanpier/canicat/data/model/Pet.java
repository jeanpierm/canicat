package com.jeanpier.canicat.data.model;


import androidx.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

public class Pet {
    @SerializedName("id")
    private String id;
    @SerializedName("name")
    private String name;
    @SerializedName("species")
    private String species;
    @SerializedName("breed")
    private String breed;
    @SerializedName("sexo")
    private String sexo;
    @SerializedName("picture")
    private String picture;
    @SerializedName("userId")
    private String userId;

    public Pet() {
    }

    public Pet(String name, String species, String breed, String sexo, String picture, String userId) {
        this.name = name;
        this.species = species;
        this.breed = breed;
        this.sexo = sexo;
        this.picture = picture;
        this.userId = userId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSpecies() {
        return species;
    }

    public void setSpecies(String species) {
        this.species = species;
    }

    public String getBreed() {
        return breed;
    }

    public void setBreed(String breed) {
        this.breed = breed;
    }

    public String getSexo() {
        return sexo;
    }

    public void setSexo(String sexo) {
        this.sexo = sexo;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    @Override
    public String toString() {
        return "Pet{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", species='" + species + '\'' +
                ", breed='" + breed + '\'' +
                ", sexo='" + sexo + '\'' +
                ", picture='" + picture + '\'' +
                ", userId='" + userId + '\'' +
                '}';
    }
}
