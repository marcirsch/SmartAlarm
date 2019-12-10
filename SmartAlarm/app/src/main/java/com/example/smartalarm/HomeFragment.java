package com.example.smartalarm;

import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

//import org.apache.http.client.utils.URIBuilder;
//import org.json.JSONObject;

//import java.net.URISyntaxException;
import org.apache.http.NoHttpResponseException;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.Math;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

//simple struct class for storing departures in a station
class departure {
    public String sign;
    public String type;
    public String headsign;
    public String departure;

    public departure(String sign, String type, String headsign, String departure) {
        this.sign = sign;
        this.type = type;
        this.headsign = headsign;
        this.departure = departure;
    }
}

public class HomeFragment extends Fragment {

    private TextView timeTV;
    private Timer timer;
    private TextView latText;
    private TextView longText;
    private TextView debugTest;
    private TextView stopName;
    private ImageView coffeeImage;
    private TextView tempText;
    private TextView locationText;
    private ImageView weatherImage;
    private TextView coffeeText;

    //stop dostance treshold
    final double treshold = 0.005;

    private String apiKey = "apaiary-test";
    private String weatherApiKey = "ccac1ecc8e30be0b216820f0cb92a969";
    RequestQueue queue;

    //Start Fused Location Provider Client
    //for location services
    //private FusedLocationProviderClient locationClient = LocationServices.getFusedLocationProviderClient(getActivity());


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //First inflate fragment, only then find time textView
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        //Find time on main screen and start update thread
        timeTV = view.findViewById(R.id.time);
        setTime();
        //Start scheduled thread to update clock
        timer = new Timer();
        startClockThread();
        //Start location updater and API thread
        startAPIThread();
        //Find helper location texts
        //latText = view.findViewById(R.id.latText);
        //longText = view.findViewById(R.id.longText);
        //Find debug text
        //debugTest = view.findViewById(R.id.debugText);
        //Stop name: clickable and entry point for API use
        stopName = view.findViewById(R.id.stop_name);
        //cofee image
        coffeeImage = view.findViewById(R.id.coffee_image);
        //coffee text
        coffeeText = view.findViewById(R.id.coffee_status);
        //weather stuff
        tempText = view.findViewById(R.id.weather_temperature);
        locationText = view.findViewById(R.id.weather_city);
        weatherImage = view.findViewById(R.id.weather_image);

        //create requestqueue for API and ESP8266 requests
        queue = Volley.newRequestQueue(getActivity().getApplicationContext());

