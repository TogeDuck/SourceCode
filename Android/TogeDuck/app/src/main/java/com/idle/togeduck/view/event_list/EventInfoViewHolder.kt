package com.idle.togeduck.view.event_list

import android.content.Context
import com.idle.togeduck.util.Theme
import android.graphics.drawable.GradientDrawable
import android.os.Build
import android.view.View
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.idle.togeduck.R
import com.idle.togeduck.databinding.ItemEventInfoBinding
import com.idle.togeduck.model.Event

class EventInfoViewHolder(
    binding: ItemEventInfoBinding,
    private var eventInfo: EventInfo,
) : RecyclerView.ViewHolder(binding.root),
    View.OnClickListener {

    private val EventLinearLayout = binding.itemEventOne
    private val posterImg = binding.posterImg
    private val cafeName = binding.cafeName
    private val eventName = binding.eventName
    private val eventPeriod = binding.eventPeriod

    init {
        EventLinearLayout.setOnClickListener(this)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun bind(event: Event, context: Context){

        val eventDrawable = ContextCompat.getDrawable(context, R.drawable.shape_all_round_20) as GradientDrawable

        EventLinearLayout.background = eventDrawable
        cafeName.text = event.cafeName
        eventName.text = event.eventName
        eventPeriod.text = event.eventPeriod


        //Todo.이미지 추후 처리
    }


    override fun onClick(v: View?) {
        TODO("Not yet implemented")
    }
}