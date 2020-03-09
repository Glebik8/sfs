package com.blackfish.a1pedal.ChatKit.media;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import androidx.fragment.app.Fragment;
import com.blackfish.a1pedal.*;
import com.blackfish.a1pedal.Auth_Registr.LoginActivity;
import com.blackfish.a1pedal.Calendar_block.CalendarActivity;
import com.blackfish.a1pedal.Calendar_block.EventsAdapter;
import com.blackfish.a1pedal.ChatKit.DemoMessagesActivity;
import com.blackfish.a1pedal.ChatKit.media.holders.IncomingVoiceMessageViewHolder;
import com.blackfish.a1pedal.ChatKit.media.holders.OutcomingVoiceMessageViewHolder;
import com.blackfish.a1pedal.ChatKit.media.holders.messages.CustomIncomingImageMessageViewHolder;
import com.blackfish.a1pedal.ChatKit.media.holders.messages.CustomIncomingTextMessageViewHolder;
import com.blackfish.a1pedal.ChatKit.media.holders.messages.CustomOutcomingImageMessageViewHolder;
import com.blackfish.a1pedal.ChatKit.media.holders.messages.CustomOutcomingTextMessageViewHolder;
import com.blackfish.a1pedal.ChatKit.media.holders.messages.IncomingGeopointMessageViewHolder;
import com.blackfish.a1pedal.ChatKit.media.holders.messages.IncomingVideoMessageViewHolder;
import com.blackfish.a1pedal.ChatKit.media.holders.messages.OutcomingGeopointMessageViewHolder;
import com.blackfish.a1pedal.ChatKit.media.holders.messages.OutcomingVideoMessageViewHolder;
import com.blackfish.a1pedal.ChatKit.model.Dialog;
import com.blackfish.a1pedal.ChatKit.model.Message;
import com.blackfish.a1pedal.ChatKit.model.User;
import com.blackfish.a1pedal.ProfileInfo.Chats;
import com.blackfish.a1pedal.ProfileInfo.Profile_Info;
import com.blackfish.a1pedal.tools_class.PostRes;
import com.blackfish.a1pedal.tools_class.RecorderActivity;
import com.blackfish.a1pedal.tools_class.SetMyLocation;
import com.blackfish.a1pedal.tools_class.showToast;
import com.droidnet.DroidListener;
import com.droidnet.DroidNet;
import com.stfalcon.chatkit.messages.MessageHolders;
import com.stfalcon.chatkit.messages.MessageInput;
import com.stfalcon.chatkit.messages.MessagesList;
import com.stfalcon.chatkit.messages.MessagesListAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class CustomMediaMessagesActivity extends DemoMessagesActivity
        implements MessageInput.InputListener,
        MessageInput.AttachmentsListener,
        MessageHolders.ContentChecker<Message>,
        DialogInterface.OnClickListener, MessageInput.TypingListener {

    LinearLayout WaitInt, InternetIsOk;
    private static final int MY_PERMISSIONS_REQUEST_READ_MEDIA = 1;
    private final int Pick_image = 2;
    final int REQUEST_CODE_PHOTO = 3;
    final int REQUEST_CODE_VIDEO = 5;
    final int REQUEST_CODE_AUDIO = 4;
    final int REQUEST_CODE_LOCATION = 6;
    final int REQUEST_CODE_APPOINTMENT = 7;
    static final private int CHOOSE_THIEF = 0;
    String typeCus;
    String type = "";
    int id = 0;
    Uri imageUri;
    private static final byte CONTENT_TYPE_VOICE = 1;
    private static final byte CONTENT_TYPE_VIDEO = 2;
    private static final byte CONTENT_TYPE_GEOPOINT = 3;
    TextView StTextView;
    JSONObject data;
    String token;
    String mCurrentPhotoPath;
    TextView LastActivyText;
    LinearLayout ShowTypingView;
    LinearLayout VideoLoadLayout;
    private DroidNet mDroidNet1;

    public static void open(Context context, int position) {
        Intent intent = new Intent(context, CustomMediaMessagesActivity.class);
        intent.putExtra("position", String.valueOf(position));
        context.startActivity(intent);
    }

    private MessagesList messagesList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom_media_messages);
        Chats.getInstance().setActual_activity("1");
        TextView ContNameText = findViewById(R.id.ContNameText);
        LastActivyText = findViewById(R.id.LastActivyText);
        ShowTypingView = findViewById(R.id.ShowTypingView);
        this.messagesList = (MessagesList) findViewById(R.id.messagesList);
        InternetIsOk = findViewById(R.id.InternetIsOk);
        WaitInt = findViewById(R.id.WaitInt);
        StTextView = findViewById(R.id.StTextView);
        TextView BackText = findViewById(R.id.BackText);
        TextView CalendarMeetText = findViewById(R.id.CalendarMeetText);
        VideoLoadLayout = findViewById(R.id.VideoLoadLayout);
        Date date = null;
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm");
        try {
            date = format.parse(Chats.getInstance().getLastActivity());
            System.out.println(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        String k = DateFormat.getDateTimeInstance(DateFormat.LONG, DateFormat.SHORT).format(date);
        LastActivyText.setText("был(а) " + k);
        ContNameText.setText(Chats.getInstance().getTittle_mess());

        BackText.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Chats.getInstance().setActual_activity("0");
                Intent intent = new Intent(DefaultDialogsActivity.BROADCAST_ACTION);
                intent.putExtra("Message", "FinishMessages");
                sendBroadcast(intent);
                finish();
                // сообщаем о старте задачи
            }
        });

        CalendarMeetText.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(CustomMediaMessagesActivity.this, EventsArchive.class);
                startActivity(intent);
                // сообщаем о старте задачи
            }
        });

        SetReadAll mt = new SetReadAll();
        mt.execute();
        MessageInput input = (MessageInput) findViewById(R.id.input);
        input.setInputListener(this);
        input.setAttachmentsListener(this);
        input.setTypingListener(this);
        initAdapter();
    }

    public void openDialog() {

    }

    public void showTyp() {
        LastActivyText.setVisibility(View.GONE);
        ShowTypingView.setVisibility(View.VISIBLE);
    }

    public void hideTyp() {
        ShowTypingView.setVisibility(View.GONE);
        LastActivyText.setVisibility(View.VISIBLE);
    }


    @Override
    public void onInternetConnectivityChanged(boolean isConnected) {
        if (isConnected) {
            //do Stuff with internet
            netIsOn1();
            netIsOn();
        } else {
            //no internet
            netIsOff1();
            netIsOff();
        }
    }

    private void netIsOn1() {
        InternetIsOk.setVisibility(View.VISIBLE);
        WaitInt.setVisibility(View.GONE);
    }

    private void netIsOff1() {
        InternetIsOk.setVisibility(View.GONE);
        WaitInt.setVisibility(View.VISIBLE);
    }


    @Override
    public boolean onSubmit(CharSequence input) {
        String avat = "";
        if (!com.blackfish.a1pedal.ProfileInfo.User.getInstance().getPhoto().equals("")) {
            avat = "http://185.213.209.188" + avat;
        }
        User us = new User("0", com.blackfish.a1pedal.ProfileInfo.User.getInstance().getName(), avat, true);
        SimpleDateFormat date = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS");
        String dateString = date.format(new Date());
        Date date1 = null;
        try {
            date1 = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss").parse(dateString);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        String content = new String().valueOf(input);
        Message mes = new Message("6", us, content, date1, "Отправлено");


        token = Profile_Info.getInstance().getToken();
        data = new JSONObject();
        try {
            data.put("sender", com.blackfish.a1pedal.ProfileInfo.User.getInstance().getPk());
            data.put("recipient", Chats.getInstance().getRecipient_id());
            data.put("type", "text");
            if (!Chats.getInstance().getChat_id().equals("newDialog")) {
                data.put("chat_id", Chats.getInstance().getChat_id());
            }
            data.put("content", content);
            id = 1;
            SendMes mt = new SendMes();
            mt.execute();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        //  super.messagesAdapter.addToStart(  mes , true);
        return true;
    }


    @Override
    public void onAddAttachments() {

        int permissionCheck = ContextCompat.checkSelfPermission(CustomMediaMessagesActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(CustomMediaMessagesActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, MY_PERMISSIONS_REQUEST_READ_MEDIA);
        } else {
            LayoutInflater li = LayoutInflater.from(CustomMediaMessagesActivity.this);
            View promptsView = li.inflate(R.layout.layout_choose_mes_type, null);
            androidx.appcompat.app.AlertDialog.Builder mDialogBuilder = new androidx.appcompat.app.AlertDialog.Builder(CustomMediaMessagesActivity.this, R.style.CustomDialog);
            mDialogBuilder.setView(promptsView);
            TextView CanselTextView = (TextView) promptsView.findViewById(R.id.CanselTextView);
            TextView CameraTextView = (TextView) promptsView.findViewById(R.id.CameraTextView);
            TextView GalleryTextView = (TextView) promptsView.findViewById(R.id.GalleryTextView);
            TextView VideoTextView = (TextView) promptsView.findViewById(R.id.VideoTextView);
            TextView AudioTextView = (TextView) promptsView.findViewById(R.id.AudioTextView);
            TextView LocationTextView = (TextView) promptsView.findViewById(R.id.LocationTextView);
            TextView AppointmentTextView = (TextView) promptsView.findViewById(R.id.AppointmentTextView);
            AlertDialog alertDialog = mDialogBuilder.create();
            //     alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            alertDialog.show();
            CanselTextView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    alertDialog.cancel();
                }
            });
            GalleryTextView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                    photoPickerIntent.setType("image/*");
                    photoPickerIntent.putExtra(Intent.EXTRA_MIME_TYPES, new String[]{"image/*"});
                    startActivityForResult(photoPickerIntent, Pick_image);
                    alertDialog.cancel();
                }
            });
            VideoTextView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                    photoPickerIntent.setType("video/*");
                    photoPickerIntent.putExtra(Intent.EXTRA_MIME_TYPES, new String[]{"video/*"});
                    startActivityForResult(photoPickerIntent, REQUEST_CODE_VIDEO);
                    alertDialog.cancel();
                }
            });
            CameraTextView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    // проверяем, что есть приложение способное обработать интент
                    if (takePictureIntent.resolveActivity(CustomMediaMessagesActivity.this.getPackageManager()) != null) {
                        // создать файл для фотографии
                        File photoFile = null;
                        try {
                            photoFile = createImageFile();
                        } catch (IOException ex) {
                            // ошибка, возникшая в процессе создания файла
                        }
                        // если файл создан, запускаем приложение камеры
                        if (photoFile != null) {
                            Uri photoURI = null;

                            photoURI = FileProvider.getUriForFile(CustomMediaMessagesActivity.this,
                                    BuildConfig.APPLICATION_ID + ".provider",
                                    photoFile);
                            takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                            takePictureIntent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                            startActivityForResult(takePictureIntent, REQUEST_CODE_PHOTO);
                            alertDialog.cancel();
                        }
                    }
                }
            });
            AudioTextView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent questionIntent = new Intent(CustomMediaMessagesActivity.this,
                            RecorderActivity.class);
                    startActivityForResult(questionIntent, REQUEST_CODE_AUDIO);
                    alertDialog.cancel();
                }
            });
            LocationTextView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent questionIntent = new Intent(CustomMediaMessagesActivity.this,
                            SetMyLocation.class);
                    startActivityForResult(questionIntent, REQUEST_CODE_LOCATION);
                    alertDialog.cancel();
                }
            });

        }
    }

    @Override
    public boolean hasContentFor(Message message, byte type) {
        Date date = null;
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm");
        try {
            date = format.parse(Chats.getInstance().getLastActivity());
            System.out.println(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        String k = DateFormat.getDateTimeInstance(DateFormat.LONG, DateFormat.SHORT).format(date);
        LastActivyText.setText("был(а) " + k);
        switch (type) {
            case CONTENT_TYPE_VOICE:
                return message.getVoice() != null
                        && message.getVoice().getUrl() != null
                        && !message.getVoice().getUrl().isEmpty();
            case CONTENT_TYPE_VIDEO:
                return message.getVideo() != null
                        && message.getVideo().getUrl() != null
                        && !message.getVideo().getUrl().isEmpty();
            case CONTENT_TYPE_GEOPOINT:
                return message.getGeopoint() != null
                        && message.getGeopoint().getLan() != null
                        && !message.getGeopoint().getLan().isEmpty()
                        && message.getGeopoint().getLat() != null
                        && !message.getGeopoint().getLat().isEmpty();

        }

        return false;
    }

    @Override
    public void onClick(DialogInterface dialogInterface, int i) {
        /*   switch (i) {
            case 0:
             messagesAdapter.addToStart(MessagesFixtures.getImageMessage(), true);
                break;
            case 1:
                messagesAdapter.addToStart(MessagesFixtures.getVoiceMessage(), true);
                break;
        }*/
    }

    private void initAdapter() {
        MessageHolders holders = (MessageHolders) new MessageHolders()
                .registerContentType(CONTENT_TYPE_VOICE,
                        IncomingVoiceMessageViewHolder.class,
                        R.layout.item_custom_incoming_voice_message,
                        OutcomingVoiceMessageViewHolder.class,
                        R.layout.item_custom_outcoming_voice_message,
                        this)
                .registerContentType(CONTENT_TYPE_VIDEO,
                        IncomingVideoMessageViewHolder.class,
                        R.layout.item_custom_incoming_video_message,
                        OutcomingVideoMessageViewHolder.class,
                        R.layout.item_custom_outcoming_video_message,
                        this)
                .registerContentType(CONTENT_TYPE_GEOPOINT,
                        IncomingGeopointMessageViewHolder.class,
                        R.layout.item_custom_incoming_geopoint_message,
                        OutcomingGeopointMessageViewHolder.class,
                        R.layout.item_custom_outcoming_geopoint_message,
                        this)
                .setIncomingTextConfig(
                        CustomIncomingTextMessageViewHolder.class,
                        R.layout.item_custom_incoming_text_message)
                .setOutcomingTextConfig(
                        CustomOutcomingTextMessageViewHolder.class,
                        R.layout.item_custom_outcoming_text_message)
                .setIncomingImageConfig(CustomIncomingImageMessageViewHolder.class,
                        R.layout.item_custom_incoming_image_message)
                .setOutcomingImageConfig(CustomOutcomingImageMessageViewHolder.class,
                        R.layout.item_custom_outcoming_image_message);
        super.messagesAdapter = new MessagesListAdapter<>(super.senderId, holders, super.imageLoader);
        super.messagesAdapter.enableSelectionMode(this);
        super.messagesAdapter.setLoadMoreListener(this);
        this.messagesList.setAdapter(super.messagesAdapter);
    }

    @Override
    public void onStartTyping() {
        Chats.getInstance().setTypingB("true");

        data = new JSONObject();
        try {
            data.put("recipient_id", Chats.getInstance().getRecipient_id());
            data.put("typing", Chats.getInstance().getTypingB());
        } catch (JSONException err) {
        }
        SetTyping er = new SetTyping();
        er.execute();
    }

    @Override
    public void onStopTyping() {
       /* Chats.getInstance().setTypingB("false");
        data = new JSONObject();
        try {
            data.put("recipient_id", Chats.getInstance().getRecipient_id());
            data.put("typing", Chats.getInstance().getTypingB());
        } catch (JSONException err){
        }
        SetTyping  er = new SetTyping();
        er.execute();*/
    }


    private class SendMes extends AsyncTask<Void, Void, String> {
        String resultJson = "";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            if (id == 5) {

            }
        }

        @Override
        protected String doInBackground(Void... params) {
            if (id == 1 || id == 6) {
                if (id == 6) {
                    data = new JSONObject();
                    try {
                        data.put("sender", com.blackfish.a1pedal.ProfileInfo.User.getInstance().getPk());

                        data.put("recipient", Chats.getInstance().getRecipient_id());
                        data.put("type", "location");
                        if (!Chats.getInstance().getChat_id().equals("newDialog")) {
                            data.put("chat_id", Chats.getInstance().getChat_id());
                        }
                        data.put("content", Chats.getInstance().getMy_loction_lat_lan());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }


                PostRes example = new PostRes();
                String response = "";
                try {
                    token = Profile_Info.getInstance().getToken();
                    response = example.post("http://185.213.209.188/api/sendmessage/", data.toString(), "Token " + token);
                    String k = response;

                } catch (IOException e) {
                    e.printStackTrace();
                }

                return response;
            } else {

                String patch = "";
                PostIcon example = new PostIcon();
                String response = "";
                if (id == 2) {
                    patch = getRealPathFromURI(imageUri);
                    typeCus = "photo";
                }
                if (id == 3) {
                    patch = imageUri.toString();
                    typeCus = "photo";
                }
                if (id == 4) {
                    patch = imageUri.toString();
                    type = "audio/mpeg";
                    typeCus = "audio";
                }
                if (id == 5) {
                    patch = imageUri.toString();
                    type = "video/mp4";
                    typeCus = "video";
                }
                try {
                    token = Profile_Info.getInstance().getToken();
                    response = example.post("http://185.213.209.188/api/sendmessage/", patch, "Token " + token, type, typeCus);
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
                String k = response;
                return response;
            }

        }

        @Override
        protected void onPostExecute(String response) {
            super.onPostExecute(response);
            if (!response.equals("")) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String chat_id = jsonObject.getString("chat_id");
                    Chats.getInstance().setChat_id(chat_id);
                    String avat = "";
                    if (!com.blackfish.a1pedal.ProfileInfo.User.getInstance().getPhoto().equals("")) {
                        avat = "http://185.213.209.188" + avat;
                    }
                    User us = new User("0", com.blackfish.a1pedal.ProfileInfo.User.getInstance().getName(), avat, true);
                    JSONObject m = jsonObject.getJSONObject("message");
                    String type = m.getString("type");
                    String content = m.getString("content");
                    String date = m.getString("date");
                    Date date1 = null;
                    try {
                        date1 = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss").parse(date);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    JSONArray read = m.getJSONArray("read");
                    String st;
                    if (read.length() > 1) {
                        st = "Прочитано";
                    } else {
                        st = "Отправлено";
                    }

                    Message mes;

                    mes = new Message("1", us, content, date1, st);

                    if (type.equals("photo")) {
                        mes = new Message("1", us, null, date1, st);
                        mes.setImage(new Message.Image("http://185.213.209.188" + content));
                    }

                    if (type.equals("audio")) {
                        mes = new Message("1", us, null, date1, st);
                        mes.setVoice(new Message.Voice("http://185.213.209.188" + content, 00));
                    }
                    if (type.equals("location")) {
                        mes = new Message("1", us, null, date1, st);
                        String[] st2 = content.split(",");
                        mes.setGeopoint(new Message.Geopoint(st2[0], st2[1]));
                    }
                    if (type.equals("video")) {
                        mes = new Message("1", us, null, date1, st);
                        mes.setVideo(new Message.Video("http://185.213.209.188" + content));
                    }

                    messagesAdapter.addToStart(mes, true);
                    if (id == 5) {
                        VideoLoadLayout.setVisibility(View.GONE);
                    }
                } catch (JSONException e) {
                    if (id == 5) {
                        VideoLoadLayout.setVisibility(View.GONE);
                    }
                    e.printStackTrace();
                }
            } else {
                if (id == 5) {
                    VideoLoadLayout.setVisibility(View.GONE);
                }
                showToast.showToast("Отсутствует интернет соедение", false, CustomMediaMessagesActivity.this);
            }
        }
    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String imageFileName = "JPEG_" + "11" + "_";
        File storageDir = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_DCIM), "Camera");
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = "file:" + image.getAbsolutePath();
        return image;
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) {
        super.onActivityResult(requestCode, resultCode, imageReturnedIntent);
        switch (requestCode) {
            case Pick_image:
                if (resultCode == RESULT_OK) {
                    try {
                        imageUri = imageReturnedIntent.getData();
                        final InputStream imageStream = CustomMediaMessagesActivity.this.getContentResolver().openInputStream(imageUri);
                        final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
                        id = 2;
                        type = "image/png";
                        imageUri = getImageUri(CustomMediaMessagesActivity.this, selectedImage);
                        SendMes mt = new SendMes();
                        mt.execute();
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                }
                break;
            case REQUEST_CODE_PHOTO:
                if (resultCode == RESULT_OK) {
                    imageUri = Uri.parse(mCurrentPhotoPath);
                    File file = new File(imageUri.getPath());
                    try {
                        InputStream ims = new FileInputStream(file);
                        type = "image/png";
                        id = 3;
                        imageUri = Uri.parse(imageUri.getPath());
                        data = new JSONObject();
                        SendMes mt = new SendMes();
                        mt.execute();
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                }
                break;
            case REQUEST_CODE_AUDIO:
                if (resultCode == RESULT_OK) {
                    imageUri = Uri.parse(Chats.getInstance().getVoice_patch());
                    id = 4;
                    SendMes mt = new SendMes();
                    mt.execute();
                } else {
                    //   infoTextView.setText(""); // стираем текст
                }
                break;
            case REQUEST_CODE_VIDEO:
                if (resultCode == RESULT_OK) {
                    imageUri = imageReturnedIntent.getData();
                    id = 5;
                    VideoLoadLayout.setVisibility(View.VISIBLE);
                    imageUri = Uri.parse(getRealPathFromURI(imageUri));
                    SendMes mt = new SendMes();
                    mt.execute();
                }
                break;
            case REQUEST_CODE_LOCATION:
                if (resultCode == RESULT_OK) {
                    id = 6;

                    SendMes mt = new SendMes();
                    mt.execute();
                }
                break;
        }
    }

    public class PostIcon {
        public final MediaType JSON = MediaType.get("application/json; charset=utf-8");

        OkHttpClient client = new OkHttpClient();

        public String post(String url, String patch, String token, String type, String typeCus) throws IOException {
            String[] st = patch.split("/");
            String Name = st[st.length - 1];
            RequestBody requestBody = new MultipartBody.Builder()
                    .setType(MultipartBody.FORM)
                    .addFormDataPart("file", Name,
                            RequestBody.create(MediaType.parse(type), new File(patch)))
                    .addFormDataPart("sender", com.blackfish.a1pedal.ProfileInfo.User.getInstance().getPk())
                    .addFormDataPart("recipient", Chats.getInstance().getRecipient_id())
                    .addFormDataPart("type", typeCus)
                    .addFormDataPart("chat_id", Chats.getInstance().getChat_id())
                    .addFormDataPart("content", "")
                    .build();

            Request request = new Request.Builder()
                    .header("Authorization", token)
                    .url(url)
                    .post(requestBody)
                    .build();
            try (Response response = client.newCall(request).execute()) {
                return response.body().string();
            }
        }
    }

    public String getRealPathFromURI(Uri uri) {
        Cursor cursor = CustomMediaMessagesActivity.this.getContentResolver().query(uri, null, null, null, null);
        cursor.moveToFirst();
        int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
        return cursor.getString(idx);
    }

    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }

    @Override
    public void onBackPressed() {
        // super.onBackPressed();
        Chats.getInstance().setActual_activity("0");
        Intent intent = new Intent(DefaultDialogsActivity.BROADCAST_ACTION);
       /* intent.putExtra("Message", "FinishMessages");
        sendBroadcast(intent);*/
        finish();
    }

    public static class SetReadAll extends AsyncTask<Void, Void, String> {
        String resultJson = "";

        @Override
        protected String doInBackground(Void... params) {
            String token = Profile_Info.getInstance().getToken();
            PostRes example = new PostRes();
            String response = "";

            JSONObject data1 = new JSONObject();
            try {
                data1.put("recipient", Chats.getInstance().getRecipient_id());
            } catch (JSONException e) {
                e.printStackTrace();
            }

            try {
                response = example.post("http://185.213.209.188/api/readallmessages/" + Chats.getInstance().getChat_id(), data1.toString(), "Token " + token);
                String k = response;

            } catch (IOException e) {
                e.printStackTrace();
            }

            return response;
        }

        @Override
        protected void onPostExecute(String response) {
            super.onPostExecute(response);
            try {
                JSONObject jsonObject = new JSONObject(response);

            } catch (JSONException err) {
            }
        }
    }

    public class SetTyping extends AsyncTask<Void, Void, String> {

        String resultJson = "";

        @Override
        protected String doInBackground(Void... params) {
            String token = Profile_Info.getInstance().getToken();
            PostRes example = new PostRes();
            String response = "";

            try {
                response = example.post("http://185.213.209.188/api/sendtyping/", data.toString(), "Token " + token);
                String k = response;

            } catch (IOException e) {
                e.printStackTrace();
            }

            return response;
        }

        @Override
        protected void onPostExecute(String response) {
            super.onPostExecute(response);
            try {
                JSONObject jsonObject = new JSONObject(response);

            } catch (JSONException err) {
            }
        }
    }

}
