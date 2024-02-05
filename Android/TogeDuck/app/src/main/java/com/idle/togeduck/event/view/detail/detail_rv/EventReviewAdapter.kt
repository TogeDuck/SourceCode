package com.idle.togeduck.event.view.detail.detail_rv

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.idle.togeduck.databinding.ItemEventReviewBinding
import com.idle.togeduck.event.model.EventReviewContent
import com.idle.togeduck.util.TogeDuckDiffUtil

class EventReviewAdapter(
    private var eventReview: EventReview,
    private val context: Context
) : ListAdapter<EventReviewContent, EventReviewViewHolder> (TogeDuckDiffUtil.reviewDiffUtilCallback){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventReviewViewHolder {
        val binding = ItemEventReviewBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return EventReviewViewHolder(binding, eventReview)
    }

    override fun onBindViewHolder(holder: EventReviewViewHolder, position: Int) {
        holder.bind(getItem(position), context)
    }
}