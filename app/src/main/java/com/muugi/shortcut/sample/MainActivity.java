package com.muugi.shortcut.sample;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.pm.ShortcutInfoCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.muugi.shortcut.core.Executor;
import com.muugi.shortcut.core.Shortcut;
import com.muugi.shortcut.core.ShortcutAction;
import com.muugi.shortcut.core.ShortcutInfoCompatExKt;
import com.muugi.shortcut.sample.base.BasicAdapter;
import com.muugi.shortcut.sample.base.MultipleTypeSupport;
import com.muugi.shortcut.sample.bean.Contact;
import com.muugi.shortcut.sample.bean.Group;
import com.muugi.shortcut.setting.AllRequest;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import static androidx.recyclerview.widget.LinearLayoutManager.VERTICAL;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    private RecyclerView mRvContact;
    private ContactAdapter mContactAdapter;
    private List<Group> mContacts;

    private final MultipleTypeSupport<Group> mMultipleTypeSupport = (list, group, position) -> {
        if (group.getPosition() != -1) {
            return R.layout.item_title;
        }
        return R.layout.item_contact;
    };

    private ShortcutPermissionTipDialog mTryTipDialog;


    private void show() {
        if (mTryTipDialog == null) {
            mTryTipDialog = new ShortcutPermissionTipDialog();
            mTryTipDialog.setTitle("已尝试添加到桌面");
            mTryTipDialog.setTvContentTip("若添加失败，请前往系统设置，为此应用打开\"创建桌面快捷方式\"的权限。");
            mTryTipDialog.setOnConfirmClickListener(v -> new AllRequest(MainActivity.this).start());
        }
        if (mTryTipDialog != null && !mTryTipDialog.isVisible()) {
            mTryTipDialog.show(getSupportFragmentManager(), "shortcut");
        }
    }

    private void dismiss() {
        if (mTryTipDialog != null && mTryTipDialog.isVisible()) {
            mTryTipDialog.dismiss();
            mTryTipDialog = null;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d(TAG, "onCreate: ");
        Shortcut.Companion.getSingleInstance().addShortcutCallback(new Shortcut.Callback() {
            @Override
            public void onAsyncCreate(@NotNull String id, @NotNull String label) {
                dismiss();
                if (!Build.MANUFACTURER.equalsIgnoreCase("huawei") && !Build.MANUFACTURER.equalsIgnoreCase("samsung")) {
                    Toast.makeText(MainActivity.this, "创建成功，id = " + id + ", label = " + label, Toast.LENGTH_SHORT).show();
                } else {
                    Log.d(TAG, "onAsyncCreate: " + "系统会提示");
                }
            }
        });
        initRvContact();
    }

    private void initRvContact() {
        mContacts = MockData.loadFriendList();
        mContactAdapter = new ContactAdapter(mContacts, this, mMultipleTypeSupport);
        mRvContact = findViewById(R.id.rv_contact);
        mRvContact.setAdapter(mContactAdapter);
        mRvContact.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));
        mRvContact.addItemDecoration(new DividerItemDecoration(this, VERTICAL));
        mContactAdapter.setOnItemClickListener((BasicAdapter.OnItemClickListener<Group>) (view, position, group) -> {
            Contact contact = mContacts.get(position).getContact();

            Intent intentForShortcut = new Intent(MainActivity.this, TransparentActivity.class);
            intentForShortcut.putExtra("name", contact.getNickname());
            intentForShortcut.putExtra("id", contact.getUid());
            intentForShortcut.putExtra("isShortcut", true);

            ShortcutInfoCompat.Builder builder = new ShortcutInfoCompat.Builder(MainActivity.this, contact.getUid())
                    .setShortLabel(contact.getNickname());
            ShortcutInfoCompatExKt.setIcon(builder, ResourcesCompat.getDrawable(getResources(), group.getContact().getDefaultDrawable(), MainActivity.this.getTheme()), true, MainActivity.this);
            ShortcutInfoCompatExKt.setIntent(builder, intentForShortcut, Intent.ACTION_VIEW);
            ShortcutInfoCompat shortcutInfoCompat = builder.build();
            Shortcut.Companion.getSingleInstance().requestPinShortcut(MainActivity.this,
                    shortcutInfoCompat,
                    true,
                    true,
                    new ShortcutAction() {
                        @Override
                        public void showPermissionDialog(@NotNull Context context, int check, @NotNull Executor executor) {
                            ShortcutPermissionTipDialog shortcutPermissionTipDialog = new ShortcutPermissionTipDialog();
                            shortcutPermissionTipDialog.show(getSupportFragmentManager(), "shortcut");
                            shortcutPermissionTipDialog.setTitle("快捷方式未开启");
                            shortcutPermissionTipDialog.setTvContentTip("检测到权限未开启，请前往系统设置，为此应用打开\"创建桌面快捷方式\"的权限。");
                            shortcutPermissionTipDialog.setOnConfirmClickListener(v -> executor.executeSetting());
                        }

                        @Override
                        public void onCreateAction(boolean requestPinShortcut, int check) {
                            show();
                        }

                        @Override
                        public void onUpdateAction(boolean updatePinShortcut) {
                            Toast.makeText(MainActivity.this, "更新成功", Toast.LENGTH_SHORT).show();
                        }
                    });
        });
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        dismiss();
    }
}
