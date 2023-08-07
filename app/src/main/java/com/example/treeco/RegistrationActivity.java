package com.example.treeco;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class RegistrationActivity extends AppCompatActivity {
    public static int setMembershipVal = 1;
    EditText edName, edEmail;
    ImageView imgIndividual, imgFree, imgback, imgSubmit, imgCorporate, imgSilver, imgGold;
    TextView free_text, silver_text, gold_text;
    int setIndividualorCorporateVal = 0;
    RelativeLayout rlfree, rlsilver, rlgold;
    private ProgressBar loadingPB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
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
        edEmail = findViewById(R.id.edit_text1);
        edName = findViewById(R.id.edit_text);
        imgIndividual = findViewById(R.id.imgIndividual);
        imgCorporate = findViewById(R.id.imgCorporate);
        imgback = findViewById(R.id.backimg);
        imgFree = findViewById(R.id.free);
        imgGold = findViewById(R.id.gold);
        imgSilver = findViewById(R.id.silver);
        rlgold = findViewById(R.id.rlgold);
        rlsilver = findViewById(R.id.rlsilver);
        rlfree = findViewById(R.id.rlfree);
        imgSubmit = findViewById(R.id.imgSubmit);
        loadingPB = findViewById(R.id.idLoadingPB);

        free_text = findViewById(R.id.free_text);
        silver_text = findViewById(R.id.silver_text);
        gold_text = findViewById(R.id.gold_text);


        imgIndividual.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setIndividualorCorporateVal = 0;
                imgIndividual.setImageResource(R.drawable.base_ek90);
                imgCorporate.setImageResource(R.drawable.base_ek89);
            }
        });
        imgCorporate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setIndividualorCorporateVal = 1;
                imgCorporate.setImageResource(R.drawable.base_ek90);
                imgIndividual.setImageResource(R.drawable.base_ek89);
            }
        });
        imgFree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setMembershipVal = 1;
                rlfree.setVisibility(View.VISIBLE);
                rlsilver.setVisibility(View.INVISIBLE);
                rlgold.setVisibility(View.INVISIBLE);
            }
        });
        imgSilver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setMembershipVal = 2;
                rlsilver.setVisibility(View.VISIBLE);
                rlfree.setVisibility(View.INVISIBLE);
                rlgold.setVisibility(View.INVISIBLE);
            }
        });
        imgGold.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setMembershipVal = 3;
                rlgold.setVisibility(View.VISIBLE);
                rlsilver.setVisibility(View.INVISIBLE);
                rlfree.setVisibility(View.INVISIBLE);
            }
        });
        imgback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(RegistrationActivity.this, RegwithOTP.class);
                startActivity(i);
            }
        });
        imgSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = edName.getText().toString();
                String email = edEmail.getText().toString();
                String mobilenum = RegwithOTP.MobileNumber;
                // Toast.makeText(RegistrationActivity.this,mobilenum,Toast.LENGTH_LONG).show();
                String user_type = "";
                String mem_type_id = "";
                String otpfetched = RegwithOTP.OTPvalfetched;
                if (setIndividualorCorporateVal == 0) {
                    user_type = "Individual";
                } else {
                    user_type = "Corporate";
                }
                if (setMembershipVal == 1) {
                    mem_type_id = "1";
                } else if (setMembershipVal == 2) {
                    mem_type_id = "2";
                } else {
                    mem_type_id = "3";
                }

                if ((name.equals(""))) {
                    Toast.makeText(RegistrationActivity.this, "fill user name", Toast.LENGTH_LONG).show();
                }
                if ((email.equals(""))) {
                    Toast.makeText(RegistrationActivity.this, "fill Email field", Toast.LENGTH_LONG).show();
                }
                if (mobilenum.equals("")) {
                    Toast.makeText(RegistrationActivity.this, "mobile number not found", Toast.LENGTH_LONG).show();
                }
                if (otpfetched.equals("")) {
                    Toast.makeText(RegistrationActivity.this, "OTP cant be blank", Toast.LENGTH_LONG).show();
                }
                if (!(name.equals("") || email.equals("") || mobilenum.equals("") || otpfetched.equals(""))) {
                    postDataUsingVolley1(mobilenum, name, email, mem_type_id, user_type, otpfetched);

                }
                //Api call here
                //Toast.makeText(getApplicationContext(),"Clicked to send info",Toast.LENGTH_LONG).show();

            }
        });

    }

    private void postDataUsingVolley1(String MobileNum, String name, String email, String mem_type_id, String user_type, String otpfetched) {
        // url to post our data
        String url = VariableDecClass.IPAddress + "apiRegistration";
        loadingPB.setVisibility(View.VISIBLE);

        // creating a new variable for our request queue
        RequestQueue queue = Volley.newRequestQueue(RegistrationActivity.this);

        // on below line we are calling a string
        // request method to post the data to our API
        // in this we are calling a post method.
        StringRequest request1 = new StringRequest(Request.Method.POST, url, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                // Toast.makeText(RegistrationActivity.this,""+response,Toast.LENGTH_LONG).show();
                // inside on response method we are
                // hiding our progress bar
                // and setting data to edit text as empty
                loadingPB.setVisibility(View.GONE);

                // on below line we are displaying a success toast message.
                // Toast.makeText(RegistrationActivity.this, "Response received", Toast.LENGTH_SHORT).show();
                try {
                    // on below line we are parsing the response
                    // to json object to extract data from it.
                    JSONObject respObj = new JSONObject(response);
                    String IDretrive = respObj.getString("id");
                    String Statusretrive = respObj.getString("status");
                    if (IDretrive.equalsIgnoreCase("0")) {
                        Toast.makeText(RegistrationActivity.this, "" + Statusretrive + " " + IDretrive, Toast.LENGTH_LONG).show();
                    } else {
                        // Toast.makeText(RegistrationActivity.this,""+Statusretrive,Toast.LENGTH_LONG).show();
                        SharedPreferences shrPrf;
                        shrPrf = getSharedPreferences("MyRegInfo", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = shrPrf.edit();
                        editor.putString("isReg", "1");
                        editor.commit();
                        // Toast.makeText(RegistrationActivity.this, "Registration successful",Toast.LENGTH_LONG).show();
                        Intent i = new Intent(RegistrationActivity.this, RegistrationSuccessful.class);
                        //Intent is used to switch from one activity to another.
                        startActivity(i);
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // method to handle errors.
                //Toast.makeText(RegistrationActivity.this, "Fail to get response = " + error, Toast.LENGTH_SHORT).show();
                loadingPB.setVisibility(View.GONE);
                // Intent i = new Intent(RegistrationActivity.this,RegistrationFailed.class);
                //Intent is used to switch from one activity to another.
                //startActivity(i);
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                // below line we are creating a map for
                // storing our values in key and value pair.
                Map<String, String> params = new HashMap<String, String>();

                // on below line we are passing our key
                // and value pair to our parameters.
                params.put("name", name);
                params.put("email", email);
                params.put("mobile_num", MobileNum);
                params.put("membershp_type_id", mem_type_id);
                params.put("user_type", user_type);
                params.put("last_otp", otpfetched);

                // at last we are
                // returning our params.
                return params;
            }
        };
        // below line is to make
        // a json object request.
        request1.setRetryPolicy(new DefaultRetryPolicy(
                0,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        queue.add(request1);

    }
}