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
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
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


public class UpcomingEventFragment extends Fragment {
    public static ArrayList<Integer> Event_id_list;
    View fragment_upcoming;
    ListView listView, listView2, listView3;
    ProgressBar LoadingPB;
    ArrayList<Integer> EventIDthisweek, EventIDnextweek, EventID;
    ArrayList<String> EventName, EventDatetime, EventLocation;
    ArrayList<String> EventNamethisweek, EventDatetimethisweek, EventLocationthisweek;
    ArrayList<String> EventNameNextweek, EventDatetimeNextweek, EventLocationNextweek;
    String LoginIDRetrived = "0";
    TextView this_week, next_week, further_week;

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
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        fragment_upcoming = inflater.inflate(R.layout.fragment_upcoming_event, container, false);
        //loginInfo Retrive
        SharedPreferences shrPrf;
        shrPrf = getActivity().getSharedPreferences("MyLoginInfo", Context.MODE_PRIVATE);
        LoginIDRetrived = shrPrf.getString("login_userid", "");
        listView = fragment_upcoming.findViewById(R.id.listview1);
        listView2 = fragment_upcoming.findViewById(R.id.listview2);
        listView3 = fragment_upcoming.findViewById(R.id.listview3);
        LoadingPB = fragment_upcoming.findViewById(R.id.idLoadingPB);
        this_week = fragment_upcoming.findViewById(R.id.this_week);
        next_week = fragment_upcoming.findViewById(R.id.next_week);
        further_week = fragment_upcoming.findViewById(R.id.further_week);

