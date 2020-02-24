package com.blackfish.a1pedal.ChatKit.media.holders.messages;

import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;

import com.blackfish.a1pedal.Auth_Registr.LoginActivity;
import com.blackfish.a1pedal.Auth_Registr.RememberPasswordActivity;
import com.blackfish.a1pedal.ChatKit.model.Message;
import com.blackfish.a1pedal.ProfileInfo.Chats;
import com.blackfish.a1pedal.R;
import com.blackfish.a1pedal.tools_class.GalleryActivity;
import com.bumptech.glide.Glide;
import com.squareup.picasso.Picasso;
import com.stfalcon.chatkit.messages.MessageHolders;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/*
 * Created by troy379 on 05.04.17.
 */
public class CustomOutcomingImageMessageViewHolder
        extends MessageHolders.OutcomingImageMessageViewHolder<Message> {
      ImageView ImageView;
ProgressBar progressBar2;
    public CustomOutcomingImageMessageViewHolder(View itemView, Object payload) {
        super(itemView, payload);
        ImageView = itemView.findViewById(R.id.image);
        progressBar2 = itemView.findViewById(R.id.progressBar2);
    }

    @Override
    public void onBind(Message message) {
        super.onBind(message);

        time.setText(message.getStatus() + " " + time.getText());
        String urlcont =message.getImageUrl();
        String []  st = urlcont.split("/");
        String Name = st[st.length-1];

        File path= Chats.getInstance().getPath();
        File path1 = new File(path+Name);
        long file_size = path1.length();
        if (!path1.exists() ||  file_size==0){
            try {
                downloadFileAsync(urlcont,"content");
                Picasso.get().load(urlcont).into(ImageView);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        else {
            Glide.with(itemView).load(path1).into(ImageView);

        }
        ImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(itemView.getContext(), GalleryActivity.class);
                intent.putExtra("path", path+Name);
                intent.putExtra("url", urlcont);
                itemView.getContext().startActivity(intent);
            }});
        ImageView.setOnLongClickListener(new View.OnLongClickListener() {
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
                        Uri uri = Uri.parse(urlcont);
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
    //Override this method to have ability to pass custom data in ImageLoader for loading image(not avatar).
    @Override
    protected Object getPayloadForImageLoader(Message message) {
        //For example you can pass size of placeholder before loading
        return new Pair<>(100, 100);
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
            }
        });
        }
}