package com.itheima.rookiemall.fragment;

import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.bigkoo.pickerview.OptionsPickerView;
import com.bigkoo.pickerview.model.IPickerViewData;
import com.itheima.rookiemall.R;
import com.itheima.rookiemall.api.HttpHelper;
import com.itheima.rookiemall.base.BaseFragment;
import com.itheima.rookiemall.bean.BaseBean;
import com.itheima.rookiemall.bean.PickerViewData;
import com.itheima.rookiemall.bean.ProvinceBean;
import com.itheima.rookiemall.call.HttpResponseCall;
import com.itheima.rookiemall.config.Constants;
import com.itheima.rookiemall.config.Urls;
import com.itheima.rookiemall.utils.SPUtils;
import com.itheima.rookiemall.utils.UIUtils;
import com.itheima.rookiemall.widget.CustomToolBar;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by lyl on 2016/10/15.
 */

public class AddAddressFragment extends BaseFragment {
    @BindView(R.id.et_name)
    EditText mEtName;
    @BindView(R.id.et_phone)
    EditText mEtPhone;
    @BindView(R.id.tv_address)
    TextView mTvAddress;
    @BindView(R.id.et_details_address)
    EditText mEtDetailsAddr;

    OptionsPickerView mOptionsPickerView;
    private ArrayList<ProvinceBean> options1Items = new ArrayList<>();
    private ArrayList<ArrayList<String>> options2Items = new ArrayList<>();
    private ArrayList<ArrayList<ArrayList<IPickerViewData>>> options3Items = new ArrayList<>();

    @Override
    public int getContentViewId() {
        return R.layout.fragment_address_add;
    }

    @Override
    protected void initToolbar(CustomToolBar toolBar) {
        super.initToolbar(toolBar);
        toolBar.setTitle("新建收件地址");
        toolBar.setRightTxt("保存");
    }

    @Override
    protected boolean isShowBackView() {
        return true;
    }

    @Override
    protected void rightClick(View v) {
        sendAddressToService();
    }


    @Override
    public void initView() {

    }

    @Override
    public void initData() {
        initSelectAddressDialog();
    }

    @Override
    public void initListener() {

    }

    @OnClick(R.id.tv_address)
    public void click(View v) {
        mOptionsPickerView.show();
    }


    public void sendAddressToService() {
        String name = mEtName.getText().toString().trim();
        String phone = mEtPhone.getText().toString().trim();
        String address = mTvAddress.getText().toString().trim();
        String detailsAddr = mEtDetailsAddr.getText().toString().trim();

        if (TextUtils.isEmpty(name) || TextUtils.isEmpty(phone)
                || TextUtils.isEmpty(address) || TextUtils.isEmpty(detailsAddr)) {
            UIUtils.showDefToast(mContext, "请把信息填写完整");
        }

        Map<String, Object> param = new HashMap<>();
        param.put(Constants.USER_ID, SPUtils.getUserId(mContext));
        param.put(Constants.TOKEN, SPUtils.getToken(mContext));
        param.put("consignee", name);
        param.put("phone", phone);
        param.put("addr", address + "<" + detailsAddr + ">");
        param.put("zip_code", "1234");
        HttpHelper.getInstance().post(Urls.api_add_address, param, new HttpResponseCall<BaseBean>() {
            @Override
            public void onResponse(BaseBean baseBean) {
                if (baseBean.isSuccess()) {
                    UIUtils.showDefToast(mContext, "地址添加成功");
                    EventBus.getDefault().post(true);
                    backViewClick(null);

                } else {
                    UIUtils.showDefToast(mContext, "添加失败：地址数量超过最大值");
                }

            }
        });

    }

