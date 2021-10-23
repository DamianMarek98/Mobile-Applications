package com.example.lab2;

import android.content.Context;
import android.hardware.Camera;
import android.os.Build;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import androidx.annotation.RequiresApi;

import java.io.IOException;
import java.util.List;

public class Preview extends SurfaceView implements SurfaceHolder.Callback {
    private SurfaceHolder mHolder;
    private Camera camera;

    Preview(Context context) {
        super(context);
        mHolder = getHolder();

        mHolder.addCallback(this);

        //musi być mimo że jest w dokumentacji jest deprecated
        mHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
    }


    public void surfaceCreated(SurfaceHolder holder) {
        camera = Camera.open();
        //Toast.makeText(this.getContext(), "surfaceCreated", Toast.LENGTH_LONG).show();
        try {
            camera.setPreviewDisplay(holder);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void surfaceDestroyed(SurfaceHolder holder) {
        camera.stopPreview();
        camera.release();
        //Toast.makeText(this.getContext(), "surfaceDestroyed", Toast.LENGTH_LONG).show();
        camera = null;

    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void surfaceChanged(SurfaceHolder holder, int format, int w, int h) {
        Camera.Parameters parameters = camera.getParameters();
        List<Camera.Size> supportedPreviews = parameters.getSupportedPreviewSizes();

        supportedPreviews.stream()
                .filter(preview -> Math.abs(preview.width - w) <= 200 && Math.abs(preview.height - h) <= 200)
                .findFirst()
                .ifPresent(size -> parameters.setPreviewSize(size.width, size.height));

        camera.setParameters(parameters);
        camera.startPreview();
    }
}
