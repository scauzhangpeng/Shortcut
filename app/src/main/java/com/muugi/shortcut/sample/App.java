package com.muugi.shortcut.sample;

import android.app.Application;

import com.squareup.leakcanary.LeakCanary;

/**
 * Created by ZP on 2019-06-16.
 */
public class App extends Application {

    public static boolean isLogined = false;

    @Override
    public void onCreate() {
        super.onCreate();
        if (LeakCanary.isInAnalyzerProcess(this)) {
            // This process is dedicated to LeakCanary for heap analysis.
            // You should not init your app in this process.
            return;
        }
        LeakCanary.install(this);
    }
}
