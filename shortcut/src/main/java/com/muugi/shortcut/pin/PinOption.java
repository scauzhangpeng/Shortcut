package com.muugi.shortcut.pin;

import android.content.Context;

import com.muugi.shortcut.setting.SettingRequest;

/**
 * Created by ZP on 2019-06-16.
 */
public interface PinOption {

    SettingRequest setting(Context context);

    InfoRequest info(String uid);
}
