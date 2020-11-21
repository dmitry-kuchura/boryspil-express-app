package com.dmytro.kuchura.kyiv.boryspil.express.models;

import com.google.gson.annotations.SerializedName;

public class Segment {
    
    @SerializedName("departureTime")
    private String departureTime;

    @SerializedName("trafficHub")
    private TrafficHub trafficHub;

    public String getDepartureTime() {
        return departureTime;
    }

    public void setDepartureTime(String departureTime) {
        this.departureTime = departureTime;
    }

    public TrafficHub getTrafficHub() {
        return trafficHub;
    }

    public void setTrafficHub(TrafficHub trafficHub) {
        this.trafficHub = trafficHub;
    }
}
