package com.blackfish.a1pedal;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.blackfish.a1pedal.API.Requests;
import com.blackfish.a1pedal.Calendar_block.CalendarActivity;
import com.blackfish.a1pedal.ProfileInfo.Chats;
import com.blackfish.a1pedal.ProfileInfo.Profile_Info;
import com.blackfish.a1pedal.data.UpdateInfo;
import com.blackfish.a1pedal.tools_class.DataApdaterFriend;
import com.bumptech.glide.Glide;
import com.squareup.picasso.Picasso;

import java.io.File;

import static com.blackfish.a1pedal.tools_class.DataApdaterFriend.currentPosition;
import static com.blackfish.a1pedal.tools_class.DataApdaterFriend.friendLists;

public class InformationActivity extends AppCompatActivity {

    ImageView imageView;
    TextView friendName;
    TextView friendNumber;
    TextView friendPush;
    TextView friendChat;
    TextView friendRemove;
    TextView friendBlock;
    TextView friendType;

    public static void start(Context context) {
        context.startActivity(new Intent(context, InformationActivity.class));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_information);
        friendName = findViewById(R.id.friendName);
        friendNumber = findViewById(R.id.friendNumber);
        friendPush = findViewById(R.id.friendPush);
        friendChat = findViewById(R.id.friendChat);
        friendRemove = findViewById(R.id.friendRemove);
        friendBlock = findViewById(R.id.friendBlock);
        imageView = findViewById(R.id.friendImage);
        friendType = findViewById(R.id.textView);
        String name = friendLists.get(currentPosition).getName();
        if (name == null)
            name = friendLists.get(currentPosition).getFio();
        friendName.setText(name);
        friendNumber.setText(friendLists.get(currentPosition).phone);
        String text = friendLists.get(currentPosition).getWork();
        String[] st = friendLists.get(currentPosition).getPhoto().split("/");
        String AvatName = st[st.length - 1];
        File path = Chats.getInstance().getPath();
        File path1 = new File(path + AvatName);
        friendType.setText(text);
        friendNumber.setText(friendLists.get(currentPosition).phone);
        if (path1.exists()) {
            Glide.with(getApplicationContext()).load(path1).into(imageView);
        } else {
            try {
                Picasso.get().load("http://185.213.209.188" + friendLists.get(currentPosition).getPhoto()).into(imageView);
            } catch (IllegalArgumentException e) {
                // TODO
            }
        }
        friendRemove.setOnClickListener(v -> {
            String number = friendLists.get(currentPosition).getPk();
            if (number == null || number.equals("")) {
                return;
            }
            Requests.Companion.performRequestByNumber(number, "delete", (UpdateInfo response) -> {
                friendLists.remove(currentPosition);
                if (FriendFragment.apdaterFriend != null) {
                    FriendFragment.apdaterFriend.notifyDataSetChanged();
                }
                onBackPressed();
                return null;
            });
        });
        friendBlock.setOnClickListener(v -> {
            String number = friendLists.get(currentPosition).getPk();
            if (number == null || number.equals("")) {
                return;
            }
            Requests.Companion.performRequestByNumber(number, "block", (UpdateInfo response) -> {
                friendLists.remove(currentPosition);
                if (FriendFragment.apdaterFriend != null) {
                    FriendFragment.apdaterFriend.notifyDataSetChanged();
                }
                onBackPressed();
                return null;
            });
        });
        friendPush.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(InformationActivity.this, CalendarActivity.class);
                startActivity(intent);
            }
        });
    }
}
