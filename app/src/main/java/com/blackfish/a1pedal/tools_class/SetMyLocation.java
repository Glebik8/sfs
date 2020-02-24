package com.blackfish.a1pedal.tools_class;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.blackfish.a1pedal.MapsActivity;
import com.blackfish.a1pedal.ProfileInfo.Chats;
import com.blackfish.a1pedal.ProfileInfo.User;
import com.blackfish.a1pedal.R;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

public class SetMyLocation extends AppCompatActivity implements OnMapReadyCallback {
    private static final int MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1 ;
    private static final int MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION1 = 1 ;
    private GoogleMap mMap;
    private MapView mapView;
    TextView BackTextView , DoneTextView ;
    String lan = "" , lat = "" ;
    Marker marker;
    private FusedLocationProviderClient fusedLocationClient;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        BackTextView = findViewById(R.id.BackText);
        DoneTextView = findViewById(R.id.DoneTextView);
        DoneTextView.setText("Отправить");
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        BackTextView.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent answerIntent = new Intent();
                setResult(RESULT_CANCELED, answerIntent);
                finish();
            }});
        DoneTextView.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (!lan.equals("") && !lat.equals(""))
                {
                    Chats.getInstance().setMy_loction_lat_lan(lat+","+lan);
                   }
                Intent answerIntent = new Intent();
                setResult(RESULT_OK, answerIntent);
                finish();
            }});


    }


    private void init() {
        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                if (marker!=null){marker.remove();}
                marker =  mMap.addMarker(new MarkerOptions() .position(latLng));
                lat = String.valueOf(latLng.latitude);
                lan = String.valueOf(latLng.longitude);
            }
        });

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
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
       /* LatLng sydney = new LatLng(-34, 151);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));*/

        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            ActivityCompat.requestPermissions(SetMyLocation.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);

         /*   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                                      int[] grantResults);*/
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.

            return;
        }
        mMap.setMyLocationEnabled(true);
        Location loc = mMap.getMyLocation();
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        fusedLocationClient.getLastLocation()
                .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        // Got last known location. In some rare situations this can be null.
                        if (location != null) {
                            lat = String.valueOf(location.getLatitude());
                            lan = String.valueOf(location.getLongitude());
                            LatLng sydney = new LatLng(location.getLatitude() , location.getLongitude());
                            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney,13));

                        }
                    }
                });
        init();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (Arrays.equals(grantResults, new int[]{RESULT_CANCELED, RESULT_OK}) ) {
            mMap.setMyLocationEnabled(true);
            Location loc = mMap.getMyLocation();
            fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
            fusedLocationClient.getLastLocation()
                    .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                        @Override
                        public void onSuccess(Location location) {
                            // Got last known location. In some rare situations this can be null.
                            if (location != null) {
                                lat = String.valueOf(location.getLatitude());
                                lan = String.valueOf(location.getLongitude());
                                LatLng sydney = new LatLng(location.getLatitude(), location.getLongitude());
                                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney, 13));
                            }
                        }
                    });
            init();
        }
        else {
            Intent answerIntent = new Intent();
            setResult(RESULT_CANCELED, answerIntent);
            finish();
        }
    }


}
