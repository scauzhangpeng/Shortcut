package com.muugi.shortcut.core;

import android.content.Context;

import com.muugi.shortcut.pin.PinOption;
import com.muugi.shortcut.setting.SettingRequest;

/**
 * Created by ZP on 2019-07-12.
 */
public interface ShortcutOption {

    PinOption pin(Context context);

    SettingRequest setting(Context context);

    void release();
}
