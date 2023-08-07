package com.example.treeco;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import androidx.biometric.BiometricManager;
import androidx.fragment.app.Fragment;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


public class ProfileFragment extends Fragment {
    public static int setMembershipValUpdate = 1;
    ToggleButton tb1, tb2;
    EditText edName, edEmail;
    ImageView imgIndividual, imgCorporate, imgFree, imgSilver, imgGold, imgSubmit, imgback;
    int setIndividualorCorporateVal = 0;
    RelativeLayout rlfree, rlsilver, rlgold;
    TextView free1, silver1, gold1, free2, silver2, gold2, resetPass, logOut;
    SharedPreferences shrPrf;
    private ProgressBar loadingPB;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment


        View v = inflater.inflate(R.layout.fragment_profile, container, false);
        //toggle button linking
        tb1 = v.findViewById(R.id.toggleButton1);
        tb2 = v.findViewById(R.id.toggleButton2);
        edEmail = v.findViewById(R.id.edit_text1);
        edName = v.findViewById(R.id.edit_text);
        imgIndividual = v.findViewById(R.id.imgIndividual);
        imgCorporate = v.findViewById(R.id.imgCorporate);
        imgback = v.findViewById(R.id.backimg);
        imgFree = v.findViewById(R.id.free);
        imgGold = v.findViewById(R.id.gold);
        imgSilver = v.findViewById(R.id.silver);
        rlgold = v.findViewById(R.id.rlgold);
        rlsilver = v.findViewById(R.id.rlsilver);
        rlfree = v.findViewById(R.id.rlfree);
        imgSubmit = v.findViewById(R.id.imgSubmit);
        loadingPB = v.findViewById(R.id.idLoadingPB);
        free1 = v.findViewById(R.id.free_1);
        free2 = v.findViewById(R.id.free_2);
        silver1 = v.findViewById(R.id.silver_1);
        silver2 = v.findViewById(R.id.silver_2);
        gold1 = v.findViewById(R.id.gold_1);
        gold2 = v.findViewById(R.id.gold_2);
        resetPass = v.findViewById(R.id.txtResetPass);
        logOut = v.findViewById(R.id.logOutTextView);


