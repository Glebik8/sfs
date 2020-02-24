package com.blackfish.a1pedal.tools_class;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;

import com.blackfish.a1pedal.ChatKit.model.Dialog;
import com.blackfish.a1pedal.ChatKit.model.Message;
import com.blackfish.a1pedal.ChatKit.model.User;
import com.blackfish.a1pedal.ProfileInfo.Cashe;
import com.blackfish.a1pedal.ProfileInfo.Chats;
import com.blackfish.a1pedal.ProfileInfo.Profile_Info;

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

public class SaveInf
  {
      ArrayList<Dialog> chats = new ArrayList<>();

      public void SaveDialog ()
      {
          GetDialogs mt = new GetDialogs();
          mt.execute();
      }
      public void SaveAllMessage ()
      {
          GetDialogs mt = new GetDialogs();
          mt.execute();
      }


      private  class GetDialogs extends AsyncTask<Void, Void, String> {
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
                  JSONArray  kol = new JSONArray(response);
                  for (int i = 0; i<kol.length() ; i++)
                  {
                      JSONObject elem  = kol.getJSONObject(i);
                      String name ; String photo;
                      String last_message =  elem.getString("last_message");
                      String date =  elem.getString("date");
                      Date date1 = null;
                      try {
                          date1=new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss").parse(date);
                      } catch (ParseException e) {
                          e.printStackTrace();
                      }
                      String last_message_type =  elem.getString("last_message_type");
                      String pk_chat = elem.getString("pk");
                      JSONArray users  = elem.getJSONArray("users");
                      JSONObject pol1 = users.getJSONObject(0);
                      JSONObject pol2  = users.getJSONObject(1);
                      String rep_pk, last_activity;
                      if (com.blackfish.a1pedal.ProfileInfo.User.getInstance().getPk().equals(pol1.getString("pk")))
                      {
                          rep_pk = pol2.getString("pk");
                          if (pol2.getString("type").equals("driver")) {
                              name =pol2.getString("brand")+" "+ pol2.getString("model")+" "+ pol2.getString("fio");
                              photo = pol2.getString("photo");
                              last_activity = pol2.getString("last_activity");
                          }
                          else
                          {
                              name = pol2.getString("name");
                              photo = pol2.getString("photo");
                              last_activity = pol2.getString("last_activity");
                          }
                      }
                      else {
                          rep_pk = pol1.getString("pk");
                          if (pol1.getString("type").equals("driver")) {
                              name =pol1.getString("brand")+" "+ pol1.getString("model")+" "+ pol1.getString("fio");
                              photo = pol1.getString("photo");
                              last_activity = pol1.getString("last_activity");
                          }
                          else
                          {
                              name = pol1.getString("name");
                              photo = pol1.getString("photo");
                              last_activity = pol1.getString("last_activity");
                          }

                      }
                      String unrmes = "0";
                      ArrayList<Chats.UnreadChats> uc = Chats.getInstance().getUnread_chats_count();
                      for (int g =0 ; g<=uc.size()-1 ; g++)
                      {
                          Chats.UnreadChats uc1 = uc.get(g);
                          if ( uc1.getChat_id().equals(pk_chat) )
                          {unrmes =  uc.get(g).getUnreadcount();}
                      }

                      if (!photo.equals("") && !photo.equals("1")){photo = "http://185.213.209.188"+photo; }
                      else {photo="";
                      }

                      User us =  new User(rep_pk,name, photo , false);
                      us.setLastActivity(last_activity);
                      JSONArray read  = elem.getJSONArray("read");
                      String st;
                      //   int readCount = 0;
                      if (read.length()>1){
                          st = "Прочитано";
                      }
                      else {
                          String k = read.getJSONObject(0).getString("pk");
                          //    if (!com.blackfish.a1pedal.ProfileInfo.User.getInstance().getPk().equals(k)) {Integer.parseInt(unrmes);}
                          st = "Отправлено";}
                      ArrayList<User> us1 = new ArrayList<>();
                      us1.add(us);
                      if (last_message_type.equals("video")){
                          last_message = "<Видео>";
                      }
                      if (last_message_type.equals("audio")){
                          last_message = "<Аудиозапись>";
                      }
                      if (last_message_type.equals("photo")){
                          last_message = "<Изображение>";
                      }
                      if (last_message_type.equals("location")){
                          last_message = "<Местоположение>";
                      }
                      Message mes =   new Message("1", us , last_message ,date1,st );
                      chats.add(new Dialog(pk_chat, name , photo , us1 , mes , Integer.parseInt(unrmes)));


                  }
              }catch (JSONException ignored){
                  String f = ignored.toString();
              }
          }
      }
  }
