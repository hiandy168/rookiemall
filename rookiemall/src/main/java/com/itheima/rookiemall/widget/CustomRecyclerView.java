package com.itheima.rookiemall.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;

import com.itheima.rookiemall.R;

/**
 * Created by lyl on 2016/10/3.
 */

public class CustomRecyclerView extends RecyclerView {
    private int mSpanCount;

    public CustomRecyclerView(Context context) {
        this(context, null);
    }

    public CustomRecyclerView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CustomRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.custom_recyclerView, defStyle, 0);
        if (typedArray != null) {
            mSpanCount = typedArray.getInt(R.styleable.custom_recyclerView_spanCount, 0);
            typedArray.recycle();
        }
        setSpanCount(mSpanCount);
    }

    public void setSpanCount(int spanCount) {
        mSpanCount = spanCount;
        setLayoutManagerType();
    }

/*    @Override
    protected void onMeasure(int widthSpec, int heightSpec) {
        super.onMeasure(widthSpec, MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2, MeasureSpec.AT_MOST));
    }*/

    public int getSpanCount() {
        return mSpanCount;
    }

    private void setLayoutManagerType() {
        LayoutManager layoutManager;
        if (getSpanCount() > 0) {
            layoutManager = new GridLayoutManager(getContext(), getSpanCount());
        } else {
            layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        }
        setLayoutManager(layoutManager);

    }
}
