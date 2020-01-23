package com.muugi.shortcut.core;

import android.content.Context;
import android.content.IntentSender;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.content.pm.ShortcutInfoCompatV2;
import androidx.core.graphics.drawable.IconCompat;

import com.muugi.shortcut.setting.AllRequest;
import com.muugi.shortcut.utils.ImageUtils;
import com.muugi.shortcut.utils.Logger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

/**
 * Created by ZP on 2019-12-04.
 */
public class ShortcutV2 {

    private static final String TAG = "ShortcutV2";

    private List<Callback> mCallbackList;
    private HashMap<String, ShortcutInfoCompatV2> mShortcutInfoTemp = new HashMap<>();

    public void addPinShortcutListener(Callback callback) {
        if (mCallbackList == null) {
            mCallbackList = new ArrayList<>();
        }
        mCallbackList.add(callback);
    }

    public void removePinShortcutListener(Callback callback) {
        if (mCallbackList == null) {
            return;
        }
        mCallbackList.remove(callback);
    }

    public void requestPinShortcut(@NonNull final Context context,
                                      @NonNull final ShortcutInfoCompatV2 shortcutInfoCompatV2) {
        Logger.get().log(TAG, "requestPinShortcut, shortcutInfo = " + shortcutInfoCompatV2.toString());
        if (shortcutInfoCompatV2.getIcon() == null) {
            Bitmap bitmap = shortcutInfoCompatV2.getIconBitmap();
            //if drawable not null, use first
            Drawable iconDrawable = shortcutInfoCompatV2.getIconDrawable();
            if (iconDrawable != null) {
                bitmap = ImageUtils.drawable2Bitmap(iconDrawable);
            }
            //
            if (bitmap != null) {
                if (canIconShapeWithLauncher(shortcutInfoCompatV2)) {
                    bitmap = ImageUtils.merge(bitmap, context);
                }
                shortcutInfoCompatV2.setIconCompat(IconCompat.createWithBitmap(bitmap));
            } else {
                throw new IllegalArgumentException("Shortcut should have a icon");
            }
        }

        ShortcutHelper.isShortcutExit(context, shortcutInfoCompatV2.getId(), shortcutInfoCompatV2.getShortLabel(), new ShortcutHelper.ShortcutExistCallback() {
            @Override
            public void shortcutNotExist() {
                Logger.get().log(TAG, "Shortcut not exist");
                boolean pinShortcut = createPinShortcut(shortcutInfoCompatV2, context);
                notifySyncCreate(pinShortcut);
            }

            @Override
            public void shortcutExist() {
                Logger.get().log(TAG, "Shortcut exist");
                if (shortcutInfoCompatV2.isUpdateIfExist()) {
                    Logger.get().log(TAG, "User set update if exist");
                    boolean updatePinShortcut = updatePinShortcut(shortcutInfoCompatV2, context);
                    notifySyncUpdate(updatePinShortcut);
                } else {
                    boolean pinShortcut = createPinShortcut(shortcutInfoCompatV2, context);
                    notifySyncCreate(pinShortcut);
                }
            }

            @Override
            public void shortcutExistWithHW() {
                Logger.get().log(TAG, "Shortcut exit HW");
                if (shortcutInfoCompatV2.isAutoCreateWithSameName()) {
                    Logger.get().log(TAG, "User set auto if exist on HuiWei");
                    try {
                        ShortcutInfoCompatV2 clone = (ShortcutInfoCompatV2) shortcutInfoCompatV2.clone();
                        clone.setShortLabel(shortcutInfoCompatV2.getShortLabel() + UUID.randomUUID().toString());
                        boolean autoCreatePinShortcut = autoCreatePinShortcut(shortcutInfoCompatV2, clone, context);
                        notifySyncAutoCreate(autoCreatePinShortcut);
                    } catch (Exception e) {
                        Logger.get().log(TAG, "Shortcut auto create error", e);
                        notifySyncAutoCreate(false);
                    }
                } else {
                    boolean pinShortcut = createPinShortcut(shortcutInfoCompatV2, context);
                    notifySyncCreate(pinShortcut);
                }
            }
        });
    }

    private boolean canIconShapeWithLauncher(ShortcutInfoCompatV2 shortcutInfoCompatV2) {
        return shortcutInfoCompatV2.isIconShapeWithLauncher() && Build.VERSION.SDK_INT >= Build.VERSION_CODES.O;
    }

