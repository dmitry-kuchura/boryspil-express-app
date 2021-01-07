package com.dmytro.kuchura.kyiv.boryspil.express.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.dmytro.kuchura.kyiv.boryspil.express.R;
import com.dmytro.kuchura.kyiv.boryspil.express.models.Schedule;

import java.util.List;

public class ScheduleAdapter extends RecyclerView.Adapter<ScheduleAdapter.ScheduleViewHolder> {

    private List<Schedule> schedules;

    public ScheduleAdapter(Context context, List<Schedule> schedules) {
        this.schedules = schedules;
    }

    @NonNull
    @Override
    public ScheduleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ScheduleViewHolder(
                LayoutInflater.from(parent.getContext()).inflate(R.layout.schedule_list_item, parent, false)
        );
    }

    @Override
    public void onBindViewHolder(@NonNull ScheduleViewHolder holder, int position) {
        holder.bindSchedule(schedules.get(position));
    }

    @Override
    public int getItemCount() {
        return schedules.size();
    }

    static class ScheduleViewHolder extends RecyclerView.ViewHolder {

        ConstraintLayout layoutSchedule;
        TextView number;
        TextView tripDate;
        TextView departureTrafficHub;
        TextView departureTrafficHubName;
        TextView textTripTime;
        TextView arrivalTrafficHub;
        TextView arrivalTrafficHubName;

        ScheduleViewHolder(@NonNull View itemView) {
            super(itemView);

            layoutSchedule = itemView.findViewById(R.id.scheduleRecyclerView);
            number = itemView.findViewById(R.id.number);
            tripDate = itemView.findViewById(R.id.tripDate);
            departureTrafficHub = itemView.findViewById(R.id.departureTrafficHub);
            departureTrafficHubName = itemView.findViewById(R.id.departureTrafficHubName);
            textTripTime = itemView.findViewById(R.id.textTripTime);
            arrivalTrafficHub = itemView.findViewById(R.id.arrivalTrafficHub);
            arrivalTrafficHubName = itemView.findViewById(R.id.arrivalTrafficHubName);
        }

        void bindSchedule(final Schedule schedule) {
            number.setText("№" + schedule.getNumber());
            tripDate.setText(schedule.getDepartureTime());
            departureTrafficHub.setText(schedule.getDepartureTrafficHub().getName());
            departureTrafficHubName.setText(schedule.getDepartureTrafficHub().getFullName());
            arrivalTrafficHub.setText(schedule.getArrivalTrafficHub().getName());
            arrivalTrafficHubName.setText(schedule.getArrivalTrafficHub().getFullName());
            textTripTime.setText(schedule.getTime());
        }
    }
}
