package com.blackfish.a1pedal.ProfileInfo;

import com.blackfish.a1pedal.ChatKit.model.Dialog;
import com.blackfish.a1pedal.ChatKit.model.Message;

import java.util.ArrayList;

public class Cashe {
    public static class MessInDial {
        private  ArrayList<Message> messages ;
        public MessInDial(ArrayList<Message> messages) {
            this.messages = messages;
        }
    }

    public ArrayList<MessInDial> getMessInAllDial() {
        return MessInAllDial;
    }

    public void setMessInAllDial(ArrayList<MessInDial> messInAllDial) {
        MessInAllDial = messInAllDial;
    }

    private ArrayList<MessInDial> MessInAllDial;


    public ArrayList<Dialog> getAllDialogs() {
        return AllDialogs;
    }

    public void setAllDialogs(ArrayList<Dialog> allDialogs) {
        AllDialogs = allDialogs;
    }

    private ArrayList<Dialog> AllDialogs;





    private static Cashe dataObject = null;
    public static Cashe getInstance() {
        if (dataObject == null)
            dataObject = new Cashe();
        return dataObject;
    }
    public Cashe() {
    }


    }
