package com.blackfish.a1pedal.ChatKit.media.holders.messages;

import android.content.Context;
import android.net.Uri;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.blackfish.a1pedal.ChatKit.model.Message;
import com.blackfish.a1pedal.ChatKit.model.User;
import com.blackfish.a1pedal.ProfileInfo.Chats;
import com.blackfish.a1pedal.R;
import com.bumptech.glide.Glide;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.stfalcon.chatkit.messages.MessageHolders;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class IncomingGeopointMessageViewHolder  extends MessageHolders.OutcomingTextMessageViewHolder<Message> implements OnMapReadyCallback {
    private TextView tvDuration;
    private TextView tvTime;
    ImageView PlayImage , image;
    TextView NameTextImage;
    ImageView messageUserAvatar;
    Context ct;
    MapView mapView;
    GoogleMap gMap;
    String lan,lat;

    GoogleMap map;
    View layout;
    public IncomingGeopointMessageViewHolder(View itemView, Object payload) {
        super(itemView, payload);
        tvTime = (TextView) itemView.findViewById(R.id.time);
        PlayImage =  itemView.findViewById(R.id.PlayImage);
        mapView = (MapView) itemView.findViewById(R.id.mapView);
        NameTextImage= itemView.findViewById(R.id.NameTextImage);
        messageUserAvatar= itemView.findViewById(R.id.messageUserAvatar);
        ct = itemView.getContext();
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
        User us = message.getUser();
        String Avat = us.getAvatar();
        String Name1 = us.getName();
        if (Avat.equals("") || Avat.equals("1"))
        {   if (Name1.length()>2){
            Name1 = Name1.substring(0,2);}
            NameTextImage.setText(Name1);}
        else
        {   String []  st = Avat.split("/");
            String AvatName = st[st.length-1];
            File path= Chats.getInstance().getPath();
            File path1 = new File(path+AvatName);
            if (!path1.exists()){
                try {
                    downloadFileAsync(Avat,"avatar");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            else{
                Glide.with(itemView).load(path1).into(messageUserAvatar);
            }
        }
         lan =     message.getGeopoint().getLan();
         lat =     message.getGeopoint().getLat();
        }


    public void downloadFileAsync(final String downloadUrl , String type) throws Exception {
        String []  st = downloadUrl.split("/");
        String Name = st[st.length-1];
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url(downloadUrl).build();
        String finalName = Name;
        client.newCall(request).enqueue(new Callback() {
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            public void onResponse(Call call, Response response) throws IOException {
                if (!response.isSuccessful()) {
                    throw new IOException("Failed to download file: " + response);
                }
                File path= Chats.getInstance().getPath();
                FileOutputStream fos = new FileOutputStream( path+ finalName);
                fos.write(response.body().bytes());
                fos.close();
                if (type.equals("avatar"))
                { File path1 = new File(path+ Name);
                    Glide.with(itemView).load(path1).into(messageUserAvatar);   }
            }
        });
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