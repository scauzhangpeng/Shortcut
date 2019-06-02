package com.muugi.shortcut.sample.bean;

/**
 * Created by ZP on 2019-06-02.
 */
public class Group {
    private int position = -1;
    private String title;
    private Contact contact;

    public Group(int position, String title) {
        this.position = position;
        this.title = title;
    }

    public Group(Contact contact) {
        this.contact = contact;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Contact getContact() {
        return contact;
    }

    public void setContact(Contact contact) {
        this.contact = contact;
    }

    public String getNickname() {
        return contact.getNickname();
    }

    public String getUid() {
        return contact.getUid();
    }

    public int getIconType() {
        return contact.getIconType();
    }

    public void setIconType(int normal) {
        contact.setIconType(normal);
    }

    public void setShowBadged(boolean isChecked) {
        contact.setShowBadged(isChecked);
    }

    public int getDefaultDrawable() {
        return contact.getDefaultDrawable();
    }

    public boolean isShowBadged() {
        return contact.isShowBadged();
    }
}
