package com.muugi.shortcut.sample.base;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

/**
 * Created by ZP on 2017/8/8.
 */

public abstract class BasicAdapter<T> extends RecyclerView.Adapter<BasicViewHolder> {

    protected int mLayoutId;//布局id
    protected List<T> mDatas;//数据源
    protected Context mContext;//上下文
    private LayoutInflater mInflater;
    private MultipleTypeSupport<T> mMultipleTypeSupport;
    private OnItemClickListener mOnItemClickListener;
    private OnItemLongClickListener mOnItemLongClickListener;

    public BasicAdapter(List<T> datas, Context context, MultipleTypeSupport<T> multipleTypeSupport) {
        this(-1, datas, context);
        mMultipleTypeSupport = multipleTypeSupport;
    }

    public BasicAdapter(int layoutId, List<T> datas, Context context) {
        this.mContext = context;
        this.mLayoutId = layoutId;
        this.mDatas = datas;
        this.mInflater = LayoutInflater.from(mContext);
    }

    @Override
    public int getItemViewType(int position) {
        if (mMultipleTypeSupport != null) {
            return mMultipleTypeSupport.getLayoutId(mDatas, mDatas.get(position), position);
        }
        return super.getItemViewType(position);
    }

    @Override
    public BasicViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (mMultipleTypeSupport != null) {
            mLayoutId = viewType;
        }

        View itemView = mInflater.inflate(mLayoutId, parent, false);
        return new BasicViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final BasicViewHolder holder, final int position) {
        bindData(holder, mDatas.get(position), position);
        if (mOnItemClickListener != null) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mOnItemClickListener.onItemClick(holder.itemView, position);
                }
            });
        }

        if (mOnItemLongClickListener != null) {
            holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    mOnItemLongClickListener.onItemLongClick(holder.itemView, position);
                    return false;
                }
            });
        }
    }

    @Override
    public void onBindViewHolder(@NonNull final BasicViewHolder holder, final int position, @NonNull List<Object> payloads) {
        if (payloads.isEmpty()) {
            bindData(holder, mDatas.get(position), position);
        } else {
            bindItemData(holder, mDatas.get(position), position, payloads);
        }
        if (mOnItemClickListener != null) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mOnItemClickListener.onItemClick(holder.itemView, position);
                }
            });
        }

        if (mOnItemLongClickListener != null) {
            holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    mOnItemLongClickListener.onItemLongClick(holder.itemView, position);
                    return false;
                }
            });
        }
    }

    protected abstract void bindData(BasicViewHolder holder, T t, int position);

    protected void bindItemData(BasicViewHolder holder, T t, int position, @NonNull List<Object> payloads) {

    }

    @Override
    public int getItemCount() {
        return mDatas.size();
    }

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    public interface OnItemLongClickListener {
        void onItemLongClick(View view, int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mOnItemClickListener = listener;
    }

    public void setOnItemLongClickListener(OnItemLongClickListener listener) {
        mOnItemLongClickListener = listener;
    }
}
