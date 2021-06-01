package com.muugi.shortcut.sample.bean;

import android.content.Context;
import android.util.Pair;

import com.muugi.shortcut.sample.GroupAdapter;
import com.muugi.shortcut.sample.R;
import com.muugi.shortcut.sample.ShortcutInfoAdapter;

import java.util.ArrayList;

public class MockData {

    public static Pair<GroupAdapter, ShortcutInfoAdapter> mockGroupUniqueId(Context context) {
        ArrayList<MockShortcutGroup> mockShortcutGroups = new ArrayList<>();
        mockShortcutGroups.add(new MockShortcutGroup("唯一性，uid不同，label相同"));
        GroupAdapter groupAdapter = new GroupAdapter(mockShortcutGroups, context);

        ArrayList<MockShortcutInfo> mockShortcutInfos  = new ArrayList<>();
        mockShortcutInfos.add(new MockShortcutInfo("1002", "annie", R.drawable.annie));
        mockShortcutInfos.add(new MockShortcutInfo("1003", "annie", R.drawable.braum));
        ShortcutInfoAdapter shortcutInfoAdapter = new ShortcutInfoAdapter(mockShortcutInfos, context);

        return new Pair<>(groupAdapter, shortcutInfoAdapter);
    }


    public static Pair<GroupAdapter, ShortcutInfoAdapter> mockGroupUniqueLabel(Context context) {
        ArrayList<MockShortcutGroup> mockShortcutGroups = new ArrayList<>();
        mockShortcutGroups.add(new MockShortcutGroup("唯一性，uid相同，label不同"));
        GroupAdapter groupAdapter = new GroupAdapter(mockShortcutGroups, context);

        ArrayList<MockShortcutInfo> mockShortcutInfos  = new ArrayList<>();
        mockShortcutInfos.add(new MockShortcutInfo("1004", "nunu", R.drawable.nunu));
        mockShortcutInfos.add(new MockShortcutInfo("1004", "kayle", R.drawable.kayle));
        ShortcutInfoAdapter shortcutInfoAdapter = new ShortcutInfoAdapter(mockShortcutInfos, context);

        return new Pair<>(groupAdapter, shortcutInfoAdapter);
    }


}
