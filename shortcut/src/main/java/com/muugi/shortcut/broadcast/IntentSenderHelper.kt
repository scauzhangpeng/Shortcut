package com.muugi.shortcut.broadcast

import android.app.PendingIntent
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.IntentSender
import android.os.Bundle

/**
 * Created by ZP on 2019/3/3.
 */
object IntentSenderHelper {
    fun getDefaultIntentSender(context: Context, action: String): IntentSender {
        return PendingIntent.getBroadcast(context, 0, Intent(action),
                PendingIntent.FLAG_ONE_SHOT).intentSender
    }

    fun getDefaultIntentSender(context: Context, action: String, clz: Class<*>, bundle: Bundle?): IntentSender {
        val intent = Intent(action)
        intent.component = ComponentName(context, clz)
        if (bundle != null) {
            intent.putExtras(bundle)
        }
        return PendingIntent.getBroadcast(context, 0, intent,
                PendingIntent.FLAG_ONE_SHOT).intentSender
    }
}