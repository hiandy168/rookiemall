package com.itheima.rookiemall.base;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.itheima.rookiemall.R;
import com.itheima.rookiemall.activity.DetailsActivity;
import com.itheima.rookiemall.config.Constants;
import com.itheima.rookiemall.ui.App;
import com.itheima.rookiemall.utils.SPUtils;
import com.itheima.rookiemall.widget.CustomToolBar;

import org.greenrobot.eventbus.EventBus;

import butterknife.ButterKnife;

/**
 * Created by lyl on 2016/10/1.
 */

public abstract class BaseFragment extends Fragment {

    protected View mRootView;
    protected Context mContext;
    protected AppCompatActivity mActivity;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mActivity = (AppCompatActivity) context;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (mRootView == null) {
            mRootView = inflater.inflate(getContentViewId(), container, false);
            ButterKnife.bind(this, mRootView);
            mContext = getContext().getApplicationContext();
            initView();
            initData();
            initListener();

        }
        return mRootView;
    }

    @Override
    public void onStart() {
        super.onStart();
        if (isRegisterEventBus()) {
            registerEventBus();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        initToolbar(((BaseActivity) getActivity()).getToolBar());
    }

    @Override
    public void onStop() {
        if (isRegisterEventBus()) {
            unRegisterEventBus();
        }
        super.onStop();
    }

    View.OnClickListener mToolbarListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.iv_back_icon:
                    backViewClick(v);
                    break;
                case R.id.tv_rightView:
                    rightClick(v);
                    break;
            }
        }
    };


    protected void initToolbar(CustomToolBar toolBar) {
        if (toolBar != null) {
            toolBar.hideAllView();
        }
        if (isShowBackView()) {
            toolBar.showBackView();
            toolBar.setBackOnClickListener(mToolbarListener);
            toolBar.setRightViewClickListener(mToolbarListener);
        }
    }

    protected boolean isShowBackView() {
        return false;
    }

    protected void backViewClick(View v) {
        if (getFragmentManager() != null && getFragmentManager().getBackStackEntryCount() > 0) {
            getFragmentManager().popBackStack();
        }

        if (getFragmentManager() == null || getFragmentManager().getBackStackEntryCount() == 0) {
            mActivity.finish();
            mActivity = null;
        }
    }


    protected void rightClick(View v) {
    }

    public abstract int getContentViewId();

    public abstract void initView();

    public abstract void initData();

    public abstract void initListener();

    protected boolean isRegisterEventBus() {
        return false;
    }

    protected void registerEventBus() {
        EventBus.getDefault().register(this);
    }

    protected void unRegisterEventBus() {
        EventBus.getDefault().unregister(this);
    }

    protected void startDetailsActivity(int layoutId, boolean isAddBackStack) {
        startDetailsActivity(layoutId, isAddBackStack, false);
    }


    protected void startDetailsActivity(int layoutId, boolean isAddBackStack, boolean isLogin) {
        Intent intent = new Intent(getActivity(), DetailsActivity.class);
        intent.putExtra(Constants.KEY_INTENT_RES_ID, layoutId);
        intent.putExtra(Constants.KEY_FRAGMENT_IS_ADD_BACK_STACK, isAddBackStack);
        //需要登录
        if (isLogin && TextUtils.isEmpty(SPUtils.getToken(mContext))) {
            //保存Intent数据
            App.getIntance().putIntent(intent);
            startDetailsActivity(R.layout.fragment_login, false, false);
        } else {
            startActivity(intent);
        }
    }


    /**
     * 释放资源
     */
    public void free() {
        mContext = null;
        mRootView = null;
//        mActivity = null;
        mToolbarListener = null;
    }

    @Override
    public void onDestroy() {
        free();
        super.onDestroy();
    }
}
