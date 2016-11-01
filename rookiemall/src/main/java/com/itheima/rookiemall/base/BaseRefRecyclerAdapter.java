package com.itheima.rookiemall.base;

import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import com.itheima.rookiemall.utils.L;

import java.lang.reflect.Constructor;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * 通过反射创建viewHolder
 * Created by lyl on 2016/10/3.
 */

public class BaseRefRecyclerAdapter<T> extends RecyclerView.Adapter<BaseRecyclerViewHolder> {
    protected List mDatas;
    protected int mItemCount;
    protected int mItemResId;

    protected Class mViewHolderClazz;

    public BaseRefRecyclerAdapter(RecyclerView recyclerView, Class<? extends BaseRecyclerViewHolder> viewHolderClazz, int itemResId, List datas) {
        mDatas = datas != null ? datas : new ArrayList();
        mItemResId = itemResId;
        if (recyclerView != null) {
            recyclerView.setAdapter(this);
        }
        mViewHolderClazz = viewHolderClazz;
    }

    @Override
    public BaseRecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //        return ViewHolderFactory.onCreateRealViewHolder(parent, mItemResId);
        try {
            Constructor<BaseRecyclerViewHolder> con = mViewHolderClazz.getConstructor(ViewGroup.class, int.class);
            return con.newInstance(parent, mItemResId);
        } catch (Exception e) {
            L.e(e);
            return null;
        }

    }

    @Override
    public void onBindViewHolder(BaseRecyclerViewHolder holder, int position) {
        holder.onBindData(position, mDatas.get(position));
    }

    public void addDatas(boolean isLoadMore, List datas) {
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

    public Class getClazz() {
        Type mySuperClass = getClass().getGenericSuperclass();
        Type type = ((ParameterizedType) mySuperClass).getActualTypeArguments()[0];
        return type.getClass();
    }
}
