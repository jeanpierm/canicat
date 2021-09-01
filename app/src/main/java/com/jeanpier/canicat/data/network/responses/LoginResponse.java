package com.jeanpier.canicat.data.network.responses;

import androidx.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

public class LoginResponse {
    @SerializedName("uid")
    private String uid;
    @SerializedName("firstname")
    private String firstname;
    @SerializedName("lastname")
    private String lastname;

    public LoginResponse() {
    }

    public LoginResponse(String uid, String firstname, String lastname) {
        this.uid = uid;
        this.firstname = firstname;
        this.lastname = lastname;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    @NonNull
    @Override
    public String toString() {
        return "LoginResponse{" +
                "uid='" + uid + '\'' +
                ", firstname='" + firstname + '\'' +
                ", lastname='" + lastname + '\'' +
                '}';
    }
}
