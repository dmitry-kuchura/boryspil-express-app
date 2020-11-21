package com.dmytro.kuchura.kyiv.boryspil.express.models;

import com.google.gson.annotations.SerializedName;

public class TrafficHub {

    @SerializedName("code")
    private int code;

    @SerializedName("name")
    private String name;

    @SerializedName("fullName")
    private String fullName;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }
}
