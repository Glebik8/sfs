package com.blackfish.a1pedal;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.TextView;

import com.blackfish.a1pedal.ProfileInfo.Discs;
import com.blackfish.a1pedal.ProfileInfo.Profile_Info;
import com.blackfish.a1pedal.ProfileInfo.Tire;
import com.blackfish.a1pedal.ProfileInfo.User;
import com.blackfish.a1pedal.tools_class.PostRes;
import com.blackfish.a1pedal.tools_class.Save;
import com.blackfish.a1pedal.tools_class.showToast;
import com.fadeedittext.FadeEditText;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Objects;

import static android.app.Activity.RESULT_OK;

public class Main_Profile_S_Fragment  extends Fragment {

   TextView NameTextView , JobTypeText , NextTextView , day0, day1, day2, day3, day4, day5, day6, ClockTextView, Cansel, Choose, BackText;
    LinearLayout ChooseLayout;
    EditText NumberTextView, SiteTextView  ;
    FadeEditText GeoTextView;
ImageView TimeImage , GeoImage;
    String token;

    JSONObject data;
    static final private int CHOOSE_THIEF = 0;
    int d1, d2, d3, d4, d0, d5 ,d6 ;
     String [] wekends1 = {"0","0","0","0","0","0","0"} ;
    String [] wekends;
    String [] time ;
    NumberPicker picker1,picker2;
    String []  time1;
    public Main_Profile_S_Fragment() {
    }

    public static Main_Profile_S_Fragment newInstance() {
        return new Main_Profile_S_Fragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.main_par_s_fram, container, false);
        NameTextView = view.findViewById(R.id.NameTextView);
        JobTypeText = view.findViewById(R.id.JobTypeTextView);
        NextTextView = view.findViewById(R.id.NextTextView);
        NumberTextView = view.findViewById(R.id.NumberTextView);
        SiteTextView = view.findViewById(R.id.SiteTextView);
        GeoTextView = view.findViewById(R.id.GeoTextView);
        ClockTextView = view.findViewById(R.id.ClockTextView);
        TimeImage= view.findViewById(R.id.TimeImage);
        GeoImage = view.findViewById(R.id.GeoImage);

        BackText=view.findViewById(R.id.backText3);

        Cansel=view.findViewById(R.id.Cansel);
        Choose  =view.findViewById(R.id.Choose);
        ChooseLayout = view.findViewById(R.id.ChooseLayout);
        token= Profile_Info.getInstance().getToken();
        NameTextView.setText(User.getInstance().getName());
        JobTypeText.setText(User.getInstance().getType());
        String ph = User.getInstance().getPhone();
        NumberTextView.setText(ph);
        if ((!User.getInstance().getSite().equals("null"))){
        SiteTextView.setText(User.getInstance().getSite());}

            JobTypeText.setText(User.getInstance().getWork());



        String s = User.getInstance().getStreet().replace("\n","");
        if ((!s.equals("null"))){
        GeoTextView.setText(s);}
        if ((!User.getInstance().getTimeFrom().equals("null")) && (!User.getInstance().getTimeFrom().equals(""))){
        ClockTextView.setText(User.getInstance().getTimeFrom()+"-"+ User.getInstance().getTimeTo());}
        else { ClockTextView.setText("24 часа");  }

        time= new String[]{
                "00:00","00:15","00:30","00:45","01:00","01:15","01:30","01:45","02:00","02:15",
                "02:30","02:45","03:00","03:15","03:30","03:45","04:00","04:15","04:30",
                "04:45","05:00","05:15","05:30","05:45","06:00","06:15","06:30","06:45",
                "07:00","07:15","07:30","07:45","08:00","08:15","08:30","08:45","09:00",
                "09:15","09:30","09:45","10:00","10:15","10:30","10:45","11:00","11:15",
                "11:30","11:45","12:00","12:15","12:30","12:45","13:00","13:15","13:30",
                "13:45","14:00","14:15","14:30","14:45","15:00","15:15","15:30","15:45",
                "16:00","16:15","16:30","16:45","17:00","17:15","17:30","17:45","18:00",
                "18:15","18:30","18:45","19:00","19:15","19:30","19:45","20:00","20:15",
                "20:30","20:45","21:00","21:15","21:30","21:45","22:00","22:15","22:30",
                "22:45","23:00","23:15","23:30","23:45"
        };

        picker2 = view.findViewById(R.id.numberPicker2);
        picker1 =  view.findViewById(R.id.numberPicker1);

        day0= view.findViewById(R.id.day0);
        day1= view.findViewById(R.id.day1);
        day2= view.findViewById(R.id.day2);
        day3= view.findViewById(R.id.day3);
        day4= view.findViewById(R.id.day4);
        day5= view.findViewById(R.id.day5);
        day6= view.findViewById(R.id.day6);

