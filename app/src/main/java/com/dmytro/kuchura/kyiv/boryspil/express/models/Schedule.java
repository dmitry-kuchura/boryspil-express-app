package com.dmytro.kuchura.kyiv.boryspil.express.models;

import java.util.Date;

public class Schedule {
    private String number;
    private TrafficHub departureTrafficHub;
    private TrafficHub arrivalTrafficHub;
    private String time;
    private Date departureTime;
    private Date arrivalTime;

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
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

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public Date getDepartureTime() {
        return departureTime;
    }

    public void setDepartureTime(Date departureTime) {
        this.departureTime = departureTime;
    }

    public Date getArrivalTime() {
        return arrivalTime;
    }

    public void setArrivalTime(Date arrivalTime) {
        this.arrivalTime = arrivalTime;
    }
}