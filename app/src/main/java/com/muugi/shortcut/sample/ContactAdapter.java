package com.muugi.shortcut.sample;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RadioGroup;

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
                        .load(R.drawable.pig)
                        .apply(bitmapTransform(new RoundedCornersTransformation(30, 0,
                                RoundedCornersTransformation.CornerType.ALL)))
                        .into(view);
                break;

        }
        RadioGroup radioGroup = holder.getView(R.id.rg_icon_type);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.rbtn_square:
                        contact.setIconType(IconTypeEnum.NORMAL);
                        Glide.with(mContext).load(R.drawable.pig).into(view);
                        break;
                    case R.id.rbtn_circle:
                        contact.setIconType(IconTypeEnum.CropCircle);
                        Glide.with(mContext).load(R.drawable.pig).apply(new RequestOptions().circleCrop()).into(view);
                        break;
                    case R.id.rbtn_cornor:
                        contact.setIconType(IconTypeEnum.CORNER);
                        Glide.with(mContext)
                                .load(R.drawable.pig)
                                .apply(bitmapTransform(new RoundedCornersTransformation(30, 0,
                                        RoundedCornersTransformation.CornerType.ALL)))
                                .into(view);
                        break;
                }
            }
        });

        CheckBox checkBox = holder.getView(R.id.cb_badged);
        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                contact.setShowBadged(isChecked);
            }
        });

        Button btnCreate = holder.getView(R.id.btn_create);
        btnCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnItemCreateClickListener != null) {
                    mOnItemCreateClickListener.onClick(v, contact, view.getDrawable());
                }
            }
        });
    }

    private OnItemCreateClickListener mOnItemCreateClickListener;


    public interface OnItemCreateClickListener {
        void onClick(View view, Group contact, Drawable drawable);
    }

    public void setOnItemCreateClickListener(OnItemCreateClickListener listener) {
        mOnItemCreateClickListener = listener;
    }


}
