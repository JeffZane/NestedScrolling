package com.zhy.stickynavlayout.demo2;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.zhy.stickynavlayout.R;
import com.zhy.stickynavlayout.view.TabFragment;

public class MainActivity2 extends FragmentActivity {
    private String[] mTitles = new String[]{"Top Reads", "Video News"};

    private FrameLayout mRootLayout;
    private StickyNavLayout2 mStickyNavLayout2;
    private TextView mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        initViews();
    }

    private void initViews() {
        mRootLayout = findViewById(R.id.fl_root);
        mStickyNavLayout2 = findViewById(R.id.sticky_nav_layout2);
        mToolbar = findViewById(R.id.id_stickynavlayout_toolbar);
        TabLayout tabLayout = findViewById(R.id.tab_layout);
        ImageView ivExpand = findViewById(R.id.iv_expand);
        ViewPager viewPager = findViewById(R.id.id_stickynavlayout_viewpager);

        ivExpand.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mStickyNavLayout2.showMenu();
            }
        });

        mStickyNavLayout2.setScrollListener(new StickyNavLayout2.ScrollListener() {
            @Override
            public void onScroll(int scrollY) {
                Log.d("yzg222", "onScroll: " + scrollY);
//                FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) mToolbar.getLayoutParams();
//                layoutParams.topMargin = -scrollY;
//                mToolbar.setLayoutParams(layoutParams);

//                mToolbar.scrollTo(0, scrollY);
//
//                ViewCompat.offsetTopAndBottom(mToolbar, -scrollY);

                mToolbar.setTranslationY(-scrollY);
            }
        });

        viewPager.setAdapter(new PagerAdapter(getSupportFragmentManager()));
        tabLayout.setupWithViewPager(viewPager);
    }

    private class PagerAdapter extends FragmentPagerAdapter {

        PagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int i) {
            return TabFragment.newInstance(mTitles[i]);
        }

        @Override
        public int getCount() {
            return 2;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            if (position == 0) {
                return "Top Reads";
            } else {
                return "Video News";
            }
        }
    }
}