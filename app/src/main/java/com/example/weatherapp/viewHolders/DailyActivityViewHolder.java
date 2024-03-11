package com.example.weatherapp.viewHolders;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.weatherapp.R;

public class DailyActivityViewHolder extends RecyclerView.ViewHolder {

    public TextView date_daily, temperature_daily, description_daily, precip_daily,uv_daily,morning_daily,noon_daily,evening_daily,night_daily;
    public ImageView icon_daily;

    public DailyActivityViewHolder(@NonNull View itemView) {
        super(itemView);
        date_daily = itemView.findViewById(R.id.date_daily);
        temperature_daily = itemView.findViewById(R.id.temper_daily);
        icon_daily = itemView.findViewById(R.id.icon_daily);
        noon_daily = itemView.findViewById(R.id.noon_daily);
        evening_daily = itemView.findViewById(R.id.even_daily);
        night_daily = itemView.findViewById(R.id.night_daily);
        description_daily = itemView.findViewById(R.id.descp_daily);
        precip_daily = itemView.findViewById(R.id.precipit_daily);
        uv_daily = itemView.findViewById(R.id.uv_daily);
        morning_daily = itemView.findViewById(R.id.morn_daily);
    }


}
