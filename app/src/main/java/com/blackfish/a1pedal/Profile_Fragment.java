package com.blackfish.a1pedal;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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
import android.provider.MediaStore;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.appcompat.app.AlertDialog;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.blackfish.a1pedal.Auth_Registr.LoginActivity;
import com.blackfish.a1pedal.ProfileInfo.Chats;
import com.blackfish.a1pedal.ProfileInfo.Profile_Info;
import com.blackfish.a1pedal.ProfileInfo.User;
import com.blackfish.a1pedal.tools_class.SocketEvent;
import com.blackfish.a1pedal.tools_class.showToast;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.custom.cn.CircleImageView;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;

import jp.wasabeef.picasso.transformations.CropCircleTransformation;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static android.app.Activity.RESULT_OK;
public class Profile_Fragment  extends Fragment {

    private static final int MY_PERMISSIONS_REQUEST_READ_MEDIA = 1 ;
    CircleImageView profile_image;
    TextView NameTextImage,ExitTextView , NameTextView , ModelTextView , TireTextView , DiskTextView, ParamTextView;
    RelativeLayout ClickEventImage;
    String type , token ;
    JSONObject data;
    private final int Pick_image = 1;
    final int TYPE_PHOTO = 1;
    final int REQUEST_CODE_PHOTO = 2;
    int id = 0;
    Uri imageUri ;
    String photo="";
    String mCurrentPhotoPath;
    SharedPreferences sPref;
    public Profile_Fragment() {
    }
    public static Fragment newInstance() {
        return new Profile_Fragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.profile_fram, container, false);
        profile_image =  view.findViewById(R.id.profile_image);

        ClickEventImage = view.findViewById(R.id.ClickEventImage);
        NameTextImage =  view.findViewById(R.id.NameTextImage);
        ExitTextView =  view.findViewById(R.id.ExitTextView);
        NameTextView =  view.findViewById(R.id.NameTextView);
        ModelTextView =  view.findViewById(R.id.ModelTextView);
        TireTextView =  view.findViewById(R.id.TireTextView);
        DiskTextView =  view.findViewById(R.id.DiskTextView);
        ParamTextView=  view.findViewById(R.id.ParamTextView);
        token= Profile_Info.getInstance().getToken();

