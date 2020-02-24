package com.blackfish.a1pedal.tools_class;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.blackfish.a1pedal.ChatKit.DemoMessagesActivity;
import com.blackfish.a1pedal.ChatKit.media.CustomMediaMessagesActivity;
import com.blackfish.a1pedal.ChatKit.model.User;
import com.blackfish.a1pedal.InformationActivity;
import com.blackfish.a1pedal.ProfileInfo.Chats;
import com.blackfish.a1pedal.ProfileInfo.FriendList;
import com.blackfish.a1pedal.Profile_Fragment;
import com.blackfish.a1pedal.R;
import com.bumptech.glide.Glide;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class DataApdaterFriend extends RecyclerView.Adapter<DataApdaterFriend.ViewHolder> {
    private Context activity;
    private LayoutInflater inflater;
    private List<FriendList> friendLists;
    public static int currentPosition;

    public DataApdaterFriend(Context context, List<FriendList> friendList) {
        activity=context;
        this.friendLists = friendList;
        this.inflater = LayoutInflater.from(context);
    }


    @Override
    public DataApdaterFriend.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.friend_element, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(DataApdaterFriend.ViewHolder holder, int position) {
        FriendList friendList = friendLists.get(position);
        if (!friendList.getImage().equals("") &&  !friendList.getImage().equals("1") ) {
            String []  st = friendList.getImage().split("/");
            String AvatName = st[st.length-1];
            File path= Chats.getInstance().getPath();
            File path1 = new File(path+AvatName);
            if (!path1.exists()){
                try {
                    downloadFileAsync(friendList.getImage(),"avatar");
                    Picasso.get().load(friendList.getImage()).into(holder.imageView);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            else{
                Glide.with(activity).load(path1).into(holder.imageView);
            }
        }
        else
        {
            String name_im = friendList.getName();
            if (name_im.length()>2){
            name_im = name_im.substring(0,2);}
            holder.NameTextImage.setText(name_im);
            holder.imageView.setImageResource(R.color.foobar);
        }
        if (friendList.getType().equals("driver"))
        {
        holder.nameView.setText(friendList.getModel());
        holder.MessageTextView.setText(friendList.getName());

        }
        else
        {
            holder.nameView.setText(friendList.getName());
            holder.MessageTextView.setText(friendList.getWork());

        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Chats.getInstance().setChat_id("newDialog");
                String rep = friendList.getid();
                if (friendList.getType().equals("driver")) {
                    Chats.getInstance().setTittle_mess(friendList.getModel() + " " + friendList.getName());
                }else
                { Chats.getInstance().setTittle_mess(friendList.getName());}
                currentPosition = position;
                InformationActivity.start(activity);
            }});
    }

    @Override
    public int getItemCount() {
        return friendLists.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        final ImageView imageView;
        final TextView nameView  ,MessageTextView , NameTextImage;
        ViewHolder(View view){
            super(view);
            imageView = (ImageView)view.findViewById(R.id.avatar_image);
            nameView = (TextView) view.findViewById(R.id.NameTextView);
            MessageTextView = (TextView) view.findViewById(R.id.MessageTextView);
            NameTextImage= (TextView) view.findViewById(R.id.NameTextImage);
        }
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
