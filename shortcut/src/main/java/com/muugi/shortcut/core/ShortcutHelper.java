package com.muugi.shortcut.core;

import android.content.Context;
import android.content.IntentSender;
import android.content.pm.ShortcutInfo;
import android.content.pm.ShortcutManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.pm.ShortcutInfoCompat;
import android.support.v4.content.pm.ShortcutManagerCompat;

import com.muugi.shortcut.setting.RuntimeSettingPage;

import java.util.Collections;
import java.util.List;

/**
 * Created by ZP on 2019/3/2.
 */
public class ShortcutHelper {

    public static boolean requestPinShortcut(@NonNull Context context, @NonNull ShortcutInfoCompat infoCompat) {
        return ShortcutManagerCompat.requestPinShortcut(context, infoCompat, null);
    }

    public static boolean requestPinShortcut(@NonNull Context context,
                                             @NonNull ShortcutInfoCompat infoCompat, @Nullable IntentSender callback) {
        return ShortcutManagerCompat.requestPinShortcut(context, infoCompat, callback);
    }

    public static boolean requestPinShortcut(@NonNull Context context, @NonNull ShortcutInfoCompat infoCompat,
                                             @Nullable IntentSender intentSender,
                                             boolean isUpdateAuto, @Nullable ShortcutCallback callback) {
        if (isUpdateAuto && isShortcutExit(context, infoCompat.getId())) {
            boolean isUpdateSuccess = updatePinShortcut(context, infoCompat);
            if (callback != null) {
                callback.isShortcutUpdate(isUpdateSuccess);
            }
            return isUpdateSuccess;
        } else {
            boolean isCreateSuccess = requestPinShortcut(context, infoCompat, intentSender);
            if (callback != null) {
                callback.isShortcutCreate(isCreateSuccess);
            }
            return isCreateSuccess;
        }
    }


    public static boolean updatePinShortcut(Context context, ShortcutInfoCompat info) {
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.N_MR1) {
            ShortcutManager mShortcutManager =
                    context.getSystemService(ShortcutManager.class);
            if (mShortcutManager == null) {
                return false;
            }

            return mShortcutManager.updateShortcuts(Collections.singletonList(info.toShortcutInfo()));
        }
        return false;
    }

    public static boolean isShortcutExit(@NonNull Context context, @NonNull String id) {
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.N_MR1) {
            ShortcutManager mShortcutManager =
                    context.getSystemService(ShortcutManager.class);
            if (mShortcutManager == null) {
                return false;
            }

            List<ShortcutInfo> pinnedShortcuts =
                    mShortcutManager.getPinnedShortcuts();
            for (ShortcutInfo pinnedShortcut : pinnedShortcuts) {
                if (pinnedShortcut.getId().equals(id)) {
                    return true;
                }
            }
        }
        return false;
    }

    public static boolean isRequestPinShortcutSupported(@NonNull Context context) {
       return ShortcutManagerCompat.isRequestPinShortcutSupported(context);
    }

    public static void toPermissionSetting(Context context) {
        new RuntimeSettingPage(context).start();
    }

    public interface ShortcutCallback {
        void isShortcutUpdate(boolean isSuccess);

        void isShortcutCreate(boolean isSuccess);
    }

    public static int getIconMaxHeight(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N_MR1) {
            ShortcutManager mShortcutManager =
                    context.getSystemService(ShortcutManager.class);
            if (mShortcutManager == null) {
                return -1;
            }
            return mShortcutManager.getIconMaxHeight();
        }
        return -1;
    }

    public static int getIconMaxWidth(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N_MR1) {
            ShortcutManager mShortcutManager =
                    context.getSystemService(ShortcutManager.class);
            if (mShortcutManager == null) {
                return -1;
            }
            return mShortcutManager.getIconMaxWidth();
        }
        return -1;
    }
}
