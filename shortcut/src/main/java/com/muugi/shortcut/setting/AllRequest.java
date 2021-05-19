package com.muugi.shortcut.setting;

import android.content.Context;

/**
 * Created by ZP on 2019-06-16.
 */
public class AllRequest implements SettingRequest {

    private final Context mContext;

    public AllRequest(Context context) {
        mContext = context;
    }

    @Override
    public void start() {
        RuntimeSettingPage runtimeSettingPage = new RuntimeSettingPage(mContext);
        runtimeSettingPage.start();
    }
}
