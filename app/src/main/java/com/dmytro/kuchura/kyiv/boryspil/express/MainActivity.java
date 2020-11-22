package com.dmytro.kuchura.kyiv.boryspil.express;

import android.content.Intent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.dmytro.kuchura.kyiv.boryspil.express.adapters.ScheduleAdapter;
import com.dmytro.kuchura.kyiv.boryspil.express.models.Schedule;
import com.dmytro.kuchura.kyiv.boryspil.express.models.TrafficHub;
import com.dmytro.kuchura.kyiv.boryspil.express.utils.TimeDiff;
import com.facebook.shimmer.ShimmerFrameLayout;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.dmytro.kuchura.kyiv.boryspil.express.utils.Api.Url.API_CURRENT_TRAINS_LIST;

public class MainActivity extends AppCompatActivity {
    private long backPressedTime;
    private Toast backToast;

    private List<Schedule> scheduleList;
    private ScheduleAdapter scheduleAdapter;

    private ShimmerFrameLayout shimmerFrameLayout;
    private RequestQueue requestQueue;

    private static final String URL = API_CURRENT_TRAINS_LIST;

    AutoCompleteTextView autoCompleteTextView;
    final String[] stations = {"Аэропорт", "Дарница", "Выдубичи", "Ж/Д Вокзал"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        autoCompleteTextView = findViewById(R.id.autoCompleteTextView);
        autoCompleteTextView.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, stations));

        requestQueue = Volley.newRequestQueue(this);
        RecyclerView recyclerView = findViewById(R.id.mainRecyclerView);
        shimmerFrameLayout = findViewById(R.id.shimmerMainContainer);

        scheduleList = new ArrayList<>();
        scheduleAdapter = new ScheduleAdapter(this, scheduleList);
        RecyclerView.LayoutManager recyclerViewLayoutManager = new LinearLayoutManager(getApplicationContext());

        recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        recyclerView.setLayoutManager(recyclerViewLayoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(scheduleAdapter);

        getTrains();
    }

    private void getTrains() {
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, URL, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                List<Schedule> scheduleItems = new ArrayList<>();

                try {
                    JSONArray dataResponse = response.getJSONArray("data");

                    for (int i = 0; i < dataResponse.length(); i++) {
                        JSONObject jsonObject = dataResponse.getJSONObject(i);

                        Schedule schedule = new Schedule();

                        int number = jsonObject.getInt("number");

                        JSONObject departure = jsonObject.getJSONObject("departureTrafficHub");
                        String departureName = departure.getString("name");
                        String departureFullName = departure.getString("fullName");

                        JSONObject arrival = jsonObject.getJSONObject("arrivalTrafficHub");
                        String arrivalName = arrival.getString("name");
                        String arrivalFullName = arrival.getString("fullName");

                        JSONArray segments = jsonObject.getJSONArray("segments");

                        String trainDepartureTime = jsonObject.getString("departureTime");
                        String[] trainDepartureTimes = trainDepartureTime.split(":");

                        String trainArrivalTime = jsonObject.getString("arrivalTime");
                        String[] trainArrivalTimes = trainArrivalTime.split(":");

                        TrafficHub departureTrafficHub = new TrafficHub();
                        departureTrafficHub.setName(departureName);
                        departureTrafficHub.setFullName(departureFullName);

                        TrafficHub arrivalTrafficHub = new TrafficHub();
                        arrivalTrafficHub.setName(arrivalName);
                        arrivalTrafficHub.setFullName(arrivalFullName);

                        SimpleDateFormat format = new SimpleDateFormat("HH:mm");
                        Date now = new Date();

                        now.setHours(Integer.parseInt(trainDepartureTimes[0]));
                        now.setMinutes(Integer.parseInt(trainDepartureTimes[1]));
                        String departureTime = format.format(now);

                        now.setHours(Integer.parseInt(trainArrivalTimes[0]));
                        now.setMinutes(Integer.parseInt(trainArrivalTimes[1]));
                        String arrivalTime = format.format(now);

                        String diff = TimeDiff.getDiffTime(departureTime, arrivalTime);

                        schedule.setNumber(number);
                        schedule.setDepartureTime(departureTime);
                        schedule.setDepartureTrafficHub(departureTrafficHub);
                        schedule.setArrivalTime(arrivalTime);
                        schedule.setArrivalTrafficHub(arrivalTrafficHub);
                        schedule.setTime(diff);

                        scheduleItems.add(schedule);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                scheduleList.clear();
                scheduleList.addAll(scheduleItems);

                scheduleAdapter.notifyDataSetChanged();

                shimmerFrameLayout.stopShimmer();
                shimmerFrameLayout.setVisibility(View.GONE);

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MainActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        requestQueue.add(request);
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