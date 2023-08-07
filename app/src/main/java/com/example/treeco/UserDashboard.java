package com.example.treeco;

import static android.Manifest.permission.ACCESS_COARSE_LOCATION;
import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static android.Manifest.permission.CAMERA;
import static android.Manifest.permission.MANAGE_EXTERNAL_STORAGE;
import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class UserDashboard extends AppCompatActivity {
    private static final int PERMISSION_REQUEST_CODE = 200;
    BottomNavigationView bottomNavigation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_dashboard);

        //ActionBar actionBar;
        //actionBar = getSupportActionBar();
        getSupportActionBar().hide();
        // Define ColorDrawable object and parse color
        // using parseColor method
        // with color hash code as its parameter
        ColorDrawable colorDrawable
                = new ColorDrawable(Color.parseColor("#32CB00"));
        // Set BackgroundDrawable
        //actionBar.setBackgroundDrawable(colorDrawable);
        //actionBar.setTitle(Html.fromHtml("<font color='#ffffff'>treeco </font>"));
        /*actionBar.setTitle(Html.fromHtml("<font color=#ffffff>" + "<small>"
                + "tre" + "</small>" + "" + "<big>" + "e"
                + "</big>" + "" + "<small>" + "co" + "</small>"));*/

        if (!checkPermission()) {
            requestPermission();
        }

        bottomNavigation = findViewById(R.id.bottom_navigation);
        FragmentManager fragmentManager1 = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction1 = fragmentManager1.beginTransaction();
        HomeFragment1 fragment2 = new HomeFragment1();
        fragmentTransaction1.replace(R.id.container, fragment2);
        fragmentTransaction1.commit();

        BottomNavigationView.OnNavigationItemSelectedListener navigationItemSelectedListener =
                new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.navigation_home:
                                FragmentManager fragmentManager1 = getSupportFragmentManager();
                                FragmentTransaction fragmentTransaction1 = fragmentManager1.beginTransaction();
                                HomeFragment1 fragment2 = new HomeFragment1();
                                fragmentTransaction1.replace(R.id.container, fragment2);
                                fragmentTransaction1.commit();
                                return true;
                            case R.id.navigation_activities:
                                try {
                                    FragmentManager fragmentManageractivity = getSupportFragmentManager();
                                    FragmentTransaction fragmentTransactionactivity = fragmentManageractivity.beginTransaction();
                                    ActivityFragment fragmentActivity1 = new ActivityFragment();
                                    fragmentTransactionactivity.replace(R.id.container, fragmentActivity1);
                                    fragmentTransactionactivity.commit();
                                    return true;
                                } catch (Exception e) {
                                    //Toast.makeText(UserDashboard.this,e.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                                return true;
                            case R.id.navigation_treemap:
                                try {
                                    FragmentManager fragmentManager = getSupportFragmentManager();
                                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                                    TreeMapFragment1 fragment1 = new TreeMapFragment1();
                                    fragmentTransaction.replace(R.id.container, fragment1);
                                    fragmentTransaction.commit();
                                    return true;
                                } catch (Exception e) {
                                    //Toast.makeText(UserDashboard.this,e.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                                return true;
                            case R.id.navigation_info:
                                Toast.makeText(UserDashboard.this, "info clicked", Toast.LENGTH_LONG).show();
                                return true;
                            case R.id.navigation_profile:
                                //Toast.makeText(UserDashboard.this,"profile clicked",Toast.LENGTH_LONG).show();
                                FragmentManager fragmentManagerProfile = getSupportFragmentManager();
                                FragmentTransaction fragmentTransactionProfile = fragmentManagerProfile.beginTransaction();
                                ProfileFragment fragmentprofile = new ProfileFragment();
                                fragmentTransactionProfile.replace(R.id.container, fragmentprofile);
                                fragmentTransactionProfile.commit();
                                return true;
                        }
                        return false;
                    }
                };
        bottomNavigation.setOnNavigationItemSelectedListener(navigationItemSelectedListener);
    }

    private boolean checkPermission() {
        int result = ContextCompat.checkSelfPermission(getApplicationContext(), ACCESS_COARSE_LOCATION);
        int result1 = ContextCompat.checkSelfPermission(getApplicationContext(), ACCESS_FINE_LOCATION);
        int result2 = ContextCompat.checkSelfPermission(getApplicationContext(), CAMERA);
        int result3 = ContextCompat.checkSelfPermission(getApplicationContext(), WRITE_EXTERNAL_STORAGE);
        int result4 = ContextCompat.checkSelfPermission(getApplicationContext(), READ_EXTERNAL_STORAGE);
        int result5 = ContextCompat.checkSelfPermission(getApplicationContext(), MANAGE_EXTERNAL_STORAGE);
        return result == PackageManager.PERMISSION_GRANTED && result1 == PackageManager.PERMISSION_GRANTED && result2 == PackageManager.PERMISSION_GRANTED && result3 == PackageManager.PERMISSION_GRANTED && result4 == PackageManager.PERMISSION_GRANTED && result5 == PackageManager.PERMISSION_GRANTED;
    }

    private void requestPermission() {
        ActivityCompat.requestPermissions(this, new String[]{ACCESS_COARSE_LOCATION, ACCESS_FINE_LOCATION, CAMERA, WRITE_EXTERNAL_STORAGE, READ_EXTERNAL_STORAGE, MANAGE_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE);
    }


}