    public void initSelectAddressDialog() {
        //选项选择器
        mOptionsPickerView = new OptionsPickerView(mActivity);


        //选项1
        options1Items.add(new ProvinceBean(0, "广东", "广东省，以岭南东道、广南东路得名", "其他数据"));
        options1Items.add(new ProvinceBean(1, "湖南", "湖南省地处中国中部、长江中游，因大部分区域处于洞庭湖以南而得名湖南", "芒果TV"));
        options1Items.add(new ProvinceBean(3, "广西", "嗯～～", ""));

        //选项2
        ArrayList<String> options2Items_01 = new ArrayList<>();
        options2Items_01.add("广州");
        options2Items_01.add("佛山");
        options2Items_01.add("东莞");
        options2Items_01.add("阳江");
        options2Items_01.add("珠海");
        ArrayList<String> options2Items_02 = new ArrayList<>();
        options2Items_02.add("长沙");
        options2Items_02.add("岳阳");
        ArrayList<String> options2Items_03 = new ArrayList<>();
        options2Items_03.add("桂林");
        options2Items.add(options2Items_01);
        options2Items.add(options2Items_02);
        options2Items.add(options2Items_03);

        //选项3
        ArrayList<ArrayList<IPickerViewData>> options3Items_01 = new ArrayList<>();
        ArrayList<ArrayList<IPickerViewData>> options3Items_02 = new ArrayList<>();
        ArrayList<ArrayList<IPickerViewData>> options3Items_03 = new ArrayList<>();
        ArrayList<IPickerViewData> options3Items_01_01 = new ArrayList<>();
        options3Items_01_01.add(new PickerViewData("天河"));
        options3Items_01_01.add(new PickerViewData("黄埔"));
        options3Items_01_01.add(new PickerViewData("海珠"));
        options3Items_01_01.add(new PickerViewData("越秀"));
        options3Items_01.add(options3Items_01_01);
        ArrayList<IPickerViewData> options3Items_01_02 = new ArrayList<>();
        options3Items_01_02.add(new PickerViewData("南海"));
        options3Items_01_02.add(new PickerViewData("高明"));
        options3Items_01_02.add(new PickerViewData("禅城"));
        options3Items_01_02.add(new PickerViewData("桂城"));
        options3Items_01.add(options3Items_01_02);
        ArrayList<IPickerViewData> options3Items_01_03 = new ArrayList<>();
        options3Items_01_03.add(new PickerViewData("其他"));
        options3Items_01_03.add(new PickerViewData("常平"));
        options3Items_01_03.add(new PickerViewData("虎门"));
        options3Items_01.add(options3Items_01_03);
        ArrayList<IPickerViewData> options3Items_01_04 = new ArrayList<>();
        options3Items_01_04.add(new PickerViewData("其他"));
        options3Items_01_04.add(new PickerViewData("其他"));
        options3Items_01_04.add(new PickerViewData("其他"));
        options3Items_01.add(options3Items_01_04);
        ArrayList<IPickerViewData> options3Items_01_05 = new ArrayList<>();

        options3Items_01_05.add(new PickerViewData("其他1"));
        options3Items_01_05.add(new PickerViewData("其他2"));
        options3Items_01.add(options3Items_01_05);

        ArrayList<IPickerViewData> options3Items_02_01 = new ArrayList<>();

        options3Items_02_01.add(new PickerViewData("长沙1"));
        options3Items_02_01.add(new PickerViewData("长沙2"));
        options3Items_02_01.add(new PickerViewData("长沙3"));
        options3Items_02_01.add(new PickerViewData("长沙4"));
        options3Items_02_01.add(new PickerViewData("长沙5"));


        options3Items_02.add(options3Items_02_01);
        ArrayList<IPickerViewData> options3Items_02_02 = new ArrayList<>();

        options3Items_02_02.add(new PickerViewData("岳阳"));
        options3Items_02_02.add(new PickerViewData("岳阳1"));
        options3Items_02_02.add(new PickerViewData("岳阳2"));
        options3Items_02_02.add(new PickerViewData("岳阳3"));
        options3Items_02_02.add(new PickerViewData("岳阳4"));
        options3Items_02_02.add(new PickerViewData("岳阳5"));

        options3Items_02.add(options3Items_02_02);
        ArrayList<IPickerViewData> options3Items_03_01 = new ArrayList<>();
        options3Items_03_01.add(new PickerViewData("好山水"));
        options3Items_03.add(options3Items_03_01);

        options3Items.add(options3Items_01);
        options3Items.add(options3Items_02);
        options3Items.add(options3Items_03);

        //三级联动效果
        mOptionsPickerView.setPicker(options1Items, options2Items, options3Items, true);
        //设置选择的三级单位
//        pwOptions.setLabels("省", "市", "区");
        mOptionsPickerView.setTitle("选择城市");
        mOptionsPickerView.setCyclic(false, true, true);
        //设置默认选中的三级项目
        //监听确定选择按钮
        mOptionsPickerView.setSelectOptions(1, 1, 1);
        mOptionsPickerView.setOnoptionsSelectListener(new OptionsPickerView.OnOptionsSelectListener() {

            @Override
            public void onOptionsSelect(int options1, int option2, int options3) {
                //返回的分别是三个级别的选中位置
                String tx = options1Items.get(options1).getPickerViewText()
                        + options2Items.get(options1).get(option2)
                        + options3Items.get(options1).get(option2).get(options3).getPickerViewText();
                mTvAddress.setText(tx);
//                vMasker.setVisibility(View.GONE);
            }
        });
    }
}
