package com.example.treeco;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.airbnb.lottie.model.Marker;
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
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class NearByNursery extends AppCompatActivity {
    SupportMapFragment supportMapFragment;
    FusedLocationProviderClient client;
    GoogleMap mMap;
    private int REQUEST_CODE = 111;
    private double lat, lng;
    private List<Marker> mNurseryMarkers = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_near_by_nursery);
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

        supportMapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        client = LocationServices.getFusedLocationProviderClient(this);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            getCurrentLocation();

        } else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE);
        }
    }

    private void getCurrentLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }

        Task<Location> task = client.getLastLocation();
        task.addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {

                if (location != null) {
                    lat = location.getLatitude();
                    lng = location.getLongitude();
                    supportMapFragment.getMapAsync(new OnMapReadyCallback() {
                        @Override
                        public void onMapReady(GoogleMap googleMap) {
                            mMap = googleMap;
                            LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
                            MarkerOptions markerOptions = new MarkerOptions().position(latLng).title("you").icon(bitmapDescriptorFromVector(NearByNursery.this, R.drawable.location_blue));
                            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(lat, lng), 10));
                            mMap.addMarker(markerOptions).showInfoWindow();
                            getNearByNurseries(latLng);

                            mMap.getUiSettings().setZoomControlsEnabled(true);
                            mMap.getUiSettings().setCompassEnabled(true);
                            mMap.getUiSettings().setMyLocationButtonEnabled(true);
                            mMap.getUiSettings().setIndoorLevelPickerEnabled(true);
                            mMap.getUiSettings().setMapToolbarEnabled(true);
                        }
                    });
                }

            }
        });
        LocationRequest locationRequest = LocationRequest.create();
        locationRequest.setInterval(6000);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setFastestInterval(5000);

        LocationCallback locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                //Toast.makeText(NearByNursery.this, ""+locationResult, Toast.LENGTH_SHORT).show();
                if (locationResult == null) {
                    //Toast.makeText(NearByNursery.this, "Current Location is null", Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    for (Location location : locationResult.getLocations()) {
                        if (location != null) {
                            //Toast.makeText(NearByNursery.this, "Position Changed", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            }
        };
        client.requestLocationUpdates(locationRequest, locationCallback, null);
    }

    private void getNearByNurseries(LatLng latLng) {

        Log.d("LatLng:", "" + latLng);

        String apiKey = getString(R.string.apikey);

        String type = "plant%2Cnursery";

        int radius = 100;

        String location = latLng.latitude + "," + latLng.longitude;

        String url = "https://maps.googleapis.com/maps/api/place/textsearch/json?input=" + type +
                "&location=" + location +
                "&radius=" + radius +
                "&inputtype=textquery&fields=formatted_address%2Cname%2Crating%2Copening_hours%2Cgeometry" +
                "&key=" + apiKey;
        Log.d("Url:", "" + url);


        RequestQueue queue = Volley.newRequestQueue(this);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                JSONArray results = null;
                try {
                    results = response.getJSONArray("results");
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
                LatLngBounds.Builder b = new LatLngBounds.Builder();

                for (int i = 0; i < results.length(); i++) {
                    JSONObject result = null;
                    try {
                        result = results.getJSONObject(i);
                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }


                    JSONObject geometry = null;
                    try {
                        geometry = result.getJSONObject("geometry");
                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }
                    JSONObject location = null;
                    try {
                        location = geometry.getJSONObject("location");
                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }

                    double lat = 0;
                    try {
                        lat = location.getDouble("lat");
                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }
                    double lng = 0;
                    try {
                        lng = location.getDouble("lng");
                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }
                    String name = null;
                    try {
                        name = result.getString("name");
                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }

                    //Log.d("nurseryName:",name);

                    LatLng latLng2 = new LatLng(lat, lng);
                    MarkerOptions markerOptions = new MarkerOptions().position(latLng2).title(name).icon(bitmapDescriptorFromVector(NearByNursery.this, R.drawable.location_orange));
                    //mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(lat,lng),15));
                    mMap.addMarker(markerOptions).showInfoWindow();
                    b.include(markerOptions.getPosition());

                    if(i== results.length()-1)
                    {
                        LatLngBounds bounds = b.build();
                        int width = getResources().getDisplayMetrics().widthPixels;
                        int height = getResources().getDisplayMetrics().heightPixels;
                        int padding = (int) (width * 0.10); // offset from edges of the map 10% of screen
                        //Change the padding as per needed
                        CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds,width,height,padding);
                        mMap.animateCamera(cu);
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //Toast.makeText(NearByNursery.this, "" + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        queue.add(jsonObjectRequest);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getCurrentLocation();
            } else {
                Toast.makeText(NearByNursery.this, "PERMISSION DENIED!!!", Toast.LENGTH_SHORT).show();
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
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