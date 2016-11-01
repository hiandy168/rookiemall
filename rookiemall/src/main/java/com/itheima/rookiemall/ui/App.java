package com.itheima.rookiemall.ui;

import android.app.Application;
import android.content.Intent;

import com.itheima.retrofitutils.RetrofitUtils;
import com.itheima.rookiemall.config.Urls;
import com.squareup.picasso.Picasso;

/**
 * Created by lyl on 2016/10/3.
 */

public class App extends Application {

    private static App sIntance;

    private Intent mIntent;

    @Override
    public void onCreate() {
        super.onCreate();
        sIntance = this;
        RetrofitUtils.init(this, Urls.getBaseUrl());
    }

    public static App getIntance() {
        return sIntance;
    }

    public void putIntent(Intent intent) {
        mIntent = intent;
    }

    public Intent getIntent() {
        Intent intent = mIntent;
        mIntent = null;
        return intent;
    }
}
