package com.dmytro.kuchura.kyiv.boryspil.express.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.dmytro.kuchura.kyiv.boryspil.express.MainActivity;
import com.dmytro.kuchura.kyiv.boryspil.express.R;
import com.dmytro.kuchura.kyiv.boryspil.express.adapters.ScheduleAdapter;
import com.dmytro.kuchura.kyiv.boryspil.express.listeners.RecyclerItemClickListener;
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

/**
 * A simple {@link Fragment} subclass.
 * create an instance of this fragment.
 */
public class ScheduleFragment extends Fragment {

    private static List<Schedule> scheduleList;
    private ScheduleAdapter scheduleAdapter;
    private static RecyclerView recyclerView;

    private ShimmerFrameLayout shimmerFrameLayout;
    private RequestQueue requestQueue;
    private TabLayout tabLayout;

    private boolean showOutbound;

    private static final String URL = API_TRAINS_LIST;

    public ScheduleFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_schedule, container, false);

        showOutbound = true;

        tabLayout = view.findViewById(R.id.scheduleTabs);

        requestQueue = Volley.newRequestQueue(getActivity().getApplicationContext());
        recyclerView = view.findViewById(R.id.scheduleRecyclerView);
        shimmerFrameLayout = view.findViewById(R.id.shimmerViewContainer);

        scheduleList = new ArrayList<>();
        scheduleAdapter = new ScheduleAdapter(getActivity().getApplicationContext(), scheduleList);
        RecyclerView.LayoutManager recyclerViewLayoutManager = new LinearLayoutManager(getActivity().getApplicationContext());

        recyclerView.addItemDecoration(new DividerItemDecoration(getActivity().getApplicationContext(), DividerItemDecoration.VERTICAL));
        recyclerView.setLayoutManager(recyclerViewLayoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(scheduleAdapter);

        getTrains();

        recyclerView.addOnItemTouchListener(new RecyclerItemClickListener(getActivity().getApplicationContext(), recyclerView, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Schedule schedule = scheduleList.get(position);

                getTrainInfo(schedule.getNumber());
            }

            @Override
            public void onItemLongClick(View view, int position) {
                Log.d("TAG", "onItemLongClick");
            }
        }));

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

        return view;
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
                Toast.makeText(getActivity().getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        requestQueue.add(request);
    }

    private void getTrainInfo(int number) {
        ((MainActivity)getActivity()).changeFragmentToTripInfo(number);
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