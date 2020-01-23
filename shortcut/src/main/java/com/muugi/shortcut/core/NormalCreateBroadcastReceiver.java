package com.muugi.shortcut.core;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.muugi.shortcut.utils.Logger;

/**
 * Created by ZP on 2019-07-08.
 */
public class NormalCreateBroadcastReceiver extends BroadcastReceiver {

    private static final String TAG = "NormalCreateBroadcastRe";

    public static final String ACTION = "com.shortcut.core.normal_create";

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        Logger.get().log(TAG, "onReceive: " + action);
        if (ACTION.equals(action)) {
            String id = intent.getStringExtra("id");
            String label = intent.getStringExtra("label");
            Logger.get().log(TAG,  "Shortcut normal create callback," +
                    " id = " + id + ",label = " + label);
            ShortcutV2.get().notifyAsyncCreate(id, id, label);
        }
    }
}
