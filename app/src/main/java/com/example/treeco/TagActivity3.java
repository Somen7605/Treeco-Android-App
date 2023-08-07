package com.example.treeco;

import android.Manifest;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

public class TagActivity3 extends AppCompatActivity {
    int action_type = -1;

    Button done, Share_social;
    TextView tagged, coins, treename;
    String points = "";
    String LoginID;
    String tree_name = "", Plant_id_fetch = "";
    private FusedLocationProviderClient client;
    private SupportMapFragment mapFragment;
    private int REQUEST_CODE = 111;
    private ProgressBar loadingPB;
    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tag3);
        ActionBar actionBar;
        actionBar = getSupportActionBar();
        // Define ColorDrawable object and parse color
        // using parseColor method
        // with color hash code as its parameter
        ColorDrawable colorDrawable
                = new ColorDrawable(Color.parseColor("#32CB00"));
        // Set BackgroundDrawable
        actionBar.setBackgroundDrawable(colorDrawable);
        actionBar.setTitle(Html.fromHtml("<font color=#ffffff>" + "<small>"
                + "tre" + "</small>" + "" + "<big>" + "e"
                + "</big>" + "" + "<small>" + "co" + "</small>"));
        loadingPB = findViewById(R.id.idLoadingPB);
        recyclerView=findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false));
        recyclerView.addItemDecoration(new SpacesItemDecoration(8 * 2));
        RecyclerViewAdapter recyclerViewAdapter=new RecyclerViewAdapter(getApplicationContext(),TagActivity2.AddImagesGlobal);
        recyclerView.setAdapter(recyclerViewAdapter);
        done = findViewById(R.id.done);
        Share_social = findViewById(R.id.share_social);
        tagged = findViewById(R.id.tagged_succesfully);
        coins = findViewById(R.id.coinsval);
        treename = findViewById(R.id.mango_tree_1);
        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), UserDashboard.class);
                startActivity(i);

            }
        });
        Share_social.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialogPromt();

            }
        });
        SharedPreferences shrPrf;
        shrPrf = getSharedPreferences("MyLoginInfo", Context.MODE_PRIVATE);
        LoginID = shrPrf.getString("login_userid", "");
        postDataUsingVolley1(TagActivity2.IDAfterTaging);
        mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.maps);

        client = LocationServices.getFusedLocationProviderClient(TagActivity3.this);

        if (ActivityCompat.checkSelfPermission(TagActivity3.this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            getCurrentLocation();
        } else {
            ActivityCompat.requestPermissions(TagActivity3.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE);
        }
        ;

    }

    private void getCurrentLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        Task<Location> task = client.getLastLocation();
        task.addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {

                mapFragment.getMapAsync(new OnMapReadyCallback() {
                    @Override
                    public void onMapReady(@NonNull GoogleMap googleMap) {
                        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());

                        MarkerOptions markerOptions = new MarkerOptions().position(latLng).title("You Are Here");

                        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 14));

                        googleMap.addMarker(markerOptions).showInfoWindow();
                    }
                });

            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getCurrentLocation();
            } else {
                Toast.makeText(this, "PERMISSION DENIED!!!", Toast.LENGTH_SHORT).show();
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    private void postDataUsingVolley1(String Logininfo) {
        //Toast.makeText(TagActivity3.this, "Logininfo" +Logininfo, Toast.LENGTH_SHORT).show();
        // url to post our data
        String url = "http://143.110.249.84/api/apiPlantInfo";
        loadingPB.setVisibility(View.VISIBLE);

        // creating a new variable for our request queue
        RequestQueue queue = Volley.newRequestQueue(TagActivity3.this);

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
                // Toast.makeText(RegistrationActivity.this, "Response received", Toast.LENGTH_SHORT).show();
                try {
                    // on below line we are parsing the response
                    // to json object to extract data from it.
                    JSONArray respObj = new JSONArray(response);
                    //Toast.makeText(TagActivity3.this,""+respObj,Toast.LENGTH_LONG).show();
                    Log.d("Json Response:", "" + respObj);
                    for (int i = 0; i < respObj.length(); i++) {
                        JSONObject jo = respObj.getJSONObject(i);
                        points = jo.getString("treeco_points");
                        coins.setText("You Won " + points + " treeco coins");
                        //Toast.makeText(TagActivity3.this,"points:"+points,Toast.LENGTH_LONG).show();
                        JSONObject plant = jo.getJSONObject("plant");
                        Plant_id_fetch = plant.getString("id");
                        //Toast.makeText(TagActivity3.this,"plant:"+plant,Toast.LENGTH_LONG).show();
                        JSONObject plant_type = plant.getJSONObject("plant_type");
                        //Toast.makeText(TagActivity3.this,"plant_type:"+plant_type,Toast.LENGTH_LONG).show();
                        tree_name = plant_type.getString("Common_Name");
                        treename.setText(tree_name);
                        //Toast.makeText(TagActivity3.this,"Common_Name:"+tree_name,Toast.LENGTH_LONG).show();
                        //Setting the values in TEXTVIEW
                        tagged.setText("This " + tree_name + " is tagged successfully");
                        //
                        //
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // method to handle errors.
                //Toast.makeText(TagActivity3.this, "Fail to get response = " + error, Toast.LENGTH_SHORT).show();
                loadingPB.setVisibility(View.GONE);
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
                params.put("id", Logininfo);


                // at last we are
                // returning our params.
                return params;
            }
        };
        // below line is to make
        // a json object request.
      /*  request1.setRetryPolicy(new DefaultRetryPolicy(
                0,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));*/
        queue.add(request1);

    }

    public void alertDialogPromt() {
        new MaterialAlertDialogBuilder(TagActivity3.this, R.style.AlertDialogTheme)
                .setTitle("Location access")
                .setMessage("Treeco uses this to locate, track trees, tag exact location of a tree.")
                .setPositiveButton("ALLOW", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int which) {

                        LinearLayout content_container = findViewById(R.id.lv);
                        TextView tvcoin = findViewById(R.id.cointag4);
                        tvcoin.setText("You Won " + points + " treeco coins");
                        ImageView img = findViewById(R.id.fetch_tree);
                        img.setImageBitmap(TagActivity2.AddImagesGlobal.get(0));
                        img.getLayoutParams().height = 1000;
                        img.getLayoutParams().width = 1000;
                        content_container.setDrawingCacheEnabled(true);
                        content_container.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
                                View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));

                        content_container.layout(0, 0, content_container.getMeasuredWidth(), content_container.getMeasuredHeight());

                        content_container.buildDrawingCache(true);
                        Bitmap b = Bitmap.createBitmap(content_container.getDrawingCache());
                        content_container.setDrawingCacheEnabled(false); // clear drawing cache
                        // Bitmap icon = BitmapFactory.decodeFile(imgFile.getAbsolutePath());

                        Intent share = new Intent(Intent.ACTION_SEND);
                        share.setType("image/jpeg");

                        ContentValues values = new ContentValues();
                        values.put(MediaStore.Images.Media.TITLE, "title");
                        values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg");
                        Uri uri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                                values);
                        OutputStream outstream;
                        try {
                            outstream = getContentResolver().openOutputStream(uri);
                            b.compress(Bitmap.CompressFormat.JPEG, 100, outstream);
                            outstream.close();
                        } catch (Exception e) {
                            System.err.println(e.toString());
                        }
                        share.putExtra(Intent.EXTRA_STREAM, uri);
                        share.putExtra(Intent.EXTRA_TEXT, "http://www.google.com/maps/place/" + TagActivity2.latval + "," + TagActivity2.lngval + "/@" + TagActivity2.latval + "," + TagActivity2.lngval + ",17z");

                        //share.setPackage("com.facebook.katana");
                        //startActivity(Intent.createChooser(share, "Share Image"));
                        //share.setPackage("com.whatsapp");
                        //startActivity(Intent.createChooser(share, "Share Image"));
                        //share.setPackage("com.instagram.android");
                        startActivity(Intent.createChooser(share, "Share Image"));
                        //postDataUsingVolley();
                        action_type = 1;
                        //share.setPackage("com.linkedin.android");

                        //done.setVisibility(View.VISIBLE);
                        //Share_social.setVisibility(View.VISIBLE);
                        //setContentView(R.layout.activity_tag3);


                    }
                })
                .setNegativeButton("DON'T ALLOW", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int which) {

                        //setContentView(R.layout.activity_tag4);
                        LinearLayout content_container = findViewById(R.id.lv);
                        TextView tvcoin = findViewById(R.id.cointag4);
                        tvcoin.setText("You Won " + points + " treeco coins");
                        ImageView img = findViewById(R.id.fetch_tree);
                        img.setImageBitmap(TagActivity2.AddImagesGlobal.get(0));
                        img.getLayoutParams().height = 1000;
                        img.getLayoutParams().width = 1000;
                        content_container.setDrawingCacheEnabled(true);
                        content_container.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
                                View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));

                        content_container.layout(0, 0, content_container.getMeasuredWidth(), content_container.getMeasuredHeight());

                        content_container.buildDrawingCache(true);
                        Bitmap b = Bitmap.createBitmap(content_container.getDrawingCache());
                        content_container.setDrawingCacheEnabled(false); // clear drawing cache
                        // Bitmap icon = BitmapFactory.decodeFile(imgFile.getAbsolutePath());

                        Intent share = new Intent(Intent.ACTION_SEND);
                        share.setType("image/jpeg");
                        ContentValues values = new ContentValues();
                        values.put(MediaStore.Images.Media.TITLE, "title");
                        values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg");
                        Uri uri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                                values);
                        OutputStream outstream;
                        try {
                            outstream = getContentResolver().openOutputStream(uri);
                            b.compress(Bitmap.CompressFormat.JPEG, 100, outstream);
                            outstream.close();
                        } catch (Exception e) {
                            System.err.println(e.toString());
                        }
                        share.putExtra(Intent.EXTRA_STREAM, uri);
                        //share.putExtra(Intent.EXTRA_TEXT, "http://www.google.com/maps/place/"+TagActivity2.latval+","+TagActivity2.lngval+"/@"+TagActivity2.latval+","+TagActivity2.lngval+",17z");

                        //share.setPackage("com.facebook.katana");
                        //startActivity(Intent.createChooser(share, "Share Image"));
                        //share.setPackage("com.whatsapp");
                        //startActivity(Intent.createChooser(share, "Share Image"));
                        //share.setPackage("com.instagram.android");
                        startActivity(Intent.createChooser(share, "Share Image"));
                        action_type = 1;
                        //postDataUsingVolley();
                        //share.setPackage("com.linkedin.android");

                        //done.setVisibility(View.VISIBLE);
                        //Share_social.setVisibility(View.VISIBLE);
                        //setContentView(R.layout.activity_tag3);


                    }
                })
                .show();







        /*AlertDialog.Builder al=new AlertDialog.Builder(this);
        al.setTitle("Location Share Alert");
        al.setMessage("Do you want to share your tagged location in social media????");
        al.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //setContentView(R.layout.activity_tag4);
                LinearLayout content_container=findViewById(R.id.lv);
                TextView tvcoin=findViewById(R.id.cointag4);
                tvcoin.setText("You Won "+points+" treeco coins");
                ImageView img=findViewById(R.id.fetch_tree);
                img.setImageBitmap(TagActivity2.AddImagesGlobal.get(0));
                img.getLayoutParams().height =1000;
                img.getLayoutParams().width = 1000;
                content_container.setDrawingCacheEnabled(true);
                content_container.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
                        View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));

                content_container.layout(0, 0, content_container.getMeasuredWidth(), content_container.getMeasuredHeight());

                content_container.buildDrawingCache(true);
                Bitmap b = Bitmap.createBitmap(content_container.getDrawingCache());
                content_container.setDrawingCacheEnabled(false); // clear drawing cache
                // Bitmap icon = BitmapFactory.decodeFile(imgFile.getAbsolutePath());

                Intent share = new Intent(Intent.ACTION_SEND);
                share.setType("image/jpeg");

                ContentValues values = new ContentValues();
                values.put(MediaStore.Images.Media.TITLE, "title");
                values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg");
                Uri uri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                        values);
                OutputStream outstream;
                try {
                    outstream = getContentResolver().openOutputStream(uri);
                    b.compress(Bitmap.CompressFormat.JPEG, 100, outstream);
                    outstream.close();
                } catch (Exception e) {
                    System.err.println(e.toString());
                }
                share.putExtra(Intent.EXTRA_STREAM, uri);
                share.putExtra(Intent.EXTRA_TEXT, "http://www.google.com/maps/place/"+TagActivity2.latval+","+TagActivity2.lngval+"/@"+TagActivity2.latval+","+TagActivity2.lngval+",17z");

                //share.setPackage("com.facebook.katana");
                //startActivity(Intent.createChooser(share, "Share Image"));
                //share.setPackage("com.whatsapp");
                //startActivity(Intent.createChooser(share, "Share Image"));
                //share.setPackage("com.instagram.android");
                startActivity(Intent.createChooser(share, "Share Image"));
                //postDataUsingVolley();
                action_type=1;
                //share.setPackage("com.linkedin.android");

                //done.setVisibility(View.VISIBLE);
                //Share_social.setVisibility(View.VISIBLE);
                //setContentView(R.layout.activity_tag3);


            }
        });
        al.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //setContentView(R.layout.activity_tag4);
                LinearLayout content_container=findViewById(R.id.lv);
                TextView tvcoin=findViewById(R.id.cointag4);
                tvcoin.setText("You Won "+points+" treeco coins");
                ImageView img=findViewById(R.id.fetch_tree);
                img.setImageBitmap(TagActivity2.AddImagesGlobal.get(0));
                img.getLayoutParams().height =1000;
                img.getLayoutParams().width = 1000;
                content_container.setDrawingCacheEnabled(true);
                content_container.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
                        View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));

                content_container.layout(0, 0, content_container.getMeasuredWidth(), content_container.getMeasuredHeight());

                content_container.buildDrawingCache(true);
                Bitmap b = Bitmap.createBitmap(content_container.getDrawingCache());
                content_container.setDrawingCacheEnabled(false); // clear drawing cache
                // Bitmap icon = BitmapFactory.decodeFile(imgFile.getAbsolutePath());

                Intent share = new Intent(Intent.ACTION_SEND);
                share.setType("image/jpeg");
                ContentValues values = new ContentValues();
                values.put(MediaStore.Images.Media.TITLE, "title");
                values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg");
                Uri uri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                        values);
                OutputStream outstream;
                try {
                    outstream = getContentResolver().openOutputStream(uri);
                    b.compress(Bitmap.CompressFormat.JPEG, 100, outstream);
                    outstream.close();
                } catch (Exception e) {
                    System.err.println(e.toString());
                }
                share.putExtra(Intent.EXTRA_STREAM, uri);
                //share.putExtra(Intent.EXTRA_TEXT, "http://www.google.com/maps/place/"+TagActivity2.latval+","+TagActivity2.lngval+"/@"+TagActivity2.latval+","+TagActivity2.lngval+",17z");

                //share.setPackage("com.facebook.katana");
                //startActivity(Intent.createChooser(share, "Share Image"));
                //share.setPackage("com.whatsapp");
                //startActivity(Intent.createChooser(share, "Share Image"));
                //share.setPackage("com.instagram.android");
                startActivity(Intent.createChooser(share, "Share Image"));
                action_type=1;
                //postDataUsingVolley();
                //share.setPackage("com.linkedin.android");

                //done.setVisibility(View.VISIBLE);
                //Share_social.setVisibility(View.VISIBLE);
                //setContentView(R.layout.activity_tag3);


            }
        });
        al.create();
        al.show();*/
    }

    private void postDataUsingVolley() {
        // url to post our data
        String url = VariableDecClass.IPAddress + "apiSharePlant";
        loadingPB.setVisibility(View.VISIBLE);

        // creating a new variable for our request queue
        RequestQueue queue = Volley.newRequestQueue(TagActivity3.this);

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
                    String IDRetrive = respObj.getString("id");
                    String Statusretrive = respObj.getString("status");
                    //Toast.makeText(TagActivity3.this, Statusretrive, Toast.LENGTH_SHORT).show();


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // method to handle errors.
                //Toast.makeText(TagActivity3.this, "Fail to get response = " + error, Toast.LENGTH_SHORT).show();
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
                params.put("type", tree_name);
                params.put("Shared_by", LoginID);
                params.put("plant_id", Plant_id_fetch);
                params.put("Shared_through", "Social Media");

                // at last we are
                // returning our params.
                return params;
            }
        };
        // below line is to make
        // a json object request.
        queue.add(request);

    }

    @Override
    protected void onResume() {
        super.onResume();
        if (action_type == 1) {
            action_type = -1;
            postDataUsingVolley();
        }
    }

    public void onBackPressed() {
        // super.onBackPressed();
    }


}