package com.example.lab3;

import android.location.Location;
import android.location.LocationListener;

import androidx.annotation.NonNull;

import java.util.function.Consumer;

public class ProcessLocation implements LocationListener {

    private Location prevLocation = null;
    private final Consumer<String> locationTextViewFiller;

    public ProcessLocation(Consumer<String> locationTextViewFiller) {
        this.locationTextViewFiller = locationTextViewFiller;
    }


    @Override
    public void onLocationChanged(@NonNull Location location) {
        processLocation(location);
    }

    private void processLocation(Location location) {
        String info;
        info = location.getLatitude() + " " + location.getLongitude();
        if (prevLocation != null) {
            float bearing = prevLocation.bearingTo(location);
            float distance = prevLocation.distanceTo(location);
            info += " " + distance + " " + bearing;
        }
        locationTextViewFiller.accept(info);
        prevLocation = location;
    }
}
