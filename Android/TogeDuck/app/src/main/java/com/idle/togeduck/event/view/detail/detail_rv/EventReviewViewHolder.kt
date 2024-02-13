package com.idle.togeduck.event.view.detail.detail_rv

import android.content.Context
import android.graphics.drawable.GradientDrawable
import android.view.View
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.idle.togeduck.R
import com.idle.togeduck.databinding.ItemEventReviewBinding
import com.idle.togeduck.common.Theme
import com.idle.togeduck.event.model.EventReviewContent

class EventReviewViewHolder (
    binding: ItemEventReviewBinding,
    private var eventReview: EventReview
): RecyclerView.ViewHolder(binding.root), View.OnClickListener{

    private val eventReviewLayout = binding.eventReviewLayout
    private val reviewUserImg = binding.reviewUserImg
    private val reviewText = binding.reviewText
    private val reviewImg = binding.reviewImg

    fun bind(review: EventReviewContent, context: Context){
        val drawable = ContextCompat.getDrawable(context, R.drawable.shape_all_round_10) as GradientDrawable
        drawable.setColor(ContextCompat.getColor(context, Theme.theme.sub200))
        drawable.setStroke(0, Theme.theme.sub200)
        eventReviewLayout.background = drawable

        val circleDrawable = ContextCompat.getDrawable(context, R.drawable.shape_circle) as GradientDrawable
        circleDrawable.setColor(ContextCompat.getColor(context, Theme.theme.sub300))
        drawable.setStroke(0, Theme.theme.sub300)
        reviewUserImg.background = circleDrawable

        reviewText.text = review.content
        reviewText.setTextColor(ContextCompat.getColor(context, R.color.black))

        if(review.reviewImageUrl != null){
            Glide
                .with(reviewImg)
                .load(review.reviewImageUrl)
                .override(700,700)
                .into(reviewImg)

            reviewImg.visibility = View.VISIBLE
        }else {
            reviewImg.visibility = View.GONE
        }
    }

    override fun onClick(v: View?) {
        TODO("Not yet implemented")
    }
}