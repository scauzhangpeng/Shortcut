package com.muugi.shortcut.core

import android.content.Context
import androidx.core.content.pm.ShortcutInfoCompat
import com.muugi.shortcut.setting.AllRequest
import com.muugi.shortcut.setting.ShortcutPermission
import com.muugi.shortcut.special.CreateAndUpdateShortcut

open class Shortcut private constructor() {
    companion object {
        val singleInstance = SingleHolder.singleHolder

        const val TAG = "Shortcut"

        const val EXTRA_ID = "extra_id"
        const val EXTRA_LABEL = "extra_label"
    }

    private object SingleHolder {
        val singleHolder = Shortcut()
    }

    private var mCallback = mutableListOf<Callback>()


    /**
     * 创建桌面快捷方式.
     * @param context [Context]
     * @param shortcutInfoCompat [ShortcutInfoCompat]
     * @param updateIfExit 如果桌面快捷方式存在，是否进行更新
     * @param fixHwOreo 是否适配华为Oreo，同名称不同id不能创建的问题
     * @param shortcutAction 回调方法
     */
    fun requestPinShortcut(
        context: Context,
        shortcutInfoCompat: ShortcutInfoCompat,
        updateIfExit: Boolean = true,
        fixHwOreo: Boolean = true,
        shortcutAction: ShortcutAction
    ) {
        //判断权限，有权限就继续，无权限返回由app控制打开设置还是取消此次操作
        val check = ShortcutPermission.check(context)
        if (check == ShortcutPermission.PERMISSION_DENIED) {
            shortcutAction.showPermissionDialog(context, check, DefaultExecutor(context))
        } else {
            //判断shortcut是否存在，如果存在则更新，不存在则创建
            val shortcutCore = if (fixHwOreo) CreateAndUpdateShortcut() else ShortcutCore()
            shortcutCore.createShortcut(context, shortcutInfoCompat, updateIfExit, shortcutAction, check)
        }
    }

    fun addShortcutCallback(callback: Callback) {
        mCallback.add(callback)
    }

    fun removeShortcutCallback(callback: Callback) {
        mCallback.remove(callback)
    }

    fun notifyCreate(id: String, name: String) {
        for (callback in mCallback) {
            callback.onAsyncCreate(id, name)
        }
    }

    interface Callback {
        fun onAsyncCreate(id: String, label: String)
    }

    fun openSetting(context: Context) {
        AllRequest(context).start()
    }
}