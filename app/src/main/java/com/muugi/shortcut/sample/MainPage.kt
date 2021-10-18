package com.muugi.shortcut.sample

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun MainPage(onItemClick: (item: ShortcutViewModel.ShortcutInfo) -> Unit) {
    val shortcutViewModel: ShortcutViewModel = viewModel(
        factory = ShortcutViewModel.provideFactory()
    )

    val collectAsState = shortcutViewModel.showPermissionTipDialog.collectAsState()
    val collectAsState1 = shortcutViewModel.showTryDialog.collectAsState()

    val current = LocalContext.current
    if (collectAsState.value) {
        DialogTip("快捷方式未开启", "检测到权限未开启，请前往系统设置，为此应用打开\"创建桌面快捷方式\"的权限。", onDismiss = {
            shortcutViewModel.changeStateForPermissionTip(false)
        }, onConfirm = {
            shortcutViewModel.executeSetting(current)
        })
    }
    if (collectAsState1.value) {
        DialogTip("已尝试添加到桌面", "若添加失败，请前往系统设置，为此应用打开\"创建桌面快捷方式\"的权限。", onDismiss = {
            shortcutViewModel.changeStateForTryDialog(false)
        }) {
            shortcutViewModel.executeSetting(current)
        }
    }

    val collectAsState2 = shortcutViewModel.uiListState.collectAsState()
    LazyColumn {
        collectAsState2.value.list.forEach { item ->
            when (item) {
                is ShortcutViewModel.Group -> {
                    item {
                        ItemGroup(item.groupName)
                    }
                }

                is ShortcutViewModel.ShortcutInfo -> {
                    item {
                        ItemNormalShortcut(item.label.toString(), item.uid, item.drawableRes) {
                            onItemClick(item)
                        }
                    }
                }
            }
        }
    }
    shortcutViewModel.mock()
}

@Composable
fun ItemGroup(text: String) {
    Text(
        text = text,
        fontSize = 18.sp,
        color = Color(0xFF404040),
        modifier = Modifier.height(35.dp).fillMaxWidth().padding(horizontal = 16.dp)
            .wrapContentSize(
                Alignment.CenterStart
            )
    )
}

@Composable
fun ItemNormalShortcut(name: String, uid: String, @DrawableRes id: Int, onItemClick: () -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth().clickable(onClick = onItemClick),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = painterResource(id),
            contentDescription = null,
            modifier = Modifier.size(52.dp).padding(start = 10.dp)
        )
        Text(text = name, fontSize = 16.sp, modifier = Modifier.padding(start = 10.dp))
        Text(text = uid, modifier = Modifier.padding(start = 10.dp))
    }
}

@Composable
fun DialogTip(title: String, content: String, onDismiss: () -> Unit, onConfirm: () -> Unit) {
    Dialog(onDismissRequest = onDismiss) {
        DialogTipCore(title, content, onDismiss, onConfirm)
    }
}

@Composable
fun DialogTipCore(title: String, content: String, onDismiss: () -> Unit, onConfirm: () -> Unit) {
    Column {
        Column(Modifier.width(280.dp).background(color = Color.White)) {
            Text(
                text = title,
                modifier = Modifier.padding(top = 20.dp).fillMaxWidth(),
                fontSize = 16.sp,
                color = Color(0xFF404040),
                textAlign = TextAlign.Center
            )
            Text(
                text = content,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 10.dp)
                    .padding(bottom = 15.dp),
                fontSize = 14.sp, color = Color(0xFF404040)
            )
            Spacer(Modifier.background(color = Color(0xFFEDEDED)).fillMaxWidth().height(1.dp))
            Row {
                Button(
                    onClick = onDismiss,
                    modifier = Modifier.weight(1.0f),
                    colors = ButtonDefaults.buttonColors(backgroundColor = Color.White)
                ) {
                    Text(text = "返回")
                }
                Button(
                    onClick = onConfirm, modifier = Modifier.weight(1.0f),
                    colors = ButtonDefaults.buttonColors(backgroundColor = Color.White)
                ) {
                    Text(text = "前往设置")
                }
            }
        }
    }

}

@Preview(showSystemUi = true)
@Composable
fun PreDialog() {
    DialogTipCore("快捷方式未开启", "检测到权限未开启，请前往系统设置，为此应用打开\"创建桌面快捷方式\"的权限。", {}, {})
}