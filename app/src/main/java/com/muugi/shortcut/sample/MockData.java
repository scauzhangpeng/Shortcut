package com.muugi.shortcut.sample;

import com.muugi.shortcut.sample.bean.Contact;
import com.muugi.shortcut.sample.bean.Group;

import java.util.Arrays;
import java.util.List;

/**
 * Created by ZP on 2019/3/4.
 */
public class MockData {

    static String defaultUrl = "";

    public static List<Group> loadFriendList() {
        Group[] data = new Group[] {
                new Group(0, "自定义图标"),
                new Group(new Contact("1001", "", R.drawable.pig,"迷晴丶Chole")),

                new Group(2, "唯一性，名称相同"),
                new Group(new Contact("1002", "", R.drawable.pig,"小菜🐷")),
                new Group(new Contact("1003", "", R.drawable.pig,"小菜🐷")),

                new Group(5, "唯一性，uid相同"),
                new Group(new Contact("1004", "", R.drawable.pig,"习斯特")),
                new Group(new Contact("1004", "", R.drawable.pig,"普菈德")),
        };
        return Arrays.asList(data);
    }
}
