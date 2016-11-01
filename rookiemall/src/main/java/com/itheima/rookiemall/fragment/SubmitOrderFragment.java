package com.itheima.rookiemall.fragment;

import android.content.ComponentName;
import android.content.Intent;
import android.view.View;
import android.widget.RadioButton;
import android.widget.TextView;

import com.itheima.retrofitutils.HttpHelper;
import com.itheima.retrofitutils.HttpResponseListener;
import com.itheima.retrofitutils.RetrofitUtils;
import com.itheima.rookiemall.R;
import com.itheima.rookiemall.base.BaseFragment;
import com.itheima.rookiemall.bean.ShoppingCar;
import com.itheima.rookiemall.bean.SubmitOrderBean;
import com.itheima.rookiemall.config.Constants;
import com.itheima.rookiemall.config.Urls;
import com.itheima.rookiemall.utils.CarProvider;
import com.itheima.rookiemall.utils.GsonUtils;
import com.itheima.rookiemall.utils.L;
import com.itheima.rookiemall.utils.SPUtils;
import com.pingplusplus.android.PaymentActivity;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by lyl on 2016/10/13.
 */

public class SubmitOrderFragment extends BaseFragment {

    @BindView(R.id.rb_alipay)
    RadioButton mRbAlipay;
    @BindView(R.id.rb_webchat)
    RadioButton mRbWebchat;
    @BindView(R.id.rb_bd)
    RadioButton mRbBd;
    @BindView(R.id.txt_total)
    TextView mTvTotal;

    @Override
    public int getContentViewId() {
        return R.layout.fragment_create_order;
    }

    @Override
    public void initView() {
        mTvTotal.setText("实付： ￥" + CarProvider.getInstance(mContext).getCarTotalPrice());
    }

    @Override
    public void initData() {


    }

    @Override
    public void initListener() {

    }

    @OnClick({R.id.rl_alipay, R.id.rl_wechat, R.id.rl_bd})
    public void click(View v) {
        mRbAlipay.setChecked(false);
        mRbWebchat.setChecked(false);
        mRbBd.setChecked(false);
        switch (v.getId()) {
            case R.id.rl_alipay:
                mRbAlipay.setChecked(true);
                break;
            case R.id.rl_wechat:
                mRbWebchat.setChecked(true);
                break;
            case R.id.rl_bd:
                mRbBd.setChecked(true);
                break;
        }
    }

    @OnClick(R.id.btn_createOrder)
    public void clickOrder(View v) {
        switch (v.getId()) {
            case R.id.btn_createOrder:
                httpPlayOrder();
                break;
        }
    }

    public String getItemJson() {
        JSONArray jsonArray = new JSONArray();
        List<ShoppingCar> allCar = CarProvider.getInstance(mContext).getAllSelectShopping();
        for (ShoppingCar car : allCar) {
            JSONObject object = new JSONObject();
            try {
                object.put("ware_id", car.id);
                object.put("amount", car.price);
                jsonArray.put(object);
            } catch (Exception e) {
                L.e(e);
            }
        }
        return jsonArray.toString();
    }

    public void httpPlayOrder() {
        Map<String, Object> param = new HashMap<>();
        param.put("user_id", SPUtils.getUserId(mContext));
        param.put(Constants.TOKEN, SPUtils.getToken(mContext));
        param.put("item_json", getItemJson());
        param.put("amount", (int) CarProvider.getInstance(mContext).getCarTotalPrice());
        param.put("addr_id", 1);
        param.put("pay_channel", "alipay");

        L.i("httpPlayOrder param:" + param.toString());


        RetrofitUtils.postAsync(Urls.api_cretae_order, param, new HttpResponseListener<SubmitOrderBean>() {
            @Override
            public void onResponse(SubmitOrderBean orderBean) {
                L.i("onResponse  SubmitOrderBean: " + orderBean.toString());
                openPaymentActivity(GsonUtils.parseToJson(orderBean.data.charge));
            }
        });


    }

    private void openPaymentActivity(String charge) {
        Intent intent = new Intent();
        String packageName = mActivity.getPackageName();
        ComponentName componentName = new ComponentName(packageName, packageName + ".wxapi.WXPayEntryActivity");
        intent.setComponent(componentName);
        intent.putExtra(PaymentActivity.EXTRA_CHARGE, charge);
        startActivityForResult(intent, 100);
    }
}
