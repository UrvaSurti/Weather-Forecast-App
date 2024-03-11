package com.example.weatherapp.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.weatherapp.MainActivity;
import com.example.weatherapp.R;
import com.example.weatherapp.models.Hours;
import com.example.weatherapp.viewHolders.MainActivityViewHolder;

import java.util.ArrayList;

public class mainActivityRvAdapter extends RecyclerView.Adapter<MainActivityViewHolder>{

    public ArrayList<Hours> hoursArrayList;

    public mainActivityRvAdapter(ArrayList<Hours> hoursArrayList) {
        this.hoursArrayList = hoursArrayList;
    }

    @NonNull
    @Override
    public MainActivityViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MainActivityViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_main, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MainActivityViewHolder holder, int position) {
        holder.icon_hour.setImageResource(hoursArrayList.get(position).getIcon_id());
        holder.temp_hour.setText(hoursArrayList.get(position).getTemperature()+ MainActivity.unit_value);
        holder.desc_hour.setText(hoursArrayList.get(position).getDescription());
        holder.day_hour.setText(hoursArrayList.get(position).getDay());
        holder.time_hour.setText(hoursArrayList.get(position).getTime());
    }

    @Override
    public int getItemCount() {
        return hoursArrayList.size();
    }
}
