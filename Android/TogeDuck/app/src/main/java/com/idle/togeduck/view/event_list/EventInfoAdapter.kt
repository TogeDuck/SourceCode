package com.idle.togeduck.view.event_list

import android.content.Context
import android.os.Build
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.ListAdapter
import com.idle.togeduck.databinding.ItemEventInfoBinding
import com.idle.togeduck.util.TogeDuckDiffUtil

class EventInfoAdapter(
    private var eventInfo: EventInfo,
    private val context: Context
) : ListAdapter<Event, EventInfoViewHolder>(TogeDuckDiffUtil.eventUtilItemCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventInfoViewHolder {
        val binding = ItemEventInfoBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return EventInfoViewHolder(binding,eventInfo)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: EventInfoViewHolder, position: Int) {
        holder.bind(getItem(position),context)
    }
}