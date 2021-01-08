package com.dmytro.kuchura.kyiv.boryspil.express.models;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class Schedule {

    @SerializedName("number")
    private int number;

    @SerializedName("departureTrafficHub")
    private TrafficHub departureTrafficHub;

    @SerializedName("arrivalTrafficHub")
    private TrafficHub arrivalTrafficHub;

    @SerializedName("segments")
    private ArrayList<Segment> segments;

    @SerializedName("time")
    private String time;

    @SerializedName("departureTime")
    private String departureTime;

    @SerializedName("arrivalTime")
    private String arrivalTime;

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public TrafficHub getDepartureTrafficHub() {
        return departureTrafficHub;
    }

    public void setDepartureTrafficHub(TrafficHub departureTrafficHub) {
        this.departureTrafficHub = departureTrafficHub;
    }

    public TrafficHub getArrivalTrafficHub() {
        return arrivalTrafficHub;
    }

    public void setArrivalTrafficHub(TrafficHub arrivalTrafficHub) {
        this.arrivalTrafficHub = arrivalTrafficHub;
    }

    public ArrayList<Segment> getSegments() {
        return segments;
    }

    public void setSegments(ArrayList<Segment> segments) {
        this.segments = segments;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getDepartureTime() {
        return departureTime;
    }

    public void setDepartureTime(String departureTime) {
        this.departureTime = departureTime;
    }

    public String getArrivalTime() {
        return arrivalTime;
    }

    public void setArrivalTime(String arrivalTime) {
        this.arrivalTime = arrivalTime;
    }
}