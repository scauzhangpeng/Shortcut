package com.muugi.shortcut.sample;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.muugi.shortcut.sample.base.BasicAdapter;
import com.muugi.shortcut.sample.base.BasicViewHolder;
import com.muugi.shortcut.sample.base.MultipleTypeSupport;
import com.muugi.shortcut.sample.bean.Group;

import java.util.List;

import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

import static com.bumptech.glide.request.RequestOptions.bitmapTransform;
import static com.muugi.shortcut.sample.IconTypeEnum.CORNER;
import static com.muugi.shortcut.sample.IconTypeEnum.CropCircle;
import static com.muugi.shortcut.sample.IconTypeEnum.NORMAL;

/**
 * Created by ZP on 2019/3/3.
 */
public class ContactAdapter extends BasicAdapter<Group> {

    public ContactAdapter(List<Group> datas, Context context, MultipleTypeSupport<Group> multipleTypeSupport) {
        super(datas, context, multipleTypeSupport);
    }


    @Override
    protected void bindData(final BasicViewHolder holder, final Group contact, final int position) {
        if (contact.getPosition() != -1) {
            holder.setText(R.id.tv_title,contact.getTitle());
            return;
        }

        holder.setText(R.id.tv_nick_name, contact.getNickname());
        holder.setText(R.id.tv_uid, contact.getUid());
        final ImageView view = holder.getView(R.id.iv_photo);
        switch (contact.getIconType()) {
            case NORMAL:
                Glide.with(mContext)
                        .load(contact.getDefaultDrawable())
                        .into(view);
                break;
            case CropCircle:
                Glide.with(mContext)
                        .load(contact.getDefaultDrawable())
                        .apply(new RequestOptions().circleCrop())
                        .into(view);
                break;
            case CORNER:
                Glide.with(mContext)
                        .load(R.drawable.braum)
                        .apply(bitmapTransform(new RoundedCornersTransformation(30, 0,
                                RoundedCornersTransformation.CornerType.ALL)))
                        .into(view);
                break;

        }
    }


}
