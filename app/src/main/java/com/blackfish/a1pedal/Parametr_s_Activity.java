package com.blackfish.a1pedal;

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

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

public class Parametr_s_Activity  extends AppCompatActivity {
    NumberPicker picker1;
    int idVib = 1;
    String [] job;
    String  token;
    EditText NameTextView , NumberTextView ;
    TextView TapJobText, Cansel , Choose, NextTextView;
    LinearLayout ChooseLayout;
    JSONObject data;
    int sizeBr, sizeMo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.registr_s_layout);
        TapJobText= findViewById(R.id.TapJobText);
        token= Profile_Info.getInstance().getToken();
        NameTextView= findViewById(R.id.NameTextView);
        NumberTextView= findViewById(R.id.NumberTextView);
        NextTextView= findViewById(R.id.NextTextView);
        Cansel= findViewById(R.id.Cansel);
        Choose= findViewById(R.id.Choose);
        ChooseLayout= findViewById(R.id.ChooseLayout);
        picker1 =(NumberPicker) findViewById(R.id.numberPicker1);

        TapJobText.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                ChooseLayout.setVisibility(View.VISIBLE);

               job = new String[]{"Автосервис", "Шиномонтаж"};
                picker1.setDisplayedValues(null);
                picker1.setMinValue(0);
                picker1.setMaxValue(1);
                picker1.setDisplayedValues( job);
                picker1.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);
            }});
        Cansel.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                ChooseLayout.setVisibility(View.GONE);
            }});

        Choose.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                    TapJobText.setText(job[picker1.getValue()]);
                    picker1.clearFocus();
                ChooseLayout.setVisibility(View.GONE);
            }});

        NextTextView.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                if (!NumberTextView.getText().toString().equals("+7") && !NameTextView.getText().toString().equals("") && !TapJobText.getText().toString().equals(""))
                {
                    if (!NumberTextView.getText().toString().equals("+7") && NumberTextView.getText().toString().length()<12 && NumberTextView.getText().toString().length()>10 )
                    {
                        try {
                            data = new JSONObject();
                            data.put("work", TapJobText.getText().toString());
                            data.put("name", NameTextView.getText().toString());
                            data.put("phone", NumberTextView.getText().toString());

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        SetInfo mt = new SetInfo();
                        mt.execute();
                }
                    else {
                        showToast.showToast("Не верно веден телефон", false,Parametr_s_Activity.this);
                    }}
                else
                {
                    showToast.showToast("Не все поля заполнены", false,Parametr_s_Activity.this);
                }
            }});

    }
    private  class SetInfo extends AsyncTask<Void, Void, String> {
        String resultJson = "";

        @Override
        protected String doInBackground(Void... params) {

            PostRes example = new PostRes();
            String response="";
            try {
                response = example.post("http://185.213.209.188/api/setserviceinfo/", data.toString(), "Token "+token);
                String k =response;

            } catch (IOException e) {
                e.printStackTrace();
            }

            return response;
        }

        @Override
        protected void onPostExecute(String response) {
            super.onPostExecute(response);
            if (!response.equals("")){
                try {
                JSONObject jsonObject = new JSONObject(response);
                SetProfileInfo(jsonObject);
                Save.ProfJson(jsonObject,Parametr_s_Activity.this);
                Intent intent = new Intent(Parametr_s_Activity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }catch (JSONException err){

                }
            } else
            {
                showToast.showToast("Отсутствует интернет соедение", false,Parametr_s_Activity.this);
                Intent intent = new Intent(Parametr_s_Activity.this, LoginActivity.class);
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