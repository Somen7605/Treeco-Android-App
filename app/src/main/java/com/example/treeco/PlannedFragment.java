package com.example.treeco;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class PlannedFragment extends Fragment {

    View planned_fragment;
    ListView listView, listView2, listView3;
    ArrayList<Integer> EventID;
    ArrayList<String> EventName, EventDatetime, EventLocation;
    TextView pendingApproval, pendingPayment, confirmedPayment;

    public static String getCalculatedDate(String dateFormat, int days) {
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat s = new SimpleDateFormat(dateFormat);
        cal.add(Calendar.DAY_OF_YEAR, days);
        return s.format(new Date(cal.getTimeInMillis()));
    }

    public static String getCurrentDate(String dateFormat) {
        Calendar cal = Calendar.getInstance();
        cal.get(Calendar.DAY_OF_YEAR);
        SimpleDateFormat s = new SimpleDateFormat(dateFormat);
        return s.format(new Date(cal.getTimeInMillis()));
    }

    public static Date getDateFromString(String date) {

        Date dt = null;
        if (date != null) {
            try {
                dt = new Date(new SimpleDateFormat("yyyy-MM-dd").parse(date).getTime());

            } catch (ParseException pe) {
                pe.printStackTrace();
            }
        }
        return dt;
    }

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
        planned_fragment = inflater.inflate(R.layout.fragment_planned, container, false);
        listView = planned_fragment.findViewById(R.id.listview1);
        listView2 = planned_fragment.findViewById(R.id.listview2);
        listView3 = planned_fragment.findViewById(R.id.listview3);
        pendingApproval = planned_fragment.findViewById(R.id.pending_approval_txt);
        pendingPayment = planned_fragment.findViewById(R.id.pending_payment_txt);
        confirmedPayment = planned_fragment.findViewById(R.id.confirmed_txt);
        SharedPreferences shrPrf1 = getActivity().getSharedPreferences("MyLoginInfo", Context.MODE_PRIVATE);
        String userid = shrPrf1.getString("login_userid", "");
        postDataUsingVolleyMyEvents(userid);


        return planned_fragment;
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
                //counter for change of textviews
                int c = 0;

                try {
                    JSONArray respArray = new JSONArray(response);
                    Log.d("response Array", "" + respArray);
                    // String thisweek=getCalculatedDate("yyyy-MM-dd", 7);
                    //Date thisweekdate=getDateFromString(thisweek);
                    String CurrentDate = getCurrentDate("yyyy-MM-dd");
                    Date CurrentDateVal = getDateFromString(CurrentDate);
                    for (int i = 0; i < respArray.length(); i++) {
                        Integer SingleEventID = respArray.getJSONObject(i).getInt("id");
                        String SingleEventName = respArray.getJSONObject(i).getString("name");
                        String Singlelocation = respArray.getJSONObject(i).getString("location");
                        String SingleDateandTime = respArray.getJSONObject(i).getString("start_on");
                        String[] splited = SingleDateandTime.split("\\s+");
                        String dateval = splited[0];
                        Date fetchdate = getDateFromString(dateval);
                        //oast.makeText(getActivity(), "Outside"+CurrentDateVal+ "other date"+fetchdate, Toast.LENGTH_SHORT).show();
                        if (fetchdate.compareTo(CurrentDateVal) >= 0) {
                            //Toast.makeText(getActivity(), "Inside", Toast.LENGTH_SHORT).show();
                            EventID.add(SingleEventID);
                            EventName.add(SingleEventName);
                            EventLocation.add(Singlelocation);
                            EventDatetime.add(SingleDateandTime);
                            BaseAdapterMEPlannedPA baseAdapter7 = new BaseAdapterMEPlannedPA(getContext(), EventID, EventName, EventLocation, EventDatetime);
                            listView.setAdapter(baseAdapter7);
                            setDynamicHeight(listView);
                            BaseAdapterMEPlannedPP baseAdapter6 = new BaseAdapterMEPlannedPP(getContext());
                            listView2.setAdapter(baseAdapter6);
                            setDynamicHeight(listView2);
                            BaseAdapterPlannedConfirmed baseAdapter5 = new BaseAdapterPlannedConfirmed(getContext());
                            listView3.setAdapter(baseAdapter5);
                            setDynamicHeight(listView3);
                            c++;
                        }
                    }
                    if (c == 0) {
                        pendingApproval.setVisibility(View.VISIBLE);
                        pendingPayment.setVisibility(View.VISIBLE);
                        confirmedPayment.setVisibility(View.VISIBLE);
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