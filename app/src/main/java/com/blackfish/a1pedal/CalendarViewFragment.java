package com.blackfish.a1pedal;

import android.annotation.TargetApi;
import android.content.Context;
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
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.blackfish.a1pedal.API.Requests;
import com.blackfish.a1pedal.Calendar_block.DataAdapterRequest;
import com.blackfish.a1pedal.Calendar_block.EventsAdapter;
import com.blackfish.a1pedal.Calendar_block.RequesrList;
import com.blackfish.a1pedal.ProfileInfo.Profile_Info;
import com.blackfish.a1pedal.ProfileInfo.User;
import com.blackfish.a1pedal.data.FriendsInfo;
import com.blackfish.a1pedal.data.Request;
import com.blackfish.a1pedal.data.Response;
import com.blackfish.a1pedal.decorators.EventDecorator;
import com.blackfish.a1pedal.decorators.EventDecoratorNumb;
import com.blackfish.a1pedal.tools_class.BadgeCalendarDay;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateLongClickListener;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;

import com.prolificinteractive.materialcalendarview.format.WeekDayFormatter;
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
public class CalendarViewFragment extends Fragment implements OnDateLongClickListener, OnDateSelectedListener {
    TextView NowDataView, contNameText;
    RecyclerView requestRecyclerView;
    List<Response> events;
    ArrayList<Response> notAccepted = new ArrayList<>();
    public static FriendsInfo friendsInfo;
    public static ArrayList<CalendarDay> accepted = new ArrayList<>();
    public static ArrayList<CalendarDay> rejected = new ArrayList<>();
    private EventsAdapter eventsAdapter;
    Context startContext;
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
        accepted.clear();
        rejected.clear();
        View view = inflater.inflate(R.layout.calendar_fram, container, false);
        contNameText = view.findViewById(R.id.ContNameText);
        NowDataView = view.findViewById(R.id.NowDataView);
        requestRecyclerView = view.findViewById(R.id.events);
        widget = view.findViewById(R.id.calendarView);
        widget.setShowOtherDates(MaterialCalendarView.SHOW_ALL);
        Requests.Companion.getFriends((FriendsInfo response) ->
        {
            friendsInfo = response;
            return null;
        });
        widget.setTopbarVisible(false);
        contNameText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                widget.clearSelection();
                Requests.Companion.getEvents("", (List<Response> response) -> {
                    updateEvents();
                    return null;
                });

            }
        });
        widget.setOnDateLongClickListener(this);
        widget.setOnDateChangedListener(this);

        Requests.Companion.getEvents("",
                (List<Response> response) ->
                {
                    events = response;
                    for (int i = 0; i < events.size(); i++) {
                        if (events.get(i).getStatus().equals("accepted")) {
                            String[] temp = events.get(i).getDate().split("/");
                            int year = Integer.parseInt(temp[2]);
                            int month = Integer.parseInt(temp[1]);
                            int day = Integer.parseInt(temp[0]);
                            accepted.add(CalendarDay.from(year, month, day));
                        }
                        if (events.get(i).getStatus().equals("new")) {
                            String[] temp = events.get(i).getDate().split("/");
                            int year = Integer.parseInt(temp[2]);
                            int month = Integer.parseInt(temp[1]);
                            int day = Integer.parseInt(temp[0]);
                            rejected.add(CalendarDay.from(year, month, day));
                        }
                    }
                    widget.removeDecorators();
                    widget.addDecorator(new EventDecorator(Color.GREEN, accepted));
                    widget.addDecorator(new EventDecorator(Color.RED, rejected));
                    notAccepted.clear();
                    for (int i = 0; i < events.size(); i++) {
                        if (User.getInstance().getType().equals("driver")) {
                            if (!events.get(i).getStatus().equals("accepted")
                                    && !events.get(i).getStatus().equals("delete")) {
                                notAccepted.add(events.get(i));
                            }
                        } else {
                            if ((!events.get(i).getStatus().equals("accepted")
                                    && !events.get(i).getStatus().equals("delete")
                                    && !events.get(i).getStatus().equals("rejected"))) {
                                notAccepted.add(events.get(i));
                            }
                        }
                    }
                    eventsAdapter = new EventsAdapter(startContext, notAccepted);
                    requestRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                    requestRecyclerView.setHasFixedSize(true);
                    requestRecyclerView.addItemDecoration(new DividerItemDecoration(getContext(),
                            DividerItemDecoration.HORIZONTAL));
                    requestRecyclerView.addItemDecoration(new DividerItemDecoration(getContext(),
                            DividerItemDecoration.VERTICAL));
                    requestRecyclerView.setAdapter(eventsAdapter);
                    return null;
                });
        //GetCalendarEvents mt = new GetCalendarEvents();
        //mt.execute();

        return view;

    }

    public void updateEvents() {
        Requests.Companion.getEvents("", (List<Response> response) -> {
            events = response;
            for (int i = 0; i < events.size(); i++) {
                if (events.get(i).getStatus().equals("accepted")) {
                    String[] temp = events.get(i).getDate().split("/");
                    int year = Integer.parseInt(temp[2]);
                    int month = Integer.parseInt(temp[1]);
                    int day = Integer.parseInt(temp[0]);
                    accepted.add(CalendarDay.from(year, month, day));
                }
                if (events.get(i).getStatus().equals("new")) {
                    String[] temp = events.get(i).getDate().split("/");
                    int year = Integer.parseInt(temp[2]);
                    int month = Integer.parseInt(temp[1]);
                    int day = Integer.parseInt(temp[0]);
                    rejected.add(CalendarDay.from(year, month, day));
                }
            }
            widget.removeDecorators();
            widget.addDecorator(new EventDecorator(Color.GREEN, accepted));
            widget.addDecorator(new EventDecorator(Color.RED, rejected));
            eventsAdapter.setEvents(response.stream().filter(it -> !it.getStatus().equals("delete")).collect(Collectors.toList()));
            eventsAdapter.notifyDataSetChanged();
            return null;
        });
    }

    @Override
    public void onDateLongClick(@NonNull MaterialCalendarView widget, @NonNull CalendarDay date) {
        Toast.makeText(getContext(), FORMATTER.format(date.getDate()), Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onDateSelected(@NonNull MaterialCalendarView widget, @NonNull CalendarDay date, boolean selected) {
        Log.d("glebik", "clicked");
        ArrayList<Response> filtered = new ArrayList<>();
        for (int i = 0; i < events.size(); i++) {
            if (events.get(i).equals(date) && !events.get(i).getStatus().equals("delete")) {
                filtered.add(events.get(i));
            }
        }
        if (eventsAdapter == null) {
            return;
        }
        eventsAdapter.setEvents(filtered);
        eventsAdapter.notifyDataSetChanged();
        NowDataView.setText(FORMATTER.format(date.getDate()));
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        startContext = context;
    }
}
