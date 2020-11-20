package com.dmytro.kuchura.kyiv.boryspil.express.utils;

import android.annotation.SuppressLint;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Diff {

    public static String getDiffTime(String departureTime, String arrivalTime) {
        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat format = new SimpleDateFormat("HH:mm");

        Date from = null;
        Date to = null;

        try {
            from = format.parse(departureTime);
            to = format.parse(arrivalTime);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        assert to != null;
        assert from != null;

        long diff = to.getTime() - from.getTime();

        long diffMinutes = diff / (60 * 1000);
        long diffHours = diff / (60 * 60 * 1000);

        return diffHours + ":" + diffMinutes + "";
    }
}
