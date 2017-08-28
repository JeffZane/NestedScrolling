package com.zhy.stickynavlayout;

import android.animation.ValueAnimator;
import android.content.Context;
import android.support.v4.view.NestedScrollingParent;
import android.support.v4.view.NestedScrollingParentHelper;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.OverScroller;

public class HomePageRootLayout extends FrameLayout implements NestedScrollingParent {

    private static final int SWIPE_SLOP = 80;
    private static final int OPEN_MENU_DURING = 300;
    private static final int OPEN_MENU_MIN_DURING = 100;

    private static final int STATE_OPENED = 1;
    private static final int STATE_CLOSED = 2;
    private static final int STATE_SCROLLED = 3; //todo: remove

    private int mState;

    private NestedScrollingParentHelper parentHelper = new NestedScrollingParentHelper(this);
    private View mMenuLayout;
    private View mContentLayout;
    private View mToolbar;
    private View mTabLayout;
    private ScrollableViewPager mViewPager;
    private int mMenuLayoutHeight;
    private OverScroller mScroller;

    public HomePageRootLayout(Context context, AttributeSet attrs) {
        super(context, attrs);

        mScroller = new OverScroller(context);
    }

    public void showToolbar() {
        if (mToolbar == null) {
            mToolbar = View.inflate(getContext(), R.layout.layout_toolbar, null);
        }

        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.WRAP_CONTENT);
        addView(mToolbar, layoutParams);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

        mContentLayout = findViewById(R.id.home_page_content_layout);
        mMenuLayout = findViewById(R.id.home_page_menu_layout);
        mTabLayout = findViewById(R.id.home_page_tab_layout);
        mViewPager = findViewById(R.id.home_page_view_pager);

        mState = STATE_OPENED;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        mMenuLayoutHeight = mMenuLayout.getMeasuredHeight();

        //上面测量的结果是viewPager的高度只能占满父控件的剩余空间
        //重新设置viewPager的高度
        ViewGroup.LayoutParams layoutParams = mViewPager.getLayoutParams();
        layoutParams.height = getMeasuredHeight() - mTabLayout.getMeasuredHeight();
        mViewPager.setLayoutParams(layoutParams);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
    }

    public void scrollContentTo(int y) {
        Log.e("scrollContentTo", "y: " + y);

        int toolbarOffset = 0;
        if (y <= 0) {
            y = 0;
            changeState(STATE_OPENED);

            toolbarOffset = 0;
        } else if (y >= getScrollRange()) {
            toolbarOffset = getToolbarHeight();

            y = getScrollRange();
            changeState(STATE_CLOSED);
        } else {
            if (y > mMenuLayoutHeight) {
                toolbarOffset = getContentScrollY() - mMenuLayoutHeight;
            } else if (y <= mMenuLayoutHeight) {
                toolbarOffset = 0;
            }

            changeState(STATE_SCROLLED);
        }

        mContentLayout.scrollTo(0, y);
        if (mTabLayout != null) {
            mToolbar.scrollTo(0, toolbarOffset);
        }
    }

    public int getContentScrollY() {
        return mContentLayout.getScrollY();
    }

    @Override
    public void computeScroll() {
        if (mScroller.computeScrollOffset()) {
            scrollContentTo(mScroller.getCurrY());
            invalidate();
        }
    }

    public void showMenu() {
        if (mState != STATE_OPENED) {
            animScroll(getContentScrollY(), 0);
        }
    }

    public void closeMenu() {
        if (mState != STATE_CLOSED) {
            animScroll(getContentScrollY(), getScrollRange());
        }
    }

    private void handleActionUp() {
        if (mState != STATE_SCROLLED) {
            return;
        }

        if (getContentScrollY() >= SWIPE_SLOP) { //close
            Log.e("handleActionUp", "call closeMenu()");
            closeMenu();
        } else {
            Log.e("handleActionUp", "call showMenu()");
            showMenu();
        }
    }

    private void animScroll(int start, int end) {
        ValueAnimator anim = ValueAnimator.ofInt(start, end);
        int during = (int) (OPEN_MENU_DURING * (Math.abs(start - end) / (float) getScrollRange()));
        anim.setDuration(Math.max(OPEN_MENU_MIN_DURING, during));
        Log.e("animScroll", "start: " + start + ", end: " + end + ", during: " + Math.max(OPEN_MENU_MIN_DURING, during));
        anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int currentValue = (int) animation.getAnimatedValue();
                scrollContentTo(currentValue);
            }
        });
        anim.start();
    }

    private void changeState(int newState) {
        if (mState != newState) {
            mState = newState;
            if (mState == STATE_OPENED) {
                Log.e("changeState", "------------- STATE_OPENED -------------");
//                if (mPagerStateListener != null) {
//                    mPagerStateListener.onPagerOpened();
//                }
            } else if (mState == STATE_CLOSED) {
                Log.e("changeState", "------------- STATE_CLOSED -------------");

//                if (mPagerStateListener != null) {
//                    mPagerStateListener.onPagerClosed();
//                }
            } else {
                Log.e("changeState", "------------- STATE_SCOLLED -------------");
            }
        }
    }

    private int getScrollRange() {
        return mMenuLayoutHeight + getToolbarHeight();
    }

    private int getToolbarHeight() {
        return getResources().getDimensionPixelOffset(R.dimen.home_page_toolbar_height);
    }

