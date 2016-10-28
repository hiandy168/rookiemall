package com.itheima.rookiemall.ui;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.itheima.rookiemall.R;
import com.itheima.rookiemall.base.BaseActivity;
import com.itheima.rookiemall.base.BaseFragment;
import com.itheima.rookiemall.base.BaseRecyclerAdapter;
import com.itheima.rookiemall.base.BaseRecyclerViewHolder;
import com.itheima.rookiemall.bean.ShoppingCar;
import com.itheima.rookiemall.utils.CarProvider;
import com.itheima.rookiemall.utils.UIUtils;
import com.itheima.rookiemall.widget.CustomCarGoodsCounterView;
import com.itheima.rookiemall.widget.CustomToolBar;
import com.squareup.picasso.Picasso;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 购物车Fragment
 * Created by lyl on 2016/9/25.
 */

public class HomeCarFragment extends BaseFragment implements View.OnClickListener {

    @BindView(R.id.recycler_view)
    RecyclerView mRecyclerView;

    @BindView(R.id.check_box)
    CheckBox mCheckBoxIsAll;

    @BindView(R.id.tv_total_money)
    TextView mTvTotalMoney;

    @BindView(R.id.ll_middle_view)
    LinearLayout llMiddleView;

    @BindView(R.id.tv_commit)
    TextView tvCommit;
    @BindView(R.id.tv_delete)
    TextView tvDelete;

    BaseRecyclerAdapter mCarRecyclerAdapter;

    boolean isShowDeleteView;

    @Override
    public int getContentViewId() {
        return R.layout.home_car_fragment;
    }

    @Override
    protected void initToolbar(CustomToolBar toolBar) {
        super.initToolbar(toolBar);
        toolBar.setTitle("购物车");
        toolBar.setRightTxt("编辑");
    }

    @Override
    protected boolean isRegisterEventBus() {
        return true;
    }

    @Override
    public void initView() {
    }

    @Override
    public void initData() {
        mCarRecyclerAdapter = new BaseRecyclerAdapter<ShoppingCar>(mRecyclerView, R.layout.item_home_car_recyclerview, null);
    }

    @Override
    public void initListener() {
        ((BaseActivity) getActivity()).getToolBar().setRightViewClickListener(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        List<ShoppingCar> carDatas = CarProvider.getInstance(mContext).getMemoryAll();
        mCarRecyclerAdapter.addDatas(false, carDatas);
        updateCarBottomUI(null);
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void updateCarBottomUI(String s) {
        mCheckBoxIsAll.setChecked(CarProvider.getInstance(mContext).isAllChecked());
        mTvTotalMoney.setText("¥" + CarProvider.getInstance(mContext).getCarTotalPrice());
    }


    @OnClick({R.id.check_box, R.id.tv_commit, R.id.tv_delete})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.check_box:
                CarProvider.getInstance(mContext).setAllCheckedState(mCheckBoxIsAll.isChecked());
                mCarRecyclerAdapter.addDatas(false, CarProvider.getInstance(mContext).getMemoryAll());
                mCarRecyclerAdapter.notifyDataSetChanged();
                break;
            case R.id.tv_rightView:
                TextView toolBarRightView = (TextView) v;
                toolBarRightView.setTag(isShowDeleteView = !isShowDeleteView);
                if ((boolean) toolBarRightView.getTag()) {
                    showDeleteView(toolBarRightView);
                } else {
                    hideDeleteView(toolBarRightView);
                }
                mCarRecyclerAdapter.addDatas(false, CarProvider.getInstance(mContext).getMemoryAll());
                mCarRecyclerAdapter.notifyDataSetChanged();
                break;
            case R.id.tv_delete:
                CarProvider.getInstance(mContext).deleteAllSelect();
                mCarRecyclerAdapter.addDatas(false, CarProvider.getInstance(mContext).getMemoryAll());
                mCarRecyclerAdapter.notifyDataSetChanged();
                break;
            case R.id.tv_commit:
                UIUtils.showDefToast(mContext, "你点击了结算");
                startDetailsActivity(R.layout.fragment_submit_order, false, true);
                break;
            default:
                break;
        }

    }

    public void hideDeleteView(TextView toolBarRightView) {
        tvDelete.setVisibility(View.GONE);
        tvCommit.setVisibility(View.VISIBLE);
        toolBarRightView.setText("编辑");
        llMiddleView.setVisibility(View.VISIBLE);
    }

    public void showDeleteView(TextView toolBarRightView) {
        tvDelete.setVisibility(View.VISIBLE);
        tvCommit.setVisibility(View.GONE);
        toolBarRightView.setText("完成");
        llMiddleView.setVisibility(View.INVISIBLE);
        CarProvider.getInstance(mContext).setAllCheckedState(false);
    }

    public static class MyCarRecyclerViewHolder extends BaseRecyclerViewHolder<ShoppingCar> implements CustomCarGoodsCounterView.UpdateGoodsNumberListener {
        @BindView(R.id.check_box)
        CheckBox checkBox;
        @BindView(R.id.iv_icon)
        ImageView ivIcon;
        @BindView(R.id.tv_title)
        TextView tvTitle;
        @BindView(R.id.tv_price)
        TextView tvPrice;
        @BindView(R.id.car_goods_counter)
        CustomCarGoodsCounterView mCustomCarGoodsCounterView;


        public MyCarRecyclerViewHolder(ViewGroup parentView, int itemResId) {
            super(parentView, itemResId);
            mCustomCarGoodsCounterView.setUpdateGoodsNumberListener(this);
        }

        @Override
        public void onBindRealData() {
            checkBox.setChecked(mData.isChecked);
            Picasso.with(mContext).load(mData.imgUrl).into(ivIcon);
            tvTitle.setText(mData.name);
            tvPrice.setText("￥" + String.valueOf(((int) mData.price * 100) / 100.0));
            mCustomCarGoodsCounterView.setGoodsNumber(mData.count);
        }


        @OnClick(R.id.check_box)
        public void click(CheckBox v) {
            mData.isChecked = !mData.isChecked;
            CarProvider.getInstance(mContext).updateCarGoods(mData);
            checkBox.setChecked(mData.isChecked);
            EventBus.getDefault().post(new String());
        }


        @Override
        public void updateGoodsNumber(int number) {
            mData.count = number;
            CarProvider.getInstance(mContext).updateCarGoods(mData);
            EventBus.getDefault().post(new String());
        }
    }


    @Override
    public void free() {
        super.free();
        mCarRecyclerAdapter = null;
        mRecyclerView = null;
        llMiddleView = null;
    }
}
