package com.itheima.rookiemall.widget;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.itheima.rookiemall.R;

/**
 * Created by lyl on 2016/10/1.
 */

public class CustomToolBar extends Toolbar implements View.OnClickListener {
    private ViewGroup mToolBarRootView;
    private LayoutParams mParams;
    private ImageView mIvIcon;
    private TextView mTvTitle;
    private TextView mTvRightView;
    private SearchView mSearchView;


    /**
     * 返回按钮点击事件
     */
    private OnClickListener mBackOnClickListener;

    /**
     * 左边按钮点击事件
     */
    private OnClickListener mRightOnClickListener;

    public CustomToolBar(Context context) {
        this(context, null, 0);
    }

    public CustomToolBar(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public void hideAllView() {
        mIvIcon.setVisibility(View.GONE);
        mTvTitle.setVisibility(View.GONE);
        mTvRightView.setVisibility(View.GONE);
        mSearchView.setVisibility(View.GONE);
    }

    public CustomToolBar(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    private void initView() {
        if (mToolBarRootView == null) {
            mToolBarRootView = (ViewGroup) View.inflate(getContext(), R.layout.item_toolbar, null);
            mTvTitle = (TextView) mToolBarRootView.findViewById(R.id.tv_title);
            mIvIcon = (ImageView) mToolBarRootView.findViewById(R.id.iv_back_icon);
            mTvRightView = (TextView) mToolBarRootView.findViewById(R.id.tv_rightView);
            mSearchView = (SearchView) mToolBarRootView.findViewById(R.id.search_view);
            mParams = new LayoutParams(-1, -1, Gravity.CENTER);
            addView(mToolBarRootView, mParams);

            mTvRightView.setOnClickListener(this);
            mIvIcon.setOnClickListener(this);
        }
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_rightView:
                if (mRightOnClickListener != null) {
                    mRightOnClickListener.onClick(v);
                }
                break;
            case R.id.iv_back_icon:
                if (mBackOnClickListener != null) {
                    mBackOnClickListener.onClick(v);
                }
                break;

        }

    }

    public void addView(View v) {
        mToolBarRootView.addView(v);
    }

    public void setShowSearchView() {
        mSearchView.setVisibility(View.VISIBLE);
    }

    @Override
    public void setNavigationIcon(@Nullable Drawable icon) {
        if (mIvIcon == null) {
            initView();
        }
        mIvIcon.setVisibility(VISIBLE);
        mIvIcon.setBackgroundDrawable(icon);
    }

    public void setRightViewBackground(int resId) {
        mTvRightView.setVisibility(View.VISIBLE);
        mTvRightView.setText("");
        mTvRightView.setBackgroundResource(resId);
    }

    @Override
    public void setTitle(@StringRes int resId) {
        setTitle(getContext().getString(resId));
    }

    @Override
    public void setTitle(CharSequence title) {
        initView();
        mTvTitle.setVisibility(VISIBLE);
        mTvTitle.setText(title);
    }

    public void setRightTxt(String rigthTxt) {
        mTvRightView.setText(rigthTxt);
        mTvRightView.setVisibility(View.VISIBLE);
    }

    public void setRightViewClickListener(OnClickListener listener) {
        mRightOnClickListener = listener;
    }

    public void showBackView() {
        mIvIcon.setVisibility(VISIBLE);
    }

    public void setBackOnClickListener(OnClickListener listener) {

        mBackOnClickListener = listener;
    }
}
