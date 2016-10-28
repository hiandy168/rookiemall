package com.itheima.rookiemall.base;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.itheima.rookiemall.R;
import com.itheima.rookiemall.call.PullToMoreListener;

import java.util.List;

import butterknife.BindView;

/**
 * Created by lyl on 2016/10/7.
 */

public class BaseLoadMoreRecyclerAdapter<T> extends BaseRecyclerAdapter<T> {
    //加载更多类型
    protected final int FOOT_TYPE = 10;

    protected PullToMoreListener mPullAndMoreListener;


    public BaseLoadMoreRecyclerAdapter(RecyclerView recyclerView, int itemResId, List datas) {
        super(recyclerView, itemResId, datas);
    }

    @Override
    public int getItemViewType(int position) {
        return (getItemCount() - 1 == position) ? FOOT_TYPE : super.getItemViewType(position);
    }

    @Override
    public int getItemCount() {
        int itemCount = super.getItemCount();
        return itemCount == 0 ? 0 : itemCount + 1;
    }

    @Override
    public final BaseRecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == FOOT_TYPE) {
            return new LoadMoreViewHolder(parent, R.layout.item_loadmore_layout);
        }
        return super.onCreateViewHolder(parent, viewType);
    }

    @Override
    public void onBindViewHolder(BaseRecyclerViewHolder holder, int position) {
        if (position == mItemCount) {
            holder.onBindData(position, null);
        } else {
            super.onBindViewHolder(holder, position);
        }
    }

    public void setPullAndMoreListener(PullToMoreListener listener) {
        mPullAndMoreListener = listener;
    }


    public class LoadMoreViewHolder extends BaseRecyclerViewHolder {

        @BindView(R.id.progress_bar)
        ProgressBar mProgressBar;

        @BindView(R.id.tv_loading)
        TextView tvLoading;

        public LoadMoreViewHolder(ViewGroup parentView, int itemResId) {
            super(parentView, itemResId);
        }

        @Override
        protected void onBindRealData() {
            if (mPullAndMoreListener != null) {
                mPullAndMoreListener.onRefreshLoadMore(this);
            }
        }

        public void loading() {
            mProgressBar.setVisibility(View.VISIBLE);
            tvLoading.setText("加载中...");
        }

        public void loadError() {
            mProgressBar.setVisibility(View.GONE);
            tvLoading.setText("加载失败");
        }

        public void noMoreData() {
            mProgressBar.setVisibility(View.GONE);
            tvLoading.setText("没有更多数据");
        }
    }
}
