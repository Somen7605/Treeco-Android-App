package com.example.treeco;

import static android.view.View.VISIBLE;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

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

public class Update_Password extends AppCompatActivity {
    EditText pass,rePass;
    ImageView update;
    String id;
    SharedPreferences shrPrf;
    private ProgressBar loadingPB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_password);
        getSupportActionBar().hide();
        loadingPB = findViewById(R.id.idLoadingPB);
        pass=findViewById(R.id.pass_edit_text);
        rePass=findViewById(R.id.re_pass_edit_text);
        update=findViewById(R.id.imgUpdate);


        shrPrf = getSharedPreferences("MyLoginInfo", Context.MODE_PRIVATE);
        id=shrPrf.getString("login_userid","");




        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String password=pass.getText().toString();
                String rePassword=rePass.getText().toString();


                if(password=="")
                {
                    pass.requestFocus();
                    pass.setError("Password Cannot be Empty");
                }
                else if(rePassword=="")
                {
                    rePass.requestFocus();
                    rePass.setError("Password Cannot be Empty");

                }
                else if(!password.equals(rePassword))
                {
                    Toast.makeText(Update_Password.this, "Password is Different", Toast.LENGTH_SHORT).show();
                    rePass.setError("Password Not Matching");
                }
                else {
                    postDataUsingVolley(id,password);
                }
            }
        });
    }

    private void postDataUsingVolley(String id, String password) {

        String url = VariableDecClass.IPAddress+"apiUpdatePassword";
        loadingPB.setVisibility(VISIBLE);

        RequestQueue queue = Volley.newRequestQueue(Update_Password.this);
        StringRequest request=new StringRequest(Request.Method.POST,url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                loadingPB.setVisibility(View.GONE);

                try {
                    JSONObject respObj = new JSONObject(response);
                    //Toast.makeText(send_Password.this, ""+respObj, Toast.LENGTH_SHORT).show();

                    String status=respObj.getString("status");
                    if(status.equals("Password Updated Successfully."))
                    {
                        //Toast.makeText(Update_Password.this, ""+status, Toast.LENGTH_SHORT).show();

                        SharedPreferences.Editor editor = shrPrf.edit();
                        editor.putString("login_password",password);
                        editor.commit();

                        Intent i=new Intent(Update_Password.this,UserDashboard.class);
                        //i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(i);

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //Toast.makeText(Update_Password.this, ""+error.getMessage().toString(), Toast.LENGTH_SHORT).show();

            }
        }){

            @Override
            protected Map<String, String> getParams() {
                // below line we are creating a map for
                // storing our values in key and value pair.
                Map<String,String> params = new HashMap<String, String>();

                // on below line we are passing our key
                // and value pair to our parameters.
                params.put("id",id);
                params.put("password",password);


                // at last we are
                // returning our params.
                return params;
            }

        };

        queue.add(request);
    }
}