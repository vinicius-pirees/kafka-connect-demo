package com.acme.connect.source.dummy.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class UserResponse {
    @SerializedName(value = "users")
    private List<UserSchema> userRecords;


    public UserResponse(List<UserSchema> UserRecords) {
        userRecords = UserRecords;
    }

    public List<UserSchema> getUserRecords() {
        return userRecords;
    }

}
