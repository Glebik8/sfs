package com.blackfish.a1pedal;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.blackfish.a1pedal.ChatKit.media.DefaultDialogsActivity;
import com.blackfish.a1pedal.ProfileInfo.FriendList;
import com.blackfish.a1pedal.ProfileInfo.Profile_Info;
import com.blackfish.a1pedal.ProfileInfo.User;
import com.blackfish.a1pedal.tools_class.DataApdaterFriend;
import com.droidnet.DroidListener;
import com.droidnet.DroidNet;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class FriendFragment   extends Fragment implements DroidListener {

    public static List<FriendList> friendLists = new ArrayList<>();
    public FriendFragment() {
    }

    public static FriendFragment newInstance() {
        return new FriendFragment();
    }
    private DroidNet mDroidNet;

    RecyclerView recyclerView;
    TextView ContNameText;
    ImageView AddImage;
    LinearLayout WaitInt;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.contacts_fram, container, false);
        AddImage=  view.findViewById(R.id.AddImage);
        ContNameText =  view.findViewById(R.id.ContNameText);
        WaitInt  =  view.findViewById(R.id.WaitInt);
        recyclerView = (RecyclerView) view.findViewById(R.id.friendList);
        mDroidNet = DroidNet.getInstance();
        mDroidNet.addInternetConnectivityListener(this);
        if (User.getInstance().getType().equals("driver"))
        {
            ContNameText.setText("Сервисы");
        }
        else
        {
            ContNameText.setText("Подписчики");
        }
        AddImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (User.getInstance().getType().equals("driver"))
                {
                    Intent intent = new Intent(getActivity(), AddSFriend.class);
                    startActivity(intent);
                }
                else

                {  String k ;
                    if (User.getInstance().getName()!=null && !User.getInstance().getName().equals(""))
                      { k = User.getInstance().getName();   }
                    else {k =  User.getInstance().getFio();  }
                    Intent sendIntent = new Intent();
                    sendIntent.setAction(Intent.ACTION_SEND);
                    sendIntent.putExtra(Intent.EXTRA_TEXT,
                            "Привет, мы используем приложение для автомобилистов 1pedal.  Скачать : https://play.google.com/....\nПодпишись на наш сервис по номеру " + User.getInstance().getPhone() + "\nС уважением команда  " +  User.getInstance().getFio() + ".");
                    sendIntent.setType("text/plain");
                    startActivity(sendIntent); }
            }});
        return view;
    }

    @Override
    public void onInternetConnectivityChanged(boolean isConnected) {

        if (isConnected) {
            //do Stuff with internet
            netIsOn();
        } else {
            //no internet
            netIsOff();
        }
    }

    private void netIsOn(){

        ContNameText.setVisibility(View.VISIBLE);
        AddImage.setVisibility(View.VISIBLE);
        WaitInt.setVisibility(View.GONE);
        GetContacts mt = new GetContacts();
        mt.execute();

    }
    private void netIsOff(){
        ContNameText.setVisibility(View.GONE);
        AddImage.setVisibility(View.GONE);
        WaitInt.setVisibility(View.VISIBLE);
        recyclerView.setAdapter(null);
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        mDroidNet.removeInternetConnectivityChangeListener(this);
    }



    class GetContacts extends AsyncTask<Void, Void, String> {

        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;
        String resultJson = "";
        String   token= Profile_Info.getInstance().getToken();
        @Override
        protected String doInBackground(Void... params) {
            URL url = null;
            try {
                url = new URL("http://185.213.209.188/api/friend/");
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }


            try {
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestProperty ("Authorization", "Token "+ token );
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();

                reader = new BufferedReader(new InputStreamReader(inputStream));

                String line;
                while ((line = reader.readLine()) != null) {
                    buffer.append(line);
                }

                resultJson = buffer.toString();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return resultJson;
        }
        @Override
        protected void onPostExecute(String response) {
            friendLists.clear();
            super.onPostExecute(response);
            String Rules="";
            try {

                JSONObject r = new JSONObject(response);
                JSONArray kol = r.getJSONObject("friends").getJSONArray("user_friends");

                for (int i = 0; i<kol.length() ; i++) {
                    JSONObject elem = kol.getJSONObject(i);
                    String name, model="";
                    String photo;
                    String pk = elem.getString("pk");
                    String type = elem.getString("type");
                    String last_activity = elem.getString("last_activity");
                    String work = "";
                    if (type.equals("service")) {
                        work = elem.getString("work");
                        name = elem.getString("name");

                    } else {
                        name = elem.getString("fio");
                        model = elem.getString("brand") + " " + elem.getString("model");
                    }
                    photo = elem.getString("photo");
                    if (!photo.equals("") && !photo.equals("1")) {
                        photo = "http://185.213.209.188" + photo;
                    } else {photo="";}
              FriendList f = new FriendList(name, photo , model, pk , work , type,last_activity );
            friendLists.add(f);
                }
                Context ct = getContext();
                if (ct!=null)
                { DataApdaterFriend adapter = new DataApdaterFriend(ct, friendLists);
                recyclerView.setLayoutManager(new LinearLayoutManager(ct));
                recyclerView.setAdapter(adapter);}
            }catch (JSONException ignored){
                String f = ignored.toString();

            }
        }
    }
}
