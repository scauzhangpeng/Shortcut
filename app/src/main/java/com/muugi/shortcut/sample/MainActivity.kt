package com.muugi.shortcut.sample

import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.core.content.pm.ShortcutInfoCompat
import androidx.lifecycle.ViewModelProvider
import com.muugi.shortcut.core.*

class MainActivity : ComponentActivity() {

    private val shortcutViewModel: ShortcutViewModel by lazy {
        ViewModelProvider(
            viewModelStore,
            ShortcutViewModel.provideFactory()
        ).get(ShortcutViewModel::class.java)
    }

    private val action: ShortcutAction by lazy {
        object : ShortcutAction() {
            override fun showPermissionDialog(context: Context, check: Int, executor: Executor) {
                shortcutViewModel.changeStateForPermissionTip(true)
            }

            override fun onCreateAction(
                requestPinShortcut: Boolean,
                check: Int,
                executor: Executor
            ) {
                shortcutViewModel.changeStateForTryDialog(true)
            }

            override fun onUpdateAction(updatePinShortcut: Boolean) {
                Toast.makeText(this@MainActivity, "更新成功", Toast.LENGTH_SHORT).show()
            }
        }
    }


    private val callback: Shortcut.Callback by lazy {
        object : Shortcut.Callback {
            override fun onAsyncCreate(id: String, label: String) {
                shortcutViewModel.changeStateForTryDialog(false)
                if (!Build.MANUFACTURER.equals("huawei", ignoreCase = true) &&
                    !Build.MANUFACTURER.equals("samsung", ignoreCase = true)
                ) {
                    Toast.makeText(
                        this@MainActivity,
                        "创建成功，id = $id, label = $label",
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    Log.d(TAG, "onAsyncCreate: " + "系统会提示")
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MainPage {
                requestPinShortcut(it)
            }
        }

        Shortcut.singleInstance.addShortcutCallback(callback)
    }

    private fun requestPinShortcut(item: ShortcutViewModel.ShortcutInfo) {
        val shortcutInfoCompat: ShortcutInfoCompat =
            ShortcutInfoCompat.Builder(this@MainActivity, item.uid).run {
                setShortLabel(item.label)
                setAlwaysBadged()
                setIcon(item.drawableRes, this@MainActivity)
                val intentForShortcut =
                    Intent(this@MainActivity, TransparentActivity::class.java).apply {
                        putExtra("name", item.label)
                        putExtra("id", item.uid)
                        putExtra("isShortcut", true)
                    }
                setIntent(intentForShortcut, Intent.ACTION_VIEW)
                build()
            }

        Shortcut.singleInstance.requestPinShortcut(
            context = this@MainActivity,
            shortcutInfoCompat = shortcutInfoCompat, shortcutAction = action
        )
    }

    override fun onDestroy() {
        super.onDestroy()
        Shortcut.singleInstance.removeShortcutCallback(callback)
    }

    companion object {
        private const val TAG = "MainActivityV2"
    }
}