        //volley call
        postDataUsingVolleyFetchMyPresence();
        postDataUsingVolleyFetchEvents();
        return fragment_upcoming;
    }

    //Api to access EventType
    private void postDataUsingVolleyFetchEvents() {
        // url to post our data
        String url = VariableDecClass.IPAddress + "apiEventList";
        LoadingPB.setVisibility(View.VISIBLE);
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
                LoadingPB.setVisibility(View.GONE);
                EventIDthisweek = new ArrayList<>();
                EventNamethisweek = new ArrayList<>();
                EventLocationthisweek = new ArrayList<>();
                EventDatetimethisweek = new ArrayList<>();

                EventIDnextweek = new ArrayList<>();
                EventNameNextweek = new ArrayList<>();
                EventLocationNextweek = new ArrayList<>();
                EventDatetimeNextweek = new ArrayList<>();

                EventID = new ArrayList<>();
                EventName = new ArrayList<>();
                EventLocation = new ArrayList<>();
                EventDatetime = new ArrayList<>();
                //current date format
                String thisweek = getCalculatedDate("yyyy-MM-dd", 7);
                Date thisweekdate = getDateFromString(thisweek);
                String Nextweek = getCalculatedDate("yyyy-MM-dd", 14);
                Date Nextweekdate = getDateFromString(Nextweek);
                String CurrentDate = getCurrentDate("yyyy-MM-dd");
                Date CurrentDateVal = getDateFromString(CurrentDate);
                //Toast.makeText(getActivity(),"newdate"+CurrentDate,Toast.LENGTH_LONG).show();
                // on below line we are displaying a success toast message.
                try {
                    // on below line we are parsing the response
                    // to json object to extract data from it.
                    JSONArray respArray = new JSONArray(response);
                    Log.d("response Length:", String.valueOf(respArray));
                    int c1 = 0, c2 = 0, c3 = 0;


                    for (int i = 0; i < respArray.length(); i++) {
                        Integer SingleEventID = respArray.getJSONObject(i).getInt("id");
                        String SingleEventName = respArray.getJSONObject(i).getString("name");
                        String Singlelocation = respArray.getJSONObject(i).getString("location");
                        String SingleDateandTime = respArray.getJSONObject(i).getString("start_on");
                        String[] splited = SingleDateandTime.split("\\s+");
                        String dateval = splited[0];
                        Date fetchdate = getDateFromString(dateval);


                        if (fetchdate.compareTo(CurrentDateVal) >= 0 && fetchdate.compareTo(thisweekdate) < 0) {
                            EventIDthisweek.add(SingleEventID);
                            EventNamethisweek.add(SingleEventName);
                            EventDatetimethisweek.add(SingleDateandTime);
                            EventLocationthisweek.add(Singlelocation);
                            BaseAdapterUEThisWeek baseAdapter3 = new BaseAdapterUEThisWeek(getContext(), EventIDthisweek, EventNamethisweek, EventLocationthisweek, EventDatetimethisweek);
                            listView.setAdapter(baseAdapter3);
                            setDynamicHeight(listView);
                            c1++;
                         /* listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                              @Override
                              public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                  Intent intent = new Intent(getActivity(), eventdetails2.class);
                                  intent.putExtra("position", position);
                                  intent.putIntegerArrayListExtra("EventIDArray", EventIDthisweek);
                                  getActivity().startActivity(intent);
                              }
                          });*/
                        } else if (fetchdate.compareTo(thisweekdate) >= 0 && fetchdate.compareTo(Nextweekdate) < 0) {
                            EventIDnextweek.add(SingleEventID);
                            EventNameNextweek.add(SingleEventName);
                            EventDatetimeNextweek.add(SingleDateandTime);
                            EventLocationNextweek.add(Singlelocation);
                            BaseAdapterUENextWeek baseAdapterNext = new BaseAdapterUENextWeek(getContext(), EventIDnextweek, EventNameNextweek, EventLocationNextweek, EventDatetimeNextweek);
                            listView2.setAdapter(baseAdapterNext);
                            setDynamicHeight(listView2);
                            c2++;
                           /* listView2.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                    Intent intent = new Intent(getActivity(), eventdetails2.class);
                                    intent.putExtra("position", position);
                                    intent.putIntegerArrayListExtra("EventIDArray", EventIDnextweek);
                                    getActivity().startActivity(intent);
                                }
                            });*/

                        } else if (fetchdate.compareTo(Nextweekdate) >= 0) {
                            EventID.add(SingleEventID);
                            EventName.add(SingleEventName);
                            EventDatetime.add(SingleDateandTime);
                            EventLocation.add(Singlelocation);
                            BaseAdapterUEFurtherWeeks baseAdapterfuture = new BaseAdapterUEFurtherWeeks(getContext(), EventID, EventName, EventLocation, EventDatetime);
                            listView3.setAdapter(baseAdapterfuture);
                            setDynamicHeight(listView3);
                            c3++;
                         /* listView3.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                              @Override
                              public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                  Intent intent = new Intent(getActivity(), eventdetails2.class);
                                  intent.putExtra("position", position);
                                  intent.putIntegerArrayListExtra("EventIDArray", EventID);
                                  getActivity().startActivity(intent);
                              }
                          });*/

                        }

                    }
                    if (c1 == 0) {
                        this_week.setVisibility(View.VISIBLE);
                    } else if (c2 == 0) {
                        next_week.setVisibility(View.VISIBLE);
                    } else if (c3 == 0) {
                        further_week.setVisibility(View.VISIBLE);
                    }

                } catch (JSONException e) {
                    //Toast.makeText(getActivity(), "Event list error" + e, Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // method to handle errors.
                //Toast.makeText(getActivity(), "Maintainer Not loaded" + error, Toast.LENGTH_SHORT).show();
                LoadingPB.setVisibility(View.GONE);

            }
        });
        // below line is to make
        // a json object request.
        queue.add(request);
    }

    private void postDataUsingVolleyFetchMyPresence() {
        // url to post our data
        String url = VariableDecClass.IPAddress + "apiPerticipantEventList";
        LoadingPB.setVisibility(View.VISIBLE);
        Event_id_list = new ArrayList<>();
        RequestQueue queue = Volley.newRequestQueue(getActivity());

        // on below line we are calling a string
        // request method to post the data to our API
        // in this we are calling a post method.
        StringRequest request = new StringRequest(Request.Method.POST, url, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    JSONArray respArray = new JSONArray(response);

                    for (int i = 0; i < respArray.length(); i++) {
                        int event_id = respArray.getJSONObject(i).getInt("event_id");
                        Event_id_list.add(event_id);
                    }

                } catch (JSONException e) {
                    //Toast.makeText(getActivity(), "Event list error" + e, Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // method to handle errors.
                //Toast.makeText(getActivity(), "Maintainer Not loaded" + error, Toast.LENGTH_SHORT).show();
                LoadingPB.setVisibility(View.GONE);

            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                // below line we are creating a map for
                // storing our values in key and value pair.
                Map<String, String> params = new HashMap<String, String>();
                params.put("user_id", LoginIDRetrived);

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