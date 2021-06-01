package com.muugi.shortcut.sample;

import android.content.Context;

import com.muugi.shortcut.sample.base.BasicAdapter;
import com.muugi.shortcut.sample.base.BasicViewHolder;
import com.muugi.shortcut.sample.bean.MockShortcutInfo;

import java.util.List;

public class ShortcutInfoAdapter extends BasicAdapter<MockShortcutInfo> {

    public ShortcutInfoAdapter(List<MockShortcutInfo> datas, Context context) {
        super(R.layout.item_contact, datas, context);
    }

    @Override
    protected void bindData(BasicViewHolder holder, MockShortcutInfo mockShortcutInfo, int position) {
        holder.setText(R.id.tv_nick_name, mockShortcutInfo.getLabel());
        holder.setImageResource(R.id.iv_photo, mockShortcutInfo.getDefaultRes());
    }

    @Override
    public int getItemViewType(int position) {
        return R.layout.item_contact;
    }
}
