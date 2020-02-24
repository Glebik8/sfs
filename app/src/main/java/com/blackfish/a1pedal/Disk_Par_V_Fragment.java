package com.blackfish.a1pedal;

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
import java.util.Objects;

public class Disk_Par_V_Fragment extends Fragment {
    String [] widths  , diametrs , disk_types;
    int idVib = 1;
    TextView TapDiskWText , TapDiskDText , TapDiskTypeText ,  Cansel, Choose, NextTextView , BackText;
    EditText ViletTextView  , PSD1TextView ,PSD2TextView,  DIATextView ;
    LinearLayout ChooseLayout;
    NumberPicker picker1;
    JSONObject data;
    String token;

    public Disk_Par_V_Fragment() {
    }

    public static Disk_Par_V_Fragment newInstance() {
        return new Disk_Par_V_Fragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.disk_par_v_fram, container, false);
        TapDiskWText = view.findViewById(R.id.TapDiskWText);
        TapDiskDText = view.findViewById(R.id.TapDiskDText);
        TapDiskTypeText = view.findViewById(R.id.TapDiskTypeText);
        picker1 = (NumberPicker) view.findViewById(R.id.numberPicker1);
        token = Profile_Info.getInstance().getToken();
        ViletTextView = view.findViewById(R.id.ViletTextView);
        PSD1TextView = view.findViewById(R.id.PSD1TextView);
        PSD2TextView = view.findViewById(R.id.PSD2TextView);
        DIATextView = view.findViewById(R.id.DIATextView);
        BackText = view.findViewById(R.id.backText2);

        try {

            if (!Discs.getInstance().getVilet().equals("null")) {
                ViletTextView.setText(Discs.getInstance().getVilet());
            }
            if (!Discs.getInstance().getPsd1().equals("null")) {
                PSD1TextView.setText(Discs.getInstance().getPsd1());
            }
            if (!Discs.getInstance().getPsd2().equals("null")) {
                PSD2TextView.setText(Discs.getInstance().getPsd2());
            }
            if (!Discs.getInstance().getDia().equals("null")) {
                DIATextView.setText(Discs.getInstance().getDia());
            }
            if (!Discs.getInstance().getWidth().equals("null")) {
                TapDiskWText.setText(Discs.getInstance().getWidth());
            }
            if (!Discs.getInstance().getDiametr().equals("null")) {
                TapDiskDText.setText(Discs.getInstance().getDiametr());
            }
            if (!Discs.getInstance().getType().equals("null")) {
                TapDiskTypeText.setText(Discs.getInstance().getType());
            }
        } catch (Exception e) {
            ViletTextView.setText(Discs.getInstance().getVilet());

            PSD1TextView.setText(Discs.getInstance().getPsd1());

            PSD2TextView.setText(Discs.getInstance().getPsd2());

            DIATextView.setText(Discs.getInstance().getDia());
            TapDiskWText.setText(Discs.getInstance().getWidth());

            TapDiskDText.setText(Discs.getInstance().getDiametr());

            TapDiskTypeText.setText(Discs.getInstance().getType());
        }



        widths= new String[] {"5.0", "5.5", "6.0", "6.5", "7.0", "7.5", "8.0", "8.5", "9.0", "9.5", "10.0", "10.5", "11.0", "11.5", "12.0", "12.5"};
        disk_types= new String[] {"стальные", "легкосплавные", "литые", "кованные"};
        diametrs= new String[] {"13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "24"};
        NextTextView=view.findViewById(R.id.NextTextView);
        Cansel=view.findViewById(R.id.Cansel);
        Choose  =view.findViewById(R.id.Choose);
        ChooseLayout = view.findViewById(R.id.ChooseLayout);
        TapDiskWText.setOnClickListener(new View.OnClickListener() {
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

        ViletTextView .setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus) {
                    ((MainActivity)getActivity()).hideNav();
                    NextTextView.setVisibility(View.VISIBLE);
                }
            }
        });
                PSD1TextView.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                    @Override
                    public void onFocusChange(View v, boolean hasFocus) {
                        if(hasFocus) {
                            ((MainActivity)getActivity()).hideNav();
                            NextTextView.setVisibility(View.VISIBLE);
                        }
                    }
                });
        PSD2TextView.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus) {
                    ((MainActivity)getActivity()).hideNav();
                    NextTextView.setVisibility(View.VISIBLE);
                }
            }
        });
                DIATextView .setOnFocusChangeListener(new View.OnFocusChangeListener() {
                    @Override
                    public void onFocusChange(View v, boolean hasFocus) {
                        if(hasFocus) {
                            ((MainActivity)getActivity()).hideNav();
                            NextTextView.setVisibility(View.VISIBLE);
                        }
                    }
                });



        BackText.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                ((MainActivity)getActivity()).showNav();
                getActivity().onBackPressed();
            }
        });
        Cansel.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                ChooseLayout.setVisibility(View.GONE);
            }
        });

        TapDiskDText.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                idVib = 2;
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
        TapDiskTypeText.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                idVib = 3;((MainActivity)getActivity()).hideNav();
                NextTextView.setVisibility(View.VISIBLE);
                ChooseLayout.setVisibility(View.VISIBLE);

                picker1.setDisplayedValues(null);
                picker1.setMinValue(0);
                picker1.setMaxValue(disk_types.length - 1);
                picker1.setDisplayedValues(disk_types);
                picker1.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);
            }
        });
        Choose.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (idVib == 1) {
                    TapDiskWText.setText(widths[picker1.getValue()]);
                    picker1.clearFocus();
                }
                if (idVib == 2) {
                    TapDiskDText.setText(diametrs[picker1.getValue()]);
                    picker1.clearFocus();
                }
                if (idVib == 3) {
                    TapDiskTypeText.setText(disk_types[picker1.getValue()]);
                    picker1.clearFocus();
                }

                ChooseLayout.setVisibility(View.GONE);
            }
        });

        NextTextView.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                data = new JSONObject();
                // Проверка
                try {
                    data.put("diametr", TapDiskDText.getText().toString());
                    data.put("dia", DIATextView.getText().toString());
                    data.put("psd1", PSD1TextView.getText().toString());
                    data.put("psd2",  PSD2TextView.getText().toString());
                    data.put("vilet", ViletTextView.getText().toString());
                    data.put("type", TapDiskTypeText.getText().toString());
                    data.put("width", TapDiskWText.getText().toString());


                  SetInfoDisk mt = new SetInfoDisk();
                    mt.execute();
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });



        return view;
    }

    private class SetInfoDisk extends AsyncTask<Void, Void, String> {
        String resultJson = "";

        @Override
        protected String doInBackground(Void... params) {

            PostRes example = new PostRes();
            String response = "";
            try {
                response = example.post("http://185.213.209.188/api/updatediscsinfo/", data.toString(), "Token " + token);
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
                Objects.requireNonNull(getActivity()).onBackPressed();
            } catch (JSONException err) {
            }
        } else {
            showToast.showToast("Отсутствует интернет соедение", false,getActivity());
            ((MainActivity)getActivity()).showNav();
            getActivity().onBackPressed();
        }}
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