package com.muugi.shortcut.core;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.v4.content.pm.ShortcutInfoCompat;
import android.support.v4.graphics.drawable.IconCompat;
import android.text.TextUtils;

/**
 * Created by ZP on 2019/3/2.
 */
public class ShortcutInfoHelper {

    public static class Builder {

        private ShortcutInfoCompat.Builder builder;

        public Builder(@NonNull Context context, @NonNull String id) {
            builder = new ShortcutInfoCompat.Builder(context, id);
        }

        @NonNull
        public Builder setShortLabel(@NonNull CharSequence shortLabel) {
            builder.setShortLabel(shortLabel);
            return this;
        }

        @NonNull
        public Builder setLongLabel(@NonNull CharSequence longLabel) {
            builder.setLongLabel(longLabel);
            return this;
        }

        public Builder setShortAndLongLabel(@NonNull CharSequence label) {
            builder.setShortLabel(label);
            builder.setLongLabel(label);
            return this;
        }

        @NonNull
        public Builder setDisabledMessage(@NonNull CharSequence disabledMessage) {
            builder.setDisabledMessage(disabledMessage);
            return this;
        }

        @NonNull
        public Builder setIcon(IconCompat icon) {
            builder.setIcon(icon);
            return this;
        }

        public Builder setIcon(@NonNull Context context, @DrawableRes int drawableRes) {
            builder.setIcon(IconCompat.createWithResource(context, drawableRes));
            return this;
        }

        public Builder setIcon(@NonNull Context context, @NonNull Drawable drawable) {
            Bitmap bitmap = ((BitmapDrawable) drawable).getBitmap();
            builder.setIcon(IconCompat.createWithBitmap(bitmap));
            return this;
        }


        public Builder setIcon(Bitmap bitmap) {
            builder.setIcon(IconCompat.createWithBitmap(bitmap));
            return this;
        }

        @NonNull
        public Builder setIntent(@NonNull Intent intent) {
            if (TextUtils.isEmpty(intent.getAction())) {
                intent.setAction(Intent.ACTION_VIEW);
            }
            builder.setIntent(intent);
            return this;
        }

        @NonNull
        public Builder setIntents(@NonNull Intent[] intents) {
            builder.setIntents(intents);
            return this;
        }

        public Builder setAlwaysBadged() {
            builder.setAlwaysBadged();
            return this;
        }

        @NonNull
        public Builder setActivity(@NonNull ComponentName activity) {
            builder.setActivity(activity);
            return this;
        }

        @NonNull
        public ShortcutInfoCompat build() {
            return builder.build();
        }
    }
}
