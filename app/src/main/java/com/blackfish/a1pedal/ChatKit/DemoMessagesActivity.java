package com.blackfish.a1pedal.ChatKit;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.blackfish.a1pedal.ChatKit.media.CustomMediaMessagesActivity;
import com.blackfish.a1pedal.ChatKit.media.DefaultDialogsActivity;
import com.blackfish.a1pedal.ChatKit.model.Message;
import com.blackfish.a1pedal.ChatKit.model.User;
import com.blackfish.a1pedal.ChatKit.utils.AppUtils;
import com.blackfish.a1pedal.ProfileInfo.Chats;
import com.blackfish.a1pedal.ProfileInfo.Profile_Info;
import com.blackfish.a1pedal.R;
import com.blackfish.a1pedal.tools_class.Save;
import com.blackfish.a1pedal.tools_class.SocketEvent;
import com.droidnet.DroidListener;
import com.droidnet.DroidNet;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.squareup.picasso.Picasso;
import com.stfalcon.chatkit.commons.ImageLoader;
import com.stfalcon.chatkit.messages.MessagesListAdapter;

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
import java.util.Locale;
import java.util.Objects;

import static com.google.android.material.internal.ContextUtils.getActivity;

/*
 * Created by troy379 on 04.04.17.
 */
public abstract class DemoMessagesActivity extends AppCompatActivity
        implements MessagesListAdapter.SelectionListener,
        MessagesListAdapter.OnLoadMoreListener, DroidListener {
    public final static String BROADCAST_ACTION ="com.blackfish.a1pedal.ChatKit.media";
  public BroadcastReceiver br;
    String last_mes_activ;
    private DroidNet mDroidNet;
    String rep_avat;
    String total_count = "5";
    String start_count ="0";
    String end_count = "15";
    int intr = 0;
    protected final String senderId = "0";
    protected ImageLoader imageLoader;
    protected MessagesListAdapter<Message> messagesAdapter;

    private Menu menu;
    private int selectionCount;
    private Date lastLoadedDate;
    ArrayList<Message> messages = new ArrayList<>();
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        imageLoader = new ImageLoader() {
            @Override
            public void loadImage(ImageView imageView, String url, Object payload) {
        //        Picasso.get().load(url).into(imageView);
            }
        };
    }

    @Override
    protected void onStart() {
        super.onStart();
        mDroidNet = DroidNet.getInstance();
        mDroidNet.addInternetConnectivityListener(this);


    //    messagesAdapter.addToStart(new Message(p), true);
        last_mes_activ= Chats.getInstance().getLastActivity();
        br = new BroadcastReceiver() {
            // действия при получении сообщений
            public void onReceive(Context context, Intent intent) {
               if (Chats.getInstance().getActual_activity().equals("1")){
                String message = intent.getStringExtra("Message");
                String f = message;
                JSONObject r= null;
                try {
                    r = new JSONObject(message);
                    JSONObject m= r.getJSONObject("message");
                    String info = m.getString("info");
                    if (info.equals("NewMessage"))
                    {
                        JSONObject c= m.getJSONObject("contains");
                         String chat = c.getString("chat");
                         if (chat.equals(Chats.getInstance().getChat_id()))
                         {
                        String type = c.getString("type");
                        String content = c.getString("content");
                        JSONObject sen_mes = c.getJSONObject("sender");
                        String last = sen_mes.getString("last_activity");
                        if (!last.equals(last_mes_activ)) {
                            last_mes_activ=last;
                        Chats.getInstance().setLastActivity(last);
                        String typeSend = sen_mes.getString("type");
                        String name;
                        if (typeSend.equals("service")){
                            name = sen_mes.getString("name");}
                        else {  name = sen_mes.getString("fio");}


                      User  us  =  new User("1",name, rep_avat , false);
                        String st;

                        Message mes;

                        mes = new Message("1", us, content,"");

                        if (type.equals("photo")) {
                            mes = new Message("1", us,null ,"");
                            mes.setImage(new Message.Image("http://185.213.209.188"+content));
                        }
                        if (type.equals("video"))
                        {
                            mes = new Message("1", us,null , "");
                            mes.setVideo(new Message.Video("http://185.213.209.188"+content));
                        }
                        if (type.equals("audio"))
                        {   mes = new Message("1", us,null ,"");
                            mes.setVoice(new Message.Voice("http://185.213.209.188"+content, 00));}
                        if (type.equals("location"))
                        {
                            mes = new Message("1", us,null ,"");
                            String []  st2 = content.split(",");
                            mes.setGeopoint(new Message.Geopoint(st2[0],st2[1]));
                        }
                        messagesAdapter.addToStart(mes, true);
                      /*  GetMessage mt = new GetMessage();
                        mt.execute();*/
                        CustomMediaMessagesActivity.SetReadAll mt2 = new CustomMediaMessagesActivity.SetReadAll();
                        mt2.execute();
                    }}}
                    if (info.equals("userTyping"))
                    {
                        JSONObject c= m.getJSONObject("contains");
                        String typing = c.getString("typing");
                        String user_pk = c.getString("user_pk");
                        if (user_pk.equals( Chats.getInstance().getRecipient_id())){
                        if (typing.equals("true") || typing.equals("1"))
                        {
                        TextView LastActivyText = ((CustomMediaMessagesActivity)DemoMessagesActivity.this).findViewById(R.id.LastActivyText);
                            LinearLayout ShowTypingView = ((CustomMediaMessagesActivity)DemoMessagesActivity.this).findViewById(R.id.ShowTypingView);
                            LastActivyText.setVisibility(View.GONE);
                            ShowTypingView.setVisibility(View.VISIBLE);
                            Handler handler = new Handler();
                            handler.postDelayed(new Runnable() {
                                public void run() {
                                    ShowTypingView.setVisibility(View.GONE);
                                    LastActivyText.setVisibility(View.VISIBLE);
                                }
                            }, 3000); //specify the number of milliseconds
                        }
                        if (typing.equals("false")){
                    /*        TextView LastActivyText = ((CustomMediaMessagesActivity)DemoMessagesActivity.this).findViewById(R.id.LastActivyText);
                            LinearLayout ShowTypingView = ((CustomMediaMessagesActivity)DemoMessagesActivity.this).findViewById(R.id.ShowTypingView);*/

                        }
                    }}
            } catch (JSONException e) {
                e.printStackTrace();
            } }
            }
        };

        IntentFilter intFilt = new IntentFilter(BROADCAST_ACTION);
        // регистрируем (включаем) BroadcastReceiver
        getApplicationContext().registerReceiver(br, intFilt);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        this.menu = menu;
        getMenuInflater().inflate(R.menu.chat_actions_menu, menu);
        onSelectionChanged(0);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_delete:
          //      messagesAdapter.deleteSelectedMessages();
                break;
            case R.id.action_copy:
           /*     messagesAdapter.copySelectedMessagesText(this, getMessageStringFormatter(), true);
                AppUtils.showToast(this, R.string.copied_message, true);*/
                break;
        }
        return true;
    }

    @Override
    public void onBackPressed() {
        if (selectionCount == 0) {
            super.onBackPressed();
        } else {
            messagesAdapter.unselectAllItems();
        }
    }

   /* @Override
    public void onInternetConnectivityChanged(boolean isConnected) {

        if (isConnected) {
            //do Stuff with internet
            netIsOn();
        } else {
            //no internet
            netIsOff();

        }
    }*/
    @Override
    public void onDestroy() {
        super.onDestroy();
        if (br!=null){
           try {
               unregisterReceiver(br);
           }catch (Exception e) {}
        }
        mDroidNet.removeInternetConnectivityChangeListener(this);
    }

    public void netIsOn(){
        intr = 1;
        if (Chats.getInstance().getChat_id() != null && !Chats.getInstance().getChat_id().equals("newDialog"))
        {
            messages = Save.loadMessages(Chats.getInstance().getChat_id() , DemoMessagesActivity.this);
            if (messages!=null){
                if (messages.size()>14)
                {
                    messages.subList(14, messages.size()-1).clear();
                }
                messagesAdapter.clear();
                messagesAdapter.addToEnd(messages, false);
            }
           }
        messages =  new ArrayList<>();
        GetMessage mt = new GetMessage();
        mt.execute();
    }
    public void netIsOff(){
        intr = 0;
        if (!Chats.getInstance().getChat_id().equals("newDialog"))
        {
            messages = Save.loadMessages(Chats.getInstance().getChat_id() , DemoMessagesActivity.this);
        if (messages!=null){
            if (messages.size()>14)
            {
                messages.subList(14, messages.size()-1).clear();
            }
            messagesAdapter.clear();
            messagesAdapter.addToEnd(messages, false);
        }
        }

      }


    @Override
    public void onLoadMore(int page, int totalItemsCount) {

        int t = Integer.parseInt(total_count);

        if (Integer.parseInt(total_count)>Integer.parseInt(end_count))
        {   end_count = String.valueOf(Integer.parseInt(end_count)+15);
            GetMessage mt = new GetMessage();
            mt.execute();
        }
    }

    @Override
    public void onSelectionChanged(int count) {
        this.selectionCount = count;
       /* menu.findItem(R.id.action_delete).setVisible(count > 0);
        menu.findItem(R.id.action_copy).setVisible(count > 0);*/
    }

    protected void loadMessages() {
             /*   ArrayList<Message> messages = MessagesFixtures.getMessages(lastLoadedDate);
                lastLoadedDate = messages.get(messages.size() - 1).getCreatedAt();
                messagesAdapter.addToEnd(messages, false);*/

    }

    private MessagesListAdapter.Formatter<Message> getMessageStringFormatter() {
        return new MessagesListAdapter.Formatter<Message>() {
            @Override
            public String format(Message message) {
                String createdAt = new SimpleDateFormat("MMM d, EEE 'at' h:mm a", Locale.getDefault())
                        .format(message.getCreatedAt());

                String text = message.getText();
                if (text == null) text = "[attachment]";

                return String.format(Locale.getDefault(), "%s: %s (%s)",
                        message.getUser().getName(), text, createdAt);
            }
        };
    }


    private  class GetMessage extends AsyncTask<Void, Void, String> {
        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;
        String resultJson = "";
        String   token= Profile_Info.getInstance().getToken();
        String pk = "";
        @Override
        protected String doInBackground(Void... params) {
            URL url = null;
            try {
                url = new URL("http://185.213.209.188/api/getmessages/"+ Chats.getInstance().getChat_id() +"?start="+start_count+"&count="+end_count);
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
                messages= new ArrayList<>();
                JSONObject r= new JSONObject(response);
               total_count = r.getString("count_messages");
                JSONObject sender = r.getJSONObject("sender");
                String sender_avat =  sender.getString("photo");
                if (!sender_avat.equals("") && !sender_avat.equals("1") )
                {sender_avat = "http://185.213.209.188"+sender_avat; }
                else {sender_avat="";
                }
                String sender_pk = sender.getString("pk");

                JSONObject repacient = r.getJSONObject("recipient");
                rep_avat =  repacient.getString("photo");
                String rep_pk = repacient.getString("pk");
                if (!rep_avat.equals("") && !rep_avat.equals("1") ){rep_avat = "http://185.213.209.188"+rep_avat; }
                else {rep_avat="";}

                JSONArray mess = r.getJSONArray("messages");
                User us;
                Message mes1 = null;
                for (int i = mess.length()-1; i>=0 ; i--)
                {
                    JSONObject elem  = mess.getJSONObject(i);
                    String type = elem.getString("type");
                    String content = elem.getString("content");
                    String date = elem.getString("date");
                    JSONObject sen_mes = elem.getJSONObject("sender");

                    String pk_mes = sen_mes.getString("pk");
                    String name;

                    if (pk_mes.equals(sender_pk)) {
                        if (sen_mes.getString("type").equals("driver")) {
                            name = sen_mes.getString("fio");
                            us = new User("0", name,  sender_avat, false);
                        } else
                        {name = sen_mes.getString("name");
                            us = new User("0", name,  sender_avat, false);}
                    }
                    else {
                        if (sen_mes.getString("type").equals("driver")) {
                            name = sen_mes.getString("fio");
                            us  =  new User("1",name, rep_avat , false);
                        } else
                        {name = sen_mes.getString("name");
                            us  =  new User("1",name, rep_avat , false);}
                    }
                    Date date1 = null;
                    try {
                        date1=new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss").parse(date);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    JSONArray read  = elem.getJSONArray("read");
                    String st;
                    if (read.length()>1){ st = "Прочитано"; } else { st = "Отправлено";}
                    Message mes;

                        mes = new Message("1", us, content, date1, st);

                    if (type.equals("photo")) {
                        mes = new Message("1", us,null, date1, st);
                        mes.setImage(new Message.Image("http://185.213.209.188"+content));
                    }
                    if (type.equals("video"))
                    {
                        mes = new Message("1", us,null, date1, st);
                        mes.setVideo(new Message.Video("http://185.213.209.188"+content));
                    }
                    if (type.equals("audio"))
                    {   mes = new Message("1", us,null, date1, st);
                        mes.setVoice(new Message.Voice("http://185.213.209.188"+content, 00));}
                    if (type.equals("location"))
                    {
                        mes = new Message("1", us,null, date1, st);
                        String []  st2 = content.split(",");
                        mes.setGeopoint(new Message.Geopoint(st2[0],st2[1]));
                    }
                      messages.add(mes);
                  if (i==mess.length()-1){ mes1 = mes;}
                }
                messagesAdapter.clear();
     //           messagesAdapter.addToStart(mes1, true);
           //   if (Integer.parseInt(total_count)<Integer.parseInt(end_count)){
                Save.saveMessages(messages, DemoMessagesActivity.this , Chats.getInstance().getChat_id());
                messagesAdapter.addToEnd(messages, false);
            //  }
            }catch (JSONException ignored){
                String f = ignored.toString();
            }
        }
    }
}
