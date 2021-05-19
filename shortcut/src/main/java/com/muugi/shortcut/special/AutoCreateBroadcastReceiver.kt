package com.muugi.shortcut.special

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.muugi.shortcut.core.Shortcut.Companion.EXTRA_ID
import com.muugi.shortcut.core.Shortcut.Companion.EXTRA_LABEL
import com.muugi.shortcut.core.Shortcut.Companion.TAG
import com.muugi.shortcut.core.Shortcut.Companion.singleInstance
import com.muugi.shortcut.core.ShortcutCore
import com.muugi.shortcut.utils.Logger

/**
 * Created by ZP on 2019-06-28.
 */
class AutoCreateBroadcastReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        val action = intent.action
        Logger.get().log(TAG, "onReceive: $action")
        if (ACTION == action) {
            val id = intent.getStringExtra(EXTRA_ID)
            val label = intent.getStringExtra(EXTRA_LABEL)
            Logger.get().log(
                TAG, "Shortcut auto create callback, " +
                        "id = $id, label = $label"
            )
            if (id != null && label != null) {
                val fetchExitShortcut = ShortcutCore().fetchExitShortcut(context, id)
                fetchExitShortcut?.let {
                    try {
                        val declaredField = it.javaClass.getDeclaredField("mTitle")
                        declaredField.isAccessible = true
                        declaredField.set(it, label)
                        val updatePinShortcut = ShortcutCore().updatePinShortcut(context, it)
                        if (updatePinShortcut) {
                            singleInstance.notifyCreate(id, label)
                        }
                    } catch (e: Exception) {
                        Logger.get().log(TAG, "receive error, ", e)
                    }
                }
            }
        }
    }

    companion object {
        const val ACTION = "com.shortcut.core.auto_create"
    }
}