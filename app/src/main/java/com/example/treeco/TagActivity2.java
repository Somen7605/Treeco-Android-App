package com.example.treeco;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Location;
import android.os.Bundle;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

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
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class TagActivity2 extends AppCompatActivity {
    public static double latval;
    public static double lngval;
    public static ArrayList<Bitmap> AddImagesGlobal;
    public static String IDAfterTaging = "";
    ArrayList<Integer> TreefetchingID, ShortStoryID;
    ArrayList<String> Treefetchingname, ShortStorydetails;
    AutoCompleteTextView autoCompleteTextView, autoCompleteTextView1;
    Button tagbtn;
    double altitute;
    CheckBox checkid;
    String SelectedShortStoryval = "", EnteredTreeName = "", DetailsValue = "";
    TextInputLayout detailsmsgcontainer;
    TextInputEditText detailsEditText, TreeNameEnter;
    EditText tvsearch;
    TextView tv1, tv2;
    String LoginIDRetrived = "0";
    int SelectedPlant_ID = 0;
    ImageView Backbtn;
    private FusedLocationProviderClient client;
    private SupportMapFragment mapFragment;
    private int REQUEST_CODE = 111;
    private ProgressBar loadingPB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tag2);
        loadingPB = findViewById(R.id.idLoadingPB);
        checkid = findViewById(R.id.checkboxid);
        detailsmsgcontainer = findViewById(R.id.detailsmsgcontainer);
        tv1 = findViewById(R.id.tv1);
        tv2 = findViewById(R.id.tv2);
        TreeNameEnter = findViewById(R.id.edit_text);
        tvsearch = findViewById(R.id.tvsearch);
        Backbtn = findViewById(R.id.backbtn1);
        SharedPreferences shrPrf;
        shrPrf = getSharedPreferences("MyLoginInfo", Context.MODE_PRIVATE);
        LoginIDRetrived = shrPrf.getString("login_userid", "");
        Backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent iback = new Intent(TagActivity2.this, UserDashboard.class);
                startActivity(iback);
            }
        });

        if (checkid.isChecked()) {
            TextInputLayout textInputLayout = findViewById(R.id.customerSpinnerLayout);
            tagbtn = findViewById(R.id.tagbutton);
            tv1.setVisibility(View.GONE);
            tv2.setVisibility(View.VISIBLE);
            detailsEditText = findViewById(R.id.noteMessage);
            Button tagbuttonsecond = findViewById(R.id.tagbutton1);
            tagbuttonsecond.setVisibility(View.GONE);
            textInputLayout.setVisibility(View.VISIBLE);
            detailsmsgcontainer.setVisibility(View.VISIBLE);
            tagbtn.setVisibility(View.VISIBLE);
            tagbtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    DetailsValue = detailsEditText.getText().toString();
                    //Toast.makeText(TagActivity2.this, "I am clicked"+DetailsValue, Toast.LENGTH_SHORT).show();
                    postDataUsingVolley();

                }
            });
        } else {
            TextInputLayout textInputLayout = findViewById(R.id.customerSpinnerLayout);
            tagbtn = findViewById(R.id.tagbutton1);
            Button tagbuttonsecond = findViewById(R.id.tagbutton);
            tagbuttonsecond.setVisibility(View.GONE);
            tagbtn.setVisibility(View.VISIBLE);
            tv2.setVisibility(View.GONE);
            tv1.setVisibility(View.VISIBLE);
            textInputLayout.setVisibility(View.GONE);
            detailsmsgcontainer.setVisibility(View.GONE);
            tagbtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //Toast.makeText(TagActivity2.this, "I am clicked", Toast.LENGTH_SHORT).show();
                    //checkBlank();
                    postDataUsingVolley();

                }
            });
        }
        checkid.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

                                               @Override
                                               public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                                                   if (isChecked) {
                                                       detailsEditText = findViewById(R.id.noteMessage);
                                                       TextInputLayout textInputLayout = findViewById(R.id.customerSpinnerLayout);
                                                       detailsmsgcontainer = findViewById(R.id.detailsmsgcontainer);
                                                       tagbtn = findViewById(R.id.tagbutton);
                                                       Button tagbuttonsecond = findViewById(R.id.tagbutton1);
                                                       tagbuttonsecond.setVisibility(View.GONE);
                                                       textInputLayout.setVisibility(View.VISIBLE);
                                                       tv1.setVisibility(View.GONE);
                                                       tv2.setVisibility(View.VISIBLE);
                                                       detailsmsgcontainer.setVisibility(View.VISIBLE);
                                                       tagbtn.setVisibility(View.VISIBLE);
                                                       tagbtn.setOnClickListener(new View.OnClickListener() {
                                                           @Override
                                                           public void onClick(View v) {
                                                               DetailsValue = detailsEditText.getText().toString();
                                                               //Toast.makeText(TagActivity2.this, "I am clicked"+DetailsValue, Toast.LENGTH_SHORT).show();

                                                               postDataUsingVolley();

                                                           }
                                                       });
                                                   } else {
                                                       TextInputLayout textInputLayout = findViewById(R.id.customerSpinnerLayout);
                                                       tagbtn = findViewById(R.id.tagbutton1);
                                                       Button tagbuttonsecond = findViewById(R.id.tagbutton);
                                                       tagbuttonsecond.setVisibility(View.GONE);
                                                       tagbtn.setVisibility(View.VISIBLE);
                                                       tv2.setVisibility(View.GONE);
                                                       tv1.setVisibility(View.VISIBLE);
                                                       textInputLayout.setVisibility(View.GONE);
                                                       detailsmsgcontainer.setVisibility(View.GONE);
                                                       tagbtn.setOnClickListener(new View.OnClickListener() {
                                                           @Override
                                                           public void onClick(View v) {
                                                               //Toast.makeText(TagActivity2.this, "I am clicked", Toast.LENGTH_SHORT).show();

                                                               postDataUsingVolley();

                                                           }
                                                       });
                                                   }

                                               }
                                           }
        );

        TextInputLayout textInputLayout = findViewById(R.id.customerSpinnerLayout);
        autoCompleteTextView = (AutoCompleteTextView) textInputLayout.getEditText();
        TextInputLayout textInputLayout1 = findViewById(R.id.customerSpinnerSearch);
        autoCompleteTextView1 = (AutoCompleteTextView) textInputLayout1.getEditText();
        //fetch from cloud
        FetchtreenameUsingVolley();
        FetchShortStoryDataUsingVolley();

        ActionBar actionBar = getSupportActionBar();
        ColorDrawable colorDrawable
                = new ColorDrawable(Color.parseColor("#32CB00"));
        // Set BackgroundDrawable
        actionBar.setBackgroundDrawable(colorDrawable);
        actionBar.setTitle(Html.fromHtml("<font color=#ffffff>" + "<small>"
                + "tre" + "</small>" + "" + "<big>" + "e"
                + "</big>" + "" + "<small>" + "co" + "</small>"));

        mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.maps);

        client = LocationServices.getFusedLocationProviderClient(TagActivity2.this);

        if (ActivityCompat.checkSelfPermission(TagActivity2.this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            getCurrentLocation();
        } else {
            ActivityCompat.requestPermissions(TagActivity2.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE);
        }

        if (!(tvsearch.getText().toString().equalsIgnoreCase("")) && (TreeNameEnter.getText().toString().equalsIgnoreCase(""))) {
            TreeNameEnter.setText(tvsearch.getText().toString());
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

                mapFragment.getMapAsync(new OnMapReadyCallback() {
                    @Override
                    public void onMapReady(@NonNull GoogleMap googleMap) {
                        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
                        latval = location.getLatitude();
                        lngval = location.getLongitude();
                        altitute = location.getAltitude();

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

    private void FetchtreenameUsingVolley() {
        TreefetchingID = new ArrayList<Integer>();
        Treefetchingname = new ArrayList<String>();
        // url to post our data
        String url = VariableDecClass.IPAddress + "apiTreeTypeList";
        loadingPB.setVisibility(View.VISIBLE);
        String treename;

        // creating a new variable for our request queue
        RequestQueue queue = Volley.newRequestQueue(TagActivity2.this);

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
                //
                // Toast.makeText(TagActivity2.this, "Data Fetched", Toast.LENGTH_SHORT).show();
                try {
                    // on below line we are parsing the response
                    // to json object to extract data from it.
                    JSONArray respArray = new JSONArray(response);
                    for (int i = 0; i < respArray.length(); i++) {
                        JSONObject jsonObject = respArray.getJSONObject(i);
                        int fetchedid = jsonObject.getInt("id");
                        String Common_Name = jsonObject.getString("Common_Name");
                        Treefetchingname.add(Common_Name);
                        TreefetchingID.add(fetchedid);
                        //Toast.makeText(TagActivity2.this, "Data Fetched"+fetchedid+"  "+Common_Name, Toast.LENGTH_SHORT).show();


                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // method to handle errors.
                //Toast.makeText(TagActivity2.this, "Fail to get response = " + error, Toast.LENGTH_SHORT).show();
                loadingPB.setVisibility(View.GONE);
            }
        });
        // below line is to make
        // a json object request.
        queue.add(request);

        ArrayAdapter arrayAdapter = new ArrayAdapter(getApplicationContext(), android.R.layout.simple_dropdown_item_1line, Treefetchingname);
        autoCompleteTextView1.setAdapter(arrayAdapter);
        autoCompleteTextView1.setThreshold(1);
        autoCompleteTextView1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // Log.d("beforeTextChanged", String.valueOf(s));
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //Log.d("onTextChanged", String.valueOf(s));
                //Toast.makeText(TagActivity2.this, ""+s, Toast.LENGTH_SHORT).show();

            }

            @Override
            public void afterTextChanged(Editable s) {
                Log.d("afterTextChanged", String.valueOf(s));
                String selectedval = String.valueOf(s);

                if (Treefetchingname.contains(selectedval)) {
                    //Toast.makeText(getApplicationContext(),"Tree Name:"+selectedval,Toast.LENGTH_LONG).show();
                    int indexval = Treefetchingname.indexOf(selectedval);
                    //Toast.makeText(getApplicationContext(),"Index val:"+indexval,Toast.LENGTH_LONG).show();
                    SelectedPlant_ID = TreefetchingID.get(indexval);
                    //Toast.makeText(getApplicationContext(),"Plant_id"+SelectedPlant_ID,Toast.LENGTH_LONG).show();
                }


            }
        });
        autoCompleteTextView1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                String selectedText = (String) adapterView.getItemAtPosition(i);

                // Set the text of the EditText widget to the selected text
                TreeNameEnter.setText(selectedText);

            }
        });


    }

    private void FetchShortStoryDataUsingVolley() {
        ShortStoryID = new ArrayList<Integer>();
        ShortStorydetails = new ArrayList<String>();
        // url to post our data
        String url = VariableDecClass.IPAddress + "apiShortStoryList";
        loadingPB.setVisibility(View.VISIBLE);

        // creating a new variable for our request queue
        RequestQueue queue = Volley.newRequestQueue(TagActivity2.this);

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
                // Toast.makeText(TagActivity2.this, "Data Fetched", Toast.LENGTH_SHORT).show();
                try {
                    // on below line we are parsing the response
                    // to json object to extract data from it.
                    JSONArray respArray = new JSONArray(response);
                    for (int i = 0; i < respArray.length(); i++) {
                        JSONObject jsonObject = respArray.getJSONObject(i);
                        int fetchedid = jsonObject.getInt("id");
                        String storyfetched = jsonObject.getString("story");
                        ShortStorydetails.add(storyfetched);
                        ShortStoryID.add(fetchedid);
                        //Toast.makeText(TagActivity2.this, "Data Fetched"+fetchedid+"  "+Common_Name, Toast.LENGTH_SHORT).show();
                    }
                    ArrayAdapter<String> arshortstory = new ArrayAdapter<String>(TagActivity2.this, android.R.layout.simple_spinner_dropdown_item, ShortStorydetails);
                    autoCompleteTextView.setAdapter(arshortstory);
                    autoCompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            SelectedShortStoryval = (String) parent.getItemAtPosition(position);
                        }
                    });
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // method to handle errors.
                //Toast.makeText(TagActivity2.this, "Fail to get response = " + error, Toast.LENGTH_SHORT).show();
                loadingPB.setVisibility(View.GONE);
            }
        });
        // below line is to make
        // a json object request.
        queue.add(request);
    }

    private void postDataUsingVolley() {
        if (tvsearch.getText().toString().equalsIgnoreCase("")) {
            Toast.makeText(this, "Tree Selection cannot be Blank!!", Toast.LENGTH_LONG).show();
        }
        if (TreeNameEnter.getText().toString().equalsIgnoreCase("")) {
            Toast.makeText(this, "Tree Name cannot be Blank!!", Toast.LENGTH_LONG).show();
        }
        if (!(tvsearch.getText().toString().equalsIgnoreCase("")) && !(TreeNameEnter.getText().toString().equalsIgnoreCase("")))
        // url to post our data
        {
            EnteredTreeName = TreeNameEnter.getText().toString();
            //Toast.makeText(getApplicationContext(),"Plant_id"+SelectedPlant_ID,Toast.LENGTH_LONG).show();
            // Toast.makeText(getApplicationContext(),"Short Story"+SelectedShortStoryval,Toast.LENGTH_LONG).show();
            String url = VariableDecClass.IPAddress + "apiAddPlantTag";
            loadingPB.setVisibility(View.VISIBLE);

            // creating a new variable for our request queue
            RequestQueue queue = Volley.newRequestQueue(TagActivity2.this);

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
                    //Toast.makeText(LoginActivity.this, "Response received", Toast.LENGTH_SHORT).show();
                    try {
                        // on below line we are parsing the response
                        // to json object to extract data from it.
                        JSONObject respObj = new JSONObject(response);
                        IDAfterTaging = respObj.getString("id");
                        // Toast.makeText(getApplicationContext(), "" + IDretrive, Toast.LENGTH_LONG).show();
                        postDataUsingVolley1(IDAfterTaging);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }, new com.android.volley.Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    // method to handle errors.
                    //Toast.makeText(TagActivity2.this, "image sending failed" + error, Toast.LENGTH_SHORT).show();
                    loadingPB.setVisibility(View.GONE);

                }
            }) {
                @Override
                protected Map<String, String> getParams() {
                    // below line we are creating a map for
                    // storing our values in key and value pair.
                    Map<String, String> params = new HashMap<String, String>();
                    //SharedPreferences shrPrf;
                    //shrPrf = getSharedPreferences("MyLoginInfo", Context.MODE_PRIVATE);
                    //String Loginid=shrPrf.getString("login_userid","");
                    // on below line we are passing our key
                    // and value pair to our parameters.
                    params.put("tag_name", EnteredTreeName);
                    params.put("tagged_by", LoginIDRetrived);
                    params.put("short_story", SelectedShortStoryval);
                    params.put("long_story", DetailsValue);
                    params.put("plant_type_id", "" + SelectedPlant_ID);
                    params.put("latitude", "" + latval);
                    params.put("longitude", "" + lngval);
                    params.put("planted_by", LoginIDRetrived);
                    params.put("altitude", "" + altitute);
                    //planted_on info
                    //Otherinfo


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

    private void postDataUsingVolley1(String idval) {
        AddImagesGlobal = new ArrayList<>();
        File imgFolder = new File(this.getExternalFilesDir(null).getAbsolutePath() + "/TempDirfortreeco");
        if (imgFolder.exists()) {
            File[] allFiles = imgFolder.listFiles(new FilenameFilter() {
                public boolean accept(File dir, String name) {
                    return (name.endsWith(".jpg") || name.endsWith(".jpeg"));
                }
            });
            if (allFiles.length != 0) {
                for (int i = 0; i < allFiles.length; i++) {
                    File imgFile = new File(this.getExternalFilesDir(null).getAbsolutePath() + "/TempDirfortreeco/img" + i + ".jpg");
                    if (imgFile.exists()) {
                        // Toast.makeText(this,"i am inside image file exist",Toast.LENGTH_LONG).show();
                        Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
                        AddImagesGlobal.add(myBitmap);
                        ByteArrayOutputStream baos = new ByteArrayOutputStream();
                        AddImagesGlobal.get(i).compress(Bitmap.CompressFormat.JPEG, 100, baos);
                        byte[] imageBytes = baos.toByteArray();
                        String imageString = Base64.encodeToString(imageBytes, Base64.DEFAULT);
                        //Toast.makeText(this, "Imagestring:" + imageString, Toast.LENGTH_LONG).show();
                        postDataUsingVolley2(idval, i, allFiles.length, imageString);

                    } else {
                        Toast.makeText(this, "No image file with the name exist folder" + AddImagesGlobal.size(), Toast.LENGTH_LONG).show();
                    }
                }


            }
        }
    }

    private void postDataUsingVolley2(String idval, int serialnum, int lentgval, String imageStringval) {
        // url to post our data
        String url = VariableDecClass.IPAddress + "apiFileSave";
        loadingPB.setVisibility(View.VISIBLE);

        // creating a new variable for our request queue
        RequestQueue queue = Volley.newRequestQueue(TagActivity2.this);

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
                //Toast.makeText(LoginActivity.this, "Response received", Toast.LENGTH_SHORT).show();
                try {
                    // on below line we are parsing the response
                    // to json object to extract data from it.
                    JSONObject respObj = new JSONObject(response);
                    //Toast.makeText(TagActivity2.this, "image sending" + respObj, Toast.LENGTH_SHORT).show();
                    if (serialnum == lentgval - 1) {
                        Intent inew = new Intent(TagActivity2.this, TagActivity3.class);
                        startActivity(inew);
                    }


                } catch (JSONException e) {
                    Toast.makeText(TagActivity2.this, "image sending" + e, Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // method to handle errors.
                //Toast.makeText(TagActivity2.this, "image sending failed" + error, Toast.LENGTH_SHORT).show();
                loadingPB.setVisibility(View.GONE);

            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                // below line we are creating a map for
                // storing our values in key and value pair.
                Map<String, String> params1 = new HashMap<String, String>();

                // on below line we are passing our key
                // and value pair to our parameters.
                params1.put("id", idval);
                params1.put("name", "" + idval + "_" + serialnum + ".jpg".trim().toString());
                params1.put("imgcontent", imageStringval);
                //Log.d("Parameters",""+params1);
                // returning our params.
                return params1;
            }
        };
        // below line is to make
        // a json object request.
        queue.add(request);
    }

    @Override
    public void onBackPressed() {
        // super.onBackPressed();
        Intent iback = new Intent(TagActivity2.this, UserDashboard.class);
        startActivity(iback);
    }
}






