package com.comp259app2.berezowskib.mapsdemo;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    public static ListView list;
    public static ArrayAdapter<?> incomingPushMsg;
    public static ArrayList<String> items = new ArrayList<String>();
    private static final String TAG = MapsActivity.class.getSimpleName();
    private BroadcastReceiver mRegistrationBroadcastReceiver;
    String newMessage = "test";
    String lat = null;
    String lon = null;
    String Description = null;
    String NDate;
    Spinner spinner;
    int T_check = 1;


    //DATABASE AND ADAPTER OBJECTS
    DBHelper dbHelper;
    ArrayAdapter<Notification> arrayAdapter;

    //SINGLE RECORD INFORMATION IN A LIST
    List<Notification> LocationArrayList = new ArrayList<Notification>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        spinner = (Spinner) findViewById(R.id.spinner);

        // TASK 1: SET UP THE DATABASE
        dbHelper = new DBHelper(getApplicationContext());

        if (dbHelper.getLocationCount() != 0)
            LocationArrayList.addAll(dbHelper.getAllLocations());

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        //Grabs the intent sent from the notification intent.  This will only run when the application is
        //closed and if a notification was sent.
        if (getIntent().getExtras() != null) {
            Bundle bundle = getIntent().getExtras();
            //Grabs information from the getIntent intent
            lat = bundle.getString("Lat");
            lon = bundle.getString("Lon");
            Description = bundle.getString("Description");
            // NDate = bundle.getString("Date");
            // show a heading indicating we are starting app from a notification with data
            items.add("Data from Notification + Data Message");

//            lat = bundle.getString("Lat");
//            lon = bundle.getString("Lon");
//            Description = bundle.getString("Description");
            //Grabs the notification (messaging sent to the notification) time note, the time is in milliseconds
            long mdate = bundle.getLong("google.sent_time");
            //sets a format string
            String format = "dd-MM-yyyy";
              //Creates an instance of SimpleDateFormat
            SimpleDateFormat formatter = new SimpleDateFormat(format);

            // Create a calendar object that will convert the date and time value in milliseconds to date.
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(mdate);
            //converts the date to date layout and saves it as a string
            NDate = formatter.format(calendar.getTime());

            Toast.makeText(getApplicationContext(), "New Location: " + lat, Toast.LENGTH_LONG).show();
           T_check = 0;  //******************* added

        }

//This  is called when a broadcast is called, happens when a notification is sent with the application running
        mRegistrationBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                //creates an instance of bundle  and puts the values into variables
                Bundle bundle = intent.getExtras();
                lat = bundle.getString("Lat");
                lon = bundle.getString("Lon");
                Description = bundle.getString("Description");
                NDate = bundle.getString("Date");
                float lat2 = Float.valueOf(lat);
                float lon2 = Float.valueOf(lon);
                LatLng regina = new LatLng(lat2, lon2);
                //clears the map
                mMap.clear();
                //sets a new marker on the map
                mMap.addMarker(new MarkerOptions().position(regina).title(Description));
                //moves the camera to the new location
                mMap.moveCamera(CameraUpdateFactory.newLatLng(regina));
                //creates an instance of notification and calls the constructor
                Notification contact = new Notification(
                        dbHelper.getLocationCount(),
                        lat,
                        lon,
                        Description,
                        NDate
                        );
//creates a location within the database
                dbHelper.createLocation(contact);
                //adds the contact to the list array
                LocationArrayList.add(contact);

                arrayAdapter.notifyDataSetChanged();
                //this sets T_check to 1, which is used to check when then when the notification was received, (if the application was running or not)
                T_check = 1;

                Toast.makeText(getApplicationContext(), "New Location: "+Description+" Lat: "+lat+"Lon: "+lon , Toast.LENGTH_LONG).show();
           }};


    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    //loads when the map is loaded
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

//creates and sets the information within the spinner
        arrayAdapter = new ArrayAdapter<Notification>(
                this,
                android.R.layout.simple_spinner_item,
                LocationArrayList
        );


        spinner.setAdapter(arrayAdapter);


                // FCM "registration complete" receiver

        // FCM "new message" receiver
        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(Global.PUSH_NOTIFICATION));

        // Checks to see if the database is empty or not,
        if(lat!=null && lon != null){
            float lat2 = Float.valueOf(lat);
            float lon2 = Float.valueOf(lon);
            LatLng regina = new LatLng(lat2, lon2);
            mMap.addMarker(new MarkerOptions().position(regina).title(Description));
            mMap.moveCamera(CameraUpdateFactory.newLatLng(regina));



            Notification contact = new Notification(
                    dbHelper.getLocationCount(),
                    lat,
                    lon,
                    Description,
                    NDate);

                dbHelper.createLocation(contact);
                LocationArrayList.add(contact);

                arrayAdapter.notifyDataSetChanged();
               mMap.addMarker(new MarkerOptions().position(regina).title(Description));
               mMap.moveCamera(CameraUpdateFactory.newLatLng(regina));

                Toast.makeText(getApplicationContext(),
                        Description
                                + " has been added.",
                        Toast.LENGTH_SHORT).show();
               lat=null;
               lon = null;
            //   T_check = 0;
            //   ******************T_check = 0;  - checking this instance



        }
        else{
        LatLng regina = new LatLng(50.44, -104.61);
        mMap.addMarker(new MarkerOptions().position(regina).title("Marker in Regina"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(regina));
        }
//spinner listener
                spinner.setOnItemSelectedListener(
                        new AdapterView.OnItemSelectedListener() {
//when an item is selected from the dropdown list
                            @Override
                            public void onItemSelected(AdapterView<?>
                                                               adapterView, View view, int pos, long id) {
  //if T_check == 1 that means the selection was from the user selecting an option from the dropdown and not from the application starting up
                              if(T_check==1){
                                mMap.clear();

                                Notification currentLoc = LocationArrayList.get(pos);
                                //stores the ID
                                String c_lon = lon;
                                String c_lat = lat;
                                lon = currentLoc.getLon();
                                //Stores the name
                                lat = currentLoc.getLat();
                                //Stores the details
                                Description = currentLoc.getDescription();

                                float lat2 = Float.valueOf(lat);
                                float lon2 = Float.valueOf(lon);
                                LatLng regina = new LatLng(lat2, lon2);
                                mMap.addMarker(new MarkerOptions().position(regina).title(Description));
                                mMap.moveCamera(CameraUpdateFactory.newLatLng(regina));}
                              else{T_check=1;}

                            }






    @Override
    public void onNothingSelected(AdapterView<?>
                                          adapterView) {
    }
});}


    @Override
    protected void onPause() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mRegistrationBroadcastReceiver);
        super.onPause();
    }



}
