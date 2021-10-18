package com.muugi.shortcut.core

import android.content.Context
import android.content.pm.ShortcutInfo
import android.content.pm.ShortcutManager
import android.os.Build
import android.os.Bundle
import androidx.core.content.pm.ShortcutInfoCompat
import androidx.core.content.pm.ShortcutManagerCompat
import com.muugi.shortcut.broadcast.IntentSenderHelper
import com.muugi.shortcut.broadcast.NormalCreateBroadcastReceiver

/**
 * Created by ZP on 2020/4/20.
 */
open class ShortcutCore {
    open fun isShortcutExit(context: Context, id: String, label: CharSequence): Boolean {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N_MR1) {
            val mShortcutManager = context.getSystemService(ShortcutManager::class.java)
                    ?: return false
            val pinnedShortcuts = mShortcutManager.pinnedShortcuts
            for (pinnedShortcut in pinnedShortcuts) {
                if (pinnedShortcut.id == id) {
                    return true
                }
            }
        }
        return false
    }

    fun fetchExitShortcut(context: Context, id: String): ShortcutInfo? {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N_MR1) {
            val mShortcutManager = context.getSystemService(ShortcutManager::class.java)
                ?: return null
            val pinnedShortcuts = mShortcutManager.pinnedShortcuts
            for (pinnedShortcut in pinnedShortcuts) {
                if (pinnedShortcut.id == id) {
                    return pinnedShortcut
                }
            }
        }
        return null
    }

    fun updatePinShortcut(context: Context, info: ShortcutInfo): Boolean {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N_MR1) {
            val mShortcutManager = context.getSystemService(ShortcutManager::class.java)
                ?: return false
            return mShortcutManager.updateShortcuts(listOf(info))
        }
        return false
    }

    fun updatePinShortcut(context: Context, info: ShortcutInfoCompat): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N_MR1) {
            updatePinShortcut(context, info.toShortcutInfo())
        } else {
            false
        }
    }

    open fun createShortcut(context: Context, shortcutInfoCompat: ShortcutInfoCompat, updateIfExit: Boolean, shortcutAction: ShortcutAction, check: Int) {
        val shortcutExit = isShortcutExit(context, shortcutInfoCompat.id, shortcutInfoCompat.shortLabel)
        if (shortcutExit && updateIfExit) {
            val updatePinShortcut = updatePinShortcut(context, shortcutInfoCompat)
            shortcutAction.onUpdateAction(updatePinShortcut)
        } else {
            val bundle = Bundle()
            bundle.putString(Shortcut.EXTRA_ID, shortcutInfoCompat.id)
            bundle.putString(Shortcut.EXTRA_LABEL, shortcutInfoCompat.shortLabel.toString())
            val defaultIntentSender = IntentSenderHelper.getDefaultIntentSender(context, NormalCreateBroadcastReceiver.ACTION, NormalCreateBroadcastReceiver::class.java, bundle)
            val requestPinShortcut = ShortcutManagerCompat.requestPinShortcut(context, shortcutInfoCompat, defaultIntentSender)
            shortcutAction.onCreateAction(requestPinShortcut, check, DefaultExecutor(context))
        }
    }

}