package com.example.lab3;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import java.util.function.Consumer;

public class MainActivity extends AppCompatActivity {

    private LocationManager lm;
    private ProcessLocation listener;
    private TextView tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.INTERNET}, 0);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        registerListener();
    }

    void registerListener() {
        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this,
                        Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        registerProcessLocation();
        lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, listener);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState, @Nullable PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
        setContentView(R.layout.activity_main);
        registerProcessLocation();
        if (ActivityCompat.checkSelfPermission(
                this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 0);
            return;
        }
        registerListener();
    }

    private void registerProcessLocation() {
        if (lm == null) {
            lm = (LocationManager) getSystemService(LOCATION_SERVICE);
            tv = findViewById(R.id.loctv);
            listener = new ProcessLocation(locationText -> {
                tv.setText(locationText);
            });
            addButtonCallback();
        }
    }

    private void addButtonCallback() {
        TextView latTv = findViewById(R.id.lat);
        TextView lonTv = findViewById(R.id.lon);
        findViewById(R.id.addLocButton).setOnClickListener(v -> {
            double lat = Double.parseDouble(latTv.getText().toString());
            double lon = Double.parseDouble(lonTv.getText().toString());
            Location location = new Location("");
            location.setLatitude(lat);
            location.setLongitude(lon);
            listener.locationConsumer.accept(location);
            latTv.setText(null);
            lonTv.setText(null);
        });
    }

}