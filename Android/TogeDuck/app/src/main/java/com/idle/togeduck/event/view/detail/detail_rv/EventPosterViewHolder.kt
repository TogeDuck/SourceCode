package com.idle.togeduck.event.view.detail.detail_rv

import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.bumptech.glide.Glide
import com.idle.togeduck.databinding.ItemEventDetailPosterBinding

class EventPosterViewHolder(
    binding: ItemEventDetailPosterBinding,
) : RecyclerView.ViewHolder(binding.root) {
    private val imageView = binding.eventPosterDetail

    fun bind(uri: String) {
        Glide
            .with(imageView)
            .load(uri)
            .override(1000,1000)
            .into(imageView)
    }
}