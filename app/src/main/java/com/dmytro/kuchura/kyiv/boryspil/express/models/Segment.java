package com.dmytro.kuchura.kyiv.boryspil.express.models;

public class Segment {
    private String departureTime;
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
