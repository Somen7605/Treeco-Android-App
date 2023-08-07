package com.example.treeco;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import androidx.fragment.app.Fragment;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;


public class PastFragment extends Fragment {

    View v;
    ListView listView;
    ArrayList<Integer> EventID;
    ArrayList<String> EventName, EventDatetime, EventLocation;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_past, container, false);
        listView = v.findViewById(R.id.listview);
        SharedPreferences shrPrf1 = getActivity().getSharedPreferences("MyLoginInfo", Context.MODE_PRIVATE);
        String userid = shrPrf1.getString("login_userid", "");
        postDataUsingVolleyMyEvents(userid);


        return v;
    }

    private void postDataUsingVolleyMyEvents(String Useridval) {

        String url = VariableDecClass.IPAddress + "apiMyEvents";
        RequestQueue queue = Volley.newRequestQueue(getActivity());

        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                EventID = new ArrayList<>();
                EventName = new ArrayList<>();
                EventLocation = new ArrayList<>();
                EventDatetime = new ArrayList<>();

                try {
                    JSONArray respArray = new JSONArray(response);
                    //Toast.makeText(getActivity(), "Response received"+respArray.length(), Toast.LENGTH_SHORT).show();

                    for (int i = 0; i < respArray.length(); i++) {
                        Integer SingleEventID = respArray.getJSONObject(i).getInt("id");
                        EventID.add(SingleEventID);
                        String SingleEventName = respArray.getJSONObject(i).getString("name");
                        EventName.add(SingleEventName);
                        String Singlelocation = respArray.getJSONObject(i).getString("location");
                        EventLocation.add(Singlelocation);
                        String SingleDateandTime = respArray.getJSONObject(i).getString("start_on");
                        EventDatetime.add(SingleDateandTime);
                        //Date
                        Calendar c = Calendar.getInstance();
                        int day = c.get(Calendar.DAY_OF_MONTH);
                        int month = c.get(Calendar.MONTH);
                        int year = c.get(Calendar.YEAR);
                        String Datetime = EventDatetime.get(i);
                        String[] splited = Datetime.split(" ");
                        String date = splited[0];
                        String[] splited2 = date.split("-");

                        int yr = Integer.parseInt(splited2[0]);
                        int mn = Integer.parseInt(splited2[1]);
                        int da = Integer.parseInt(splited2[2]);

                        //Toast.makeText(getActivity(), ""+yr, Toast.LENGTH_SHORT).show();

                        if (yr < year || mn < month || da < day) {
                            BaseAdapterMEPast baseAdapter8 = new BaseAdapterMEPast(getContext(), EventID, EventName, EventLocation, EventDatetime);
                            listView.setAdapter(baseAdapter8);
                        }


                        //BaseAdapter8 baseAdapter8=new BaseAdapter8(getContext(),EventID,EventName,EventLocation,EventDatetime);
                        //listView.setAdapter(baseAdapter8);
                    }


                } catch (JSONException e) {
                    //Toast.makeText(getActivity(), "Event list error" + e, Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                //Toast.makeText(getActivity(), "Maintainer Not loaded" + error, Toast.LENGTH_SHORT).show();

            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                // below line we are creating a map for
                // storing our values in key and value pair.
                Map<String, String> params = new HashMap<String, String>();

                // on below line we are passing our key
                // and value pair to our parameters.
                params.put("user_id", Useridval);
                // at last we are
                // returning our params.
                return params;
            }
        };
        queue.add(request);

    }
}