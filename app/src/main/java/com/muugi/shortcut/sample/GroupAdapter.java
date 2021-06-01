package com.muugi.shortcut.sample;

import android.content.Context;

import com.muugi.shortcut.sample.base.BasicAdapter;
import com.muugi.shortcut.sample.base.BasicViewHolder;
import com.muugi.shortcut.sample.bean.MockShortcutGroup;

import java.util.List;

public class GroupAdapter extends BasicAdapter<MockShortcutGroup> {

    public GroupAdapter(List<MockShortcutGroup> datas, Context context) {
        super(R.layout.item_title, datas, context);
    }

    @Override
    protected void bindData(BasicViewHolder holder, MockShortcutGroup mockShortcutGroup, int position) {
        holder.setText(R.id.tv_title, mockShortcutGroup.getGroupName());
    }

    @Override
    public int getItemViewType(int position) {
        return R.layout.item_title;
    }
}
