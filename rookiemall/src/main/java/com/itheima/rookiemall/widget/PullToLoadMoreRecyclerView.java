package com.itheima.rookiemall.widget;

import android.support.v4.widget.SwipeRefreshLayout;

import com.itheima.retrofitutils.HttpHelper;
import com.itheima.retrofitutils.HttpResponseListener;
import com.itheima.retrofitutils.RetrofitUtils;
import com.itheima.rookiemall.R;
import com.itheima.rookiemall.base.BaseLoadMoreRecyclerAdapter;
import com.itheima.rookiemall.bean.BasePageBean;
import com.itheima.rookiemall.call.PullToMoreListener;

import java.util.HashMap;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;

/**
 * 
 * Created by lyl on 2016/10/7.
 */

public abstract class PullToLoadMoreRecyclerView<HttpResponseBean extends BasePageBean> implements PullToMoreListener {

    private SwipeRefreshLayout mSwipeRefreshLayout;
    private CustomRecyclerView mRecyclerView;

    public PullToLoadMoreRecyclerView(SwipeRefreshLayout swipeRefreshLayout, CustomRecyclerView recyclerView) {
        mSwipeRefreshLayout = swipeRefreshLayout;
        mRecyclerView = recyclerView;
        initView();
        initData();
    }

    private void initView() {
        if (mSwipeRefreshLayout != null) {
            mSwipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary);
            mSwipeRefreshLayout.setOnRefreshListener(this);
        }
    }

    public void setSpanCount(int spanCount) {
        mRecyclerView.setSpanCount(spanCount);
    }


    protected void initData() {
        mLoadMoreRecyclerViewAdapter = new BaseLoadMoreRecyclerAdapter(mRecyclerView, getItemResId(), null);
        mLoadMoreRecyclerViewAdapter.setPullAndMoreListener(this);
    }


    public int mCurPage = 1;
    public int mPageSize = 20;

    public int mTotalPage = 0;

    public Call mCall;
    private BaseLoadMoreRecyclerAdapter mLoadMoreRecyclerViewAdapter;

    public abstract int getItemResId();

    public abstract String getApi();


    public Map<String, Object> putParam(String key, Object value) {
        mParam.put(key, value);
        return mParam;
    }


    @Override
    public void onRefresh() {
        requestData();
    }

    @Override
    public void onRefreshLoadMore(BaseLoadMoreRecyclerAdapter.LoadMoreViewHolder holder) {
        if (mCurPage <= mTotalPage) {
            holder.loading();
            requestData(true);
        } else {
            holder.noMoreData();
            pullLoadFinish();
        }
    }

    public void requestData() {
        mCurPage = 1;
        requestData(false);
    }

    Map<String, Object> mParam = new HashMap<>();

    private void requestData(final boolean isLoadMore) {
        mParam.put("curPage", String.valueOf(mCurPage));
        mParam.put("pageSize", String.valueOf(mPageSize));
        mCall = RetrofitUtils.getAsync(getApi(), mParam, new HttpResponseListener<HttpResponseBean>() {
            @Override
            public void onResponse(HttpResponseBean responseBean) {
                mTotalPage = responseBean.totalPage;
                mCurPage++;
                mLoadMoreRecyclerViewAdapter.addDatas(isLoadMore, responseBean.getItemDatas());
                pullLoadFinish();
                if (mHttpResponseCall != null) {
                    mHttpResponseCall.onResponse(responseBean);
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable e) {
                super.onFailure(call, e);
                pullLoadFinish();
                if (mHttpResponseCall != null) {
                    mHttpResponseCall.onFailure(call, e);
                }

            }


            @Override
            public Class getClazz() {
                return PullToLoadMoreRecyclerView.this.getClass();
            }
        });

    }

    private HttpResponseListener<HttpResponseBean> mHttpResponseCall;

    public void setHttpListener(HttpResponseListener<HttpResponseBean> call) {
        mHttpResponseCall = call;
    }

    public void pullLoadFinish() {
        if (mSwipeRefreshLayout != null) {
            mSwipeRefreshLayout.setRefreshing(false);
        }
    }

    public void free() {
        mRecyclerView = null;
        if (mSwipeRefreshLayout != null) {
            mSwipeRefreshLayout.setRefreshing(false);
            mSwipeRefreshLayout = null;

        }
        if (mCall != null) {
            mCall.cancel();
            mCall = null;
        }
        mLoadMoreRecyclerViewAdapter.setPullAndMoreListener(null);
        mLoadMoreRecyclerViewAdapter = null;

        mHttpResponseCall = null;
    }


}
