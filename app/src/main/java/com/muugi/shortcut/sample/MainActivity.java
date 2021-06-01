package com.muugi.shortcut.sample;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.util.Pair;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.pm.ShortcutInfoCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.ConcatAdapter;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.muugi.shortcut.core.Executor;
import com.muugi.shortcut.core.Shortcut;
import com.muugi.shortcut.core.ShortcutAction;
import com.muugi.shortcut.core.ShortcutInfoCompatExKt;
import com.muugi.shortcut.sample.base.BasicAdapter;
import com.muugi.shortcut.sample.bean.MockData;
import com.muugi.shortcut.sample.bean.MockShortcutInfo;

import org.jetbrains.annotations.NotNull;

import static androidx.recyclerview.widget.LinearLayoutManager.VERTICAL;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    private ShortcutPermissionTipDialog mTryTipDialog;

    private ShortcutPermissionTipDialog mPermissionDialog;

    private final BasicAdapter.OnItemClickListener<MockShortcutInfo> itemClick = (view, position, mockShortcutInfo) -> {
        Intent intentForShortcut = new Intent(MainActivity.this, TransparentActivity.class);
        intentForShortcut.putExtra("name", mockShortcutInfo.getLabel());
        intentForShortcut.putExtra("id", mockShortcutInfo.getUid());
        intentForShortcut.putExtra("isShortcut", true);


        ShortcutInfoCompat.Builder builder = new ShortcutInfoCompat.Builder(MainActivity.this, mockShortcutInfo.getUid())
                .setShortLabel(mockShortcutInfo.getLabel());
        ShortcutInfoCompatExKt.setIcon(builder, ResourcesCompat.getDrawable(getResources(), mockShortcutInfo.getDefaultRes(), MainActivity.this.getTheme()),
                MainActivity.this, true);
        ShortcutInfoCompatExKt.setIntent(builder, intentForShortcut, Intent.ACTION_VIEW);

        ShortcutInfoCompat shortcutInfoCompat = builder.build();
        Shortcut.Companion.getSingleInstance().requestPinShortcut(MainActivity.this,
                shortcutInfoCompat,
                true,
                true,
                new ShortcutAction() {
                    @Override
                    public void showPermissionDialog(@NotNull Context context, int check, @NotNull Executor executor) {
                        showPermissionTipDialog(executor);
                    }

                    @Override
                    public void onCreateAction(boolean requestPinShortcut, int check, @NotNull Executor executor) {
                        showTryTipDialog(executor);
                    }

                    @Override
                    public void onUpdateAction(boolean updatePinShortcut) {
                        Toast.makeText(MainActivity.this, "更新成功", Toast.LENGTH_SHORT).show();
                    }
                });
    };

    private void showPermissionTipDialog(Executor executor) {
        if (mPermissionDialog == null) {
            mPermissionDialog = new ShortcutPermissionTipDialog();
            mPermissionDialog.setTitle("快捷方式未开启");
            mPermissionDialog.setTvContentTip("检测到权限未开启，请前往系统设置，为此应用打开\"创建桌面快捷方式\"的权限。");
            mPermissionDialog.setOnConfirmClickListener(v -> executor.executeSetting());
        }
        if (mPermissionDialog != null && !mPermissionDialog.isVisible()) {
            mPermissionDialog.show(getSupportFragmentManager(), "shortcut");
        }
    }

    private void dismissPermissionTipDialog() {
        if (mPermissionDialog != null && mPermissionDialog.isVisible()) {
            mPermissionDialog.dismiss();
            mPermissionDialog = null;
        }
    }


    private void showTryTipDialog(Executor executor) {
        if (mTryTipDialog == null) {
            mTryTipDialog = new ShortcutPermissionTipDialog();
            mTryTipDialog.setTitle("已尝试添加到桌面");
            mTryTipDialog.setTvContentTip("若添加失败，请前往系统设置，为此应用打开\"创建桌面快捷方式\"的权限。");
            mTryTipDialog.setOnConfirmClickListener(v -> executor.executeSetting());
        }
        if (mTryTipDialog != null && !mTryTipDialog.isVisible()) {
            mTryTipDialog.show(getSupportFragmentManager(), "shortcut");
        }
    }

    private void dismissTryTipDialog() {
        if (mTryTipDialog != null && mTryTipDialog.isVisible()) {
            mTryTipDialog.dismiss();
            mTryTipDialog = null;
        }
    }

    private final Shortcut.Callback callback = (id, label) -> {
        dismissTryTipDialog();
        if (!Build.MANUFACTURER.equalsIgnoreCase("huawei") && !Build.MANUFACTURER.equalsIgnoreCase("samsung")) {
            Toast.makeText(MainActivity.this, "创建成功，id = " + id + ", label = " + label, Toast.LENGTH_SHORT).show();
        } else {
            Log.d(TAG, "onAsyncCreate: " + "系统会提示");
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Shortcut.Companion.getSingleInstance().addShortcutCallback(callback);
        initRvContact();
    }

    private void initRvContact() {
        Pair<GroupAdapter, ShortcutInfoAdapter> uniqueId = MockData.mockGroupUniqueId(this);
        Pair<GroupAdapter, ShortcutInfoAdapter> uniqueLabel = MockData.mockGroupUniqueLabel(this);
        ShortcutInfoAdapter uniqueIdSecond = uniqueId.second;
        ShortcutInfoAdapter uniqueLabelSecond = uniqueLabel.second;

        uniqueIdSecond.setOnItemClickListener(itemClick);
        uniqueLabelSecond.setOnItemClickListener(itemClick);
        ConcatAdapter concatAdapter = new ConcatAdapter(uniqueId.first, uniqueId.second, uniqueLabel.first, uniqueLabel.second);

        RecyclerView mRvContact = findViewById(R.id.rv_contact);
        mRvContact.setAdapter(concatAdapter);
        mRvContact.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));
        mRvContact.addItemDecoration(new DividerItemDecoration(this, VERTICAL));
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        dismissPermissionTipDialog();
        dismissTryTipDialog();
        Shortcut.Companion.getSingleInstance().removeShortcutCallback(callback);
    }
}
