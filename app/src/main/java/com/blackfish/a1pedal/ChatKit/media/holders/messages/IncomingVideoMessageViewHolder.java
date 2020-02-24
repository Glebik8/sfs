package com.blackfish.a1pedal.ChatKit.media.holders.messages;

import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;

import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.VideoView;

import androidx.appcompat.app.AlertDialog;

import com.blackfish.a1pedal.ChatKit.model.Message;
import com.blackfish.a1pedal.ChatKit.model.User;
import com.blackfish.a1pedal.ProfileInfo.Chats;
import com.blackfish.a1pedal.R;
import com.blackfish.a1pedal.tools_class.videoplayer;
import com.bumptech.glide.Glide;
import com.devbrackets.android.exomedia.listener.OnCompletionListener;
import com.squareup.picasso.Picasso;
import com.stfalcon.chatkit.messages.MessageHolders;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


public class IncomingVideoMessageViewHolder
        extends MessageHolders.IncomingTextMessageViewHolder<Message> {

    private com.devbrackets.android.exomedia.ui.widget.VideoView videoView;
    TextView NameTextImage;
    Context ct;
    ImageView UserAvatar;
    ImageView PlayImage;
    public IncomingVideoMessageViewHolder(View itemView, Object payload) {
        super(itemView, payload);
        videoView = (com.devbrackets.android.exomedia.ui.widget.VideoView)itemView.findViewById(R.id.video_view);
        ct = itemView.getContext();
        NameTextImage= itemView.findViewById(R.id.NameTextImage);
        UserAvatar= itemView.findViewById(R.id.UserAvatar);
        PlayImage =  itemView.findViewById(R.id.PlayImage);

    }

    @Override
    public void onBind(Message message) {
        super.onBind(message);
        String  url = message.getVideo().getUrl();
        User us = message.getUser();
        String Avat = us.getAvatar();
        String Name1 = us.getName();
        if (Avat.equals("") || Avat.equals("1"))
        {    if (Name1.length()>2){
            Name1 = Name1.substring(0,2);}
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
        String []  st = url.split("/");
        String Name = st[st.length-1];
      /*  File path= Chats.getInstance().getPath();
        File path1 = new File(path+Name);
        if (!path1.exists()){
            try {
                downloadFileAsync(url,"content");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        else {
            Uri finalVideoUri = Uri.fromFile(path1);
            videoView.setVideoURI(finalVideoUri);
            videoView.setOnCompletionListener(new OnCompletionListener()
            {
                @Override
                public void onCompletion() {
                    videoView.setVideoURI(finalVideoUri);
                }
            });
        }*/
        PlayImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(ct, videoplayer.class);
                i.putExtra("url", url);
                ct.startActivity(i);
            }});
        bubble.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                // TODO Auto-generated method stub
                LayoutInflater li = LayoutInflater.from(itemView.getContext());
                View promptsView = li.inflate(R.layout.layout_long_click, null);
                androidx.appcompat.app.AlertDialog.Builder mDialogBuilder = new androidx.appcompat.app.AlertDialog.Builder(itemView.getContext(), R.style.CustomDialog);
                mDialogBuilder.setView(promptsView);
                TextView DownloadTextView = (TextView) promptsView.findViewById(R.id.DownloadTextView);
                AlertDialog alertDialog = mDialogBuilder.create();
                //     alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                alertDialog.show();
                DownloadTextView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        alertDialog.cancel();
                        DownloadManager downloadmanager = (DownloadManager) itemView.getContext().getSystemService(Context.DOWNLOAD_SERVICE);
                        Uri uri = Uri.parse(url);
                        DownloadManager.Request request = new DownloadManager.Request(uri);
                        request.setTitle(Name);
                        request.setDescription("Downloading");
                        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
                        request.setVisibleInDownloadsUi(false);
                        //   request.setDestinationUri(Uri.parse(Avat));
                        downloadmanager.enqueue(request);
                    }});
                return true;
            }
        });
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
                if (type.equals("content")) {
                    File path1 = new File(path + finalName);
                    Uri finalVideoUri = Uri.fromFile(path1);
                    videoView.setVideoURI(finalVideoUri);
                }
              /*  if (type.equals("avatar"))
                { File path1 = new File(path+ Name);
                    Glide.with(itemView).load(path1).into(messageUserAvatar);   }*/
            }
        });
    }
}
