package com.muugi.shortcut.sample

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.muugi.shortcut.core.Shortcut
import com.muugi.shortcut.setting.ShortcutPermission

/**
 * Created by ZP on 2019-06-16.
 */
class WelcomeActivity : ComponentActivity() {

    val shortcutViewModel by lazy {
        ShortcutViewModel()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Column {
                val collectAsState = shortcutViewModel.permissionState.collectAsState()
                ItemAction("创建桌面快捷方式权限", collectAsState.value) {
                    Shortcut.singleInstance.openSetting(this@WelcomeActivity)
                }

                ItemAction("预览桌面快捷方式", "预览") {
                    goToPreview()
                }
                Button(
                    colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xFF00574B)),
                    modifier = Modifier.fillMaxWidth().height(56.dp).padding(10.dp), onClick = {
                        goToCreateShortcut()
                    }) {
                    Text(text = "创建快捷方式", color = Color.White, fontSize = 16.sp)
                }
            }
        }
        App.isLogined = true
    }

    @Composable
    fun ItemAction(actionName: String, actionState: String, onClick: () -> Unit) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(horizontal = 16.dp).fillMaxWidth().height(68.dp)
                .clickable { onClick() }) {
            Text(text = actionName, color = Color(0xFF2a2a2a), fontSize = 18.sp)
            Spacer(Modifier.fillMaxWidth().weight(1.0f))
            Text(text = actionState, color = Color(0xFFD5D5D5), fontSize = 15.sp)
        }
    }


    override fun onResume() {
        super.onResume()
        shortcutViewModel.checkPermission(this)
    }

    fun goToCreateShortcut() {
        startActivity(Intent(this, MainActivity::class.java))
    }

    fun goToPreview() {
        val intent = Intent(this, PreviewActivity::class.java)
        startActivity(intent)
    }
}