package com.muugi.shortcut.core;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.muugi.shortcut.utils.Logger;

/**
 * Created by ZP on 2019-06-28.
 */
public class AutoCreateBroadcastReceiver extends BroadcastReceiver {

    private static final String TAG = "AutoCreateBroadcastRece";

    public static final String ACTION = "com.shortcut.core.auto_create";

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        Logger.get().log(TAG, "onReceive: " + action);
        if (ACTION.equals(action)) {
            String id = intent.getStringExtra("id");
            String label = intent.getStringExtra("label");
            String label_clone = intent.getStringExtra("label_clone");
            Logger.get().log(TAG, "Shortcut auto create callback, " +
                    "id = " + id + ", label = " + label + ", label_clone = " + label);
            ShortcutV2.get().updateAfterCreate(context, id, label, label_clone);
        }
    }
}
