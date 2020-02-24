package com.blackfish.a1pedal.Calendar_block;


import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.blackfish.a1pedal.CalendarViewFragment;
import com.blackfish.a1pedal.R;

public class CalendarActivity extends AppCompatActivity {

    RecyclerView recyclerView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);
        getSupportFragmentManager().beginTransaction().add(R.id.check, CalendarViewFragment.newInstance()).commit();
        recyclerView = findViewById(R.id.regRecycler);
        recyclerView.setAdapter(new RegisterAdapter());
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }
}
