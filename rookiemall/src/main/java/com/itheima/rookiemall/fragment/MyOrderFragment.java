package com.itheima.rookiemall.fragment;

import android.support.design.widget.TabLayout;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.itheima.retrofitutils.HttpHelper;
import com.itheima.retrofitutils.HttpResponseListener;
import com.itheima.retrofitutils.RetrofitUtils;
import com.itheima.rookiemall.R;
import com.itheima.rookiemall.base.BaseFragment;
import com.itheima.rookiemall.base.BaseRecyclerAdapter;
import com.itheima.rookiemall.base.BaseRecyclerViewHolder;
import com.itheima.rookiemall.bean.MyOrderBean;
import com.itheima.rookiemall.config.Constants;
import com.itheima.rookiemall.config.Urls;
import com.itheima.rookiemall.utils.SPUtils;
import com.itheima.rookiemall.widget.CustomRecyclerView;
import com.itheima.rookiemall.widget.CustomToolBar;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;

/**
 * 我的订单
 * Created by lyl on 2016/10/15.
 */

public class MyOrderFragment extends BaseFragment {
    @BindView(R.id.tab_layout)
    TabLayout mTabLayout;
    @BindView(R.id.recycler_view)
    CustomRecyclerView mRecyclerView;
    private final String[] mTitles = {"全部", "支付成功", "代支付", "支付失败"};
    //订单状态
    int mStatus = 2;
    private BaseRecyclerAdapter<MyOrderBean> mMyOrderAdapter;


    @Override
    public int getContentViewId() {
        return R.layout.fragment_my_order;
    }

    @Override
    protected void initToolbar(CustomToolBar toolBar) {
        super.initToolbar(toolBar);
        toolBar.setTitle("我的订单");
    }

    @Override
    protected boolean isShowBackView() {
        return true;
    }

    @Override
    public void initView() {
        for (String tabTxt : mTitles) {
            mTabLayout.addTab(mTabLayout.newTab().setText(tabTxt));
        }

    }

    @Override
    public void initData() {
        requestData();
    }

    private void requestData() {
        Map<String, Object> param = new HashMap<>();
        param.put(Constants.USER_ID, SPUtils.getUserId(mContext));
        param.put(Constants.TOKEN, SPUtils.getToken(mContext));
        param.put("status", mStatus);
        RetrofitUtils.getAsync(Urls.api__order_list, param, new HttpResponseListener<List<MyOrderBean>>() {
            @Override
            public void onResponse(List<MyOrderBean> myOrderBean) {
                if (mMyOrderAdapter == null) {
                    mMyOrderAdapter = new BaseRecyclerAdapter<>(mRecyclerView, R.layout.item_my_orders_list, myOrderBean);
                } else {
                    mMyOrderAdapter.addDatas(false, myOrderBean);
                }
            }
        });

    }

    @Override
    public void initListener() {
        mTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                switch (tab.getPosition()) {
                    case 0:
                        mStatus = 2;
                        break;
                    case 1:
                        mStatus = 1;
                        break;
                    case 2:
                        mStatus = 0;
                        break;
                    case 3:
                        mStatus = -2;
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


    public static class MyOrderViewHolder extends BaseRecyclerViewHolder<MyOrderBean> {
        @BindView(R.id.txt_order_num)
        TextView mTvOrderNum;
        @BindView(R.id.txt_status)
        TextView mTvOrderStatus;
        @BindView(R.id.recycler_view)
        CustomRecyclerView mGridView;
        @BindView(R.id.txt_order_money)
        TextView mTvOrderMoney;

        GridAdapter mGridAdapter;


        public MyOrderViewHolder(ViewGroup parentView, int itemResId) {
            super(parentView, itemResId);
            mGridAdapter = new GridAdapter(mGridView, R.layout.my_imageview, null);
        }

        @Override
        protected void onBindRealData() {
            mTvOrderNum.setText(mData.orderNum);
            mTvOrderStatus.setText(getOrderStatus(mData.status));
            mTvOrderMoney.setText("实付金额：" + mData.amount);
            mGridAdapter.addDatas(false, mData.items);

        }

        class GridAdapter extends BaseRecyclerAdapter<MyOrderBean.Items> {
            public GridAdapter(RecyclerView recyclerView, int itemResId, List<MyOrderBean.Items> datas) {
                super(recyclerView, itemResId, datas);
            }

            @Override
            public BaseRecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                return new MyViewHolder(parent, mItemResId);
            }

            class MyViewHolder extends BaseRecyclerViewHolder<MyOrderBean.Items> {
                @BindView(R.id.iv)
                ImageView iv;

                public MyViewHolder(ViewGroup parentView, int itemResId) {
                    super(parentView, itemResId);
                }

                @Override
                protected void onBindRealData() {
                    Picasso.with(mContext).load(mData.wares.imgUrl).into(iv);
                }
            }
        }
    }

    public static String getOrderStatus(int status) {
        String statusStr = "";
        switch (status) {
            case 0:
                statusStr = "待支付";
                break;
            case 1:
                statusStr = "成功";
                break;
            case -2:
                statusStr = "支付失败";
                break;
        }
        return statusStr;
    }

}
