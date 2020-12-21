package com.dmytro.kuchura.kyiv.boryspil.express.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.dmytro.kuchura.kyiv.boryspil.express.R;
import com.dmytro.kuchura.kyiv.boryspil.express.adapters.ScheduleAdapter;
import com.dmytro.kuchura.kyiv.boryspil.express.models.Schedule;
import com.dmytro.kuchura.kyiv.boryspil.express.models.TrafficHub;
import com.dmytro.kuchura.kyiv.boryspil.express.utils.TimeDiff;
import com.facebook.shimmer.ShimmerFrameLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.dmytro.kuchura.kyiv.boryspil.express.utils.Api.Url.API_CURRENT_TRAINS_LIST;

/**
 * A simple {@link Fragment} subclass.
 * create an instance of this fragment.
 */
public class MainFragment extends Fragment {

    private List<Schedule> scheduleList;
    private ScheduleAdapter scheduleAdapter;

    private ShimmerFrameLayout shimmerFrameLayout;
    private RequestQueue requestQueue;

    private static final String URL = API_CURRENT_TRAINS_LIST;

    AutoCompleteTextView autoCompleteTextView;
    final String[] stations = {"Аэропорт", "Дарница", "Выдубичи", "Ж/Д Вокзал"};

    public MainFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);

        requestQueue = Volley.newRequestQueue(getActivity().getApplicationContext());
        RecyclerView recyclerView = view.findViewById(R.id.mainRecyclerView);
        shimmerFrameLayout = view.findViewById(R.id.shimmerMainContainer);

        scheduleList = new ArrayList<>();
        scheduleAdapter = new ScheduleAdapter(getActivity().getApplicationContext(), scheduleList);
        RecyclerView.LayoutManager recyclerViewLayoutManager = new LinearLayoutManager(getActivity().getApplicationContext());

        recyclerView.addItemDecoration(new DividerItemDecoration(getActivity().getApplicationContext(), DividerItemDecoration.VERTICAL));
        recyclerView.setLayoutManager(recyclerViewLayoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(scheduleAdapter);

        getTrains();

//        TextView allText = (TextView) view.findViewById(R.id.textAll);
//
//        // создаем обработчик нажатия
//        View.OnClickListener onClickAllText = new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                // Меняем текст в TextView (tvOut)
//            }
//        };
//
//        allText.setOnClickListener(onClickAllText);

        return view;
    }

    public void showFullSchedule() {
//        Intent fullSchedule = new Intent(MainActivity.this, ScheduleActivity.class);
//        startActivity(fullSchedule);
//
//        finish();
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
                Toast.makeText(getActivity().getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        requestQueue.add(request);
    }
}