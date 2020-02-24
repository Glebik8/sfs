package com.blackfish.a1pedal;

import android.content.Context;
import android.content.SharedPreferences;
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
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;

public class Main_Profile_V_Fragment  extends Fragment {
    NumberPicker picker1;
    int idVib = 1;
    String[] model, marka, age, motorTypes, gearboxs, privods;
    String token;
    JSONArray Brand, Model;
    TextView TapBrendText, TapModelText, TapAgeText, TypeDvTextView, TypeKPPTextView, PrivodTextView, Cansel, Choose, NextTextView, BackText;
    EditText NumberTextView, GosNumTextView, VolumDvTextView, VINTextView, MilesTextView;
    LinearLayout ChooseLayout;

    Context t;
    JSONObject data;

    public Main_Profile_V_Fragment() {
    }

    public static Main_Profile_V_Fragment newInstance() {
        return new Main_Profile_V_Fragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.main_par_v_fram, container, false);
        NextTextView = view.findViewById(R.id.NextTextView);
        motorTypes = new String[]{"бензин", "дизель", "газ", "гибрид", "электро"};
        gearboxs = new String[]{"автоматическая", "механическая", "вариатор", "робот"};
        privods = new String[]{"полный", "передний", "задний"};
        GosNumTextView = view.findViewById(R.id.GosNumTextView);
        VolumDvTextView = view.findViewById(R.id.VolumDvTextView);
        VINTextView = view.findViewById(R.id.VINTextView);
        MilesTextView = view.findViewById(R.id.MilesTextView);
        token= Profile_Info.getInstance().getToken();
        TapBrendText = view.findViewById(R.id.TapBrandText);
        TapModelText = view.findViewById(R.id.TapModelText);
        TapAgeText = view.findViewById(R.id.TapAgeText);
        Cansel = view.findViewById(R.id.Cansel);
        Choose = view.findViewById(R.id.Choose);
        ChooseLayout = view.findViewById(R.id.ChooseLayout);
        NumberTextView = view.findViewById(R.id.NumberTextView);

        TypeDvTextView = view.findViewById(R.id.TypeDvTextView);
        TypeKPPTextView = view.findViewById(R.id.TypeKPPTextView);
        PrivodTextView = view.findViewById(R.id.PrivodTextView);
        BackText=view.findViewById(R.id.backText4);

        NumberTextView.setText(User.getInstance().getPhone());

       if (!User.getInstance().getNumber().equals("null"))
       {GosNumTextView.setText(User.getInstance().getNumber());}

        if (!User.getInstance().getMotor_volume().equals("null"))
        {  VolumDvTextView.setText(User.getInstance().getMotor_volume());}

        if (!User.getInstance().getVin().equals("null"))
        {    VINTextView.setText(User.getInstance().getVin());}

        if (!User.getInstance().getMiles().equals("null"))
        { MilesTextView.setText(User.getInstance().getMiles());}


        TapBrendText.setText(User.getInstance().getBrand());
        TapModelText.setText(User.getInstance().getModel());
        TapAgeText.setText(User.getInstance().getYear());

