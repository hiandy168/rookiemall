package com.itheima.rookiemall.ui;

import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.itheima.rookiemall.R;
import com.itheima.rookiemall.base.BaseFragment;
import com.itheima.rookiemall.base.BaseRecyclerViewHolder;
import com.itheima.rookiemall.bean.HomeHotBean;
import com.itheima.rookiemall.bean.ShoppingCar;
import com.itheima.rookiemall.config.Urls;
import com.itheima.rookiemall.utils.CarProvider;
import com.itheima.rookiemall.utils.UIUtils;
import com.itheima.rookiemall.widget.CustomRecyclerView;
import com.itheima.rookiemall.widget.CustomToolBar;
import com.itheima.rookiemall.widget.PullToLoadMoreRecyclerView;
import com.squareup.picasso.Picasso;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 热卖Fragment
 * Created by lyl on 2016/9/25.
 */

public class HomeHotFragment extends BaseFragment {

    @BindView(R.id.swipe_refresh_layout)
    SwipeRefreshLayout mSwipeRefreshLayout;
    @BindView(R.id.recycler_view)
    CustomRecyclerView mRecyclerView;
    private PullToLoadMoreRecyclerView<HomeHotBean> mPullMoreView;

    @Override
    public int getContentViewId() {
        return R.layout.layout_container;
    }

    @Override
    protected void initToolbar(CustomToolBar toolBar) {
        super.initToolbar(toolBar);
        toolBar.setShowSearchView();
    }

    @Override
    public void initView() {
        mPullMoreView = new PullToLoadMoreRecyclerView<HomeHotBean>(mSwipeRefreshLayout, mRecyclerView, HomeHotViewHolder.class) {

            @Override
            public String getApi() {
                return Urls.api_homehot_lists;
            }

            @Override
            public int getItemResId() {
                return R.layout.item_homehot_recylcerview;
            }
        };

    }

    @Override
    public void initData() {
        mPullMoreView.requestData();
    }

    @Override
    public void initListener() {


    }


    public static class HomeHotViewHolder extends BaseRecyclerViewHolder<HomeHotBean.ItemHomeHotBean> {

        @BindView(R.id.iv_icon)
        ImageView ivIcon;
        @BindView(R.id.tv_title)
        TextView tvName;
        @BindView(R.id.tv_price)
        TextView tvPrice;
//        @BindView(R.id.butt_submit)
//        Button buttonSubmit;

        public HomeHotViewHolder(ViewGroup parentView, int itemResId) {
            super(parentView, itemResId);
        }

        @Override
        public void onBindRealData() {
            Picasso.with(mContext).load(mData.imgUrl).into(ivIcon);
            tvName.setText(mData.name);
            tvPrice.setText("￥" + String.valueOf(((int) (mData.price * 100)) / 100.0));
        }

        @OnClick({R.id.butt_submit, R.id.root_view})
        void click(View v) {
            switch (v.getId()) {
                case R.id.butt_submit:
                    CarProvider.getInstance(mContext).put(mData);
                    UIUtils.showDefToast(mContext, "添加购物车成功");
                    break;
                case R.id.root_view:
                    EventBus.getDefault().post(mData);
                    break;
            }
        }
    }

    @Override
    public void free() {
        super.free();
        mPullMoreView.free();
        mPullMoreView = null;
    }
}
