package com.blackfish.a1pedal;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.blackfish.a1pedal.Auth_Registr.LoginActivity;
import com.blackfish.a1pedal.ProfileInfo.Discs;
import com.blackfish.a1pedal.ProfileInfo.Profile_Info;
import com.blackfish.a1pedal.ProfileInfo.Tire;
import com.blackfish.a1pedal.ProfileInfo.User;
import com.blackfish.a1pedal.tools_class.Save;
import com.droidnet.DroidListener;
import com.droidnet.DroidNet;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

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

public class FirstActivity extends AppCompatActivity implements DroidListener {
String Token;
        SharedPreferences sPref;
    private DroidNet mDroidNet;
        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.first_layout);

            mDroidNet = DroidNet.getInstance();
            mDroidNet.addInternetConnectivityListener(this);
        }


        String    loadText() {
            sPref = (SharedPreferences) getSharedPreferences("TOKEN" , Context.MODE_PRIVATE);
            String savedText = sPref.getString("token", "");
            return  savedText;
        }

    private  class GetIn extends AsyncTask<Void, Void, String> {
        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;
        String resultJson = "";
        @Override
        protected String doInBackground(Void... params) {
            URL url = null;
            try {
                url = new URL("http://185.213.209.188/api/getuserinfo/");
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
            try {
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestProperty ("Authorization", "Token "+ Token );
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
                Save.ProfJson(jsonObject,FirstActivity.this);

                SetProfileInfo(jsonObject);
                Profile_Info.getInstance().setToken ( Token);
                ChekResp (jsonObject);
            }catch (JSONException ignored){
                Intent intent = new Intent(FirstActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
                mDroidNet.removeInternetConnectivityChangeListener(FirstActivity.this);

            }
        }
    }
    JSONObject loadProfJson () {
        SharedPreferences   sPref = (SharedPreferences) getSharedPreferences("PROFILE" , Context.MODE_PRIVATE);
        String savedText = sPref.getString("profile", "");
        savedText = savedText.replace("\"nameValuePairs\":{","");
        savedText = savedText.replace("\"null\"","null");
        JSONObject jsonObject= null;
        try {
            jsonObject = new JSONObject(savedText);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return  jsonObject ;
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

    private void netIsOn(){
        if (loadText().equals(""))
        {
            Intent intent = new Intent(FirstActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
            mDroidNet.removeInternetConnectivityChangeListener(this);
        }
        else
        {
            Token = loadText();
            GetIn mt = new GetIn();
            mt.execute();
        }
    }
    private void netIsOff(){
        if (loadText().equals(""))
        {
            Intent intent = new Intent(FirstActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
            mDroidNet.removeInternetConnectivityChangeListener(this);

        }
        else
        {
            Token = loadText();
            Profile_Info.getInstance().setToken(Token);
           JSONObject js = loadProfJson();
           if (js!=null)
           {
               try {
                   js = js.getJSONObject("user");
               } catch (JSONException e) {
                   e.printStackTrace();
               }
               SetProfileInfo(js);
               ChekResp (js);
           }
           else {
               Intent intent = new Intent(FirstActivity.this, LoginActivity.class);
               startActivity(intent);
               finish();
               mDroidNet.removeInternetConnectivityChangeListener(this);

           }
        }
    }

    public void ChekResp (JSONObject jsonObject) {
            try {
        JSONObject user =jsonObject ;
        String type = user.getString("type");
        if (type.equals("driver")){
            String brand = user.getString("brand");
            String model = user.getString("model");
            if (brand.equals("") || brand.equals("null")  )
            {
                Intent intent = new Intent(FirstActivity.this, Parametr_v_Activity.class);
                startActivity(intent);
                finish();
                mDroidNet.removeInternetConnectivityChangeListener(this);

            }
            else {
                SetProfileInfo(jsonObject);
                Intent intent = new Intent(FirstActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
                mDroidNet.removeInternetConnectivityChangeListener(this);
            }}
        else {
            String work = user.getString("work");
            String name = user.getString("name");
            if (work.equals("") && name.equals("")|| work.equals("null") && name.equals("null"))
            {
                Intent intent = new Intent(FirstActivity.this, Parametr_s_Activity.class);
                startActivity(intent);
                finish();
                mDroidNet.removeInternetConnectivityChangeListener(this);

            }
            else {
                SetProfileInfo(jsonObject);
                Intent intent = new Intent(FirstActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
                mDroidNet.removeInternetConnectivityChangeListener(this);

            }}

    }catch (JSONException ignored){
                Intent intent = new Intent(FirstActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
                mDroidNet.removeInternetConnectivityChangeListener(this);

            }
        }



    public void SetProfileInfo (JSONObject json1)
    {
        try {
            JSONObject json = json1;

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
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
        }
