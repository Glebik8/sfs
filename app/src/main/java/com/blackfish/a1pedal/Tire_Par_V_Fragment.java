package com.blackfish.a1pedal;

import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
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

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

public class Tire_Par_V_Fragment extends Fragment {
TextView TapTireWText , TapTireHText, TapTireDText , TapTireTypeText , yesThornssel,noThornssel, yesRunFlatsel, noRunFlatsel, Cansel, Choose, NextTextView, BackText;
EditText IndexTextView ;
    LinearLayout ChooseLayout;
    JSONObject data;
    String token;
    int idVib = 1 , idRunFlat = -1 , idThorns = -1;
String [] widths , heights , diametrs , tire_types;

    NumberPicker picker1;

    public Tire_Par_V_Fragment() {
    }

    public static Tire_Par_V_Fragment newInstance() {
        return new Tire_Par_V_Fragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.tire_par_v_fram, container, false);
        TapTireWText =view.findViewById(R.id.TapTireWText);
        TapTireHText =view.findViewById(R.id.TapTireHText);
        TapTireDText=view.findViewById(R.id.TapTireDText);
        TapTireTypeText=view.findViewById(R.id.TapTireTypeText);
        yesThornssel=view.findViewById(R.id.yesThornssel);
        noThornssel=view.findViewById(R.id.noThornssel);
        yesRunFlatsel=view.findViewById(R.id.yesRunFlatsel);
        noRunFlatsel=view.findViewById(R.id.noRunFlatsel);
        BackText=view.findViewById(R.id.backText);

        Cansel=view.findViewById(R.id.Cansel);
        Choose  =view.findViewById(R.id.Choose);
        ChooseLayout = view.findViewById(R.id.ChooseLayout);

        NextTextView=view.findViewById(R.id.NextTextView);
        token= Profile_Info.getInstance().getToken();
        IndexTextView=view.findViewById(R.id.IndexTextView);
        picker1 = (NumberPicker) view.findViewById(R.id.numberPicker1);

        TapTireWText.setText(Tire.getInstance().getWidth());
        TapTireHText.setText(Tire.getInstance().getHeight());
        TapTireDText.setText(Tire.getInstance().getDiametr());
        TapTireTypeText.setText(Tire.getInstance().getType());

     //   !Tire.getInstance().getRunflat().equals("") &&
        if ( Tire.getInstance().getRunflat() != null && !Tire.getInstance().getRunflat().equals(""))
        {

            idRunFlat = Integer.parseInt(Tire.getInstance().getRunflat());
            select(yesRunFlatsel,noRunFlatsel, idRunFlat);
        }
        else {  select(yesRunFlatsel,noRunFlatsel, idRunFlat);}
      //  !Tire.getInstance().getThrons().equals("") &&
        if (Tire.getInstance().getThrons() != null && !Tire.getInstance().getRunflat().equals(""))
        {
            idThorns = Integer.parseInt(Tire.getInstance().getThrons());
            select(yesThornssel,noThornssel, idThorns);
        }
        else {  select(yesThornssel,noThornssel, idThorns);}


        IndexTextView.setText(Tire.getInstance().getIndex());


