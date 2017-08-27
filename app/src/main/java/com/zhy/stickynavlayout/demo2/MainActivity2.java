package com.zhy.stickynavlayout.demo2;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;

import com.zhy.stickynavlayout.R;
import com.zhy.stickynavlayout.view.TabFragment;

public class MainActivity2 extends FragmentActivity {
    private String[] mTitles = new String[]{"Top Reads", "Video News"};

    private StickyNavLayout2 mStickyNavLayout2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        initViews();
    }

    private void initViews() {
        mStickyNavLayout2 = findViewById(R.id.sticky_nav_layout2);
        TabLayout tabLayout = findViewById(R.id.tab_layout);
        ImageView ivExpand = findViewById(R.id.iv_expand);
        ViewPager viewPager = findViewById(R.id.id_stickynavlayout_viewpager);

        ivExpand.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mStickyNavLayout2.scrollTo(0, -2000);
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