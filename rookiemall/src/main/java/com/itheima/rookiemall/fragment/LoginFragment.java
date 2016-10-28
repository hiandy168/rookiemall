package com.itheima.rookiemall.fragment;

import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.itheima.rookiemall.R;
import com.itheima.rookiemall.api.HttpHelper;
import com.itheima.rookiemall.base.BaseFragment;
import com.itheima.rookiemall.bean.LoginBean;
import com.itheima.rookiemall.call.HttpResponseCall;
import com.itheima.rookiemall.config.Constants;
import com.itheima.rookiemall.config.Urls;
import com.itheima.rookiemall.ui.App;
import com.itheima.rookiemall.utils.DESUtil;
import com.itheima.rookiemall.utils.SPUtils;
import com.itheima.rookiemall.utils.UIUtils;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by lyl on 2016/10/11.
 */

public class LoginFragment extends BaseFragment {
    @BindView(R.id.et_tel_phone)
    EditText mEtTelPhone;
    @BindView(R.id.et_password)
    EditText mEtPass;
    @BindView(R.id.butt_submit)
    Button btSubmit;

    @Override
    public int getContentViewId() {
        return R.layout.fragment_login;
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

    @Override
    public void onStart() {
        super.onStart();

    }

    @OnClick({R.id.iv_back_icon, R.id.butt_submit, R.id.tv_register})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back_icon:
                backViewClick(v);
                break;
            case R.id.butt_submit:
                login();
                break;
            case R.id.tv_register:
                register();
                break;
            case R.id.tv_forget_pass:
                forgetPass();
                break;
        }

    }

    /**
     * 忘记密码
     */
    private void forgetPass() {
    }

    public void register() {
        startDetailsActivity(R.layout.fragment_register, false, false);
    }

    public void login() {
        String phone = mEtTelPhone.getText().toString().trim();
        if (TextUtils.isEmpty(phone)) {
            UIUtils.showDefToast(mContext, "请输入手机号码");
            return;
        }
        String pass = mEtPass.getText().toString().trim();
        if (TextUtils.isEmpty(pass)) {
            UIUtils.showDefToast(mContext, "请输入密码");
            return;
        }
        Map<String, Object> param = new HashMap<>();
        param.put("phone", phone);
        param.put("password", DESUtil.encode(Constants.DES_KEY, pass));
        HttpHelper.getInstance().post(Urls.api_login, param, new HttpResponseCall<LoginBean>() {
            @Override
            public void onResponse(LoginBean loginBean) {
                if (loginBean.isSuccess()) {
                    SPUtils.putUserData(mContext, loginBean.data.toString());
                    SPUtils.putToken(mContext, loginBean.token);
                    SPUtils.putPhone(mContext, loginBean.data.mobi);
                    SPUtils.putUserId(mContext, loginBean.data.id);
                    Intent toIntent = App.getIntance().getIntent();
                    if (toIntent != null) {
                        startActivity(toIntent);
                    }
                    backViewClick(null);
                } else {
                    UIUtils.showDefToast(mContext, loginBean.message);
                }
            }
        });
    }

    @Override
    public void free() {
        super.free();
    }
}
