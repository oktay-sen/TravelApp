package com.gmail.senokt16.travelapp;


import android.location.Location;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.ServerValue;

public class Step {
    public double lat, lon;
    public long timestamp;

    public Step() {

    }

    public Step(Location location) {
        this(location.getLatitude(), location.getLongitude());
    }

    public Step(double latitude, double longitude) {
        lat = latitude;
        lon = longitude;
    }
/*
    public static double measure (double lat1, double lon1, double lat2, double lon2) {// generally used geo measurement function
        double R = 6378.137; // Radius of earth in KM
        double dLat = lat2 * Math.PI / 180 - lat1 * Math.PI / 180;
        double dLon = lon2 * Math.PI / 180 - lon1 * Math.PI / 180;
        double a = Math.sin(dLat/2) * Math.sin(dLat/2) +
                Math.cos(lat1 * Math.PI / 180) * Math.cos(lat2 * Math.PI / 180) *
                        Math.sin(dLon/2) * Math.sin(dLon/2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
        double d = R * c;
        return d * 1000; // meters
    }

    public static double measureFromZero(double lat, double lon) {
        double R = 6378.137;
        double dLat = lat * Math.PI / 180;
        double dLon = lon * Math.PI / 180;

    }*/

    public static double numToDeg(int num) {
        return num * 5 / 111111;
    }

    public static int degToNum(double deg) {
        return (int) (deg * 111111 / 5);
    }

    /*
    public static int latToY(double latitude) {
        return (int) (latitude/UNIT);
    }

    public static int longToX(double longitude) {
        return (int) (longitude/UNIT);
    }
    */

    @Override
    public boolean equals(Object obj) {
        return obj instanceof Step && ((Step) obj).lat == lat && ((Step) obj).lon == lon;
    }
}
