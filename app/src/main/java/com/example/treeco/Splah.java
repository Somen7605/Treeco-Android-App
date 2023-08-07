package com.example.treeco;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.text.Html;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

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

public class Splah extends AppCompatActivity {
    private static int SPLASH_SCREEN_TIME_OUT = 3000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);//will hide the title
        getSupportActionBar().hide(); //hide the title bar
        setContentView(R.layout.splash);
        ImageView image = findViewById(R.id.subtraction_11_ek6);
        TextView txt1 = findViewById(R.id.treeco_ek6);
        txt1.setText(Html.fromHtml("<font color=#000000>" + "<small>"
                + "tre" + "</small>" + "" + "<big>" + "e"
                + "</big>" + "" + "<small>" + "co" + "</small>"));
        TextView txt2 = findViewById(R.id.this_feature_isn_t_available_ek6);
        Animation animation1 = AnimationUtils.loadAnimation(getApplicationContext(),
                R.anim.myscale);
        image.startAnimation(animation1);
        txt1.startAnimation(animation1);
        txt2.startAnimation(animation1);


        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                SharedPreferences shrPrf, shrp;
                shrPrf = getSharedPreferences("MyRegInfo", Context.MODE_PRIVATE);
                shrp = getSharedPreferences("MyLoginInfo", Context.MODE_PRIVATE);
                String isReg = shrPrf.getString("isReg", "");
                String mobileNum = shrp.getString("login_mobilenum", "");
                String pass = shrp.getString("login_password", "");
                int isLog = shrp.getInt("mylogin", 0);
                String userId = shrp.getString("login_userid", "");
//                if (isReg.equalsIgnoreCase("")) {
//                    Intent i = new Intent(Splah.this, RegwithOTP.class);
//                    startActivity(i);
//                }
                if (mobileNum != ""&& pass!="") {
                    postDataUsingVolley(mobileNum, pass);
                }
                else if(mobileNum != "")
                {
                    Intent i = new Intent(Splah.this, UserDashboard.class);
                    //Intent is used to switch from one activity to another.
                    startActivity(i);
                    finish();
                }
                //invoke the SecondActivity.
                else {
                    Intent i = new Intent(Splah.this, RegwithOTP.class);
                    startActivity(i);
                }
                //Toast.makeText(Splah.this, ""+isLog, Toast.LENGTH_SHORT).show();
                finish();
                //the current activity will get finished.
            }
        }, SPLASH_SCREEN_TIME_OUT);
    }

    private void postDataUsingVolley(String MobileNum, String Password) {
        // url to post our data
        String url = VariableDecClass.IPAddress + "apiLoginWithPass";
        //loadingPB.setVisibility(View.VISIBLE);

        // creating a new variable for our request queue
        RequestQueue queue = Volley.newRequestQueue(Splah.this);

        // on below line we are calling a string
        // request method to post the data to our API
        // in this we are calling a post method.
        StringRequest request = new StringRequest(Request.Method.POST, url, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                // inside on response method we are
                // hiding our progress bar
                // and setting data to edit text as empty
                //loadingPB.setVisibility(View.GONE);

                // on below line we are displaying a success toast message.
                //Toast.makeText(Splah.this, "Response received", Toast.LENGTH_SHORT).show();
                try {
                    // on below line we are parsing the response
                    // to json object to extract data from it.
                    JSONObject respObj = new JSONObject(response);
                    String IDretrive = respObj.getString("id");
                    String Statusretrive = respObj.getString("status");
                    if (IDretrive.equalsIgnoreCase("0")) {
                        //Toast.makeText(LoginActivity.this, "" + Statusretrive, Toast.LENGTH_LONG).show();
                        Intent i = new Intent(Splah.this, LoginActivity.class);
                        startActivity(i);
                        finish();

                    } else {
                        //Toast.makeText(LoginActivity.this,""+Statusretrive+IDretrive,Toast.LENGTH_LONG).show();
                        SharedPreferences shrPrf;
                        shrPrf = getSharedPreferences("MyLoginInfo", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = shrPrf.edit();
                        editor.putString("login_mobilenum", MobileNum);
                        editor.putString("login_password", Password);
                        editor.putString("login_userid", IDretrive);
                        editor.putInt("mylogin", 1);
                        editor.commit();

                        //Toast.makeText(RegistrationActivity.this, "Registration successful",Toast.LENGTH_LONG).show();
                        Intent i = new Intent(Splah.this, UserDashboard.class);
                        //Intent is used to switch from one activity to another.
                        startActivity(i);
                        finish();

                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                    //Toast.makeText(Splah.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // method to handle errors.
                //Toast.makeText(Splah.this, "Login Failed", Toast.LENGTH_SHORT).show();
                //loadingPB.setVisibility(View.GONE);

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
                params.put("password", Password);

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