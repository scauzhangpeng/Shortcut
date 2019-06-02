package com.muugi.shortcut.sample.bean;

import com.muugi.shortcut.sample.IconTypeEnum;

/**
 * Created by ZP on 2019/3/3.
 */
public class Contact {

    private String uid;

    private String photoUrl;

    private int defaultDrawable;

    private String nickname;

    private @IconTypeEnum.IconType
    int iconType = IconTypeEnum.NORMAL;

    private boolean isShowBadged = false;

    public Contact(String uid, String photoUrl, int defaultDrawable, String nickname) {
        this.uid = uid;
        this.photoUrl = photoUrl;
        this.defaultDrawable = defaultDrawable;
        this.nickname = nickname;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public int getDefaultDrawable() {
        return defaultDrawable;
    }

    public void setDefaultDrawable(int defaultDrawable) {
        this.defaultDrawable = defaultDrawable;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public int getIconType() {
        return iconType;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public boolean isShowBadged() {
        return isShowBadged;
    }

    public void setShowBadged(boolean showBadged) {
        isShowBadged = showBadged;
    }

    public void setIconType(int iconType) {
        this.iconType = iconType;
    }
}