//实现NestedScrollParent接口-------------------------------------------------------------------------


    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
//        Log.e("TouchEvent", "dispatchTouchEvent " + ev.getAction());
        if (ev.getAction() == MotionEvent.ACTION_UP) {
            Log.e("onInterceptTouchEvent", "ACTION_UP ScrollY: " + getContentScrollY());
            handleActionUp();
        }
        return super.dispatchTouchEvent(ev);
    }

    @Override
    public boolean onStartNestedScroll(View child, View target, int nestedScrollAxes) {
        Log.e("onStartNestedScroll", "called");
        return (nestedScrollAxes & ViewCompat.SCROLL_AXIS_VERTICAL) != 0;
    }

    @Override
    public void onNestedScrollAccepted(View child, View target, int nestedScrollAxes) {
        parentHelper.onNestedScrollAccepted(child, target, nestedScrollAxes);
    }

    @Override
    public void onStopNestedScroll(View target) {
        Log.e("onStopNestedScroll", "called");
        mViewPager.setScrollable(true);
        parentHelper.onStopNestedScroll(target);
    }

    @Override
    public void onNestedScroll(View target, int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed) {
    }

    @Override
    public void onNestedPreScroll(View target, int dx, int dy, int[] consumed) {
        // target: RecyclerView
        Log.e("onNestedPreScroll", "called, scrollY: " + getContentScrollY());

        boolean enableNestedScrollUp = dy > 0 && getContentScrollY() < getScrollRange(); //上滑且顶部控件未完全隐藏
        boolean enableNestedScrollDown = dy < 0 && getContentScrollY() > 0 && getContentScrollY() < getScrollRange(); //上滑且顶部控件未完全隐藏
        if (enableNestedScrollUp || enableNestedScrollDown) {
            mViewPager.setScrollable(false);

            scrollContentTo(dy + getContentScrollY());
            consumed[1] = dy;
            Log.e("onNestedPreScroll", "consumed");
        }
    }

    //boolean consumed:子view是否消耗了fling
    //返回值：自己是否消耗了fling。可见，要消耗只能全部消耗
    @Override
    public boolean onNestedFling(View target, float velocityX, float velocityY, boolean consumed) {
        Log.e("onNestedFling", "called");
        return false;
    }

    //返回值：自己是否消耗了fling。可见，要消耗只能全部消耗
    @Override
    public boolean onNestedPreFling(View target, float velocityX, float velocityY) {
        Log.e("onNestedPreFling", "called, scrollY: " + getContentScrollY());
        return false;
    }

    @Override
    public int getNestedScrollAxes() {
        return parentHelper.getNestedScrollAxes();
    }
}