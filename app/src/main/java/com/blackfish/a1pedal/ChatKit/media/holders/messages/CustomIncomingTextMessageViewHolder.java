package com.blackfish.a1pedal.ChatKit.media.holders.messages;

import android.net.Uri;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.blackfish.a1pedal.ChatKit.model.Message;
import com.blackfish.a1pedal.ChatKit.model.User;
import com.blackfish.a1pedal.ProfileInfo.Chats;
import com.blackfish.a1pedal.R;
import com.bumptech.glide.Glide;
import com.squareup.picasso.Picasso;
import com.stfalcon.chatkit.messages.MessageHolders;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URI;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


public class CustomIncomingTextMessageViewHolder
        extends MessageHolders.IncomingTextMessageViewHolder<Message> {

    private View onlineIndicator;
    TextView NameTextImage;
  ImageView UserAvatar;
    public CustomIncomingTextMessageViewHolder(View itemView, Object payload) {
        super(itemView, payload);
        onlineIndicator = itemView.findViewById(R.id.onlineIndicator);
        NameTextImage= itemView.findViewById(R.id.NameTextImage);
        UserAvatar= itemView.findViewById(R.id.UserAvatar);
    }

    @Override
    public void onBind(Message message) {
        super.onBind(message);

        User us = message.getUser();
        String Avat = us.getAvatar();
        String Name1 = us.getName();
        if (Avat.equals("") || Avat.equals("1"))
        {   if (Name1.length()>2)
            {Name1 = Name1.substring(0,2);}
            NameTextImage.setText(Name1);}
        else
        {   String []  st = Avat.split("/");
            String AvatName = st[st.length-1];
            File path= Chats.getInstance().getPath();
            File path1 = new File(path+AvatName);
            if (!path1.exists()){
                try {
                    downloadFileAsync(Avat,"avatar");
                    Picasso.get().load(Avat).into(UserAvatar);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            else{
                Glide.with(itemView).load(path1).into(UserAvatar);
            }
        }

      /*  boolean isOnline = message.getUser().isOnline();
        if (isOnline) {
            onlineIndicator.setBackgroundResource(R.drawable.shape_bubble_online);
        } else {
            onlineIndicator.setBackgroundResource(R.drawable.shape_bubble_offline);
        }

        //We can set click listener on view from payload
        final Payload payload = (Payload) this.payload;
        userAvatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (payload != null && payload.avatarClickListener != null) {
                    payload.avatarClickListener.onAvatarClick();
                }
            }
        });*/
    }

    public static class Payload {
        public OnAvatarClickListener avatarClickListener;
    }

    public interface OnAvatarClickListener {
        void onAvatarClick();
    }
    public void downloadFileAsync(final String downloadUrl , String type) throws Exception {
        String []  st = downloadUrl.split("/");
        String Name = st[st.length-1];
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url(downloadUrl).build();
        String finalName = Name;
        client.newCall(request).enqueue(new Callback() {
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            public void onResponse(Call call, Response response) throws IOException {
                if (!response.isSuccessful()) {
                    throw new IOException("Failed to download file: " + response);
                }
                File path= Chats.getInstance().getPath();
                FileOutputStream fos = new FileOutputStream( path+ finalName);
                fos.write(response.body().bytes());
                fos.close();
             /*   if (type.equals("avatar"))
                { File path1 = new File(path+ finalName);
                Glide.with(itemView).load(path1).into(messageUserAvatar);   } */        }
        });
    }
}
