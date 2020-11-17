package com.dmytro.kuchura.kyiv.boryspil.express;

import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
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
import com.facebook.shimmer.ShimmerFrameLayout;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ScheduleActivity extends AppCompatActivity {
    private ShimmerFrameLayout shimmerContainer;
    private List<Schedule> scheduleList;
    private RequestQueue requestQueue;
    private long backPressedTime;
    private Toast backToast;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule);

        shimmerContainer = findViewById(R.id.shimmer_view_container);

        RecyclerView recyclerView = findViewById(R.id.scheduleRecyclerView);

        scheduleList = new ArrayList<>();

        requestQueue = Volley.newRequestQueue(this);

        scheduleList = getTrains();

        final ScheduleAdapter scheduleAdapter = new ScheduleAdapter(ScheduleActivity.this, scheduleList);
        recyclerView.setAdapter(scheduleAdapter);
    }


    private List<Schedule> getTrains() {
        String url = "http://138.197.186.137:8080/api/trains";

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONObject dataResponse = response.getJSONObject("data");
                    JSONArray trainsResponse = dataResponse.getJSONArray("trains");

                    for (int i = 0; i < trainsResponse.length(); i++) {
                        JSONObject jsonObject = trainsResponse.getJSONObject(i);

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
                        String trainArrivalTime = jsonObject.getString("arrivalTime");

                        TrafficHub departureTrafficHub = new TrafficHub();
                        departureTrafficHub.setName(departureName);
                        departureTrafficHub.setFullName(departureFullName);

                        TrafficHub arrivalTrafficHub = new TrafficHub();
                        arrivalTrafficHub.setName(arrivalName);
                        arrivalTrafficHub.setFullName(arrivalFullName);

                        SimpleDateFormat format = new SimpleDateFormat("HH:mm");
                        Date now = new Date();

                        now.setHours(1);
                        now.setMinutes(7);
                        String departureTime = format.format(now);

                        now.setHours(1);
                        now.setMinutes(45);
                        String arrivalTime = format.format(now);

                        String diff = getDiffTime(departureTime, arrivalTime);

                        schedule.setNumber(number);
                        schedule.setDepartureTime(departureTime);
                        schedule.setDepartureTrafficHub(arrivalTrafficHub);
                        schedule.setArrivalTime(arrivalTime);
                        schedule.setArrivalTrafficHub(departureTrafficHub);
                        schedule.setTime(diff);

                        scheduleList.add(schedule);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });

        requestQueue.add(request);

        return scheduleList;
    }

    public String getDiffTime(String departureTime, String arrivalTime) {
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
        shimmerContainer.startShimmer();
    }

    @Override
    public void onPause() {
        shimmerContainer.stopShimmer();
        super.onPause();
    }
}