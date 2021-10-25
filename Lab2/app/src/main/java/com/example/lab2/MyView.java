package com.example.lab2;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.view.View;

public class MyView  extends View  {

    private float[] data =null;
    private String orientationText;
    private String deviceVectorText;
    private String userGgAngle;
    private String userMoloAngle;
    private String directionsGg;
    private String directionsMolo;
    private String angleGg;
    private String angleMolo;


    public void setAngleMolo(String angleMolo) {
        this.angleMolo = angleMolo;
    }

    public void setDirectionsGg(String directionsGg) {
        this.directionsGg = directionsGg;
    }

    public void setDirectionsMolo(String directionsMolo) {
        this.directionsMolo = directionsMolo;
    }

    public void setAngleGg(String angleGg) {
        this.angleGg = angleGg;
    }

    public void setOrientationText(String orientationText) {
        this.orientationText = orientationText;
    }

    public void setDeviceVectorText(String deviceVectorText) {
        this.deviceVectorText = deviceVectorText;
    }

    public void setUserGgAngle(String userGgAngle) {
        this.userGgAngle = userGgAngle;
    }

    public void setUserMoloAngle(String userMoloAngle) {
        this.userMoloAngle = userMoloAngle;
    }

    public void setData(float[] data) {
        this.data = data;
        j++;
    }

    private int i =0;
    private int j=0;

    public MyView(Context context) {
        super(context);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Paint p = new Paint();
        p.setARGB(255, 255, 0, 0);
        final float testTextSize = 48f;
        p.setTextSize(testTextSize);

        canvas.drawText("MojeView.onDraw: i="+i++, 200, 100, p);
        canvas.drawText("MojeView.onDraw: j="+j++, 200, 150, p);
        canvas.drawText("Orientation: " + orientationText, 200, 200, p);
        canvas.drawText("Device vector: " + deviceVectorText, 200, 250, p);
        canvas.drawText("Angle gg: " + userGgAngle, 200, 300, p);
        canvas.drawText("Angle molo: " + userMoloAngle, 200, 350, p);
        canvas.drawText("Directions gg: " + directionsGg, 200, 400, p);
        canvas.drawText("Directions molo: " + directionsMolo, 200, 450, p);
        canvas.drawText("Angle vector gg: " + angleGg, 200, 500, p);
        canvas.drawText("Angle vector molo: " + angleMolo, 200, 550, p);
        if(data !=null)
        {
            canvas.drawLine(0, 0, 100* data[0], 100* data[0], p);
            p.setStyle(Paint.Style.STROKE);
            canvas.drawRect(100, 100, 200, 200, p);

        }
    }




}
