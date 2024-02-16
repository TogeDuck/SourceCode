package com.idle.togeduck.event.view.detail.detail_rv

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.idle.togeduck.databinding.ItemEventDetailPosterBinding
import com.idle.togeduck.util.TogeDuckDiffUtil

class EventPosterAdapter() : ListAdapter<String, EventPosterViewHolder>(TogeDuckDiffUtil
    .stringDiffUtilItemCallback) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventPosterViewHolder {
        val binding = ItemEventDetailPosterBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return EventPosterViewHolder(binding)
    }

    override fun onBindViewHolder(holder: EventPosterViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}