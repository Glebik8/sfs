package com.blackfish.a1pedal.tools_class;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.blackfish.a1pedal.API.Requests;
import com.blackfish.a1pedal.ChatKit.DemoMessagesActivity;
import com.blackfish.a1pedal.ChatKit.media.CustomMediaMessagesActivity;
import com.blackfish.a1pedal.InformationActivity;
import com.blackfish.a1pedal.ProfileInfo.Chats;
import com.blackfish.a1pedal.ProfileInfo.FriendList;
import com.blackfish.a1pedal.ProfileInfo.Profile_Info;
import com.blackfish.a1pedal.ProfileInfo.User;
import com.blackfish.a1pedal.Profile_Fragment;
import com.blackfish.a1pedal.R;
import com.blackfish.a1pedal.data.FriendsInfo;
import com.blackfish.a1pedal.data.Info;
import com.blackfish.a1pedal.data.UpdateInfo;
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
    public static FriendsInfo friendLists;
    public static int currentPosition;
    public boolean friendsOnly = true;

    public DataApdaterFriend(Context context, FriendsInfo friendList) {
        activity=context;
        this.friendLists = friendList;
        this.inflater = LayoutInflater.from(context);
    }


    @Override
    public int getItemViewType(int position) {
        if (friendLists.isFriend(position))
            return 0;
        return 1;
    }

    @Override
    public DataApdaterFriend.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.friend_element, parent, false);
        RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) view.getLayoutParams();
        if (viewType == 0)
            params.height = 300;
        view.setLayoutParams(params);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position2) {
        int correct = position2;
        if (!friendsOnly) {
            correct += friendLists.friends.userFriends.size();
        }
        Info friendList = friendLists.get(correct);
        if (friendList == null)
            return;
        if (friendLists.isFriend(correct)) {
            holder.requests.setVisibility(View.INVISIBLE);
        } else {
            if (User.getInstance().isDriver()) {
                holder.accept.setVisibility(View.INVISIBLE);
                holder.deny.setVisibility(View.INVISIBLE);
            } else {
                holder.requestSend.setVisibility(View.INVISIBLE);
            }
            int finalCorrect = correct;
            holder.accept.setOnClickListener(v -> Requests.Companion.performRequestByNumber(
                    friendLists.get(finalCorrect).phone,
                    "add",
                    (UpdateInfo response) -> {
                        friendLists.makeFriend(finalCorrect);
                        notifyDataSetChanged();
                        return null;
                    }
            ));
            int finalCorrect1 = correct;
            holder.deny.setOnClickListener(v -> Requests.Companion.performRequestByNumber(
                    friendLists.get(finalCorrect1).phone,
                    "delete",
                    (UpdateInfo response) -> {
                        friendLists.remove(finalCorrect1);
                        notifyDataSetChanged();
                        return null;
                    }
            ));
        }
        try {
            if (!friendList.getPhoto().equals("")) {
                String[] st = friendList.getPhoto().split("/");
                String AvatName = st[st.length - 1];
                File path = Chats.getInstance().getPath();
                File path1 = new File(path + AvatName);
                if (!path1.exists()) {
                    try {
                        Picasso.get().load("http://185.213.209.188" + friendList.getPhoto()).into(holder.imageView);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    Glide.with(activity).load(path1).into(holder.imageView);
                }
            } else  {
                String name_im = friendList.getName();
                if (name_im == null)
                    name_im = friendList.getFio();
                if (name_im.length() > 2) {
                    name_im = name_im.substring(0, 2);
                }
                holder.NameTextImage.setText(name_im);
                holder.imageView.setImageResource(R.color.foobar);
            }
            String name = friendList.getName();
            if (name == null)
                name = friendList.getFio();
            if (friendList.getType().equals("driver")) {
                holder.nameView.setText(friendList.getModel());
                holder.MessageTextView.setText(name);

            } else {
                holder.nameView.setText(name);
                holder.MessageTextView.setText(friendList.getWork());

            }

            int finalCorrect2 = correct;
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Chats.getInstance().setChat_id("newDialog");
                    String rep = friendList.getPk();
                    if (friendList.getType().equals("driver")) {
                        Chats.getInstance().setTittle_mess(friendList.getModel() + " " + friendList.getName());
                    } else {
                        Chats.getInstance().setTittle_mess(friendList.getName());
                    }
                    currentPosition = finalCorrect2;
                    InformationActivity.start(activity);
                }
            });
        } catch (Exception e) {

        }
    }

    @Override
    public int getItemCount() {
        return (friendsOnly ? friendLists.friends.userFriends.size() :  friendLists.requests.size());
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        final ImageView imageView;
        final TextView nameView  ,MessageTextView , NameTextImage, requestSend;
        final LinearLayout requests;
        Button accept, deny;
        ViewHolder(View view){
            super(view);
            imageView = view.findViewById(R.id.avatar_image);
            nameView = view.findViewById(R.id.NameTextView);
            MessageTextView = view.findViewById(R.id.MessageTextView);
            NameTextImage= view.findViewById(R.id.NameTextImage);
            requests = view.findViewById(R.id.requests);
            accept = view.findViewById(R.id.accept);
            deny = view.findViewById(R.id.deny);
            requestSend = view.findViewById(R.id.request_send);
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
