package com.muugi.shortcut.core;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import androidx.annotation.NonNull;

/**
 * Created by ZP on 2019/3/3.
 */
public class IntentSenderHelper {

    public static IntentSender getDefaultIntentSender(@NonNull Context context, @NonNull String action) {
        return PendingIntent.getBroadcast(context, 0, new Intent(action),
                PendingIntent.FLAG_ONE_SHOT).getIntentSender();
    }
}
