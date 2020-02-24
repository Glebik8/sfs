package com.blackfish.a1pedal.Auth_Registr;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import com.blackfish.a1pedal.Parametr_s_Activity;
import com.blackfish.a1pedal.Parametr_v_Activity;
import com.blackfish.a1pedal.ProfileInfo.Discs;
import com.blackfish.a1pedal.ProfileInfo.Profile_Info;
import com.blackfish.a1pedal.ProfileInfo.Tire;
import com.blackfish.a1pedal.ProfileInfo.User;
import com.blackfish.a1pedal.R;
import com.blackfish.a1pedal.tools_class.Save;
import com.blackfish.a1pedal.tools_class.showToast;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Objects;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class RegistrActivity extends AppCompatActivity {
    TextView Se_sel, Dr_sel, BackText, RegistrText , EnterText, RulesText ;
    EditText NameText , EmailText, PasswordText, RePasswordText;
    int selektr = 1;
    String name, email,  password, repassword , fcmToken;
    CheckBox CheckBox;
    SharedPreferences sPref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.registr_main);
        Se_sel = findViewById(R.id.Se_sel);
        Dr_sel = findViewById(R.id.Dr_sel);
        NameText = findViewById(R.id.NameText);
        BackText = findViewById(R.id.BackText);
        EmailText = findViewById(R.id.EmailText);
        PasswordText = findViewById(R.id.PasswordText);
        RePasswordText = findViewById(R.id.RePasswordText);
        RegistrText = findViewById(R.id.RegistrText);
        EnterText = findViewById(R.id.EnterText);
        CheckBox = findViewById(R.id.checkBox);
        RulesText= findViewById(R.id.RulesText);
        EnterText.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(RegistrActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });
        FirebaseInstanceId.getInstance().getInstanceId()
                .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                    @Override
                    public void onComplete(@NonNull Task<InstanceIdResult> task) {
                        if (!task.isSuccessful()) {
                            return;
                        }
                        fcmToken = task.getResult().getToken();
                    }});
        Se_sel.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (selektr == 1) {
                    Dr_sel.setBackgroundColor((Color.parseColor("#ffffff")));
                    Dr_sel.setTextColor((Color.parseColor("#000000")));
                    Se_sel.setBackgroundColor((Color.parseColor("#565357")));
                    Se_sel.setTextColor((Color.parseColor("#ffffff")));
                    NameText.setVisibility(View.GONE);
                    selektr = 2;
                }
            }
        });
        Dr_sel.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (selektr == 2) {
                    Se_sel.setBackgroundColor((Color.parseColor("#ffffff")));
                    Se_sel.setTextColor((Color.parseColor("#000000")));
                    Dr_sel.setBackgroundColor((Color.parseColor("#565357")));
                    Dr_sel.setTextColor((Color.parseColor("#ffffff")));
                    NameText.setVisibility(View.VISIBLE);
                    selektr = 1;
                }
            }
        });

        BackText.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                finish();
            }
        });
        RulesText.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                GetRule mt = new GetRule();
                mt.execute();
            }
        });

        RegistrText.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (CheckBox.isChecked()) {
                    if (selektr == 1) {
                        name = NameText.getText().toString();
                        email = EmailText.getText().toString();
                        password = PasswordText.getText().toString();
                        repassword = RePasswordText.getText().toString();
                        if (ChekText(name, email, password, repassword)) {
                            if (password.equals(repassword)) {
                                if (isValidEmail(email) )
                                {
                                    Registr mt = new Registr();
                                        mt.execute();

                                } else { showToast.showToast("Неверно указан адрес электронной почты", false, RegistrActivity.this);}
                            }else {  showToast.showToast("Пароли не совпадают", false, RegistrActivity.this);}
                        } else {
                            showToast.showToast("Не все поля заполнены", false, RegistrActivity.this);
                        }
                    } else {
                        email = EmailText.getText().toString();
                        password = PasswordText.getText().toString();
                        repassword = RePasswordText.getText().toString();
                        if (ChekText("", email, password, repassword)) {
                            Registr mt = new Registr();
                            mt.execute();
                        } else {
                            showToast.showToast("Не все поля заполнены", false, RegistrActivity.this);
                        }
                    }
                } else {
                    showToast.showToast("Вы не согласились с пользовательским соглашением", false, RegistrActivity.this);
                }
            }
        });
    }
        public Boolean ChekText (String name, String email, String password, String repassword){
            if (selektr == 1) {
                if (!name.equals("") && !email.equals("") && !password.equals("") && !repassword.equals("")) {
                    return true;
                } else {
                    return false;
                }
            } else {
                if (!email.equals("") && !password.equals("")&& !repassword.equals("") ) {
                    return true;
                } else {
                    return false;
                }
            }
        }

    private  class Registr extends AsyncTask<Void, Void, String> {
        String resultJson = "";
        @Override
        protected String doInBackground(Void... params) {

            PostRes example = new RegistrActivity.PostRes();
            String response="";
            try {

                JSONObject    data = new JSONObject();
                if (selektr == 1) {
                    data.put("fio", name);
                data.put("email", email);
                data.put("password", password);
                data.put("type", "driver");
                    data.put("os", "android");
                    data.put("token", fcmToken);
                }
               else {
                    data.put("fio", "1");
                    data.put("email", email);
                    data.put("password", password);
                    data.put("type", "service");
                    data.put("os", "android");
                    data.put("token", fcmToken);
                }

                response = example.post("http://185.213.209.188/api/register/", data.toString());
                try {
                    JSONObject jsonObject = new JSONObject(response);


                }catch (JSONException err){
                }
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return response;
        }
        @Override
        protected void onPostExecute(String response) {
            super.onPostExecute(response);
            try {
                JSONObject jsonObject = new JSONObject(response);
                String token = jsonObject.getString("token");
                Profile_Info.getInstance().setToken(token);
                SetProfileInfo(jsonObject);
                Save.ProfJson(jsonObject,RegistrActivity.this);

                saveToken (token);
                showToast.showToast("Вы зарегистрованы", true , RegistrActivity.this);
                     if (selektr == 1) {
                         Intent intent = new Intent( RegistrActivity.this , Parametr_v_Activity.class);
                         startActivity(intent);
                         finish();
                    }
                    else {
                         Intent intent = new Intent( RegistrActivity.this , Parametr_s_Activity.class);
                         startActivity(intent);
                         finish();
                    }
            }catch (JSONException err){
                showToast.showToast("Такой логин и/или пароль уже используется", false, RegistrActivity.this);
            }

        }
    }
    private  class GetRule extends AsyncTask<Void, Void, String> {

        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;
        String resultJson = "";

        @Override
        protected String doInBackground(Void... params) {
            URL url = null;
            try {
                url = new URL("http://185.213.209.188/api/getrules/");
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }


            try {
                urlConnection = (HttpURLConnection) url.openConnection();

            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            InputStream inputStream = urlConnection.getInputStream();
            StringBuffer buffer = new StringBuffer();

            reader = new BufferedReader(new InputStreamReader(inputStream));

            String line;
            while ((line = reader.readLine()) != null) {
                buffer.append(line);
            }

            resultJson = buffer.toString();
        } catch (IOException e) {
            e.printStackTrace();
        }

            return resultJson;
        }
        @Override
        protected void onPostExecute(String response) {
            super.onPostExecute(response);
            String Rules="";
            try {
                JSONObject jsonObject = new JSONObject(response);
                Rules = jsonObject.getString("text");
            }catch (JSONException ignored){
            }
            LayoutInflater li = LayoutInflater.from(RegistrActivity.this);
            View promptsView = li.inflate(R.layout.layout_rules, null);
            AlertDialog.Builder mDialogBuilder = new AlertDialog.Builder(RegistrActivity.this);
            mDialogBuilder.setView(promptsView);
            TextView tv = (TextView) promptsView.findViewById(R.id.tv);
            tv.setText(Rules);
            tv.setTextSize(15);
            mDialogBuilder
                    .setCancelable(false)

                   .setNegativeButton("Ок",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();
                                }
                            });
            AlertDialog alertDialog = mDialogBuilder.create();
            alertDialog.show();
        }
    }
    public class PostRes {
        public final MediaType JSON = MediaType.get("application/json; charset=utf-8");

        OkHttpClient client = new OkHttpClient();

        String post(String url, String json) throws IOException {
            RequestBody body = (RequestBody) RequestBody.create(JSON, json);
            Request request = new Request.Builder()
                    .url(url)
                    .post(body)
                    .build();
            try (Response response = client.newCall(request).execute()) {
                return response.body().string();

            }
        }}

    public final static boolean isValidEmail(CharSequence target) {
        if (TextUtils.isEmpty(target)) {
            return false;
        } else {
            return android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
        }
    }




    public void SetProfileInfo (JSONObject json1)
    {
        try {
            Profile_Info.getInstance().setToken ( json1.getString("token"));
            JSONObject json = json1.getJSONObject("user");
            User.getInstance().setPk(json.getString("pk"));
            User.getInstance().setEmail(json.getString("email"));
            User.getInstance().setFio(json.getString("fio"));
            User.getInstance().setPhoto(json.getString("photo"));
            User.getInstance().setType(json.getString("type"));
            User.getInstance().setName(json.getString("name"));
            User.getInstance().setGps(json.getString("gps"));
            User.getInstance().setWork(json.getString("work"));
            User.getInstance().setStreet(json.getString("street"));
            User.getInstance().setTimeFrom(json.getString("timeFrom"));
            User.getInstance().setTimeTo(json.getString("timeTo"));
            User.getInstance().setWeekends(json.getString("weekends"));
            User.getInstance().setSite(json.getString("site"));
            User.getInstance().setPhone(json.getString("phone"));
            User.getInstance().setBrand(json.getString("brand"));
            User.getInstance().setGearbox(json.getString("gearbox"));
            User.getInstance().setMiles(json.getString("miles"));
            User.getInstance().setModel(json.getString("model"));
            User.getInstance().setMotory_type(json.getString("motory_type"));
            User.getInstance().setMotor_volume(json.getString("motor_volume"));
            User.getInstance().setVin(json.getString("vin"));
            User.getInstance().setNumber(json.getString("number"));
            User.getInstance().setPrivod(json.getString("privod"));
            User.getInstance().setYear(json.getString("year"));
            if (User.getInstance().getType().equals("driver")){
                if (User.getInstance().getType().equals("driver")) {
                    try {
                        json = json1.getJSONObject("tire");
                        Tire.getInstance().setDiametr(json.getString("diametr"));
                        Tire.getInstance().setHeight(json.getString("height"));
                        Tire.getInstance().setRunflat(json.getString("runflat"));
                        Tire.getInstance().setThrons(json.getString("throns"));
                        Tire.getInstance().setType(json.getString("type"));
                        Tire.getInstance().setIndex(json.getString("index"));
                        Tire.getInstance().setWidth(json.getString("width"));
                    } catch (Exception e) {}

                    try {
                        json = json1.getJSONObject("discs");
                        Discs.getInstance().setDia(json.getString("dia"));
                        Discs.getInstance().setDiametr(json.getString("diametr"));
                        Discs.getInstance().setPsd1(json.getString("psd1"));
                        Discs.getInstance().setPsd2(json.getString("psd2"));
                        Discs.getInstance().setType(json.getString("type"));
                        Discs.getInstance().setVilet(json.getString("vilet"));
                        Discs.getInstance().setWidth(json.getString("width"));
                    } catch (Exception e) {}
                }}
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    void saveToken(String t ) {
        sPref = (SharedPreferences) getSharedPreferences("TOKEN" , Context.MODE_PRIVATE);
        SharedPreferences.Editor ed = sPref.edit();
        ed.putString("token", t);
        ed.commit();

    }
}