        NumberTextView.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus) {
                    ((MainActivity)getActivity()).hideNav();
                    NextTextView.setVisibility(View.VISIBLE);
                }
            }
        });
                GosNumTextView.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                    @Override
                    public void onFocusChange(View v, boolean hasFocus) {
                        if(hasFocus) {
                            ((MainActivity)getActivity()).hideNav();
                            NextTextView.setVisibility(View.VISIBLE);
                        }
                    }
                });
        VolumDvTextView.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus) {
                    ((MainActivity)getActivity()).hideNav();
                    NextTextView.setVisibility(View.VISIBLE);
                }
            }
        });
                VINTextView.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                    @Override
                    public void onFocusChange(View v, boolean hasFocus) {
                        if(hasFocus) {
                            ((MainActivity)getActivity()).hideNav();
                            NextTextView.setVisibility(View.VISIBLE);
                        }
                    }
                });
        MilesTextView.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus) {
                    ((MainActivity)getActivity()).hideNav();
                    NextTextView.setVisibility(View.VISIBLE);
                }
            }
        });



        if (!User.getInstance().getMotory_type().equals("null")) {
            TypeDvTextView.setText(User.getInstance().getMotory_type());
        }
        if (!User.getInstance().getGearbox() .equals("null")) {
            TypeKPPTextView.setText(User.getInstance().getGearbox());
        }
        if (!User.getInstance().getPrivod() .equals("null")) {
            PrivodTextView.setText(User.getInstance().getPrivod());
        }

        picker1 = (NumberPicker) view.findViewById(R.id.numberPicker1);
        try {
            InputStream is = getActivity().getResources().openRawResource(R.raw.brand);
            Brand = new JSONArray(loadJSONFromAsset(is));
            int sizeBr = Brand.length();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            InputStream is = getActivity().getResources().openRawResource(R.raw.model);
            Model = new JSONArray(loadJSONFromAsset(is));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        TapBrendText.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                idVib = 1;
                ChooseLayout.setVisibility(View.VISIBLE);
                ((MainActivity)getActivity()).hideNav();
                NextTextView.setVisibility(View.VISIBLE);
                marka = new String[Brand.length()];
                for (int i = 0; i < Brand.length(); i++) {
                    JSONObject jsonobject = null;
                    try {
                        jsonobject = Brand.getJSONObject(i);
                        String title = jsonobject.getString("title");
                        marka[i] = title;

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                picker1.setDisplayedValues(null);
                picker1.setMinValue(0);
                picker1.setMaxValue(201);
                picker1.setDisplayedValues(marka);
                picker1.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);
                TapModelText.setText("");
                TapAgeText.setText("");
            }
        });
        BackText.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                ((MainActivity)getActivity()).showNav();
                getActivity().onBackPressed();
            }
        });
        TapAgeText.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String parent = (String) TapBrendText.getText();
                if (!parent.equals("")) {
                    idVib = 3;
                    ChooseLayout.setVisibility(View.VISIBLE);
                    ((MainActivity)getActivity()).hideNav();
                    NextTextView.setVisibility(View.VISIBLE);
                    age = new String[2020 - 1990];
                    for (int i = 1990; i < 2020; i++) {

                        age[i - 1990] = String.valueOf(i);
                    }
                    picker1.setDisplayedValues(null);
                    picker1.setMinValue(0);
                    picker1.setMaxValue(age.length - 1);
                    picker1.setDisplayedValues(age);

                    picker1.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);
                    picker1.setWrapSelectorWheel(false);
                }
            }
        });
        TapModelText.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String parent = (String) TapBrendText.getText();
                if (!parent.equals("")) {
                    idVib = 2;
                    ChooseLayout.setVisibility(View.VISIBLE);
                    ((MainActivity)getActivity()).hideNav();
                    NextTextView.setVisibility(View.VISIBLE);
                    model = new String[Model.length()];
                    int kol = 1;
                    String[] k = new String[1000];
                    for (int i = 0; i < Model.length(); i++) {
                        JSONObject jsonobject = null;
                        try {
                            jsonobject = Model.getJSONObject(i);
                            String parent1 = jsonobject.getString("parent");
                            String title = jsonobject.getString("title");
                            if (parent.equals(parent1)) {
                                k[kol] = title;
                                model = new String[kol];
                                for (int j = 0; j < kol; j++) {
                                    model[j] = k[j + 1];
                                }
                                kol++;
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    String[] jas = model;
                    picker1.setDisplayedValues(null);
                    picker1.setMinValue(0);
                    picker1.setMaxValue(model.length - 1);
                    picker1.setDisplayedValues(model);
                    picker1.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);
                }
            }
        });

        TypeDvTextView.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                idVib = 4;
                ((MainActivity)getActivity()).hideNav();
                NextTextView.setVisibility(View.VISIBLE);
                ChooseLayout.setVisibility(View.VISIBLE);
                picker1.setDisplayedValues(null);
                picker1.setMinValue(0);
                picker1.setMaxValue(motorTypes.length - 1);
                picker1.setDisplayedValues(motorTypes);
                picker1.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);
            }
        });
        TypeKPPTextView.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                idVib = 5;
                ((MainActivity)getActivity()).hideNav();
                NextTextView.setVisibility(View.VISIBLE);
                ChooseLayout.setVisibility(View.VISIBLE);
                picker1.setDisplayedValues(null);
                picker1.setMinValue(0);
                picker1.setMaxValue(gearboxs.length - 1);
                picker1.setDisplayedValues(gearboxs);
                picker1.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);
            }
        });
        PrivodTextView.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                idVib = 6;
                ((MainActivity)getActivity()).hideNav();
                NextTextView.setVisibility(View.VISIBLE);
                ChooseLayout.setVisibility(View.VISIBLE);
                picker1.setDisplayedValues(null);
                picker1.setMinValue(0);
                picker1.setMaxValue(privods.length - 1);
                picker1.setDisplayedValues(privods);
                picker1.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);
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
                    TapBrendText.setText(marka[picker1.getValue()]);
                    picker1.clearFocus();
                }
                if (idVib == 2) {
                    TapModelText.setText(model[picker1.getValue()]);
                    picker1.clearFocus();
                }
                if (idVib == 3) {
                    TapAgeText.setText(age[picker1.getValue()]);
                    picker1.clearFocus();
                }
                if (idVib == 4) {
                    TypeDvTextView.setText(motorTypes[picker1.getValue()]);
                    picker1.clearFocus();
                }
                if (idVib == 5) {
                    TypeKPPTextView.setText(gearboxs[picker1.getValue()]);
                    picker1.clearFocus();
                }
                if (idVib == 6) {
                    PrivodTextView.setText(privods[picker1.getValue()]);
                    picker1.clearFocus();
                }
                ((MainActivity)getActivity()).hideNav();
                NextTextView.setVisibility(View.VISIBLE);
                ChooseLayout.setVisibility(View.GONE);
            }
        });
        NextTextView.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                data = new JSONObject();
                // Проверка
                if (!TapAgeText.getText().toString().equals("") && !TapBrendText.getText().toString().equals("") && !TapModelText.getText().toString().equals("")) {

                    try {
                    data.put("brand", TapBrendText.getText().toString());
                    data.put("gearbox", TypeKPPTextView.getText().toString());
                    data.put("miles", MilesTextView.getText().toString());
                    data.put("model", TapModelText.getText().toString());
                    data.put("motory_type", TypeDvTextView.getText().toString());
                    data.put("motor_volume", VolumDvTextView.getText().toString());
                    data.put("phone", NumberTextView.getText().toString());
                    data.put("number", GosNumTextView.getText().toString());
                    data.put("privod", PrivodTextView.getText().toString());
                    data.put("vin", VINTextView.getText().toString());
                    data.put("year", TapAgeText.getText().toString());

                    SetInfoUser mt = new SetInfoUser();
                    mt.execute();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                }
                else
                {
                    showToast.showToast("Не все поля заполнены", false, getActivity());
                }
            }
        });
        return view;
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

    private class SetInfoUser extends AsyncTask<Void, Void, String> {
        String resultJson = "";
        @Override
        protected String doInBackground(Void... params) {

            PostRes example = new PostRes();
            String response = "";
            try {
                response = example.post("http://185.213.209.188/api/updatedriverinfo/", data.toString(), "Token " + token);
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
                Save.ProfJson(jsonObject,getActivity());
                ((MainActivity)getActivity()).showNav();
                getActivity().onBackPressed();
            } catch (JSONException err) {
            }
        }else {
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
                }
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}