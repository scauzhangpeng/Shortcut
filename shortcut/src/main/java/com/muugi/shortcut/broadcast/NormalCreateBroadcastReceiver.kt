package com.muugi.shortcut.broadcast

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.muugi.shortcut.core.Shortcut.Companion.EXTRA_ID
import com.muugi.shortcut.core.Shortcut.Companion.EXTRA_LABEL
import com.muugi.shortcut.core.Shortcut.Companion.TAG
import com.muugi.shortcut.core.Shortcut.Companion.singleInstance
import com.muugi.shortcut.utils.Logger

/**
 * Created by ZP on 2019-07-08.
 */
class NormalCreateBroadcastReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val action = intent.action
        Logger.get().log(TAG, "onReceive: $action")
        if (ACTION == action) {
            val id = intent.getStringExtra(EXTRA_ID)
            val label = intent.getStringExtra(EXTRA_LABEL)
            Logger.get().log(TAG, "Shortcut normal create callback," +
                    " id = $id, label = $label")
            if (id != null && label != null) {
                singleInstance.notifyCreate(id, label)
            }
        }
    }

    companion object {
        const val ACTION = "com.shortcut.core.normal_create"
    }
}