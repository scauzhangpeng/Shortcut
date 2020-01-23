package com.muugi.shortcut.sample;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.pm.ShortcutInfoCompatV2;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.muugi.shortcut.core.ShortcutV2;
import com.muugi.shortcut.sample.base.BasicAdapter;
import com.muugi.shortcut.sample.base.MultipleTypeSupport;
import com.muugi.shortcut.sample.bean.Contact;
import com.muugi.shortcut.sample.bean.Group;
import com.muugi.shortcut.setting.ShortcutPermission;
import com.muugi.shortcut.utils.Logger;

import java.util.List;

import static androidx.recyclerview.widget.LinearLayoutManager.VERTICAL;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    private RecyclerView mRvContact;
    private ContactAdapter mContactAdapter;
    private List<Group> mContacts;

    private MultipleTypeSupport<Group> mMultipleTypeSupport = (list, group, position) -> {
        if (group.getPosition() != -1) {
            return R.layout.item_title;
        }
        return R.layout.item_contact;
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d(TAG, "onCreate: ");
        ShortcutV2.get().addPinShortcutListener(mCallback);
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
            int check = ShortcutPermission.check(MainActivity.this);
            if (check == ShortcutPermission.PERMISSION_GRANTED || check == ShortcutPermission.PERMISSION_UNKNOWN) {
                Contact contact = mContacts.get(position).getContact();
                Intent intentForShortcut = new Intent(MainActivity.this, TransparentActivity.class);
                intentForShortcut.putExtra("name", contact.getNickname());
                intentForShortcut.putExtra("id", contact.getUid());
                intentForShortcut.putExtra("isShortcut", true);
                ShortcutInfoCompatV2 shortcutInfoCompatV2 = new ShortcutInfoCompatV2.Builder(MainActivity.this, contact.getUid())
                        .setShortLabel(contact.getNickname())
                        .iconShapeWithLauncher(true)
                        .autoCreateWithSameName(true)
                        .updateIfExist(true)
                        .setIcon(getResources().getDrawable(group.getContact().getDefaultDrawable()))
                        .setIntent(intentForShortcut)
                        .build();
                ShortcutV2.get().requestPinShortcut(MainActivity.this, shortcutInfoCompatV2);
            } else {
                ShortcutPermissionTipDialog shortcutPermissionTipDialog = new ShortcutPermissionTipDialog();
                shortcutPermissionTipDialog.show(getSupportFragmentManager(), "shortcut");
                shortcutPermissionTipDialog.setTitle("快捷方式未开启");
                shortcutPermissionTipDialog.setTvContentTip("检测到权限未开启，请前往系统设置，为此应用打开\"创建桌面快捷方式\"的权限。");
                shortcutPermissionTipDialog.setOnConfirmClickListener(v -> ShortcutV2.get().setting(MainActivity.this));
            }
        });
    }

    private ShortcutV2.Callback mCallback = new ShortcutV2.Callback() {
        @Override
        public void onSyncCreate(boolean pinShortcut) {
            Logger.get().log(TAG, "onSyncCreate = " + pinShortcut);
            Toast.makeText(MainActivity.this, "创建快捷方式，同步结果 = " + pinShortcut, Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onAsyncCreate(String id, String label, String label_clone) {
            Logger.get().log(TAG, "onAsyncCreate, id = " + id + ", label = " + label + ", clone = " + label_clone);
            Toast.makeText(MainActivity.this, "创建快捷方式，异步结果, id = " + id + ", label = " + label + ", clone = " + label_clone, Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onSyncUpdate(boolean updatePinShortcut) {
            Logger.get().log(TAG, "onSyncUpdate = " + updatePinShortcut);
            Toast.makeText(MainActivity.this, "更新快捷方式，异步结果 = " + updatePinShortcut, Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onSyncAutoCreate(boolean autoCreatePinShortcut) {
            Logger.get().log(TAG, "onSyncAutoCreate = " + autoCreatePinShortcut);
            Toast.makeText(MainActivity.this, "创建快捷方式，同步结果（huawei） = " + autoCreatePinShortcut, Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onAsyncAutoCreate(boolean updatePinShortcut, String id, String label, String label_clone) {
            Logger.get().log(TAG, "onAsyncAutoCreate, id = " + id + ", label = " + label + ", clone = " + label_clone);
            Toast.makeText(MainActivity.this, "创建快捷方式，异步结果（huawei）, id = " + id + ", label = " + label + ", clone = " + label_clone, Toast.LENGTH_SHORT).show();
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ShortcutV2.get().removePinShortcutListener(mCallback);
    }
}