        shrPrf = getActivity().getSharedPreferences("MyLoginInfo", Context.MODE_PRIVATE);
        String user_id = shrPrf.getString("login_userid", "");
        String mobilenum = shrPrf.getString("login_mobilenum", "");
        postDataUsingVolley(user_id);
        //patternStatusChecking
        patternStatusChecking();
        //Biometric Status checking function
        BiometricStatusChecking();
        //toggle status checking
        tb1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton tooglebutton, boolean isChecked) {
                if (isChecked) {
                    BiometricLoginFunction();
                    //Toast.makeText(getActivity(),"Biometric enable",Toast.LENGTH_LONG).show();
                } else {
                    StoreSharePreferences("biometric", "disable");
                    Toast.makeText(getActivity(), "Biometric disable", Toast.LENGTH_LONG).show();
                }

            }
        });
        tb2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton togglebutton1, boolean isChecked1) {
                if (isChecked1) {
                    StoreSharePreferences("pattern", "enable");
                    Toast.makeText(getActivity(), "Pattern enable", Toast.LENGTH_LONG).show();

                } else {
                    StoreSharePreferences("pattern", "disable");
                    Toast.makeText(getActivity(), "Pattern disabled", Toast.LENGTH_LONG).show();
                }

            }
        });
        imgIndividual.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setIndividualorCorporateVal = 0;
                imgIndividual.setImageResource(R.drawable.gradient2);
                imgCorporate.setImageResource(R.drawable.base_ek89);
            }
        });
        imgCorporate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setIndividualorCorporateVal = 1;
                imgCorporate.setImageResource(R.drawable.gradient2);
                imgIndividual.setImageResource(R.drawable.base_ek89);
            }
        });
        imgFree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setMembershipValUpdate = 1;
                rlfree.setVisibility(View.VISIBLE);
                rlsilver.setVisibility(View.INVISIBLE);
                rlgold.setVisibility(View.INVISIBLE);
                free1.setVisibility(View.INVISIBLE);
                free2.setVisibility(View.VISIBLE);
                silver1.setVisibility(View.VISIBLE);
                silver2.setVisibility(View.INVISIBLE);
                gold1.setVisibility(View.VISIBLE);
                gold2.setVisibility(View.INVISIBLE);
            }
        });
        imgSilver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setMembershipValUpdate = 2;
                rlsilver.setVisibility(View.VISIBLE);
                rlfree.setVisibility(View.INVISIBLE);
                rlgold.setVisibility(View.INVISIBLE);
                free1.setVisibility(View.VISIBLE);
                free2.setVisibility(View.INVISIBLE);
                silver1.setVisibility(View.INVISIBLE);
                silver2.setVisibility(View.VISIBLE);
                gold1.setVisibility(View.VISIBLE);
                gold2.setVisibility(View.INVISIBLE);
            }
        });
        imgGold.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setMembershipValUpdate = 3;
                rlgold.setVisibility(View.VISIBLE);
                rlsilver.setVisibility(View.INVISIBLE);
                rlfree.setVisibility(View.INVISIBLE);
                free1.setVisibility(View.VISIBLE);
                free2.setVisibility(View.INVISIBLE);
                silver1.setVisibility(View.VISIBLE);
                silver2.setVisibility(View.INVISIBLE);
                gold1.setVisibility(View.INVISIBLE);
                gold2.setVisibility(View.VISIBLE);

            }
        });
        imgSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = edName.getText().toString();
                String email = edEmail.getText().toString();
                String user_type = "";
                String mem_type_id = "";
                if (setIndividualorCorporateVal == 0) {
                    user_type = "Individual";
                } else {
                    user_type = "Corporate";
                }
                if (setMembershipValUpdate == 1) {
                    mem_type_id = "1";
                } else if (setMembershipValUpdate == 2) {
                    mem_type_id = "2";
                } else {
                    mem_type_id = "3";
                }

                if ((name.equals(""))) {
                    Toast.makeText(getActivity(), "fill user name", Toast.LENGTH_LONG).show();
                }
                if ((email.equals(""))) {
                    Toast.makeText(getActivity(), "fill Email field", Toast.LENGTH_LONG).show();
                }
                if (mobilenum.equals("")) {
                    Toast.makeText(getActivity(), "mobile number not found", Toast.LENGTH_LONG).show();
                }
                if (!(name.equals("") || email.equals("") || mobilenum.equals(""))) {
                    postDataUsingVolley1(user_id, mobilenum, email, mem_type_id, user_type);

                }
                //Api call here
                //Toast.makeText(getApplicationContext(),"Clicked to send info",Toast.LENGTH_LONG).show();

            }
        });
        resetPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), Update_Password.class));
            }
        });
        logOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                SharedPreferences shrPrf, shrp;
                shrPrf = getActivity().getSharedPreferences("MyRegInfo", Context.MODE_PRIVATE);
                shrp = getActivity().getSharedPreferences("MyLoginInfo", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = shrPrf.edit();
                SharedPreferences.Editor editorLogin = shrp.edit();
                //editor.remove("isReg");
                editorLogin.remove("login_mobilenum");
                editorLogin.remove("login_password");
                editorLogin.remove("login_userid");
                //editorLogin.remove("mylogin");
                //
                editorLogin.commit();

                /*SharedPreferences.Editor editor = shrPr2.edit();
                editor.putInt("mylogin",1);
                editor.commit();*/

                Intent i = new Intent(getActivity(), LoginActivity.class);
                i.addFlags(i.FLAG_ACTIVITY_CLEAR_TASK | i.FLAG_ACTIVITY_NEW_TASK);
                startActivity(i);
                getActivity().finish();

            }
        });

        return v;
    }

    public void StoreSharePreferences(String key, String val) {
        SharedPreferences shrPrf;
        shrPrf = getActivity().getSharedPreferences("MyLoginPatternBiometricInfo", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = shrPrf.edit();
        editor.putString(key, val);
        editor.commit();
    }

    public void patternStatusChecking() {
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("MyLoginPatternBiometricInfo", 0);
        String PatternStatus = sharedPreferences.getString("pattern", "0");
        if (PatternStatus.equalsIgnoreCase("enable")) {
            tb2.setChecked(true);
        } else if (PatternStatus.equalsIgnoreCase("disable")) {
            tb2.setChecked(false);
        }
    }

    public void BiometricStatusChecking() {
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("MyLoginPatternBiometricInfo", 0);
        String BiometricStatus = sharedPreferences.getString("biometric", "0");
        if (BiometricStatus.equalsIgnoreCase("enable")) {
            tb1.setChecked(true);
        } else if (BiometricStatus.equalsIgnoreCase("disable")) {
            tb1.setChecked(false);
        }
    }

    public void BiometricLoginFunction() {
        //Toast.makeText(LoginActivity.this, "Feature not yet explored", Toast.LENGTH_SHORT).show();

        // creating a variable for our BiometricManager
        // and lets check if our user can use biometric sensor or not
        BiometricManager biometricManager = androidx.biometric.BiometricManager.from(getActivity());
        switch (biometricManager.canAuthenticate()) {

            // this means we can use biometric sensor
            case BiometricManager.BIOMETRIC_SUCCESS:
                StoreSharePreferences("biometric", "enable");
                break;

            // this means that the device doesn't have fingerprint sensor
            case BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE:
                Toast.makeText(getActivity(), "This device doesnot have a fingerprint sensor", Toast.LENGTH_LONG).show();
                StoreSharePreferences("biometric", "disable");
                break;

            // this means that biometric sensor is not available
            case BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE:
                Toast.makeText(getActivity(), "Biometric Sensor not Available", Toast.LENGTH_LONG).show();
                StoreSharePreferences("biometric", "disable");
                break;

            // this means that the device doesn't contain your fingerprint
            case BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED:
                Toast.makeText(getActivity(), "Your device doesn't have fingerprint saved,please check your security settings and Try again", Toast.LENGTH_LONG).show();
                StoreSharePreferences("biometric", "disable");
                break;
        }
    }

    private void postDataUsingVolley1(String userid, String mobilenum, String email, String mem_type_id, String usertype) {
        // url to post our data
        String url = VariableDecClass.IPAddress + "apiLoginUpdate";
        loadingPB.setVisibility(View.VISIBLE);

        // creating a new variable for our request queue
        RequestQueue queue = Volley.newRequestQueue(getActivity());

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
                        //Toast.makeText(getActivity(), Statusretrive, Toast.LENGTH_LONG).show();
                    } else {
                        //Toast.makeText(getActivity(), "Updated Successful", Toast.LENGTH_LONG).show();

                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // method to handle errors.
                //Toast.makeText(getActivity(), "Fail to get response = " + error, Toast.LENGTH_SHORT).show();
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
                params.put("email", email);
                params.put("mobile_num", mobilenum);
                params.put("membershp_type_id", mem_type_id);
                params.put("id", userid);
                params.put("user_type", usertype);

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

    private void postDataUsingVolley(String userid) {
        // url to post our data
        String url = VariableDecClass.IPAddress + "apiUserInfo";
        loadingPB.setVisibility(View.VISIBLE);
        try {
            JSONObject params = new JSONObject();
            params.put("user_id", userid);
            params.put("includeDeleted", false);

            // creating a new variable for our request queue
            RequestQueue queue1 = Volley.newRequestQueue(getActivity());
            // on below line we are calling a string
            // request method to post the data to our API
            // in this we are calling a post method.
            JsonObjectRequest request1 = new JsonObjectRequest(Request.Method.POST, url, params, new com.android.volley.Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    // inside on response method we are
                    // hiding our progress bar
                    // and setting data to edit text as empty
                    loadingPB.setVisibility(View.GONE);

                    // on below line we are displaying a success toast message.
                    //Toast.makeText(RegwithOTP.this, "OTP received", Toast.LENGTH_SHORT).show();
                    try {
                        // on below line we are parsing the response
                        // to json object to extract data from it.
                        JSONObject respObj = response;
                        //Toast.makeText(getActivity(), "resonse " + respObj, Toast.LENGTH_SHORT).show();
                        String membershipid = respObj.getString("membershp_type_id");
                        String email = respObj.getString("email");
                        String user_type = respObj.getString("user_type");
                        String user_name = respObj.getString("name");
                        edEmail.setText(email);
                        edName.setText(user_name);
                        if (user_type.equalsIgnoreCase("Individual")) {

                            setIndividualorCorporateVal = 0;
                            imgIndividual.setImageResource(R.drawable.gradient2);
                            imgCorporate.setImageResource(R.drawable.base_ek89);
                        } else {
                            setIndividualorCorporateVal = 1;
                            imgCorporate.setImageResource(R.drawable.gradient2);
                            imgIndividual.setImageResource(R.drawable.base_ek89);
                        }
                        if (membershipid.equalsIgnoreCase("1")) {
                            setMembershipValUpdate = 1;
                            rlfree.setVisibility(View.VISIBLE);
                            rlsilver.setVisibility(View.INVISIBLE);
                            rlgold.setVisibility(View.INVISIBLE);
                            silver1.setVisibility(View.VISIBLE);
                            silver2.setVisibility(View.INVISIBLE);
                            gold1.setVisibility(View.VISIBLE);
                            gold2.setVisibility(View.INVISIBLE);

                        } else if (membershipid.equalsIgnoreCase("2")) {
                            setMembershipValUpdate = 2;
                            rlsilver.setVisibility(View.VISIBLE);
                            rlfree.setVisibility(View.INVISIBLE);
                            rlgold.setVisibility(View.INVISIBLE);
                            free1.setVisibility(View.VISIBLE);
                            free2.setVisibility(View.INVISIBLE);
                            gold1.setVisibility(View.VISIBLE);
                            gold2.setVisibility(View.INVISIBLE);
                        } else {
                            setMembershipValUpdate = 3;
                            rlgold.setVisibility(View.VISIBLE);
                            rlsilver.setVisibility(View.INVISIBLE);
                            rlfree.setVisibility(View.INVISIBLE);
                            free1.setVisibility(View.VISIBLE);
                            free2.setVisibility(View.INVISIBLE);
                            silver1.setVisibility(View.VISIBLE);
                            silver2.setVisibility(View.INVISIBLE);
                        }

                        // Toast.makeText(getActivity(), "name " + user_name, Toast.LENGTH_SHORT).show();


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }, new com.android.volley.Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    // method to handle errors.
                    //Toast.makeText(getActivity(), "Fail to get response = " + error, Toast.LENGTH_SHORT).show();
                    loadingPB.setVisibility(View.GONE);
                }
            });
            // below line is to make
            // a json object request.
            queue1.add(request1);
        } catch (Exception e) {
            //Toast.makeText(getActivity(), "Fail to create json = " + e, Toast.LENGTH_SHORT).show();
            loadingPB.setVisibility(View.GONE);
        }

    }
}