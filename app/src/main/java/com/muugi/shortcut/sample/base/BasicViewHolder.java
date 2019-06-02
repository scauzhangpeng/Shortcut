package com.muugi.shortcut.sample.base;

import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by ZP on 2017/8/8.
 */

public class BasicViewHolder extends RecyclerView.ViewHolder {

    private SparseArray<View> mViews;

    public BasicViewHolder(View itemView) {
        super(itemView);
        mViews = new SparseArray<>();
    }


    public <V extends View> V getView(int id) {
        View view = mViews.get(id);
        if (view == null) {
            view = itemView.findViewById(id);
            mViews.put(id, view);
        }
        return (V) view;
    }

    public BasicViewHolder setText(int id, String text) {
        TextView tv = getView(id);
        tv.setText(text);
        return this;
    }

    public BasicViewHolder setText(int id, CharSequence charSequence) {
        TextView tv = getView(id);
        tv.setText(charSequence);
        return this;
    }

    public BasicViewHolder setImageResource(int viewId, int resId) {
        ImageView view = getView(viewId);
        view.setImageResource(resId);
        return this;
    }

    public BasicViewHolder setImagePath(int viewId, AbstractImageLoader abstractImageLoader) {
        ImageView view = getView(viewId);
        abstractImageLoader.loadImage(view, abstractImageLoader.getPath());
        return this;
    }

    public BasicViewHolder setVisibility(int viewId, int visibility) {
        View view = getView(viewId);
        view.setVisibility(visibility);
        return this;
    }

    public BasicViewHolder setCheckBoxChecked(int viewId, boolean checked) {
        CheckBox view = getView(viewId);
        view.setChecked(checked);
        return this;
    }

    public BasicViewHolder setOnItemClickListener(View.OnClickListener listener) {
        itemView.setOnClickListener(listener);
        return this;
    }

    public BasicViewHolder setTextColor(int id, int color) {
        TextView view = getView(id);
        view.setTextColor(color);
        return this;
    }
}
