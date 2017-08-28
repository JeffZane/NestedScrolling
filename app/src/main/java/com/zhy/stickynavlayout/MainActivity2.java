package com.zhy.stickynavlayout;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class MainActivity2 extends FragmentActivity {

    private HomePageRootLayout mHomePageRootLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        initViews();
    }

    private void initViews() {
        mHomePageRootLayout = findViewById(R.id.home_root_layout);
        mHomePageRootLayout.showToolbar();

        TabLayout tabLayout = findViewById(R.id.tab_layout);
        ImageView ivExpand = findViewById(R.id.iv_expand);
        ViewPager viewPager = findViewById(R.id.home_page_view_pager);

        ivExpand.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mHomePageRootLayout.showMenu();
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
            return TestFragment.newInstance(i);
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