        widths= new String[] {"155", "165", "175", "185", "195", "205", "215", "225", "235", "245", "255", "265", "275", "285", "295", "305", "315", "325", "335"};
        heights= new String[] {"40", "45", "50", "55", "60", "65", "70", "75", "80", "85", "90"};
        diametrs= new String[] {"13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "24"};
        tire_types= new String[] {"зимние", "летние", "всесезонные"};

        TapTireWText.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                idVib = 1;
                ((MainActivity)getActivity()).hideNav();
                NextTextView.setVisibility(View.VISIBLE);
                ChooseLayout.setVisibility(View.VISIBLE);
                picker1.setDisplayedValues(null);
                picker1.setMinValue(0);
                picker1.setMaxValue(widths.length - 1);
                picker1.setDisplayedValues(widths);
                picker1.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);
            }
        });
        TapTireHText.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                idVib = 2;
                ((MainActivity)getActivity()).hideNav();
                NextTextView.setVisibility(View.VISIBLE);
                ChooseLayout.setVisibility(View.VISIBLE);
                picker1.setDisplayedValues(null);
                picker1.setMinValue(0);
                picker1.setMaxValue(heights.length - 1);
                picker1.setDisplayedValues(heights);
                picker1.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);
            }
        });
        BackText.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                ((MainActivity)getActivity()).showNav();
                getActivity().onBackPressed();
            }
        });

        TapTireDText.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                idVib = 3;
                ((MainActivity)getActivity()).hideNav();
                NextTextView.setVisibility(View.VISIBLE);
                ChooseLayout.setVisibility(View.VISIBLE);
                picker1.setDisplayedValues(null);
                picker1.setMinValue(0);
                picker1.setMaxValue(diametrs.length - 1);
                picker1.setDisplayedValues(diametrs);
                picker1.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);
            }
        });
        TapTireTypeText.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                idVib = 4;
                ((MainActivity)getActivity()).hideNav();
                NextTextView.setVisibility(View.VISIBLE);
                ChooseLayout.setVisibility(View.VISIBLE);
                picker1.setDisplayedValues(null);
                picker1.setMinValue(0);
                picker1.setMaxValue(tire_types.length - 1);
                picker1.setDisplayedValues(tire_types);
                picker1.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);
            }
        });

        IndexTextView.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus) {
                    ((MainActivity)getActivity()).hideNav();
                    NextTextView.setVisibility(View.VISIBLE);
                }
            }
        });
        Cansel.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                ChooseLayout.setVisibility(View.GONE);
            }
        });

        Choose.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (idVib == 1) {
                    TapTireWText.setText(widths[picker1.getValue()]);
                    picker1.clearFocus();
                }
                if (idVib == 2) {
                    TapTireHText.setText(heights[picker1.getValue()]);
                    picker1.clearFocus();
                }
                if (idVib == 3) {
                    TapTireDText.setText(diametrs[picker1.getValue()]);
                    picker1.clearFocus();
                }
                if (idVib == 4) {
                    TapTireTypeText.setText(tire_types[picker1.getValue()]);
                    picker1.clearFocus();
                }

                ChooseLayout.setVisibility(View.GONE);
            }
        });
        yesThornssel.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                ((MainActivity)getActivity()).hideNav();
                NextTextView.setVisibility(View.VISIBLE);
                select(yesThornssel,noThornssel, 0);
                idThorns = 0;
            }});
        noThornssel.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                ((MainActivity)getActivity()).hideNav();
                NextTextView.setVisibility(View.VISIBLE);
                select(yesThornssel,noThornssel, 1);
                idThorns = 1;
            }});
        yesRunFlatsel.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                ((MainActivity)getActivity()).hideNav();
                NextTextView.setVisibility(View.VISIBLE);
                select(yesRunFlatsel,noRunFlatsel, 0);
                idRunFlat= 0;
            }});
        noRunFlatsel.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                ((MainActivity)getActivity()).hideNav();
                NextTextView.setVisibility(View.VISIBLE);
                select(yesRunFlatsel,noRunFlatsel, 1);
                idRunFlat= 1;
            }});
        NextTextView.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                data = new JSONObject();
                // Проверка
                try {
                    data.put("diametr", TapTireDText.getText().toString());
                    data.put("height", TapTireHText.getText().toString());
                    data.put("runflat", String.valueOf(idRunFlat));
                    data.put("throns",  String.valueOf(idThorns));
                    data.put("type", TapTireTypeText.getText().toString());
                    data.put("width", TapTireWText.getText().toString());
                    data.put("index", IndexTextView.getText().toString());

                    SetInfoTire mt = new SetInfoTire();
                    mt.execute();
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });
        return view;
    }
    public void select(TextView yes , TextView no , int id )
    {
        if (id == -1) {
            yes.setBackgroundColor((Color.parseColor("#ffffff")));
            yes.setTextColor((Color.parseColor("#5d9ece")));
            no.setBackgroundColor((Color.parseColor("#ffffff")));
            no.setTextColor((Color.parseColor("#5d9ece")));
        }
        if (id == 0)
        {   yes.setBackgroundColor((Color.parseColor("#5d9ece")));
            yes.setTextColor((Color.parseColor("#ffffff")));
            no.setBackgroundColor((Color.parseColor("#ffffff")));
            no.setTextColor((Color.parseColor("#5d9ece")));}
        if (id==1){
            yes.setBackgroundColor((Color.parseColor("#ffffff")));
            yes.setTextColor((Color.parseColor("#5d9ece")));
            no.setBackgroundColor((Color.parseColor("#5d9ece")));
            no.setTextColor((Color.parseColor("#ffffff")));}
        }

    private class SetInfoTire extends AsyncTask<Void, Void, String> {
        String resultJson = "";

        @Override
        protected String doInBackground(Void... params) {

            PostRes example = new PostRes();
            String response = "";
            try {
                response = example.post("http://185.213.209.188/api/updatetireinfo/", data.toString(), "Token " + token);
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
                SetProfileInfo(jsonObject);
                Save.ProfJson(jsonObject, getActivity());
                ((MainActivity)getActivity()).showNav();
                getActivity().onBackPressed();
            } catch (JSONException err) {
            }
        } else {
               showToast.showToast("Отсутствует интернет соедение", false,getActivity());
               ((MainActivity)getActivity()).showNav();
               getActivity().onBackPressed();
           }
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