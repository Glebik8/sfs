package com.blackfish.a1pedal.tools_class;

import android.content.Context;
import android.content.SharedPreferences;

import com.blackfish.a1pedal.ChatKit.model.Dialog;
import com.blackfish.a1pedal.ChatKit.model.Message;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Save {

    public static void ProfJson(JSONObject json1 , Context ct) {
        if (json1!=null ){
            SharedPreferences sPref = (SharedPreferences) Objects.requireNonNull(ct).getSharedPreferences("PROFILE" , Context.MODE_PRIVATE);
            SharedPreferences.Editor ed;
            String json = json1.toString();
            json = json.replace("null","\"null\"");
            ed = sPref.edit();
            ed.remove("profile").apply();
            ed.putString("profile", json);
            ed.commit();
        }
    }

    public static void saveDialogs(ArrayList<Dialog> dialogs , Context ct) {
        if (dialogs!=null && ct!=null){
            SharedPreferences sPref = (SharedPreferences) Objects.requireNonNull(ct).getSharedPreferences("DIALOGLIST" , Context.MODE_PRIVATE);
            SharedPreferences.Editor ed;
            Gson gson = new Gson();
            String json = gson.toJson(dialogs);
            ed = sPref.edit();
            ed.remove("dialogs").apply();
            ed.putString("dialogs", json);
            ed.commit();
        }
    }

    public static void saveMessages(ArrayList<Message> messages ,Context ct , String id ) {
        if (messages!=null){
            SharedPreferences sPref = (SharedPreferences) Objects.requireNonNull(ct).getSharedPreferences("MESSAGESSLIST" , Context.MODE_PRIVATE);
            SharedPreferences.Editor ed;
            Gson gson = new Gson();
            String json = gson.toJson(messages);
            ed = sPref.edit();
            ed.remove("messages"+id).apply();
            ed.putString("messages"+id, json);
            ed.commit();
        }
    }
    public static ArrayList<Message> loadMessages (String id , Context ct) {
        SharedPreferences sPref = (SharedPreferences) ct.getSharedPreferences("MESSAGESSLIST" , Context.MODE_PRIVATE);
        String savedText = sPref.getString("messages"+id, "");
        Gson gson = new Gson();
        ArrayList<Message> lstArrayList = gson.fromJson(savedText,
                new TypeToken<List<Message>>(){}.getType());
        return  lstArrayList;
    }

    public static void saveTotal_Unread(String t, Context ct ) {
        SharedPreferences sPref = (SharedPreferences) ct.getSharedPreferences("TOTAL_UNREAD" , Context.MODE_PRIVATE);
        SharedPreferences.Editor ed = sPref.edit();
        ed.putString("total_unread", t);
        ed.commit();
    }
    public static String loadTotal_Unread (Context ct) {
        SharedPreferences  sPref = (SharedPreferences) ct.getSharedPreferences("TOTAL_UNREAD" , Context.MODE_PRIVATE);
        String savedText = sPref.getString("total_unread", "");
        return  savedText;
    }

}
