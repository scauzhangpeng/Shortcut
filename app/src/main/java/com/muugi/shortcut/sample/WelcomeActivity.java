package com.muugi.shortcut.sample;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

/**
 * Created by ZP on 2019-06-16.
 */
public class WelcomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        App.isLogined = true;
        findViewById(R.id.open).setOnClickListener(v -> startActivity(new Intent(WelcomeActivity.this, MainActivity.class)));
    }
}
