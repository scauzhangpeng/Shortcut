package com.muugi.shortcut;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.IntentSender;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Build;
import androidx.core.content.pm.ShortcutInfoCompat;
import androidx.core.graphics.drawable.IconCompat;

import com.muugi.shortcut.core.Action;
import com.muugi.shortcut.core.AutoCreateBroadcastReceiver;
import com.muugi.shortcut.pin.BaseRequest;
import com.muugi.shortcut.utils.ImageUtils;
import com.muugi.shortcut.core.IntentSenderHelper;
import com.muugi.shortcut.core.NormalCreateBroadcastReceiver;
import com.muugi.shortcut.core.ShortcutHelper;
import com.muugi.shortcut.core.ShortcutInfoExtend;
import com.muugi.shortcut.pin.InfoRequest;
import com.muugi.shortcut.pin.IntentRequest;
import com.muugi.shortcut.pin.PinOption;
import com.muugi.shortcut.setting.AllRequest;
import com.muugi.shortcut.setting.SettingRequest;

/**
 * Created by ZP on 2019-06-16.
 */
public class Shortcut implements PinOption, InfoRequest, IntentRequest {


    private static volatile Shortcut INSTANCE;

    private Action<Boolean> mActionNormalCreate;
    private Action<Boolean> mActionUpdate;
    private Action<Boolean> mActionAutoCreate;
    private Action<Boolean> mActionAsyncNormalCreate;
    private Action<Boolean> mActionAsyncAutoCreate;

    private AutoCreateBroadcastReceiver mAutoCreateBroadcastReceiver;
    private NormalCreateBroadcastReceiver mNormalCreateBroadcastReceiver;

    private ShortcutInfoExtend.Builder mBuilder;
    private Context mApplicationContext;

    private Shortcut() {

    }

    private void registerCallback(Context context) {
        mApplicationContext = checkNotNull(context);
        if (mAutoCreateBroadcastReceiver == null) {
            mAutoCreateBroadcastReceiver = new AutoCreateBroadcastReceiver();
            mAutoCreateBroadcastReceiver.setOnAutoCreateListener(new AutoCreateBroadcastReceiver.OnAutoCreateListener() {
                @Override
                public void onReceive(Context context, Intent intent) {

                }
            });
            mApplicationContext.registerReceiver(mAutoCreateBroadcastReceiver, new IntentFilter("com.shortcut.core.auto_create"));
        }

        if (mNormalCreateBroadcastReceiver == null) {
            mNormalCreateBroadcastReceiver = new NormalCreateBroadcastReceiver();
            mNormalCreateBroadcastReceiver.setOnNormalCreateListener(new NormalCreateBroadcastReceiver.OnNormalCreateListener() {
                @Override
                public void onReceive(Context context, Intent intent) {
                    if (mActionAsyncNormalCreate != null) {
                        mActionAsyncNormalCreate.onAction(true);
                    }
                }
            });
            mApplicationContext.registerReceiver(mNormalCreateBroadcastReceiver, new IntentFilter("com.shortcut.core.normal_create"));
        }
    }

    private Context checkNotNull(Context context) {
        if (context == null) {
            throw new IllegalArgumentException("context cannot be null");
        }
        context = context.getApplicationContext();
        if (context == null) {
            throw new IllegalArgumentException(
                    "context not associated with any application (using a mock context?)");
        }
        return context;
    }

    public static Shortcut get() {
        if (INSTANCE == null) {
            synchronized (Shortcut.class) {
                if (INSTANCE == null) {
                    INSTANCE = new Shortcut();
                }
            }
        }
        return INSTANCE;
    }

    public void release() {

        mActionNormalCreate = null;
        mActionAutoCreate = null;
        mActionUpdate = null;
        mActionAsyncAutoCreate = null;
        mActionAsyncNormalCreate = null;

        if (mAutoCreateBroadcastReceiver != null) {
            mAutoCreateBroadcastReceiver.setOnAutoCreateListener(null);
            mApplicationContext.unregisterReceiver(mAutoCreateBroadcastReceiver);
            mAutoCreateBroadcastReceiver = null;
        }

        if (mNormalCreateBroadcastReceiver != null) {
            mNormalCreateBroadcastReceiver.setOnNormalCreateListener(null);
            mApplicationContext.unregisterReceiver(mNormalCreateBroadcastReceiver);
            mNormalCreateBroadcastReceiver = null;
        }
    }

    public PinOption pin(Context context) {
        registerCallback(context);
        return this;
    }

    @Override
    public SettingRequest setting(Context context) {
        return new AllRequest(context);
    }

    @Override
    public InfoRequest info(String uid) {
        mBuilder = new ShortcutInfoExtend.Builder(mApplicationContext, uid);
        return this;
    }

    @Override
    public InfoRequest setShortLabel(CharSequence label) {
        mBuilder.setShortLabel(label);
        return this;
    }

    @Override
    public InfoRequest setLongLabel(CharSequence label) {
        mBuilder.setLongLabel(label);
        return this;
    }

    @Override
    public InfoRequest setDisabledMessage(CharSequence disabledMessage) {
        mBuilder.setDisabledMessage(disabledMessage);
        return this;
    }

    @Override
    public InfoRequest setIcon(Bitmap bitmap) {
        mBuilder.setIcon(bitmap);
        return this;
    }

    @Override
    public InfoRequest setIcon(Drawable drawable) {
        mBuilder.setIcon(drawable);
        return this;
    }

