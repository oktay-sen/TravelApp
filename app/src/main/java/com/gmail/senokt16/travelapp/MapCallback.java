package com.gmail.senokt16.travelapp;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapCallback implements OnMapReadyCallback {
    private GoogleMap mMap;

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

        // Add a marker in Sydney and move the camera
	Location loca = mMap.getMyLocatoin();
	double latt = loca.getLatitude();
	double lont = loca.getLongitude();
	CircleOptions coTest = new CircleOptions();
	coTest.center(new LatLng(latt, lont));
	coTest.fillColor(0);
        mMap.addCircle(coTest);
    }
}
