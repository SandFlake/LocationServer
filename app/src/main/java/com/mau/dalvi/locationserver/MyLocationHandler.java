package com.mau.dalvi.locationserver;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;

public class MyLocationHandler {

    private static final String TAG = "MyLocationHandler";
    private double latitude;
    private double longitude;
    private MainActivity mainActivity;
    private Controller controller;


    public MyLocationHandler(MainActivity mainActivity) {
        this.mainActivity = mainActivity;
    }

    public void setController(Controller controller){
        this.controller = controller;
    }


    public void getMyLocation() {

        LocationManager locationManager = (LocationManager) mainActivity.getSystemService(Context.LOCATION_SERVICE);
        LocationListener locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                latitude = location.getLatitude();
                longitude = location.getLongitude();
                controller.setMyLatitude(latitude);
                controller.setMyLongitude(longitude);

            }


            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {

            }
        };

        if (ContextCompat.checkSelfPermission(mainActivity, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            Log.d(TAG, "getMyLocation: acces NOT granted");
            ActivityCompat.requestPermissions(mainActivity, new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    1);

        }
        if (ContextCompat.checkSelfPermission(mainActivity, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            Log.d(TAG, "getMyLocation: access granted");
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 10, 0, locationListener);
            //onPositionChanged();
            Log.d(TAG, "getMyLocation: test 2 : long " + longitude + " lat: " + latitude);
        }
    }



    public double getLatitude(){
        return latitude;
    }

    public double getLongitude(){return longitude;}
}
