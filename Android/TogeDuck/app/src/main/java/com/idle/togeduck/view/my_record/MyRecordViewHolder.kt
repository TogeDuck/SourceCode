package com.idle.togeduck.view.my_record

import android.content.Context
import android.graphics.drawable.GradientDrawable
import android.view.View
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.idle.togeduck.R
import com.idle.togeduck.databinding.ItemMyRecordBinding
import com.idle.togeduck.util.Theme
import com.idle.togeduck.util.getColor

class MyRecordViewHolder(
    binding: ItemMyRecordBinding,
    private val iMyRecord: IMyRecord
) : RecyclerView.ViewHolder(binding.root), View.OnClickListener {
    private val mainLayout = binding.llMainLayout
    private val ivMyRecordMainImg = binding.ivMyRecordMainImg
    private val tvDate = binding.tvDate
    private val tvMyRecord = binding.tvMyRecord

    init {
        mainLayout.setOnClickListener(this)
    }

    fun bind(questRecruit: QuestShare, context: Context) {
        setTheme(context)

        tvDate.text = questRecruit.title
        tvMyRecord.text = questRecruit.content

        Glide
            .with(ivMyRecordMainImg)
            .load(questRecruit.imgUrl)
            .thumbnail(
                Glide.with(ivMyRecordMainImg).load(questRecruit.imgUrl).override(50, 50)
            )
            .circleCrop()
            .override(500, 500)
            .into(ivMyRecordMainImg)
    }

    private fun setTheme(context: Context) {
        val strokeCircleDrawable = ContextCompat.getDrawable(context, R.drawable.shape_circle) as GradientDrawable
        strokeCircleDrawable.setStroke(4, getColor(context, Theme.theme.sub500))
        ivMyRecordMainImg.background = strokeCircleDrawable

        tvDate.setTextColor(getColor(context, Theme.theme.main500))
        tvMyRecord.setTextColor(getColor(context, R.color.black))

        val squareCircleDrawable = ContextCompat.getDrawable(context, R.drawable.shape_square_circle) as GradientDrawable
        squareCircleDrawable.setColor(getColor(context, Theme.theme.sub200))
        squareCircleDrawable.setStroke(0, Theme.theme.main500)

        mainLayout.background = squareCircleDrawable
    }

    override fun onClick(view: View?) {
        when (view) {
            mainLayout -> iMyRecord.recordClicked(bindingAdapterPosition)
        }
    }

}