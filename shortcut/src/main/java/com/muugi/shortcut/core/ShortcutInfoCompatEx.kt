package com.muugi.shortcut.core

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.os.Build
import android.text.TextUtils
import androidx.annotation.ChecksSdkIntAtLeast
import androidx.core.content.pm.ShortcutInfoCompat
import androidx.core.content.res.ResourcesCompat
import androidx.core.graphics.drawable.IconCompat
import com.muugi.shortcut.utils.ImageUtils

fun ShortcutInfoCompat.Builder.setIcon(
    bitmap: Bitmap,
    context: Context,
    sharpWithLauncher: Boolean = true
): ShortcutInfoCompat.Builder {
    if (sharpWithLauncher && canSharpWithLauncher()) {
        setIcon(IconCompat.createWithBitmap(ImageUtils.merge(bitmap, context)))
    } else {
        setIcon(IconCompat.createWithBitmap(bitmap))
    }
    return this
}

@ChecksSdkIntAtLeast(api = Build.VERSION_CODES.O)
private fun canSharpWithLauncher(): Boolean {
    return Build.VERSION.SDK_INT >= Build.VERSION_CODES.O
}


fun ShortcutInfoCompat.Builder.setIcon(
    drawable: Drawable,
    context: Context,
    sharpWithLauncher: Boolean = true
): ShortcutInfoCompat.Builder {
    val drawable2Bitmap = ImageUtils.drawable2Bitmap(drawable)
    return setIcon(drawable2Bitmap, context, sharpWithLauncher)
}

fun ShortcutInfoCompat.Builder.setIcon(
    drawableIds: Int,
    context: Context,
    sharpWithLauncher: Boolean = true
): ShortcutInfoCompat.Builder {
    val drawable =
        ResourcesCompat.getDrawable(context.resources, drawableIds, context.applicationContext.theme)
    val drawable2Bitmap = ImageUtils.drawable2Bitmap(drawable)
    return setIcon(drawable2Bitmap, context, sharpWithLauncher)
}

fun ShortcutInfoCompat.Builder.setIntent(
    intent: Intent,
    action: String = Intent.ACTION_VIEW
): ShortcutInfoCompat.Builder {
    if (TextUtils.isEmpty(intent.action)) {
        intent.action = action
    }
    return setIntent(intent)
}

