package com.example.weatherapp.models;

import java.io.Serializable;

public class Hours implements Serializable {
    String day, time;
    int icon_id;
    String temperature, description;

    public Hours(String day, String time, int icon_id, String temperature, String description) {
        this.day=day;
        this.time = time;
        this.icon_id=icon_id;
        this.temperature=temperature;
        this.description=description;
    }


    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public int getIcon_id() {
        return icon_id;
    }

    public void setIcon_id(int icon_id) {
        this.icon_id = icon_id;
    }

    public String getTemperature() {
        return temperature;
    }

    public void setTemperature(String temperature) {
        this.temperature = temperature;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return "Hours{" +
                "day='" + day + '\'' +
                ", time='" + time + '\'' +
                ", icon_id='" + icon_id + '\'' +
                ", temperature='" + temperature + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}
