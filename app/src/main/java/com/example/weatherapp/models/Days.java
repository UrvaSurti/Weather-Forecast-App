package com.example.weatherapp.models;

import java.io.Serializable;

public class Days implements Serializable {
    String days_date, days_temp_max, days_temp_min, days_description, days_precip;
    String days_uv, days_morning, days_afternoon, days_evening, days_night;
    int days_icon_id;

    public Days() {

    }

    public String getDate() {
        return days_date;
    }

    public void setDate(String date) {
        this.days_date = date;
    }

    public String getTemp_max() {
        return days_temp_max;
    }

    public void setTemp_max(String temp_max) {
        this.days_temp_max = temp_max;
    }

    public String getTemp_min() {
        return days_temp_min;
    }

    public void setTemp_min(String temp_min) {
        this.days_temp_min = temp_min;
    }

    public String getDescription() {
        return days_description;
    }

    public void setDescription(String description) {
        this.days_description = description;
    }

    public String getPrecip() {
        return days_precip;
    }

    public void setPrecip(String precip) {
        this.days_precip = precip;
    }

    public String getUv() {
        return days_uv;
    }

    public void setUv(String uv) {
        this.days_uv = uv;
    }

    public String getMorning() {
        return days_morning;
    }

    public void setMorning(String morning) {
        this.days_morning = morning;
    }

    public String getAfternoon() {
        return days_afternoon;
    }

    public void setAfternoon(String afternoon) {
        this.days_afternoon = afternoon;
    }

    public String getEvening() {
        return days_evening;
    }

    public void setEvening(String evening) {
        this.days_evening = evening;
    }

    public String getNight() {
        return days_night;
    }

    public void setNight(String night) {
        this.days_night = night;
    }

    public int getIcon_id() {
        return days_icon_id;
    }

    public void setIcon_id(int icon_id) {
        this.days_icon_id = icon_id;
    }

    @Override
    public String toString() {
        return "Days{" +
                "date='" + days_date + '\'' +
                ", temp_max='" + days_temp_max + '\'' +
                ", temp_min='" + days_temp_min + '\'' +
                ", description='" + days_description + '\'' +
                ", precip='" + days_precip + '\'' +
                ", uv='" + days_uv + '\'' +
                ", morning='" + days_morning + '\'' +
                ", afternoon='" + days_afternoon + '\'' +
                ", evening='" + days_evening + '\'' +
                ", night='" + days_night + '\'' +
                ", icon_id=" + days_icon_id +
                '}';
    }
}
