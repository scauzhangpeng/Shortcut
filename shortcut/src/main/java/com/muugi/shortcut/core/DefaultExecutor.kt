package com.muugi.shortcut.core

import android.content.Context
import com.muugi.shortcut.setting.AllRequest

open class DefaultExecutor(val context: Context): Executor {

    override fun executeSetting() {
        AllRequest(context).start()
    }
}