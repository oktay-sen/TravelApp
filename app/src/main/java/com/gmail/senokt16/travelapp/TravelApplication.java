package com.gmail.senokt16.travelapp;

import android.app.Application;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;

public class TravelApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        FirebaseApp.initializeApp(this);
        FirebaseAuth.getInstance().addAuthStateListener(new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if (firebaseAuth.getCurrentUser() == null) {
                    Log.v("TRAVEL", "User not logged in.");
                    firebaseAuth.signInAnonymously();
                } else {
                    Log.v("TRAVEL", "User " + firebaseAuth.getCurrentUser().getUid() + " logged in successfully.");
                }
            }
        });
    }
}