    @Override
    public InfoRequest setIcon(IconCompat icon) {
        mBuilder.setIcon(icon);
        return this;
    }

    @Override
    public InfoRequest setActivity(ComponentName activity) {
        mBuilder.setActivity(activity);
        return this;
    }

    @Override
    public InfoRequest setAlwaysBadge() {
        mBuilder.setAlwaysBadged();
        return this;
    }

    @Override
    public IntentRequest setIntent(Class<?> clz) {
        Intent intent = new Intent(mApplicationContext, clz);
        intent.setAction(Intent.ACTION_VIEW);
        mBuilder.setIntent(intent);
        return this;
    }

    @Override
    public IntentRequest setIntent(Intent intent) {
        if (intent.getAction() == null) {
            intent.setAction(Intent.ACTION_VIEW);
        }
        mBuilder.setIntent(intent);
        return this;
    }

    @Override
    public IntentRequest setIntent(Intent[] intents) {
        mBuilder.setIntents(intents);
        return this;
    }

    @Override
    public InfoRequest updateIfExist(boolean updateIfExist) {
        mBuilder.updateIfExist(updateIfExist);
        return this;
    }

    @Override
    public InfoRequest createWithSameName(boolean isAutoCreate) {
        mBuilder.autoCreateWithSameName(isAutoCreate);
        return this;
    }

    @Override
    public InfoRequest iconShapeWithLauncher(boolean isIconAutoShape) {
        mBuilder.iconShapeWithLauncher(isIconAutoShape);
        return this;
    }

    @Override
    public void start() {
        //icon
        Bitmap bitmap = mBuilder.getIconBitmap();
        if (mBuilder.getIconDrawable() != null) {
            bitmap = ImageUtils.drawable2Bitmap(mBuilder.getIconDrawable());
        }
        if (bitmap != null) {
            if (mBuilder.isIconShapeWithLauncher() && Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                bitmap = ImageUtils.merge(bitmap, mApplicationContext);
            }
            if (mBuilder.isAwaysBadge() && Build.MANUFACTURER.toLowerCase().equals("xiaomi")) {
                bitmap = ImageUtils.badge(bitmap, mApplicationContext);
            }
            mBuilder.setIcon(IconCompat.createWithBitmap(bitmap));

        }

        ShortcutInfoExtend shortcutInfoExtend = mBuilder.buildEx();
        ShortcutInfoCompat shortcutInfoCompat = shortcutInfoExtend.getShortcutInfoCompat();
        if (shortcutInfoExtend.isUpdateIfExist() && ShortcutHelper.isShortcutExit(mApplicationContext, shortcutInfoExtend.getId())) {
            boolean updatePinShortcut = ShortcutHelper.updatePinShortcut(mApplicationContext, shortcutInfoCompat);
            if (mActionUpdate != null) {
                mActionUpdate.onAction(updatePinShortcut);
            }
        } else {
            IntentSender defaultIntentSender = IntentSenderHelper.getDefaultIntentSender(mApplicationContext, "com.shortcut.core.normal_create");
            boolean requestPinShortcut = ShortcutHelper.requestPinShortcut(mApplicationContext, shortcutInfoCompat, defaultIntentSender);
            if (mActionNormalCreate != null) {
                mActionNormalCreate.onAction(requestPinShortcut);
            }
        }
    }

    @Override
    public BaseRequest onCreated(Action<Boolean> action) {
        mActionNormalCreate = action;
        return this;
    }

    @Override
    public BaseRequest onAsyncCreate(Action<Boolean> action) {
        mActionAsyncNormalCreate = action;
        return this;
    }

    @Override
    public BaseRequest onUpdated(Action<Boolean> action) {
        mActionUpdate = action;
        return this;
    }

    @Override
    public BaseRequest onAutoCreate(Action<Boolean> action) {
        mActionAutoCreate = action;
        return this;
    }

    @Override
    public BaseRequest onAsyncAutoCreate(Action<Boolean> action) {
        mActionAsyncAutoCreate = action;
        return this;
    }

    @Override
    public IntentRequest putExtra(String name, boolean value) {
        mBuilder.getIntent().putExtra(name, value);
        return this;
    }

    @Override
    public IntentRequest putExtra(String name, String value) {
        mBuilder.getIntent().putExtra(name, value);
        return this;
    }

    @Override
    public IntentRequest putExtra(String name, int value) {
        mBuilder.getIntent().putExtra(name, value);
        return this;
    }

    @Override
    public IntentRequest putExtra(String name, double value) {
        mBuilder.getIntent().putExtra(name, value);
        return this;
    }

    @Override
    public IntentRequest putExtra(String name, long value) {
        mBuilder.getIntent().putExtra(name, value);
        return this;
    }

    @Override
    public IntentRequest putExtra(String name, boolean[] value) {
        mBuilder.getIntent().putExtra(name, value);
        return this;
    }

    @Override
    public IntentRequest putExtra(String name, String[] value) {
        mBuilder.getIntent().putExtra(name, value);
        return this;
    }

    @Override
    public IntentRequest putExtra(String name, int[] value) {
        mBuilder.getIntent().putExtra(name, value);
        return this;
    }

    @Override
    public IntentRequest putExtra(String name, double[] value) {
        mBuilder.getIntent().putExtra(name, value);
        return this;
    }

    @Override
    public IntentRequest putExtra(String name, long[] value) {
        mBuilder.getIntent().putExtra(name, value);
        return this;
    }
}
