package com.example.treeco;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class success_pass extends AppCompatActivity implements View.OnClickListener {

    TextView emailTextView;
    ImageView signInImg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_success_pass);
        getSupportActionBar().hide();

        emailTextView = findViewById(R.id.emailTextView);
        signInImg = findViewById(R.id.signInImg);
        signInImg.setOnClickListener(this);


        String email = getIntent().getStringExtra("email");
        int length = email.length();
        String maskedEmail = "";
        if (length > 6) {
            maskedEmail = "******" + email.substring(length - 13, length);
        } else {
            maskedEmail = email;
        }
        emailTextView.setText("Please check your " + maskedEmail);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.signInImg) {
            startActivity(new Intent(success_pass.this, LoginActivity.class));
            finish();
        }
    }
}