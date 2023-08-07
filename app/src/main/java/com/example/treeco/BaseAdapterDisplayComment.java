package com.example.treeco;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class BaseAdapterDisplayComment extends BaseAdapter {
    LayoutInflater inflater;
    Context context;
    ImageView sendreply;
    ArrayList<Integer> ArrayPID, ArrayCommentID, ArrayReplyCount, ArrayCommentCount;
    ArrayList<String> ArrayPName, ArrayPCooment, ArrayPCommentedOn;
    ArrayList<JSONArray> ArrayReplyJson;
    Integer[] SameUserVal;
    int replyval = 0;


    ArrayList<Integer> ArrayRPID;
    ArrayList<String> ArrayReplyName, ArrayReplyComment, ArrayReplyCommentedOn;

    public BaseAdapterDisplayComment(Context c, ArrayList<Integer> ArraycID, ArrayList<Integer> ArraypID, ArrayList<String> ArraypName, ArrayList<String> ArraypComment, ArrayList<String> ArraypCommentedon, ArrayList<Integer> ArrayRCount, ArrayList<JSONArray> ArrayReplyJson, ArrayList<Integer> ArrayCCount, Integer[] SameuserStatus) {
        inflater = (LayoutInflater.from(c));
        this.context = c;
        this.ArrayPID = ArraypID;
        this.ArrayPName = ArraypName;
        this.ArrayPCooment = ArraypComment;
        this.ArrayPCommentedOn = ArraypCommentedon;
        this.ArrayCommentID = ArraycID;
        this.ArrayReplyCount = ArrayRCount;
        this.ArrayReplyJson = ArrayReplyJson;
        this.ArrayCommentCount = ArrayCCount;
        this.SameUserVal = SameuserStatus;
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
    public int getCount() {
        return ArrayPName.size();
    }

    @Override
    public Object getItem(int i) {
        return ArrayPName.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int position, View convertview, ViewGroup parent) {
        convertview = inflater.inflate(R.layout.discussion_list, null);
        //Toast.makeText(context,"helloworld"+ArrayCommentCount.size(),Toast.LENGTH_LONG).show();
        ListView lv = convertview.findViewById(R.id.lvbaseforreply);
        TextView tvname = convertview.findViewById(R.id.namedisplay);
        tvname.setText(ArrayPName.get(position));
        TextView tvcomment = convertview.findViewById(R.id.commentdisplay);
        tvcomment.setText(ArrayPCooment.get(position));
        TextView tvcommenton = convertview.findViewById(R.id.commentedon);
        String[] splitedtimeval = ArrayPCommentedOn.get(position).split("\\+");
        tvcommenton.setText(splitedtimeval[0]);
        LinearLayout replylv = convertview.findViewById(R.id.replylv);
        TextInputEditText replycomment = convertview.findViewById(R.id.replycomment);
        sendreply = convertview.findViewById(R.id.sendreply);
        ImageView reply = convertview.findViewById(R.id.reply_disc);
        ImageView like = convertview.findViewById(R.id.likeimg);
        TextView replyTxt = convertview.findViewById(R.id.replynum);
        replyTxt.setText("" + ArrayReplyCount.get(position));
        TextView tvLikednum = convertview.findViewById(R.id.likednum);
        tvLikednum.setText("" + ArrayCommentCount.get(position));
        //like chcking
        if (SameUserVal[position] > 0) {
            like.setImageResource(R.drawable.heart_solid);
            like.setEnabled(false);
        } else {
            like.setImageResource(R.drawable.fi_rr_heart);
            like.setEnabled(true);
        }
        /*if(replyval==1)
        {
            replylv.setVisibility(View.VISIBLE);
            sendreply.setVisibility(View.VISIBLE);
            replycomment.setVisibility(View.VISIBLE);
        }*/
        reply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //replyval=1;
                //Toast.makeText(context.getApplicationContext(), "Reply is Clicked"+position,Toast.LENGTH_SHORT).show();
                if (!(replylv.getVisibility() == View.VISIBLE)) {
                    replylv.setVisibility(View.VISIBLE);
                    sendreply.setVisibility(View.VISIBLE);
                    replycomment.setVisibility(View.VISIBLE);
                    lv.setVisibility(View.VISIBLE);
                    JsonArrayParse(ArrayReplyJson.get(position), lv);
                } else {
                    replylv.setVisibility(View.GONE);
                    sendreply.setVisibility(View.GONE);
                    replycomment.setVisibility(View.GONE);
                    lv.setVisibility(View.GONE);
                }
            }
        });
        like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Toast.makeText(context.getApplicationContext(), "Reply is Clicked",Toast.LENGTH_SHORT).show();
                PostVolleyCommentLike(ArrayCommentID.get(position));

            }
        });
        sendreply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //replycomment.setText("hello");
                if (replycomment.getText().toString().equalsIgnoreCase("")) {
                    Toast.makeText(context, "Please add comment", Toast.LENGTH_LONG).show();
                } else {
                    //Toast.makeText(context,"Please add comment"+ArrayPID.get(position),Toast.LENGTH_LONG).show();
                    PostVolleySendReplyComment(replycomment.getText().toString(), ArrayCommentID.get(position), replycomment);
                }

            }
        });
        return convertview;
    }

    private void PostVolleySendReplyComment(String Comment, int parentid, TextInputEditText replycomment) {
        String url = VariableDecClass.IPAddress + "apiReplyComment";

        // creating a new variable for our request queue
        RequestQueue queue = Volley.newRequestQueue(context);

        // on below line we are calling a string
        // request method to post the data to our API
        // in this we are calling a post method.
        StringRequest request = new StringRequest(Request.Method.POST, url, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                // inside on response method we are
                // hiding our progress bar
                // and setting data to edit text as empty

                // on below line we are displaying a success toast message.
                // Toast.makeText(PlantActivity2.this, "Data Fetched", Toast.LENGTH_SHORT).show();
                try {
                    JSONObject respObj = new JSONObject(response);
                    int success = respObj.getInt("sucess");
                    String status = respObj.getString("status");
                    Toast.makeText(context, "reply:" + status, Toast.LENGTH_LONG).show();
                    replycomment.setText("");

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // method to handle errors.
                //Toast.makeText(context, "Fail to get response = " + error, Toast.LENGTH_SHORT).show();

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
                params.put("user_id", "" + EachEventDetailsShow.LoginInfo);
                params.put("comment", Comment);
                params.put("parent_id", "" + parentid);

                // at last we are
                // returning our params.
                return params;
            }
        };
        // below line is to make
        // a json object request.
        queue.add(request);
    }

    private void PostVolleyCommentLike(int Commentid) {
        String url = VariableDecClass.IPAddress + "apiCommentLike";

        // creating a new variable for our request queue
        RequestQueue queue = Volley.newRequestQueue(context);

        // on below line we are calling a string
        // request method to post the data to our API
        // in this we are calling a post method.
        StringRequest request = new StringRequest(Request.Method.POST, url, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                // inside on response method we are
                // hiding our progress bar
                // and setting data to edit text as empty

                // on below line we are displaying a success toast message.
                // Toast.makeText(PlantActivity2.this, "Data Fetched", Toast.LENGTH_SHORT).show();
                try {
                    JSONObject respObj = new JSONObject(response);
                    int success = respObj.getInt("sucess");
                    String status = respObj.getString("status");
                    Toast.makeText(context, "reply:" + status, Toast.LENGTH_LONG).show();


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // method to handle errors.
                //Toast.makeText(context, "Fail to get response = " + error, Toast.LENGTH_SHORT).show();

            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                // below line we are creating a map for
                // storing our values in key and value pair.
                Map<String, String> params = new HashMap<String, String>();

                // on below line we are passing our key
                // and value pair to our parameters.
                params.put("user_id", "" + EachEventDetailsShow.LoginInfo);
                params.put("comment_id", "" + Commentid);


                // at last we are
                // returning our params.
                return params;
            }
        };
        // below line is to make
        // a json object request.
        queue.add(request);
    }

    public void JsonArrayParse(JSONArray singleArray, ListView lvval) {
        ArrayRPID = new ArrayList<>();
        ArrayReplyComment = new ArrayList<>();
        ArrayReplyCommentedOn = new ArrayList<>();
        ArrayReplyName = new ArrayList<>();
        for (int i = 0; i < singleArray.length(); i++) {
            try {
                int Participant_id = singleArray.getJSONObject(i).getInt("participant_id");
                ArrayRPID.add(Participant_id);
                //Parent_id=singleArray.getJSONObject(i).getInt("participant_id");
                //Toast.makeText(context,"Parent IC"+Parent_id,Toast.LENGTH_LONG).show();
                //ArrayRParentID.add(Parent_id);
                //Toast.makeText(context,"Parent IC"+ArrayRParentID.size(),Toast.LENGTH_LONG).show();
                String commenton = singleArray.getJSONObject(i).getString("commented_on");
                ArrayReplyCommentedOn.add(commenton);
                String comment = singleArray.getJSONObject(i).getString("comment");
                ArrayReplyComment.add(comment);
                //Toast.makeText(context.getApplicationContext(), "calling main"+ArrayReplyComment.size(),Toast.LENGTH_SHORT).show();
                PostVolleyGetReplyName(Participant_id, lvval);
            } catch (Exception e) {

            }
        }

    }

    private void PostVolleyGetReplyName(int id, ListView lvval) {

        String url = VariableDecClass.IPAddress + "apiPerticipantName";
        // creating a new variable for our request queue
        RequestQueue queue = Volley.newRequestQueue(context);
        // on below line we are calling a string
        // request method to post the data to our API
        // in this we are calling a post method.
        StringRequest request = new StringRequest(Request.Method.POST, url, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                // inside on response method we are
                // hiding our progress bar
                // and setting data to edit text as empty

                // on below line we are displaying a success toast message.
                // Toast.makeText(PlantActivity2.this, "Data Fetched", Toast.LENGTH_SHORT).show();
                try {
                    JSONArray resArray = new JSONArray(response);
                    if (resArray.length() > 0) {
                        // Toast.makeText(getActivity(),""+resArray,Toast.LENGTH_LONG).show();
                        for (int j = 0; j < resArray.length(); j++) {
                            String name = resArray.getJSONObject(j).getString("name");
                            ArrayReplyName.add(name);
                            //Toast.makeText(context.getApplicationContext(), "calling"+ArrayReplyName.size(),Toast.LENGTH_SHORT).show();
                            if (j == resArray.length() - 1) {
                                // Toast.makeText(getActivity(),""+participantCommentedOn.size(),Toast.LENGTH_LONG).show();
                                ReplyBaseAdopter baseAdapter = new ReplyBaseAdopter(context, ArrayReplyName, ArrayReplyComment, ArrayReplyCommentedOn);
                                lvval.setAdapter(baseAdapter);
                                setDynamicHeight(lvval);
                            }
                        }
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // method to handle errors.
                //Toast.makeText(context, "Fail to get response = " + error, Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                // below line we are creating a map for
                // storing our values in key and value pair.
                Map<String, String> params = new HashMap<String, String>();

                // on below line we are passing our key
                // and value pair to our parameters.
                params.put("participant_id", "" + id);

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
