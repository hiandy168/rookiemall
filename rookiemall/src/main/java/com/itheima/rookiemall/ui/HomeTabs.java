package com.itheima.rookiemall.ui;

import com.itheima.rookiemall.R;

/**
 * Created by lyl on 2016/9/25.
 */

public enum HomeTabs {
    HOME(R.string.homeTabHomeTxt, R.drawable.home_tabhost_icon_selector, HomeFragment.class),
    HOT(R.string.homeTabHotTxt, R.drawable.home_tabhost_hot_icon_selector, HomeHotFragment.class),
    TYPE(R.string.homeTabTypeTxt, R.drawable.home_tabhost_type_icon_selector, HomeTypeFragment.class),
    CAR(R.string.homeTabCarTxt, R.drawable.home_tabhost_car_icon_selector, HomeCarFragment.class),
    MY(R.string.homeTabMyTxt, R.drawable.home_tabhost_my_icon_selector, HomeMyFragment.class);


    public int txtResId;
    public int iconSelectorId;
    public Class<?> fragmentClazz;


    HomeTabs(int txtResId, int iconSelectorId, Class<?> fragmentClazz) {
        this.txtResId = txtResId;
        this.iconSelectorId = iconSelectorId;
        this.fragmentClazz = fragmentClazz;
    }

}
