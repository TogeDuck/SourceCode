package com.idle.togeduck.view.event_detail

import android.content.Context
import android.graphics.drawable.GradientDrawable
import android.view.View
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.idle.togeduck.R
import com.idle.togeduck.databinding.ItemEventReviewBinding
import com.idle.togeduck.util.Theme

class EventReviewViewHolder (
    binding: ItemEventReviewBinding,
    private var eventReview: EventReview
): RecyclerView.ViewHolder(binding.root), View.OnClickListener{

    private val eventReviewLayout = binding.eventReviewLayout
    private val reviewUserImg = binding.reviewUserImg
    private val reviewText = binding.reviewText
    private val reviewImg = binding.reviewImg

    init {
        eventReviewLayout.setOnClickListener(this)
    }

    fun bind(review: Review, context: Context){
        val drawable = ContextCompat.getDrawable(context, R.drawable.shape_all_round_10) as GradientDrawable
        drawable.setColor(ContextCompat.getColor(context, Theme.theme.sub200))
        drawable.setStroke(0,Theme.theme.sub200)
        eventReviewLayout.background = drawable

        val circleDrawable = ContextCompat.getDrawable(context, R.drawable.shape_circle) as GradientDrawable
        circleDrawable.setColor(ContextCompat.getColor(context, Theme.theme.sub300))
        drawable.setStroke(0,Theme.theme.sub300)
        reviewUserImg.background = circleDrawable

        reviewText.text = review.content
        reviewText.setTextColor(ContextCompat.getColor(context, R.color.black))

        //todo.이미지 추가 (Glide) - reviewImg 처리
        //todo. 이미지 null이 아니면 visibility = visible null이면 반대
    }

    override fun onClick(v: View?) {
        TODO("Not yet implemented")
    }
}