        //set on-click listerer for stop name text: entry point to most API features
        stopName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //getNearStops(3, 3);
                getStarts("BKK_F02203");
            }
        });

        //set on-click listener for coffee image
        coffeeImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendCoffeeRequest(PreferencesHelper.getIpAddress(getContext()),2);
            }
        });

        /*locationClient.getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        lastLocation = location;
                        if (location != null) {
                            lastLocation = null;
                        }
                    }
                });*/

        //getLocation();
        return view;
    }


    @Override
    public void onPause() {
        super.onPause();
        timer.cancel();
    }

    private void startClockThread() {
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        setTime();
                    }
                });

            }
        }, 0, 1000);
    }

    private void setTime(){
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm", Locale.ENGLISH);
        String currentTime = sdf.format(new Date());
        timeTV.setText(currentTime);
    }

    private void startAPIThread() {
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        getLocation();
                        getWeather();
                    }
                });

            }
        }, 0, 1000);
    }

    private void getLocation()  {
        //retrieve last known location from main activity
        ((MainActivity)getActivity()).retrieveLocation();
        //longText.setText("Longitude: " + ((MainActivity)getActivity()).longitude);
        //latText.setText("Latitude: " + ((MainActivity)getActivity()).latitude);
    }

    private void getNearStops(final double latitude, double longitude) {
        String baseUrl = "https://futar.bkk.hu/api/query/v1/ws/otp/api/where/";
        String funcUrl = "stops-for-location.json";
        String paramUrl = "?";

        Map<String, String> params = new HashMap<String, String>();
        params.put("key", apiKey);
        params.put("lon", Double.toString(longitude));
        params.put("lat", Double.toString(latitude));
        params.put("version", "");
        params.put("appVersion", "");
        params.put("includeReferences", "");
        params.put("lonSpan", "");
        params.put("latSpan", "");
        params.put("radius", "");
        params.put("query", "");

        for (Map.Entry<String, String> actParam : params.entrySet()) {
            paramUrl += (actParam.getKey() + "=" + actParam.getValue() + "&");
        }

        String requestUrl = baseUrl + funcUrl + paramUrl;
        debugTest.setText(requestUrl);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, baseUrl + funcUrl, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                //itt kell a választ feldolgozni
                try {
                    JSONArray stopList = response.getJSONObject("data").getJSONArray("list");
                    JSONArray goodList = new JSONArray();
                    HashMap<String, String> allStops = new HashMap<String, String>();
                    for(int i = 0; i<stopList.length(); i++)
                    {
                        //collect stops that are near from all stops (API can only return all stops)
                        if((Math.abs(stopList.getJSONObject(i).getDouble("lat") - ((MainActivity)getActivity()).latitude) < treshold) && (Math.abs(stopList.getJSONObject(i).getDouble("lon") - ((MainActivity)getActivity()).longitude) < treshold)) {
                            goodList.put(stopList.getJSONObject(i));
                        }
                    }
                }
                catch (JSONException e) {
                    debugTest.append("JSON error");
                }
                debugTest.append("--Success");
            }
        }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    //itt kell a hibát kezelni
                    debugTest.append("--Failure");
                }
            });

        jsonObjectRequest.setShouldCache(false);
        queue.add(jsonObjectRequest);
    }

    private void getStarts(String stopId) {
        String baseUrl = "https://futar.bkk.hu/api/query/v1/ws/otp/api/where/";
        String funcUrl = "arrivals-and-departures-for-stop.json";
        String paramUrl = "?";

        Map<String, String> params = new HashMap<String, String>();
        params.put("key", apiKey);
        params.put("version", "");
        params.put("appVersion", "");
        params.put("includeReferences", "");
        params.put("stopId", stopId);
        params.put("onlyDepartures", "");
        params.put("limit", "");
        params.put("minutesBefore", "");
        params.put("minutesAfter", "");

        paramUrl = "?key=apaiary-test&version=&appVersion=&includeReferences=&stopId=" + stopId + "&onlyDepartures=&limit=&minutesBefore=&minutesAfter=";
        /*for (Map.Entry<String, String> actParam : params.entrySet()) {
            paramUrl += (actParam.getKey() + "=" + actParam.getValue() + "&");
        }*/

        String requestUrl = baseUrl + funcUrl + paramUrl;
        //debugTest.setText(requestUrl);
        Log.d("STOP-API", requestUrl);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, baseUrl + funcUrl, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                //itt kell a választ feldolgozni
                try {
                    String status = response.getString("status");
                    if (status != "OK") {
                        throw new Exception(status);
                    }
                    JSONArray stopTimes = response.getJSONObject("data").getJSONObject("entry").getJSONArray("stopTimes");

                    ArrayList<departure> departures = new ArrayList<departure>();

                    for (int i = 0; i< stopTimes.length(); i++) {
                        String depTime = stopTimes.getJSONObject(i).getString("departureTime");
                        String tripID = stopTimes.getJSONObject(i).getString("tripId");
                        String routeID = "0";
                        String sign = "0";
                        String headsign = stopTimes.getJSONObject(i).getString("stopHeadsign");
                        String type = "0";

                        JSONObject trips = response.getJSONObject("data").getJSONObject("references").getJSONObject("trips");
                        Iterator<String> keys = trips.keys();
                        while(keys.hasNext()) {
                            String key = keys.next();
                            if (trips.get(key) instanceof JSONObject) {
                                if (((JSONObject) trips.get(key)).getString("id") == tripID) {
                                    routeID = ((JSONObject) trips.get(key)).getString("routeId");
                                }
                            }
                        }

                        JSONObject routes = response.getJSONObject("data").getJSONObject("references").getJSONObject("routes");
                        keys = routes.keys();
                        while(keys.hasNext()) {
                            String key = keys.next();
                            if (trips.get(key) instanceof JSONObject) {
                                if (((JSONObject) trips.get(key)).getString("id") == routeID) {
                                    sign = ((JSONObject) trips.get(key)).getString("shortName");
                                    type = ((JSONObject) trips.get(key)).getString("type");
                                }
                            }
                        }

                        departures.add(new departure(sign, type, headsign, depTime));

                    }

                    for(int i=0; i<departures.size(); i++) {
                        debugTest.append(departures.get(i).type + ".");
                    }


                    JSONArray stopList = response.getJSONObject("data").getJSONArray("list");
                    JSONArray goodList = new JSONArray();
                    HashMap<String, String> allStops = new HashMap<String, String>();
                    for(int i = 0; i<stopList.length(); i++)
                    {
                        //collect stops that are near from all stops (API can only return all stops)
                        if((Math.abs(stopList.getJSONObject(i).getDouble("lat") - ((MainActivity)getActivity()).latitude) < treshold) && (Math.abs(stopList.getJSONObject(i).getDouble("lon") - ((MainActivity)getActivity()).longitude) < treshold)) {
                            goodList.put(stopList.getJSONObject(i));
                        }
                    }
                }
                catch (JSONException e) {
                    debugTest.append("JSON error");
                }
                catch (Exception e) {
                    debugTest.append(e.getMessage());
                }
                debugTest.append("--Success");
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //itt kell a hibát kezelni
                debugTest.append("--Failure");
            }
        });

        jsonObjectRequest.setShouldCache(false);
        queue.add(jsonObjectRequest);
    }

    private void sendCoffeeRequest(String ipAddress, int state) {
        //state: 0: OFF, 1: ON, -1: TOOGLE
        String stateString;
        switch (state) {
            case 0:
                stateString = "0";
                break;
            case 1:
                stateString = "1";
                break;
            case 2:
                stateString = "2";
                break;
            default:
                stateString = "2";
        }

        String requestUrl = "http://" + ipAddress + "/cm?cmnd=Power%20" + stateString;

        StringRequest stringRequest = new StringRequest(Request.Method.GET, requestUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if(response.contains("OFF")) {
                    coffeeText.setText("Turned off");
                }
                else if(response.contains("ON")) {
                    coffeeText.setText("Turned on");
                }
                else coffeeText.setText(response);
                //do something with the response
                Toast.makeText(getContext(), "Success! Response: " + response, Toast.LENGTH_SHORT).show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //do something in case of error
                Toast.makeText(getContext(), "Failed! " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        stringRequest.setShouldCache(false);
        queue.add(stringRequest);
    }

    private void getWeather() {
        String url = "https://api.openweathermap.org/data/2.5/weather?lat=" + ((MainActivity)getActivity()).latitude + "&lon=" + ((MainActivity)getActivity()).longitude + "&units=metric&appid=" + weatherApiKey;

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                //itt kell a választ feldolgozni
                try {
                    String icon = "w" + ((JSONObject)response.getJSONArray("weather").get(0)).getString("icon");
                    Double temp_d = response.getJSONObject("main").getDouble("temp");
                    String temp = Long.toString(Math.round(temp_d));
                    String location = response.getString(("name"));

                    tempText.setText(temp + "°C");
                    locationText.setText(location);
                    weatherImage.setImageResource(getResources().getIdentifier(icon, "drawable", getActivity().getPackageName()));


                } catch (JSONException e) {
                    debugTest.append("Weather: " + e.getMessage());
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    //itt kell a hibát kezelni
                    debugTest.append(error.getMessage());
                }
            });

        jsonObjectRequest.setShouldCache(false);
        queue.add(jsonObjectRequest);

    }

}
