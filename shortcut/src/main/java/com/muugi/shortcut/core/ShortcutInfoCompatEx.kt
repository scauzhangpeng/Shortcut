package com.muugi.shortcut.core

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.os.Build
import android.text.TextUtils
import androidx.core.content.pm.ShortcutInfoCompat
import androidx.core.graphics.drawable.IconCompat
import com.muugi.shortcut.utils.ImageUtils

fun ShortcutInfoCompat.Builder.setIcon(
    bitmap: Bitmap,
    sharpWithLauncher: Boolean = true,
    context: Context
) {
    if (sharpWithLauncher && canSharpWithLauncher()) {
        setIcon(IconCompat.createWithBitmap(ImageUtils.merge(bitmap, context)))
    } else {
        setIcon(IconCompat.createWithBitmap(bitmap))
    }
}

private fun canSharpWithLauncher(): Boolean {
    return Build.VERSION.SDK_INT >= Build.VERSION_CODES.O
}


fun ShortcutInfoCompat.Builder.setIcon(
    drawable: Drawable,
    sharpWithLauncher: Boolean = true,
    context: Context
) {
    val drawable2Bitmap = ImageUtils.drawable2Bitmap(drawable)
    setIcon(drawable2Bitmap, sharpWithLauncher, context)
}

fun ShortcutInfoCompat.Builder.setIntent(intent: Intent, action: String = Intent.ACTION_VIEW) {
    if (TextUtils.isEmpty(intent.action)) {
        intent.action = action
    }
    setIntent(intent)
}

