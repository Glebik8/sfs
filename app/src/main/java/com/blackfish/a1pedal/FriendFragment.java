package com.blackfish.a1pedal;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.blackfish.a1pedal.API.Requests;
import com.blackfish.a1pedal.ChatKit.media.DefaultDialogsActivity;
import com.blackfish.a1pedal.ProfileInfo.FriendList;
import com.blackfish.a1pedal.ProfileInfo.Profile_Info;
import com.blackfish.a1pedal.ProfileInfo.User;
import com.blackfish.a1pedal.data.FriendRequest;
import com.blackfish.a1pedal.data.FriendsInfo;
import com.blackfish.a1pedal.data.Info;
import com.blackfish.a1pedal.data.Request;
import com.blackfish.a1pedal.tools_class.DataApdaterFriend;
import com.droidnet.DroidListener;
import com.droidnet.DroidNet;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class FriendFragment extends Fragment implements DroidListener {

    private FriendsInfo friendLists;

    public FriendFragment() {
    }

    public static FriendFragment newInstance() {
        return new FriendFragment();
    }

    private DroidNet mDroidNet;
    public static DataApdaterFriend apdaterFriend;
    RecyclerView recyclerView;
    TextView ContNameText;
    ImageView AddImage;
    LinearLayout WaitInt;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.contacts_fram, container, false);
        AddImage = view.findViewById(R.id.AddImage);
        ContNameText = view.findViewById(R.id.ContNameText);
        WaitInt = view.findViewById(R.id.WaitInt);
        recyclerView = (RecyclerView) view.findViewById(R.id.friendList);
        mDroidNet = DroidNet.getInstance();
        mDroidNet.addInternetConnectivityListener(this);
        if (User.getInstance().getType().equals("driver")) {
            ContNameText.setText("Сервисы");
        } else {
            ContNameText.setText("Подписчики");
        }
        String a = (User.getInstance().isDriver() ? "Сервисы" : "Подписчики");
        ContNameText.setOnClickListener(v -> {
            if (ContNameText.getText() == a) {
                ContNameText.setText("Заявки");
            } else {
                ContNameText.setText(a);
            }
            apdaterFriend.friendsOnly = !apdaterFriend.friendsOnly;
            apdaterFriend.notifyDataSetChanged();
        });
        AddImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (User.getInstance().getType().equals("driver")) {
                    Intent intent = new Intent(getActivity(), AddSFriend.class);
                    startActivity(intent);
                } else {
                    String k;
                    if (User.getInstance().getName() != null && !User.getInstance().getName().equals("")) {
                        k = User.getInstance().getName();
                    } else {
                        k = User.getInstance().getFio();
                    }
                    Intent sendIntent = new Intent();
                    sendIntent.setAction(Intent.ACTION_SEND);
                    sendIntent.putExtra(Intent.EXTRA_TEXT,
                            "Привет, мы используем приложение для автомобилистов 1pedal.  Скачать : https://play.google.com/....\nПодпишись на наш сервис по номеру " + User.getInstance().getPhone() + "\nС уважением команда  " + User.getInstance().getFio() + ".");
                    sendIntent.setType("text/plain");
                    startActivity(sendIntent);
                }
            }
        });
        return view;
    }

    @Override
    public void onInternetConnectivityChanged(boolean isConnected) {

        if (isConnected) {
            //do Stuff with internet
            netIsOn();
        } else {
            //no internet
            netIsOff();
        }
    }

    private void netIsOn() {
        ContNameText.setVisibility(View.VISIBLE);
        AddImage.setVisibility(View.VISIBLE);
        WaitInt.setVisibility(View.GONE);
        Requests.Companion.getFriends(
                (FriendsInfo response) -> {
                    Log.d("glebik", response.toString());
                    response.filter((FriendRequest request) -> {
                        if (User.getInstance().getType().equals("driver"))
                            return request.getRecipient();
                        return request.getSender();
                    });
                    apdaterFriend = new DataApdaterFriend(getContext(), response);
                    recyclerView.addItemDecoration(new DividerItemDecoration(getContext(),
                            DividerItemDecoration.HORIZONTAL));
                    recyclerView.addItemDecoration(new DividerItemDecoration(getContext(),
                            DividerItemDecoration.VERTICAL));
                    recyclerView.setAdapter(apdaterFriend);
                    recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                    return null;
                }
        );

    }

    private void netIsOff() {
        ContNameText.setVisibility(View.GONE);
        AddImage.setVisibility(View.GONE);
        WaitInt.setVisibility(View.VISIBLE);
        recyclerView.setAdapter(null);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mDroidNet.removeInternetConnectivityChangeListener(this);
    }


}