        wekIn();

        NumberTextView.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus) {
                    ((MainActivity)getActivity()).hideNav();
                    NextTextView.setVisibility(View.VISIBLE);
                }
            }
        });

                SiteTextView.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                    @Override
                    public void onFocusChange(View v, boolean hasFocus) {
                        if(hasFocus) {
                            ((MainActivity)getActivity()).hideNav();
                            NextTextView.setVisibility(View.VISIBLE);
                        }
                    }
                });


        GeoTextView.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus) {
                    ((MainActivity)getActivity()).hideNav();
                    NextTextView.setVisibility(View.VISIBLE);
                }else{

                }

        }});
        TimeImage.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                ((MainActivity)getActivity()).hideNav();
                NextTextView.setVisibility(View.VISIBLE);
                ClockTextView.setText("24 часа");
            }
        });

        Cansel.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                ChooseLayout.setVisibility(View.GONE);
            }
        });
        ClockTextView.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                ChooseLayout.setVisibility(View.VISIBLE);
                ((MainActivity)getActivity()).hideNav();
                NextTextView.setVisibility(View.VISIBLE);
                picker1.setDisplayedValues(null);
                picker1.setMinValue(0);
                picker1.setMaxValue(time.length - 1);
                picker1.setDisplayedValues(time);
                picker1.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);
              if (picker1.getValue()>0)
              {
                  time1 = new String[time.length-picker1.getValue()];
                  int  k = picker1.getValue();
                  for (int i= 0; i<time.length-picker1.getValue(); i++ )
                  {
                      time1[i] = time[k];
                      k++;
                  }

                  picker2.setDisplayedValues(null);
                  picker2.setMinValue(0);
                  picker2.setMaxValue(time1.length - 1);
                  picker2.setDisplayedValues(time1);
                  picker2.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);
              }
              else {
                  picker2.setDisplayedValues(null);
                  picker2.setMinValue(0);
                  picker2.setMaxValue(time.length - 1);
                  picker2.setDisplayedValues(time);
                  picker2.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);
              }

            }});

        picker1.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal){
               time1 = new String[time.length-newVal];
             int  k = newVal;
                for (int i= 0; i<time.length-newVal; i++ )
                {
                    time1[i] = time[k];
                    k++;
                }

                picker2.setDisplayedValues(null);
                picker2.setMinValue(0);
                picker2.setMaxValue(time1.length - 1);
                picker2.setDisplayedValues(time1);
                picker2.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);

            }
        });






        Choose.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                 ClockTextView.setText(time[picker1.getValue()]+"-"+time1[picker2.getValue()]);
                ChooseLayout.setVisibility(View.GONE);

            }
        });
        BackText.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                ((MainActivity) Objects.requireNonNull(getActivity())).showNav();
                Objects.requireNonNull(getActivity()).onBackPressed();

            }
        });
        day0.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                ((MainActivity)getActivity()).hideNav();
                NextTextView.setVisibility(View.VISIBLE);
                if (wekends1[0].equals("0"))
                { Selectr1(day0 , true);
                    wekends1[0]="1"; }
                else {
                    Selectr1(day0 , false);
                    wekends1[0]="0";
                }
            }});
        day1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                ((MainActivity)getActivity()).hideNav();
                NextTextView.setVisibility(View.VISIBLE);
                if (wekends1[1].equals("0"))
                { Selectr1(day1 , true);
                    wekends1[1]="1"; }
                else {
                    Selectr1(day1 , false);
                    wekends1[1]="0";
                }
            }});
        day2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                ((MainActivity)getActivity()).hideNav();
                NextTextView.setVisibility(View.VISIBLE);
                if (wekends1[2].equals("0"))
                { Selectr1(day2 , true);
                    wekends1[2]="1"; }
                else {
                    Selectr1(day2 , false);
                    wekends1[2]="0";
                }
            }});
        day3.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                ((MainActivity)getActivity()).hideNav();
                NextTextView.setVisibility(View.VISIBLE);
                if (wekends1[3].equals("0"))
                { Selectr1(day3 , true);
                    wekends1[3]="1"; }
                else {
                    Selectr1(day3 , false);
                    wekends1[3]="0";
                }
            }});
        day4.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                ((MainActivity)getActivity()).hideNav();
                NextTextView.setVisibility(View.VISIBLE);
                if (wekends1[4].equals("0"))
                { Selectr1(day4 , true);
                    wekends1[4]="1"; }
                else {
                    Selectr1(day4 , false);
                    wekends1[4]="0";
                }
            }});
        day5.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                ((MainActivity)getActivity()).hideNav();
                NextTextView.setVisibility(View.VISIBLE);
                if (wekends1[5].equals("0"))
                { Selectr1(day5 , true);
                    wekends1[5]="1"; }
                else {
                    Selectr1(day5 , false);
                    wekends1[5]="0";
                }
            }});
        day6.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                ((MainActivity)getActivity()).hideNav();
                NextTextView.setVisibility(View.VISIBLE);
                if (wekends1[6].equals("0"))
                { Selectr1(day6 , true);
                    wekends1[6]="1"; }
                else {
                    Selectr1(day6 , false);
                    wekends1[6]="0";
                }
            }});

        NextTextView.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                data = new JSONObject();
                // Проверка
                int l =  NumberTextView.getText().toString().length();
                if (!NumberTextView.getText().toString().equals("+7") && (l<=12) && l>10) {

                    try {
                        data.put("gps", User.getInstance().getGps());
                        if (!ClockTextView.getText().toString().equals("24 часа")) {
                            String[] s = ClockTextView.getText().toString().split("-");
                            data.put("timeFrom", s[0]);
                            data.put("timeTo", s[1]);
                        } else {
                            data.put("timeFrom", "");
                            data.put("timeTo", "");
                        }
                        data.put("street", GeoTextView.getText().toString());
                        String w = "";
                        for (int i = 0; i < 7; i++) {
                            if (wekends1[i].equals("1")) {
                                w = w + i + ",";
                            }
                        }
                        if (!w.equals("")) {
                            w = w.substring(0, w.length() - 1);
                        }
                        data.put("weekends", w);
                        data.put("site", SiteTextView.getText().toString());
                        data.put("phone", NumberTextView.getText().toString());
                        SetInfoServ mt = new SetInfoServ();
                        mt.execute();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                else {
                    showToast.showToast("Не верно веден телефон", false,getActivity());
                }
            }

        });

        GeoImage.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent questionIntent = new Intent(getActivity(),
                        MapsActivity.class);
                startActivityForResult(questionIntent, CHOOSE_THIEF);
            }});


        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CHOOSE_THIEF) {
            if (resultCode == RESULT_OK) {

                GeoTextView.setText(User.getInstance().getStreet());
                NextTextView.setVisibility(View.VISIBLE);
            }else {
             //   infoTextView.setText(""); // стираем текст
            }
        }
    }
    public void wekIn ()
    {
            if (!User.getInstance().getWeekends().equals("null") &&!User.getInstance().getWeekends().equals(""))
            {
                wekends= User.getInstance().getWeekends().split(",");
             for (int i = 0 ; i<wekends.length; i++)
          {
               wekends1 [Integer.parseInt(wekends[i])]= (String) String.valueOf(1);
               Init(Integer.parseInt(wekends[i]));           }
            }
            else
            {}
    }
    public void   Init (int i)
    { if (i == 0 ) { Selectr1(day0 , true);}
        if (i == 1 ) {Selectr1(day1 , true);}
        if (i == 2 ) {Selectr1(day2 , true);}
        if (i == 3 ) {Selectr1(day3 , true);}
        if (i == 4 ) {Selectr1(day4 , true);}
        if (i == 5 ) {Selectr1(day5 , true);}
        if (i == 6 ) {Selectr1(day6 , true);} }
    public void   Selectr1 (TextView t , Boolean q)
    {
        if (q){
            t.setBackgroundColor((Color.parseColor("#5d9ece")));
            t.setTextColor((Color.parseColor("#ffffff")));
        }
        else {
            t.setBackgroundColor((Color.parseColor("#ffffff")));
            t.setTextColor((Color.parseColor("#5d9ece")));
        }
    }

    private class SetInfoServ extends AsyncTask<Void, Void, String> {
        String resultJson = "";

        @Override
        protected String doInBackground(Void... params) {

            PostRes example = new PostRes();
            String response = "";
            try {
                response = example.post("http://185.213.209.188/api/updateserviceinfo/", data.toString(), "Token " + token);
                String k = response;

            } catch (IOException e) {
                e.printStackTrace();
            }
            return response;
        }

        @Override
        protected void onPostExecute(String response) {
            super.onPostExecute(response);
            if (!response.equals("")) {
            try {
                JSONObject jsonObject = new JSONObject(response);
                try {  if (jsonObject.getString("status").equals("Error")) {
                   if (jsonObject.getString("msg").equals("phone already exist"))
                   {showToast.showToast("Этот телефон уже занят", false, getActivity());}
                   else
                   {showToast.showToast(jsonObject.getString("msg"), false, getActivity());}
               }
                } catch (JSONException err) {
                }
               { SetProfileInfo(jsonObject);
                 Save.ProfJson(jsonObject,getActivity());
                ((MainActivity)getActivity()).showNav();
                getActivity().onBackPressed();}
            } catch (JSONException err) {
            }
        } else {
                showToast.showToast("Отсутствует интернет соедение", false,getActivity());
                ((MainActivity)getActivity()).showNav();
                getActivity().onBackPressed();
            }
    }}



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
}