    private boolean autoCreatePinShortcut(ShortcutInfoCompatV2 shortcutInfoCompatV2, @NonNull ShortcutInfoCompatV2 clone, @NonNull Context context) {
        mShortcutInfoTemp.put(shortcutInfoCompatV2.getId(), shortcutInfoCompatV2);
        Bundle bundle = new Bundle();
        bundle.putString("id", shortcutInfoCompatV2.getId());
        bundle.putCharSequence("label", shortcutInfoCompatV2.getShortLabel());
        bundle.putCharSequence("label_clone", clone.getShortLabel());
        IntentSender intentSender = IntentSenderHelper.getDefaultIntentSender(context, AutoCreateBroadcastReceiver.ACTION, AutoCreateBroadcastReceiver.class, bundle);
        return ShortcutHelper.requestPinShortcut(context, clone, intentSender);
    }

    private boolean createPinShortcut(@NonNull ShortcutInfoCompatV2 shortcutInfoCompatV2, @NonNull Context context) {
        Bundle bundle = new Bundle();
        bundle.putString("id", shortcutInfoCompatV2.getId());
        bundle.putCharSequence("label", shortcutInfoCompatV2.getShortLabel());
        IntentSender intentSender = IntentSenderHelper.getDefaultIntentSender(context, NormalCreateBroadcastReceiver.ACTION, NormalCreateBroadcastReceiver.class, bundle);
        return ShortcutHelper.requestPinShortcut(context, shortcutInfoCompatV2, intentSender);
    }

    private boolean updatePinShortcut(@NonNull ShortcutInfoCompatV2 shortcutInfoCompatV2, @NonNull Context context) {
        return ShortcutHelper.updatePinShortcut(context, shortcutInfoCompatV2);
    }

    protected void updateAfterCreate(Context context, String id, String label, String label_clone) {
        ShortcutInfoCompatV2 shortcutInfoCompatV2 = mShortcutInfoTemp.get(id);
        if (shortcutInfoCompatV2 == null) {
            notifyAsyncAutoCreate(false, id,label, label_clone);
            return;
        }
        boolean updatePinShortcut = updatePinShortcut(shortcutInfoCompatV2, context);
        notifyAsyncAutoCreate(updatePinShortcut, id, label, label_clone);
        mShortcutInfoTemp.remove(id);
    }

    protected void notifySyncCreate(boolean pinShortcut) {
        if (mCallbackList == null) {
            return;
        }
        for (Callback callback : mCallbackList) {
            Log.d(TAG, "notifySyncCreate: printlnprintlnprintln");
            callback.onSyncCreate(pinShortcut);
        }
    }

    protected void notifyAsyncCreate(String id, String label, String label_clone) {
        if (mCallbackList == null) {
            return;
        }
        for (Callback callback : mCallbackList) {
            callback.onAsyncCreate(id, label, label_clone);
        }
    }

    protected void notifySyncUpdate(boolean updatePinShortcut) {
        if (mCallbackList == null) {
            return;
        }
        for (Callback callback : mCallbackList) {
            callback.onSyncUpdate(updatePinShortcut);
        }
    }

    protected void notifySyncAutoCreate(boolean autoCreatePinShortcut) {
        if (mCallbackList == null) {
            return;
        }
        for (Callback callback : mCallbackList) {
            callback.onSyncAutoCreate(autoCreatePinShortcut);
        }
    }

    protected void notifyAsyncAutoCreate(boolean updatePinShortcut, String id, String label, String label_clone) {
        if (mCallbackList == null) {
            return;
        }
        for (Callback callback : mCallbackList) {
            callback.onAsyncAutoCreate(updatePinShortcut, id, label, label_clone);
        }
    }

    public void setting(Context context) {
        new AllRequest(context).start();
    }


    public interface Callback {
        void onSyncCreate(boolean pinShortcut);

        void onAsyncCreate(String id, String label, String label_clone);

        void onSyncUpdate(boolean updatePinShortcut);

        void onSyncAutoCreate(boolean autoCreatePinShortcut);

        void onAsyncAutoCreate(boolean updatePinShortcut, String id, String label, String label_clone);
    }

    private ShortcutV2() {

    }

    public static ShortcutV2 get() {
        return InnerHolder.INSTANCE;
    }

    private static final class InnerHolder {
        private static final ShortcutV2 INSTANCE = new ShortcutV2();
    }
}
