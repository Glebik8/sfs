package com.blackfish.a1pedal;

import android.content.Context;
import android.content.Intent;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.StyleSpan;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.blackfish.a1pedal.API.API;
import com.blackfish.a1pedal.API.Request;
import com.blackfish.a1pedal.API.RequestsKt;
import com.blackfish.a1pedal.Calendar_block.CalendarActivity;
import com.blackfish.a1pedal.ProfileInfo.Chats;
import com.blackfish.a1pedal.tools_class.PostRes;
import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.internal.GsonBuildConfig;
import com.squareup.picasso.Picasso;
import okhttp3.Credentials;
import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import java.io.File;
import java.io.IOException;

import static com.blackfish.a1pedal.tools_class.DataApdaterFriend.currentPosition;
import static com.blackfish.a1pedal.FriendFragment.friendLists;

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
        friendName.setText(friendLists.get(currentPosition).getName());
        friendNumber.setText(friendLists.get(currentPosition).getPhone());
        String text = friendLists.get(currentPosition).getWork();
        String[] st = friendLists.get(currentPosition).getImage().split("/");
        String AvatName = st[st.length - 1];
        File path = Chats.getInstance().getPath();
        File path1 = new File(path + AvatName);
        friendType.setText(text);
        RequestsKt.getNumberById(
                friendLists.get(currentPosition).getid(), (String response) -> {
                    friendNumber.setText(response);
                    return null;
                }
        );
        if (path1.exists()) {
            Glide.with(getApplicationContext()).load(path1).into(imageView);
        } else {
            try {
                Picasso.get().load(friendLists.get(currentPosition).getImage()).into(imageView);
            } catch (IllegalArgumentException e) {
                // TODO
            }
        }
        friendRemove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String number = friendLists.get(currentPosition).getid();
                if (number == null || number.equals("")) {
                    return;
                }
                RequestsKt.performRequest(number, "delete", (String response) -> {
                    Log.d("glebik", response);
                    return null;
                });
            }
        });
        friendBlock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String number = friendLists.get(currentPosition).getid();
                if (number == null || number.equals("")) {
                    return;
                }
                RequestsKt.performRequest(number, "block", (String response) -> {
                    Log.d("glebik", response);
                    return null;
                });
            }
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
