package com.dmytro.kuchura.kyiv.boryspil.express.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.dmytro.kuchura.kyiv.boryspil.express.R;
import com.dmytro.kuchura.kyiv.boryspil.express.adapters.ScheduleInfoAdapter;
import com.dmytro.kuchura.kyiv.boryspil.express.models.Schedule;
import com.dmytro.kuchura.kyiv.boryspil.express.models.TrafficHub;
import com.dmytro.kuchura.kyiv.boryspil.express.utils.TimeDiff;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.dmytro.kuchura.kyiv.boryspil.express.utils.Api.Url.API_TRAIN_INFO;

/**
 * A simple {@link Fragment} subclass.
 * create an instance of this fragment.
 */
public class TripFragment extends Fragment {

    private static final String URL = API_TRAIN_INFO;

    private Schedule scheduleInfo;
    private ScheduleInfoAdapter scheduleAdapter;

    private RequestQueue requestQueue;

    public TripFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);

        getTrainInfo();

        return view;
    }

    private void setInfoToTemplate(Schedule schedule) {


    }

    private void getTrainInfo() {
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, URL, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Schedule schedule = new Schedule();

                try {
                    JSONObject jsonObject = response.getJSONObject("train");

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
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                setInfoToTemplate(schedule);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getActivity().getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        requestQueue.add(request);
    }
}