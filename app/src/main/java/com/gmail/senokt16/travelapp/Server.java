package com.gmail.senokt16.travelapp;

import android.location.Location;
import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;

import java.util.HashMap;
import java.util.Map;

public class Server {
    private static final String TAG = "TAPP-Server";
    public static void submitStep(Location location) {
        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            DatabaseReference ref = FirebaseDatabase.getInstance().getReference("users").child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                    .child("steps").push();
            Step s = new Step(location);
            Map<String, Object> step = new HashMap<>();
            step.put("lat", s.lat);
            step.put("lon", s.lon);
            step.put("timestamp", ServerValue.TIMESTAMP);
            ref.updateChildren(step);
        }
        else
            Log.d(TAG, "Current user is null during submit step.");
    }
}
