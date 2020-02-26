package com.blackfish.a1pedal.Calendar_block

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.blackfish.a1pedal.API.Requests
import com.blackfish.a1pedal.ProfileInfo.User
import com.blackfish.a1pedal.R
import com.blackfish.a1pedal.data.Request
import com.blackfish.a1pedal.data.Response
import com.prolificinteractive.materialcalendarview.CalendarDay
import com.prolificinteractive.materialcalendarview.MaterialCalendarView
import kotlinx.android.synthetic.main.calendar_fram.view.*
import kotlinx.android.synthetic.main.event_layout.view.*

import com.blackfish.a1pedal.CalendarViewFragment.accepted
import com.blackfish.a1pedal.CalendarViewFragment.rejected
import com.blackfish.a1pedal.tools_class.DataApdaterFriend.currentPosition
import com.blackfish.a1pedal.CalendarViewFragment.friendsInfo

class EventsAdapter(
        val context: Context,
        private var events: MutableList<Response>
) : RecyclerView.Adapter<EventsAdapter.Card>() {

    companion object {
        lateinit var lastId: String
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Card {
        return Card(LayoutInflater.from(parent.context).inflate(R.layout.event_layout, parent, false))
    }

    override fun getItemCount(): Int {
        accepted.clear()
        rejected.clear()
        return events.size
    }

    override fun onBindViewHolder(holder: Card, position: Int) {
        if (User.getInstance().type == "driver") {
            holder.accept.visibility = View.INVISIBLE
            holder.deny.visibility = View.INVISIBLE
            if (events[position].status == "accepted") {
                holder.retry.visibility = View.INVISIBLE
            } else {
                holder.waitForSubmit.visibility = View.INVISIBLE
                holder.retry.setOnClickListener {
                    currentPosition = friendsInfo?.friends?.userFriends?.indexOfFirst {
                        it.pk == events[position].service.pk
                    } ?: -1
                    if (currentPosition != -1) {
                        events.removeAt(position)
                        notifyDataSetChanged()
                    }
                    context.startActivity(Intent(context, CalendarActivity::class.java))
                }
            }
        } else {
            holder.accept.setOnClickListener {
                Requests.updateEvent(events[position].pk, "accepted") {
                    Requests.getEvents {
                        events = it
                        notifyDataSetChanged()
                    }
                }
            }
            holder.deny.setOnClickListener {
                Requests.updateEvent(events[position].pk, "rejected") {
                    Requests.getEvents {
                        events = it
                        notifyDataSetChanged()
                    }
                }
            }
        }
        holder.waitForSubmit.text = when (events[position].status) {
            "new" -> "Ожидает подтверждения"
            "accepted" -> "Принято"
            else -> ""
        }
        holder.date.text = events[position].date
        holder.time.text = events[position].time
        if (User.getInstance().type == "driver") {
            holder.name.text = events[position].service.name
        } else {
            holder.name.text = events[position].driver.fio
        }
        if (events[position].status == "accepted") {
            holder.accept.visibility = View.INVISIBLE
            holder.deny.visibility = View.INVISIBLE
            if (User.getInstance().type == "service") {
                holder.waitForSubmit.visibility = View.VISIBLE
            }
        }
    }

    class Card(view: View): RecyclerView.ViewHolder(view) {
        val name: TextView = view.name
        val date: TextView = view.date
        val time: TextView = view.time
        val accept: Button = view.accept_event
        val deny: Button = view.deny_event
        val waitForSubmit: TextView = view.wait_for_submit
        val retry: Button = view.retry
    }
}