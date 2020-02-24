package com.blackfish.a1pedal;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.blackfish.a1pedal.Calendar_block.DataAdapterRequest;
import com.blackfish.a1pedal.Calendar_block.RequesrList;
import com.blackfish.a1pedal.ProfileInfo.Chats;
import com.blackfish.a1pedal.ProfileInfo.Profile_Info;
import com.prolificinteractive.materialcalendarview.CalendarDay;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.threeten.bp.LocalDate;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class RequestActivity  extends AppCompatActivity {
    String id ;
    RecyclerView WaitAppointListView , OkAppointListView , ArhivListView;
    TextView BackText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.appointment_spisok_layout);
        TextView BsckText = findViewById(R.id.BackText);
         WaitAppointListView = findViewById(R.id.WaitAppointListView);
         OkAppointListView = findViewById(R.id.OkAppointListView);
         ArhivListView = findViewById(R.id.ArhivListView);
        BackText = findViewById(R.id.BackText);
        BackText.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            finish();
            }
        });
        id= Chats.getInstance().getRecipient_id();

        GetCalendarRequests mt = new GetCalendarRequests();
        mt.execute();
    }



    public class GetCalendarRequests extends AsyncTask<Void, Void, String> {
        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;
        String resultJson = "";
        String   token= Profile_Info.getInstance().getToken();
        @Override
        protected String doInBackground(Void... params) {
            URL url = null;
            try {
                url = new URL("http://185.213.209.188/api/getcalendarevents/"+ id);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }

            try {
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestProperty ("Authorization", "Token "+ token );
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
                JSONArray json = new JSONArray(response);
                initRequest(json);
            }catch (JSONException ignored){

            }
        }
    }

    public void initRequest (JSONArray json){
        try {
             List<RequesrList> WaitAppointLists     = new ArrayList<>();
             List<RequesrList> OkAppointLists   = new ArrayList<>();
             List<RequesrList> ArhivAppointLists    = new ArrayList<>();

            for (int i=0 ; i<json.length() ; i ++)
            {
                JSONObject elem = json.getJSONObject(i);
                String time = elem.getString("time");
                String date = elem.getString("date");
                String status = elem.getString("status");
                if (status.equals("new"))
                {WaitAppointLists.add(new RequesrList(date,time,status));}
                if (status.equals("accepted"))
                {OkAppointLists.add(new RequesrList(date,time,status));}
            }
            DataAdapterRequest adapter = new DataAdapterRequest(this, WaitAppointLists);
            WaitAppointListView.setLayoutManager(new LinearLayoutManager(this));
            WaitAppointListView.setAdapter( adapter);

            DataAdapterRequest adapter1 = new DataAdapterRequest(this, OkAppointLists);
            OkAppointListView.setLayoutManager(new LinearLayoutManager(this));
            OkAppointListView.setAdapter( adapter1);
        }catch (JSONException ignored){
        }

    }


}