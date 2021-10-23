package com.example.lab2;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.view.View;

public class MyView  extends View  {

    private float[] data =null;
    private String orientationText;
    private String deviceVectorText;

    public void setOrientationText(String orientationText) {
        this.orientationText = orientationText;
    }

    public void setDeviceVectorText(String deviceVectorText) {
        this.deviceVectorText = deviceVectorText;
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
        if(data !=null)
        {
            canvas.drawLine(0, 0, 100* data[0], 100* data[0], p);
            p.setStyle(Paint.Style.STROKE);
            canvas.drawRect(100, 100, 200, 200, p);

        }
    }




}
