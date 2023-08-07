package com.example.treeco;

import android.Manifest;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class TreeMapFragment1 extends Fragment {
    private FusedLocationProviderClient client;
    private SupportMapFragment mapFragment;
    private int REQUEST_CODE = 111;
    private String latitude = "", longitude = "", is_planted, commonName, tagNameRetrive;
    private ArrayList<Double> lat_list;
    private ArrayList<Double> long_list;
    private ArrayList<String> is_planted_Array;
    private ArrayList<String> Common_name_Array;
    private ArrayList<String> TagNameArray;
    private String user_id = "";
    private GoogleMap mMap;


    public TreeMapFragment1() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        //Intitialize arraylist
        lat_list = new ArrayList<>();
        long_list = new ArrayList<>();
        Common_name_Array = new ArrayList<>();
        is_planted_Array = new ArrayList<>();
        TagNameArray = new ArrayList<>();
        SharedPreferences shrp;
        shrp = getActivity().getSharedPreferences("MyLoginInfo", Context.MODE_PRIVATE);
        user_id = shrp.getString("login_userid", "");
        //Toast.makeText(getActivity(), "" + user_id.toString(), Toast.LENGTH_SHORT).show();
        postDataUsingVolley(user_id);

        //setting the fragment
        View v = inflater.inflate(R.layout.fragment_tree_map1, container, false);
        mapFragment = (SupportMapFragment) this.getChildFragmentManager().findFragmentById(R.id.maps2);

        return v;
    }

    private void getCurrentLocation() {
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        Task<Location> task = client.getLastLocation();
        task.addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {

                mapFragment.getMapAsync(new OnMapReadyCallback() {
                    @Override
                    public void onMapReady(GoogleMap googleMap) {

                        //Double t1=lat_list.get(0);
                        //Double t2=long_list.get(0);
                        LatLngBounds.Builder b = new LatLngBounds.Builder();
                        for (int i = 0; i < lat_list.size(); i++) {
                            Double t1 = lat_list.get(i);
                            Double t2 = long_list.get(i);
                            String plantedstatus = is_planted_Array.get(i);
                            String tagname = TagNameArray.get(i);
                            String commonname = Common_name_Array.get(i);
                            String NametoShow = tagname + "(" + commonname + ")";
                            //Toast.makeText(getActivity(),"Latitude:"+t1+" Longitude:"+t2,Toast.LENGTH_LONG).show();


                            LatLng latLng = new LatLng(t1, t2);
                            if (plantedstatus.equalsIgnoreCase("null")) {
                                //Toast.makeText(getActivity(),"Imside tagged",Toast.LENGTH_LONG).show();
                                MarkerOptions markerOptions = new MarkerOptions().position(latLng).title(NametoShow).icon(bitmapDescriptorFromVector(getContext(), R.drawable.location_blue));
                                //googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 14));
                                googleMap.addMarker(markerOptions).showInfoWindow();
                                b.include(markerOptions.getPosition());
                            } else if (plantedstatus.equalsIgnoreCase("true")) {
                                //Toast.makeText(getActivity(),"Inside planted",Toast.LENGTH_LONG).show();
                                MarkerOptions markerOptions = new MarkerOptions().position(latLng).title(NametoShow).icon(bitmapDescriptorFromVector(getContext(), R.drawable.location_orange));
                                //googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 14));
                                googleMap.addMarker(markerOptions).showInfoWindow();
                                b.include(markerOptions.getPosition());
                            } else if (plantedstatus.equalsIgnoreCase("false")) {
                                //Toast.makeText(getActivity(),"Inside tagged",Toast.LENGTH_LONG).show();
                                MarkerOptions markerOptions = new MarkerOptions().position(latLng).title(NametoShow).icon(bitmapDescriptorFromVector(getContext(), R.drawable.location_blue));
                                //googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 14));
                                googleMap.addMarker(markerOptions).showInfoWindow();
                                b.include(markerOptions.getPosition());
                            }
                            if(i== lat_list.size()-1)
                            {
                                LatLngBounds bounds = b.build();
                                int width = getResources().getDisplayMetrics().widthPixels;
                                int height = getResources().getDisplayMetrics().heightPixels;
                                int padding = (int) (width * 0.10); // offset from edges of the map 10% of screen
                                //Change the padding as per needed
                                CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds,width,height,padding);
                                googleMap.animateCamera(cu);
                            }
                        }


                        googleMap.getUiSettings().setZoomControlsEnabled(true);
                        googleMap.getUiSettings().setCompassEnabled(true);
                        googleMap.getUiSettings().setMyLocationButtonEnabled(true);
                        googleMap.getUiSettings().setIndoorLevelPickerEnabled(true);
                        googleMap.getUiSettings().setMapToolbarEnabled(true);
                        googleMap.setPadding(0,0,0,120);
                    }
                });
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getCurrentLocation();
            } else {
                Toast.makeText(getActivity(), "PERMISSION DENIED!!!", Toast.LENGTH_SHORT).show();
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    private void postDataUsingVolley(String user_id) {
        //Toast.makeText(TagActivity3.this, "Logininfo" +Logininfo, Toast.LENGTH_SHORT).show();
        // url to post our data
        String url = VariableDecClass.IPAddress + "apiMyPlants";


        // creating a new variable for our request queue
        RequestQueue queue = Volley.newRequestQueue(getActivity());

        // on below line we are calling a string
        // request method to post the data to our API
        // in this we are calling a post method.
        StringRequest request1 = new StringRequest(Request.Method.POST, url, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                // inside on response method we are
                // hiding our progress bar
                // and setting data to edit text as empty

                String obj1;
                // on below line we are displaying a success toast message.
                //Toast.makeText(getActivity(), "Response received"+response, Toast.LENGTH_SHORT).show();
                try {
                    // on below line we are parsing the response
                    // to json object to extract data from it.
                    JSONArray respObj = new JSONArray(response);
                    //Toast.makeText(getActivity(),""+respObj,Toast.LENGTH_LONG).show();
                    //Log.d("Json Response:",""+respObj);
                    for (int i = 0; i < respObj.length(); i++) {
                        JSONObject jo = respObj.getJSONObject(i);
                        //Toast.makeText(getActivity(),"Object"+jo,Toast.LENGTH_LONG).show();
                        latitude = jo.getString("latitude");
                        longitude = jo.getString("longitude");
                        is_planted = jo.getString("is_planted");
                        commonName = jo.getString("Common_Name");
                        tagNameRetrive = jo.getString("tag_name");
                        Common_name_Array.add(commonName);
                        TagNameArray.add(tagNameRetrive);
                        Double d1 = Double.parseDouble(latitude);
                        Double d2 = Double.parseDouble(longitude);
                        lat_list.add(d1);
                        long_list.add(d2);
                        is_planted_Array.add(is_planted);
                    }
                    client = LocationServices.getFusedLocationProviderClient(getActivity());
                    //Toast.makeText(getActivity(), "" + user_id.toString(), Toast.LENGTH_SHORT).show();
                    if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION)
                            == PackageManager.PERMISSION_GRANTED) {
                        getCurrentLocation();
                    } else {
                        ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE);
                    }
                    //Toast.makeText(getActivity(),"Latitude:"+lat_list+" Longitude:"+long_list,Toast.LENGTH_LONG).show();
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

                // Intent i = new Intent(RegistrationActivity.this,RegistrationFailed.class);
                //Intent is used to switch from one activity to another.
                //startActivity(i);
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                // below line we are creating a map for
                // storing our values in key and value pair.
                Map<String, String> params = new HashMap<String, String>();
                // on below line we are passing our key
                // and value pair to our parameters.
                params.put("user_id", user_id);

                // at last we are
                // returning our params.
                return params;
            }
        };
        // below line is to make
        // a json object request.
        queue.add(request1);

    }

    private BitmapDescriptor bitmapDescriptorFromVector(Context context, int vectorResId) {
        Drawable vectorDrawable = ContextCompat.getDrawable(context, vectorResId);
        vectorDrawable.setBounds(0, 0, vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight());
        Bitmap bitmap = Bitmap.createBitmap(vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        vectorDrawable.draw(canvas);
        return BitmapDescriptorFactory.fromBitmap(bitmap);
    }


}
