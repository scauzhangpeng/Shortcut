package com.muugi.shortcut.sample;

import android.app.Application;
import android.util.Log;

import com.muugi.shortcut.utils.Logger;
import com.muugi.shortcut.utils.Printer;

/**
 * Created by ZP on 2019-06-16.
 */
public class App extends Application {

    public static boolean isLogined = false;

    @Override
    public void onCreate() {
        super.onCreate();
//        if (LeakCanary.isInAnalyzerProcess(this)) {
//            // This process is dedicated to LeakCanary for heap analysis.
//            // You should not init your app in this process.
//            return;
//        }
//        LeakCanary.install(this);
        initLog();
    }

    private void initLog() {
        Logger.get().setLogger(new Printer() {
            @Override
            public void log(String tag, String message) {
                Log.d(tag, "println: " + message);
            }

            @Override
            public void log(String tag, String message, Exception exception) {
                exception.printStackTrace();
                Log.d(tag, "println: " + message);
            }
        });
    }
}
