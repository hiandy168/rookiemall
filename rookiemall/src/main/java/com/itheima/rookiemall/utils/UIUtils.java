package com.itheima.rookiemall.utils;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.itheima.rookiemall.R;
import com.itheima.rookiemall.ui.App;

import java.lang.ref.WeakReference;

/**
 * Created by lyl on 2016/10/3.
 */

public class UIUtils {


    private static WeakReference<Toast> sToast;
    private static WeakReference<Toast> sDefToast;
    //状态栏的高度
    static int sStatusBarHeight = 0;
    static int sScreenWidth = 0;
    static int sScreenHeight = 0;
    static int sToastHeight = 0;
    private static ToastTask sToastTask;

    private static Toast getToast(Context context) {
        if (sToast == null || sToast.get() == null) {
            sToast = new WeakReference<Toast>(Toast.makeText(context, null, Toast.LENGTH_SHORT));
            int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
            if (resourceId > 0) {
                //根据资源ID获取响应的尺寸值
                sStatusBarHeight = context.getResources().getDimensionPixelSize(resourceId);
            }
            WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
            DisplayMetrics outMetrics = new DisplayMetrics();
            wm.getDefaultDisplay().getMetrics(outMetrics);
            sScreenWidth = outMetrics.widthPixels;
            sScreenHeight = outMetrics.heightPixels;
            sToastHeight = SizeUtils.dp2px(context, 26);
            sToastTask = new ToastTask(context);
        }
        return sToast.get();
    }

//    public static void showToast(Context context, String txt) {
//        Toast toast = getToast(context);
//        toast.setText(txt);
//        toast.show();
//
//    }

    private static Toast getDefToast(Context context) {
        if (sDefToast == null || sDefToast.get() == null) {
            sDefToast = new WeakReference<Toast>(Toast.makeText(context, null, Toast.LENGTH_SHORT));
        }
        return sDefToast.get();
    }

    public static void showDefToast(Context context, String txt) {
        getToast(context);
        sToastTask.setText(txt);
        if (Thread.currentThread().getId() == Looper.getMainLooper().getThread().getId()) {
            sToastTask.run();
        } else {
            new Handler(Looper.getMainLooper()).post(sToastTask);
        }

    }

    public static void showDefToast(String txt) {
        getToast(App.getIntance());
        sToastTask.setText(txt);
        if (Thread.currentThread().getId() == Looper.getMainLooper().getThread().getId()) {
            sToastTask.run();
        } else {
            new Handler(Looper.getMainLooper()).post(sToastTask);
        }

    }

    static class ToastTask implements Runnable {
        String mTxt;
        Context mContext;

        public ToastTask(Context context) {
            mContext = context;
        }

        public void setText(String txt) {

            this.mTxt = txt;

        }

        @Override
        public void run() {
            Toast toast = getDefToast(mContext);
            toast.setText(mTxt);
            toast.show();
        }
    }

    public static void showFullScreenToast(Context context, View parentView, String txt) {
        Toast toast = getToast(context);
        int viewScreenLocation[] = new int[2];
        parentView.getLocationOnScreen(viewScreenLocation);
        toast.setGravity(Gravity.TOP, 0, viewScreenLocation[1] + parentView.getHeight() - sStatusBarHeight);
        LinearLayout toastView = (LinearLayout) toast.getView();
        toastView.removeAllViews();
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(sScreenWidth, sToastHeight);
        layoutParams.setMargins(0, 0, 0, 0);
        toastView.setLayoutParams(layoutParams);
        toastView.setPadding(0, 0, 0, 0);


        TextView tv = new TextView(context);
        int padding = SizeUtils.dp2px(context, 3);
        tv.setPadding(padding, padding, padding, padding);
        LinearLayout.LayoutParams vlp = new LinearLayout.LayoutParams(sScreenWidth, SizeUtils.dp2px(context, 26));
        vlp.setMargins(0, 0, 0, 0);
        tv.setLayoutParams(vlp);
        tv.setText(txt);
        tv.setBackgroundResource(R.color.colorPrimary);
        tv.setGravity(Gravity.CENTER);

        toastView.addView(tv);
        toast.show();
    }

}
