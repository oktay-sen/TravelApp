package com.gmail.senokt16.travelapp;

import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.util.Pair;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.GroundOverlay;
import com.google.android.gms.maps.model.GroundOverlayOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polygon;
import com.google.android.gms.maps.model.PolygonOptions;
import com.google.android.gms.maps.model.TileOverlayOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.maps.android.SphericalUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

public class MapCallback implements OnMapReadyCallback {
    private GoogleMap mMap;
    private LatLng currentPlace;
    private Context context;
    private Map<LatLng, Polygon> squares = new HashMap<>();
    private Set<Step> steps = new HashSet<>();
    Bitmap overlay;
    private static final double UNIT = 0.000005;
    private GroundOverlay groundOverlay;

    public MapCallback(Context context) {
        this.context = context;
    }

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

/*        // Add a marker in Sydney and move the camera
        CircleOptions putDot = new CircleOptions();
        putDot.center(this.currentPlace);
        putDot.fillColor(Color.BLACK);
        putDot.radius(500);
        mMap.addCircle(putDot);*/
        mMap.moveCamera(CameraUpdateFactory.newLatLng(this.currentPlace));
        if (ActivityCompat.checkSelfPermission(context, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        mMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(context, R.raw.style_json));
        mMap.setMyLocationEnabled(true);
        mMap.setIndoorEnabled(false);
        mMap.setMinZoomPreference(15.5f);
        mMap.setMaxZoomPreference(20);
        mMap.getUiSettings().setTiltGesturesEnabled(false);
        //mMap.getUiSettings().setZoomGesturesEnabled(false);
        mMap.getUiSettings().setScrollGesturesEnabled(false);
        //mMap.addTileOverlay(new TileOverlayOptions().tileProvider(new BlackTileProvider(context)).fadeIn(false));
        overlay = Bitmap.createBitmap(2048, 2048, Bitmap.Config.ARGB_8888);
        FirebaseAuth.getInstance().addAuthStateListener(new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if (firebaseAuth.getCurrentUser() != null) {
                    FirebaseDatabase.getInstance().getReference("users").child(firebaseAuth.getCurrentUser().getUid())
                            .child("steps").addChildEventListener(new ChildEventListener() {
                        @Override
                        public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                            Step step = dataSnapshot.getValue(Step.class);
                            if (step != null)
                                steps.add(step);

/*                            Pair<Integer, Integer> pos = new Pair<>(step.x, step.y);
                            GroundOverlay overlay = mMap.addGroundOverlay(new GroundOverlayOptions()
                            .image(BitmapDescriptorFactory.fromResource(R.drawable.black_tile))
                            .position(new LatLng(Step.numToDeg(step.y), Step.numToDeg(step.x)), 25f)
                            .transparency(0.5f));
                            squares.put(pos, overlay);*/
                        }

                        @Override
                        public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                        }

                        @Override
                        public void onChildRemoved(DataSnapshot dataSnapshot) {

                        }

                        @Override
                        public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                }
            }
        });
    }

    public void onLocationChanged(Location location) {
        this.currentPlace = new LatLng(location.getLatitude(), location.getLongitude());
        double curLat = location.getLatitude();
        double curLon = location.getLongitude();
        Server.submitStep(location);

        overlay = Bitmap.createBitmap(2048, 2048, Bitmap.Config.ARGB_8888);
        Canvas c = new Canvas(overlay);
        Paint black = new Paint();
        black.setColor(Color.GRAY);
        Paint trans = new Paint();
        trans.setColor(Color.TRANSPARENT);
        trans.setAntiAlias(true);
        trans.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_OUT));
        c.drawColor(Color.DKGRAY);
        for (Step s : steps) {
            LatLng st = new LatLng(s.lat, s.lon);
            SphericalUtil.computeDistanceBetween(st, currentPlace);
            int xOffset = (int) SphericalUtil.computeDistanceBetween(new LatLng(curLat, s.lon), currentPlace);
            if (s.lon < curLon)
                xOffset *= -1;
            int yOffset = (int) SphericalUtil.computeDistanceBetween(new LatLng(s.lat, curLon), currentPlace);
            if (s.lat < curLat)
                yOffset *= -1;
            c.drawCircle(1024 + xOffset, 1024 + yOffset, 128, trans);
        }
        c.drawCircle(1024, 1024, 128, trans);

        Log.d("Location", "Lat: " + location.getLatitude());
        Log.d("Location", "x: " + Step.degToNum(location.getLatitude()));
        Log.d("Location", "NewLat: " + Step.numToDeg(Step.degToNum(location.getLatitude())));

        if (mMap != null) {
/*            mMap.addMarker(new MarkerOptions()
            .position(new LatLng(location.getLatitude(), location.getLongitude())));*/

/*            mMap.addPolygon(new PolygonOptions()
                    .add(new LatLng(location.getLatitude() - 0.000001, location.getLongitude() - 0.000001))
                    .add(new LatLng(location.getLatitude() - 0.000001, location.getLongitude() + 0.000001))
                    .add(new LatLng(location.getLatitude() + 0.000001, location.getLongitude() + 0.000001))
                    .add(new LatLng(location.getLatitude() + 0.000001, location.getLongitude() - 0.000001))
                    .fillColor(Color.BLACK)
            );*/
            if (groundOverlay == null) {
                groundOverlay = mMap.addGroundOverlay(new GroundOverlayOptions()
                                .image(BitmapDescriptorFactory.fromBitmap(overlay))
                                .anchor(0.5f, 0.5f)
                                .position(currentPlace, 2048f));
            } else {
                groundOverlay.setImage(BitmapDescriptorFactory.fromBitmap(overlay));
                groundOverlay.setPosition(currentPlace);
            }


/*            mMap.addGroundOverlay(new GroundOverlayOptions()
                    .image(BitmapDescriptorFactory.fromResource(R.drawable.black_tile))
                    .position(new LatLng(Step.numToDeg(Step.degToNum(location.getLatitude())), Step.numToDeg(Step.degToNum(location.getLongitude()))), 25f));*/

/*            for (double lat=location.getLatitude()-(UNIT * 25); lat <= location.getLatitude()+(UNIT * 25); lat+=UNIT) {
                for (double lng=location.getLongitude()-(UNIT * 25); lng <= location.getLongitude()+(UNIT * 25); lng+=UNIT) {
                    LatLng cur = new LatLng(lat, lng);
                    if (squares.get(cur) == null) {
                        Polygon polygon = mMap.addPolygon(new PolygonOptions()
                                .add(new LatLng(lat - UNIT/2, lng - UNIT/2))
                                .add(new LatLng(lat - UNIT/2, lng + UNIT/2))
                                .add(new LatLng(lat + UNIT/2, lng + UNIT/2))
                                .add(new LatLng(lat + UNIT/2, lng - UNIT/2))
                                .fillColor(Color.BLACK)
                        );

                        squares.put(cur, polygon);
                    }
                }
            }*/

/*            Random rnd = new Random();
            int color = Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256));

            CircleOptions putDot = new CircleOptions();
            putDot.center(this.currentPlace);
            putDot.fillColor(color);
            putDot.radius(10);
            mMap.addCircle(putDot);*/
            mMap.moveCamera(CameraUpdateFactory.newLatLng(this.currentPlace));
        }
    }
}
