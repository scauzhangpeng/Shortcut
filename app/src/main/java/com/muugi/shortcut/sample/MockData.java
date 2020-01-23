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
        Group[] data = new Group[]{
                new Group(0, "唯一性，名称相同"),
                new Group(new Contact("1002", "", R.drawable.annie, "annie")),
                new Group(new Contact("1003", "", R.drawable.braum, "annie")),

                new Group(4, "唯一性，uid相同"),
                new Group(new Contact("1004", "", R.drawable.kayle, "kayle")),
                new Group(new Contact("1004", "", R.drawable.nunu, "nunu")),
        };
        return Arrays.asList(data);
    }
}
