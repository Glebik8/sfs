package com.blackfish.a1pedal.ChatKit.media.holders;

import android.media.MediaPlayer;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.blackfish.a1pedal.ChatKit.media.CustomMediaMessagesActivity;
import com.blackfish.a1pedal.ChatKit.model.Message;
import com.blackfish.a1pedal.ChatKit.model.User;
import com.blackfish.a1pedal.ChatKit.utils.FormatUtils;
import com.blackfish.a1pedal.ProfileInfo.Chats;
import com.blackfish.a1pedal.R;
import com.bumptech.glide.Glide;
import com.squareup.picasso.Picasso;
import com.stfalcon.chatkit.messages.MessageHolders;

import com.stfalcon.chatkit.utils.DateFormatter;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import me.jagar.chatvoiceplayerlibrary.VoicePlayerView;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


public class IncomingVoiceMessageViewHolder
        extends MessageHolders.IncomingTextMessageViewHolder<Message> {
    MediaPlayer mediaPlayer;
    TextView NameTextImage;
    ImageView UserAvatar;
    private TextView tvTime;
    VoicePlayerView voicePlayerView;
    public IncomingVoiceMessageViewHolder(View itemView, Object payload) {
        super(itemView, payload);
        tvTime = (TextView) itemView.findViewById(R.id.time);
        voicePlayerView  = itemView.findViewById(R.id.voicePlayerView);
        voicePlayerView.setShareButtonVisibility( false);
        NameTextImage= itemView.findViewById(R.id.NameTextImage);
        UserAvatar= itemView.findViewById(R.id.UserAvatar);

    }

    @Override
    public void onBind(Message message) {
        super.onBind(message);
        String  url = message.getVoice().getUrl();
        User us = message.getUser();
        String Avat = us.getAvatar();
        String Name1 = us.getName();
        if (Avat.equals("") || Avat.equals("1"))
        {  if (Name1.length()>2){
            Name1 = Name1.substring(0,2);}
            NameTextImage.setText(Name1);}
        else
        {   String []  st = Avat.split("/");
            String AvatName = st[st.length-1];
            File path= Chats.getInstance().getPath();
            File path1 = new File(path+AvatName);
            if (!path1.exists()){
                try {
                    Picasso.get().load(Avat).into(UserAvatar);
                    downloadFileAsync(Avat,"avatar");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            else{
                Glide.with(itemView).load(path1).into(UserAvatar);
            }
        }

        String []  st = url.split("/");
        String Name = st[st.length-1];

        File path= Chats.getInstance().getPath();
        File path1 = new File(path+Name);
        if (!path1.exists()){
            try {
                downloadFileAsync(url,"content");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        else {
            String AudioSavePathInDevice = path+Name;
            voicePlayerView.setAudio(AudioSavePathInDevice);
        }

     /*   String k =  message.getStatus();
        tvTime.setText(k);*/
        tvTime.setText(DateFormatter.format(message.getCreatedAt(), DateFormatter.Template.TIME));


    }
    public void downloadFileAsync(final String downloadUrl , String type) throws Exception {
       String []  st = downloadUrl.split("/");
       String Name = st[st.length-1];
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url(downloadUrl).build();
        client.newCall(request).enqueue(new Callback() {
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            public void onResponse(Call call, Response response) throws IOException {
                if (!response.isSuccessful()) {
                    throw new IOException("Failed to download file: " + response);
                }
                File path= Chats.getInstance().getPath();
                FileOutputStream fos = new FileOutputStream( path+Name);
                fos.write(response.body().bytes());
                fos.close();
                if (type.equals("content"))
                {String AudioSavePathInDevice = path+Name;
                voicePlayerView.setAudio(AudioSavePathInDevice);}
           /*     if (type.equals("avatar"))
                { File path1 = new File(path+ Name);
                    Glide.with(itemView).load(path1).into(messageUserAvatar);   }*/
            }
        });
    }
}
