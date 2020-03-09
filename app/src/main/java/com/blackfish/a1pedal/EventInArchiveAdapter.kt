package com.blackfish.a1pedal

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.blackfish.a1pedal.API.Requests
import com.blackfish.a1pedal.Calendar_block.CalendarActivity
import com.blackfish.a1pedal.ProfileInfo.User
import com.blackfish.a1pedal.data.ArchiveEvent
import com.blackfish.a1pedal.data.Info
import com.blackfish.a1pedal.data.Response
import com.blackfish.a1pedal.tools_class.DataApdaterFriend
import com.thoughtbot.expandablerecyclerview.ExpandableRecyclerViewAdapter
import com.thoughtbot.expandablerecyclerview.models.ExpandableGroup
import com.thoughtbot.expandablerecyclerview.viewholders.ChildViewHolder
import com.thoughtbot.expandablerecyclerview.viewholders.GroupViewHolder

class EventInArchiveAdapter(
        val context: Context,
        groups: List<ArchiveEvent>
) : ExpandableRecyclerViewAdapter<EventInArchiveAdapter.DividerCard, EventInArchiveAdapter.NormalCard>(groups) {

    override fun onCreateGroupViewHolder(parent: ViewGroup?, viewType: Int): DividerCard {
        return DividerCard(LayoutInflater.from(parent!!.context).inflate(R.layout.divider_layout, parent, false))
    }

    override fun onCreateChildViewHolder(parent: ViewGroup?, viewType: Int): NormalCard {
        return NormalCard(LayoutInflater.from(parent!!.context).inflate(R.layout.event_in_archive, parent, false))
    }

    override fun onBindChildViewHolder(holder: NormalCard?, flatPosition: Int, group: ExpandableGroup<*>?, childIndex: Int) {
        val response = group!!.items[childIndex] as Response
        holder!!.content.text = "Запись на ${response.date}, в ${response.time}"
        when(group.title) {
            "Архив" -> {
                holder.deny.visibility = View.INVISIBLE
                holder.accept.visibility = View.INVISIBLE
                holder.change.visibility = View.INVISIBLE
            }
            "Подтверждено" -> {
                holder.accept.visibility = View.INVISIBLE
                holder.deny.visibility = View.INVISIBLE
                holder.change.setOnClickListener {
                    Requests.deleteEvent(response.pk) {
                        CalendarActivity.open(context, response.service.pk, response.driver.pk)
                    }
                }
            }
            "Ожидание подтверждения" -> {
                if (User.getInstance().isDriver && response.status != "waitingD") {
                    holder.deny.visibility = View.INVISIBLE
                    holder.accept.visibility = View.INVISIBLE
                }
                holder.deny.setOnClickListener {
                    Requests.updateEvent(response.pk, "rejected") {
                        notifyItemRemoved(childIndex)
                        group.items.removeAt(childIndex)
                    }
                }
                holder.accept.setOnClickListener {
                    Requests.deleteEvent(response.pk) {
                        Requests.updateEvent(response.pk, "accepted") {
                            notifyItemRemoved(childIndex)
                            group.items.removeAt(childIndex)

                        }
                    }
                }
                holder.change.setOnClickListener {
                    Requests.deleteEvent(response.pk) {
                        group.items.removeAt(childIndex)
                        CalendarActivity.open(context, response.service.pk, response.driver.pk)
                    }
                }
            }
        }
    }

    override fun onBindGroupViewHolder(holder: DividerCard?, flatPosition: Int, group: ExpandableGroup<*>?) {
        holder!!.divider.text = group!!.title
    }

    inner class NormalCard(view: View): ChildViewHolder(view) {
        val content: TextView = view.findViewById(R.id.events_content)
        val deny: TextView = view.findViewById(R.id.events_deny)
        val accept: TextView = view.findViewById(R.id.events_accept)
        val change: TextView = view.findViewById(R.id.events_change)
    }

    inner class DividerCard(view: View): GroupViewHolder(view) {
        val divider: TextView = view.findViewById(R.id.events_divider)
    }
}