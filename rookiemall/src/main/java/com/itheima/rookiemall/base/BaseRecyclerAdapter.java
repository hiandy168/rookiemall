package com.itheima.rookiemall.base;

import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import com.itheima.rookiemall.base.BaseRecyclerViewHolder;
import com.itheima.rookiemall.viewholder.ViewHolderFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lyl on 2016/10/3.
 */

public class BaseRecyclerAdapter<T> extends RecyclerView.Adapter<BaseRecyclerViewHolder> {
    protected List<T> mDatas;
    protected int mItemCount;
    protected int mItemResId;

    public BaseRecyclerAdapter(RecyclerView recyclerView, int itemResId, List<T> datas) {
        mDatas = datas != null ? datas : new ArrayList<T>();
        mItemResId = itemResId;
        if(recyclerView != null){
            recyclerView.setAdapter(this);
        }
    }

    @Override
    public BaseRecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return ViewHolderFactory.onCreateRealViewHolder(parent, mItemResId);
    }

    @Override
    public void onBindViewHolder(BaseRecyclerViewHolder holder, int position) {
        holder.onBindData(position, mDatas.get(position));
    }

    public void addDatas(boolean isLoadMore, List<T> datas) {
        if (!isLoadMore) {
            mDatas.clear();
        }
        mDatas.addAll(datas);
        notifyDataSetChanged();
    }


    @Override
    public int getItemCount() {
        mItemCount = mDatas != null ? mDatas.size() : 0;
        return mItemCount;
    }
}
