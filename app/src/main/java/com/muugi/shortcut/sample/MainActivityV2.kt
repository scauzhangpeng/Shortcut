package com.muugi.shortcut.sample

import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.pm.ShortcutInfoCompat
import androidx.recyclerview.widget.ConcatAdapter
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.muugi.shortcut.core.*
import com.muugi.shortcut.sample.base.BasicAdapter
import com.muugi.shortcut.sample.bean.MockData
import com.muugi.shortcut.sample.bean.MockShortcutInfo

class MainActivityV2: AppCompatActivity() {

    private val itemClick: BasicAdapter.OnItemClickListener<MockShortcutInfo> by lazy {
        BasicAdapter.OnItemClickListener<MockShortcutInfo> { _, _, t ->

            val shortcutInfoCompat: ShortcutInfoCompat = ShortcutInfoCompat.Builder(this@MainActivityV2, t.uid).run {
                setShortLabel(t.label)
                setAlwaysBadged()
                setIcon(t.defaultRes, this@MainActivityV2)
                val intentForShortcut = Intent(this@MainActivityV2, TransparentActivity::class.java).apply {
                    putExtra("name", t.label)
                    putExtra("id", t.uid)
                    putExtra("isShortcut", true)
                }
                setIntent(intentForShortcut, Intent.ACTION_VIEW)
                build()
            }



            Shortcut.singleInstance.requestPinShortcut(
                context = this@MainActivityV2,
                shortcutInfoCompat = shortcutInfoCompat, shortcutAction = action
            )
        }
    }

    private val action: ShortcutAction by lazy {
        object : ShortcutAction() {
            override fun showPermissionDialog(context: Context, check: Int, executor: Executor) {
                mPermissionDialogTip.run {
                    show(supportFragmentManager, "shortcut")
                    setOnConfirmClickListener { executor.executeSetting() }
                }
            }

            override fun onCreateAction(requestPinShortcut: Boolean, check: Int, executor: Executor) {
                mTryDialogTip.run {
                    show(supportFragmentManager, "shortcut")
                    setOnConfirmClickListener { executor.executeSetting() }
                }
            }

            override fun onUpdateAction(updatePinShortcut: Boolean) {
                Toast.makeText(this@MainActivityV2, "更新成功", Toast.LENGTH_SHORT).show()
            }
        }
    }

    val mPermissionDialogTip: ShortcutPermissionTipDialog by lazy {
        ShortcutPermissionTipDialog().apply {
            setTitle("快捷方式未开启")
            setTvContentTip("检测到权限未开启，请前往系统设置，为此应用打开\"创建桌面快捷方式\"的权限。")
        }
    }

    val mTryDialogTip: ShortcutPermissionTipDialog by lazy {
        ShortcutPermissionTipDialog().apply {
            setTitle("已尝试添加到桌面")
            setTvContentTip("若添加失败，请前往系统设置，为此应用打开\"创建桌面快捷方式\"的权限。")
        }
    }

    private val callback: Shortcut.Callback by lazy {
        object : Shortcut.Callback {
            override fun onAsyncCreate(id: String, label: String) {
                if (mTryDialogTip.isVisible) {
                    mTryDialogTip.dismiss()
                }
                if (!Build.MANUFACTURER.equals("huawei", ignoreCase = true) &&
                    !Build.MANUFACTURER.equals("samsung", ignoreCase = true)
                ) {
                    Toast.makeText(
                        this@MainActivityV2,
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
        setContentView(R.layout.activity_main)
        val rv = findViewById<RecyclerView>(R.id.rv_contact)
        rv.layoutManager = LinearLayoutManager(this)
        rv.addItemDecoration(DividerItemDecoration(this, RecyclerView.VERTICAL))
        //mock data
        val mockGroupUniqueId = MockData.mockGroupUniqueId(this)
        val mockGroupUniqueLabel = MockData.mockGroupUniqueLabel(this)
        //click
        mockGroupUniqueId.second.setOnItemClickListener(itemClick)
        mockGroupUniqueLabel.second.setOnItemClickListener(itemClick)

        val concatAdapter = ConcatAdapter(
            mockGroupUniqueId.first, mockGroupUniqueId.second,
            mockGroupUniqueLabel.first, mockGroupUniqueLabel.second
        )
        rv.adapter = concatAdapter

        Shortcut.singleInstance.addShortcutCallback(callback)
    }

    override fun onDestroy() {
        super.onDestroy()
        if (mPermissionDialogTip.isVisible) {
            mPermissionDialogTip.dismiss()
        }
        if (mTryDialogTip.isVisible) {
            mTryDialogTip.dismiss()
        }
        Shortcut.singleInstance.removeShortcutCallback(callback)
    }

    companion object {
        private const val TAG = "MainActivityV2"
    }
}