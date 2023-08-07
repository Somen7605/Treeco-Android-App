package com.example.treeco;

import static android.view.View.VISIBLE;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class send_Password extends AppCompatActivity implements View.OnClickListener {

    EditText email;
    ImageView sendPass;
    TextView signIn;
    private ProgressBar loadingPB;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_password);
        getSupportActionBar().hide();

        email = findViewById(R.id.editEmail);
        sendPass = findViewById(R.id.sendPassBtn);
        signIn = findViewById(R.id.txtSignIn);
        loadingPB = findViewById(R.id.idLoadingPB);

        signIn.setOnClickListener(this);
        sendPass.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {

        if (view.getId() == R.id.sendPassBtn) {
            if (email.getText().toString() != null) {
                String s = email.getText().toString().trim();
                postDataUsingVolley(s);


            }
        }
        if (view.getId() == R.id.txtSignIn) {
            startActivity(new Intent(send_Password.this, LoginActivity.class));
        }
    }

    private void postDataUsingVolley(String s) {

        String url = VariableDecClass.IPAddress + "apiResetPassword";
        loadingPB.setVisibility(VISIBLE);

        RequestQueue queue = Volley.newRequestQueue(send_Password.this);
        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                loadingPB.setVisibility(View.GONE);

                try {
                    JSONObject respObj = new JSONObject(response);
                    //Toast.makeText(send_Password.this, ""+respObj, Toast.LENGTH_SHORT).show();

                    String status = respObj.getString("status");
                    if (status.equals("Password reset successfully done.")) {
                        //Toast.makeText(send_Password.this, ""+status, Toast.LENGTH_SHORT).show();
                        signIn.setVisibility(View.VISIBLE);
                        Intent i = new Intent(send_Password.this, success_pass.class);
                        i.putExtra("email", s);
                        startActivity(i);


                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //Toast.makeText(send_Password.this, "" + error.getMessage().toString(), Toast.LENGTH_SHORT).show();

            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // below line we are creating a map for
                // storing our values in key and value pair.
                Map<String, String> params = new HashMap<String, String>();

                // on below line we are passing our key
                // and value pair to our parameters.
                params.put("email", s);


                // at last we are
                // returning our params.
                return params;
            }

        };

        queue.add(request);

    }
}