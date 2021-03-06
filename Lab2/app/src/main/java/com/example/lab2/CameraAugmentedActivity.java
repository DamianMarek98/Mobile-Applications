package com.example.lab2;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.WindowManager;
import android.widget.RelativeLayout;

import androidx.annotation.RequiresApi;

import com.google.android.gms.location.FusedLocationProviderClient;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class CameraAugmentedActivity extends Activity implements SensorEventListener {
    /**
     * Called when the activity is first created.
     */

    RelativeLayout rl;
    SensorManager sm;
    MyView myCameraOverlay;
    Preview myCameraView;
    private Sensor sensorMagnetic;
    private Sensor gravSensor;
    private Location ggLocation = new Location("");
    private Location sopotMoloLoaction = new Location("");
    private Location userLocation = new Location("");
    private float[] ggDirections = new float[3];
    private float[] moloDirections = new float[3];
    private FusedLocationProviderClient fusedLocationClient;

    @SuppressLint("MissingPermission")
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.main);

        rl = findViewById(R.id.relativeLayout1);

        myCameraView = new Preview(this);
        rl.addView(myCameraView);

        myCameraOverlay = new MyView(this);
        rl.addView(myCameraOverlay);

        sm = (SensorManager) getSystemService(SENSOR_SERVICE);
        gravSensor = sm.getDefaultSensor(Sensor.TYPE_GRAVITY);
        sensorMagnetic = sm.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
    }

    private void setCoordinates() {
        ggLocation.setLatitude(54.37144083542352);
        ggLocation.setLongitude(18.619128815946382);
        sopotMoloLoaction.setLatitude(54.447115370150804);
        sopotMoloLoaction.setLongitude(18.573474841027867);
        userLocation.setLatitude(54.371391991585206);
        userLocation.setLongitude(18.613172884655746);
    }

    @Override
    protected void onPause() {
        super.onPause();
        sm.unregisterListener(this);
        //Toast.makeText(this, "KameraAugmentedActivity.onPause()\nunregisterListener", Toast.LENGTH_LONG).show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        Sensor def = sm.getDefaultSensor(Sensor.TYPE_ALL);
        sm.registerListener(this, def, SensorManager.SENSOR_DELAY_NORMAL);
        //Toast.makeText(this, "KameraAugmentedActivity.onResume()\nregisterListener", Toast.LENGTH_LONG).show();
        if (gravSensor != null) {
            sm.registerListener(this, gravSensor,
                    SensorManager.SENSOR_DELAY_GAME);
        }
        if (sensorMagnetic != null) {
            sm.registerListener(this, sensorMagnetic,
                    SensorManager.SENSOR_DELAY_GAME);
        }
    }

    //Nadpisane z SensorEventListener
    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }

    float[] gravity = null;
    float[] mGeomagnetic = null;
    float[] cameraVector = {0, 0, -1};

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onSensorChanged(SensorEvent event) {
        myCameraOverlay.setData(event.values);
        myCameraView.invalidate();

        if (event.sensor.getType() == Sensor.TYPE_GRAVITY) {
            gravity = event.values.clone();
            float[] rotationMatrix = new float[9];
            float[] I = new float[9];
            boolean success = SensorManager.getRotationMatrix(rotationMatrix, I, gravity, mGeomagnetic);
            if (success) {
                float[] floats = SensorManager.getOrientation(rotationMatrix, new float[3]);
                double[] doubles = IntStream
                        .range(0, floats.length)
                        .mapToDouble(i -> floats[i])
                        .toArray();
                List<Double> degrees = Arrays.stream(doubles)
                        .map(Math::toDegrees)
                        .boxed()
                        .collect(Collectors.toList());
                myCameraOverlay.setOrientationText(degrees.toString());

                setCoordinates();
                float degreeUserGg = userLocation.bearingTo(ggLocation);
                float degreeUserMolo = userLocation.bearingTo(sopotMoloLoaction);
                Log.i("molo", Float.toString(degreeUserMolo));
                Log.i("gg", Float.toString(degreeUserGg));
                myCameraOverlay.setUserGgAngle(Float.toString(degreeUserGg));
                myCameraOverlay.setUserMoloAngle(Float.toString(degreeUserMolo));

                // Wektor obrotu urz??dzenia wyliczony poprzez przemno??enie macierzy obrotu
                // z wektorem kamery:
                float[] deviceVector = new float[3];
                deviceVector[0] = rotationMatrix[0] * cameraVector[0] + rotationMatrix[1] * cameraVector[1] + rotationMatrix[2] * cameraVector[2];
                deviceVector[1] = rotationMatrix[3] * cameraVector[0] + rotationMatrix[4] * cameraVector[1] + rotationMatrix[5] * cameraVector[2];
                deviceVector[2] = rotationMatrix[6] * cameraVector[0] + rotationMatrix[7] * cameraVector[1] + rotationMatrix[8] * cameraVector[2];
                myCameraOverlay.setDeviceVectorText(Arrays.toString(deviceVector));

                ggDirections = setDirections(ggDirections, degreeUserGg);
                moloDirections = setDirections(moloDirections, degreeUserMolo);
                myCameraOverlay.setDirectionsGg(Arrays.toString(ggDirections));
                myCameraOverlay.setDirectionsMolo(Arrays.toString(moloDirections));
                myCameraOverlay.setAngleGg(Double.toString(Math.toDegrees(getAngle(ggDirections, deviceVector))));
                myCameraOverlay.setAngleMolo(Double.toString(Math.toDegrees(getAngle(moloDirections, deviceVector))));


                myCameraOverlay.invalidate();
            }
        } else if (event.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD) {
            mGeomagnetic = event.values.clone();
        }
    }

    private float[] setDirections(float[] directions, float degree) {
        directions[0] = (float) Math.sin(Math.toRadians(degree));
        directions[1] = (float) Math.cos(Math.toRadians(degree));
        directions[2] = 0f;
        return directions;
    }

    double getRadians(float degree) {
        return degree * 3.14 / 180;
    }

    double getDegree(float radians) {
        return radians * 180 / 3.14;
    }

    float getAngle(float[] a, float b[]) {
        return (float) Math.acos(
                dotProduct(a, b) / (vectorMagnitude(a) * vectorMagnitude(b))
        );
    }

    float dotProduct(float[] a, float[] b) {
        return a[0] * b[0] + a[1] * b[1] + a[2] * b[2];
    }

    float vectorMagnitude(float[] vec) {
        return (float) Math.sqrt(vec[0] * vec[0] + vec[1] * vec[1] + vec[2] * vec[2]);
    }

}
