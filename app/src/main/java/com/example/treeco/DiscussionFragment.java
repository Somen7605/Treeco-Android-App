package com.example.treeco;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

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


public class DiscussionFragment extends Fragment {
    ListView listViewdisplay;
    TextInputEditText maincomment;
    ImageView sendbtn;
    ProgressBar LoadingPB;
    ArrayList<Integer> participantID, CommentID, replyCount, CommentCount;
    Integer[] LikedUserStatus;
    ArrayList<String> participantName, participantComment, participantCommentedOn;
    ArrayList<JSONArray> ReplyJson;

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
        View fragmentdisc = inflater.inflate(R.layout.fragment_disc_fragment, container, false);
        sendbtn = fragmentdisc.findViewById(R.id.maincommentsend1);
        maincomment = fragmentdisc.findViewById(R.id.maininputedittext1);
        LoadingPB = fragmentdisc.findViewById(R.id.idLoadingPB);
        listViewdisplay = fragmentdisc.findViewById(R.id.listdiscussion);
        PostVolleyGetComment();
        sendbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String Comment = maincomment.getText().toString();
                if (Comment.equalsIgnoreCase("")) {
                    Toast.makeText(getActivity(), "Please add comment", Toast.LENGTH_LONG).show();
                } else {
                    PostVolleySendComment(Comment);
                }

            }
        });

        return fragmentdisc;
    }

    private void PostVolleySendComment(String Comment) {
        String url = VariableDecClass.IPAddress + "apiAddComment";
        LoadingPB.setVisibility(View.VISIBLE);

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
                LoadingPB.setVisibility(View.GONE);

                // on below line we are displaying a success toast message.
                // Toast.makeText(PlantActivity2.this, "Data Fetched", Toast.LENGTH_SHORT).show();
                try {
                    JSONObject respObj = new JSONObject(response);
                    int success = respObj.getInt("sucess");
                    String status = respObj.getString("status");
                    Toast.makeText(getActivity(), "" + status, Toast.LENGTH_LONG).show();
                    maincomment.setText("");
                    PostVolleyGetComment();

                } catch (JSONException e) {
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
                params.put("user_id", "" + EachEventDetailsShow.LoginInfo);
                params.put("comment", Comment);

                // at last we are
                // returning our params.
                return params;
            }
        };
        // below line is to make
        // a json object request.
        queue.add(request);
    }

    private void PostVolleyGetComment() {
        String url = VariableDecClass.IPAddress + "apiGetComment";
        LoadingPB.setVisibility(View.VISIBLE);
        ReplyJson = new ArrayList<JSONArray>();
        replyCount = new ArrayList<>();
        CommentCount = new ArrayList<>();
        participantComment = new ArrayList<>();
        participantCommentedOn = new ArrayList<>();
        participantID = new ArrayList<>();
        CommentID = new ArrayList<>();
        participantName = new ArrayList<>();
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
                LoadingPB.setVisibility(View.GONE);

                // on below line we are displaying a success toast message.
                // Toast.makeText(PlantActivity2.this, "Data Fetched", Toast.LENGTH_SHORT).show();
                try {
                    JSONArray resArray = new JSONArray(response);
                    //Toast.makeText(getActivity(), "Data Fetched"+resArray.length(), Toast.LENGTH_SHORT).show();
                    if (resArray.length() > 0) {
                        LikedUserStatus = new Integer[resArray.length()];
                        for (int i = 0; i < resArray.length(); i++) {
                            LikedUserStatus[i] = 0;
                            String Parent_id = resArray.getJSONObject(i).getString("parent_id");
                            if (Parent_id.equalsIgnoreCase("null")) {
                                int Comment_id = resArray.getJSONObject(i).getInt("id");
                                CommentID.add(Comment_id);

                                //Toast.makeText(getActivity(),"comment array"+resArray.length(),Toast.LENGTH_LONG).show();
                                int Participant_id = resArray.getJSONObject(i).getInt("participant_id");
                                participantID.add(Participant_id);
                                String commenton = resArray.getJSONObject(i).getString("commented_on");
                                participantCommentedOn.add(commenton);
                                String comment = resArray.getJSONObject(i).getString("comment");
                                participantComment.add(comment);
                                JSONArray reply = resArray.getJSONObject(i).getJSONArray("replies");
                                ReplyJson.add(reply);
                                replyCount.add(reply.length());
                                PostVolleyGetCommentlikes(Comment_id, i, Participant_id);

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
        queue.add(request);
    }

    private void PostVolleyGetCommentlikes(int commentid, int positionval, int Participant_id) {

        String url = VariableDecClass.IPAddress + "apiCommentLikes";
        LoadingPB.setVisibility(View.VISIBLE);
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
                LoadingPB.setVisibility(View.GONE);

                // on below line we are displaying a success toast message.
                // Toast.makeText(PlantActivity2.this, "Data Fetched", Toast.LENGTH_SHORT).show();
                try {
                    JSONArray resArray1 = new JSONArray(response);
                    CommentCount.add(resArray1.length());
                    //Toast.makeText(getActivity(),"comment array"+resArray1.length(),Toast.LENGTH_LONG).show();
                    if (resArray1.length() > 0) {
                        ArrayList<Integer> ArrayLikedinEachComment = new ArrayList<>();
                        for (int k = 0; k < resArray1.length(); k++) {
                            int liked_by = resArray1.getJSONObject(k).getInt("liked_by");
                            ArrayLikedinEachComment.add(liked_by);
                            if (ArrayLikedinEachComment.contains(EachEventDetailsShow.LoginInfo)) {
                                LikedUserStatus[positionval] = 1;
                            }
                        }
                        PostVolleyGetName(Participant_id);
                    } else {
                        PostVolleyGetName(Participant_id);
                    }


                } catch (JSONException e) {
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
                params.put("comment_id", "" + commentid);

                // at last we are
                // returning our params.
                return params;
            }
        };
        // below line is to make
        // a json object request.
        queue.add(request);
    }

    private void PostVolleyGetName(int id) {
        String url = VariableDecClass.IPAddress + "apiPerticipantName";
        LoadingPB.setVisibility(View.VISIBLE);
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
                LoadingPB.setVisibility(View.GONE);

                // on below line we are displaying a success toast message.
                // Toast.makeText(PlantActivity2.this, "Data Fetched", Toast.LENGTH_SHORT).show();
                try {
                    JSONArray resArray = new JSONArray(response);
                    //Toast.makeText(getActivity(), "Nm" + resArray.length(), Toast.LENGTH_SHORT).show();
                    if (resArray.length() > 0) {
                        // Toast.makeText(getActivity(),""+resArray,Toast.LENGTH_LONG).show();
                        for (int j = 0; j < resArray.length(); j++) {
                            String name = resArray.getJSONObject(j).getString("name");
                            participantName.add(name);
                            if (j == resArray.length() - 1) {
                                // Toast.makeText(getActivity(),""+participantCommentedOn.size(),Toast.LENGTH_LONG).show();
                                BaseAdapterDisplayComment baseAdapter2 = new BaseAdapterDisplayComment(getContext(), CommentID, participantID, participantName, participantComment, participantCommentedOn, replyCount, ReplyJson, CommentCount, LikedUserStatus);
                                listViewdisplay.setAdapter(baseAdapter2);
                                setDynamicHeight(listViewdisplay);
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