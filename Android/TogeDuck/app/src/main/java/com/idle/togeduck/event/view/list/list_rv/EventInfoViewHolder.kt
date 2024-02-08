package com.idle.togeduck.event.view.list.list_rv

import android.content.Context
import android.graphics.drawable.GradientDrawable
import android.os.Build
import android.view.View
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.idle.togeduck.R
import com.idle.togeduck.common.Theme
import com.idle.togeduck.databinding.ItemEventInfoBinding
import com.idle.togeduck.event.model.Event
import com.idle.togeduck.util.DpPxUtil
import kotlinx.datetime.LocalDate

class EventInfoViewHolder(
    binding: ItemEventInfoBinding,
    private var eventInfo: EventInfo,
    private val type: Int
) : RecyclerView.ViewHolder(binding.root),
    View.OnClickListener {

    private val eventLinearLayout = binding.itemEventOne
    private val posterImg = binding.posterImg
    private val cafeName = binding.cafeName
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
        eventDrawable.setColor(ContextCompat.getColor(context, Theme.theme.sub400))
        eventLinearLayout.background = eventDrawable

        cafeName.text = event.name

        eventPeriod.text = makeDateToString(event.startDate, event.endDate)

        //포스터
        Glide
            .with(posterImg)
            .load(event.image1)
            .override(500,500)
            .transform(CenterCrop(), RoundedCorners(DpPxUtil.dpToPx(20, context)))
            .into(posterImg)

        // TODO. image2, image3 처리 필요

        //즐겨찾기
        changeLikeImage()
    }

    override fun onClick(v: View?) {
        when (v) {
            //이벤트 클릭 시 이벤트 상세 화면 보여주기
            eventLinearLayout -> eventInfo.eventClicked(bindingAdapterPosition, type)

            //즐겨찾기 추가, 취소
            isStarImg -> {
                event.isStar = !event.isStar

                //토글된 isStar 값을 바탕으로 이미지 재설정
                changeLikeImage()
                eventInfo.likeClick(bindingAdapterPosition, type)
            }
        }
    }

    private fun changeLikeImage() {
        if(event.isStar){
            Glide
                .with(isStarImg)
                .load(R.drawable.common_cupcake8)
                .override(700,700)
                .into(isStarImg)
        }else{
            Glide
                .with(isStarImg)
                .load(R.drawable.common_cupcake8_empty)
                .override(700,700)
                .into(isStarImg)
        }
    }

    private fun makeDateToString(startDate: LocalDate, endDate: LocalDate): String{
        return startDate.toString()+" ~ "+endDate.toString()
    }
}