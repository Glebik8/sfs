package com.blackfish.a1pedal.Auth_Registr;

import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.blackfish.a1pedal.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class RememberPasswordActivity  extends AppCompatActivity {
    TextView EnterText , RegistrText, RememText;
    EditText EmailText;
String email;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.remember_password);
        EmailText = findViewById(R.id.EmailText);
        EnterText= findViewById(R.id.EnterText);
        RegistrText= findViewById(R.id.RegistrText);
        RememText = findViewById(R.id.RememPasswordText);
        EnterText.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(RememberPasswordActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }});
        RegistrText.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(RememberPasswordActivity.this, RegistrActivity.class);
                startActivity(intent);
                finish();
            }});
        RememText.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
             /*   Intent intent = new Intent(RememberPasswordActivity.this, RegistrActivity.class);
                startActivity(intent);*/
             if (!EmailText.getText().toString().equals(""))
             {
                 email = EmailText.getText().toString();
                 if (isValidEmail(email) ){
                         Restorepwd mt = new Restorepwd();
                         mt.execute();
                 } else {showToast("Неверно указан адрес электронной почты", false);}

             }
             else {
                 showToast("Поле не заполнено", false);
             }



            }});


    }
    private  class Restorepwd extends AsyncTask<Void, Void, String> {
        String resultJson = "";
        @Override
        protected String doInBackground(Void... params) {

            PostRes example = new PostRes();
            String response="";
            try {

                JSONObject data = new JSONObject();

                    data.put("email", email);

                response = example.post("http://185.213.209.188/api/restorepwd/", data.toString());
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
                String status = jsonObject.getString("status");
              if (status.equals("OK")) {
                  showToast("Вам на почту отправлена ссылка для восстановления пароля", true);
              }
              else
              {  showToast("Пользователь не найден", false);}
            }catch (JSONException err){
                showToast("Пользователь не найден", false);
            }

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
    public void showToast( String text, boolean ok) {
        Toast toast3 = Toast.makeText(getApplicationContext(),
                text, Toast.LENGTH_LONG);
        toast3.setGravity(Gravity.CENTER, 0, 0);
        LinearLayout toastContainer = (LinearLayout) toast3.getView();
        // toastContainer.setBackgroundColor((Color.parseColor("#f2f2f2")));
        toastContainer.setBackgroundResource(R.drawable.toast_border);
        toastContainer.setPadding(50,50,50,50);
        TextView v = (TextView) toast3.getView().findViewById(android.R.id.message);
        v.setTextColor(Color.parseColor("#000000"));
        v.setTextSize(17);
        v.setPadding(0,30,0,0);
        ImageView Icon = new ImageView(getApplicationContext());
        Icon.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,100));

        if (ok){
            Icon.setImageResource(R.drawable.ic_checked);
        }
        else  {
            Icon.setImageResource(R.drawable.ic_cancel);
        }
        toastContainer.addView(Icon, 0);
        toast3.show();
    }
    public final static boolean isValidEmail(CharSequence target) {
        if (TextUtils.isEmpty(target)) {
            return false;
        } else {
            return android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
        }
    }
}
