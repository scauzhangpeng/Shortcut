package com.muugi.shortcut.core;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * Created by ZP on 2019-06-28.
 */
public class AutoCreateBroadcastReceiver extends BroadcastReceiver {

    private static final String TAG = "AutoCreateBroadcastRece";

    private OnAutoCreateListener mOnAutoCreateListener;

    public interface OnAutoCreateListener {
        void onReceive(Context context, Intent intent);
    }

    public  void setOnAutoCreateListener(OnAutoCreateListener listener) {
        this.mOnAutoCreateListener = listener;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        Log.d(TAG, "onReceive: " + action);
        if ("com.shortcut.core.auto_create".equals(action)) {
            Log.d(TAG, "onReceive: auto create, listener = " + (mOnAutoCreateListener != null));
            if (mOnAutoCreateListener != null) {
                mOnAutoCreateListener.onReceive(context, intent);
            }
        }
    }
}
