package com.example.treeco;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

public class    RegistrationSuccessful extends AppCompatActivity implements View.OnClickListener {
    TextView tveditmemberval;
    ImageView imgdone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration_successful);
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
        actionBar.setTitle(Html.fromHtml("<font color=#ffffff>" + "<small>"
                + "tre" + "</small>" + "" + "<big>" + "e"
                + "</big>" + "" + "<small>" + "co" + "</small>"));
        tveditmemberval = findViewById(R.id.membershipval);
        imgdone = findViewById(R.id.doneimg);
        imgdone.setOnClickListener(this);
        String membershipval = "";
        if (RegistrationActivity.setMembershipVal == 1) {
            membershipval = "Free";
        }
        if (RegistrationActivity.setMembershipVal == 2) {
            membershipval = "Silver";

        }
        if (RegistrationActivity.setMembershipVal == 3) {
            membershipval = "Gold";
        }
        tveditmemberval.setText("You have successfully registered as a " + membershipval + " member..Please check your Registered mail id for password..");

    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.doneimg) {
            Intent i = new Intent(this, LoginActivity.class);
            startActivity(i);
        }

    }
}