package com.example.treeco;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;

public class EventDetails extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_details);
        ImageView backbtn = findViewById(R.id.backimage);
        backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), UserDashboard.class);
                startActivity(intent);
            }
        });
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        UpcomingEventFragment fragment1 = new UpcomingEventFragment();
        fragmentTransaction.replace(R.id.fragmentadd, fragment1);
        fragmentTransaction.commit();
        // Define ActionBar object
        ActionBar actionBar;
        actionBar = getSupportActionBar();
        // Define ColorDrawable object and parse color
        // using parseColor method
        // with color hash code as its parameter
        ColorDrawable colorDrawable
                = new ColorDrawable(Color.parseColor("#32CB00"));
        // Set BackgroundDrawable
        actionBar.setBackgroundDrawable(colorDrawable);
        // actionBar.setTitle(Html.fromHtml("<font color='#ffffff'>tre</font>"+"<font color='#ffffff' face='sans-serif-black'>e</font>"+"<font color='#ffffff'>co</font>"));
        actionBar.setTitle(Html.fromHtml("<font color=#ffffff>" + "<small>"
                + "tre" + "</small>" + "" + "<big>" + "e"
                + "</big>" + "" + "<small>" + "co" + "</small>"));
        //Tab action
        TabLayout tabLayout = findViewById(R.id.tablayout);
        tabLayout.setSelectedTabIndicatorColor(getResources().getColor(R.color.trees_near_by_color));
        tabLayout.setTabTextColors(getResources().getColor(R.color.this_feature_isn_t_available_color), getResources().getColor(R.color.black));
        //floating window code
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Intent to createevent
                Intent i = new Intent(getApplicationContext(), CreateEvent.class);
                startActivity(i);
            }
        });
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {

                //tabLayout.setTabTextColors(getResources().getColor(R.color.this_feature_isn_t_available_color), getResources().getColor(R.color.trees_near_by_color));
                //tabLayout.setSelectedTabIndicatorColor(getResources().getColor(R.color.trees_near_by_color));


                if (tabLayout.getSelectedTabPosition() == 0) {
                    try {
                        FragmentManager fragmentManager = getSupportFragmentManager();
                        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                        UpcomingEventFragment fragment1 = new UpcomingEventFragment();
                        fragmentTransaction.replace(R.id.fragmentadd, fragment1);
                        fragmentTransaction.commit();
                    } catch (Exception e) {
                        //Toast.makeText(EventDetails.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }

                } else if (tabLayout.getSelectedTabPosition() == 1) {
                    try {
                        FragmentManager fragmentManager = getSupportFragmentManager();
                        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                        MyEventFragment fragment2 = new MyEventFragment();
                        fragmentTransaction.replace(R.id.fragmentadd, fragment2);
                        fragmentTransaction.commit();
                    } catch (Exception e) {
                        //Toast.makeText(EventDetails.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }

                }

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

    }
}