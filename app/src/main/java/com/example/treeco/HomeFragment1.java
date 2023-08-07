package com.example.treeco;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.denzcoskun.imageslider.models.SlideModel;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class HomeFragment1 extends Fragment {
    TextView treetag, treemaintain, eventsplanned, eventsexecuted, plantationplanned, plantationsurvived, carbonfootprint, coins, namedisplay;
    CardView nurseryCardView, environmentNewsCardView;
    int[] images = {R.drawable.image_tree_plantation_camp, R.drawable.image_forest_day};
    private ProgressBar loadingPB;

    public HomeFragment1() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_home1, container, false);
        treetag = v.findViewById(R.id.treetaggedid);
        treemaintain = v.findViewById(R.id.treemaintainedid);

        eventsplanned = v.findViewById(R.id.eventsplanned);
        eventsexecuted = v.findViewById(R.id.eventsexecuted);

        plantationplanned = v.findViewById(R.id.plantationplanned);
        plantationsurvived = v.findViewById(R.id.plantationsurvived);
        namedisplay = v.findViewById(R.id.welcome_name);
        carbonfootprint = v.findViewById(R.id.carbonfp);
        coins = v.findViewById(R.id.coins);
        nurseryCardView = v.findViewById(R.id.NurseryCardView);
        environmentNewsCardView = v.findViewById(R.id.EnvironmentNewsCardView);
        loadingPB = v.findViewById(R.id.idLoadingPB);
        SharedPreferences shrPrf;
        shrPrf = getActivity().getSharedPreferences("MyLoginInfo", Context.MODE_PRIVATE);
        String user_id = shrPrf.getString("login_userid", "");
        postDataUsingVolley(user_id);

        ImageSlider imageSlider = v.findViewById(R.id.imageSlider);
        List<SlideModel> slideModels = new ArrayList<>();
        slideModels.add(new SlideModel(R.drawable.image_forest_day, ScaleTypes.FIT));
        slideModels.add(new SlideModel(R.drawable.image_tree_plantation_camp, ScaleTypes.FIT));
        imageSlider.setImageList(slideModels);

        //onclicklisterner for cardviews
        nurseryCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), NearByNursery.class));
            }
        });
        environmentNewsCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), "Environment News is not yet implemented", Toast.LENGTH_SHORT).show();
            }
        });
        return v;
    }

    private void postDataUsingVolley(String userid) {
        // url to post our data
        String url = VariableDecClass.IPAddress + "apiDashboard";
        loadingPB.setVisibility(View.VISIBLE);

        // creating a new variable for our request queue
        RequestQueue queue = Volley.newRequestQueue(getActivity());

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
                //Toast.makeText(RegwithOTP.this, "OTP received", Toast.LENGTH_SHORT).show();
                try {
                    // on below line we are parsing the response
                    // to json object to extract data from it.
                    JSONObject respObj = new JSONObject(response);

                    //Toast.makeText(getActivity(), "responded " + respObj, Toast.LENGTH_SHORT).show();
                    String carbon_fp = respObj.getString("carbon_fp");
                    String coinsval = respObj.getString("coins");
                    carbonfootprint.setText(carbon_fp + "kg");
                    coins.setText("" + coinsval);

                    String t_tagges = respObj.getString("t_tagges");
                    String t_maintained = respObj.getString("t_maintained");
                    treetag.setText(t_tagges);
                    treemaintain.setText(t_maintained);

                    String e_planed = respObj.getString("e_planed");
                    String e_executed = respObj.getString("e_executed");
                    eventsplanned.setText(e_planed);
                    eventsexecuted.setText(e_executed);

                    String p_planed = respObj.getString("p_planed");
                    String p_survived = respObj.getString("p_survived");
                    plantationplanned.setText(p_planed);
                    plantationsurvived.setText(p_survived);
                    postDataUsingVolley1(userid);


                } catch (JSONException e) {
                    e.printStackTrace();
                    //Toast.makeText(getActivity(), "Fail to get response = " + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // method to handle errors.
                //Toast.makeText(getActivity(), "Fail to get response = " + error, Toast.LENGTH_SHORT).show();
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
                params.put("user_id", userid);
                // at last we are
                // returning our params.
                return params;
            }
        };
        // below line is to make
        // a json object request.
        queue.add(request);

    }

    private void postDataUsingVolley1(String userid) {
        // url to post our data
        String url = VariableDecClass.IPAddress + "apiUserInfo";
        loadingPB.setVisibility(View.VISIBLE);

        // creating a new variable for our request queue
        RequestQueue queue1 = Volley.newRequestQueue(getActivity());
        // on below line we are calling a string
        // request method to post the data to our API
        // in this we are calling a post method.
        StringRequest request1 = new StringRequest(Request.Method.POST, url, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                // inside on response method we are
                // hiding our progress bar
                // and setting data to edit text as empty
                loadingPB.setVisibility(View.GONE);

                // on below line we are displaying a success toast message.
                //Toast.makeText(RegwithOTP.this, "OTP received", Toast.LENGTH_SHORT).show();
                try {
                    // on below line we are parsing the response
                    // to json object to extract data from it.
                    JSONObject respObj = new JSONObject(response);
                    //Toast.makeText(getActivity(), "resonse " + respObj, Toast.LENGTH_SHORT).show();
                    String user_name = respObj.getString("name");
                    // Toast.makeText(getActivity(), "name " + user_name, Toast.LENGTH_SHORT).show();
                    namedisplay.setText("Welcome " + user_name);


                } catch (JSONException e) {
                    e.printStackTrace();
                    //Toast.makeText(getActivity(), "" + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // method to handle errors.
                //Toast.makeText(getActivity(), "Fail to get response = " + error, Toast.LENGTH_SHORT).show();
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
                params.put("user_id", userid);
                // at last we are
                // returning our params.
                return params;
            }
        };
        // below line is to make
        // a json object request.
        queue1.add(request1);

    }
}