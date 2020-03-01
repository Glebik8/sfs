package com.blackfish.a1pedal.Calendar_block

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.blackfish.a1pedal.API.Requests
import com.blackfish.a1pedal.CalendarViewFragment.friendsInfo
import com.blackfish.a1pedal.ProfileInfo.User
import com.blackfish.a1pedal.R
import com.blackfish.a1pedal.data.Response
import com.blackfish.a1pedal.tools_class.DataApdaterFriend.currentPosition

import kotlinx.android.synthetic.main.manage_card.view.*
import kotlinx.android.synthetic.main.retry_event.view.*
import kotlinx.android.synthetic.main.simple_text_event.view.*
import kotlinx.android.synthetic.main.simple_text_event.view.retry_date
import kotlinx.android.synthetic.main.simple_text_event.view.retry_name
import kotlinx.android.synthetic.main.simple_text_event.view.retry_time

class EventsAdapter(
        val context: Context,
        var events: MutableList<Response>
) : RecyclerView.Adapter<EventsAdapter.Card>() {



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Card {
        return when(viewType) {
            0 -> Card(LayoutInflater.from(parent.context).inflate(R.layout.manage_card, parent, false)).ManageCard()
            1 -> Card(LayoutInflater.from(parent.context).inflate(R.layout.retry_event, parent, false)).RetryCard()
            else -> Card(LayoutInflater.from(parent.context).inflate(R.layout.simple_text_event, parent, false)).SimpleTextCard()
        }
    }

    override fun getItemCount(): Int {
        return events.size
    }

    override fun onBindViewHolder(holder: Card, position: Int) {
        when(User.getInstance().type) {
            "driver" -> holder.name.text = events[position].service.name
            "service" -> holder.name.text = events[position].driver.fio
        }
        holder.date.text = events[position].date
        holder.time.text = events[position].time
        when(holder) {
            is Card.SimpleTextCard -> {
                holder.status.text = when(events[position].status) {
                    "new" -> "Ожидает подтверждения"
                    else -> "Принято"
                }
            }
            is Card.ManageCard -> {
                holder.eventsPk = events[position].pk
            }
            is Card.RetryCard -> {
                holder.usersPk = events[position].service.pk
                holder.eventsPk = events[position].pk
                Log.d("glebik", holder.usersPk)
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        if (User.getInstance().type == "service") {
            if (events[position].status in listOf("accepted", "rejected"))
                return 2
            return 0
        }
        if (events[position].status == "rejected")
            return 1
        return 2
    }

    /*
        waiting, accepted
        accept, deny
        retry
     */

    open inner class Card(val view: View): RecyclerView.ViewHolder(view) {

        lateinit var name: TextView
        lateinit var date: TextView
        lateinit var time: TextView


        inner class SimpleTextCard : Card(view) {

            val status: TextView

            init {
                name = view.retry_name
                date = view.retry_date
                time = view.retry_time
                status = view.simple_status
            }
        }

        inner class ManageCard : Card(view) {

            var eventsPk: Int = -1
            private val accept: TextView
            private val deny: TextView

            init {
                name = view.manage_name
                date = view.manage_date
                time = view.manage_time
                accept = view.manage_accept
                deny = view.manage_deny

                accept.setOnClickListener {
                    Requests.updateEvent(eventsPk, "accepted") {
                        Requests.getEvents {
                            events = it
                            notifyDataSetChanged()
                        }
                    }
                }

                deny.setOnClickListener {
                    Requests.updateEvent(eventsPk, "rejected") {
                        Requests.getEvents { list ->
                            events = list.filter {
                                it.status != "rejected"
                            }.toMutableList()
                            notifyDataSetChanged()
                        }
                    }
                }
            }
        }

        inner class RetryCard: Card(view) {

            var usersPk = ""
            var eventsPk = -1
            private val retry: TextView

            init {
                name = view.retry_name
                date = view.retry_date
                time = view.retry_time
                retry = view.retry

                retry.setOnClickListener {
                    currentPosition = friendsInfo?.friends?.userFriends?.indexOfFirst {
                        it.pk == usersPk
                    } ?: -1
                    if (currentPosition != -1) {
                        events.removeAt(adapterPosition)
                        notifyDataSetChanged()
                    }
                    Requests.updateEvent(eventsPk, "delete") {
                        context.startActivity(Intent(context, CalendarActivity::class.java))
                    }
                }
            }


        }

    }
}