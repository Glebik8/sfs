package com.blackfish.a1pedal;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.blackfish.a1pedal.ProfileInfo.User;
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

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {
    private static final int MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1 ;
    private static final int MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION1 = 1 ;
    private GoogleMap mMap;
    private MapView mapView;
    TextView BackTextView , DoneTextView ;
    String Gps = "" , Street = "" ;
    Marker marker;
    private FusedLocationProviderClient fusedLocationClient;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        BackTextView = findViewById(R.id.BackText);
        DoneTextView = findViewById(R.id.DoneTextView);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
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
               if (!Gps.equals("") && !Street.equals(""))
               {
                User.getInstance().setStreet(Street);
                User.getInstance().setGps(Gps);}
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
              Gps = latLng.latitude + "," + latLng.longitude;
               marker =  mMap.addMarker(new MarkerOptions() .position(latLng));

               Geocoder gCoder = new Geocoder(MapsActivity.this);
                ArrayList<Address> addresses = null;
                try {
                    addresses = (ArrayList<Address>) gCoder.getFromLocation(latLng.latitude, latLng.longitude, 1);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                if (addresses != null && addresses.size() > 0) {
                    if(addresses.get(0).getThoroughfare() != null) {
                        Street = addresses.get(0).getThoroughfare() ;
                      if (addresses.get(0).getFeatureName() != null)
                      {
                          Street= Street + " " + addresses.get(0).getFeatureName();
                      }
                    }
                }
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
            ActivityCompat.requestPermissions(MapsActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);

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

                            LatLng sydney = new LatLng(location.getLatitude() , location.getLongitude());
                            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney,13));

                        }
                    }
                });
        if (!User.getInstance().getGps().equals("null") && !User.getInstance().getGps().equals("")){
            String [] l = User.getInstance().getGps().split(",");

            marker =  mMap.addMarker(new MarkerOptions() .position( new LatLng(Double.parseDouble(l[0]) ,Double.parseDouble( l[1] ) )));}
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

                              LatLng sydney = new LatLng(location.getLatitude(), location.getLongitude());
                              mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney, 13));

                          }
                      }
                  });
          if (!User.getInstance().getGps().equals("null") && !User.getInstance().getGps().equals("")) {
              String[] l = User.getInstance().getGps().split(",");

              marker = mMap.addMarker(new MarkerOptions().position(new LatLng(Double.parseDouble(l[0]), Double.parseDouble(l[1]))));
          }
          init();
      }
      else {
          Intent answerIntent = new Intent();
          setResult(RESULT_CANCELED, answerIntent);
          finish();
      }
    }


}
