package com.muugi.shortcut.sample;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import com.muugi.shortcut.utils.Logger;
import com.muugi.shortcut.utils.Printer;

import me.weishu.reflection.Reflection;

/**
 * Created by ZP on 2019-06-16.
 */
public class App extends Application {

    public static boolean isLogined = false;

    @Override
    public void onCreate() {
        super.onCreate();
        initLog();
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        Reflection.unseal(base);
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
