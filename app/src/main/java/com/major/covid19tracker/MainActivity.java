package com.major.covid19tracker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class    MainActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {

    //UI views
    private TextView titleTv;
    private ImageButton refreshBtn;
    private BottomNavigationView navigationView;

    //Fragments
    private Fragment homeFragment, statsFragment;
    private Fragment activeFragment;
    private FragmentManager fragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //init UI views
        titleTv=findViewById(R.id.titleTv);
        refreshBtn=findViewById(R.id.refreshBtn);
        navigationView=findViewById(R.id.navigationView);

        initFragments();

        //refresh button clicks, refresh records
        refreshBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                homeFragment.onResume();

            }
        });

        navigationView.setOnNavigationItemSelectedListener(this);
    }

    private void initFragments() {

        //init fragments

        homeFragment = new HomeFragment();
        statsFragment = new StatsFragment();

        fragmentManager = getSupportFragmentManager();
        activeFragment = homeFragment;

        fragmentManager.beginTransaction()
                .add(R.id.frame, homeFragment, "homeFragment")
                .commit();

        fragmentManager.beginTransaction()
                .add(R.id.frame, statsFragment, "statsFragment")
                .hide(statsFragment)
                .commit();
    }

    private void loadHomeFragment() {
        titleTv.setText("Home");

        fragmentManager.beginTransaction().hide(activeFragment).show(homeFragment).commit();
        activeFragment = homeFragment;

    }

    private void loadStatsFragment() {
        titleTv.setText("COVID-19 Stats");

        fragmentManager.beginTransaction().hide(activeFragment).show(statsFragment).commit();
        activeFragment = statsFragment;
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        //handle bottom nav item clicks
        switch (item.getItemId())
        {
            case R.id.nav_home:
                loadHomeFragment();
                return true;
            case R.id.nav_stats:
                loadStatsFragment();
                return true;
        }
        return false;
    }
}