package com.blackfish.a1pedal

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.blackfish.a1pedal.ChatKit.media.DefaultDialogsActivity
import com.blackfish.a1pedal.ProfileInfo.User
import com.blackfish.a1pedal.data.ArchiveEvent
import com.blackfish.a1pedal.data.Response
import com.prolificinteractive.materialcalendarview.CalendarDay
import java.util.Collections.addAll
import com.blackfish.a1pedal.utils.toCalendarDay

class EventsArchive : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_events_archive)
        val recycler = findViewById<RecyclerView>(R.id.archive_recycler)
        val archive = mutableListOf<Response>()
        if (MainActivity.events != null) {
            val temp = MainActivity.events
                    .filter {
                        if (User.getInstance().type == "service") {
                            return@filter it.driver.pk == DefaultDialogsActivity.lastChat
                        }
                        return@filter it.service.pk == DefaultDialogsActivity.lastChat
                    }
                    .apply {
                        forEach {
                            val calendarDate = it.date.toCalendarDay()
                            if (calendarDate.isBefore(CalendarDay.today())) {
                                archive.add(it)
                            }
                        }
                    }
                    .groupBy { it.status }
                    .toMutableMap()
            if (temp["accepted"] == null) {
                temp["accepted"] = emptyList()
            }
            val new = (temp["new"] ?: emptyList()).toMutableList()
            new.addAll(temp["waitingS"] ?: emptyList())
            new.addAll(temp["waitingD"] ?: emptyList())
            val accepted by temp
            val list = listOf(ArchiveEvent("Ожидание подтверждения", new),
                    ArchiveEvent("Подтверждено", accepted),
                    ArchiveEvent("Архив", archive))
            val customAdapter = EventInArchiveAdapter(applicationContext, list)
            recycler.apply {
                adapter = customAdapter
                layoutManager = LinearLayoutManager(this@EventsArchive)
                setHasFixedSize(true)
                addItemDecoration(DividerItemDecoration(applicationContext,
                        DividerItemDecoration.HORIZONTAL))
                addItemDecoration(DividerItemDecoration(applicationContext,
                        DividerItemDecoration.VERTICAL))
            }
        }
    }
}
