package com.muugi.shortcut.special

import android.content.Context
import android.content.pm.ShortcutManager
import android.os.Build
import android.os.Bundle
import androidx.core.content.pm.ShortcutInfoCompat
import androidx.core.content.pm.ShortcutManagerCompat
import com.muugi.shortcut.broadcast.IntentSenderHelper
import com.muugi.shortcut.core.DefaultExecutor
import com.muugi.shortcut.core.Shortcut
import com.muugi.shortcut.core.Shortcut.Companion.TAG
import com.muugi.shortcut.core.ShortcutAction
import com.muugi.shortcut.core.ShortcutCore
import com.muugi.shortcut.utils.Logger
import java.lang.RuntimeException

class CreateAndUpdateShortcut : ShortcutCore() {

    override fun isShortcutExit(context: Context, id: String, label: CharSequence): Boolean {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N_MR1) {
            val mShortcutManager = context.getSystemService(ShortcutManager::class.java)
                ?: return false
            var withSameName = false
            val pinnedShortcuts = mShortcutManager.pinnedShortcuts
            for (pinnedShortcut in pinnedShortcuts) {
                if (pinnedShortcut.id == id) {
                    return true
                }
                if (label == pinnedShortcut.shortLabel) {
                    withSameName = true
                }
            }
            /**
             * 华为8.0 桌面快捷方式同名称不同Id也不可以创建
             * 正常情况不同Id即可创建，华为9.0 之后修复了这个问题
             */
            if (withSameName && needCheckSameName()) {
                throw RuntimeException("huawei shortcut exit with same label")
            }
        }
        return false
    }

    /**
     * 华为8.0、8.1同label无法创建快捷方式
     * Vivo 8.1.0、9.0同label无法创建快捷方式
     */
    private fun needCheckSameName(): Boolean {
        if ("vivo".equals(Build.MANUFACTURER, false)) {
            return true
        }
        return "huawei".equals(Build.MANUFACTURER, false) && Build.VERSION.SDK_INT >= Build.VERSION_CODES.O && Build.VERSION.SDK_INT <= Build.VERSION_CODES.O_MR1
    }

    override fun createShortcut(
        context: Context,
        shortcutInfoCompat: ShortcutInfoCompat,
        updateIfExit: Boolean,
        shortcutAction: ShortcutAction,
        check: Int
    ) {
        try {
            isShortcutExit(context, shortcutInfoCompat.id, shortcutInfoCompat.shortLabel)
            super.createShortcut(context, shortcutInfoCompat, updateIfExit, shortcutAction, check)
            return
        } catch (e: Exception) {
            Logger.get().log(TAG, "huawei create shortcut error, ", e)
        }
        //华为8.0 相同的label无法创建桌面快捷方式，先创建一个加后缀"."，然后再对这个快捷方式进行更新
        val bundle = Bundle()
        bundle.putString(Shortcut.EXTRA_ID, shortcutInfoCompat.id)
        bundle.putString(Shortcut.EXTRA_LABEL, shortcutInfoCompat.shortLabel.toString())
        val defaultIntentSender = IntentSenderHelper.getDefaultIntentSender(
            context,
            AutoCreateBroadcastReceiver.ACTION,
            AutoCreateBroadcastReceiver::class.java,
            bundle
        )

        try {
            val declaredField = shortcutInfoCompat.javaClass.getDeclaredField("mLabel")
            declaredField.isAccessible = true
            declaredField.set(shortcutInfoCompat, shortcutInfoCompat.shortLabel.toString() + ".")
        } catch (e: Exception) {
            Logger.get().log(TAG, "huawei create shortcut error, ", e)
        }
        val requestPinShortcut = ShortcutManagerCompat.requestPinShortcut(
            context,
            shortcutInfoCompat,
            defaultIntentSender
        )
        shortcutAction.onCreateAction(requestPinShortcut, check, DefaultExecutor(context))
    }
}