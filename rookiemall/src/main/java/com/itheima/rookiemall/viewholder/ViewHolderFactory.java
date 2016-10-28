package com.itheima.rookiemall.viewholder;

import android.view.ViewGroup;

import com.itheima.rookiemall.R;
import com.itheima.rookiemall.base.BaseRecyclerViewHolder;
import com.itheima.rookiemall.fragment.AddressListFragment;
import com.itheima.rookiemall.fragment.MyOrderFragment;
import com.itheima.rookiemall.ui.HomeCarFragment;
import com.itheima.rookiemall.ui.HomeHotFragment;
import com.itheima.rookiemall.ui.HomeTypeFragment;

/**
 * Created by lyl on 2016/10/7.
 */

public final class ViewHolderFactory {
    public static BaseRecyclerViewHolder onCreateRealViewHolder(ViewGroup parent, int itemResId) {
        BaseRecyclerViewHolder holder = null;
        switch (itemResId) {
            case R.layout.item_hometype_right_recylerview:
                holder = new HomeTypeFragment.MyRightViewHolder(parent, itemResId);
                break;
            case R.layout.item_hometype_left:
                holder = new HomeTypeFragment.MyLeftViewHolder(parent, itemResId);
                break;
            case R.layout.item_home_car_recyclerview:
                holder = new HomeCarFragment.MyCarRecyclerViewHolder(parent, R.layout.item_home_car_recyclerview);
                break;
            case R.layout.item_homehot_recylcerview:
                holder = new HomeHotFragment.HomeHotViewHolder(parent, itemResId);
                break;
            case R.layout.item_fragment_address_list:
                holder = new AddressListFragment.AddressListViewHolder(parent, itemResId);
                break;
            case R.layout.item_my_orders_list:
                holder = new MyOrderFragment.MyOrderViewHolder(parent,itemResId);
                break;
        }
        return holder;
    }
}
