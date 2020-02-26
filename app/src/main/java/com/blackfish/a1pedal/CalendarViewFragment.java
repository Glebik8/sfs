package com.blackfish.a1pedal;

import android.annotation.TargetApi;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.JsonReader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.blackfish.a1pedal.API.Requests;
import com.blackfish.a1pedal.Calendar_block.DataAdapterRequest;
import com.blackfish.a1pedal.Calendar_block.EventsAdapter;
import com.blackfish.a1pedal.Calendar_block.RequesrList;
import com.blackfish.a1pedal.ProfileInfo.Profile_Info;
import com.blackfish.a1pedal.ProfileInfo.User;
import com.blackfish.a1pedal.data.Response;
import com.blackfish.a1pedal.decorators.EventDecorator;
import com.blackfish.a1pedal.decorators.EventDecoratorNumb;
import com.blackfish.a1pedal.tools_class.BadgeCalendarDay;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateLongClickListener;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.threeten.bp.DayOfWeek;
import org.threeten.bp.LocalDate;
import org.threeten.bp.format.DateTimeFormatter;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Array;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

import com.blackfish.a1pedal.API.Requests.*;


@TargetApi(Build.VERSION_CODES.O)
public class CalendarViewFragment  extends Fragment implements OnDateLongClickListener , OnDateSelectedListener {
    TextView NowDataView;
    RecyclerView requestRecyclerView ;
    List<Response> events;
    public static CalendarDay clickedDate;
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("EEE, d MMM yyyy");
    MaterialCalendarView widget;
    public CalendarViewFragment() {
    }

    public static CalendarViewFragment newInstance() {
        return new CalendarViewFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.calendar_fram, container, false);
        NowDataView = view.findViewById(R.id.NowDataView);
        requestRecyclerView = view.findViewById(R.id.events);
        widget  = view.findViewById(R.id.calendarView);
        widget.setShowOtherDates(MaterialCalendarView.SHOW_ALL);
        widget.setTopbarVisible(false);
        widget.setOnTitleClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
               // Toast.makeText(getContext(), R.string.today, Toast.LENGTH_SHORT).show();
            }
        });

        widget.setOnDateLongClickListener(this);
        widget.setOnDateChangedListener(this);

        Requests.Companion.getEvents("",
                (List<Response> response) ->
                {
                    Log.d("glebik", response.toString());
                    events = response.stream().filter(it -> !it.getStatus().equals("rejected")).collect(Collectors.toList());
                    EventsAdapter adapter = new EventsAdapter(events);
                    requestRecyclerView.setAdapter(adapter);
                    requestRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                    return null;
                });
        //GetCalendarEvents mt = new GetCalendarEvents();
        //mt.execute();
        return view;

    }



    @Override
    public void onDateLongClick(@NonNull MaterialCalendarView widget, @NonNull CalendarDay date) {
        Toast.makeText(getContext(), FORMATTER.format(date.getDate()), Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onDateSelected(@NonNull MaterialCalendarView widget, @NonNull CalendarDay date, boolean selected) {
        clickedDate = date;
        NowDataView.setText(FORMATTER.format(date.getDate()));
    }

    public class GetCalendarEvents extends AsyncTask<Void, Void, String> {
        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;
        String resultJson = "";
        String   token= Profile_Info.getInstance().getToken();
        @Override
        protected String doInBackground(Void... params) {
            URL url = null;
            try {
                url = new URL("http://185.213.209.188/api/getcalendarevents/");
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
               initEvent(json);
                initRequest (json);
            }catch (JSONException ignored){

            }
        }
    }

    public void initRequest (JSONArray json){
        try {
            List<RequesrList> AppointLists     = new ArrayList<>();

            for (int i=0 ; i<json.length() ; i ++)
            {
                JSONObject elem = json.getJSONObject(i);
                String time = elem.getString("time");
                String date = elem.getString("date");
                String status = elem.getString("status");
                if (status.equals("new"))
                {AppointLists.add(new RequesrList(date,time,status));}
                if (status.equals("accepted"))
                {AppointLists.add(new RequesrList(date,time,status));}
            }


        }catch (JSONException ignored){
        }}


        public void initEvent (JSONArray json)
    {
        final ArrayList<CalendarDay> dates = new ArrayList<>();
        Array [] k ;
        try {
            for (int i=0 ; i<json.length() ; i ++)
            {   int are = 1;
                int badge = 1;
                JSONObject elem = json.getJSONObject(i);
                String time = elem.getString("time");
                String date = elem.getString("date");
                String status = elem.getString("status");
                SimpleDateFormat dateFormat= new SimpleDateFormat("dd/MM/yyyy");
                String st [] = date.split("/");
                int year = Integer.parseInt(st[2]);
                int month = Integer.parseInt(st[1]);
                int dayOfMonth = Integer.parseInt(st[0]);
                Date d=dateFormat.parse(date);
                final CalendarDay day = CalendarDay.from(LocalDate.of(year,month,dayOfMonth));
                dates.add(day);
                if (i==0){
                    k=new Array[are];
                }


                for (int y=i+1 ; y<json.length(); y++){
                    JSONObject elem1 = json.getJSONObject(y);
                    String date1 = elem.getString("date");
                    if (date.equals(date1)){
                        badge = badge+1;
                    }
                }

            }
        }catch (JSONException ignored){
        } catch (ParseException e) {
            e.printStackTrace();
        }
        ArrayList <BadgeCalendarDay> ret = getNumbEvent (json);

        widget.addDecorator(new EventDecorator(Color.RED, dates));
        widget.addDecorator(new EventDecoratorNumb(ret));
    }

    public ArrayList <BadgeCalendarDay> getNumbEvent (JSONArray json)
    {
        ArrayList <BadgeCalendarDay> ret = new ArrayList<>();
        try {
            ArrayList <String> dates = new ArrayList<>();
        for (int i = 0 ; i < json.length(); i++) {
            int uniq = 0;
            JSONObject elem = json.getJSONObject(i);
            String date = elem.getString("date");
            dates.add(date);
        }

        ArrayList <String> uniqDates = new ArrayList<>();

        for (int i = 0 ; i < dates.size(); i++ )
        {
            int unik = 0;
            String k = dates.get(i);
            for (int y=i+1 ; y<dates.size(); y++)
            {
                String k1 = dates.get(y);
                if (k.equals(k1)){unik=1;}
            }
            if (unik==0){uniqDates.add(k);}
        }

            for (int i = 0 ; i < uniqDates.size(); i++ )
            {
                int unik = 0;
                String k = uniqDates.get(i);
                for (int y=0 ; y<dates.size(); y++)
                {
                    String k1 = dates.get(y);
                    if (k.equals(k1)){unik=unik+1;}
                }
                String st [] = k.split("/");
                int year = Integer.parseInt(st[2]);
                int month = Integer.parseInt(st[1]);
                int dayOfMonth = Integer.parseInt(st[0]);
                final CalendarDay day = CalendarDay.from(LocalDate.of(year,month,dayOfMonth));
                ret.add(new BadgeCalendarDay(String.valueOf(unik),day));
            }
    } catch (JSONException e) {
        e.printStackTrace();
        Toast.makeText(getContext(), "!", Toast.LENGTH_LONG).show();
    }
        return ret;
    }


}
