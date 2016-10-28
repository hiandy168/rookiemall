package com.itheima.rookiemall.fragment;

import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import com.itheima.rookiemall.R;
import com.itheima.rookiemall.api.HttpHelper;
import com.itheima.rookiemall.base.BaseFragment;
import com.itheima.rookiemall.base.BaseRecyclerAdapter;
import com.itheima.rookiemall.base.BaseRecyclerViewHolder;
import com.itheima.rookiemall.bean.AddressListBean;
import com.itheima.rookiemall.bean.BaseBean;
import com.itheima.rookiemall.call.HttpResponseCall;
import com.itheima.rookiemall.config.Constants;
import com.itheima.rookiemall.config.Urls;
import com.itheima.rookiemall.utils.SPUtils;
import com.itheima.rookiemall.utils.UIUtils;
import com.itheima.rookiemall.widget.CustomRecyclerView;
import com.itheima.rookiemall.widget.CustomToolBar;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by lyl on 2016/10/15.
 */

public class AddressListFragment extends BaseFragment {

    @BindView(R.id.recycler_view)
    CustomRecyclerView mRecyclerView;

    @Override
    public int getContentViewId() {
        return R.layout.fragment_address_list;
    }

    @Override
    protected void initToolbar(CustomToolBar toolBar) {
        super.initToolbar(toolBar);
        toolBar.setTitle("地址列表");
        toolBar.setRightViewBackground(R.drawable.icon_add);
    }


    @Override
    protected boolean isRegisterEventBus() {
        return true;
    }

    @Override
    protected void rightClick(View v) {
        startDetailsActivity(R.layout.fragment_add_address, false);
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
        httpAddressList(true);

    }

    @Override
    public void initListener() {

    }

    @Subscribe
    public void httpAddressList(Boolean isSendGetAddressHttp) {
        if (!isSendGetAddressHttp) {
            return;
        }
        Map<String, Object> param = new HashMap<>();
        param.put(Constants.USER_ID, SPUtils.getUserId(mContext));
        param.put(Constants.TOKEN, SPUtils.getToken(mContext));
        HttpHelper.getInstance().get(Urls.api_address_list, param, new HttpResponseCall<List<AddressListBean>>() {
            @Override
            public void onResponse(List<AddressListBean> addressListBean) {
                new BaseRecyclerAdapter<AddressListBean>(mRecyclerView, R.layout.item_fragment_address_list, addressListBean);
            }
        });
    }

    public static class AddressListViewHolder extends BaseRecyclerViewHolder<AddressListBean> {
        @BindView(R.id.txt_name)
        TextView mTvName;
        @BindView(R.id.txt_phone)
        TextView mTvPhone;
        @BindView(R.id.txt_address)
        TextView mTvAddress;
        @BindView(R.id.cb_is_defualt)
        CheckBox mCbisDef;
        @BindView(R.id.txt_edit)
        TextView mTvEdit;
        @BindView(R.id.txt_del)
        TextView mTvDel;

        public AddressListViewHolder(ViewGroup parentView, int itemResId) {
            super(parentView, itemResId);
        }

        @OnClick(R.id.txt_del)
        public void click(View v) {
            Map<String, Object> param = new HashMap<>();
            param.put(Constants.USER_ID, SPUtils.getUserId(mContext));
            param.put(Constants.TOKEN, SPUtils.getToken(mContext));
            param.put("id", mData.id);
            HttpHelper.getInstance().get(Urls.api_del_address, param, new HttpResponseCall<BaseBean>() {
                @Override
                public void onResponse(BaseBean baseBean) {
                    UIUtils.showDefToast(mContext, baseBean.message);
                    EventBus.getDefault().post(true);

                }
            });
        }

        @Override
        protected void onBindRealData() {
            mTvName.setText(mData.consignee);
            mTvPhone.setText(mData.phone);
            mTvAddress.setText(mData.addr);
            mCbisDef.setChecked(mData.isDefault);

        }


    }
}
