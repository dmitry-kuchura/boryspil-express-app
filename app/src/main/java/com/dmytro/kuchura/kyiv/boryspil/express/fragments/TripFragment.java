package com.dmytro.kuchura.kyiv.boryspil.express.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.dmytro.kuchura.kyiv.boryspil.express.MainActivity;
import com.dmytro.kuchura.kyiv.boryspil.express.R;
import com.dmytro.kuchura.kyiv.boryspil.express.models.Schedule;
import com.dmytro.kuchura.kyiv.boryspil.express.models.Segment;
import com.dmytro.kuchura.kyiv.boryspil.express.models.TrafficHub;
import com.dmytro.kuchura.kyiv.boryspil.express.utils.TimeDiff;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import static com.dmytro.kuchura.kyiv.boryspil.express.utils.Api.Url.API_TRAIN_INFO;

/**
 * A simple {@link Fragment} subclass.
 * create an instance of this fragment.
 */
public class TripFragment extends Fragment {

    private static final String URL = API_TRAIN_INFO;

    private String trainNumber = "1";

    private RequestQueue requestQueue;

    private TextView textNumber;
    private TextView textStartTrafficHub;
    private TextView textDescriptionStartTrafficHub;
    private TextView textFirstStopTrafficHub;
    private TextView textFirstDescriptionStopTrafficHub;
    private TextView textSecondStopTrafficHub;
    private TextView textSecondDescriptionStopTrafficHub;
    private TextView textEndTrafficHub;
    private TextView textDescriptionEndTrafficHub;

    public TripFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_trip, container, false);

        requestQueue = Volley.newRequestQueue(getActivity().getApplicationContext());

        textNumber = view.findViewById(R.id.textNumber);
        textStartTrafficHub = view.findViewById(R.id.textStartTrafficHub);
        textDescriptionStartTrafficHub = view.findViewById(R.id.textDescriptionStartTrafficHub);

        textFirstStopTrafficHub = view.findViewById(R.id.textFirstStopTrafficHub);
        textFirstDescriptionStopTrafficHub = view.findViewById(R.id.textDescriptionFirstStopTrafficHub);

        textSecondStopTrafficHub = view.findViewById(R.id.textSecondStopTrafficHub);
        textSecondDescriptionStopTrafficHub = view.findViewById(R.id.textDescriptionSecondStopTrafficHub);

        textEndTrafficHub = view.findViewById(R.id.textEndTrafficHub);
        textDescriptionEndTrafficHub = view.findViewById(R.id.textDescriptionEndTrafficHub);

        ImageView goBack = view.findViewById(R.id.goBack);
        goBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity) getActivity()).changeMenu(R.id.menu_schedule);
                ((MainActivity) getActivity()).changeFragmentToSchedule();
            }
        });

        return view;
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);

        if (!hidden) {
            getTrainNumber();
            getTrainInfo();
        }
    }

    private void getTrainInfo() {
        if (trainNumber != "1") {
            JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, URL + trainNumber, null, new Response.Listener<JSONObject>() {
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

                        if (segments.length() > 0) {
                            ArrayList<Segment> segmentsList = new ArrayList<Segment>();

                            for (int i = 0; i < segments.length(); i++) {
                                JSONObject segmentsObject = segments.getJSONObject(i);

                                Segment segment = new Segment();
                                TrafficHub trafficHub = new TrafficHub();

                                String arrivalTime = segmentsObject.getString("arrivalTime");
                                String departureTime = segmentsObject.getString("departureTime");

                                JSONObject trafficHubObject = segmentsObject.getJSONObject("trafficHub");
                                String trafficHubName = trafficHubObject.getString("name");
                                String trafficHubFullName = trafficHubObject.getString("fullName");

                                trafficHub.setName(trafficHubName);
                                trafficHub.setFullName(trafficHubFullName);

                                segment.setArrivalTime(arrivalTime);
                                segment.setDepartureTime(departureTime);
                                segment.setTrafficHub(trafficHub);

                                segmentsList.add(segment);
                            }

                            schedule.setSegments(segmentsList);
                        }

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

    private void setInfoToTemplate(Schedule schedule) {
        String number = String.valueOf(schedule.getNumber());

        String startTrafficHub = schedule.getSegments().get(0).getTrafficHub().getName();
        String descriptionStartTrafficHub = schedule.getSegments().get(0).getTrafficHub().getFullName();

        String firstStopTrafficHub = schedule.getSegments().get(1).getTrafficHub().getName();
        String firstDescriptionStopTrafficHub = schedule.getSegments().get(1).getTrafficHub().getFullName();

        String secondStopTrafficHub = schedule.getSegments().get(2).getTrafficHub().getName();
        String secondDescriptionStopTrafficHub = schedule.getSegments().get(2).getTrafficHub().getFullName();

        String endTrafficHub = schedule.getSegments().get(3).getTrafficHub().getName();
        String descriptionEndTrafficHub = schedule.getSegments().get(3).getTrafficHub().getFullName();

        textNumber.setText(number);

        textStartTrafficHub.setText(startTrafficHub);
        textDescriptionStartTrafficHub.setText(descriptionStartTrafficHub);

        textFirstStopTrafficHub.setText(firstStopTrafficHub);
        textFirstDescriptionStopTrafficHub.setText(firstDescriptionStopTrafficHub);

        textSecondStopTrafficHub.setText(secondStopTrafficHub);
        textSecondDescriptionStopTrafficHub.setText(secondDescriptionStopTrafficHub);

        textEndTrafficHub.setText(endTrafficHub);
        textDescriptionEndTrafficHub.setText(descriptionEndTrafficHub);
    }

    private void getTrainNumber() {
        MainActivity activity = (MainActivity) getActivity();
        trainNumber = activity.getTrainNumber();
    }
}