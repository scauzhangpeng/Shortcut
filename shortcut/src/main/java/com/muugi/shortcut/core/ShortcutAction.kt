package com.muugi.shortcut.core

import android.content.Context

abstract class ShortcutAction {

    abstract fun showPermissionDialog(context: Context, check: Int, executor: Executor)
    abstract fun onCreateAction(requestPinShortcut: Boolean, check: Int, executor: Executor)
    abstract fun onUpdateAction(updatePinShortcut: Boolean)
}