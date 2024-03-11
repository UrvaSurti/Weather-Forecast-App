package com.example.weatherapp;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.weatherapp.adapters.mainActivityRvAdapter;
import com.example.weatherapp.models.Days;
import com.example.weatherapp.models.Hours;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    final String apiUrl = "https://weather.visualcrossing.com/VisualCrossingWebServices/rest/services/timeline/";
    final String key = "TBSE8ECJGMHYGBA9BBRL6RLMZ";
    private RequestQueue queue;
    public ArrayList<Hours> hoursArrayList;
    public ArrayList<Days> daysArrayList;
    public TextView temp_textView, dateandtime, feels_like, descript, windy, humid_textView, uv_Index, visibility_measu;
    public ImageView weatherofIcon;
    public TextView morn, after_noon, even, night1, sub_morn, sub_night1, sub_after_noon, sub_even, sun_rise, sun_set;
    public static String location, unit, unit_value;
    String resolved_location, unit_icon;
    public RecyclerView recyclerView;
    public mainActivityRvAdapter mainActivityRvAdapter;
    public SwipeRefreshLayout swipeRefreshLayout;
    public SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setStaticValues();
        setUserInterfaceIds();
        loadUserPreferences();
        queue = Volley.newRequestQueue(this);
        weatherDataDownload();
        setRecyclerAdapter();
        setSwipeLayout();
    }

    @Override
    public void onPause() {
        Log.d("Key", "onPause: ");
        saveUserPreferences();
        super.onPause();
    }

    private void setRecyclerAdapter() {
        mainActivityRvAdapter = new mainActivityRvAdapter(hoursArrayList);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mainActivityRvAdapter);
    }

    private void setSwipeLayout() {
        swipeRefreshLayout = findViewById(R.id.swiperfresh);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                daysArrayList.removeAll(daysArrayList);
                hoursArrayList.removeAll(hoursArrayList);
                weatherDataDownload();
                swipeRefreshLayout.setRefreshing(false);
                //Log.d("KEY", "onRefresh: ");
            }
        });
    }

    public void weatherDataDownload(){
        if(!checkNetworkConnection()){
            dateandtime.setText("No internet connection!");
            return;
        }
        String url = apiUrl +location+"/?unitGroup="+unit+"&key="+key;
        Response.Listener<JSONObject> listener = new Response.Listener<JSONObject>() {
            @SuppressLint({"SetTextI18n", "NotifyDataSetChanged"})
            @Override
            public void onResponse(JSONObject response) {
                try{

                    JSONArray days = response.getJSONArray("days");
                    JSONArray hours_array = days.getJSONObject(0).getJSONArray("hours");
                    JSONObject currentConditions = response.getJSONObject("currentConditions");

                    resolved_location = response.get("resolvedAddress").toString();
                    Log.d("resolvedlocation",resolved_location);

                    Objects.requireNonNull(getSupportActionBar()).setTitle(resolved_location);

                    setMainLayoutValues(currentConditions,hours_array);
                    setHoursValues(days, getHourOfDay(currentConditions.get("datetimeEpoch").toString()));
                    setDaysValues(days);

                    recyclerView.setVisibility(View.VISIBLE);
                    mainActivityRvAdapter.notifyDataSetChanged();
                }
                catch(Exception exception){
                    Log.d("KEY", "ExceptionListener"+exception.toString());
                }
            }
        };

        Response.ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                try {
                    JSONObject jsonObject = new JSONObject(new String(error.networkResponse.data));
                }
                catch(Exception exception){
                    Toast.makeText(getApplicationContext(), "Error!", Toast.LENGTH_LONG).show();
                    loadUserPreferences();
                    Log.d("KEY", "ExceptionErrorListener");
                }
            }
        };
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, listener, errorListener);
        queue.add(jsonObjectRequest);
    }

    @SuppressLint("DiscouragedApi")
    private void setHoursValues(JSONArray days, int currentTimeIndex) throws Exception{
        String day;
        String time;
        int icon_id;
        String temperature;
        String description;
        JSONObject hour;
        JSONArray hours_array;
        for(int i=0; i<4; i++){
            hours_array = days.getJSONObject(i).getJSONArray("hours");
            day = "Today";
            for(int j=0; j<24; j++){
                hour = hours_array.getJSONObject(j);
                time = timeOnlySet(hour.get("datetimeEpoch").toString());
                icon_id = getApplicationContext().getResources().getIdentifier(hour.get("icon").toString().replace("-", "_"), "drawable", getApplicationContext().getPackageName());
                temperature = hour.get("temp").toString();
                description = hour.get("conditions").toString();
                hoursArrayList.add(new Hours(day, time, icon_id, temperature, description));
            }
        }
    }

    @SuppressLint("SetTextI18n")
    void setMainLayoutValues(JSONObject currentConditions, JSONArray hours_array) throws Exception {


        String winddir = getDirection(Double.parseDouble(currentConditions.get("winddir").toString()));
        String windspeed = currentConditions.get("windspeed").toString();
        String windgust = currentConditions.get("windgust").toString();


        temp_textView.setText(currentConditions.get("temp").toString() + unit_value);
        dateandtime.setText(fullDateSet(currentConditions.get("datetimeEpoch").toString()));
        feels_like.setText("Feels like "+currentConditions.get("feelslike").toString()+ unit_value);

        @SuppressLint("DiscouragedApi") int icon_id = getApplicationContext().getResources().getIdentifier(currentConditions.get("icon").toString().replace("-", "_"), "drawable", getApplicationContext().getPackageName());
        weatherofIcon.setImageResource(icon_id);
        weatherofIcon.setVisibility(View.VISIBLE);

        descript.setText(currentConditions.get("conditions").toString()+" ("+currentConditions.get("cloudcover").toString()+"% clouds)");
        if(windgust.equals("null")){
            windy.setText("Winds: "+winddir+" at "+windspeed+" mph"+" not gusting");
        }
        else{
            windy.setText("Winds: "+winddir+" at "+windspeed+" mph"+" gusting to "+windgust+" mph");
        }

        humid_textView.setText("Humidity: "+currentConditions.get("humidity").toString()+"%");
        uv_Index.setText("UV Index: "+currentConditions.get("uvindex").toString());
        visibility_measu.setText("Visibility: "+currentConditions.get("visibility").toString()+"mi");

        morn.setText(((JSONObject)hours_array.get(8)).get("temp").toString()+ unit_value);
        sub_morn.setVisibility(View.VISIBLE);

        after_noon.setText(((JSONObject)hours_array.get(13)).get("temp").toString()+ unit_value);
        sub_after_noon.setVisibility(View.VISIBLE);

        even.setText(((JSONObject)hours_array.get(17)).get("temp").toString()+ unit_value);
        sub_even.setVisibility(View.VISIBLE);

        night1.setText(((JSONObject)hours_array.get(23)).get("temp").toString()+ unit_value);
        sub_night1.setVisibility(View.VISIBLE);

        sun_rise.setText("Sunrise: "+timeOnlySet(currentConditions.get("sunriseEpoch").toString()));
        sun_set.setText("Sunset: "+timeOnlySet(currentConditions.get("sunsetEpoch").toString()));
    }

    @SuppressLint("DiscouragedApi")
    public void setDaysValues(JSONArray days) throws Exception{
        for(int i=0; i<15; i++) {
            Days local_days = new Days();

            local_days.setDate(dayDateSet(((JSONObject) days.get(i)).get("datetimeEpoch").toString()));
            local_days.setMorning(((JSONObject) ((JSONArray) ((JSONObject) days.get(i)).get("hours")).get(8)).get("temp").toString());
            local_days.setAfternoon(((JSONObject) ((JSONArray) ((JSONObject) days.get(i)).get("hours")).get(13)).get("temp").toString());
            local_days.setEvening(((JSONObject) ((JSONArray) ((JSONObject) days.get(i)).get("hours")).get(17)).get("temp").toString());
            local_days.setNight(((JSONObject) ((JSONArray) ((JSONObject) days.get(i)).get("hours")).get(22)).get("temp").toString());
            local_days.setTemp_max(((JSONObject) days.get(i)).get("tempmax").toString());
            local_days.setTemp_min(((JSONObject) days.get(i)).get("tempmin").toString());
            local_days.setDescription(((JSONObject) days.get(i)).get("description").toString());
            local_days.setPrecip(((JSONObject) days.get(i)).get("precipprob").toString());
            local_days.setUv(((JSONObject) days.get(i)).get("uvindex").toString());
            local_days.setIcon_id(getApplicationContext().getResources().getIdentifier(((JSONObject) days.get(i)).get("icon").toString().replace("-", "_"), "drawable", getApplicationContext().getPackageName()));
            daysArrayList.add(local_days);
        }

    }


    public void loadUserPreferences() {
        preferences = getSharedPreferences("loc", Context.MODE_PRIVATE);
        if(!preferences.getString("location", "").equals("")){
            location = preferences.getString("location", "");
        }

        if(!preferences.getString("unit", "").equals("")){
            unit = preferences.getString("unit", "");
        }

        if(!preferences.getString("unit_value", "").equals("")){
            unit_value = preferences.getString("unit_value", "");
        }

        if(!preferences.getString("unit_icon", "").equals("")){
            unit_icon = preferences.getString("unit_icon", "");
        }
    }

    public void saveUserPreferences(){
        SharedPreferences preferences = getSharedPreferences("loc", Context.MODE_PRIVATE);
        SharedPreferences.Editor prefsEditor = preferences.edit();
        prefsEditor.putString("location", location);
        prefsEditor.putString("unit", unit);
        prefsEditor.putString("unit_value", unit_value);
        prefsEditor.putString("unit_icon", unit_icon);
        prefsEditor.apply();
    }


    public void setUserInterfaceIds() {
        temp_textView = findViewById(R.id.temp);
        dateandtime = findViewById(R.id.dateandtime);
        feels_like = findViewById(R.id.feels_like);
        morn = findViewById(R.id.morn);
        after_noon = findViewById(R.id.after_noon);
        even = findViewById(R.id.even);
        night1 = findViewById(R.id.night1);
        sub_morn = findViewById(R.id.sub_morn);
        sub_night1 = findViewById(R.id.sub_night1);
        sub_after_noon = findViewById(R.id.sub_after_noon);
        sub_even = findViewById(R.id.sub_even);
        sun_rise = findViewById(R.id.sun_rise);
        sun_set = findViewById(R.id.sun_set);
        descript = findViewById(R.id.descript);
        windy = findViewById(R.id.windy);
        humid_textView = findViewById(R.id.humid);
        uv_Index = findViewById(R.id.uv_Index);
        visibility_measu = findViewById(R.id.visibility_measu);
        weatherofIcon = findViewById(R.id.weatherofIcon);
        hoursArrayList = new ArrayList<>();
        daysArrayList = new ArrayList<>();
        recyclerView = findViewById(R.id.recyclerViewMain);
    }


    public void setStaticValues() {
        unit_icon = "units_f";
        location = "Chicago,IL";
        resolved_location = "";
        unit = "us";
        unit_value = "°F";
    }

    public boolean checkNetworkConnection() {
        ConnectivityManager connectivityManager = getSystemService(ConnectivityManager.class);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return (networkInfo != null && networkInfo.isConnectedOrConnecting());
    }

    public String fullDateSet(String datetime){
        Date date = new Date(Long.parseLong(datetime) * 1000L);
        SimpleDateFormat fullDate = new SimpleDateFormat("EEE MMM dd h:mm a, yyyy", Locale.getDefault());
        return fullDate.format(date);
    }

    public String timeOnlySet(String datetime){
        Date date = new Date(Long.parseLong(datetime) * 1000L);
        SimpleDateFormat timeOnly = new SimpleDateFormat("h:mm a", Locale.getDefault());
        return timeOnly.format(date);
    }

    public String dayOnlySet(String datetime){
        Date date = new Date(Long.parseLong(datetime) * 1000L);
        SimpleDateFormat dayOnly = new SimpleDateFormat("EEE", Locale.getDefault());
        return dayOnly.format(date);
    }

    public String dayDateSet(String datetime){
        Date date = new Date(Long.parseLong(datetime) * 1000L);
        SimpleDateFormat dayDate = new SimpleDateFormat("EEEE MM/dd", Locale.getDefault());
        return dayDate.format(date);
    }

    public int getHourOfDay(String datetime){
        Date date = new Date(Long.parseLong(datetime) * 1000L);
        return date.getHours();
    }

    public String getDirection(double degrees) {
        if (degrees >= 337.5 || degrees < 22.5)
            return "N";
        if (degrees >= 22.5 && degrees < 67.5)
            return "NE";
        if (degrees >= 67.5 && degrees < 112.5)
            return "E";
        if (degrees >= 112.5 && degrees < 157.5)
            return "SE";
        if (degrees >= 157.5 && degrees < 202.5)
            return "S";
        if (degrees >= 202.5 && degrees < 247.5)
            return "SW";
        if (degrees >= 247.5 && degrees < 292.5)
            return "W";
        if (degrees >= 292.5 && degrees < 337.5)
            return "NW";
        return "X"; // We'll use 'X' as the default if we get a bad value
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.mainmenu, menu);
        int icon_id = getApplicationContext().getResources().getIdentifier(unit_icon, "drawable", getApplicationContext().getPackageName());
        MenuItem menuItem = menu.findItem(R.id.mainMenu_transform);
        menuItem.setIcon(icon_id);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem){
        if(!checkNetworkConnection()){
            return false;
        }
        if(menuItem.getItemId() == R.id.mainMenu_daily){
            Intent intent = new Intent(this, Daily_weather.class);
            intent.putExtra("incoming", daysArrayList);
            intent.putExtra("location", resolved_location);
            startActivity(intent);
        }
        else if(menuItem.getItemId() == R.id.mainMenu_transform){
            if(unit_icon.equals("units_f")){
                unit_icon = "units_c";
                unit_value = "°C";
                unit = "uk";
            }
            else{
                unit_icon = "units_f";
                unit_value = "°F";
                unit = "us";
            }
            int icon_id = getApplicationContext().getResources().getIdentifier(unit_icon, "drawable", getApplicationContext().getPackageName());
            menuItem.setIcon(icon_id);
            daysArrayList.removeAll(daysArrayList);
            hoursArrayList.removeAll(hoursArrayList);
            weatherDataDownload();
        }
        else{
            showDialogLocation();
        }
        return super.onOptionsItemSelected(menuItem);
    }


    public void showDialogLocation() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        final EditText editText = new EditText(this);
        editText.setInputType(InputType.TYPE_CLASS_TEXT);
        editText.setGravity(Gravity.CENTER_HORIZONTAL);
        builder.setTitle("Enter a location");
        builder.setMessage("For US locations enter as 'City', or 'City, State'.\n\n" +
                "For International locations enter as 'City, Country'.");
        builder.setView(editText);
        builder.setCancelable(true);

        builder.setPositiveButton(
                "OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogInterface, int i) {
                        location = editText.getText().toString();
                        daysArrayList.removeAll(daysArrayList);
                        hoursArrayList.removeAll(hoursArrayList);
                        weatherDataDownload();

                    }
                });

        builder.setNegativeButton(
                "CANCEL",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                });

        AlertDialog alert = builder.create();
        alert.show();

    }
}