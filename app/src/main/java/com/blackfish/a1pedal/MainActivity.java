package com.blackfish.a1pedal;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import androidx.annotation.NonNull;


import com.blackfish.a1pedal.API.Requests;
import com.blackfish.a1pedal.ChatKit.media.CustomMediaMessagesActivity;
import com.blackfish.a1pedal.ChatKit.media.DefaultDialogsActivity;
import com.blackfish.a1pedal.ProfileInfo.Chats;
import com.blackfish.a1pedal.ProfileInfo.FriendList;
import com.blackfish.a1pedal.tools_class.PostRes;
import com.blackfish.a1pedal.tools_class.Save;
import com.blackfish.a1pedal.tools_class.SocketEvent;
import com.droidnet.DroidListener;
import com.droidnet.DroidNet;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationItemView;
import com.google.android.material.bottomnavigation.BottomNavigationMenuView;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.appcompat.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.blackfish.a1pedal.ProfileInfo.Profile_Info;
import com.blackfish.a1pedal.ProfileInfo.User;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

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
import java.util.Objects;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;

import static com.blackfish.a1pedal.tools_class.DataApdaterFriend.currentPosition;

public class MainActivity extends AppCompatActivity implements DroidListener {
    public final static String BROADCAST_ACTION ="com.blackfish.a1pedal.ChatKit.media";
    BroadcastReceiver br;
    private DroidNet mDroidNet;
    BottomNavigationView navigation;
    private TextView mTextMessage;
String type , token, fcmToken ;
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    loadFragment(DefaultDialogsActivity.newInstance());
                    return true;
                case R.id.navigation_dashboard:
                      loadFragment(CalendarViewFragment.newInstance());
                    return true;
                case R.id.navigation_notifications:
                    loadFragment(FriendFragment.newInstance());
                    return true;
                case R.id.navigation_notifications1:
                    loadFragment(Profile_Fragment.newInstance());
                    return true;
            }
            return false;
        }
    };
    public void loadFragment(Fragment fragment) {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.fl_content, fragment);
        ft.commit();
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mDroidNet = DroidNet.getInstance();
        mDroidNet.addInternetConnectivityListener(this);
        navigation = (BottomNavigationView) findViewById(R.id.navigation);
        token= Profile_Info.getInstance().getToken();
        type = User.getInstance().getType();
        Chats.getInstance().setExit("false");
        Intent intent = getIntent();

        if (type.equals("driver")) {
            navigation.inflateMenu(R.menu.navigation_v);
            navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
            navigation.setSelectedItemId(R.id.navigation_notifications1);
            try {
                String id = intent.getStringExtra("id");
                if (id.equals("3")) {
                    navigation.setSelectedItemId(R.id.navigation_notifications);
                }
                if (id.equals("1"))
                { navigation.setSelectedItemId(R.id.navigation_home);}
            }catch (Exception e){

            }
        }
        else
        {
            navigation.inflateMenu(R.menu.navigation_s);
            navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
            navigation.setSelectedItemId(R.id.navigation_notifications1);
            try {
                String id = intent.getStringExtra("id");
                if (id.equals("3")) {
                    navigation.setSelectedItemId(R.id.navigation_notifications);
                }
                if (id.equals("1"))
                {navigation.setSelectedItemId(R.id.navigation_home);}
            }catch (Exception e){

            }
        }

        br = new BroadcastReceiver() {
            // действия при получении сообщений
            public void onReceive(Context context, Intent intent) {
                    String message = intent.getStringExtra("Message");
                    String f = message;
                    try {
                        JSONObject   r = new JSONObject(message);
                        JSONObject m= r.getJSONObject("message");
                        String info = m.getString("info");
                        if (info.equals("NewMessage") ) {
                                JSONObject c = m.getJSONObject("contains");
                                String chat = c.getString("chat");
                                JSONObject s = c.getJSONObject("sender");
                                String last_activity = s.getString("last_activity");

                                ArrayList<Chats.UnreadChats> uc = Chats.getInstance().getUnread_chats_count();
                                ArrayList<Chats.UnreadChats> uc3 = new ArrayList<>();

                                if (!last_activity.equals(Chats.getInstance().getLastActivity())) {
                                    Chats.getInstance().setLastActivity(last_activity);
                                          if (uc.size()!=0) {
                                              if (Chats.getInstance().getActual_activity().equals("1"))
                                              {
                                                  int alse = 0;
                                              for (int g = 0; g <= uc.size() - 1; g++) {
                                                  Chats.UnreadChats uc1 = uc.get(g);
                                                  String ch = uc1.getChat_id();
                                                  String un = uc1.getUnreadcount();
                                                  if (ch.equals(Chats.getInstance().getChat_id()))
                                                  {
                                                      Chats.getInstance().setTotal_unread(String.valueOf(Integer.parseInt(Chats.getInstance().getTotal_unread()) - Integer.parseInt(un)));
                                                      un = String.valueOf(0);
                                                    }
                                                  if (!chat.equals(Chats.getInstance().getChat_id()) && ch.equals(chat))
                                                  {
                                                      un = String.valueOf(Integer.parseInt(un) + 1);
                                                      Chats.getInstance().setTotal_unread(String.valueOf(Integer.parseInt(Chats.getInstance().getTotal_unread()) + 1));
                                                  }
                                                  uc3.add(new Chats.UnreadChats(ch, un));
                                                   }
                                              }
                                                  else {
                                                  for (int g = 0; g <= uc.size() - 1; g++) {
                                                      Chats.UnreadChats uc1 = uc.get(g);
                                                      String ch = uc1.getChat_id();
                                                      String un = uc1.getUnreadcount();
                                                  if (ch.equals(chat)) {
                                                    un = String.valueOf(Integer.parseInt(un) + 1);
                                                    Chats.getInstance().setTotal_unread(String.valueOf(Integer.parseInt(Chats.getInstance().getTotal_unread()) + 1));
                                                  }
                                                      uc3.add(new Chats.UnreadChats(ch, un));
                                                  }
                                                  }
                                              Chats.getInstance().setUnread_chats_count(uc3);
                                          } else {
                                              if (!Chats.getInstance().getActual_activity().equals("1"))
                                              {
                                                  Chats.getInstance().setTotal_unread(String.valueOf(1));
                                                  uc3.add(new Chats.UnreadChats(chat, "1"));
                                                  Chats.getInstance().setUnread_chats_count(uc3);}
                                              else {
                                                  Chats.getInstance().setTotal_unread(String.valueOf(0));
                                                  uc3.add(new Chats.UnreadChats(chat, "0"));
                                                  Chats.getInstance().setUnread_chats_count(uc3);
                                              }
                                                 }
                                    Save.saveTotal_Unread (Chats.getInstance().getTotal_unread(),MainActivity.this);
                                    showBadge();
                                }}
                    } catch (JSONException e) {
                        e.printStackTrace();
                        if (message.equals("StartMessages") ){
                          /*  GetUnreadMess mt = new GetUnreadMess();
                            mt.execute();*/
                          showBadge();
                        }
                    }
                    }

            };
        IntentFilter intFilt = new IntentFilter(BROADCAST_ACTION);
        // регистрируем (включаем) BroadcastReceiver
        registerReceiver(br, intFilt);
    }
    @Override
    public void onDestroy()
    {   super.onDestroy();
        try {
            unregisterReceiver(br);
        }catch (Exception e) {}
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
        startService(new Intent(MainActivity.this, SocketEvent.class));
        GetUnreadMess mt = new GetUnreadMess();
        mt.execute();
        FirebaseInstanceId.getInstance().getInstanceId()
                .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                    @Override
                    public void onComplete(@NonNull Task<InstanceIdResult> task) {
                        if (!task.isSuccessful()) {
                            return;
                        }
                        fcmToken = task.getResult().getToken();
                        SetTokenOs mt1 = new SetTokenOs();
                        mt1.execute();
                    }});
    }
    private void netIsOff(){
        String total_unread = Save.loadTotal_Unread(MainActivity.this);
        Chats.getInstance().setTotal_unread(total_unread);
         showBadge();
    }



    public void showNav() {
        navigation.setVisibility(View.VISIBLE);
    }
    public void hideNav() {
        navigation.setVisibility(View.GONE);
    }
    public void showBadge () {
       if (!Chats.getInstance().getTotal_unread().equals("0") && !Chats.getInstance().getTotal_unread().equals("") ) {
           navigation.showBadge(R.id.navigation_home).setNumber(Integer.parseInt(Chats.getInstance().getTotal_unread()));
       }else {hideBadge();}
    }
    public void hideBadge () { navigation.removeBadge(R.id.navigation_home); }

    public class GetUnreadMess extends AsyncTask<Void, Void, String> {
        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;
        String resultJson = "";
        String   token= Profile_Info.getInstance().getToken();
        @Override
        protected String doInBackground(Void... params) {
            URL url = null;
            try {
                url = new URL("http://185.213.209.188/api/getunreadusermessages/");
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
            super.onPostExecute(response);
            String Rules="";
            try {
                 String total_unread = "0";
                JSONArray   kol = new JSONArray(response);
                ArrayList<Chats.UnreadChats> ch = new ArrayList<>();
                for (int i = 0 ; i<=kol.length()-1 ; i++)
                {
                    JSONObject elem = kol.getJSONObject(i);
                    String chat_id = elem.getString("chat_id");
                    String unread = elem.getString("unread_count");
                    total_unread =  String.valueOf( Integer.parseInt(total_unread) + Integer.parseInt(unread));
                    ch.add(new Chats.UnreadChats(chat_id,unread));
                }
                     Chats.getInstance().setUnread_chats_count(ch);
                     Chats.getInstance().setTotal_unread(total_unread);

                       Save.saveTotal_Unread (total_unread,MainActivity.this);
                     showBadge();


            }catch (JSONException ignored){
                String f = ignored.toString();
            }
        }
    }

    public class SetTokenOs extends AsyncTask<Void, Void, String> {
        String resultJson = "";
        @Override
        protected String doInBackground(Void... params) {
            String    token = Profile_Info.getInstance().getToken();
            PostRes example = new PostRes();
            String response="";
            try {
                JSONObject    data = new JSONObject();
                    data.put("os", "android");
                    data.put("token", fcmToken);
                response = example.post("http://185.213.209.188/api/updatetoken/", data.toString(), "Token "+token);
                String k =response;
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return response;
        }

        @Override
        protected void onPostExecute(String response) {
            super.onPostExecute(response);
            try {
                JSONObject jsonObject = new JSONObject(response);

            }catch (JSONException err){
            }
        }
    }
}
