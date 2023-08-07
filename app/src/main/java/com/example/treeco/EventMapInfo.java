package com.example.treeco;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.os.Bundle;
import android.os.Looper;
import android.text.Html;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polygon;
import com.google.android.gms.maps.model.PolygonOptions;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class EventMapInfo extends AppCompatActivity {
    public static double latval;
    public static double lngval;
    public static double altitute;
    ExtendedFloatingActionButton add, next, done;
    ArrayList<Double> StoreLat;
    ArrayList<Double> StoreLong;
    ArrayList<Double> StoreAlt;
    LocationRequest locationRequest;
    Marker marker;
    int pointval = 1;
    int PlantIDfromCreateEvent;
    private SupportMapFragment mapFragment;
    private int REQUEST_CODE = 111;
    private FusedLocationProviderClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_map_info);
        //IntentValue accessing
        Intent ival = getIntent();
        PlantIDfromCreateEvent = ival.getIntExtra("plantid", 0);
        //add resource content
        add = findViewById(R.id.addbtn);
        next = findViewById(R.id.backbtn);
        done = findViewById(R.id.donebtn);
        // Define ActionBar object
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
        StoreAlt = new ArrayList<>();
        StoreLat = new ArrayList<>();
        StoreLong = new ArrayList<>();
        //Instantiating the Location request and setting the priority and the interval I need to update the location.
        locationRequest = locationRequest.create();
        locationRequest.setInterval(100);
        locationRequest.setFastestInterval(50);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.maps2);
        client = LocationServices.getFusedLocationProviderClient(EventMapInfo.this);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(EventMapInfo.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            ActivityCompat.requestPermissions(EventMapInfo.this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, 2);
        }
        //instantiating the LocationCallBack
        LocationCallback locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                if (locationResult != null) {
                    if (locationResult == null) {
                        return;
                    }
                    //Showing the latitude, longitude and accuracy on the home screen.
                    for (Location location : locationResult.getLocations()) {
                        latval = location.getLatitude();
                        lngval = location.getLongitude();
                        altitute = location.getAltitude();
                        mapFragment.getMapAsync(new OnMapReadyCallback() {
                            @Override
                            public void onMapReady(@NonNull GoogleMap googleMap) {
                                LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
                                latval = location.getLatitude();
                                lngval = location.getLongitude();
                                altitute = location.getAltitude();
                                if (marker != null) {
                                    marker.remove();
                                }
                                marker = googleMap.addMarker(new MarkerOptions()
                                        .position(
                                                new LatLng(latval,
                                                        lngval))
                                        .title("You are here")
                                        .icon(bitmapDescriptorFromVector(getApplicationContext(), R.drawable.ic_baseline_location_on_24))
                                        .draggable(true).visible(true));
                                //markerOptions = new MarkerOptions().position(latLng).title("You Are Here");
                                googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 16));
                                add.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        StoreLat.add(latval);
                                        StoreLong.add(lngval);
                                        StoreAlt.add(altitute);
                                        //Toast.makeText(MainActivity.this, "Clicked location Latitude:" + latval+"Longitude:"+lngval+"added to array:size is"+StoreLat.size(), Toast.LENGTH_SHORT).show();
                                        MarkerOptions markerOptions = new MarkerOptions().position(latLng).title("Point" + pointval).icon(bitmapDescriptorFromVector(getApplicationContext(), R.drawable.addlocation));
                                        googleMap.addMarker(markerOptions).showInfoWindow();
                                        pointval++;
                                    }
                                });
                                done.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        DrawPolygonFunction(googleMap, StoreLat, StoreLong);
                                    }
                                });

                            }
                        });
                    }
                }
            }
        };
        client.requestLocationUpdates(locationRequest, locationCallback, Looper.getMainLooper());
        //go back to approval page
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addLocationVolley();
                Intent i = new Intent(EventMapInfo.this, CreateEvent.class);
                startActivity(i);
                finish();

            }
        });
    }

    public void DrawPolygonFunction(GoogleMap googlemap, ArrayList<Double> lat, ArrayList<Double> lng) {

        if (lat.size() != 0) {
            ArrayList<LatLng> listlastlng = new ArrayList<LatLng>();
            for (int i = 0; i < lat.size(); i++) {
                LatLng latLng = new LatLng(lat.get(i), lng.get(i));
                listlastlng.add(latLng);

            }
            PolygonOptions rectOptions = new PolygonOptions().addAll(listlastlng).strokeColor(Color.BLUE).fillColor(Color.CYAN).strokeWidth(7);
            Polygon polygon = googlemap.addPolygon(rectOptions);
        }


    }

    private BitmapDescriptor bitmapDescriptorFromVector(Context context, int vectorResId) {
        Drawable vectorDrawable = ContextCompat.getDrawable(context, vectorResId);
        vectorDrawable.setBounds(0, 0, vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight());
        Bitmap bitmap = Bitmap.createBitmap(vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        vectorDrawable.draw(canvas);
        return BitmapDescriptorFactory.fromBitmap(bitmap);
    }

    private void addLocationVolley() {
        String url = VariableDecClass.IPAddress + "apiAddGeoTag";
        ArrayList<Double> latGroup = new ArrayList<>();
        ArrayList<Double> longGroup = new ArrayList<>();
        ArrayList<Double> altGroup = new ArrayList<>();
        latGroup.add(17.0258);
        latGroup.add(17.5858);
        longGroup.add(95.25896);
        longGroup.add(95.3698);
        altGroup.add(0.32569);
        altGroup.add(0.42569);
        Intent intent = getIntent();
        String plantId = intent.getStringExtra("plantid");
        String eventId = intent.getStringExtra("eventid");
        int createdBy = intent.getIntExtra("created_by", 0);

        RequestQueue queue = Volley.newRequestQueue(EventMapInfo.this);
        try {
            JSONObject params = new JSONObject();
            params.put("plantation_id", plantId);
            params.put("created_by", createdBy);
            JSONArray geotagsArray = new JSONArray();
            for (int i = 0; i < latGroup.size(); i++) {
                JSONObject geotagsObject = new JSONObject();
                geotagsObject.put("latitude", latGroup.get(i));
                geotagsObject.put("longitude", longGroup.get(i));
                geotagsObject.put("altitude", altGroup.get(i));
                geotagsArray.put(geotagsObject);
            }
            params.put("geo_tags", geotagsArray);
            //Toast.makeText(this, ""+params, Toast.LENGTH_SHORT).show();

            JsonObjectRequest request1 = new JsonObjectRequest(Request.Method.POST, url, params, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {

                    try {
                        String Statusretrive = response.getString("status");
                        String success = response.getString("sucess");
                        //Toast.makeText(EventMapInfo.this, "Status:" + Statusretrive + "  Success:" + success, Toast.LENGTH_LONG).show();

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                    //Toast.makeText(EventMapInfo.this, "Fail to Add Map Details = " + error, Toast.LENGTH_SHORT).show();
                }
            });
            queue.add(request1);

        } catch (JSONException e) {
            //Toast.makeText(EventMapInfo.this, "Unable to create json" + e, Toast.LENGTH_SHORT).show();
        }

    }

}