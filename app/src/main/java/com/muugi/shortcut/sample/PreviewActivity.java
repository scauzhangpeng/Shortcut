package com.muugi.shortcut.sample;

import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;

import com.bumptech.glide.Glide;
import com.muugi.shortcut.utils.ImageUtils;

/**
 * Created by ZP on 2020-01-09.
 */
public class PreviewActivity extends AppCompatActivity {

    private ImageView mIvAppIcon;
    private ImageView mIvScaleBitmap;
    private ImageView mIvMerge;
    private TextView mTvAppIcon;
    private TextView mTvAppIcon1;
    private ImageView mIvAppIcon1;
    private ImageView mIvScaleBitmap1;
    private ImageView mIvMerge1;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preview);
        initView();
        showGetApplicationIcon(ResourcesCompat.getDrawable(getResources(), R.drawable.annie, getTheme()), new Callback() {
            @Override
            public void showAppIcon(Bitmap bitmap) {
                Glide.with(PreviewActivity.this).asBitmap().load(bitmap).into(mIvAppIcon1);
            }

            @Override
            public void showScaledShortcutIcon(Bitmap scaledShortcutIcon) {
                Glide.with(PreviewActivity.this).asBitmap().load(scaledShortcutIcon).into(mIvScaleBitmap1);
            }

            @Override
            public void showMergeShortcutIcon(Bitmap mergeShortcutIcon) {
                Glide.with(PreviewActivity.this).asBitmap().load(mergeShortcutIcon).into(mIvMerge1);
            }
        });

        showLoadIconFromAppList(ResourcesCompat.getDrawable(getResources(), R.drawable.annie, getTheme()), new Callback() {
            @Override
            public void showAppIcon(Bitmap bitmap) {
                Glide.with(PreviewActivity.this).asBitmap().load(bitmap).into(mIvAppIcon);
            }

            @Override
            public void showScaledShortcutIcon(Bitmap scaledShortcutIcon) {
                Glide.with(PreviewActivity.this).asBitmap().load(scaledShortcutIcon).into(mIvScaleBitmap);
            }

            @Override
            public void showMergeShortcutIcon(Bitmap mergeShortcutIcon) {
                Glide.with(PreviewActivity.this).asBitmap().load(mergeShortcutIcon).into(mIvMerge);
            }
        });
    }

    private void showLoadIconFromAppList(Drawable shortcutIcon, Callback callback) {
        Bitmap appIcon = ImageUtils.drawable2Bitmap(ImageUtils.loadAppsInfo(this, this.getPackageManager()));
        callback.showAppIcon(appIcon);
        int width = appIcon.getWidth();
        int height = appIcon.getHeight();
        int index = 0;
        for (int i = 0; i < height; i++) {
            int pixel = appIcon.getPixel(width / 2, i);
            if (pixel != 0) {
                index = i;
                break;
            }
        }
        Bitmap scaledShortcutIcon = Bitmap.createScaledBitmap(ImageUtils.drawable2Bitmap(shortcutIcon), (width - index * 2), (height - index * 2), true);
        callback.showScaledShortcutIcon(scaledShortcutIcon);
        Bitmap mergeShortcutIcon = ImageUtils.merge1(ImageUtils.drawable2Bitmap(shortcutIcon), this);
        callback.showMergeShortcutIcon(mergeShortcutIcon);
    }


    private void showGetApplicationIcon(Drawable shortcutIcon, Callback callback) {
        try {
            Bitmap appIcon = ImageUtils.drawable2Bitmap(getPackageManager().getApplicationIcon(getPackageName()));
            callback.showAppIcon(appIcon);
            int width = appIcon.getWidth();
            int height = appIcon.getHeight();
            int index = 0;
            for (int i = 0; i < height; i++) {
                int pixel = appIcon.getPixel(width / 2, i);
                if (pixel != 0) {
                    index = i;
                    break;
                }
            }
            Bitmap scaledShortcutIcon = Bitmap.createScaledBitmap(ImageUtils.drawable2Bitmap(shortcutIcon), (width - index * 2), (height - index * 2), true);
            callback.showScaledShortcutIcon(scaledShortcutIcon);
            Bitmap mergeShortcutIcon = ImageUtils.merge(ImageUtils.drawable2Bitmap(shortcutIcon), this);
            callback.showMergeShortcutIcon(mergeShortcutIcon);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

    }


    public interface Callback {
        void showAppIcon(Bitmap bitmap);

        void showScaledShortcutIcon(Bitmap scaledShortcutIcon);

        void showMergeShortcutIcon(Bitmap mergeShortcutIcon);
    }

    private void initView() {
        mIvAppIcon = findViewById(R.id.iv_app_icon);
        mIvScaleBitmap = findViewById(R.id.iv_scale_bitmap);
        mIvMerge = findViewById(R.id.iv_merge);
        mTvAppIcon = findViewById(R.id.tv_app_icon);
        mTvAppIcon1 = findViewById(R.id.tv_app_icon1);
        mIvAppIcon1 = findViewById(R.id.iv_app_icon1);
        mIvScaleBitmap1 = findViewById(R.id.iv_scale_bitmap1);
        mIvMerge1 = findViewById(R.id.iv_merge1);
    }
}
