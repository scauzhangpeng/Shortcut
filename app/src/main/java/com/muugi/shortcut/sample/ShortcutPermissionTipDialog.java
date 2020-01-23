package com.muugi.shortcut.sample;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

/**
 * Created by ZP on 2019-12-03.
 */
public class ShortcutPermissionTipDialog extends DialogFragment {

    private Button mBtnCancel;
    private Button mBtnConfirm;
    private CharSequence mTitle;
    private CharSequence mContentTip;
    private View.OnClickListener mOnClickListener;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_shortcut_permission_tip, container, false);
        initViews(view);
        return view;
    }

    private void initViews(View view) {
        mBtnCancel = view.findViewById(R.id.btn_cancel);
        mBtnConfirm = view.findViewById(R.id.btn_confirm);
        mBtnCancel.setOnClickListener(v -> dismiss());
        mBtnConfirm.setOnClickListener(v -> {
            if (mOnClickListener != null) {
                mOnClickListener.onClick(v);
            }
            dismiss();
        });
        TextView tvTitle = view.findViewById(R.id.tv_title);
        if (TextUtils.isEmpty(mTitle)) {
            tvTitle.setVisibility(View.GONE);
        } else {
            tvTitle.setText(mTitle);
            tvTitle.setVisibility(View.VISIBLE);
        }

        TextView tvContentTip = view.findViewById(R.id.tv_content_tip);
        if (TextUtils.isEmpty(mContentTip)) {
            tvContentTip.setVisibility(View.GONE);
        } else {
            tvContentTip.setText(mContentTip);
            tvContentTip.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setCancelable(false);
        if (getDialog() != null) {
            Window window = getDialog().getWindow();
            if (window != null) {
                window.setGravity(Gravity.CENTER);
//                window.setWindowAnimations(R.style.animate_dialog);
                window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                window.setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            }
        }
    }

    public void setOnConfirmClickListener(View.OnClickListener listener) {
        mOnClickListener = listener;
    }

    public void setTitle(CharSequence charSequence) {
        mTitle = charSequence;
    }

    public void setTvContentTip(CharSequence charSequence) {
        mContentTip = charSequence;
    }
}