        type = User.getInstance().getType();
        if (User.getInstance().getType().equals("driver")){
            String t = User.getInstance().getBrand()+" "+ User.getInstance().getModel();
        ModelTextView.setText(t);
        NameTextView.setText(User.getInstance().getFio());
            Chats.getInstance().setActual_activity("4");
        }
        else
        {   NameTextView.setVisibility(View.GONE);
            ModelTextView.setVisibility(View.GONE);
            TireTextView.setVisibility(View .GONE);
            DiskTextView.setVisibility(View.GONE);
            ParamTextView.setVisibility(View.VISIBLE);
            ParamTextView.setText(User.getInstance().getName());

        }
        if (User.getInstance().getPhoto().equals("") )
        {
            if (User.getInstance().getType().equals("driver")){
            if (User.getInstance().getFio()=="1"){
           }
            else { String name_im = User.getInstance().getFio();
            if (name_im.length()>2){
            name_im = name_im.substring(0,2);}
            NameTextImage.setText(name_im);}
        }
        else
            {

                String name_im = User.getInstance().getName();
                if (name_im.length()>2){
                    name_im = name_im.substring(0,2);}
                NameTextImage.setVisibility(View.VISIBLE);
                NameTextImage.setText(name_im);
            }
        }
      else {

            NameTextImage.setVisibility(View.GONE);
            String AvatName = User.getInstance().getPhoto();
            File path= Chats.getInstance().getPath();
            File path1 = new File(path+AvatName);
            if (!path1.exists()){
                try {
                    Picasso.get().load("http://185.213.209.188" + User.getInstance().getPhoto()).resize(200, 200)
                            .transform(new CropCircleTransformation()).noFade().into(profile_image);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            else{
                Glide.with(this).load(path1).apply(RequestOptions.circleCropTransform()).dontAnimate().into(profile_image);
            }


        }


        ParamTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showFragment(Main_Profile_S_Fragment.newInstance());
            }});


        ExitTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveToken("");
                SharedPreferences sPref = (SharedPreferences) Objects.requireNonNull(getActivity()).getSharedPreferences("PROFILE" , Context.MODE_PRIVATE);
                sPref.edit().clear().apply();
                SharedPreferences sPref1 = (SharedPreferences) Objects.requireNonNull(getActivity()).getSharedPreferences("MESSAGESSLIST" , Context.MODE_PRIVATE);
                sPref1.edit().clear().apply();
                SharedPreferences sPref2 = (SharedPreferences) Objects.requireNonNull(getActivity()).getSharedPreferences("TOTAL_UNREAD" , Context.MODE_PRIVATE);
                sPref2.edit().clear().apply();
                SharedPreferences sPref3 = (SharedPreferences) Objects.requireNonNull(getActivity()).getSharedPreferences("DIALOGLIST" , Context.MODE_PRIVATE);
                sPref3.edit().clear().apply();
                Intent intent1 = new Intent(getActivity(), SocketEvent.class);
                Chats.getInstance().setExit("true");
                getContext().stopService(intent1);
                Intent intent = new Intent(getActivity(), LoginActivity.class);
                startActivity(intent);
                getActivity().finish();
            }});

        ClickEventImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int permissionCheck = ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE);

                if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, MY_PERMISSIONS_REQUEST_READ_MEDIA);
                } else {
                    LayoutInflater li = LayoutInflater.from(getActivity());
                View promptsView = li.inflate(R.layout.layout_choose_camera, null);
                AlertDialog.Builder mDialogBuilder = new AlertDialog.Builder(getActivity(), R.style.CustomDialog);
                mDialogBuilder.setView(promptsView);

                TextView CanselTextView = (TextView) promptsView.findViewById(R.id.CanselTextView);
                TextView CameraTextView = (TextView) promptsView.findViewById(R.id.CameraTextView);
                TextView GalleryTextView = (TextView) promptsView.findViewById(R.id.GalleryTextView);

                AlertDialog alertDialog = mDialogBuilder.create();
           //     alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                alertDialog.show();
                CanselTextView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {alertDialog.cancel();}});
                 GalleryTextView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                    Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                    photoPickerIntent.setType("image/*");
                    startActivityForResult(photoPickerIntent, Pick_image);
                    alertDialog.cancel();
                    }});
                CameraTextView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

                        // проверяем, что есть приложение способное обработать интент
                        if (takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null) {
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

                                    photoURI = FileProvider.getUriForFile(getActivity(),
                                            BuildConfig.APPLICATION_ID + ".provider",
                                            photoFile);


                                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT,photoURI);
                                takePictureIntent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                        startActivityForResult(takePictureIntent, REQUEST_CODE_PHOTO);
                        alertDialog.cancel();
                            }
                    }}});
            }
            }
        });
        ModelTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showFragment(Main_Profile_V_Fragment.newInstance());
            }});
        TireTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showFragment(Tire_Par_V_Fragment.newInstance());
            }});
        DiskTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showFragment(Disk_Par_V_Fragment.newInstance());
            }});

        return view;

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


    public void showFragment(Fragment fragment) {
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction
                .replace(R.id.fl_content, fragment)
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) {
        super.onActivityResult(requestCode, resultCode, imageReturnedIntent);

        switch(requestCode) {
            case Pick_image:
                if(resultCode == RESULT_OK){
                    try {
                        imageUri = imageReturnedIntent.getData();
                        final InputStream imageStream = getActivity().getContentResolver().openInputStream(imageUri);
                        final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
                        profile_image.setImageBitmap(selectedImage);
                        NameTextImage.setText("");
                        id =1;
                        imageUri = getImageUri(getActivity(), selectedImage);
                        SetImage mt = new SetImage();
                        mt.execute();
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                }break;
            case REQUEST_CODE_PHOTO :
                if (resultCode == RESULT_OK) {
                    imageUri = Uri.parse(mCurrentPhotoPath);
                    File file = new File(imageUri.getPath());
                    try {
                        InputStream ims = new FileInputStream(file);
                        profile_image.setImageBitmap(BitmapFactory.decodeStream(ims));
                        NameTextImage.setText("");
                        id = 2;
                        imageUri = Uri.parse(imageUri.getPath());
                        SetImage mt = new SetImage();
                        mt.execute();
                    } catch (FileNotFoundException e) {

                    }
                }break;
                            }

        }
    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }


    public String getRealPathFromURI(Uri uri) {
        Cursor cursor = getActivity().getContentResolver().query(uri, null, null, null, null);
        cursor.moveToFirst();
        int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
        return cursor.getString(idx);
    }
    private  class SetImage extends AsyncTask<Void, Void, String> {
        String resultJson = "";

        @Override
        protected String doInBackground(Void... params) {
            String patch="";
            PostIcon example = new PostIcon();
            String response="";
if(id ==1){
          patch = getRealPathFromURI(imageUri);
          }
            if(id ==2) { patch = imageUri.toString();}

            try {
                response = example.post("http://185.213.209.188/api/updateuserphoto/", patch, "Token "+token);
            } catch (IOException e1) {
                e1.printStackTrace();
            }
            String k =response;
            return response; }
        @Override
        protected void onPostExecute(String response) {
            super.onPostExecute(response);
            if (!response.equals("")) {
            try {
                JSONObject jsonObject = new JSONObject(response);
                User.getInstance().setPhoto(jsonObject.getString("photo"));
            }catch (JSONException err){
            }
        }else {
                showToast.showToast("Отсутствует интернет соедение", false,getActivity());
                ((MainActivity)getActivity()).showNav();
                getActivity().onBackPressed();
            }
        }
    }
    public class PostIcon {
        public final MediaType JSON = MediaType.get("application/json; charset=utf-8");

        OkHttpClient client = new OkHttpClient();

        public String post(String url, String patch, String token) throws IOException {
            RequestBody requestBody = new MultipartBody.Builder()
                    .setType(MultipartBody.FORM)
                    .addFormDataPart("file", "Icon.png",
                            RequestBody.create(MediaType.parse("image/png"), new File(patch)))
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
    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_READ_MEDIA:
                if ((grantResults.length > 0) && (grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                //    readDataExternal();
                }
                break;

            default:
                break;
        }
    }
    void saveToken(String t ) {
        sPref = (SharedPreferences) getActivity().getSharedPreferences("TOKEN" , Context.MODE_PRIVATE);
        SharedPreferences.Editor ed = sPref.edit();
        ed.putString("token", t);
        ed.commit();
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
