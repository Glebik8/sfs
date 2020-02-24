package com.blackfish.a1pedal.ChatKit.media.holders.dialogs;

import android.content.Context;
import android.graphics.Color;
import android.text.format.Formatter;
import android.view.View;
import android.widget.TextView;

import com.blackfish.a1pedal.ChatKit.model.Dialog;
import com.blackfish.a1pedal.ChatKit.model.Message;
import com.blackfish.a1pedal.ChatKit.model.User;
import com.blackfish.a1pedal.ProfileInfo.Chats;
import com.blackfish.a1pedal.R;
import com.bumptech.glide.Glide;
import com.squareup.picasso.Picasso;
import com.stfalcon.chatkit.dialogs.DialogsListAdapter;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


/*
 * Created by Anton Bevza on 1/18/17.
 */
public class CustomDialogViewHolder
        extends DialogsListAdapter.DialogViewHolder<Dialog> {

    private View onlineIndicator;
TextView NameTextImage, dialogUnreadBubble;
Context ct;
    de.hdodenhof.circleimageview.CircleImageView dialogAvatar;
    public CustomDialogViewHolder(View itemView) {
        super(itemView);
        dialogAvatar= itemView.findViewById(R.id.dialogAvatar);
        onlineIndicator = itemView.findViewById(R.id.onlineIndicator);
        NameTextImage= itemView.findViewById(R.id.NameTextImage);
        dialogUnreadBubble= itemView.findViewById(R.id.dialogUnreadBubble);
        ct = itemView.getContext();
    }

    @Override
    public void onBind(Dialog dialog) {
        super.onBind(dialog);
        ArrayList<User> k =    dialog.getUsers();
        User us = k.get(0);
        String Avat =  us.getAvatar();
        String Name = us.getName();
        if (Avat.equals("") || Avat.equals("1"))
        {
            if (Name.length()>2){
            Name = Name.substring(0,2);}
            NameTextImage.setText(Name);
            dialogAvatar.setImageResource(R.color.foobar);
        }
        else
        {   NameTextImage.setText("");
            String []  st = Avat.split("/");
            String AvatName = st[st.length-1];
            File path= Chats.getInstance().getPath();
            File path1 = new File(path+AvatName);
            long file_size = path1.length();
            if (!path1.exists() ||  file_size==0){
                try {
                    downloadFileAsync(Avat,"avatar");
                    Picasso.get().load(Avat).into(dialogAvatar);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            else{
                Glide.with(itemView).load(path1).dontAnimate().into(dialogAvatar);
            }
        }

        Message d = dialog.getLastMessage();
        if (!d.getStatus().equals("Прочитано") && d.getUser().getId().equals(com.blackfish.a1pedal.ProfileInfo.User.getInstance().getPk()))
        {
            dialogUnreadBubble.setText(" ");
        }
      /*  if (dialog.getUsers().size() > 1) {
            onlineIndicator.setVisibility(View.GONE);
        } else {
            boolean isOnline = dialog.getUsers().get(0).isOnline();
            onlineIndicator.setVisibility(View.VISIBLE);
            if (isOnline) {
                onlineIndicator.setBackgroundResource(R.drawable.shape_bubble_online);
            } else {
                onlineIndicator.setBackgroundResource(R.drawable.shape_bubble_offline);
            }
        }*/
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
                  /*  if (type.equals("avatar"))
                    { File path1 = new File(path+ finalName);
                        Glide.with(itemView).load(path1).into(messageUserAvatar);   }*/

            }
        });
    }
}
