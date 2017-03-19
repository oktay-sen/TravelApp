package com.gmail.senokt16.travelapp;


import android.location.Location;

import com.google.firebase.database.Exclude;

import java.util.Map;

public class User {
    public String email;
    public Map<String, Step> steps;

    @Exclude
    public boolean isVisited(Location l) {
        Step s = new Step(l);
        return steps.containsValue(s);
    }

    @Exclude
    public boolean isVisited(double latitude, double longitude) {
        Step s = new Step(latitude, longitude);
        return steps.containsValue(s);
    }

    @Exclude
    public boolean isVisited(int lat, int lon) {
        Step s = new Step();
        s.lat = lat;
        s.lon = lon;
        return steps.containsValue(s);
    }
}
