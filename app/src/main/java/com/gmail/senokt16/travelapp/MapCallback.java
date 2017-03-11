package com.gmail.senokt16.travelapp;

import android.graphics.Color;
import android.location.Location;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.Random;

public class MapCallback implements OnMapReadyCallback {
    private GoogleMap mMap;
    private LatLng currentPlace;

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {

        mMap = googleMap;

        if (currentPlace == null) {
            currentPlace = new LatLng(-34, 151);
        }

        // Add a marker in Sydney and move the camera
        CircleOptions putDot = new CircleOptions();
        putDot.center(this.currentPlace);
        putDot.fillColor(Color.BLACK);
        putDot.radius(500);
        mMap.addCircle(putDot);
        mMap.moveCamera(CameraUpdateFactory.newLatLng(this.currentPlace));
    }

    public void onLocationChanged(Location location) {
        this.currentPlace = new LatLng(location.getLatitude(), location.getLongitude());
        if (mMap != null) {
            Random rnd = new Random();
            int color = Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256));

            CircleOptions putDot = new CircleOptions();
            putDot.center(this.currentPlace);
            putDot.fillColor(color);
            putDot.radius(200);
            mMap.addCircle(putDot);
            mMap.moveCamera(CameraUpdateFactory.newLatLng(this.currentPlace));
        }
    }
}
