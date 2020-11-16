package com.dmytro.kuchura.kyiv.boryspil.express.utils;

import android.content.Context;
import androidx.annotation.NonNull;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

public class Data {

    public static String getJsonFromAssets(@NonNull Context context, String fileName) {
        String jsonString;

        try {
            InputStream is = context.getAssets().open(fileName);

            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();

            jsonString = new String(buffer, StandardCharsets.UTF_8);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

        return jsonString;
    }
}