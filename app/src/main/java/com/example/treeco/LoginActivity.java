package com.example.treeco;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.biometric.BiometricManager;
import androidx.biometric.BiometricPrompt;
import androidx.core.content.ContextCompat;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executor;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    EditText edmobnum, edpsw;
    TextView tvotp, tvbiometric, tvpattern, forgotPass;
    ImageView submit;

    private ProgressBar loadingPB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //requestWindowFeature(Window.FEATURE_NO_TITLE);//will hide the title
        //getSupportActionBar().hide(); //hide the title bar
        setContentView(R.layout.activity_login);
        getSupportActionBar().hide();
        SharedPreferences shrPrf1 = getSharedPreferences("MyLoginInfo", Context.MODE_PRIVATE);
        String LoginMobile = shrPrf1.getString("login_mobilenum", "");
        String Loginpassword = shrPrf1.getString("login_password", "");
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
        //pattern status checking
        edmobnum = findViewById(R.id.editmobilenum);
        edmobnum.setText(LoginMobile);
        edpsw = findViewById(R.id.edpassword);
        edpsw.setText(Loginpassword);
        tvotp = findViewById(R.id.txtotplogin);
        tvbiometric = findViewById(R.id.txtBiometric);
        tvpattern = findViewById(R.id.txtpattern);
        submit = findViewById(R.id.loginbtn);
        tvotp.setOnClickListener(this);
        tvbiometric.setOnClickListener(this);
        tvpattern.setOnClickListener(this);
        submit.setOnClickListener(this);
        loadingPB = findViewById(R.id.idLoadingPB);
        forgotPass = findViewById(R.id.txtoforgotpass);
        forgotPass.setOnClickListener(this);


    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.txtotplogin) {
            SharedPreferences shrPrf;
            shrPrf = getSharedPreferences("MyLoginInfo", Context.MODE_PRIVATE);
            int islogin = shrPrf.getInt("mylogin", 0);
            if (islogin == 1) {
                Intent otppage = new Intent(LoginActivity.this, LoginwithOTP.class);
                startActivity(otppage);
            } else {
                Toast.makeText(LoginActivity.this, "First login should be through password for verification", Toast.LENGTH_SHORT).show();
            }

        }
        if (v.getId() == R.id.loginbtn) {
            String mobnum = edmobnum.getText().toString();
            String password = edpsw.getText().toString();
            if (mobnum.equalsIgnoreCase("")) {
                Toast.makeText(LoginActivity.this, "Please fill Mobile Number field", Toast.LENGTH_SHORT).show();
            }
            if (password.equalsIgnoreCase("")) {
                Toast.makeText(LoginActivity.this, "Please fill Password field", Toast.LENGTH_SHORT).show();
            }
            if (!(mobnum.equalsIgnoreCase("") && password.equalsIgnoreCase(""))) {

                postDataUsingVolley(mobnum, password);
            }

        }
        if (v.getId() == R.id.txtBiometric) {
            SharedPreferences shrPrf;
            shrPrf = getSharedPreferences("MyLoginInfo", Context.MODE_PRIVATE);
            int islogin = shrPrf.getInt("mylogin", 0);
            if (islogin == 1) {
                BiometricStatusChecking();
            } else {
                Toast.makeText(LoginActivity.this, "First login should be through password for verification", Toast.LENGTH_SHORT).show();
            }

        }
        if (v.getId() == R.id.txtpattern) {
            SharedPreferences shrPrf;
            shrPrf = getSharedPreferences("MyLoginInfo", Context.MODE_PRIVATE);
            int islogin = shrPrf.getInt("mylogin", 0);
            if (islogin == 1) {
                //Toast.makeText(LoginActivity.this, "Feature not yet explored", Toast.LENGTH_SHORT).show();
                patternStatusChecking();

            } else {
                Toast.makeText(LoginActivity.this, "First login should be through password for verification", Toast.LENGTH_SHORT).show();
            }
        }
        if (v.getId() == R.id.txtoforgotpass) {
            startActivity(new Intent(LoginActivity.this, send_Password.class));
        }

    }

    private void postDataUsingVolley(String MobileNum, String Password) {
        // url to post our data
        String url = VariableDecClass.IPAddress + "apiLoginWithPass";
        loadingPB.setVisibility(View.VISIBLE);

        // creating a new variable for our request queue
        RequestQueue queue = Volley.newRequestQueue(LoginActivity.this);

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
                //Toast.makeText(LoginActivity.this, "Response received", Toast.LENGTH_SHORT).show();
                try {
                    // on below line we are parsing the response
                    // to json object to extract data from it.
                    JSONObject respObj = new JSONObject(response);
                    String IDretrive = respObj.getString("id");
                    String Statusretrive = respObj.getString("status");
                    if (IDretrive.equalsIgnoreCase("0")) {
                        Toast.makeText(LoginActivity.this, "" + Statusretrive, Toast.LENGTH_LONG).show();
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
                        Intent i = new Intent(LoginActivity.this, UserDashboard.class);
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
                Toast.makeText(LoginActivity.this, "Login Failed", Toast.LENGTH_SHORT).show();
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

    public void BiometricLoginFunction() {
        //Toast.makeText(LoginActivity.this, "Feature not yet explored", Toast.LENGTH_SHORT).show();

        // creating a variable for our BiometricManager
        // and lets check if our user can use biometric sensor or not
        BiometricManager biometricManager = androidx.biometric.BiometricManager.from(this);
        switch (biometricManager.canAuthenticate()) {

            // this means we can use biometric sensor
            case BiometricManager.BIOMETRIC_SUCCESS:
                tvbiometric.setEnabled(true);
                break;

            // this means that the device doesn't have fingerprint sensor
            case BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE:
                Toast.makeText(LoginActivity.this, "This device doesnot have a fingerprint sensor", Toast.LENGTH_LONG).show();
                tvbiometric.setEnabled(false);
                tvbiometric.setTextColor(Color.GRAY);
                break;

            // this means that biometric sensor is not available
            case BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE:
                Toast.makeText(LoginActivity.this, "Biometric Sensor not Available", Toast.LENGTH_LONG).show();
                tvbiometric.setEnabled(false);
                tvbiometric.setTextColor(Color.GRAY);
                break;

            // this means that the device doesn't contain your fingerprint
            case BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED:
                Toast.makeText(LoginActivity.this, "Your device doesn't have fingerprint saved,please check your security settings", Toast.LENGTH_LONG).show();
                tvbiometric.setEnabled(false);
                tvbiometric.setTextColor(Color.GRAY);
                break;
        }
        // creating a variable for our Executor
        Executor executor = ContextCompat.getMainExecutor(this);
        // this will give us result of AUTHENTICATION
        final BiometricPrompt biometricPrompt = new BiometricPrompt(LoginActivity.this, executor, new BiometricPrompt.AuthenticationCallback() {
            @Override
            public void onAuthenticationError(int errorCode, @NonNull CharSequence errString) {
                super.onAuthenticationError(errorCode, errString);
            }

            // THIS METHOD IS CALLED WHEN AUTHENTICATION IS SUCCESS
            @Override
            public void onAuthenticationSucceeded(@NonNull BiometricPrompt.AuthenticationResult result) {
                super.onAuthenticationSucceeded(result);
                Toast.makeText(getApplicationContext(), "Login Success", Toast.LENGTH_SHORT).show();
                Intent i = new Intent(LoginActivity.this, UserDashboard.class);
                startActivity(i);
            }

            @Override
            public void onAuthenticationFailed() {
                super.onAuthenticationFailed();
            }
        });
        final BiometricPrompt.PromptInfo promptInfo = new BiometricPrompt.PromptInfo.Builder()
                .setTitle("Biometric Authentication")
                .setSubtitle("Login using biometric")
                .setNegativeButtonText("User app password")
                .build();
        biometricPrompt.authenticate(promptInfo);
    }

    public void PatternFunction() {
        SharedPreferences sharedPreferences = getSharedPreferences("PREFS", 0);
        String password = sharedPreferences.getString("password", "0");
        if (password.equals("0")) {
            // Intent to navigate to Create Password Screen
            Intent intent = new Intent(getApplicationContext(), CreatePasswordActivity.class);
            startActivity(intent);
            //finish();
        } else {
            // Intent to navigate to Input Password Screen
            Intent intent = new Intent(getApplicationContext(), InputPasswordActivity.class);
            startActivity(intent);
            //finish();
        }
    }

    public void patternStatusChecking() {
        SharedPreferences sharedPreferences = getSharedPreferences("MyLoginPatternBiometricInfo", 0);
        String PatternStatus = sharedPreferences.getString("pattern", "0");
        if (PatternStatus.equalsIgnoreCase("enable")) {
            //tvpattern.setEnabled(true);
            PatternFunction();
        } else {
            //tvpattern.setEnabled(false);
            //tvpattern.setTextColor(Color.GRAY);
            alertDialogPromtforPattern();
        }
    }

    public void alertDialogPromtforPattern() {
        AlertDialog.Builder al = new AlertDialog.Builder(this);
        al.setTitle("Pattern Login Disabled");
        al.setMessage("To enable the pattern login go to profile tab of the app and enable it..");
        al.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {


            }
        });

        al.create();
        al.show();
    }

    public void alertDialogPromtforBiometric() {
        AlertDialog.Builder al = new AlertDialog.Builder(this);
        al.setTitle("Biometric Login Disabled");
        al.setMessage("To enable the Biometric login go to profile tab of the app and enable it..");
        al.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {


            }
        });

        al.create();
        al.show();
    }

    public void BiometricStatusChecking() {
        SharedPreferences sharedPreferences = getSharedPreferences("MyLoginPatternBiometricInfo", 0);
        String BiometricStatus = sharedPreferences.getString("biometric", "0");
        if (BiometricStatus.equalsIgnoreCase("enable")) {
            BiometricLoginFunction();
        } else {
            alertDialogPromtforBiometric();
        }
    }

}