package com.example.treeco;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.slider.Slider;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class CreateEvent extends AppCompatActivity {
    Integer day, month, year, hour, minute;
    EditText dated_on, time;
    TextInputLayout NumberoftreeInput, ChoiceofMaintainerInput, ChoiceofExpertInput, ChoiceofEventType, ChoiceofMaintanceType, ChoiceofTree, ChoiceofPOC;
    FrameLayout frameLayout;
    AutoCompleteTextView NumberoftreeACTextview, ChoiceofMaintainerACTextview, ChoiceofExpertACTextview, ChoiceofEventTypeACTextview, ChoiceofMaintenceTypeACTextview, ChoiceofTreeACTextView, ChoiceofPOCACTextView;
    ExtendedFloatingActionButton sendApprovalButton;
    TextInputEditText NameTxtMainPage, EventDesTxtMainPage, EventStart_On, PlantationAreaEditText, Locationtxt;
    ImageView disablepopup;
    String retriveNumberofTree = "";
    String DateTimeInfo = "", Dateinfo = "", TimeInfo = "";
    String SelectedMaintainerInfo = "", SelectedMaintainerDuration = "", SelectedExpertInfo = "", selectedTreeval = "", SelectedMaintainancetype = "";
    ArrayList<String> arMaintainerName, arExpertName;
    //treeinfo array
    ArrayList<String> arTreefetchingname, arCarbonFP, arOxygen, arAQI, arWateringDuration, arApplicableSoilCond, arFGrowthCondition, arSurvivalrate, arPerTreeCost, arMaintainerCost;
    ArrayList<Integer> arTreefetchingID, arPlantTypeID, arExpertID, arMaintainerTypeID, arMaintainerID;
    ArrayList<List> arExpertIDandPrice, arExpertIDandPlantID, arMaintainIDandPrice, arMaintainIDandPMainTypeID;
    int SelectedPlant_ID = -1, SelectedExpertID, SelectedMaintainerID, SelectedMaintainerTypeID, SelectedEventTypeID, SelectedUserID;
    int positopnvalExpert, positionalvalMaintainer, positionMaintainanceretriveval;
    String PlantationDistance = "";
    String LoginIDRetrived;
    float totalMaintaincost, totaleventcost;
    ImageView closebtn;
    ArrayList<Integer> arUserID;
    ArrayList<String> arUserName;
    private Calendar mCalendar;
    private ProgressBar loadingPB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_event);
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

        //add res contents
        closebtn = findViewById(R.id.closebtn);
        loadingPB = findViewById(R.id.idLoadingPB);
        //disablepopup=findViewById(R.id.disablepopup);
        frameLayout = findViewById(R.id.mainlayout);
        sendApprovalButton = findViewById(R.id.extended_fab1);
        NumberoftreeInput = findViewById(R.id.customerSpinnerSearch4);
        NumberoftreeACTextview = (AutoCompleteTextView) NumberoftreeInput.getEditText();
        ChoiceofMaintainerInput = findViewById(R.id.customerSpinnerSearch6);
        ChoiceofMaintainerACTextview = (AutoCompleteTextView) ChoiceofMaintainerInput.getEditText();
        ChoiceofPOC = findViewById(R.id.filledTextField1);
        ChoiceofPOCACTextView = (AutoCompleteTextView) ChoiceofPOC.getEditText();
        ChoiceofExpertInput = findViewById(R.id.customerSpinnerSearch2);
        ChoiceofExpertACTextview = (AutoCompleteTextView) ChoiceofExpertInput.getEditText();
        ChoiceofEventType = findViewById(R.id.customerSpinnerSearch);
        ChoiceofEventTypeACTextview = (AutoCompleteTextView) ChoiceofEventType.getEditText();
        ChoiceofMaintanceType = findViewById(R.id.customerSpinnerSearch5);
        ChoiceofMaintenceTypeACTextview = (AutoCompleteTextView) ChoiceofMaintanceType.getEditText();
        ChoiceofTree = findViewById(R.id.customerSpinnerSearch3);
        ChoiceofTreeACTextView = (AutoCompleteTextView) ChoiceofTree.getEditText();
        PlantationAreaEditText = findViewById(R.id.edit_text13);
        arUserID = new ArrayList<>();
        arUserName = new ArrayList<>();

        //Shared Preferences value fetch
        SharedPreferences shrPrf;
        shrPrf = getSharedPreferences("MyLoginInfo", Context.MODE_PRIVATE);
        LoginIDRetrived = shrPrf.getString("login_userid", "");
        //add date time
        dated_on = findViewById(R.id.edit_dated);
        time = findViewById(R.id.edit_time);
        mCalendar = Calendar.getInstance();
        day = mCalendar.get(Calendar.DAY_OF_MONTH);
        year = mCalendar.get(Calendar.YEAR);
        month = mCalendar.get(Calendar.MONTH);
        hour = mCalendar.get(Calendar.HOUR_OF_DAY);
        minute = mCalendar.get(Calendar.MINUTE);
        closebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), EventDetails.class);
                startActivity(intent);
            }
        });
        dated_on.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(CreateEvent.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year1, int month1, int day1) {
                        Dateinfo = Integer.toString(year1) + "-" + Integer.toString(month1 + 1) + "-" + Integer.toString(day1);
                        dated_on.setText(Integer.toString(year1) + "-" + Integer.toString(month1 + 1) + "-" + Integer.toString(day1));
                    }
                }, year, month, day);
                datePickerDialog.show();
            }
        });
        time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TimePickerDialog timePickerDialog = new TimePickerDialog(CreateEvent.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int hourOfDay, int minute) {
                        TimeInfo = Integer.toString(hourOfDay) + ":" + Integer.toString(minute);
                        time.setText(Integer.toString(hourOfDay) + ":" + Integer.toString(minute));
                    }
                }, hour, minute, true);
                timePickerDialog.show();
            }
        });
        //volley calls
        postDataUsingVolleyToFetchExpertList();
        postDataUsingVolleyToFetchMaintainerList();
        postDataUsingVolleyToFetchEventType();
        postDataUsingVolleyToFetchMaintainerType();
        FetchtreenameUsingVolley();
        postDataUsingVolleyToretriveUser();
        sendApprovalButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AddEventUsingVolley();
            }
        });

        //Number of tree planted
        NumberoftreeACTextview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                showBottomSheetDialogNumberoftree();
            }

        });


    }

    public void AlertFunction(String Plantationid, String Eventid) {
        int LoginInfo = Integer.parseInt(LoginIDRetrived);
        AlertDialog.Builder al = new AlertDialog.Builder(this);
        al.setTitle("Location Share Alert");
        al.setMessage("Do you want to specify the event area?");
        al.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent i = new Intent(CreateEvent.this, EventMapInfo.class);
                i.putExtra("plantid", Plantationid);
                i.putExtra("eventid", Eventid);
                i.putExtra("created_by", LoginInfo);
                startActivity(i);
            }
        });
        al.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(getApplicationContext(), EventDetails.class);
                startActivity(intent);
            }
        });
        al.create();
        al.show();
    }

    //hide keyboard
    private void hideKeyboard() {
        // Check if no view has focus:
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager inputManager = (InputMethodManager) this.getSystemService(Context.INPUT_METHOD_SERVICE);
            inputManager.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    //number of tree dialog
    private void showBottomSheetDialogNumberoftree() {

        hideKeyboard();
        String TotalPlantationArea = PlantationAreaEditText.getText().toString();
        if (TotalPlantationArea.equalsIgnoreCase("")) {
            Toast.makeText(CreateEvent.this, "Please provide plantation area", Toast.LENGTH_LONG).show();
        } else {
            final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(this);
            bottomSheetDialog.setContentView(R.layout.activity_createevent_numberoftree);
            Slider sliderval = bottomSheetDialog.findViewById(R.id.slider);
            TextView totalarea = bottomSheetDialog.findViewById(R.id.totalarea);
            totalarea.setText("" + TotalPlantationArea);
            TextView showNumTree = bottomSheetDialog.findViewById(R.id.showtree);
            TextInputEditText inputfetch = bottomSheetDialog.findViewById(R.id.edit_text_numberoftree);
            ImageView selectbtn = bottomSheetDialog.findViewById(R.id.selectbtnval);
            int amountofTree = NumberofPlantCalculation(1, totalarea.getText().toString());
            showNumTree.setText("" + amountofTree);
            selectbtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (inputfetch.getText().toString().equalsIgnoreCase("")) {
                        retriveNumberofTree = showNumTree.getText().toString();
                        PlantationDistance = "" + sliderval.getValue();

                    } else {
                        retriveNumberofTree = inputfetch.getText().toString();
                        Double distanceval = DistanceCalculation(TotalPlantationArea, retriveNumberofTree);
                        PlantationDistance = "" + distanceval;
                    }
                    bottomSheetDialog.dismiss();
                    NumberoftreeACTextview.setText("" + retriveNumberofTree);

                }
            });
            sliderval.addOnSliderTouchListener(new Slider.OnSliderTouchListener() {
                @Override
                public void onStartTrackingTouch(Slider slider) {

                }

                @Override
                public void onStopTrackingTouch(Slider slider) {

                    int amountofTree = NumberofPlantCalculation(slider.getValue(), totalarea.getText().toString());
                    showNumTree.setText("" + amountofTree);
                }
            });


            bottomSheetDialog.show();
        }
    }

    //Maintainance of tree dialog
    private void showBottomSheetDialogMaintainance(int positionretrive) {
        //ArrayAdopter for showing duration
        String[] arMaintainerDuration = {"1", "2", "3", "4", "5", "6", "7", "8", "9", "10"};
        SelectedMaintainerDuration = arMaintainerDuration[0];
        positionMaintainanceretriveval = positionretrive;
        hideKeyboard();
        final BottomSheetDialog bottomSheetDialog1 = new BottomSheetDialog(this);
        bottomSheetDialog1.setContentView(R.layout.activity_createevent_maintainance);
        TextView ChoiceofGardnerText = bottomSheetDialog1.findViewById(R.id.gardner_id);
        TextView costmaintain = bottomSheetDialog1.findViewById(R.id.cost_maintain);
        ChoiceofGardnerText.setText(SelectedMaintainerInfo);
        TextView treenum = bottomSheetDialog1.findViewById(R.id.treenum);
        TextView durationtxt = bottomSheetDialog1.findViewById(R.id.duration);
        TextView treenameMaintain = bottomSheetDialog1.findViewById(R.id.treenamemaintain);
        TextView priceMaintain = bottomSheetDialog1.findViewById(R.id.pricemaintain);
        MaintainerDetailsCal(positionretrive, costmaintain, treenum, durationtxt, treenameMaintain, priceMaintain);

        TextInputLayout ChoiceofMaintainerInputPopup = bottomSheetDialog1.findViewById(R.id.customerSpinnerSearchMaintainer);
        AutoCompleteTextView ChoiceofMaintainerACTextviewPopup = (AutoCompleteTextView) ChoiceofMaintainerInputPopup.getEditText();
        TextInputLayout ChoiceofMaintainerInputDurationPopup = bottomSheetDialog1.findViewById(R.id.customerSpinnerSearchDuration);
        AutoCompleteTextView ChoiceofMaintainerACTextviewDurationPopup = (AutoCompleteTextView) ChoiceofMaintainerInputDurationPopup.getEditText();
        ArrayAdapter arrayAdapterarMaintainerList = new ArrayAdapter(CreateEvent.this, android.R.layout.simple_spinner_dropdown_item, arMaintainerName);
        ChoiceofMaintainerACTextviewPopup.setAdapter(arrayAdapterarMaintainerList);
        //selected info showing at combo
        ChoiceofMaintainerACTextviewPopup.setText(ChoiceofMaintainerACTextviewPopup.getAdapter().getItem(positionMaintainanceretriveval).toString(), false);
        ChoiceofMaintainerACTextviewPopup.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                SelectedMaintainerInfo = (String) parent.getItemAtPosition(position);
                positionMaintainanceretriveval = position;
                ChoiceofGardnerText.setText(SelectedMaintainerInfo);
                MaintainerDetailsCal(positionretrive, costmaintain, treenum, durationtxt, treenameMaintain, priceMaintain);
            }
        });
        ArrayAdapter arrayAdapterarMaintainerDuration = new ArrayAdapter(CreateEvent.this, android.R.layout.simple_spinner_dropdown_item, arMaintainerDuration);
        ChoiceofMaintainerACTextviewDurationPopup.setAdapter(arrayAdapterarMaintainerDuration);
        //selected info showing at combo
        ChoiceofMaintainerACTextviewDurationPopup.setText(ChoiceofMaintainerACTextviewDurationPopup.getAdapter().getItem(0).toString(), false);
        ChoiceofMaintainerACTextviewDurationPopup.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                positionMaintainanceretriveval = position;
                SelectedMaintainerDuration = (String) parent.getItemAtPosition(position);
                MaintainerDetailsCal(positionretrive, costmaintain, treenum, durationtxt, treenameMaintain, priceMaintain);
            }
        });


        //selected Maintainer action
        ImageView selectMaintainancebtn = bottomSheetDialog1.findViewById(R.id.selectMaintainancebtn);
        selectMaintainancebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Implemented function
                ChoiceofMaintainerACTextview.setText(ChoiceofMaintainerACTextview.getAdapter().getItem(positionMaintainanceretriveval).toString(), false);
                bottomSheetDialog1.dismiss();
            }
        });

        bottomSheetDialog1.show();

    }

    //ChoiceofExpert of tree dialog
    private void showBottomSheetDialogChoiceofExpert(int position) {
        positopnvalExpert = position;
        hideKeyboard();
        final BottomSheetDialog bottomSheetDialog2 = new BottomSheetDialog(this);
        bottomSheetDialog2.setContentView(R.layout.activity_createevent_choiceofexpert);
        TextView treenametext = bottomSheetDialog2.findViewById(R.id.treenametext);
        TextView costTxt = bottomSheetDialog2.findViewById(R.id.costtxt);
        TextView carbon_foottxt = bottomSheetDialog2.findViewById(R.id.carbon_foot);
        TextView oxygen_inputtxt = bottomSheetDialog2.findViewById(R.id.oxygen_input);
        TextView watering_durationtxt = bottomSheetDialog2.findViewById(R.id.watering_duration);
        TextView full_growth_durationtxt = bottomSheetDialog2.findViewById(R.id.full_growth_duration);
        TextView survival_ratetxt = bottomSheetDialog2.findViewById(R.id.survival_rate);
        TextView Total_event_Cost = bottomSheetDialog2.findViewById(R.id.totaleventcost);
        //Choice of expert
        TextInputLayout ChoiceofExpertPopup = bottomSheetDialog2.findViewById(R.id.customerSpinnerCEPopup);
        AutoCompleteTextView ChoiceofExpertACPopup = (AutoCompleteTextView) ChoiceofExpertPopup.getEditText();
        ArrayAdapter arrayAdapterarExpertList = new ArrayAdapter(CreateEvent.this, android.R.layout.simple_spinner_dropdown_item, arExpertName);
        ChoiceofExpertACPopup.setAdapter(arrayAdapterarExpertList);

        //selected info showing at combo
        ChoiceofExpertACPopup.setText(ChoiceofExpertACPopup.getAdapter().getItem(positopnvalExpert).toString(), false);

        ChoiceofExpertACPopup.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                SelectedExpertInfo = (String) parent.getItemAtPosition(position);
                SelectedExpertID = arExpertID.get(position);
                positopnvalExpert = position;
            }
        });
        //Choice of tree
        TextInputLayout ChoiceofTTPnPopup = bottomSheetDialog2.findViewById(R.id.customerSpinnerSearchTTPPopup);
        AutoCompleteTextView ChoiceofTTPACPopup = (AutoCompleteTextView) ChoiceofTTPnPopup.getEditText();
        ArrayAdapter arrayAdapterarTreeList = new ArrayAdapter(CreateEvent.this, android.R.layout.simple_spinner_dropdown_item, arTreefetchingname);
        ChoiceofTTPACPopup.setAdapter(arrayAdapterarTreeList);
        ChoiceofTTPACPopup.setThreshold(1);
        //selected info showing at combo
        if (SelectedPlant_ID != -1) {
            ChoiceofTTPACPopup.setText(ChoiceofTTPACPopup.getAdapter().getItem(SelectedPlant_ID).toString(), false);
            treenametext.setText(selectedTreeval);
            carbon_foottxt.setText(arCarbonFP.get(positopnvalExpert));
            oxygen_inputtxt.setText(arOxygen.get(positopnvalExpert));
            watering_durationtxt.setText(arWateringDuration.get(positopnvalExpert));
            full_growth_durationtxt.setText(arFGrowthCondition.get(positopnvalExpert));
            survival_ratetxt.setText(arSurvivalrate.get(positopnvalExpert));
            List<Integer> retrivePlantID = arExpertIDandPlantID.get(positopnvalExpert);
            List<String> retrivePlantCost = arExpertIDandPrice.get(positopnvalExpert);
            if (retrivePlantID.contains(SelectedPlant_ID)) {
                int indexofPlantID = retrivePlantID.indexOf(SelectedPlant_ID);
                String costofPlant = retrivePlantCost.get(indexofPlantID);
                costTxt.setText(costofPlant);
                if (!retriveNumberofTree.equalsIgnoreCase("")) {
                    int numberoftree = Integer.parseInt(retriveNumberofTree);
                    float Cost = Float.parseFloat(costofPlant);
                    totaleventcost = Cost * numberoftree;
                    Total_event_Cost.setText("" + totaleventcost);
                } else {
                    Total_event_Cost.setText("");
                }
            } else {
                int indexofPlantID = retrivePlantID.indexOf(0);
                String costofPlant = retrivePlantCost.get(indexofPlantID);
                costTxt.setText(costofPlant);
                if (!retriveNumberofTree.equalsIgnoreCase("")) {
                    int numberoftree = Integer.parseInt(retriveNumberofTree);
                    float Cost = Float.parseFloat(costofPlant);
                    totaleventcost = Cost * numberoftree;
                    Total_event_Cost.setText("" + totaleventcost);
                } else {
                    Total_event_Cost.setText("");
                }
            }

        }
        ChoiceofTTPACPopup.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // Log.d("beforeTextChanged", String.valueOf(s));
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //Log.d("onTextChanged", String.valueOf(s));

            }

            @Override
            public void afterTextChanged(Editable s) {
                selectedTreeval = String.valueOf(s);
                if (arTreefetchingname.contains(selectedTreeval)) {
                    //Toast.makeText(getApplicationContext(),"Tree Name:"+selectedval,Toast.LENGTH_LONG).show();
                    int indexval1 = arTreefetchingname.indexOf(selectedTreeval);
                    selectedTreeval = arTreefetchingname.get(indexval1);
                    carbon_foottxt.setText(arCarbonFP.get(indexval1));
                    oxygen_inputtxt.setText(arOxygen.get(indexval1));
                    watering_durationtxt.setText(arWateringDuration.get(indexval1));
                    full_growth_durationtxt.setText(arFGrowthCondition.get(indexval1));
                    survival_ratetxt.setText(arSurvivalrate.get(indexval1));
                    treenametext.setText(selectedTreeval);
                    SelectedPlant_ID = indexval1;
                    List<Integer> retrivePlantID = arExpertIDandPlantID.get(positopnvalExpert);
                    List<String> retrivePlantCost = arExpertIDandPrice.get(positopnvalExpert);
                    if (retrivePlantID.contains(SelectedPlant_ID)) {
                        int indexofPlantID = retrivePlantID.indexOf(SelectedPlant_ID);
                        String costofPlant = retrivePlantCost.get(indexofPlantID);
                        costTxt.setText(costofPlant);
                        if (!retriveNumberofTree.equalsIgnoreCase("")) {
                            int numberoftree = Integer.parseInt(retriveNumberofTree);
                            float Cost = Float.parseFloat(costofPlant);
                            float totaleventcost = Cost * numberoftree;
                            Total_event_Cost.setText("" + totaleventcost);
                        } else {
                            Total_event_Cost.setText("");
                        }
                    } else {
                        int indexofPlantID = retrivePlantID.indexOf(0);
                        String costofPlant = retrivePlantCost.get(indexofPlantID);
                        costTxt.setText(costofPlant);
                        if (!retriveNumberofTree.equalsIgnoreCase("")) {
                            int numberoftree = Integer.parseInt(retriveNumberofTree);
                            float Cost = Float.parseFloat(costofPlant);
                            float totaleventcost = Cost * numberoftree;
                            Total_event_Cost.setText("" + totaleventcost);
                        } else {
                            Total_event_Cost.setText("");
                        }
                    }
                }
            }
        });


        //selected Expert action
        ImageView selectedExpertbtn = bottomSheetDialog2.findViewById(R.id.selectexpertbtn);
        selectedExpertbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Implemented function
                ChoiceofTreeACTextView.setText(ChoiceofTreeACTextView.getAdapter().getItem(SelectedPlant_ID).toString(), false);
                ChoiceofExpertACTextview.setText(ChoiceofExpertACTextview.getAdapter().getItem(positopnvalExpert).toString(), false);
                bottomSheetDialog2.dismiss();
            }
        });

        bottomSheetDialog2.show();
    }

    public int NumberofPlantCalculation(float slider, String totalarea) {

        Double totalareafloat = Double.parseDouble(totalarea);
        float radius = slider / 2;
        Double areaofOneTree = 1.27324 * 3.141 * radius * radius;
        float totalNumofTree = Float.parseFloat("" + totalareafloat / areaofOneTree);
        int NumberofTreeCanPlant = Math.round(totalNumofTree);
        return NumberofTreeCanPlant;

    }

    public double DistanceCalculation(String totalarea, String Numberoftree) {

        Double totalareaD = Double.parseDouble(totalarea);
        int NumofTree = Integer.parseInt(Numberoftree);
        Double radius = Math.sqrt(totalareaD / (NumofTree * 1.27324 * 3.141));
        Double distance = radius * radius;
        return distance;

    }

    //Api to access MaintainerList
    private void postDataUsingVolleyToFetchMaintainerList() {
        // url to post our data
        String url = VariableDecClass.IPAddress + "apiMaintainerList";
        loadingPB.setVisibility(View.VISIBLE);
        arMaintainerName = new ArrayList<>();
        arMaintainerID = new ArrayList<>();
        arMaintainIDandPMainTypeID = new ArrayList<>();
        arMaintainIDandPrice = new ArrayList<>();
        // creating a new variable for our request queue
        RequestQueue queue = Volley.newRequestQueue(CreateEvent.this);

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
                    JSONArray respArray = new JSONArray(response);

                    for (int i = 0; i < respArray.length(); i++) {
                        arMaintainerTypeID = new ArrayList<>();
                        arMaintainerCost = new ArrayList<>();
                        String respName = respArray.getJSONObject(i).getString("name");
                        arMaintainerName.add(respName);
                        int MaintainerID = respArray.getJSONObject(i).getInt("id");
                        arMaintainerID.add(MaintainerID);
                        JSONArray MaintainerPriceArray = respArray.getJSONObject(i).getJSONArray("maintainance_price");
                        for (int j = 0; j < MaintainerPriceArray.length(); j++) {
                            int Maintainertypeid = MaintainerPriceArray.getJSONObject(j).getInt("maintainer_id");
                            String MaintainerCost = MaintainerPriceArray.getJSONObject(j).getString("per_tree_cost");
                            arMaintainerTypeID.add(Maintainertypeid);
                            arMaintainerCost.add(MaintainerCost);

                        }
                        arMaintainIDandPrice.add(arMaintainerCost);
                        arMaintainIDandPMainTypeID.add(arMaintainerTypeID);
                        //Toast.makeText(CreateEvent.this, "Maintainer List" + arMaintainIDandPMainTypeID.size(), Toast.LENGTH_SHORT).show();
                    }


                    ArrayAdapter arrayAdapterarMaintainerList = new ArrayAdapter(CreateEvent.this, android.R.layout.simple_spinner_dropdown_item, arMaintainerName);
                    ChoiceofMaintainerACTextview.setAdapter(arrayAdapterarMaintainerList);
                    ChoiceofMaintainerACTextview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            SelectedMaintainerInfo = (String) parent.getItemAtPosition(position);
                            positionalvalMaintainer = position;
                            SelectedMaintainerID = arMaintainerID.get(position);
                            showBottomSheetDialogMaintainance(position);

                            //Toast.makeText(CreateEvent.this, "Maintainer List" + SelectedMaintainerInfo, Toast.LENGTH_SHORT).show();

                        }
                    });


                } catch (JSONException e) {
                    //Toast.makeText(CreateEvent.this, "Maintainer list error" + e, Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // method to handle errors.
                //Toast.makeText(CreateEvent.this, "Expert List Not loaded" + error, Toast.LENGTH_SHORT).show();
                loadingPB.setVisibility(View.GONE);

            }
        });
        // below line is to make
        // a json object request.
        queue.add(request);
    }

    //Api to access ExpertList
    private void postDataUsingVolleyToFetchExpertList() {
        // url to post our data
        String url = VariableDecClass.IPAddress + "apiExpertList";
        loadingPB.setVisibility(View.VISIBLE);
        arExpertName = new ArrayList<>();
        arExpertID = new ArrayList<>();
        arExpertIDandPrice = new ArrayList<>();
        arExpertIDandPlantID = new ArrayList<>();
        // creating a new variable for our request queue
        RequestQueue queue = Volley.newRequestQueue(CreateEvent.this);
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
                    JSONArray respArray = new JSONArray(response);
                    for (int i = 0; i < respArray.length(); i++) {
                        arPlantTypeID = new ArrayList<>();
                        arPerTreeCost = new ArrayList<>();
                        String respName = respArray.getJSONObject(i).getString("name");
                        int expID = respArray.getJSONObject(i).getInt("id");
                        arExpertName.add(respName);
                        arExpertID.add(expID);
                        JSONArray expPriceArray = respArray.getJSONObject(i).getJSONArray("expert_price");
                        for (int j = 0; j < expPriceArray.length(); j++) {
                            int plantid = expPriceArray.getJSONObject(j).getInt("plant_type_id");
                            String plantCost = expPriceArray.getJSONObject(j).getString("per_tree_cost");
                            arPlantTypeID.add(plantid);
                            arPerTreeCost.add(plantCost);

                        }
                        arExpertIDandPrice.add(arPerTreeCost);
                        arExpertIDandPlantID.add(arPlantTypeID);

                    }
                    ArrayAdapter arrayAdapterExpertList = new ArrayAdapter(CreateEvent.this, android.R.layout.simple_spinner_dropdown_item, arExpertName);
                    ChoiceofExpertACTextview.setAdapter(arrayAdapterExpertList);
                    ChoiceofExpertACTextview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            SelectedExpertInfo = (String) parent.getItemAtPosition(position);
                            positopnvalExpert = position;
                            SelectedExpertID = arExpertID.get(position);
                            showBottomSheetDialogChoiceofExpert(position);
                        }
                    });


                } catch (JSONException e) {
                    //Toast.makeText(CreateEvent.this, "Event list error" + e, Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // method to handle errors.
                //Toast.makeText(CreateEvent.this, "Expert List Not loaded" + error, Toast.LENGTH_SHORT).show();
                loadingPB.setVisibility(View.GONE);

            }
        });
        // below line is to make
        // a json object request.
        queue.add(request);
    }

    //Api to access EventType
    private void postDataUsingVolleyToFetchEventType() {
        // url to post our data
        String url = VariableDecClass.IPAddress + "apiEventType";
        loadingPB.setVisibility(View.VISIBLE);
        ArrayList<String> arEventType = new ArrayList<>();
        ArrayList<Integer> arEventID = new ArrayList<>();
        // creating a new variable for our request queue
        RequestQueue queue = Volley.newRequestQueue(CreateEvent.this);

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
                    JSONArray respArray = new JSONArray(response);

                    for (int i = 0; i < respArray.length(); i++) {
                        String respName = respArray.getJSONObject(i).getString("name");
                        int respID = respArray.getJSONObject(i).getInt("id");
                        arEventType.add(respName);
                        arEventID.add(respID);
                        //Toast.makeText(CreateEvent.this, "Expert List" + respName, Toast.LENGTH_SHORT).show();
                    }
                    ArrayAdapter arrayAdapterEventType = new ArrayAdapter(CreateEvent.this, android.R.layout.simple_spinner_dropdown_item, arEventType);
                    ChoiceofEventTypeACTextview.setAdapter(arrayAdapterEventType);
                    ChoiceofEventTypeACTextview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            String SelectedEventType = (String) parent.getItemAtPosition(position);
                            SelectedEventTypeID = arEventID.get(position);
                            //Toast.makeText(CreateEvent.this, "Event Type" + SelectedEventType, Toast.LENGTH_SHORT).show();

                        }
                    });


                } catch (JSONException e) {
                    //Toast.makeText(CreateEvent.this, "Event list error" + e, Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // method to handle errors.
                //Toast.makeText(CreateEvent.this, "Event Type Not loaded" + error, Toast.LENGTH_SHORT).show();
                loadingPB.setVisibility(View.GONE);

            }
        });
        // below line is to make
        // a json object request.
        queue.add(request);
    }

    //Api to access EventType
    private void postDataUsingVolleyToFetchMaintainerType() {
        // url to post our data
        String url = VariableDecClass.IPAddress + "apiMaintainenceType";
        loadingPB.setVisibility(View.VISIBLE);
        ArrayList<String> arMaintainerType = new ArrayList<>();
        // creating a new variable for our request queue
        RequestQueue queue = Volley.newRequestQueue(CreateEvent.this);

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
                    JSONArray respArray = new JSONArray(response);

                    for (int i = 0; i < respArray.length(); i++) {
                        String respName = respArray.getJSONObject(i).getString("name");
                        arMaintainerType.add(respName);
                        //Toast.makeText(CreateEvent.this, "Expert List" + respName, Toast.LENGTH_SHORT).show();
                    }
                    ArrayAdapter arrayAdapterEventType = new ArrayAdapter(CreateEvent.this, android.R.layout.simple_spinner_dropdown_item, arMaintainerType);
                    ChoiceofMaintenceTypeACTextview.setAdapter(arrayAdapterEventType);
                    ChoiceofMaintenceTypeACTextview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            SelectedMaintainancetype = (String) parent.getItemAtPosition(position);
                            SelectedMaintainerTypeID = position;
                            //Toast.makeText(CreateEvent.this, "Maintainer" + SelectedMaintainancetype, Toast.LENGTH_SHORT).show();

                        }
                    });


                } catch (JSONException e) {
                    Toast.makeText(CreateEvent.this, "Maintainer list error" + e, Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // method to handle errors.
                //Toast.makeText(CreateEvent.this, "Maintainer Not loaded" + error, Toast.LENGTH_SHORT).show();
                loadingPB.setVisibility(View.GONE);

            }
        });
        // below line is to make
        // a json object request.
        queue.add(request);
    }

    //Api to fetch types of tree

    private void FetchtreenameUsingVolley() {
        arTreefetchingID = new ArrayList<Integer>();
        arTreefetchingname = new ArrayList<String>();
        arOxygen = new ArrayList<String>();
        arAQI = new ArrayList<String>();
        arCarbonFP = new ArrayList<String>();
        arWateringDuration = new ArrayList<String>();
        arApplicableSoilCond = new ArrayList<String>();
        arFGrowthCondition = new ArrayList<String>();
        arSurvivalrate = new ArrayList<String>();
        arPerTreeCost = new ArrayList<String>();
        // url to post our data
        String url = VariableDecClass.IPAddress + "apiTreeTypeList";
        loadingPB.setVisibility(View.VISIBLE);

        // creating a new variable for our request queue
        RequestQueue queue = Volley.newRequestQueue(CreateEvent.this);

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
                // Toast.makeText(PlantActivity2.this, "Data Fetched", Toast.LENGTH_SHORT).show();
                try {
                    // on below line we are parsing the response
                    // to json object to extract data from it.
                    JSONArray respArray = new JSONArray(response);
                    for (int i = 0; i < respArray.length(); i++) {
                        JSONObject jsonObject = respArray.getJSONObject(i);
                        int fetchedid = jsonObject.getInt("id");
                        String Common_Name = jsonObject.getString("Common_Name");
                        String Carbon_footprint = jsonObject.getString("Carbon_footprint");
                        String Oxygen_release = jsonObject.getString("Oxygen_release");
                        String Avg_Time_Full_Growth = jsonObject.getString("Avg_Time_Full_Growth");
                        String Survival_Rate = jsonObject.getString("Survival_Rate");
                        String Watering_Duration = jsonObject.getString("Watering_Duration");
                        arTreefetchingname.add(Common_Name);
                        arTreefetchingID.add(fetchedid);
                        arCarbonFP.add(Carbon_footprint);
                        arOxygen.add(Oxygen_release);
                        // arAQI.add("");
                        //arApplicableSoilCond.add("");
                        arFGrowthCondition.add(Avg_Time_Full_Growth);
                        arSurvivalrate.add(Survival_Rate);
                        arWateringDuration.add(Watering_Duration);

                        //Toast.makeText(TagActivity2.this, "Data Fetched"+fetchedid+"  "+Common_Name, Toast.LENGTH_SHORT).show();
                        ArrayAdapter arrayAdapter = new ArrayAdapter(getApplicationContext(), android.R.layout.simple_dropdown_item_1line, arTreefetchingname);
                        ChoiceofTreeACTextView.setAdapter(arrayAdapter);
                        ChoiceofTreeACTextView.setThreshold(1);
                        ChoiceofTreeACTextView.addTextChangedListener(new TextWatcher() {
                            @Override
                            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                                // Log.d("beforeTextChanged", String.valueOf(s));
                            }

                            @Override
                            public void onTextChanged(CharSequence s, int start, int before, int count) {
                                //Log.d("onTextChanged", String.valueOf(s));

                            }

                            @Override
                            public void afterTextChanged(Editable s) {
                                Log.d("afterTextChanged", String.valueOf(s));
                                selectedTreeval = String.valueOf(s);
                                if (arTreefetchingname.contains(selectedTreeval)) {
                                    //Toast.makeText(getApplicationContext(),"Tree Name:"+selectedval,Toast.LENGTH_LONG).show();
                                    int indexval = arTreefetchingname.indexOf(selectedTreeval);
                                    //Toast.makeText(getApplicationContext(),"Index val:"+indexval,Toast.LENGTH_LONG).show();
                                    SelectedPlant_ID = arTreefetchingID.get(indexval);
                                    //Toast.makeText(getApplicationContext(),"Plant_id"+SelectedPlant_ID,Toast.LENGTH_LONG).show();
                                }


                            }
                        });
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // method to handle errors.
                //Toast.makeText(CreateEvent.this, "Fail to get response = " + error, Toast.LENGTH_SHORT).show();
                loadingPB.setVisibility(View.GONE);
            }
        });
        // below line is to make
        // a json object request.
        queue.add(request);
    }

    public void MaintainerDetailsCal(int position, TextView costmaintain, TextView treenum, TextView durationtxt, TextView treenameMaintain, TextView priceMaintain) {
        treenum.setText("" + retriveNumberofTree);
        durationtxt.setText(SelectedMaintainerDuration + "Yr");
        treenameMaintain.setText(selectedTreeval);
        List<Integer> retriveMaintainerTypeID = arMaintainIDandPMainTypeID.get(position);
        List<String> retriveMaintainerCost = arMaintainIDandPrice.get(position);
        if (retriveMaintainerTypeID.contains(SelectedMaintainerTypeID)) {
            int indexofMaintainerTypeID = retriveMaintainerTypeID.indexOf(SelectedMaintainerTypeID);
            String costval = retriveMaintainerCost.get(indexofMaintainerTypeID);
            costmaintain.setText(costval);
            if (!retriveNumberofTree.equalsIgnoreCase("")) {
                int numberoftree = Integer.parseInt(retriveNumberofTree);
                float Cost = Float.parseFloat(costval);
                int durationval = Integer.parseInt(SelectedMaintainerDuration);
                totalMaintaincost = Cost * numberoftree * durationval;
                priceMaintain.setText("" + totalMaintaincost);
            } else {
                priceMaintain.setText("");
            }
        } else if (retriveMaintainerTypeID.contains(0)) {
            int indexofMaintainerTypeID = retriveMaintainerTypeID.indexOf(0);
            String costval = retriveMaintainerCost.get(indexofMaintainerTypeID);
            costmaintain.setText(costval);
            if (!retriveNumberofTree.equalsIgnoreCase("")) {
                int numberoftree = Integer.parseInt(retriveNumberofTree);
                float Cost = Float.parseFloat(costval);
                int durationval = Integer.parseInt(SelectedMaintainerDuration);
                totalMaintaincost = Cost * numberoftree * durationval;
                priceMaintain.setText("" + totalMaintaincost);
            } else {
                priceMaintain.setText("");
            }
        } else {
            costmaintain.setText("NA");
            priceMaintain.setText("0");

        }


    }

    private void AddEventUsingVolley() {
        //NameTxtMainPage,POCTxtMainPage,EventDesTxtMainPage,EventStart_On,PlantationAreaEditText;
        NameTxtMainPage = findViewById(R.id.namefield);
        Locationtxt = findViewById(R.id.locationtxt);
        // POCTxtMainPage=findViewById(R.id.pocfield);
        EventDesTxtMainPage = findViewById(R.id.noteMessage);
        DateTimeInfo = Dateinfo + " " + TimeInfo;
        //need to change in future
        int LoginInfo;
        if (LoginIDRetrived.equalsIgnoreCase("")) {
            LoginInfo = 65;
        } else {
            LoginInfo = Integer.parseInt(LoginIDRetrived);

        }
        // url to post our data
        String url = VariableDecClass.IPAddress + "apiAddEvent";
        loadingPB.setVisibility(View.VISIBLE);
        // creating a new variable for our request queue
        RequestQueue queue = Volley.newRequestQueue(CreateEvent.this);
        try {
            JSONObject params = new JSONObject();
            params.put("name", NameTxtMainPage.getText().toString());
            params.put("contact_person", SelectedUserID);
            params.put("description", EventDesTxtMainPage.getText().toString());
            params.put("start_on", DateTimeInfo);
            params.put("location", Locationtxt.getText().toString());
            params.put("event_type_id", SelectedEventTypeID);
            params.put("expert_id", SelectedExpertID);
            params.put("plantation_area", PlantationAreaEditText.getText().toString());
            params.put("plantation_cost", totaleventcost);
            params.put("created_by", LoginInfo);
            params.put("maintainer_id", SelectedMaintainerID);
            params.put("maintainence_type_id", SelectedMaintainerTypeID);
            params.put("plants_distance", PlantationDistance);
            params.put("maintainence_cost", totalMaintaincost);
            params.put("maintainence_duration", Integer.parseInt(SelectedMaintainerDuration) * 12);
            JSONArray plantationgroupArray = new JSONArray();
            JSONObject plantationgroupObject = new JSONObject();
            plantationgroupObject.put("plant_type_id", SelectedPlant_ID);
            plantationgroupObject.put("num_of_plants", retriveNumberofTree);
            plantationgroupArray.put(plantationgroupObject);
            params.put("plantation_groups", plantationgroupArray);
            Log.d("Params", params.toString());

            JsonObjectRequest request1 = new JsonObjectRequest(Request.Method.POST, url, params, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    loadingPB.setVisibility(View.GONE);
                    try {
                        String EventID = response.getString("event_id");
                        String Statusretrive = response.getString("status");
                        String PlantationID = response.getString("plantation_id");
                        AlertFunction(PlantationID, EventID);
                        Toast.makeText(CreateEvent.this, "" + Statusretrive, Toast.LENGTH_LONG).show();


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    //Toast.makeText(CreateEvent.this, "Fail to Add event = " + error, Toast.LENGTH_LONG).show();
                    loadingPB.setVisibility(View.GONE);

                }
            });
            // below line is to make
            // a json object request.
            request1.setRetryPolicy(new DefaultRetryPolicy(
                    0,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            queue.add(request1);
        } catch (Exception e) {
            //Toast.makeText(CreateEvent.this, "Unable to create json" + e, Toast.LENGTH_SHORT).show();
        }


    }

    private void postDataUsingVolleyToretriveUser() {
        // url to post our data
        String url = VariableDecClass.IPAddress + "apiUserInfo";

        loadingPB.setVisibility(View.VISIBLE);
        try {
            JSONArray jarray = new JSONArray();
            JSONObject params = new JSONObject();
            params.put("includeDeleted", false);
            jarray.put(params);
            //Toast.makeText(CreateEvent.this, ""+params, Toast.LENGTH_SHORT).show();

            // creating a new variable for our request queue
            RequestQueue queue1 = Volley.newRequestQueue(CreateEvent.this);
            // on below line we are calling a string
            // request method to post the data to our API
            // in this we are calling a post method.
            JsonArrayRequest request1 = new JsonArrayRequest(Request.Method.POST, url, jarray, new com.android.volley.Response.Listener<JSONArray>() {
                @Override
                public void onResponse(JSONArray response) {
                    // inside on response method we are
                    // hiding our progress bar
                    // and setting data to edit text as empty
                    loadingPB.setVisibility(View.GONE);

                    // on below line we are displaying a success toast message.
                    //Toast.makeText(RegwithOTP.this, "OTP received", Toast.LENGTH_SHORT).show();
                    try {
                        // on below line we are parsing the response
                        // to json object to extract data from it.
                        JSONArray resArray = response;
                        // Toast.makeText(CreateEvent.this, "resonse " + respObj, Toast.LENGTH_SHORT).show();
                        for (int i = 0; i < resArray.length(); i++) {
                            int userid = resArray.getJSONObject(i).getInt("id");
                            arUserID.add(userid);
                            String username = resArray.getJSONObject(i).getString("name");
                            arUserName.add(username);
                        }
                        //Toast.makeText(CreateEvent.this, "resonse " + arUserID, Toast.LENGTH_SHORT).show();
                        ArrayAdapter arrayAdapterUser = new ArrayAdapter(CreateEvent.this, android.R.layout.simple_spinner_dropdown_item, arUserName);
                        ChoiceofPOCACTextView.setAdapter(arrayAdapterUser);
                        ChoiceofPOCACTextView.setThreshold(1);
                        ChoiceofPOCACTextView.addTextChangedListener(new TextWatcher() {
                            @Override
                            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                                // Log.d("beforeTextChanged", String.valueOf(s));
                            }

                            @Override
                            public void onTextChanged(CharSequence s, int start, int before, int count) {
                                //Log.d("onTextChanged", String.valueOf(s));

                            }

                            @Override
                            public void afterTextChanged(Editable s) {
                                Log.d("afterTextChanged", String.valueOf(s));
                                String SelectedUser = String.valueOf(s);
                                int indexval = arUserName.indexOf(SelectedUser);
                                //Toast.makeText(getApplicationContext(),"Index val:"+indexval,Toast.LENGTH_LONG).show();
                                SelectedUserID = arUserID.get(indexval);
                                Log.d("Selected User", String.valueOf(SelectedUserID));
                            }
                        });

                        // Toast.makeText(getActivity(), "name " + user_name, Toast.LENGTH_SHORT).show();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }, new com.android.volley.Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    // method to handle errors.
                    //Toast.makeText(CreateEvent.this, "Fail to get response = " + error, Toast.LENGTH_SHORT).show();
                    loadingPB.setVisibility(View.GONE);
                }
            });
            // below line is to make
            // a json object request.
            queue1.add(request1);
        } catch (Exception e) {
            //Toast.makeText(CreateEvent.this, "Fail to create json = " + e, Toast.LENGTH_SHORT).show();
            loadingPB.setVisibility(View.GONE);
        }

    }

}
