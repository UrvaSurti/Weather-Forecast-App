package com.example.weatherapp.viewHolders;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.weatherapp.R;

public class MainActivityViewHolder extends RecyclerView.ViewHolder{

    public TextView day_hour, time_hour, temp_hour, desc_hour;
    public ImageView icon_hour;

    public MainActivityViewHolder(@NonNull View itemView) {
        super(itemView);
        day_hour = itemView.findViewById(R.id.day_hour);
        time_hour = itemView.findViewById(R.id.time_hour);
        temp_hour = itemView.findViewById(R.id.temp_hour);
        desc_hour = itemView.findViewById(R.id.desc_hour);
        icon_hour = itemView.findViewById(R.id.icon_hour);
    }
}
