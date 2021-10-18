package com.muugi.shortcut.sample

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.muugi.shortcut.core.Shortcut
import com.muugi.shortcut.setting.ShortcutPermission
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class ShortcutViewModel : ViewModel() {
    data class Group(var groupName: String)

    data class ShortcutInfo(var uid: String, var label: CharSequence, var drawableRes: Int)

    data class ListState(
        val list: List<Any> = emptyList()
    )

    private val _permission = MutableStateFlow("未知")
    val permissionState = _permission.asStateFlow()

    private val _uiListState = MutableStateFlow(ListState())
    val uiListState = _uiListState.asStateFlow()

    private var _showPermissionDialog = MutableStateFlow(false)
    private var _showTryDialog = MutableStateFlow(false)

    var showPermissionTipDialog = _showPermissionDialog.asStateFlow()
    var showTryDialog = _showTryDialog.asStateFlow()

    fun changeStateForPermissionTip(show: Boolean) {
        _showPermissionDialog.compareAndSet(_showPermissionDialog.value, show)
    }

    fun changeStateForTryDialog(show: Boolean) {
        _showTryDialog.value = show
    }

    fun mock() {
        val groupUid = Group("唯一性，uid不同，label相同")
        val shortcutInfo1002 = ShortcutInfo("1002", "annie", R.drawable.annie)
        val shortcutInfo1003 = ShortcutInfo("1003", "annie", R.drawable.braum)

        val groupLabel = Group("唯一性，uid相同，label不同")
        val shortcutInfo1004 = ShortcutInfo("1004", "nunu", R.drawable.nunu)
        val shortcutInfo1005 = ShortcutInfo("1004", "kayle", R.drawable.kayle)

        val list: ArrayList<Any> = ArrayList()
        list.add(groupUid)
        list.add(shortcutInfo1002)
        list.add(shortcutInfo1003)
        list.add(groupLabel)
        list.add(shortcutInfo1004)
        list.add(shortcutInfo1005)
        _uiListState.compareAndSet(_uiListState.value, ListState(list))
    }

    fun checkPermission(context: Context) {
        val state = when (ShortcutPermission.check(context)) {
            ShortcutPermission.PERMISSION_DENIED -> "已禁止"
            ShortcutPermission.PERMISSION_GRANTED -> "已同意"
            ShortcutPermission.PERMISSION_ASK -> "询问"
            ShortcutPermission.PERMISSION_UNKNOWN -> "未知"
            else -> "未知"
        }
        _permission.value = state
    }

    fun executeSetting(current: Context) {
        changeStateForPermissionTip(false)
        Shortcut.singleInstance.openSetting(current)
    }

    companion object {
        fun provideFactory(): ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                return ShortcutViewModel() as T
            }
        }
    }
}