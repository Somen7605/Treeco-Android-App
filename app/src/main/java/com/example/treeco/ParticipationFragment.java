package com.example.treeco;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;

import androidx.fragment.app.Fragment;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class ParticipationFragment extends Fragment {

    public static ArrayList<Integer> arPartipationUserID;
    View fragmentpart;
    ListView listView;
    ProgressBar LoadingPB;

    public static void setDynamicHeight(ListView listView) {
        ListAdapter adapter = listView.getAdapter();
        //check adapter if null
        if (adapter == null) {
            return;
        }
        int height = 0;
        int desiredWidth = View.MeasureSpec.makeMeasureSpec(listView.getWidth(), View.MeasureSpec.UNSPECIFIED);
        for (int i = 0; i < adapter.getCount(); i++) {
            View listItem = adapter.getView(i, null, listView);
            listItem.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);
            height += listItem.getMeasuredHeight();
        }
        ViewGroup.LayoutParams layoutParams = listView.getLayoutParams();
        layoutParams.height = height + (listView.getDividerHeight() * (adapter.getCount() - 1));
        listView.setLayoutParams(layoutParams);
        listView.requestLayout();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        fragmentpart = inflater.inflate(R.layout.fragment_part_fragment, container, false);
        LoadingPB = fragmentpart.findViewById(R.id.idLoadingPB);
        listView = fragmentpart.findViewById(R.id.listview1);
        postDataUsingVolleyToretriveParticipant();
        return fragmentpart;
    }

    private void postDataUsingVolleyToretriveParticipant() {
        // url to post our data
        String url = VariableDecClass.IPAddress + "apiPerticipantList";
        ArrayList<Integer> arPartipationUserID = new ArrayList<>();
        ArrayList<String> arParticipationName = new ArrayList<>();
        LoadingPB.setVisibility(View.VISIBLE);
        try {
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
                    LoadingPB.setVisibility(View.GONE);
                    // on below line we are displaying a success toast message.
                    //Toast.makeText(RegwithOTP.this, "OTP received", Toast.LENGTH_SHORT).show();
                    try {
                        // on below line we are parsing the response
                        // to json object to extract data from it.
                        JSONArray resArray = new JSONArray(response);
                        // Toast.makeText(getActivity(), "resonse " + resArray, Toast.LENGTH_SHORT).show();
                        for (int i = 0; i < resArray.length(); i++) {
                            int id = resArray.getJSONObject(i).getInt("user_id");
                            arPartipationUserID.add(id);
                            String name = resArray.getJSONObject(i).getString("name");
                            arParticipationName.add(name);
                        }
                        //Toast.makeText(CreateEvent.this, "resonse " + arUserID, Toast.LENGTH_SHORT).show();
                        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, arParticipationName);
                        listView.setAdapter(arrayAdapter);
                        setDynamicHeight(listView);


                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }, new com.android.volley.Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    // method to handle errors.
                    //Toast.makeText(getActivity(), "Fail to get response = " + error, Toast.LENGTH_SHORT).show();
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
                    params.put("event_id", "" + EachEventDetailsShow.EventIDForParticipation);

                    // at last we are
                    // returning our params.
                    return params;
                }
            };
            // below line is to make
            // a json object request.
            queue1.add(request1);
        } catch (Exception e) {
            //Toast.makeText(getActivity(), "Fail to create json = " + e, Toast.LENGTH_SHORT).show();
            LoadingPB.setVisibility(View.GONE);
        }

    }
}