package com.blackfish.a1pedal.Auth_Registr;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.blackfish.a1pedal.MainActivity;
import com.blackfish.a1pedal.Parametr_s_Activity;
import com.blackfish.a1pedal.Parametr_v_Activity;
import com.blackfish.a1pedal.ProfileInfo.Discs;
import com.blackfish.a1pedal.ProfileInfo.Profile_Info;
import com.blackfish.a1pedal.ProfileInfo.Tire;
import com.blackfish.a1pedal.ProfileInfo.User;
import com.blackfish.a1pedal.R;
import com.blackfish.a1pedal.tools_class.ChekInter;
import com.blackfish.a1pedal.tools_class.Save;
import com.blackfish.a1pedal.tools_class.showToast;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Objects;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class LoginActivity extends AppCompatActivity {
    EditText EmailText, PasswordText ;
    TextView EnterText , RegistrText, RememText;
    String  fcmToken="1";
    JSONObject data;
    int selektr=1;
    SharedPreferences sPref;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_layout);
        EmailText= findViewById(R.id.EmailText);
        PasswordText= findViewById(R.id.PasswordText);
        EnterText= findViewById(R.id.EnterText);
        RegistrText= findViewById(R.id.RegistrText);
        RememText = findViewById(R.id.RememPasswordText);
        EnterText.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (ChekInter.isConnected(LoginActivity.this)) {
                    String email = EmailText.getText().toString();
                    String password = PasswordText.getText().toString();
                    if (!password.equals("") && !email.equals("")) {


                        email = EmailText.getText().toString();
                        if (isValidEmail(email)) {
                            try {
                                data = new JSONObject();
                                data.put("email", email);
                                data.put("password", password);
                                data.put("os", "android");
                                data.put("token", fcmToken);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            Auth mt = new Auth();
                            mt.execute();
                        } else {
                            showToast.showToast("Неверно указан адрес электронной почты", false, LoginActivity.this);
                        }

                    } else {
                        showToast.showToast("Не все поля заполнены", false, LoginActivity.this);
                    }
                }  else {
                    showToast.showToast("Отсутствует интернет соедение", false,LoginActivity.this);}
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
        RegistrText.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
              if (ChekInter.isConnected(LoginActivity.this))
              {
                Intent intent = new Intent(LoginActivity.this, RegistrActivity.class);
                startActivity(intent);}
              else {
                  showToast.showToast("Отсутствует интернет соедение", false,LoginActivity.this);}
            }});
        RememText.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (ChekInter.isConnected(LoginActivity.this)) {
                    Intent intent = new Intent(LoginActivity.this, RememberPasswordActivity.class);
                    startActivity(intent);
                }  else {
                    showToast.showToast("Отсутствует интернет соедение", false,LoginActivity.this);}
            }
            });
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
        private  class Auth extends AsyncTask<Void, Void, String> {
            String resultJson = "";

            @Override
            protected String doInBackground(Void... params) {

                PostRes example = new PostRes();
                    String response="";
                    try {
                        response = example.post("http://185.213.209.188/api/auth/", data.toString());
                        String k =response;

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
                    String token = jsonObject.getString("token");
                Profile_Info.getInstance().setToken(token);
                    saveToken (token);

                    JSONObject user = jsonObject.getJSONObject("user");
                    String type = user.getString("type");
                  if (type.equals("driver")){
                      String brand = user.getString("brand");
                      String model = user.getString("model");
                     if (brand.equals("") && model.equals(""))
                     {
                         Intent intent = new Intent(LoginActivity.this, Parametr_v_Activity.class);
                         startActivity(intent);
                     }
                     else {
                    //     Profile_Info profile_info = new Profile_Info();
                         SetProfileInfo(jsonObject);
                         Save.ProfJson(jsonObject,LoginActivity.this);
                         Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                         startActivity(intent);
                         finish();
                     }}
                  else {
                          String work = user.getString("work");
                          String name = user.getString("name");
                      if (work.equals("") && name.equals(""))
                      {
                          Intent intent = new Intent(LoginActivity.this, Parametr_s_Activity.class);
                          startActivity(intent);
                      }
                      else {
                          SetProfileInfo(jsonObject);
                          Save.ProfJson(jsonObject,LoginActivity.this);
                          Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                          startActivity(intent);
                          finish();

                      }}

                }catch (JSONException err){
                    String t = err.toString();
                    if (response.equals(""))
                    {
                        showToast.showToast("Отсутствует интернет соедение", false,LoginActivity.this);
                    }
                    else {
                    showToast.showToast("Не верный логин и/или пароль", false,LoginActivity.this);}
                }
            }

        }

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
             try {
                 json = json.getJSONObject("tire");
                 Tire.getInstance().setDiametr(json.getString("diametr"));
                 Tire.getInstance().setHeight(json.getString("height"));
                 Tire.getInstance().setRunflat(json.getString("runflat"));
                 Tire.getInstance().setThrons(json.getString("throns"));
                 Tire.getInstance().setType(json.getString("type"));
                 Tire.getInstance().setIndex(json.getString("index"));
                 Tire.getInstance().setWidth(json.getString("width"));
             } catch (Exception e) {
                 Tire.getInstance().setDiametr("");
                 Tire.getInstance().setHeight("");
                 Tire.getInstance().setRunflat("");
                 Tire.getInstance().setThrons("");
                 Tire.getInstance().setType("");
                 Tire.getInstance().setIndex("");
                 Tire.getInstance().setWidth("");
             }

             try {
                 json = json.getJSONObject("discs");
                 Discs.getInstance().setDia(json.getString("dia"));
                 Discs.getInstance().setDiametr(json.getString("diametr"));
                 Discs.getInstance().setPsd1(json.getString("psd1"));
                 Discs.getInstance().setPsd2(json.getString("psd2"));
                 Discs.getInstance().setType(json.getString("type"));
                 Discs.getInstance().setVilet(json.getString("vilet"));
                 Discs.getInstance().setWidth(json.getString("width"));
             } catch (Exception e) {
                 Discs.getInstance().setDia("");
                 Discs.getInstance().setDiametr("");
                 Discs.getInstance().setPsd1("");
                 Discs.getInstance().setPsd2("");
                 Discs.getInstance().setType("");
                 Discs.getInstance().setVilet("");
                 Discs.getInstance().setWidth("");}
         }
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