package com.itheima.rookiemall.fragment;

import android.support.design.widget.TabLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.view.ViewGroup;

import com.itheima.rookiemall.R;
import com.itheima.rookiemall.base.BaseFragment;
import com.itheima.rookiemall.bean.HomeHotBean;
import com.itheima.rookiemall.call.HttpResponseCall;
import com.itheima.rookiemall.config.Urls;
import com.itheima.rookiemall.utils.UIUtils;
import com.itheima.rookiemall.widget.CustomRecyclerView;
import com.itheima.rookiemall.widget.CustomToolBar;
import com.itheima.rookiemall.widget.PullToLoadMoreRecyclerView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import butterknife.BindView;

/**
 * Created by lyl on 2016/10/8.
 */

public class GoodsListDetailsFragment extends BaseFragment {
    @BindView(R.id.tab_layout)
    TabLayout mTabLayout;
    @BindView(R.id.swipe_refresh_layout)
    SwipeRefreshLayout mSwipeRefreshLayout;
    @BindView(R.id.recycler_view)
    CustomRecyclerView mRecyclerView;


    //recyclerView类型，[0:recyclerView,2:GridView]
    int reyclerViewType = 0;

    //活动id
    int mCampaignId;
    //排序
    int mOrderBy;

    private final String[] mTitles = {"默认", "销量", "价格"};
    private PullToLoadMoreRecyclerView<HomeHotBean> mPullMoreView;

    @Override
    public int getContentViewId() {
        return R.layout.fragment_home_list_detail;
    }

    @Override
    protected void initToolbar(CustomToolBar toolBar) {
        super.initToolbar(toolBar);
        toolBar.setTitle("商品列表");
        toolBar.setRightViewBackground(R.drawable.icon_list_32);
    }

    @Override
    protected boolean isShowBackView() {
        return true;
    }

    @Override
    protected void rightClick(View v) {
        mPullMoreView.setSpanCount(reyclerViewType = (reyclerViewType == 0 ? 2 : 0));
    }

    @Override
    protected boolean isRegisterEventBus() {
        return true;
    }

    @Override
    public void initView() {
        for (String tabTxt : mTitles) {
            mTabLayout.addTab(mTabLayout.newTab().setText(tabTxt));
        }

        mPullMoreView = new PullToLoadMoreRecyclerView<HomeHotBean>( mSwipeRefreshLayout, mRecyclerView) {
            @Override
            public int getItemResId() {
                return R.layout.item_homehot_recylcerview;
            }

            @Override
            public String getApi() {
                return Urls.api_home_details;
            }
        };

    }

    @Override
    public void initData() {
    }

    @Subscribe
    public void startWebViewDetailsFragment(HomeHotBean.ItemHomeHotBean hotBean) {
        unRegisterEventBus();
        EventBus.getDefault().postSticky(hotBean);
        startDetailsActivity(R.layout.fragment_webview_layout, false, false);
    }

    @Subscribe(sticky = true)
    public void onEventToCurrentView(Integer campaignId) {
        EventBus.getDefault().removeStickyEvent(campaignId);
        mCampaignId = campaignId;
        requestData();
    }

    public void requestData() {
        mPullMoreView.putParam("campaignId", mCampaignId);
        mPullMoreView.putParam("orderBy", mOrderBy);
        mPullMoreView.requestData();
    }

    @Override
    public void initListener() {
        mPullMoreView.setHttpListener(new HttpResponseCall<HomeHotBean>() {
            @Override
            public void onResponse(HomeHotBean homeHotBean) {
//                UIUtils.showToast(mContext, mTabLayout, "共有" + homeHotBean.totalCount + "件商品");
                UIUtils.showFullScreenToast(mContext, mTabLayout, "共有" + homeHotBean.totalCount + "件商品");
            }
        });
        mTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                switch (tab.getPosition()) {
                    case 0:
                        mOrderBy = 0;
                        break;
                    case 1:
                        mOrderBy = 1;
                        break;
                    case 2:
                        mOrderBy = 2;
                        break;
                }
                requestData();
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

    }

    @Override
    public void free() {
        super.free();
        if (mPullMoreView != null) {
            mPullMoreView.free();
            mPullMoreView = null;

            mTabLayout.addOnTabSelectedListener(null);
            mTabLayout.removeAllTabs();
            mTabLayout.removeAllViews();
            mTabLayout = null;
        }


    }
}
