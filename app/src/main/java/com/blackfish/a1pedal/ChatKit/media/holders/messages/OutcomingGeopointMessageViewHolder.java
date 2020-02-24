package com.blackfish.a1pedal.ChatKit.media.holders.messages;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentManager;

import com.blackfish.a1pedal.ChatKit.model.Message;
import com.blackfish.a1pedal.MapsActivity;
import com.blackfish.a1pedal.ProfileInfo.Chats;
import com.blackfish.a1pedal.ProfileInfo.User;
import com.blackfish.a1pedal.R;
import com.blackfish.a1pedal.tools_class.videoplayer;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.stfalcon.chatkit.messages.MessageHolders;
import com.stfalcon.chatkit.utils.DateFormatter;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.Executor;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;
import static androidx.core.app.ActivityCompat.startActivityForResult;
import static androidx.core.content.ContextCompat.checkSelfPermission;

public class OutcomingGeopointMessageViewHolder
        extends MessageHolders.OutcomingTextMessageViewHolder<Message> implements OnMapReadyCallback {
    private TextView tvDuration;
    private TextView tvTime;
    ImageView PlayImage;
    MapView mapView;
    GoogleMap gMap;
    String lan,lat;
    static final private int CHOOSE_THIEF = 0;
    private FusedLocationProviderClient fusedLocationClient;
    Context ct;

    //SupportMapFragment mapFragment;
    public OutcomingGeopointMessageViewHolder(View itemView, Object payload) {
        super(itemView, payload);
        tvTime = (TextView) itemView.findViewById(R.id.messageTime);
        PlayImage = itemView.findViewById(R.id.PlayImage);
        ct = itemView.getContext();
        mapView = (MapView) itemView.findViewById(R.id.mapview);
        if (mapView != null)
        {
            mapView.onCreate(null);
            mapView.onResume();
            mapView.getMapAsync(this);
        }
    }

    @Override
    public void onBind(Message message) {
        super.onBind(message);
        lan =     message.getGeopoint().getLan();
        lat =     message.getGeopoint().getLat();

        // Needs to call MapsInitializer before doing any CameraUpdateFactory calls
        // Updates the location and zoom of the MapView
        tvTime.setText(message.getStatus() + " " + DateFormatter.format(message.getCreatedAt(), DateFormatter.Template.TIME));
}
    @Override
    public void onMapReady(GoogleMap googleMap) {
        MapsInitializer.initialize(ct);
        gMap = googleMap;
        if(gMap != null) {
            gMap.clear();
            LatLng sydney = new LatLng(Double.valueOf(lat) , (Double.valueOf(lan)));
            gMap.addMarker(new MarkerOptions().position(sydney));
            CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(sydney, 13);
            gMap.moveCamera(cameraUpdate);
        }
    }


}
