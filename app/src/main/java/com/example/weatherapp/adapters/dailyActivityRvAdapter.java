package com.example.weatherapp.adapters;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.weatherapp.MainActivity;
import com.example.weatherapp.R;
import com.example.weatherapp.models.Days;
import com.example.weatherapp.viewHolders.DailyActivityViewHolder;

import java.util.ArrayList;

public class dailyActivityRvAdapter extends RecyclerView.Adapter<DailyActivityViewHolder> {

    public ArrayList<Days> daysArrayList;

    public dailyActivityRvAdapter(ArrayList<Days> daysArrayList) {
        this.daysArrayList = daysArrayList;
    }

    @NonNull
    @Override
    public DailyActivityViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new DailyActivityViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_daily, parent, false));
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull DailyActivityViewHolder holder, int position) {
        holder.date_daily.setText(daysArrayList.get(position).getDate());
        holder.temperature_daily.setText(daysArrayList.get(position).getTemp_min()+ MainActivity.unit_value+"/"+daysArrayList.get(position).getTemp_max()+MainActivity.unit_value);
        holder.icon_daily.setImageResource(daysArrayList.get(position).getIcon_id());
        holder.description_daily.setText(daysArrayList.get(position).getDescription());
        holder.precip_daily.setText("("+daysArrayList.get(position).getPrecip()+"% precip.)");
        holder.uv_daily.setText("UV Index: "+daysArrayList.get(position).getUv());
        holder.morning_daily.setText(daysArrayList.get(position).getMorning()+MainActivity.unit_value);
        holder.noon_daily.setText(daysArrayList.get(position).getAfternoon()+MainActivity.unit_value);
        holder.evening_daily.setText(daysArrayList.get(position).getEvening()+MainActivity.unit_value);
        holder.night_daily.setText(daysArrayList.get(position).getNight()+MainActivity.unit_value);
    }

    @Override
    public int getItemCount() {
        return daysArrayList.size();
    }
}
