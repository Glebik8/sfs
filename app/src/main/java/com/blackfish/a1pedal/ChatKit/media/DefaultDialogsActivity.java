package com.blackfish.a1pedal.ChatKit.media;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.blackfish.a1pedal.ChatKit.DemoDialogsActivity;
import com.blackfish.a1pedal.ChatKit.media.holders.dialogs.CustomDialogViewHolder;
import com.blackfish.a1pedal.ChatKit.model.Dialog;
import com.blackfish.a1pedal.ChatKit.model.Message;
import com.blackfish.a1pedal.ChatKit.model.User;
import com.blackfish.a1pedal.FriendFragment;
import com.blackfish.a1pedal.ProfileInfo.Chats;
import com.blackfish.a1pedal.ProfileInfo.Profile_Info;
import com.blackfish.a1pedal.R;
import com.blackfish.a1pedal.tools_class.Save;
import com.droidnet.DroidListener;
import com.droidnet.DroidNet;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.stfalcon.chatkit.dialogs.DialogsList;
import com.stfalcon.chatkit.dialogs.DialogsListAdapter;
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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;


import static com.blackfish.a1pedal.tools_class.DataApdaterFriend.currentPosition;
import static com.blackfish.a1pedal.tools_class.DataApdaterFriend.friendLists;

public class DefaultDialogsActivity extends DemoDialogsActivity implements DroidListener {

    private DroidNet mDroidNet;
    public ArrayList<Dialog> chats = new ArrayList<>();
    JSONArray   kol;
    public final static String BROADCAST_ACTION ="com.blackfish.a1pedal.ChatKit.media";
    BroadcastReceiver br;
ImageView AddImage;
    SharedPreferences sPref;
    TextView ContNameText;
    LinearLayout WaitInt;
    Context ct;
    public static String lastChat = null;
    public static void open(Context context) {
       // context.startActivity(new Intent(context, DefaultDialogsActivity.class));
    }

    private DialogsList dialogsList;
    public static DefaultDialogsActivity newInstance() {
        return new DefaultDialogsActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_custom_layout_dialogs, container, false);
        ContNameText = view.findViewById(R.id.ContNameText);
        WaitInt = view.findViewById(R.id.WaitInt);
        AddImage = view.findViewById(R.id.AddImage);
        ct = view.getContext();

        Chats.getInstance().setActual_activity("0"); dialogsList = (DialogsList) view.findViewById(R.id.dialogsList);
        mDroidNet = DroidNet.getInstance();
        mDroidNet.addInternetConnectivityListener(this);
         Chats.getInstance().setPath(getActivity().getExternalCacheDir());

        AddImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showFragment(FriendFragment.newInstance());
                closeFragment(DefaultDialogsActivity.this);
            }});


        br = new BroadcastReceiver() {
            // действия при получении сообщений
            public void onReceive(Context context, Intent intent) {
                if (Chats.getInstance().getActual_activity().equals("0") || (Chats.getInstance().getActual_activity().equals("1"))){
                    String message = intent.getStringExtra("Message");
                String f = message;
                    try {
                       JSONObject   r = new JSONObject(message);
                        JSONObject m= r.getJSONObject("message");
                        String info = m.getString("info");
                        if (info.equals("NewMessage")) {
                            GetUserFromJson(m);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        if (message.equals("StartMessege") ){

                        }
                    }
               }
            }
        };

        IntentFilter intFilt = new IntentFilter(BROADCAST_ACTION);
        // регистрируем (включаем) BroadcastReceiver
        getActivity().registerReceiver(br, intFilt);

        return view;
    }


    @Override
    public void onDialogClick(Dialog dialog) {
           Chats.getInstance().setChat_id(dialog.getId());
           String ds = dialog.getId();
           ArrayList<User> k =    dialog.getUsers();
           User us = k.get(0);
           String rep =  us.getId();
           Chats.getInstance().setRecipient_id(rep);
           Chats.getInstance().setLastActivity(us.getLastActivity());
           Chats.getInstance().setTittle_mess(us.getName());
           lastChat = Chats.getInstance().getRecipient_id();
            Log.d("glebik", Chats.getInstance().getChat_id());
           ArrayList<Chats.UnreadChats> uc = Chats.getInstance().getUnread_chats_count();
           ArrayList<Chats.UnreadChats> uc3 = new ArrayList<>();
           if (uc!=null) {
            for (int g = 0; g <= uc.size() - 1; g++) {
                Chats.UnreadChats uc1 = uc.get(g);
                String ch = uc1.getChat_id();
                String un = uc1.getUnreadcount();
                if (ch.equals(Chats.getInstance().getChat_id()))
                {
                    Chats.getInstance().setTotal_unread(String.valueOf(Integer.parseInt(Chats.getInstance().getTotal_unread()) - Integer.parseInt(un)));
                    un = String.valueOf(0);
                }
                uc3.add(new Chats.UnreadChats(ch, un));
            }
            Chats.getInstance().setUnread_chats_count(uc3);
            Intent intent = new Intent(DefaultDialogsActivity.BROADCAST_ACTION);
            intent.putExtra("Message", "StartMessages");
            getActivity().sendBroadcast(intent);
        }

           for (int po = 0 ; po<chats.size(); po++)
           {
               Dialog dd = chats.get(po);
               String ct_id = dd.getId();
               if (ct_id.equals(Chats.getInstance().getChat_id()))
               {
                   dd.setUnreadCount(0);
               }
           }
        CustomMediaMessagesActivity.open(getActivity(), -1);
    }
    private void initAdapter() {
        super.dialogsAdapter = new DialogsListAdapter<>(R.layout.item_custom_dialog , CustomDialogViewHolder.class, super.imageLoader );
        super.dialogsAdapter.setItems(chats);
        super.dialogsAdapter.setOnDialogClickListener(this);
        super.dialogsAdapter.setOnDialogLongClickListener(this);
        dialogsList.setAdapter(super.dialogsAdapter);
    }

    //for example
    private void onNewMessage(String dialogId, Message message) {


        boolean isUpdated = dialogsAdapter.updateDialogWithMessage(dialogId, message);
        if (!isUpdated) {
            //Dialog with this ID doesn't exist, so you can create new Dialog or update all dialogs list
        }
        else {
            int unrmes =  GetUnrCount(dialogId);
            dialogsAdapter.getItemById(dialogId).setUnreadCount(unrmes);
             int ind = chats.indexOf(dialogsAdapter.getItemById(dialogId));
             chats.get(ind).setLastMessage(message);
            Save.saveDialogs(chats, ct);
        }

    }
    //for example
    private void onNewDialog(Dialog dialog) {

        dialogsAdapter.addItem(dialog);
    }

    class GetDialogs extends AsyncTask<Void, Void, String> {
        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;
        String resultJson = "";
        String   token= Profile_Info.getInstance().getToken();
        @Override
        protected String doInBackground(Void... params) {
            URL url = null;

            try {
                url = new URL("http://185.213.209.188/api/getchats/");
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
            chats= new ArrayList<>();
            try {
                kol = new JSONArray(response);
                for (int i = 0; i<kol.length() ; i++)
                {
                    JSONObject elem  = kol.getJSONObject(i);
                    Dialog dia = GetDialogFromJson(elem);
                    chats.add(dia);
                }
                Save.saveDialogs(chats, ct);
                initAdapter();
            }catch (JSONException ignored){
                String f = ignored.toString();
            }
         }
    }

    public void showFragment(Fragment fragment) {
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction
                .replace(R.id.fl_content, fragment)
                .commit();
    }
    public void closeFragment(Fragment fragment) {
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction
                .remove(this).commit();
    }


    @Override
    public void onBackPressed() {
        //super.onBackPressed();
    }



    ArrayList<Dialog> loadDialogs () {
        SharedPreferences   sPref = (SharedPreferences) getActivity().getSharedPreferences("DIALOGLIST" , Context.MODE_PRIVATE);
        String savedText = sPref.getString("dialogs", "");
        Gson gson = new Gson();
        ArrayList<Dialog> lstArrayList = gson.fromJson(savedText,
                new TypeToken<List<Dialog>>(){}.getType());
        return  lstArrayList;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        try {
            ct.unregisterReceiver(br);
        }catch (Exception e) {}
        mDroidNet.removeInternetConnectivityChangeListener(this);
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
        chats= loadDialogs();
        if (chats!=null){
            initAdapter();}
        GetDialogs mt = new GetDialogs();
        mt.execute();
    }
    private void netIsOff(){
        ContNameText.setVisibility(View.GONE);
        AddImage.setVisibility(View.GONE);
        WaitInt.setVisibility(View.VISIBLE);
        chats= loadDialogs();
        if (chats!=null){
            initAdapter();}
    }

    private Dialog GetDialogFromJson (JSONObject elem) {
        try {
            String name;
            String photo;
            String last_message = elem.getString("last_message");
            String date = elem.getString("date");
            Date date1 = null;
            try {
                date1 = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss").parse(date);
            } catch (ParseException e) {
                e.printStackTrace();
            }

            String last_message_type = elem.getString("last_message_type");
            String pk_chat = elem.getString("pk");
            JSONArray users = elem.getJSONArray("users");
            JSONObject pol1 = users.getJSONObject(0);
            JSONObject pol2 = users.getJSONObject(1);
            String rep_pk, last_activity;
            if (com.blackfish.a1pedal.ProfileInfo.User.getInstance().getPk().equals(pol1.getString("pk"))) {
                rep_pk = pol2.getString("pk");
                if (pol2.getString("type").equals("driver")) {
                    name = pol2.getString("brand") + " " + pol2.getString("model") + " " + pol2.getString("fio");
                    photo = pol2.getString("photo");
                    last_activity = pol2.getString("last_activity");
                } else {
                    name = pol2.getString("name");
                    photo = pol2.getString("photo");
                    last_activity = pol2.getString("last_activity");
                }
            } else {
                rep_pk = pol1.getString("pk");
                if (pol1.getString("type").equals("driver")) {
                    name = pol1.getString("brand") + " " + pol1.getString("model") + " " + pol1.getString("fio");
                    photo = pol1.getString("photo");
                    last_activity = pol1.getString("last_activity");
                } else {
                    name = pol1.getString("name");
                    photo = pol1.getString("photo");
                    last_activity = pol1.getString("last_activity");
                }

            }

            int unrmes =  GetUnrCount(pk_chat);

            if (!photo.equals("") && !photo.equals("1")) {
                photo = "http://185.213.209.188" + photo;
            } else {
                photo = "";
            }

            User us = new User(rep_pk, name, photo, false);
            us.setLastActivity(last_activity);
            JSONArray read = elem.getJSONArray("read");
            String st;
            //   int readCount = 0;
            if (read.length() > 1) {
                st = "Прочитано";
            } else {
                String k = read.getJSONObject(0).getString("pk");
                st = "Отправлено";
            }
            ArrayList<User> us1 = new ArrayList<>();
            us1.add(us);
            if (last_message_type.equals("video")) {
                last_message = "<Видео>";
            }
            if (last_message_type.equals("audio")) {
                last_message = "<Аудиозапись>";
            }
            if (last_message_type.equals("photo")) {
                last_message = "<Изображение>";
            }
            if (last_message_type.equals("location")) {
                last_message = "<Местоположение>";
            }
            Message mes =   new Message("1", us , last_message ,date1,st );
            return new Dialog(pk_chat, name , photo , us1 , mes , unrmes);
        } catch (JSONException ignored) {
            String f = ignored.toString();
            return null;
        }
    }
    private void GetUserFromJson (JSONObject elem) {
        try {
            JSONObject contains = elem.getJSONObject("contains");
            String ChatId = contains.getString("chat");
           if ( !dialogsAdapter.isEmpty() && dialogsAdapter.getItemById(ChatId)!=null)
           {
               Dialog d =   dialogsAdapter.getItemById(ChatId);
               Message mes = d.getLastMessage();
               User us = mes.getUser();
               String last_message_type = contains.getString("type");
               String last_message = contains.getString("content");

               if (last_message_type.equals("video")) {
                   last_message = "<Видео>";
               }
               if (last_message_type.equals("audio")) {
                   last_message = "<Аудиозапись>";
               }
               if (last_message_type.equals("photo")) {
                   last_message = "<Изображение>";
               }
               if (last_message_type.equals("location")) {
                   last_message = "<Местоположение>";
               }
              Message m11 = new Message("1", us , last_message ,new Date(),"Прочитано" );

               ArrayList<Chats.UnreadChats> uc = Chats.getInstance().getUnread_chats_count();

               onNewMessage (ChatId , m11);
           }
           else
           {

           }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public int GetUnrCount (String pk_chat){
        String unrmes = "0";
        ArrayList<Chats.UnreadChats> uc = Chats.getInstance().getUnread_chats_count();
        for (int g = 0; g <= uc.size() - 1; g++) {
            Chats.UnreadChats uc1 = uc.get(g);
            if (uc1.getChat_id().equals(pk_chat)) {
                unrmes = uc.get(g).getUnreadcount();
            }
        }
        return Integer.parseInt(unrmes);
    }
    }






