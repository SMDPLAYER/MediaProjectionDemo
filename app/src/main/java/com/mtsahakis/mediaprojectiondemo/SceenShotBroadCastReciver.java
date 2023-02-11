package com.mtsahakis.mediaprojectiondemo;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class SceenShotBroadCastReciver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if (context!=null) ScreenCaptureService.isFirst=true;
    }
}
