package com.itheima.rookiemall.base;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;

import com.itheima.rookiemall.R;
import com.itheima.rookiemall.widget.CustomToolBar;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by lyl on 2016/9/26.
 */

public abstract class BaseActivity extends AppCompatActivity {

    protected Context mContext;
    private Unbinder mBind;

    @BindView(R.id.toolbar)
    CustomToolBar mCustomToolBar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getContentViewId());
        mBind = ButterKnife.bind(this);
        initToolBar();
        initView();
        initData();
        initListener();

    }

    protected abstract int getContentViewId();

    protected abstract void initView();

    protected abstract void initData();

    protected abstract void initListener();


    protected void initToolBar() {
    }

    public CustomToolBar getToolBar() {
        return mCustomToolBar;
    }


    public void replace(int fragmentLayoutResId, Fragment fragment) {
        replace(fragmentLayoutResId, fragment, false);
    }

    public void replace(int fragmentLayoutResId, Fragment fragment, boolean isAddBackStack) {
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = manager.beginTransaction();
        fragmentTransaction.replace(fragmentLayoutResId, fragment);
        if (isAddBackStack) {
            fragmentTransaction.addToBackStack(null);
        }
        fragmentTransaction.commit();
    }

    public void add(int fragmentLayoutResId, Fragment fragment, boolean isAddBackStack) {
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = manager.beginTransaction();
        fragmentTransaction.add(fragmentLayoutResId, fragment);
        if (isAddBackStack) {
            fragmentTransaction.addToBackStack(Fragment.class.getSimpleName());
        }
        fragmentTransaction.commit();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        mContext = null;
        mBind.unbind();
        mBind = null;
    }
}
