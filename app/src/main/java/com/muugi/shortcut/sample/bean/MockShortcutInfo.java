package com.muugi.shortcut.sample.bean;

import androidx.annotation.DrawableRes;

public class MockShortcutInfo {
    protected String uid;

    protected CharSequence label;

    protected @DrawableRes int defaultRes;


    public MockShortcutInfo(String uid, CharSequence label, int defaultRes) {
        this.uid = uid;
        this.label = label;
        this.defaultRes = defaultRes;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public CharSequence getLabel() {
        return label;
    }

    public void setLabel(CharSequence label) {
        this.label = label;
    }

    public int getDefaultRes() {
        return defaultRes;
    }

    public void setDefaultRes(int defaultRes) {
        this.defaultRes = defaultRes;
    }
}
