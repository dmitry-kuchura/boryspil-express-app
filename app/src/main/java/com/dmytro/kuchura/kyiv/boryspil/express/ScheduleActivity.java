package com.dmytro.kuchura.kyiv.boryspil.express;

import android.view.View;
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
import com.google.android.material.tabs.TabLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.dmytro.kuchura.kyiv.boryspil.express.utils.Api.Url.API_TRAINS_LIST;

public class ScheduleActivity extends AppCompatActivity {
    private List<Schedule> scheduleList;
    private ScheduleAdapter scheduleAdapter;

    private ShimmerFrameLayout shimmerFrameLayout;
    private RequestQueue requestQueue;
    private TabLayout tabLayout;

    private long backPressedTime;
    private Toast backToast;

    private boolean showOutbound;

    private static final String URL = API_TRAINS_LIST;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule);

        showOutbound = true;

        tabLayout = findViewById(R.id.scheduleTabs);

        requestQueue = Volley.newRequestQueue(this);
        RecyclerView recyclerView = findViewById(R.id.scheduleRecyclerView);
        shimmerFrameLayout = findViewById(R.id.shimmerViewContainer);

        scheduleList = new ArrayList<>();
        scheduleAdapter = new ScheduleAdapter(this, scheduleList);
        RecyclerView.LayoutManager recyclerViewLayoutManager = new LinearLayoutManager(getApplicationContext());

        recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        recyclerView.setLayoutManager(recyclerViewLayoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(scheduleAdapter);

        getTrains();

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                showOutbound = tabLayout.getSelectedTabPosition() != 1;
                getTrains();
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                showOutbound = tabLayout.getSelectedTabPosition() != 1;
                getTrains();
            }
        });
    }

    private void getTrains() {
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, URL, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                List<Schedule> scheduleItems = new ArrayList<>();

                try {
                    JSONObject dataResponse = response.getJSONObject("data");
                    JSONArray trainsResponse = dataResponse.getJSONArray("trains");

                    for (int i = 0; i < trainsResponse.length(); i++) {
                        JSONObject jsonObject = trainsResponse.getJSONObject(i);

                        Schedule schedule = new Schedule();

                        int number = jsonObject.getInt("number");

                        boolean isOutbound = jsonObject.getBoolean("isOutbound");

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

                        if (showOutbound) {
                            if (isOutbound) {
                                scheduleItems.add(schedule);
                            }
                        } else {
                            if (!isOutbound) {
                                scheduleItems.add(schedule);
                            }
                        }
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
                Toast.makeText(ScheduleActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        requestQueue.add(request);
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

    @Override
    public void onResume() {
        super.onResume();
        shimmerFrameLayout.startShimmer();
    }

    @Override
    public void onPause() {
        shimmerFrameLayout.stopShimmer();
        super.onPause();
    }
}