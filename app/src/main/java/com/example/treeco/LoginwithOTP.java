package com.example.treeco;

import static android.Manifest.permission.READ_SMS;
import static android.Manifest.permission.RECEIVE_SMS;

import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.auth.api.phone.SmsRetriever;
import com.google.android.gms.auth.api.phone.SmsRetrieverClient;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LoginwithOTP extends AppCompatActivity implements View.OnClickListener {

    private static final int REQ_USER_CONSENT = 200;
    private static final int PERMISSION_REQUEST_CODE = 200;
    public static String MobileNumberLogin = "", OTPvalfetchedforLogin = "";
    EditText edMobNum;
    SmsBroadcastReceiver smsBroadcastReceiver;
    private ProgressBar loadingPB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loginwith_otp);
        getSupportActionBar().hide();
        // Define ActionBar object
        //ActionBar actionBar;
        //actionBar = getSupportActionBar();
        // Define ColorDrawable object and parse color
        // using parseColor method
        // with color hash code as its parameter
        ColorDrawable colorDrawable
                = new ColorDrawable(Color.parseColor("#32CB00"));

        // Set BackgroundDrawable
        /*actionBar.setBackgroundDrawable(colorDrawable);
        actionBar.setTitle(Html.fromHtml("<font color=#ffffff>" + "<small>"
                + "tre" + "</small>" + "" + "<big>" + "e"
                + "</big>" + "" + "<small>" + "co" + "</small>"));*/
        if (!checkPermission()) {
            requestPermission();
        }
        TextView tvskip = findViewById(R.id.skip);
        edMobNum = findViewById(R.id.edmobile);
        loadingPB = findViewById(R.id.idLoadingPB);
        ImageView signup = findViewById(R.id.signupbtn);
        tvskip.setOnClickListener(this);
        signup.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.skip) {

            Intent i = new Intent(this,
                    LoginActivity.class);
            //Intent is used to switch from one activity to another.
            startActivity(i);

        }
        if (v.getId() == R.id.signupbtn) {
            if (edMobNum.getText().toString().equalsIgnoreCase("")) {
                Toast.makeText(this, "Enter Mobile Number", Toast.LENGTH_LONG).show();

            } else {
                MobileNumberLogin = edMobNum.getText().toString();

                postDataUsingVolley(MobileNumberLogin);
                startSmartUserConsent();
            }


        }
    }

    private void postDataUsingVolley(String MobileNum) {
        // url to post our data
        String url = VariableDecClass.IPAddress + "apiGenerateOTPLogin";
        loadingPB.setVisibility(View.VISIBLE);

        // creating a new variable for our request queue
        RequestQueue queue = Volley.newRequestQueue(LoginwithOTP.this);

        // on below line we are calling a string
        // request method to post the data to our API
        // in this we are calling a post method.
        StringRequest request = new StringRequest(Request.Method.POST, url, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                // inside on response method we are
                // hiding our progress bar
                // and setting data to edit text as empty


                // on below line we are displaying a success toast message.
                //Toast.makeText(RegwithOTP.this, "OTP received", Toast.LENGTH_SHORT).show();
                try {
                    // on below line we are parsing the response
                    // to json object to extract data from it.
                    JSONObject respObj = new JSONObject(response);
                    String OTPretrive = respObj.getString("otp");
                    String Statusretrive = respObj.getString("status");
                    if (OTPretrive.equalsIgnoreCase("0")) {
                        //Toast.makeText(LoginwithOTP.this, "" + Statusretrive, Toast.LENGTH_LONG).show();
                        Intent i = new Intent(LoginwithOTP.this, LoginActivity.class);
                        //Intent is used to switch from one activity to another.
                        startActivity(i);
                        finish();
                    } else {

                        //OTPvalfetchedforLogin = OTPretrive;
                        //Toast.makeText(LoginwithOTP.this, "" + Statusretrive, Toast.LENGTH_LONG).show();
                        //Intent i = new Intent(LoginwithOTP.this, LoginwithOTPgiving.class);
                        //Intent is used to switch from one activity to another.
                        //startActivity(i);
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // method to handle errors.
                //Toast.makeText(LoginwithOTP.this, "Fail to get response = " + error, Toast.LENGTH_SHORT).show();
                loadingPB.setVisibility(View.GONE);
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                // below line we are creating a map for
                // storing our values in key and value pair.
                Map<String, String> params = new HashMap<String, String>();

                // on below line we are passing our key
                // and value pair to our parameters.
                params.put("mobile_num", MobileNum);

                // at last we are
                // returning our params.
                return params;
            }
        };
        // below line is to make
        // a json object request.
        queue.add(request);

    }

    private void startSmartUserConsent() {

        SmsRetrieverClient client = SmsRetriever.getClient(this);
        client.startSmsUserConsent(null);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQ_USER_CONSENT) {

            if ((resultCode == RESULT_OK) && (data != null)) {

                String message = data.getStringExtra(SmsRetriever.EXTRA_SMS_MESSAGE);
                loadingPB.setVisibility(View.GONE);
                getOtpFromMessage(message);
            } else if (resultCode==RESULT_CANCELED) {
                startActivity(new Intent(LoginwithOTP.this,LoginwithOTPgiving.class));

            }


        }

    }

    private void getOtpFromMessage(String message) {

        Pattern otpPattern = Pattern.compile("(|^)\\d{4}");
        Matcher matcher = otpPattern.matcher(message);
        if (matcher.find()) {

            OTPvalfetchedforLogin = matcher.group(0);
            Intent i = new Intent(LoginwithOTP.this, LoginwithOTPgiving.class);
            i.putExtra("mobileNumber", MobileNumberLogin);
            //Intent is used to switch from one activity to another.
            startActivity(i);


        } else {
            Intent intent = new Intent(LoginwithOTP.this, LoginActivity.class);
            startActivity(intent);
            finish();
        }


    }

    private void registerBroadcastReceiver() {

        smsBroadcastReceiver = new SmsBroadcastReceiver();

        smsBroadcastReceiver.smsBroadcastReceiverListener = new SmsBroadcastReceiver.SmsBroadcastReceiverListener() {
            @Override
            public void onSuccess(Intent intent) {

                startActivityForResult(intent, REQ_USER_CONSENT);

            }

            @Override
            public void onFailure() {

            }
        };

        IntentFilter intentFilter = new IntentFilter(SmsRetriever.SMS_RETRIEVED_ACTION);
        registerReceiver(smsBroadcastReceiver, intentFilter);

    }

    @Override
    protected void onStart() {
        super.onStart();
        registerBroadcastReceiver();

    }

    @Override
    protected void onStop() {
        super.onStop();
        unregisterReceiver(smsBroadcastReceiver);
    }

    private boolean checkPermission() {
        int result = ContextCompat.checkSelfPermission(getApplicationContext(), RECEIVE_SMS);
        return result == PackageManager.PERMISSION_GRANTED;
    }

    private void requestPermission() {
        ActivityCompat.requestPermissions(this, new String[]{READ_SMS}, PERMISSION_REQUEST_CODE);
    }

}