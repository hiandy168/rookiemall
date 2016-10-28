package com.itheima.rookiemall.call;

import android.support.v4.widget.SwipeRefreshLayout;

import com.itheima.rookiemall.base.BaseLoadMoreRecyclerAdapter;

/**
 * Created by lyl on 2016/10/7.
 */

public interface PullToMoreListener extends SwipeRefreshLayout.OnRefreshListener {
    /**
     * 加载更多
     */
    void onRefreshLoadMore(BaseLoadMoreRecyclerAdapter.LoadMoreViewHolder holder);
}
