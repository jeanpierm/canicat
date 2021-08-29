package com.jeanpier.canicat.data.model;

import com.google.gson.annotations.SerializedName;

public class VaccineRecord {
    @SerializedName("id")
    private String id;
    @SerializedName("name")
    private String name;
    @SerializedName("type")
    private String type;
    @SerializedName("lastVaccineDate")
    private String lastVaccineDate;
    @SerializedName("nextVaccineDate")
    private String nextVaccineDate;
    @SerializedName("description")
    private String description;

    public VaccineRecord(){

    }

    public VaccineRecord(String id, String name, String type, String lastVaccineDate, String nextVaccineDate, String description) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.lastVaccineDate = lastVaccineDate;
        this.nextVaccineDate = nextVaccineDate;
        this.description = description;
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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getLastVaccineDate() {
        return lastVaccineDate;
    }

    public void setLastVaccineDate(String lastVaccineDate) {
        this.lastVaccineDate = lastVaccineDate;
    }

    public String getNextVaccineDate() {
        return nextVaccineDate;
    }

    public void setNextVaccineDate(String nextVaccineDate) {
        this.nextVaccineDate = nextVaccineDate;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return "VaccineRecord{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", type='" + type + '\'' +
                ", lastVaccineDate='" + lastVaccineDate + '\'' +
                ", nextVaccineDate='" + nextVaccineDate + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}