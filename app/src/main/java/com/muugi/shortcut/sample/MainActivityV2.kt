package com.muugi.shortcut.sample

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.pm.ShortcutInfoCompat

class MainActivityV2: AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ShortcutInfoCompat.Builder(this, "111")

    }
}