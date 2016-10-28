package com.itheima.rookiemall.ui;

import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.TabHost;
import android.widget.TextView;

import com.itheima.rookiemall.R;
import com.itheima.rookiemall.base.BaseActivity;
import com.itheima.rookiemall.utils.SizeUtils;
import com.itheima.rookiemall.widget.MyFragmentTabHost;

import butterknife.BindView;

public class MainActivity extends BaseActivity {

    @BindView(R.id.tab_host)
    MyFragmentTabHost mTabHost;


    @Override
    protected int getContentViewId() {
        return R.layout.activity_main;
    }

    @Override
    protected void initView() {
        initTabs();

    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initListener() {

    }

    public void initTabs() {
        mTabHost.setup(this, getSupportFragmentManager(), R.id.realtabcontent);
        for (HomeTabs tab : HomeTabs.values()) {
            TabHost.TabSpec tabSpec = mTabHost.newTabSpec(getResources().getString(tab.txtResId));
            TextView indicatorTextView = (TextView) View.inflate(getBaseContext(), R.layout.home_tabhost_indicator, null);
            Drawable topDrawable = getResources().getDrawable(tab.iconSelectorId);
            int width = SizeUtils.dp2px(getApplicationContext(), 23);
            topDrawable.setBounds(0, 0, width, width);
            indicatorTextView.setCompoundDrawables(null, topDrawable, null, null);
            indicatorTextView.setText(tab.txtResId);
            tabSpec.setIndicator(indicatorTextView);
            mTabHost.addTab(tabSpec, tab.fragmentClazz, null);
        }
    }
}
