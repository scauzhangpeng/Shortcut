package com.muugi.shortcut.core;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * Created by ZP on 2019-07-08.
 */
public class NormalCreateBroadcastReceiver extends BroadcastReceiver {

    private static final String TAG = "NormalCreateBroadcastRe";

    private OnNormalCreateListener mOnNormalCreateListener;

    public interface OnNormalCreateListener {
        void onReceive(Context context, Intent intent);
    }

    public void setOnNormalCreateListener(OnNormalCreateListener listener) {
        this.mOnNormalCreateListener = listener;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        Log.d(TAG, "onReceive: " + action);
        if ("com.shortcut.core.normal_create".equals(action)) {
            Log.d(TAG, "onReceive: auto create, listener = " + (mOnNormalCreateListener != null));
            if (mOnNormalCreateListener != null) {
                mOnNormalCreateListener.onReceive(context, intent);
            }
        }
    }
}
