package com.example.lab1;

import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Arrays;

public class MainActivity extends AppCompatActivity implements SensorEventListener {

    private SensorManager sm;
    private Sensor sensorMagnetic;
    private Sensor gravSensor;
    private Sensor accSensor;
    private TextView accSensorTextView;
    private TextView orientationTextView;
    private View colorView;
    private ImageView image;
    private float currentDegree = 0f;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        accSensorTextView = findViewById(R.id.test1);
        orientationTextView = findViewById(R.id.test2);
        colorView = findViewById(R.id.test3);
        image = findViewById(R.id.imageViewCompass);
        sm = (SensorManager) getSystemService(SENSOR_SERVICE);
        sensorMagnetic = sm.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
        gravSensor = sm.getDefaultSensor(Sensor.TYPE_GRAVITY);
        accSensor = sm.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (accSensor != null) {
            sm.registerListener(this, accSensor,
                    SensorManager.SENSOR_DELAY_GAME);
        }
        if (gravSensor != null) {
            sm.registerListener(this, gravSensor,
                    SensorManager.SENSOR_DELAY_GAME);
        }
        if (sensorMagnetic != null) {
            sm.registerListener(this, sensorMagnetic,
                    SensorManager.SENSOR_DELAY_GAME);
        }
        sm.registerListener(this, sm.getDefaultSensor(Sensor.TYPE_ORIENTATION),
                SensorManager.SENSOR_DELAY_GAME);
    }

    @Override
    protected void onPause() {
        super.onPause();
        sm.unregisterListener(this);
    }

    float[] gravity = null;
    float[] mGeomagnetic = null;

    @Override
    public void onSensorChanged(SensorEvent event) {
        switch (event.sensor.getType()) {
            case Sensor.TYPE_GRAVITY:
                gravity = event.values.clone();
                float[] rotationMatrix = new float[9];
                float[] I = new float[9];
                boolean success = SensorManager.getRotationMatrix(rotationMatrix, I, gravity, mGeomagnetic);
                if (success) {
                    orientationTextView.setText(Arrays.toString(SensorManager.getOrientation(rotationMatrix, new float[3])));
                    int inclination = (int) Math.round(Math.toDegrees(Math.acos(rotationMatrix[8])));
                    if (inclination < 90) {
                        // face up
                        colorView.setBackgroundColor(Color.YELLOW);
                    } else if (inclination > 90) {
                        // face down
                        colorView.setBackgroundColor(Color.RED);
                    }
                }
                break;
            case Sensor.TYPE_ACCELEROMETER:
                gravity = event.values.clone();
                accSensorTextView.setText(Arrays.toString(gravity));
                break;
            case Sensor.TYPE_MAGNETIC_FIELD:
                mGeomagnetic = event.values.clone();
                break;
            case Sensor.TYPE_ORIENTATION:
                rotateCompass(event);
                break;
        }
    }

    private void rotateCompass(SensorEvent event) {
        float degree = Math.round(event.values.clone()[0]);

        RotateAnimation ra = new RotateAnimation(
                currentDegree,
                -degree,
                Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF,
                0.5f);

        ra.setDuration(210);
        ra.setFillAfter(true);

        image.startAnimation(ra);
        currentDegree = -degree;
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }
}