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
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.blackfish.a1pedal.API.Notification;
import com.blackfish.a1pedal.API.Requests;
import com.blackfish.a1pedal.Calendar_block.DataAdapterRequest;
import com.blackfish.a1pedal.Calendar_block.EventsAdapter;
import com.blackfish.a1pedal.Calendar_block.RequesrList;
import com.blackfish.a1pedal.ProfileInfo.Profile_Info;
import com.blackfish.a1pedal.ProfileInfo.User;
import com.blackfish.a1pedal.data.Friends;
import com.blackfish.a1pedal.data.FriendsInfo;
import com.blackfish.a1pedal.data.Request;
import com.blackfish.a1pedal.data.Response;
import com.blackfish.a1pedal.decorators.EventDecorator;
import com.blackfish.a1pedal.decorators.EventDecoratorNumb;
import com.blackfish.a1pedal.tools_class.BadgeCalendarDay;
import com.google.android.gms.dynamic.SupportFragmentWrapper;
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

import static com.blackfish.a1pedal.MainActivity.events;


@TargetApi(Build.VERSION_CODES.O)
public class CalendarViewFragment extends Fragment implements
        OnDateLongClickListener, OnDateSelectedListener {
    TextView NowDataView, contNameText;
    RecyclerView requestRecyclerView;
    int lastK = 0;
    ArrayList<Response> notAccepted = new ArrayList<>();
    public static FriendsInfo friendsInfo;
    public static ArrayList<CalendarDay> accepted = new ArrayList<>();
    public static ArrayList<CalendarDay> rejected = new ArrayList<>();
    public static EventsAdapter eventsAdapter;
    public static FragmentManager fragmentManager = null;
    Context startContext;
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("EEE, d MMM yyyy");
    public static MaterialCalendarView widget;

    public CalendarViewFragment() {

    }

    public static CalendarViewFragment newInstance() {
        return new CalendarViewFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        fragmentManager = getChildFragmentManager();
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
        contNameText.setOnClickListener(view1 -> widget.clearSelection());
        widget.setOnDateLongClickListener(this);
        widget.setOnDateChangedListener(this);
        eventsAdapter = new EventsAdapter(startContext, notAccepted);
        requestRecyclerView.setLayoutManager(new LinearLayoutManager(startContext));
        requestRecyclerView.setHasFixedSize(true);
        requestRecyclerView.addItemDecoration(new DividerItemDecoration(startContext,
                DividerItemDecoration.HORIZONTAL));
        requestRecyclerView.addItemDecoration(new DividerItemDecoration(startContext,
                DividerItemDecoration.VERTICAL));
        requestRecyclerView.setAdapter(eventsAdapter);
        widget.removeDecorators();
        widget.addDecorator(new com.blackfish.a1pedal.decorators.EventDecorator(Color.GREEN, accepted));
        widget.addDecorator(new EventDecorator(Color.RED, rejected));
        /* Requests.Companion.getEvents("",
                (List<Response> newEvents) ->
                {
                    for (int i = 0; i < newEvents.size(); i++) {
                        if (newEvents.get(i).getStatus().equals("accepted")) {
                            String[] temp = newEvents.get(i).getDate().split("/");
                            int year = Integer.parseInt(temp[2]);
                            int month = Integer.parseInt(temp[1]);
                            int day = Integer.parseInt(temp[0]);
                            accepted.add(CalendarDay.from(year, month, day));
                        }
                        if (newEvents.get(i).getStatus().equals("waitingS") || newEvents.get(i).getStatus().equals("waitingD")) {
                            String[] temp = events.get(i).getDate().split("/");
                            int year = Integer.parseInt(temp[2]);
                            int month = Integer.parseInt(temp[1]);
                            int day = Integer.parseInt(temp[0]);
                            rejected.add(CalendarDay.from(year, month, day));
                        }
                    }
                    notAccepted.clear();
                    for (int i = 0; i < newEvents.size(); i++) {
                        if (User.getInstance().getType().equals("driver")) {
                            if (!newEvents.get(i).getStatus().equals("accepted")
                                    && !newEvents.get(i).getStatus().equals("delete")) {
                                notAccepted.add(newEvents.get(i));
                            }
                        } else {
                            if ((!newEvents.get(i).getStatus().equals("accepted")
                                    && !newEvents.get(i).getStatus().equals("delete")
                                    && !newEvents.get(i).getStatus().equals("rejected"))) {
                                notAccepted.add(newEvents.get(i));
                            }
                        }
                    }
                    widget.removeDecorators();
                    widget.addDecorator(new com.blackfish.a1pedal.decorators.EventDecorator(Color.GREEN, accepted));
                    widget.addDecorator(new EventDecorator(Color.RED, rejected));
                    eventsAdapter = new EventsAdapter(startContext, notAccepted);
                    requestRecyclerView.setLayoutManager(new LinearLayoutManager(startContext));
                    requestRecyclerView.setHasFixedSize(true);
                    requestRecyclerView.addItemDecoration(new DividerItemDecoration(startContext,
                            DividerItemDecoration.HORIZONTAL));
                    requestRecyclerView.addItemDecoration(new DividerItemDecoration(startContext,
                            DividerItemDecoration.VERTICAL));
                    requestRecyclerView.setAdapter(eventsAdapter);
                    return null;
                });
        //GetCalendarEvents mt = new GetCalendarEvents();
        //mt.execute();
        */
        return view;

    }

    public void handleDifference(List<Response> newData) {
        int k = 0;
        for (int i = 0; i < newData.size(); i++) {
            if (!events.contains(newData.get(i))) {
                k++;
            }
        }
        if (lastK != 0) {
            Notification.INSTANCE.makeNotification(k - lastK, startContext);
        }
        lastK = k;
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
            if (events.get(i).equals(date)) {
                if (User.getInstance().getType().equals("service") && !events.get(i).getStatus().equals("rejected")) {
                    filtered.add(events.get(i));
                } else if (User.getInstance().getType().equals("driver")) {
                    filtered.add(events.get(i));
                }
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

    @Override
    public void onPause() {
        super.onPause();
        Requests.Companion.setRunning(false);
    }
}
