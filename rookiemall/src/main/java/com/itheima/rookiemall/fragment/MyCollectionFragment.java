package com.itheima.rookiemall.fragment;

import com.itheima.rookiemall.R;
import com.itheima.rookiemall.base.BaseFragment;
import com.itheima.rookiemall.widget.CustomToolBar;

/**
 * 我的收藏
 * Created by lyl on 2016/10/15.
 */

public class MyCollectionFragment extends BaseFragment {
    @Override
    public int getContentViewId() {
        return R.layout.fragment_my_collection;
    }


    @Override
    protected void initToolbar(CustomToolBar toolBar) {
        super.initToolbar(toolBar);
        toolBar.setTitle("我的收藏");
    }

    @Override
    protected boolean isShowBackView() {
        return true;
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
}
