package com.major.covid19tracker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {

    //UI views
    private TextView titleTv;
    private ImageButton refreshBtn;
    private BottomNavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //init UI views
        titleTv=findViewById(R.id.titleTv);
        refreshBtn=findViewById(R.id.refreshBtn);
        navigationView=findViewById(R.id.navigationView);

        //refresh button clicks, refresh records
        refreshBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        navigationView.setOnNavigationItemSelectedListener(this);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        //handle bottom nav item clicks
        switch (item.getItemId())
        {
            case R.id.nav_home:
                //load home data
                return true;
            case R.id.nav_stats:
                //load stats
                return true;
        }
        return false;
    }
}