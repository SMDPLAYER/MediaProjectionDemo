package com.mtsahakis.mediaprojectiondemo;

import static com.mtsahakis.mediaprojectiondemo.ScreenCaptureService.liveCapture;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.media.Image;
import android.media.projection.MediaProjectionManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;

import com.bumptech.glide.Glide;

import java.nio.ByteBuffer;


public class ScreenCaptureActivity extends AppCompatActivity {

    private static final int REQUEST_CODE = 100;
    ImageView imgScreenshot;

    /****************************************** Activity Lifecycle methods ************************/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // start projection
        Button startButton = findViewById(R.id.startButton);
        startButton.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                startProjection();
            }
        });

        // stop projection
        Button stopButton = findViewById(R.id.stopButton);
        imgScreenshot = findViewById(R.id.imgScreenshot);
        stopButton.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                stopProjection();
            }
        });
        liveCapture.observe(this, new Observer<Bitmap>() {
            @Override
            public void onChanged(Bitmap bitmap) {

                Glide.with(imgScreenshot).load(bitmap).into(imgScreenshot);
//                imgScreenshot.setImageBitmap(bitmap);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
                    startForegroundService(ScreenCaptureService.getStartIntent(this, resultCode, data));
                    else startService(ScreenCaptureService.getStartIntent(this, resultCode, data));

            }
        }
    }

    /****************************************** UI Widget Callbacks *******************************/
    private void startProjection() {
        MediaProjectionManager mProjectionManager =
                (MediaProjectionManager) getSystemService(Context.MEDIA_PROJECTION_SERVICE);
        startActivityForResult(mProjectionManager.createScreenCaptureIntent(), REQUEST_CODE);
    }

    private void stopProjection() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
            startForegroundService(com.mtsahakis.mediaprojectiondemo.ScreenCaptureService.getStopIntent(this));
            else startService(com.mtsahakis.mediaprojectiondemo.ScreenCaptureService.getStopIntent(this));
    }

}