package com.muugi.shortcut.sample;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.muugi.shortcut.core.Shortcut;

import com.muugi.shortcut.setting.ShortcutPermission;

/**
 * Created by ZP on 2019-06-16.
 */
public class WelcomeActivity extends AppCompatActivity {

    private TextView mTvShortcutPermissionState;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        App.isLogined = true;
        initViews();
    }

    private void initViews() {
        mTvShortcutPermissionState = findViewById(R.id.tv_shortcut_permission_state);
        mTvShortcutPermissionState.setOnClickListener(v -> {
            Shortcut.Companion.getSingleInstance().openSetting(WelcomeActivity.this);
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        int check = ShortcutPermission.check(this);
        String state = "未知";
        switch (check) {
            case ShortcutPermission.PERMISSION_DENIED:
                state = "已禁止";
                break;
            case ShortcutPermission.PERMISSION_GRANTED:
                state = "已同意";
                break;
            case ShortcutPermission.PERMISSION_ASK:
                state = "询问";
                break;
            case ShortcutPermission.PERMISSION_UNKNOWN:
                state = "未知";
                break;
        }
        mTvShortcutPermissionState.setText(state);
    }

    public void goToCreateShortcut(View view) {
        startActivity(new Intent(this, MainActivityV2.class));
    }

    public void goToPreview(View view) {
        Intent intent = new Intent(this, PreviewActivity.class);
        startActivity(intent);
    }
}
