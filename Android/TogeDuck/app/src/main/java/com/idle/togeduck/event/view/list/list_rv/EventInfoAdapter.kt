package com.idle.togeduck.event.view.list.list_rv

import android.content.Context
import android.os.Build
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.ListAdapter
import com.idle.togeduck.databinding.ItemEventInfoBinding
import com.idle.togeduck.event.model.Event
import com.idle.togeduck.util.TogeDuckDiffUtil

class EventInfoAdapter(
    private var eventInfo: EventInfo,
    private val context: Context,
    private val type: Int
) : ListAdapter<Event, EventInfoViewHolder>(TogeDuckDiffUtil.eventDiffUtilCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventInfoViewHolder {
        val binding = ItemEventInfoBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return EventInfoViewHolder(binding,eventInfo, type)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: EventInfoViewHolder, position: Int) {
        holder.bind(getItem(position), context)
    }
}