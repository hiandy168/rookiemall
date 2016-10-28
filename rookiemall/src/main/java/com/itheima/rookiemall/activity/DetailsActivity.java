package com.itheima.rookiemall.activity;

import android.content.Intent;
import android.support.v4.app.Fragment;

import com.itheima.rookiemall.R;
import com.itheima.rookiemall.base.BaseActivity;
import com.itheima.rookiemall.config.Constants;
import com.itheima.rookiemall.fragment.AddAddressFragment;
import com.itheima.rookiemall.fragment.AddressListFragment;
import com.itheima.rookiemall.fragment.GoodsListDetailsFragment;
import com.itheima.rookiemall.fragment.LoginFragment;
import com.itheima.rookiemall.fragment.MyOrderFragment;
import com.itheima.rookiemall.fragment.MyCollectionFragment;
import com.itheima.rookiemall.fragment.SubmitOrderFragment;
import com.itheima.rookiemall.fragment.RegisterFragment;
import com.itheima.rookiemall.fragment.WebViewFragment;

/**
 * Created by lyl on 2016/10/8.
 */

public class DetailsActivity extends BaseActivity {

    private int mFragmentResId;

    @Override
    protected int getContentViewId() {
        return R.layout.activity_details_layout;
    }

    @Override
    protected void initView() {
        Intent intent = getIntent();
        mFragmentResId = intent.getIntExtra(Constants.KEY_INTENT_RES_ID, 0);
        boolean isAddBackStack = intent.getBooleanExtra(Constants.KEY_FRAGMENT_IS_ADD_BACK_STACK, false);
        add(R.id.root_view, createFragment(mFragmentResId), isAddBackStack);

    }

    @Override
    protected void initData() {
    }

    @Override
    protected void initListener() {

    }


    public Fragment createFragment(int fragmentResId) {
        Fragment fragment = null;
        switch (fragmentResId) {
            case R.layout.fragment_home_list_detail:
                fragment = new GoodsListDetailsFragment();
                break;
            case R.layout.fragment_webview_layout:
                fragment = new WebViewFragment();
                break;
            case R.layout.fragment_login:
                fragment = new LoginFragment();
                break;
            case R.layout.fragment_register:
                fragment = new RegisterFragment();
                break;
            case R.layout.fragment_submit_order:
                fragment = new SubmitOrderFragment();
                break;
            case R.layout.fragment_address_list:
                fragment = new AddressListFragment();
                break;
            case R.layout.fragment_add_address:
                fragment = new AddAddressFragment();
                break;
            case R.layout.fragment_my_order:
                fragment = new MyOrderFragment();
                break;
            case R.layout.fragment_my_collection:
                fragment = new MyCollectionFragment();
                break;
        }
        return fragment;
    }


}
