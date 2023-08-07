package com.example.treeco;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Html;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.andrognito.patternlockview.PatternLockView;
import com.andrognito.patternlockview.listener.PatternLockViewListener;
import com.andrognito.patternlockview.utils.PatternLockUtils;

import java.util.List;

public class InputPasswordActivity extends AppCompatActivity {
    PatternLockView mPatternLockView;
    String password;
    TextView tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input_password);
        // Define ActionBar object
        ActionBar actionBar;
        actionBar = getSupportActionBar();
        tv = findViewById(R.id.textView);
        // Define ColorDrawable object and parse color
        // using parseColor method
        // with color hash code as its parameter
        ColorDrawable colorDrawable
                = new ColorDrawable(Color.parseColor("#32CB00"));
        // Set BackgroundDrawable
        actionBar.setBackgroundDrawable(colorDrawable);
        actionBar.setTitle(Html.fromHtml("<font color=#ffffff>" + "<small>"
                + "tre" + "</small>" + "" + "<big>" + "e"
                + "</big>" + "" + "<small>" + "co" + "</small>"));
        // shared preference when user comes second time to the app
        SharedPreferences sharedPreferences = getSharedPreferences("PREFS", 0);
        password = sharedPreferences.getString("password", "0");

        mPatternLockView = (PatternLockView) findViewById(R.id.pattern_lock_view);
        mPatternLockView.addPatternLockListener(new PatternLockViewListener() {
            @Override
            public void onStarted() {

            }

            @Override
            public void onProgress(List<PatternLockView.Dot> progressPattern) {

            }

            @Override
            public void onComplete(List<PatternLockView.Dot> pattern) {
                // if drawn pattern is equal to created pattern you will navigate to home screen
                if (password.equals(PatternLockUtils.patternToString(mPatternLockView, pattern))) {
                    tv.setText("Wrong Pattern");
                    Intent intent = new Intent(getApplicationContext(), UserDashboard.class);
                    startActivity(intent);
                    finish();
                    //Toast.makeText(InputPasswordActivity.this,"Correct pattern",Toast.LENGTH_LONG).show();
                } else {
                    // other wise you will get error wrong password
                    //Toast.makeText(InputPasswordActivity.this, "Wrong Password", Toast.LENGTH_SHORT).show();
                    tv.setText("Wrong Pattern");
                    mPatternLockView.clearPattern();
                }
            }

            @Override
            public void onCleared() {

            }
        });
    }
}