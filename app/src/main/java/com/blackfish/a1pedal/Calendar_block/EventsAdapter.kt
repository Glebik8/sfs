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
import com.blackfish.a1pedal.CalendarViewFragment
import com.blackfish.a1pedal.CalendarViewFragment.friendsInfo
import com.blackfish.a1pedal.ChatKit.media.CustomMediaMessagesActivity
import com.blackfish.a1pedal.dialog.Dialog
import com.blackfish.a1pedal.MainActivity
import com.blackfish.a1pedal.ProfileInfo.Chats
import com.blackfish.a1pedal.ProfileInfo.User
import com.blackfish.a1pedal.R
import com.blackfish.a1pedal.data.Response
import com.blackfish.a1pedal.dialog.Moves
import com.blackfish.a1pedal.tools_class.DataApdaterFriend.currentPosition


class EventsAdapter(
        val context: Context,
        var events: MutableList<Response>
) : RecyclerView.Adapter<EventsAdapter.Card>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Card {
        return when(viewType) {
            1 -> Card(LayoutInflater.from(parent.context).inflate(R.layout.retry_event, parent, false)).RetryCard()
            else -> Card(LayoutInflater.from(parent.context).inflate(R.layout.simple_text_event, parent, false)).SimpleTextCard()
        }
    }

    override fun getItemCount(): Int {
        return events.size
    }

    override fun onBindViewHolder(holder: Card, position: Int) {
        holder.view.setOnClickListener {
            val dialog = Dialog(events[position], object : Moves {
                override fun accept(response: Response) {
                    Requests.updateEvent(response.pk, "accepted") {
                        notifyItemRemoved(position)
                        events.removeAt(position)
                    }
                }

                override fun chat(response: Response) {
                    var id = MainActivity.dialogInfos.find { list ->
                        list.users[0].pk.toString() == response.notMe.pk
                    }?.pk?.toString()
                    with(Chats.getInstance()) {
                        recipient_id = response.driver.pk
                        lastActivity = response.driver.lastActivity
                        tittle_mess = response.name
                    }
                    if (id != null) {
                        with(Chats.getInstance()) {
                            chat_id = id
                        }
                        context.startActivity(Intent(context, CustomMediaMessagesActivity::class.java))
                    } else {
                        Requests.createDialog(User.getInstance().pk.toInt(), response.notMe.pk.toInt()) {
                            with(Chats.getInstance()) {
                                chat_id = id
                            }
                            context.startActivity(Intent(context, CustomMediaMessagesActivity::class.java))
                        }
                    }
                }

                override fun change(response: Response) {
                    Requests.deleteEvent(response.pk) {
                        notifyItemRemoved(position)
                        events.removeAt(position)
                        CalendarActivity.open(context, response.service.pk, response.driver.pk)
                    }
                }

                override fun deny(response: Response) {
                    Requests.updateEvent(response.pk, "rejected") {
                        notifyItemRemoved(position)
                        events.removeAt(position)

                    }
                }

            })
            dialog.show(CalendarViewFragment.fragmentManager, "dialog")
        }
        when(User.getInstance().type) {
            "driver" -> holder.name.text = events[position].service.name
            "service" -> holder.name.text = events[position].driver.fio
        }
        holder.date.text = events[position].date
        holder.time.text = events[position].time
        when(holder) {
            is Card.SimpleTextCard -> {
                holder.status.text = when(events[position].status) {
                    "waitingS", "waitingD" -> handleStatus(events[position].status)
                    "accepted" -> "Принято"
                    else -> ""
                }
            }
            is Card.ManageCard -> {
                holder.eventsPk = events[position].pk
            }
            is Card.RetryCard -> {
                holder.usersPk = events[position].service.pk
                holder.driversPk = events[position].driver.pk
                holder.eventsPk = events[position].pk
                Log.d("glebik", holder.usersPk)
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        if (User.getInstance().type == "service") {
            if (events[position].status in listOf("accepted", "rejected", "waitingS"))
                return 2
            return 0
        }
        if (events[position].status == "waitingD")
            return 0
        if (events[position].status == "rejected")
            return 1
        return 2
    }


    private fun handleStatus(status: String): String {
        if (User.getInstance().isDriver) {
            if (status == "waitingD")
                return "Ожидает Вашего подтверждения"
            return "Ожидает подтверждения"
        }
        if (status == "waitingS")
            return "Ожидает Вашего подвтерждения"
        return "Ожидает подтверждения"
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
                name = view.findViewById(R.id.retry_name)
                date = view.findViewById(R.id.retry_date)
                time = view.findViewById(R.id.retry_time)
                status = view.findViewById(R.id.simple_status)
            }
        }

        inner class ManageCard : Card(view) {

            var eventsPk: Int = -1
            private val accept: TextView
            private val deny: TextView

            init {
                name = view.findViewById(R.id.manage_name)
                date = view.findViewById(R.id.manage_date)
                time = view.findViewById(R.id.manage_time)
                accept = view.findViewById(R.id.manage_accept)
                deny = view.findViewById(R.id.manage_deny)

                accept.setOnClickListener {
                    Requests.updateEvent(eventsPk, "accepted") {
                        events.removeAt(adapterPosition)
                        notifyDataSetChanged()
                    }
                }

                deny.setOnClickListener {
                    Requests.updateEvent(eventsPk, "rejected") {
                        events.removeAt(adapterPosition)
                        notifyDataSetChanged()
                    }
                }
            }
        }

        inner class RetryCard: Card(view) {

            var usersPk = ""
            var eventsPk = -1
            var driversPk = ""
            private val retry: TextView

            init {
                name = view.findViewById(R.id.retry_name)
                date = view.findViewById(R.id.retry_date)
                time = view.findViewById(R.id.retry_time)
                retry = view.findViewById(R.id.retry)

                retry.setOnClickListener {
                    currentPosition = friendsInfo?.friends?.userFriends?.indexOfFirst {
                        it.pk == usersPk
                    } ?: -1
                    if (currentPosition != -1) {
                        events.removeAt(adapterPosition)
                        notifyDataSetChanged()
                    }
                    Requests.deleteEvent(eventsPk) {
                        CalendarActivity.open(context, usersPk, driversPk)
                    }
                }
            }


        }

    }
}