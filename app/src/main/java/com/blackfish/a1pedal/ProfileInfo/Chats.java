package com.blackfish.a1pedal.ProfileInfo;

import java.io.File;
import java.util.ArrayList;

public class Chats {
    private String name;
    private String image;
    private String model;
    private String date;
    private String message;
    private String chat_id;
    private String typingB;
    private String actual_activity;
    private String total_unread;

    public String getExit() {
        return exit;
    }

    public void setExit(String exit) {
        this.exit = exit;
    }

    private String exit;

    public String getMy_loction_lat_lan() {
        return my_loction_lat_lan;
    }

    public void setMy_loction_lat_lan(String my_loction_lat_lan) {
        this.my_loction_lat_lan = my_loction_lat_lan;
    }

    private String my_loction_lat_lan;




    public String getTypingB() {
        return typingB;
    }

    public void setTypingB(String typingB) {
        this.typingB = typingB;
    }


    public String getActual_activity() {
        return actual_activity;
    }

    public void setActual_activity(String actual_activity) {
        this.actual_activity = actual_activity;
    }



    public String getTotal_unread() {
        return total_unread;
    }

    public void setTotal_unread(String total_unread) {
        this.total_unread = total_unread;
    }



    public ArrayList<UnreadChats> getUnread_chats_count() {
        return unread_chats_count;
    }

    public void setUnread_chats_count(ArrayList<UnreadChats> unread_chats_count) {
        this.unread_chats_count = unread_chats_count;
    }

    private ArrayList<UnreadChats> unread_chats_count;

    public String getTittle_mess() {
        return tittle_mess;
    }

    public void setTittle_mess(String tittle_mess) {
        this.tittle_mess = tittle_mess;
    }

    private String tittle_mess;

    public String getLastActivity() {
        return lastActivity;
    }

    public void setLastActivity(String lastActivity) {
        this.lastActivity = lastActivity;
    }

    private String lastActivity;

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }

    private String count;
    public String getVoice_patch() {
        return voice_patch;
    }

    public void setVoice_patch(String voice_patch) {
        this.voice_patch = voice_patch;
    }

    private String voice_patch;

    public File getPath() {
        return path;
    }

    public void setPath(File path) {
        this.path = path;
    }

    File path;


    private String recipient_id;


    private static Chats dataObject = null;

    public static Chats getInstance() {
        if (dataObject == null)
            dataObject = new Chats();
        return dataObject;
    }
    public Chats() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getChat_id() {
        return chat_id;
    }

    public void setChat_id(String chat_id) {
        this.chat_id = chat_id;
    }
    public String getRecipient_id() {
        return recipient_id;
    }

    public void setRecipient_id(String recipient_id) {
        this.recipient_id = recipient_id;
    }

    public static class UnreadChats {
        private String chat_id;
        private String unreadcount;

        public UnreadChats(String chat_id , String unreadcount) {
            this.chat_id = chat_id;
            this.unreadcount = unreadcount;
        }
        public String getChat_id() {
            return chat_id;
        }

        public String getUnreadcount() {
            return unreadcount;
        }

    }


}
