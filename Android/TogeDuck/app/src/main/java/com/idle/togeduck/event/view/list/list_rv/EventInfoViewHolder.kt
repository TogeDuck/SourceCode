package com.idle.togeduck.event.view.list.list_rv

import android.content.Context
import android.graphics.drawable.GradientDrawable
import android.os.Build
import android.view.View
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.idle.togeduck.R
import com.idle.togeduck.databinding.ItemEventInfoBinding
import com.idle.togeduck.event.model.Event
import kotlinx.datetime.LocalDate

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
    private var isStarImg = binding.isStarBtn

    init {
        EventLinearLayout.setOnClickListener(this)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun bind(event: Event, context: Context){

        val eventDrawable = ContextCompat.getDrawable(context, R.drawable.shape_all_round_20) as GradientDrawable

        EventLinearLayout.background = eventDrawable
        cafeName.text = event.name
        eventName.text = event.description
        eventPeriod.text = makeDateToString(event.startDate, event.endDate)

        //Todo.이미지 추후 처리
        //포스터, 즐겨찾기
    }

    fun makeDateToString(startDate: LocalDate, endDate: LocalDate): String{
        return startDate.toString()+" ~ "+endDate.toString()
    }

    override fun onClick(v: View?) {
    }
}