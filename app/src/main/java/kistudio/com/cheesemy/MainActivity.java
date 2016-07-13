package kistudio.com.cheesemy;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.View;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    //Section adapter for tabs in MainActivity
    private SectionsPagerAdapter mSectionsPagerAdapter;

    //ViewPager for tabs in MainActivity
    private ViewPager mViewPager;

    //Number of tabs in MainActivity. Now you can change it and number of tabs will change too.
    //I used an arraylist for storage all fragments and size of it is the same.
    private static final int PAGES_COUNT = 5;

    //Array list of all fragments in tabs. If you want to show not standard (CheeseListFragment) fragment,
    // you may add it to collection and not using initFragmentList method for default initiation.
    private ArrayList<CheeseListFragment> cheeseListFragmentList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // initiation of UI views
        initUI();

        // define array list of Fragments, we make it only once when activity create.
        // We don't change content until activity not destroyed.
        initFragmentList();
    }

    private void initUI() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            }
        });
    }

    // Use android:launchMode="singleTop" that's why activity doesn't recreating after return from CheeseDetailActivity with HomeAsUp button

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
    }

    // Using our PAGES_COUNT creating array list of fragments
    private void initFragmentList() {
        cheeseListFragmentList = new ArrayList<>(PAGES_COUNT);
        for (int i =1; i<=PAGES_COUNT; i++){
            cheeseListFragmentList.add(new CheeseListFragment());
        }
    }

    // section pager adapter using array list of fragments and PAGES_COUNT show tabs
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return cheeseListFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return PAGES_COUNT;
        }

        @Override
        public CharSequence getPageTitle(int position) {

            return "SECTION "+position;
        }
    }
}
