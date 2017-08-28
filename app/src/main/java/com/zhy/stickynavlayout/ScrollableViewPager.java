package com.zhy.stickynavlayout;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

public class ScrollableViewPager extends ViewPager {

    public ScrollableViewPager(Context context) {
        super(context);
    }

    public ScrollableViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    private boolean mScrollable = true;

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        return mScrollable && super.onTouchEvent(ev);

    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent arg0) {
        return mScrollable && super.onInterceptTouchEvent(arg0);
    }

    public void setScrollable(boolean isCanScroll) {
        this.mScrollable = isCanScroll;
    }
}
