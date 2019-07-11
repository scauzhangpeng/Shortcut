package com.muugi.shortcut.core;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.pm.ShortcutInfoCompat;
import androidx.core.graphics.drawable.IconCompat;

/**
 * Created by ZP on 2019-07-05.
 */
public class ShortcutInfoExtend {

    @Nullable
    private Bitmap mIconBitmap;
    @Nullable
    private Drawable mIconDrawable;
    @Nullable
    private IconCompat mIconCompat;
    @NonNull
    private ShortcutInfoCompat mShortcutInfoCompat;

    private boolean iconShapeWithLauncher = true;
    private boolean updateIfExist = true;
    private boolean autoCreateWithSameName = true;

    @Nullable
    public Bitmap getIconBitmap() {
        return mIconBitmap;
    }

    @Nullable
    public Drawable getIconDrawable() {
        return mIconDrawable;
    }

    @Nullable
    public IconCompat getIconCompat() {
        return mIconCompat;
    }

    @NonNull
    public ShortcutInfoCompat getShortcutInfoCompat() {
        return mShortcutInfoCompat;
    }

    @NonNull
    public String getId() {
        return getShortcutInfoCompat().getId();
    }

    @NonNull
    public CharSequence getShortLabel() {
        return getShortcutInfoCompat().getShortLabel();
    }

    public boolean isIconShapeWithLauncher() {
        return iconShapeWithLauncher;
    }

    public boolean isUpdateIfExist() {
        return updateIfExist;
    }

    public boolean isAutoCreateWithSameName() {
        return autoCreateWithSameName;
    }

    private ShortcutInfoExtend(Builder builder) {
        mIconBitmap = builder.mIconBitmap;
        mIconDrawable = builder.mIconDrawable;
        mIconCompat = builder.mIconCompat;
        mShortcutInfoCompat = builder.mShortcutInfoCompat;
        iconShapeWithLauncher = builder.iconShapeWithLauncher;
        updateIfExist = builder.updateIfExist;
        autoCreateWithSameName = builder.autoCreateWithSameName;
    }


    public static final class Builder extends ShortcutInfoCompat.Builder {
        private Bitmap mIconBitmap;
        private Drawable mIconDrawable;
        private IconCompat mIconCompat;
        private ShortcutInfoCompat mShortcutInfoCompat;
        private boolean iconShapeWithLauncher;
        private boolean updateIfExist;
        private boolean autoCreateWithSameName;
        private Intent[] mIntents;

        public Builder(@NonNull Context context, @NonNull String id) {
            super(context, id);
        }

        @NonNull
        public Builder setIcon(Bitmap val) {
            mIconBitmap = val;
            mIconDrawable = null;
            mIconCompat = null;
            return this;
        }

        @NonNull
        public Builder setIcon(Drawable val) {
            mIconBitmap = null;
            mIconDrawable = val;
            mIconCompat = null;
            return this;
        }

        @NonNull
        public Builder iconShapeWithLauncher(boolean val) {
            iconShapeWithLauncher = val;
            return this;
        }

        @NonNull
        public Builder updateIfExist(boolean val) {
            updateIfExist = val;
            return this;
        }

        @NonNull
        public Builder autoCreateWithSameName(boolean val) {
            autoCreateWithSameName = val;
            return this;
        }

        @NonNull
        @Override
        public Builder setIntent(@NonNull Intent intent) {
            return this.setIntents(new Intent[]{intent});
        }

        @NonNull
        @Override
        public Builder setIntents(@NonNull Intent[] intents) {
            this.mIntents = intents;
            return this;
        }

        @NonNull
        public ShortcutInfoExtend buildEx() {
            super.setIntents(mIntents);
            mShortcutInfoCompat = super.build();
            return new ShortcutInfoExtend(this);
        }

        @Nullable
        public Bitmap getIconBitmap() {
            return mIconBitmap;
        }

        @Nullable
        public Drawable getIconDrawable() {
            return mIconDrawable;
        }

        public boolean isIconShapeWithLauncher() {
            return iconShapeWithLauncher;
        }

        @NonNull
        public Intent getIntent() {
            return this.mIntents[this.mIntents.length - 1];
        }
    }
}
