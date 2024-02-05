package com.idle.togeduck.event.view.list.list_rv

import android.content.Context
import android.graphics.drawable.GradientDrawable
import android.os.Build
import android.view.View
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.idle.togeduck.R
import com.idle.togeduck.databinding.ItemEventInfoBinding
import com.idle.togeduck.event.model.Event
import kotlinx.datetime.LocalDate

class EventInfoViewHolder(
    binding: ItemEventInfoBinding,
    private var eventInfo: EventInfo,
) : RecyclerView.ViewHolder(binding.root),
    View.OnClickListener {

    private val eventLinearLayout = binding.itemEventOne
    private val posterImg = binding.posterImg
    private val cafeName = binding.cafeName
    private val eventName = binding.eventName
    private val eventPeriod = binding.eventPeriod
    private var isStarImg = binding.isStarBtn

    private lateinit var event: Event

    init {
        eventLinearLayout.setOnClickListener(this)
        isStarImg.setOnClickListener(this)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun bind(event: Event, context: Context){
        this.event = event
        val eventDrawable = ContextCompat.getDrawable(context, R.drawable.shape_all_round_20) as GradientDrawable

        eventLinearLayout.background = eventDrawable
        cafeName.text = event.name
        eventName.text = event.description
        eventPeriod.text = makeDateToString(event.startDate, event.endDate)

        //Todo.이미지 처리
        //포스터
        Glide
            .with(posterImg)
            .load(event.imgUrl)
            .override(70,70)
            .into(posterImg)

        //즐겨찾기
        if(event.isStar){
            Glide
                .with(isStarImg)
                .load(R.drawable.common_cupcake8)
                .override(70,50)
                .into(isStarImg)
        }else{
            Glide
                .with(isStarImg)
                .load(R.drawable.common_cupcake8_empty)
                .override(70,50)
                .into(isStarImg)
        }
    }

    fun makeDateToString(startDate: LocalDate, endDate: LocalDate): String{
        return startDate.toString()+" ~ "+endDate.toString()
    }

    override fun onClick(v: View?) {
//        when (v) {
//            // 이벤트 정보 영역 클릭 시 이벤트 상세 화면으로 이동
//            eventLinearLayout -> {
//
//            }
//
//            //즐겨찾기 / 취소
//            isStarImg -> {
//                event.isStar = !event.isStar
//
//                // 토글된 isStar 값을 바탕으로 이미지 재설정
//                if (event.isStar) {
//                    Glide
//                        .with(isStarImg)
//                        .load(R.drawable.common_cupcake8)
//                        .override(70, 50)
//                        .into(isStarImg)
//                } else {
//                    Glide
//                        .with(isStarImg)
//                        .load(R.drawable.common_cupcake8_empty)
//                        .override(70, 50)
//                        .into(isStarImg)
//                }
//            }
//        }
    }
}