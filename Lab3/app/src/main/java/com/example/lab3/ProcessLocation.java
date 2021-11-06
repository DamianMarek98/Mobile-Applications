package com.example.lab3;

import android.location.Location;
import android.location.LocationListener;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.function.Consumer;

public class ProcessLocation implements LocationListener {

    private Location prevLocation = null;
    private final Consumer<String> locationTextViewFiller;
    public final Consumer<Location> locationConsumer;
    private final List<Location> locations = new ArrayList<>();
    private Location closestLocation;

    public ProcessLocation(Consumer<String> locationTextViewFiller) {
        this.locationTextViewFiller = locationTextViewFiller;
        locationConsumer = locations::add;
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
            info += "Dystans: " + distance + "m, kierunek:" + bearing + "\n";
            info += "Prędkość: " + location.getSpeed() + "m/s\n";
            info += "Akt. pozycja: lon:" + location.getLongitude() + ", lat: " + location.getLatitude() + "\n";
            info += "Pop. pozycja: lon:" + prevLocation.getLongitude() + ", lat: " + prevLocation.getLatitude() + "\n";
            try {
                info += addClosestPointInfo(location);
            } catch (Exception e) {
                info += "Brak najbliższej lokalizacji!";
            }
        }
        locationTextViewFiller.accept(info);
        prevLocation = location;
    }

    private String addClosestPointInfo(Location location) throws Exception {
        if (locations.isEmpty()) {
            return "Brak najbliższej lokalizacji!";
        }

        Location closestLocation = locations.stream()
                .min(Comparator.comparing(l -> l.distanceTo(location)))
                .orElseThrow(Exception::new);

        if (this.closestLocation == null) {
            this.closestLocation = closestLocation;
        } else if (!this.closestLocation.equals(closestLocation)) {
            this.closestLocation = closestLocation;
        }
        this.closestLocation = closestLocation;
        return "Najbliższy pkt: dystans: " + location.distanceTo(this.closestLocation) / 1000 + "km, kierunek:" +
                location.bearingTo(this.closestLocation) + "\n";
    }
}
