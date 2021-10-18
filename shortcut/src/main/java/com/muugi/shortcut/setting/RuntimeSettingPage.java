/*
 * Copyright 2018 Yan Zhenjie
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.muugi.shortcut.setting;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.text.TextUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Created by YanZhenjie on 2018/4/30.
 */
public class RuntimeSettingPage {

    private static final String MIUI_VERSION_NAME = "ro.miui.ui.version.name";
    private static final String MARK = Build.MANUFACTURER.toLowerCase();

    private final Context mSource;

    public RuntimeSettingPage(Context source) {
        this.mSource = source;
    }

    /**
     * Start.
     */
    public void start() {
        Intent intent;
        if (MARK.contains("huawei")) {
            intent = huaweiApi();
        } else if (MARK.contains("xiaomi")) {
            intent = xiaomiApi(mSource);
        } else if (MARK.contains("oppo")) {
            intent = oppoApi(mSource);
        } else if (MARK.contains("vivo")) {
            intent = vivoApi(mSource);
        } else if (MARK.contains("meizu")) {
            intent = meizuApi(mSource);
        } else {
            intent = defaultApi(mSource);
        }
        try {
            mSource.startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
            intent = defaultApi(mSource);
            mSource.startActivity(intent);
        }
    }

    private static Intent defaultApi(Context context) {
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        intent.setData(Uri.fromParts("package", context.getPackageName(), null));
        return intent;
    }

    private static Intent huaweiApi() {
        Intent intent = new Intent();
        intent.setComponent(new ComponentName("com.huawei.systemmanager", "com.huawei.permissionmanager.ui.MainActivity"));
        return intent;
    }

    private static Intent xiaomiApi(Context context) {
        String version = getSystemProperty(MIUI_VERSION_NAME);
        if (TextUtils.isEmpty(version)) {
            return defaultApi(context);
        }
        int versionI;
        try {
            versionI = Integer.parseInt(version.substring(1));
        } catch (NumberFormatException e) {
            e.printStackTrace();
            return defaultApi(context);
        }
        //xiaomi note2 MIUI 9.5进行适配，需要跳转至小米自带的权限软件
        //xiaomi note2 MUUI 10.0进行适配，需要跳转至小米自带的权限软件
        if (versionI >= 9) {
            Intent intent = new Intent("miui.intent.action.APP_PERM_EDITOR");
            intent.setComponent(new ComponentName("com.miui.securitycenter", "com.miui.permcenter.permissions.PermissionsEditorActivity"));
            intent.putExtra("extra_pkgname", context.getPackageName());
            return intent;
        }

        if (versionI >= 7) {
            Intent intent = new Intent("miui.intent.action.APP_PERM_EDITOR");
            intent.putExtra("extra_pkgname", context.getPackageName());
            return intent;
        }

        return defaultApi(context);
    }

    private static Intent vivoApi(Context context) {
        Intent intent = new Intent();
        intent.putExtra("packagename", context.getPackageName());
        intent.setComponent(new ComponentName("com.vivo.permissionmanager", "com.vivo.permissionmanager.activity.SoftPermissionDetailActivity"));
        if (hasActivity(context, intent)) return intent;

        intent.setComponent(new ComponentName("com.iqoo.secure", "com.iqoo.secure.safeguard.SoftPermissionDetailActivity"));
        return intent;
    }

    private static Intent oppoApi(Context context) {
        Intent intent = new Intent();
        intent.putExtra("packageName", context.getPackageName());
        //OPPO R9S 6.0.1,由于OPPO手机在"应用列表-xx应用-权限"下的权限列表并没有创建快捷方式一项，只能到"权限隐私-创建快捷方式"中设置。
        intent.setClassName("com.oppo.launcher", "com.oppo.launcher.shortcut.ShortcutSettingsActivity");
        if (hasActivity(context, intent)) return intent;
        intent.setComponent(new ComponentName("com.color.safecenter", "com.color.safecenter.permission.PermissionManagerActivity"));
        return intent;
    }

    private static Intent meizuApi(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            return defaultApi(context);
        }
        Intent intent = new Intent("com.meizu.safe.security.SHOW_APPSEC");
        intent.putExtra("packageName", context.getPackageName());
        intent.setComponent(new ComponentName("com.meizu.safe", "com.meizu.safe.security.AppSecActivity"));
        return intent;
    }

    private static boolean hasActivity(Context context, Intent intent) {
        PackageManager packageManager = context.getPackageManager();
        return packageManager.queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY).size() > 0;
    }

    public static String getSystemProperty(String propName) {
        BufferedReader input = null;
        try {
            Process p = Runtime.getRuntime().exec("getprop " + propName);
            input = new BufferedReader(new InputStreamReader(p.getInputStream()), 1024);
            return input.readLine();
        } catch (IOException ex) {
            return "";
        } finally {
            if (input != null) {
                try {
                    input.close();
                } catch (IOException ignored) {
                }
            }
        }
    }
}