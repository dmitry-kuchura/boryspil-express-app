package com.dmytro.kuchura.kyiv.boryspil.express;

import android.content.Intent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {
    private long backPressedTime;
    private Toast backToast;

    AutoCompleteTextView autoCompleteTextView;
    final String[] stations = {"Аэропорт", "Дарница", "Выдубичи", "Ж/Д Вокзал"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        autoCompleteTextView = findViewById(R.id.autoCompleteTextView);
        autoCompleteTextView.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, stations));
    }

    public void showFullSchedule(View view) {
        Intent fullSchedule = new Intent(MainActivity.this, ScheduleActivity.class);
        startActivity(fullSchedule);

        finish();
    }

    @Override
    public void onBackPressed() {
        if (backPressedTime + 2000 > System.currentTimeMillis()) {
            backToast.cancel();
            super.onBackPressed();
        } else {
            backToast = Toast.makeText(getBaseContext(), R.string.press_again_to_exit, Toast.LENGTH_SHORT);
            backToast.show();
        }

        backPressedTime = System.currentTimeMillis();
    }
}