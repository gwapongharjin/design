package com.m3das.biomech.design;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ScrollView;

public class CustomScroll extends ScrollView {
    OnBottomReachedListener mListener;

    public CustomScroll(Context context, AttributeSet attrs,
                        int defStyle) {
        super(context, attrs, defStyle);
    }

    public CustomScroll(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CustomScroll(Context context) {
        super(context);
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        View view = getChildAt(getChildCount() - 1);
        int diff = (view.getBottom() - (getHeight() + getScrollY())) -  view.getPaddingBottom();

        if (diff <= 0 && mListener != null) {
            mListener.onBottomReached();
        }

        super.onScrollChanged(l, t, oldl, oldt);
    }

    // Getters & Setters

    public OnBottomReachedListener getOnBottomReachedListener() {
        return mListener;
    }

    public void setOnBottomReachedListener(
            OnBottomReachedListener onBottomReachedListener) {
        mListener = onBottomReachedListener;
    }

    //Event listener.

    public interface OnBottomReachedListener {
        void onBottomReached();
    }
}