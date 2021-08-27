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
    @SerializedName("race")
    private String race;
    @SerializedName("sexo")
    private String sexo;
    @SerializedName("color")
    private String color;
    @SerializedName("picture")
    private String picture;

    public Pet() {
    }

    public Pet(String id, String name, String species, String race, String sexo, String color, String picture) {
        this.id = id;
        this.name = name;
        this.species = species;
        this.race = race;
        this.sexo = sexo;
        this.color = color;
        this.picture = picture;
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

    public String getRace() {
        return race;
    }

    public void setRace(String race) {
        this.race = race;
    }

    public String getSexo() {
        return sexo;
    }

    public void setSexo(String sexo) {
        this.sexo = sexo;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    @NonNull
    @Override
    public String toString() {
        return "Pet{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", species='" + species + '\'' +
                ", race='" + race + '\'' +
                ", sexo='" + sexo + '\'' +
                ", color='" + color + '\'' +
                ", picture='" + picture + '\'' +
                '}';
    }
}
