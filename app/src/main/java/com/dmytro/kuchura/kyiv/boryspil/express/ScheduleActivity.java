package com.dmytro.kuchura.kyiv.boryspil.express;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.RecyclerView;
import com.dmytro.kuchura.kyiv.boryspil.express.adapters.ScheduleAdapter;
import com.dmytro.kuchura.kyiv.boryspil.express.models.Schedule;
import com.dmytro.kuchura.kyiv.boryspil.express.models.TrafficHub;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ScheduleActivity extends AppCompatActivity implements ScheduleListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule);

        RecyclerView recyclerView = findViewById(R.id.scheduleRecyclerView);

        List<Schedule> scheduleList = new ArrayList<>();

        for (int i = 0; i < 25; i++) {
            Schedule schedule = new Schedule();

            TrafficHub departureTrafficHub = new TrafficHub();
            departureTrafficHub.setId(1 + i);
            departureTrafficHub.setName("Борисполь");
            departureTrafficHub.setFullName("Международный аэропорт");

            TrafficHub arrivalTrafficHub = new TrafficHub();
            arrivalTrafficHub.setId(2 + i);
            arrivalTrafficHub.setName("Ж/Д Вокзал");
            arrivalTrafficHub.setFullName("Центральный ж/д вокзал");

            schedule.setNumber("80" + i);
            schedule.setDepartureTime(new Date());
            schedule.setDepartureTrafficHub(arrivalTrafficHub);
            schedule.setArrivalTime(new Date());
            schedule.setArrivalTrafficHub(departureTrafficHub);

            scheduleList.add(schedule);
        }

        final ScheduleAdapter scheduleAdapter = new ScheduleAdapter(scheduleList, this);
        recyclerView.setAdapter(scheduleAdapter);
    }

    @Override
    public void onScheduleAction(Boolean isSelected) {

    }
}