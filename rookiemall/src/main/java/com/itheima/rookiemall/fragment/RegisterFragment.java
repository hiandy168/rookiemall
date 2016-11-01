package com.itheima.rookiemall.fragment;

import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.itheima.retrofitutils.HttpHelper;
import com.itheima.retrofitutils.HttpResponseListener;
import com.itheima.retrofitutils.RetrofitUtils;
import com.itheima.rookiemall.R;
import com.itheima.rookiemall.base.BaseFragment;
import com.itheima.rookiemall.bean.LoginBean;
import com.itheima.rookiemall.config.Constants;
import com.itheima.rookiemall.config.Urls;
import com.itheima.rookiemall.utils.DESUtil;
import com.itheima.rookiemall.utils.L;
import com.itheima.rookiemall.utils.UIUtils;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;

/**
 * Created by lyl on 2016/10/11.
 */

public class RegisterFragment extends BaseFragment {
    @BindView(R.id.et_tel_phone)
    EditText mEtTelPhone;
    @BindView(R.id.et_password)
    EditText mEtPass;
    @BindView(R.id.et_code)
    EditText mEtCode;

    @BindView(R.id.bt_send_code)
    Button btSendCode;

    EventHandler mEventHandler;

    Handler mSendSmsHandler;
    int mSecond = 60;

    @Override
    public int getContentViewId() {
        return R.layout.fragment_register;
    }

    @Override
    public void initView() {
        mSendSmsHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                if (mSecond > 0) {
                    mSecond--;
                    btSendCode.setText(String.valueOf(mSecond));
                    btSendCode.setEnabled(false);
                    sendEmptyMessageDelayed(0, 1000);
                } else {
                    mSecond = 60;
                    btSendCode.setEnabled(true);
                    btSendCode.setText("发送验证码");
                }

            }
        };


    }

    @Override
    public void initData() {
        SMSSDK.initSDK(getContext().getApplicationContext()
                , "17e07214e59a0", "e968f5fa12417133e4eddcfec1868fff");
        mEventHandler = new EventHandler() {
            public void afterEvent(final int event, final int result, final Object data) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            L.i("event:" + event + " result:" + result + " data:" + (data == null ? "=null" : data.toString()));
                            if (result == SMSSDK.RESULT_COMPLETE) {
                                //回调完成
                                if (event == SMSSDK.EVENT_SUBMIT_VERIFICATION_CODE) {
                                    //提交验证码成功
                                    UIUtils.showDefToast(mContext, "提交验证码成功");
                                    register();
                                } else if (event == SMSSDK.EVENT_GET_VERIFICATION_CODE) {
                                    //获取验证码成功
                                    UIUtils.showDefToast(mContext, "短信验证码发送成功");
                                    mSendSmsHandler.sendEmptyMessageDelayed(0, 0);
                                } else if (event == SMSSDK.EVENT_GET_SUPPORTED_COUNTRIES) {
                                    //返回支持发送验证码的国家列表
                                }
                            } else {
                                ((Throwable) data).printStackTrace();
                                ((Throwable) data).printStackTrace();
                                Throwable resId = (Throwable) data;
                                JSONObject object = new JSONObject(resId.getMessage());
                                String des = object.optString("detail");
                                int status1 = object.optInt("status");
                                if (!TextUtils.isEmpty(des)) {
                                    UIUtils.showDefToast(mContext, des);
                                    return;
                                }
                            }
                        } catch (Exception e) {
                            L.e(e);
                        }
                    }
                });

            }
        };
        //注册短信回调
        SMSSDK.registerEventHandler(mEventHandler);

    }

    public void getSmsCode() {
        String phone = mEtTelPhone.getText().toString().trim();
        if (TextUtils.isEmpty(phone)) {
            UIUtils.showDefToast(mContext, "请输入手机号码");
            return;
        }
        //"42"国家，电话号码
        SMSSDK.getVerificationCode("86", phone);

    }

    /**
     * 验证短信验证码是否正确
     */
    public void submitVerificationCode() {
        String phone = mEtTelPhone.getText().toString().trim();
        if (TextUtils.isEmpty(phone)) {
            UIUtils.showDefToast(mContext, "请输入手机号码");
            return;
        }
        String smsCode = mEtCode.getText().toString().trim();
        if (TextUtils.isEmpty(smsCode)) {
            UIUtils.showDefToast(mContext, "请填写短信验证码");
            return;
        }
//        submitVerificationCode(String country, String phone, String code)
        SMSSDK.submitVerificationCode("86", phone, smsCode);
    }


    @Override
    public void initListener() {

    }

    @OnClick({R.id.iv_back_icon, R.id.butt_submit, R.id.bt_send_code})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back_icon:
                backViewClick(v);
                break;
            case R.id.butt_submit:
//                register();
                submitVerificationCode();
                break;
            case R.id.bt_send_code:
                getSmsCode();
                break;
        }

    }


    public void register() {
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
        RetrofitUtils.getAsync(Urls.api_register, param, new HttpResponseListener<LoginBean>() {
            @Override
            public void onResponse(LoginBean loginBean) {
                if (loginBean.status == 1) {
//                    SPUtils.putUserData(mContext, loginBean.data);
//                    SPUtils.putToken(mContext, loginBean.token);
//                    backViewClick(null);
                    UIUtils.showDefToast(mContext, "注册成功");
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
        SMSSDK.unregisterEventHandler(mEventHandler);
        mEventHandler = null;
        mSendSmsHandler.removeCallbacksAndMessages(null);
        mSendSmsHandler = null;
    }
}
