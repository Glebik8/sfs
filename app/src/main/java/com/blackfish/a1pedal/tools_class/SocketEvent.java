package com.blackfish.a1pedal.tools_class;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Binder;
import android.os.IBinder;
import android.widget.Toast;

import com.blackfish.a1pedal.ChatKit.media.DefaultDialogsActivity;
import com.blackfish.a1pedal.ProfileInfo.Chats;
import com.blackfish.a1pedal.ProfileInfo.User;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.URI;
import java.net.URISyntaxException;

import tech.gusavila92.websocketclient.WebSocketClient;

public class SocketEvent extends Service {

    String acc_email;
    private WebSocketClient webSocketClient;
    public class LocalBinder extends Binder {
        SocketEvent getService() {
            return SocketEvent.this;
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (webSocketClient!=null)
        {
            webSocketClient.close();
            boolean t = false;
            while (!t){
                try {
                    webSocketClient.disableAutomaticReconnection();
                    t=true;
                }catch (Exception e ) {
                    webSocketClient.close();
                    t=false;
                }
            }
        }
        if (User.getInstance().getPk() != null)
        {
            createWebSocketClient();
        }

        return super.onStartCommand(intent, flags, startId);

    }

    @Override
    public IBinder onBind(Intent intent) {
        return new Binder();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        webSocketClient.close();
        boolean t = false;
        while (!t){
            try {
                webSocketClient.disableAutomaticReconnection();
                t=true;
            }catch (Exception e ) {
                webSocketClient.close();
                t=false;
            }


        }



    }



    private void createWebSocketClient() {
        URI uri;
        try {
            uri = new URI("ws://185.213.209.188:8881/?room="+User.getInstance().getPk());
        }
        catch (URISyntaxException e) {
            e.printStackTrace();
            return;
        }

        webSocketClient = new WebSocketClient(uri) {
            @Override
            public void onOpen() {
                System.out.println("onOpen");
              //  webSocketClient.send("Hello, World!");
            }

            @Override
            public void onTextReceived(String message) {
                System.out.println("onTextReceived");
                Intent intent = new Intent(DefaultDialogsActivity.BROADCAST_ACTION);
                // сообщаем о старте задачи
                if (!message.contains("Socket"))
                {
                intent.putExtra("Message", message);
                sendBroadcast(intent);}
            }
            @Override
            public void onBinaryReceived(byte[] data) {
                System.out.println("onBinaryReceived");
            }

            @Override
            public void onPingReceived(byte[] data) {
                System.out.println("onPingReceived");
            }

            @Override
            public void onPongReceived(byte[] data) {
                System.out.println("onPongReceived");
            }

            @Override
            public void onException(Exception e) {
                System.out.println(e.getMessage());
                if ( Chats.getInstance().getExit().equals("true"))
                { webSocketClient.close();
                    boolean t = false;
                    while (!t){
                        try {
                            webSocketClient.disableAutomaticReconnection();
                            t=true;
                        }catch (Exception er ) {
                            webSocketClient.close();
                            t=false;
                        }
                }}

            }

            @Override
            public void onCloseReceived()
            {
                System.out.println("onCloseReceived");
            }
        };
        webSocketClient.setConnectTimeout(10000);
        webSocketClient.setReadTimeout(60000);
        webSocketClient.enableAutomaticReconnection(5000);
        webSocketClient.connect();
    }



}

