package com.example.treeco;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.tabs.TabLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class EachEventDetailsShow extends AppCompatActivity {
    public static int EventIDForParticipation;
    public static int LoginInfo;
    TextView Registerbtn;
    ProgressBar LoadingPB;
    String LoginIDRetrived;
    ImageView back;
    TextView EventName, EventLocation, EvtDate, EvtTime, EventDesc, TreeName, NoofTree, SqfeetArea;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_eventdetails2);
        Registerbtn = findViewById(R.id.register);
        LoadingPB = findViewById(R.id.idLoadingPB);
        EventName = findViewById(R.id.eventname);
        EventLocation = findViewById(R.id.evtlocation);
        EvtDate = findViewById(R.id.dateset);
        EvtTime = findViewById(R.id.timeset);
        EventDesc = findViewById(R.id.eventdesc);
        TreeName = findViewById(R.id.treename);
        NoofTree = findViewById(R.id.notree);
        SqfeetArea = findViewById(R.id.pltarea);
        back = findViewById(R.id.back);

        //Shared Preferences value fetch
        SharedPreferences shrPrf;
        shrPrf = getSharedPreferences("MyLoginInfo", Context.MODE_PRIVATE);
        LoginIDRetrived = shrPrf.getString("login_userid", "");
        LoginInfo = Integer.parseInt(LoginIDRetrived);


        Registerbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Registerbtn.getText().toString().equalsIgnoreCase("Register")) {
                    postDataUsingVolleyToStoreUserRegister();
                } else {
                    Toast.makeText(getApplicationContext(), "Already Registered", Toast.LENGTH_LONG).show();
                }

            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), EventDetails.class);
                startActivity(intent);
            }
        });


        //Toast.makeText(this,""+EventIDForParticipation,Toast.LENGTH_LONG).show();
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        ParticipationFragment fragment1 = new ParticipationFragment();
        fragmentTransaction.replace(R.id.fragmentadd, fragment1);
        fragmentTransaction.commit();

        //Registation Checking
        Intent i = getIntent();
        ArrayList eventArray = i.getIntegerArrayListExtra("EventIDArray");
        int position = i.getIntExtra("position", -1);
        if (position == -1) {
            Toast.makeText(this, "Event details not found", Toast.LENGTH_LONG).show();
        } else {
            EventIDForParticipation = (int) eventArray.get(position);
            if (UpcomingEventFragment.Event_id_list.contains(EventIDForParticipation) || ParticipationFragment.arPartipationUserID.contains(EventIDForParticipation)) {
                Registerbtn.setText("Registered");
            } else {
                Registerbtn.setText("Register");
            }
            postDataUsingVolleyToExtratEventDetails(EventIDForParticipation);
        }
        ActionBar actionBar;
        actionBar = getSupportActionBar();
        // Define ColorDrawable object and parse color
        // using parseColor method
        // with color hash code as its parameter
        ColorDrawable colorDrawable
                = new ColorDrawable(Color.parseColor("#32CB00"));
        // Set BackgroundDrawable
        actionBar.setBackgroundDrawable(colorDrawable);
        // actionBar.setTitle(Html.fromHtml("<font color='#ffffff'>tre</font>"+"<font color='#ffffff' face='sans-serif-black'>e</font>"+"<font color='#ffffff'>co</font>"));
        actionBar.setTitle(Html.fromHtml("<font color=#ffffff>" + "<small>"
                + "tre" + "</small>" + "" + "<big>" + "e"
                + "</big>" + "" + "<small>" + "co" + "</small>"));
        TabLayout tabLayout = findViewById(R.id.tablayout);
        tabLayout.setSelectedTabIndicatorColor(getResources().getColor(R.color.trees_near_by_color));
        tabLayout.setTabTextColors(getResources().getColor(R.color.this_feature_isn_t_available_color), getResources().getColor(R.color.trees_near_by_color));
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {

                tabLayout.setTabTextColors(getResources().getColor(R.color.this_feature_isn_t_available_color), getResources().getColor(R.color.trees_near_by_color));
                tabLayout.setSelectedTabIndicatorColor(getResources().getColor(R.color.trees_near_by_color));


                if (tabLayout.getSelectedTabPosition() == 0) {
                    try {
                        FragmentManager fragmentManager = getSupportFragmentManager();
                        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                        ParticipationFragment fragment1 = new ParticipationFragment();
                        fragmentTransaction.replace(R.id.fragmentadd, fragment1);
                        fragmentTransaction.commit();
                    } catch (Exception e) {
                        //Toast.makeText(EachEventDetailsShow.this,e.getMessage(), Toast.LENGTH_SHORT).show();
                    }

                } else if (tabLayout.getSelectedTabPosition() == 1) {
                    try {
                        FragmentManager fragmentManager = getSupportFragmentManager();
                        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                        DiscussionFragment fragment2 = new DiscussionFragment();
                        fragmentTransaction.replace(R.id.fragmentadd, fragment2);
                        fragmentTransaction.commit();
                    } catch (Exception e) {
                        //Toast.makeText(EachEventDetailsShow.this,e.getMessage(), Toast.LENGTH_SHORT).show();
                    }

                }

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    private void postDataUsingVolleyToStoreUserRegister() {
        //Toast.makeText(getApplicationContext(),"I am clicked",Toast.LENGTH_LONG).show();
        // url to post our data
        String url = VariableDecClass.IPAddress + "apiEventRegistration";
        LoadingPB.setVisibility(View.VISIBLE);
        try {
            JSONObject params = new JSONObject();
            params.put("event_id", EventIDForParticipation);
            params.put("user_id", LoginInfo);
            //Toast.makeText(eventdetails2.this, ""+params, Toast.LENGTH_SHORT).show();

            // creating a new variable for our request queue
            RequestQueue queue1 = Volley.newRequestQueue(EachEventDetailsShow.this);
            // on below line we are calling a string
            // request method to post the data to our API
            // in this we are calling a post method.
            JsonObjectRequest request1 = new JsonObjectRequest(Request.Method.POST, url, params, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    LoadingPB.setVisibility(View.GONE);
                    //Toast.makeText(getApplicationContext(),""+response,Toast.LENGTH_LONG).show();
                    try {
                        String Status = response.getString("status");
                        //int ParticpationID = response.getInt("participant_id");
                        Registerbtn.setText("Registered");
                        Intent intent = new Intent(getApplicationContext(), EachEventDetailsShow.class);
                        startActivity(intent);
                        //Toast.makeText(getApplicationContext(),""+Status,Toast.LENGTH_LONG).show();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    LoadingPB.setVisibility(View.GONE);
                    //Toast.makeText(EachEventDetailsShow.this, "Fail to respond = " + error, Toast.LENGTH_SHORT).show();
                }
            });

            queue1.add(request1);
        } catch (Exception e) {
            //Toast.makeText(EachEventDetailsShow.this, "Fail to create json = " + e, Toast.LENGTH_SHORT).show();
            LoadingPB.setVisibility(View.GONE);
        }

    }

    private void postDataUsingVolleyToExtratEventDetails(int eventid) {
        //Toast.makeText(getApplicationContext(),"I am clicked",Toast.LENGTH_LONG).show();
        // url to post our data
        String url = VariableDecClass.IPAddress + "apiEventDetails";
        LoadingPB.setVisibility(View.VISIBLE);
        // creating a new variable for our request queue
        RequestQueue queue = Volley.newRequestQueue(EachEventDetailsShow.this);

        // on below line we are calling a string
        // request method to post the data to our API
        // in this we are calling a post method.
        StringRequest request = new StringRequest(Request.Method.POST, url, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                // inside on response method we are
                // hiding our progress bar
                // and setting data to edit text as empty
                LoadingPB.setVisibility(View.GONE);

                // on below line we are displaying a success toast message.
                //Toast.makeText(LoginActivity.this, "Response received", Toast.LENGTH_SHORT).show();
                try {
                    // on below line we are parsing the response
                    // to json object to extract data from it.
                    JSONArray respArray = new JSONArray(response);
                    for (int i = 0; i < respArray.length(); i++) {
                        //Toast.makeText(eventdetails2.this,"rsponse from event"+respArray,Toast.LENGTH_LONG).show();
                        String Eventname = respArray.getJSONObject(i).getString("name");
                        String Eventloc = respArray.getJSONObject(i).getString("location");
                        EventName.setText(Eventname);
                        EventLocation.setText(Eventloc);
                        String Eventdatetime = respArray.getJSONObject(i).getString("start_on");
                        String[] splited = Eventdatetime.split("\\s+");
                        EvtDate.setText(splited[0]);
                        String[] splitedtimeval = splited[1].split("\\+");
                        EvtTime.setText(splitedtimeval[0] + " AM");
                        String Eventdesc = respArray.getJSONObject(i).getString("description");
                        EventDesc.setText(Eventdesc);
                        JSONObject plantation = respArray.getJSONObject(i).getJSONObject("plantation");
                        String num_of_plants = plantation.getString("num_of_plants");
                        String plantation_area = plantation.getString("plantation_area");
                        int plantid = plantation.getInt("plant_type_id");
                        NoofTree.setText(num_of_plants);
                        SqfeetArea.setText(plantation_area + " sq.Mts");
                        FetchtreenameUsingVolley(plantid);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // method to handle errors.
               // Toast.makeText(EachEventDetailsShow.this, "FetchingFailed", Toast.LENGTH_SHORT).show();
                LoadingPB.setVisibility(View.GONE);

            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                // below line we are creating a map for
                // storing our values in key and value pair.
                Map<String, String> params = new HashMap<String, String>();

                // on below line we are passing our key
                // and value pair to our parameters.
                params.put("id", "" + EventIDForParticipation);

                // at last we are
                // returning our params.
                return params;
            }
        };
        // below line is to make
        // a json object request.
        queue.add(request);

    }

    private void FetchtreenameUsingVolley(int id) {
        String url = VariableDecClass.IPAddress + "apiTreeTypeList";
        LoadingPB.setVisibility(View.VISIBLE);

        // creating a new variable for our request queue
        RequestQueue queue = Volley.newRequestQueue(EachEventDetailsShow.this);

        // on below line we are calling a string
        // request method to post the data to our API
        // in this we are calling a post method.
        StringRequest request = new StringRequest(Request.Method.POST, url, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                // inside on response method we are
                // hiding our progress bar
                // and setting data to edit text as empty
                LoadingPB.setVisibility(View.GONE);

                // on below line we are displaying a success toast message.
                // Toast.makeText(PlantActivity2.this, "Data Fetched", Toast.LENGTH_SHORT).show();
                try {
                    // on below line we are parsing the response
                    // to json object to extract data from it.
                    JSONObject respObj = new JSONObject(response);
                    String treename = respObj.getString("Common_Name");
                    String[] splited = treename.split("\\s+");
                    TreeName.setText(splited[0]);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // method to handle errors.
                //Toast.makeText(EachEventDetailsShow.this, "Fail to get response = " + error, Toast.LENGTH_SHORT).show();
                LoadingPB.setVisibility(View.GONE);
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                // below line we are creating a map for
                // storing our values in key and value pair.
                Map<String, String> params = new HashMap<String, String>();

                // on below line we are passing our key
                // and value pair to our parameters.
                params.put("id", "" + id);
                params.put("includeDeleted", "" + false);

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


