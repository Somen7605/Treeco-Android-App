package com.example.treeco;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class LoginwithOTPgiving extends AppCompatActivity implements View.OnClickListener {

    EditText edotp1, edotp2, edotp3, edotp4;
    TextView backtv, resendtxt;
    ImageView login;
    private ProgressBar loadingPB;
    String mobileNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loginwith_otpgiving);
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

        edotp1 = findViewById(R.id.edotp1);
        edotp2 = findViewById(R.id.edotp2);
        edotp3 = findViewById(R.id.edotp3);
        edotp4 = findViewById(R.id.edotp4);
        loadingPB = findViewById(R.id.idLoadingPB);
        edotp1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (edotp1.getText().toString().length() == 1) {
                    edotp2.requestFocus();
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        edotp2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (edotp2.getText().toString().length() == 1) {
                    edotp3.requestFocus();
                }

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (edotp2.getText().toString().length() == 0) {
                    edotp1.requestFocus();
                }


            }
        });
        edotp3.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (edotp3.getText().toString().length() == 1) {
                    edotp4.requestFocus();
                }

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (edotp3.getText().toString().length() == 0) {
                    edotp2.requestFocus();
                }


            }
        });
        edotp4.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (edotp3.getText().toString().length() == 1) {
                    edotp4.requestFocus();
                }

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (edotp4.getText().toString().length() == 0) {
                    edotp3.requestFocus();
                } else if (edotp4.getText().toString().length() == 1) {
                    edotp4.clearFocus();
                }

            }
        });
        backtv = findViewById(R.id.backtv);
        resendtxt = findViewById(R.id.resendtxt);
        login = findViewById(R.id.loginbtn);
        backtv.setOnClickListener(this);
        resendtxt.setOnClickListener(this);
        login.setOnClickListener(this);
        // Toast.makeText(this,RegwithOTP.OTPvalfetched,Toast.LENGTH_LONG).show();
        if (!(LoginwithOTP.OTPvalfetchedforLogin.equalsIgnoreCase("0") || LoginwithOTP.OTPvalfetchedforLogin.equalsIgnoreCase(""))) {
            String valfetched = LoginwithOTP.OTPvalfetchedforLogin;
            char chars[] = valfetched.toCharArray();

            edotp1.setText("" + String.valueOf(chars[0]));
            edotp2.setText("" + String.valueOf(chars[1]));
            edotp3.setText("" + String.valueOf(chars[2]));
            edotp4.setText("" + String.valueOf(chars[3]));
            String otp= String.valueOf(chars[0]+chars[1]+chars[2]+chars[3]);
            Intent i=getIntent();
            mobileNumber=i.getStringExtra("mobileNumber");
            postDataUsingVolley2(mobileNumber,otp);
        }
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.loginbtn) {
            if (!(edotp1.equals("") && edotp2.equals("") && edotp3.equals("") && edotp3.equals(""))) {

//                SharedPreferences shrPrf;
//                shrPrf = getSharedPreferences("MyLoginInfo", Context.MODE_PRIVATE);
//                SharedPreferences.Editor editor = shrPrf.edit();
//                editor.putString("login_mobilenum", MobileNum);
//                editor.putString("login_password", Password);
//                editor.putString("login_userid", IDretrive);
//                editor.putInt("mylogin", 1);
//                editor.commit();
                Intent i = new Intent(this,
                        UserDashboard.class);
                //Intent is used to switch from one activity to another.
                startActivity(i);
            } else {
                Toast.makeText(this, "Input OTP you have received", Toast.LENGTH_LONG).show();
            }


        }
        if (v.getId() == R.id.resendtxt) {
            String MobileNumber = LoginwithOTP.MobileNumberLogin;
            if (!MobileNumber.equalsIgnoreCase("")) {
                postDataUsingVolley(MobileNumber);
            } else {
                Toast.makeText(this, "Mobile Number not found", Toast.LENGTH_LONG).show();
            }
        }
        if (v.getId() == R.id.backtv) {
            Intent i = new Intent(this,
                    RegwithOTP.class);
            //Intent is used to switch from one activity to another.
            startActivity(i);
        }

    }
    private void postDataUsingVolley2(String mobileNum,String otp)
    {
        String url = VariableDecClass.IPAddress + "apiLoginWithOTP";
        loadingPB.setVisibility(View.VISIBLE);
        // creating a new variable for our request queue
        RequestQueue queue = Volley.newRequestQueue(LoginwithOTPgiving.this);

        // on below line we are calling a string
        // request method to post the data to our API
        // in this we are calling a post method.
        StringRequest request = new StringRequest(Request.Method.POST, url, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                // inside on response method we are
                // hiding our progress bar
                // and setting data to edit text as empty
                loadingPB.setVisibility(View.GONE);

                // on below line we are displaying a success toast message.
                //Toast.makeText(LoginwithOTPgiving.this, "OTP Fetched", Toast.LENGTH_SHORT).show();
                try {
                    // on below line we are parsing the response
                    // to json object to extract data from it.
                    JSONObject respObj = new JSONObject(response);

                    String id = respObj.getString("id");
                    String Statusretrive = respObj.getString("status");

                    if (Statusretrive.equalsIgnoreCase("Successfully Logged In")) {
                        //Toast.makeText(LoginwithOTPgiving.this, "" + Statusretrive, Toast.LENGTH_LONG).show();
                        SharedPreferences sharedPreferences=getSharedPreferences("MyLoginInfo", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString("login_userid",id);
                        editor.putString("login_mobilenum", mobileNum);
                        //editor.putInt("mylogin", 1);
                        editor.apply();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // method to handle errors.
                //Toast.makeText(LoginwithOTPgiving.this, "Fail to get response = " + error, Toast.LENGTH_SHORT).show();
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
                params.put("mobile_num", mobileNum);
                params.put("otp",otp);

                // at last we are
                // returning our params.
                return params;
            }
        };
        // below line is to make
        // a json object request.
        queue.add(request);
    }

    private void postDataUsingVolley(String MobileNum) {
        // url to post our data
        String url = VariableDecClass.IPAddress + "apiGenerateOTPLogin";
        loadingPB.setVisibility(View.VISIBLE);

        // creating a new variable for our request queue
        RequestQueue queue = Volley.newRequestQueue(LoginwithOTPgiving.this);

        // on below line we are calling a string
        // request method to post the data to our API
        // in this we are calling a post method.
        StringRequest request = new StringRequest(Request.Method.POST, url, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                // inside on response method we are
                // hiding our progress bar
                // and setting data to edit text as empty
                loadingPB.setVisibility(View.GONE);

                // on below line we are displaying a success toast message.
                //Toast.makeText(LoginwithOTPgiving.this, "OTP Fetched", Toast.LENGTH_SHORT).show();
                try {
                    // on below line we are parsing the response
                    // to json object to extract data from it.
                    JSONObject respObj = new JSONObject(response);

                    String OTPretrive = respObj.getString("otp");
                    String Statusretrive = respObj.getString("status");

                    if (OTPretrive.equalsIgnoreCase("0")) {
                        Toast.makeText(LoginwithOTPgiving.this, "" + Statusretrive, Toast.LENGTH_LONG).show();
                        edotp1.setText("");
                        edotp2.setText("");
                        edotp3.setText("");
                        edotp4.setText("");
                    } else {
                        // Toast.makeText(LoginwithOTPgiving.this, "" + Statusretrive + ":OTP val:" + OTPretrive, Toast.LENGTH_LONG).show();
                        //LoginwithOTP.OTPvalfetchedforLogin = OTPretrive;
                        char chars[] = OTPretrive.toCharArray();

                        edotp1.setText("" + String.valueOf(chars[0]));
                        edotp2.setText("" + String.valueOf(chars[1]));
                        edotp3.setText("" + String.valueOf(chars[2]));
                        edotp4.setText("" + String.valueOf(chars[3]));

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // method to handle errors.
                //Toast.makeText(LoginwithOTPgiving.this, "Fail to get response = " + error, Toast.LENGTH_SHORT).show();
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
}
