package com.example.jarigye.mapsapplication;

import android.Manifest;
import android.app.Activity;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.support.v7.widget.Toolbar;
import android.widget.Toast;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.tasks.OnSuccessListener;


public class MainActivity extends AppCompatActivity  {
    private static Location mCurrentLocation;
    int permissionCheck;

    LocationRequest mLocationRequest;
    GoogleApiClient mGoogleApiClient;
    Location mLastLocation;
    Marker mCurrLocationMarker;
    private FusedLocationProviderClient mFusedLocationClient;
    private Boolean mRequestingLocationUpdates=true;
    private LocationCallback mLocationCallback;
    private String phoneNo;
    private String message;
    private BroadcastReceiver yourReceiver;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final TextView Txt_latitude = (TextView) findViewById(R.id.txt_lt);
        final TextView Txt_longitude = (TextView) findViewById(R.id.textView4);
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
       int permissionCheck = ContextCompat.checkSelfPermission(this,Manifest.permission.ACCESS_FINE_LOCATION);
        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.ACCESS_FINE_LOCATION},1);

        mFusedLocationClient.getLastLocation().addOnSuccessListener(this, new OnSuccessListener<Location>() {

            public void onSuccess(Location location) {
               //  Got last known location. In some rare situations this can be null.
                if (location != null) {
                    mCurrentLocation = location;
                   // startLocationUpdates();
                    Txt_latitude.setText(Double.toString(mCurrentLocation.getLatitude()));
                    Txt_longitude.setText(Double.toString(mCurrentLocation.getLongitude()));
                    message="Lat:"+ Double.toString(mCurrentLocation.getLatitude())+""+"Longitude"+Double.toString(mCurrentLocation.getLongitude()
                    );
                }
            }
        });
       // startLocationUpdates();
        mLocationCallback = new LocationCallback() {
            public void onLocationResult(LocationResult locationResult) {
                for (Location mCurrentLocation : locationResult.getLocations()) {
                    // Update UI with location data
                    Txt_latitude.setText(Double.toString(mCurrentLocation.getLatitude()));
                    Txt_longitude.setText(Double.toString(mCurrentLocation.getLongitude()));
                    message="Lat:"+Double.toString(mCurrentLocation.getLatitude())+""+"Longitude"+Double.toString(mCurrentLocation.getLongitude()
                    );
                };
            }
        };


    }

    public void onRequestPermissionsResult(int requestCode, String[] permissions,int[] grantResults)
    {
        switch (requestCode) {
            case 1: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    SmsManager smsManager = SmsManager.getDefault();
                    smsManager.sendTextMessage("+250734598916", null, message, null, null);
                    Toast.makeText(getApplicationContext(), "SMS sent.",
                            Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(getApplicationContext(),
                            "SMS failed, please try again.", Toast.LENGTH_LONG).show();
                    return;
                }
                return;
            }

        }
    }

    private void startLocationUpdates() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            LocationRequest mLocationRequest = null;
            mFusedLocationClient.requestLocationUpdates(mLocationRequest,
                    mLocationCallback,null);
        }}
   public void onButtonClick(View view){

       Fragment fragment= new MapFragment();
       // Fragment fragment = CustomFragment.newInstance();
       FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
       transaction.replace(R.id.contaner, fragment);
       transaction.commit();
   }
    public void onTextClick(View view){

        sendSMS();
    }
    protected void sendSMS() {
        phoneNo = "+250734598916";
       // String sms = "Location";
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.SEND_SMS)
                != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.SEND_SMS)) {
            } else {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.SEND_SMS},
                        1);
            }
    }}
    @Override
    protected void onStop()
    {
       // unregisterReceiver(this);
        super.onStop();
    }
}
