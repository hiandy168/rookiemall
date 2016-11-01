package com.itheima.rookiemall.ui;

import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.TextSliderView;
import com.itheima.retrofitutils.HttpHelper;
import com.itheima.retrofitutils.HttpResponseListener;
import com.itheima.retrofitutils.RetrofitUtils;
import com.itheima.rookiemall.R;
import com.itheima.rookiemall.base.BaseFragment;
import com.itheima.rookiemall.base.BaseRecyclerAdapter;
import com.itheima.rookiemall.base.BaseRecyclerViewHolder;
import com.itheima.rookiemall.bean.HomeBannerBean;
import com.itheima.rookiemall.bean.HomeHotBean;
import com.itheima.rookiemall.config.Urls;
import com.itheima.rookiemall.widget.CustomRecyclerView;
import com.itheima.rookiemall.widget.CustomToolBar;
import com.itheima.rookiemall.widget.PullToLoadMoreRecyclerView;
import com.squareup.picasso.Picasso;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import retrofit2.Call;

/**
 * 分类Fragment
 * Created by lyl on 2016/9/25.
 */

public class HomeTypeFragment extends BaseFragment {


    @BindView(R.id.swipe_refresh_layout)
    SwipeRefreshLayout mSwipeRefreshLayout;

    @BindView(R.id.left_recycler_view)
    CustomRecyclerView mLeftRecyclerView;

    @BindView(R.id.right_recycler_view)
    CustomRecyclerView mRightRecyclerView;


    @BindView(R.id.slider_layout)
    SliderLayout mSliderLayout;

    Call mCall;
    private BaseRecyclerAdapter mLeftAdapter;
    private PullToLoadMoreRecyclerView mPullToLoadMoreRecyclerView;

    @Override
    protected void initToolbar(CustomToolBar toolBar) {
        super.initToolbar(toolBar);
        toolBar.setTitle("分类");
    }

    @Override
    public int getContentViewId() {
        return R.layout.home_type_fragment;
    }

    @Override
    protected boolean isRegisterEventBus() {
        return true;
    }

    @Override
    public void initView() {
        mPullToLoadMoreRecyclerView = new PullToLoadMoreRecyclerView<HomeHotBean>(mSwipeRefreshLayout, mRightRecyclerView) {
            @Override
            public int getItemResId() {
                return R.layout.item_hometype_right_recylerview;
            }

            @Override
            public String getApi() {
                return Urls.api_hometype_right_lists;
            }
        };

//        mSliderLayout = new SliderLayout(mContext);
//        mSliderLayout.setClickable(true);
//        mSliderLayout.setLayoutParams(new RelativeLayout.LayoutParams(-1, SizeUtils.dp2px(mContext, 120)));
//        mPullToLoadMoreRecyclerView.addHeader(mSliderLayout);

    }

    @Override
    public void initData() {
//        mRightAdapter = new BaseRecyclerAdapter(mGridRecyclerView, R.layout.item_hometype_right_recylerview, null);
//        requestLeftData();
        requestLeftData();
        requestBanner();


    }

    @Override
    public void initListener() {
    }


    public void requestLeftData() {
        mCall = RetrofitUtils.getAsync(Urls.api_hometype_left_lists, null, new HttpResponseListener<List<LeftBean>>() {
            @Override
            public void onResponse(List<LeftBean> leftBeen) {
                mLeftAdapter = new BaseRecyclerAdapter(mLeftRecyclerView, R.layout.item_hometype_left, leftBeen);
                requestRightData(leftBeen.get(0).id);
            }
        });
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void requestRightData(Integer categoryId) {
        mLeftAdapter.notifyDataSetChanged();
        mPullToLoadMoreRecyclerView.putParam("categoryId", String.valueOf(categoryId));
        mPullToLoadMoreRecyclerView.requestData();
    }


    public void requestBanner() {
        Map<String, Object> param = new HashMap<>();
        param.put("type", String.valueOf(1));
        RetrofitUtils.getAsync(Urls.api_homepage_banner, param, new HttpResponseListener<List<HomeBannerBean>>() {
            @Override
            public void onResponse(List<HomeBannerBean> homeBannerBeans) {
                for (HomeBannerBean bannerBean : homeBannerBeans) {
                    TextSliderView textSliderView = new TextSliderView(getContext());
                    textSliderView.description(bannerBean.name)
                            .image(bannerBean.imgUrl);
                    mSliderLayout.addSlider(textSliderView);
                }
                mSliderLayout.startAutoCycle();
            }
        });

    }


    public static class MyRightViewHolder extends BaseRecyclerViewHolder<HomeHotBean.ItemHomeHotBean> {
        @BindView(R.id.iv_icon)
        ImageView ivIcon;
        @BindView(R.id.tv_name)
        TextView tvName;
        @BindView(R.id.tv_price)
        TextView tvPrice;

        public MyRightViewHolder(ViewGroup parentView, int itemResId) {
            super(parentView, itemResId);
        }

        @Override
        public void onBindRealData() {
            Picasso.with(mContext).load(mData.imgUrl).into(ivIcon);
            tvName.setText(mData.name);
            tvPrice.setText("￥" + String.valueOf(mData.price));
        }

    }

    static int mClickPosition;

    public static class MyLeftViewHolder extends BaseRecyclerViewHolder<LeftBean> {
        @BindView(R.id.tv_name)
        TextView tvName;

        public MyLeftViewHolder(ViewGroup parentView, int itemResId) {
            super(parentView, itemResId);
        }

        @Override
        public void onBindRealData() {
            tvName.setText(mData.name);
            tvName.setSelected(mPosition == mClickPosition);
        }

        @OnClick(R.id.tv_name)
        public void click(View v) {
            int clickPosition = getAdapterPosition();
            mClickPosition = clickPosition;
            EventBus.getDefault().post(mData.id);

        }
    }


    public class LeftBean {
        public int id;
        public String name;
        public int sort;
    }

    @Override
    public void onStop() {
        super.onStop();
        mSliderLayout.stopAutoCycle();
    }

    @Override
    public void free() {
        super.free();
        mLeftAdapter = null;
        mSliderLayout = null;
        mLeftRecyclerView.setAdapter(null);
        mLeftRecyclerView = null;
        mPullToLoadMoreRecyclerView.free();
        mPullToLoadMoreRecyclerView = null;

        if (mCall != null && !mCall.isCanceled()) {
            mCall.cancel();
            mCall = null;
        }
    }
}
