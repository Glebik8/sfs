package com.blackfish.a1pedal;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.TextView;

import com.blackfish.a1pedal.Auth_Registr.LoginActivity;
import com.blackfish.a1pedal.ProfileInfo.Discs;
import com.blackfish.a1pedal.ProfileInfo.Profile_Info;
import com.blackfish.a1pedal.ProfileInfo.Tire;
import com.blackfish.a1pedal.ProfileInfo.User;
import com.blackfish.a1pedal.tools_class.PostRes;
import com.blackfish.a1pedal.tools_class.Save;
import com.blackfish.a1pedal.tools_class.showToast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;

public class Parametr_v_Activity extends AppCompatActivity {
            NumberPicker picker1;
            int idVib = 1;
            String [] model,marka,age;
            String token;
            JSONArray Brand, Model;
            TextView TapBrendText, TapModelText,TapAgeText, Cansel , Choose,NextTextView;
            EditText NumberTextView;
            LinearLayout ChooseLayout;
            Context t;
    JSONObject data;
            int sizeBr, sizeMo;
            @Override
            protected void onCreate(Bundle savedInstanceState) {
                super.onCreate(savedInstanceState);
                setContentView(R.layout.registr_v_layout);
                NextTextView= findViewById(R.id.NextTextView);
                token= Profile_Info.getInstance().getToken();
                TapBrendText= findViewById(R.id.TapBrandText);
                TapModelText= findViewById(R.id.TapModelText);
                TapAgeText= findViewById(R.id.TapAgeText);
                Cansel= findViewById(R.id.Cansel);
                Choose= findViewById(R.id.Choose);
                ChooseLayout= findViewById(R.id.ChooseLayout);
                t=Parametr_v_Activity.this;
                NumberTextView = findViewById(R.id.NumberTextView);
        ChooseLayout= findViewById(R.id.ChooseLayout);
        picker1 =(NumberPicker) findViewById(R.id.numberPicker1);
        try {
            InputStream is = t.getResources().openRawResource(R.raw.brand);
            Brand =  new JSONArray(loadJSONFromAsset(is));
            int sizeBr =Brand.length();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        try {
            InputStream is = t.getResources().openRawResource(R.raw.model);
            Model =  new JSONArray(loadJSONFromAsset(is));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        TapBrendText.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                idVib = 1;
                ChooseLayout.setVisibility(View.VISIBLE);

                marka = new String[Brand.length()];
                for (int i = 0 ; i<Brand.length(); i++)
                {
                    JSONObject jsonobject = null;
                    try {
                        jsonobject = Brand.getJSONObject(i);
                        String title    = jsonobject.getString("title");
                        marka [i] = title;

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                picker1.setDisplayedValues(null);
                picker1.setMinValue(0);
                picker1.setMaxValue(201);
                picker1.setDisplayedValues( marka);
                picker1.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);
                TapModelText.setText("");
                TapAgeText.setText("");

            }});

        TapAgeText.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String parent = (String) TapBrendText.getText();
                if (!parent.equals("")) {
                    idVib = 3;
                    ChooseLayout.setVisibility(View.VISIBLE);
                    age = new String[2020 - 1990];
                    for (int i = 1990; i < 2020; i++) {

                        age[i - 1990] = String.valueOf(i);
                    }
                    picker1.setDisplayedValues(null);
                    picker1.setMinValue(0);
                    picker1.setMaxValue(age.length - 1);
                    picker1.setDisplayedValues(age);
                    picker1.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);

                }
            }});
        TapModelText.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
               String parent = (String) TapBrendText.getText();
               if (!parent.equals("")) {
                   idVib = 2;
                   ChooseLayout.setVisibility(View.VISIBLE);

                   model = new String[Model.length()];
                   int kol = 1;
                   String [] k = new String[1000];
                   for (int i = 0; i < Model.length(); i++) {
                       JSONObject jsonobject = null;
                       try {
                           jsonobject = Model.getJSONObject(i);
                           String parent1 = jsonobject.getString("parent");
                           String title = jsonobject.getString("title");
                           if (parent.equals(parent1))
                           {
                               k[kol]= title;
                               model =  new String[kol];
                               for (int j = 0 ; j<kol ; j++)
                               {
                                   model [j] = k[j+1];
                               }
                            kol++;
                           } } catch (JSONException e) { e.printStackTrace(); } }
                   String [] jas = model;
                   picker1.setDisplayedValues(null);
                   picker1.setMinValue(0);
                   picker1.setMaxValue(model.length - 1);
                   picker1.setDisplayedValues(model);
                   picker1.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS); } }});


Cansel.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        ChooseLayout.setVisibility(View.GONE);
                    }});

                NextTextView.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {

                        if (!TapAgeText.getText().toString().equals("") && !TapBrendText.getText().toString().equals("") && !TapModelText.getText().toString().equals("")) {
                            try {
                                data = new JSONObject();
                                data.put("brand", TapBrendText.getText().toString());
                                data.put("model", TapModelText.getText().toString());
                                data.put("phone", NumberTextView.getText().toString());
                                data.put("year", TapAgeText.getText().toString());
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            SetInfo mt = new SetInfo();
                            mt.execute();
                        }
                        else
                        {
                            showToast.showToast("Не все поля заполнены", false, Parametr_v_Activity.this);
                        }
                    }});

    Choose.setOnClickListener(new View.OnClickListener() {
      public void onClick(View v) {
      if (idVib==1){
        TapBrendText.setText(marka[picker1.getValue()]);

        }
        if (idVib==2){
            TapModelText.setText(model[picker1.getValue()]);
            picker1.clearFocus();
        }
        if (idVib==3){
            TapAgeText.setText(age[picker1.getValue()]);
            picker1.clearFocus();
        }
        ChooseLayout.setVisibility(View.GONE);
       }});
    }
    public String loadJSONFromAsset(InputStream is) {
        String json = null;
        try {
       //     InputStream is = t.getResources().openRawResource(R.raw.brand);
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;
    }

    private  class SetInfo extends AsyncTask<Void, Void, String> {
        String resultJson = "";

        @Override
        protected String doInBackground(Void... params) {

            PostRes example = new PostRes();
            String response="";
            try {
                response = example.post("http://185.213.209.188/api/setdriverinfo/", data.toString(), "Token "+token);
                String k =response;

            } catch (IOException e) {
                e.printStackTrace();
            }

            return response;
        }

        @Override
        protected void onPostExecute(String response) {
            super.onPostExecute(response);
            JSONObject jsonObject = null;
            if (!response.equals("")){
            try {

                 jsonObject = new JSONObject(response);
                if (!jsonObject.getString("status").equals("Error"))
                {
               }
                else {
                    if (jsonObject.getString("msg").equals("phone already exist"))
                    {
                        showToast.showToast("Такой номер уже используется", false, Parametr_v_Activity.this);
                    }
                }
            }catch (JSONException err) {
                if (!(jsonObject == null)) {
                    SetProfileInfo(jsonObject);
                    Save.ProfJson(jsonObject, Parametr_v_Activity.this);
                    Intent intent = new Intent(Parametr_v_Activity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
            }
            else {
                showToast.showToast("Отсутствует интернет соедение", false,Parametr_v_Activity.this);
                Intent intent = new Intent(Parametr_v_Activity.this, LoginActivity.class);
                startActivity(intent);
                finish();}
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
            if (User.getInstance().getType().equals("driver")){
                try {
                    json = json1.getJSONObject("tire");
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
                    json = json1.getJSONObject("discs");
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
                    Discs.getInstance().setWidth("");
                }
                }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}