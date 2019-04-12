package com.mau.dalvi.locationserver;

import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.lang.reflect.Member;

import static android.content.ContentValues.TAG;

public class MapMaster implements OnMapReadyCallback {

    private GoogleMap mMap;
    private Members[] markers;
    private Controller controller;


    public MapMaster(SupportMapFragment mapFragment) {
        mapFragment.getMapAsync(this);
    }

    public void setMarkers(Members[] markers){this.markers = markers;}

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        addSelfMarker();
        addMarkers();

    }

    public void setController(Controller controller){
        this.controller = controller;
    }

    public void addSelfMarker(){

        LatLng me = new LatLng(controller.getMyLatitude(), controller.getMyLongitude());
        Log.d(TAG, "addSelfMarker: my location" + controller.getMyLatitude() + " , " + controller.getMyLongitude());
        mMap.addMarker(new MarkerOptions().position(me).title("Here I Am"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(me));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(me, 16));
    }

    public void addMarkers(){
        if (markers != null) {
            for (Members marker: markers) {
                LatLng latLng = new LatLng(marker.getLatitude(), marker.getLongitude());
                MarkerOptions markerOptions = new MarkerOptions();
                markerOptions.position(latLng);
                Log.d(TAG, "addMarkers: getting here" + marker.getLongitude() + " " + marker.getName());
                markerOptions.title(marker.getName() + " at: " + marker.getLatitude() + ", " + marker.getLongitude());
                mMap.addMarker(markerOptions);

            }
        }
    }
    public void updateMap(){

        if(mMap != null ){
            mMap.clear();
            addMarkers();
          // addSelfMarker();
        }


    }
}
