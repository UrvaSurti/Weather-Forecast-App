package com.example.weatherapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;

import com.example.weatherapp.adapters.dailyActivityRvAdapter;
import com.example.weatherapp.models.Days;

import java.util.ArrayList;
import java.util.Objects;

public class Daily_weather extends AppCompatActivity {
    ArrayList<Days>daysArrayList;
    RecyclerView Daily_recyclerView;
    dailyActivityRvAdapter dailyActivityRvAdapter;
    String resolvedLocation;

    @SuppressLint("NotifyDataSetChanged")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daily_weather);
        
        Daily_recyclerView = findViewById(R.id.Daily_recyclerView);
        
        getIntentData();
        setAdapter();
        
        dailyActivityRvAdapter.notifyDataSetChanged();
    }

    private void setAdapter() {
        dailyActivityRvAdapter = new dailyActivityRvAdapter(daysArrayList);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        Daily_recyclerView.setLayoutManager(layoutManager);
        Daily_recyclerView.setItemAnimator(new DefaultItemAnimator());
        Daily_recyclerView.setAdapter(dailyActivityRvAdapter);
    }

    private void getIntentData() {
        Intent intent = getIntent();
        daysArrayList = (ArrayList<Days>) intent.getSerializableExtra("incoming");
        resolvedLocation = intent.getStringExtra("location");
        Objects.requireNonNull(getSupportActionBar()).setTitle(resolvedLocation+" 15 day");
    }
}