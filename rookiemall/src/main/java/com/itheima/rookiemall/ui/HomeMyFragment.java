package com.itheima.rookiemall.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.itheima.rookiemall.R;
import com.itheima.rookiemall.activity.DetailsActivity;
import com.itheima.rookiemall.base.BaseFragment;
import com.itheima.rookiemall.config.Constants;
import com.itheima.rookiemall.utils.SPUtils;
import com.itheima.rookiemall.utils.UIUtils;
import com.itheima.rookiemall.widget.CustomToolBar;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 我的Fragment
 * Created by lyl on 2016/9/25.
 */

public class HomeMyFragment extends BaseFragment {

    @BindView(R.id.iv_head)
    ImageView mIvHead;
    @BindView(R.id.tv_head)
    TextView mTvHead;
    @BindView(R.id.butt_submit)
    Button btSubmit;
    private String mToken;

    @Override
    public int getContentViewId() {
        return R.layout.home_my_fragment;
    }

    @Override
    public void onStart() {
        super.onStart();
        loginStateChangeUpdateUI();
    }

    @Override
    public void initView() {

    }

    @Override
    public void initData() {

    }

    @Override
    public void initListener() {

    }

    private void loginStateChangeUpdateUI() {
        mToken = SPUtils.getToken(mContext);
        //判断是否登录
        if (TextUtils.isEmpty(mToken)) {
            btSubmit.setVisibility(View.INVISIBLE);
            mTvHead.setVisibility(View.VISIBLE);
        } else {
            btSubmit.setVisibility(View.VISIBLE);
            mTvHead.setVisibility(View.INVISIBLE);
        }
    }

    @OnClick({R.id.ll_head, R.id.iv_head, R.id.butt_submit
            , R.id.ll_my_order, R.id.ll_my_collections, R.id.ll_my_address})
    public void click(View v) {
        switch (v.getId()) {
            case R.id.ll_head:
            case R.id.iv_head:
                if (TextUtils.isEmpty(mToken)) {
                    startDetailsActivity(R.layout.fragment_login, false, false);
                }
                break;
            case R.id.butt_submit:
                SPUtils.putToken(mContext, "");
                loginStateChangeUpdateUI();
                break;
            case R.id.ll_my_order:
                startDetailsActivity(R.layout.fragment_my_order, false, true);
                break;
            case R.id.ll_my_collections:
                startDetailsActivity(R.layout.fragment_my_collection, false, true);
                break;
            case R.id.ll_my_address:
                startDetailsActivity(R.layout.fragment_address_list, false, true);
                break;
        }
    }

    @Override
    public void free() {
        super.free();
    }
}
