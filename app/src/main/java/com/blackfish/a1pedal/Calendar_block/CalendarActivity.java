package com.blackfish.a1pedal.Calendar_block;


import android.os.Bundle;
import android.util.Log;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.blackfish.a1pedal.CalendarViewFragment;
import com.blackfish.a1pedal.R;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;


public class CalendarActivity extends AppCompatActivity implements OnDateSelectedListener {

    public static CalendarDay clickedDate;
    RecyclerView recyclerView;
    MaterialCalendarView widget;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);

        widget = findViewById(R.id.check);
        widget.setOnDateChangedListener(this);
        recyclerView = findViewById(R.id.regRecycler);
        recyclerView.setAdapter(new RegisterAdapter(getApplicationContext()));
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    public void onDateSelected(@NonNull MaterialCalendarView widget, @NonNull CalendarDay date, boolean selected) {
        if (!date.isAfter(CalendarDay.today()) ) {
            widget.setSelectedDate(CalendarDay.today());
            return;
        }
        clickedDate = date;
    }
}
