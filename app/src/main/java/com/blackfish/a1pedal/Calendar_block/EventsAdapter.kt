package com.blackfish.a1pedal.Calendar_block

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
import kotlinx.android.synthetic.main.calendar_fram.view.*
import kotlinx.android.synthetic.main.event_layout.view.*

class EventsAdapter(
        private var events: List<Response>
) : RecyclerView.Adapter<EventsAdapter.Card>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Card {
        return Card(LayoutInflater.from(parent.context).inflate(R.layout.event_layout, parent, false))
    }

    override fun getItemCount() = events.size

    override fun onBindViewHolder(holder: Card, position: Int) {
        if (User.getInstance().type == "driver") {
            holder.accept.visibility = View.INVISIBLE
            holder.deny.visibility = View.INVISIBLE
        } else {
            holder.accept.setOnClickListener {
                Requests.updateEvent(events[position].pk, "accepted") {
                    Requests.getEvents {
                        events = it.filter {
                            it.status != "rejected"
                        }
                        notifyDataSetChanged()
                    }
                }
            }
            holder.deny.setOnClickListener {
                Requests.updateEvent(events[position].pk, "rejected") {
                    Requests.getEvents {
                        events = it.filter {
                            it.status != "rejected"
                        }
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
    }
}