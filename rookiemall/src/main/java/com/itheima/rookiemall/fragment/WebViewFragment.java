package com.itheima.rookiemall.fragment;

import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.itheima.rookiemall.R;
import com.itheima.rookiemall.base.BaseFragment;
import com.itheima.rookiemall.bean.HomeHotBean;
import com.itheima.rookiemall.config.Urls;
import com.itheima.rookiemall.utils.CarProvider;
import com.itheima.rookiemall.utils.L;
import com.itheima.rookiemall.utils.UIUtils;
import com.itheima.rookiemall.widget.CustomToolBar;
import com.itheima.rookiemall.widget.CustomWebView;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;

/**
 * Created by lyl on 2016/10/10.
 */

public class WebViewFragment extends BaseFragment {
    @BindView(R.id.web_view)
    CustomWebView mWebView;

    WebViewJavascriptInterface mJavascriptInterface;
    private HomeHotBean.ItemHomeHotBean mItemHomeHotBean;

    @Override
    public int getContentViewId() {
        return R.layout.fragment_webview_layout;
    }

    @Override
    protected void initToolbar(CustomToolBar toolBar) {
        super.initToolbar(toolBar);
        toolBar.setTitle("商品详情");
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
        mJavascriptInterface = new WebViewJavascriptInterface();
        mWebView.addJavascriptInterface(mJavascriptInterface, "appInterface");
        mWebView.setWebViewClient(new MyViewClient());

        mItemHomeHotBean = EventBus.getDefault().removeStickyEvent(HomeHotBean.ItemHomeHotBean.class);
        mWebView.loadUrl(Urls.getBaseUrl() + Urls.api_details_webview);

    }


    class WebViewJavascriptInterface {
        @JavascriptInterface
        public void showDetail() {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mWebView.loadUrl("javascript:showDetail(" + mItemHomeHotBean.id + ")");
                }
            });
        }

        //立即购买
        @JavascriptInterface
        public void buy(long id) {
            L.i("立即购买:" + id);
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    UIUtils.showDefToast(mContext, "立即购买");
                    CarProvider.getInstance(mContext).put(mItemHomeHotBean);
                    startDetailsActivity(R.layout.fragment_submit_order, false);
                }
            });
        }

        //添加到购物车
        @JavascriptInterface
        public void addToCart(long id) {
            L.i("添加到购物车:" + id);
            CarProvider.getInstance(mContext).put(mItemHomeHotBean);
            UIUtils.showDefToast(mContext, "添加到购物车");
        }


        //收藏
        @JavascriptInterface
        public void addFavorites(long id) {
            L.i("收藏:" + id);
        }

    }

    class MyViewClient extends WebViewClient {

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            mJavascriptInterface.showDetail();
        }

    }


    @Override
    public void initListener() {

    }
}
