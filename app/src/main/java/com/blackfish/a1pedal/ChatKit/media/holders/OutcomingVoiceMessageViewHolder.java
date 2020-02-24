package com.blackfish.a1pedal.ChatKit.media.holders;

import android.media.MediaPlayer;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.blackfish.a1pedal.ChatKit.model.Message;
import com.blackfish.a1pedal.ChatKit.utils.FormatUtils;
import com.blackfish.a1pedal.ProfileInfo.Chats;
import com.blackfish.a1pedal.R;
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

/*
 * Created by troy379 on 05.04.17.
 */
public class OutcomingVoiceMessageViewHolder
        extends MessageHolders.OutcomingTextMessageViewHolder<Message> {

    private TextView tvTime;
    VoicePlayerView voicePlayerView;
    public OutcomingVoiceMessageViewHolder(View itemView, Object payload) {
        super(itemView, payload);
        tvTime = (TextView) itemView.findViewById(R.id.time);
        voicePlayerView  = itemView.findViewById(R.id.voicePlayerView);
        voicePlayerView.setShareButtonVisibility( false);
    }

    @Override
    public void onBind(Message message) {
        super.onBind(message);

        String  url = message.getVoice().getUrl();

        String []  st = url.split("/");
        String Name = st[st.length-1];

        File path= Chats.getInstance().getPath();
        File path1 = new File(path+Name);
        if (!path1.exists()){
            try {
                downloadFileAsync(url);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        else {
            String AudioSavePathInDevice = path+Name;
            voicePlayerView.setAudio(AudioSavePathInDevice);
        }
        tvTime.setText(message.getStatus() + " " +DateFormatter.format(message.getCreatedAt(), DateFormatter.Template.TIME));


    }

    public void downloadFileAsync(final String downloadUrl) throws Exception {
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
               String AudioSavePathInDevice = path+Name;
                voicePlayerView.setAudio(AudioSavePathInDevice);

            }
        });
    }
}
