package com.blackfish.a1pedal;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.blackfish.a1pedal.ProfileInfo.Profile_Info;
import com.blackfish.a1pedal.tools_class.PostRes;
import com.blackfish.a1pedal.tools_class.showToast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import br.com.sapereaude.maskedEditText.MaskedEditText;

public class AddSFriend extends AppCompatActivity {
    String Token;
    MaskedEditText NumberTextView;
TextView CompTextView , BackText;
 JSONObject   data;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.friend_add_fram);
        NumberTextView = findViewById(R.id.NumberTextView);
        CompTextView = findViewById(R.id.CompTextView);
        BackText = findViewById(R.id.BackText);
        Log.d("glebik", Profile_Info.getInstance().getToken());

        CompTextView.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                if (!NumberTextView.getText().toString().equals("+7") )
                {
                    if (!NumberTextView.getText().toString().equals("+7")  && NumberTextView.getText().toString().length()>10 )
                    {

                        try {
                            data = new JSONObject();
                            data.put("type", "request");
                            data.put("phone", NumberTextView.getText().toString());

                        } catch (JSONException e) {
                            Log.d("glebik", e.toString());
                            e.printStackTrace();
                        }
                        SetAddFriend mt = new SetAddFriend();
                        mt.execute();
                    }
                    else {
                        showToast.showToast("Не верно веден телефон", false,AddSFriend.this);
                    }} }});

        BackText.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                        finish();
            }
        });
    }
    class SetAddFriend extends AsyncTask<Void, Void, String> {
        String resultJson = "";

        @Override
        protected String doInBackground(Void... params) {
            String token = Profile_Info.getInstance().getToken();
            PostRes example = new PostRes();
            String response = "";
            try {
                response = example.post("http://185.213.209.188/api/friend/", data.toString(), "Token " + token);
                String k = response;
                Log.d("glebik", k);
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
                try {
                    String st = jsonObject.getString("status");
                    Log.d("glebik", response);
                    if (st.equals("Error")) {
                        showToast.showToast("Номер не найден", false, AddSFriend.this);
                    }
                } catch (JSONException err1) {
                    showToast.showToast("Сервис добавлен", true, AddSFriend.this);
                    Intent intent = new Intent(AddSFriend.this, MainActivity.class);
                    intent.putExtra("id", "3");
                    startActivity(intent);
                    finish();
                }

            } catch (JSONException err) {
            }
        }
    }
}
