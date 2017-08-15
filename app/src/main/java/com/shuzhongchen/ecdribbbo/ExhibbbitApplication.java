package com.shuzhongchen.ecdribbbo;

import android.app.Application;

import com.facebook.drawee.backends.pipeline.Fresco;

/**
 * Created by shuzhongchen on 8/15/17.
 */

public class ExhibbbitApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Fresco.initialize(this);
    }
}
