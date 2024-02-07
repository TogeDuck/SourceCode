package com.idle.togeduck.quest.recruit.view.recruit_rv

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.drawable.GradientDrawable
import android.view.View
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.idle.togeduck.R
import com.idle.togeduck.common.RandomCupcake
import com.idle.togeduck.databinding.ItemQuestRecruitBinding
import com.idle.togeduck.common.Theme
import com.idle.togeduck.quest.recruit.model.Recruit
import com.idle.togeduck.util.getColor

class QuestRecruitViewHolder(
    binding: ItemQuestRecruitBinding,
    private val iQuestRecruit: IQuestRecruit
) : RecyclerView.ViewHolder(binding.root), View.OnClickListener {
    private val mainLayout = binding.llMainLayout
    private val ivMainIcon = binding.ivMainIcon
    private val tvTitle = binding.tvTitle
    private val ivPersonIcon = binding.ivPersonIcon
    private val tvPersonCnt = binding.tvPersonCnt
    private val tvEnter = binding.tvEnter
    private val tvTimer = binding.tvTimer

    init {
        tvEnter.setOnClickListener(this)
    }

    fun bind(recruit: Recruit, context: Context) {
        setTheme(context)

        tvTitle.text = recruit.title
        tvPersonCnt.text = "${recruit.current} / ${recruit.maximum}"
    }

    private fun setTheme(context: Context) {
        val whiteCircleDrawable = ContextCompat.getDrawable(context, R.drawable.shape_circle) as GradientDrawable
        whiteCircleDrawable.setColor(getColor(context, R.color.white))
        whiteCircleDrawable.setStroke(0, getColor(context, Theme.theme.main500))
        ivMainIcon.background = whiteCircleDrawable
        ivMainIcon.setImageDrawable(ContextCompat.getDrawable(context, RandomCupcake.getImage()))

        val greenCircleDrawable = ContextCompat.getDrawable(context, R.drawable.shape_circle) as GradientDrawable
        greenCircleDrawable.setColor(getColor(context, R.color.green))
        greenCircleDrawable.setStroke(0, getColor(context, Theme.theme.main500))
        tvEnter.background = greenCircleDrawable
        tvEnter.setTextColor(getColor(context, R.color.white))

        val squareCircleDrawable = ContextCompat.getDrawable(context, R.drawable.shape_square_circle) as GradientDrawable
        squareCircleDrawable.setColor(getColor(context, Theme.theme.sub200))
        squareCircleDrawable.setStroke(0, getColor(context, Theme.theme.main500))
        mainLayout.background = squareCircleDrawable

        val main500CircleDrawable = ContextCompat.getDrawable(context, R.drawable.shape_circle) as GradientDrawable
        main500CircleDrawable.setColor(getColor(context, Theme.theme.sub500))
        main500CircleDrawable.setStroke(0, getColor(context, Theme.theme.main500))
        tvTimer.background = main500CircleDrawable
        tvTimer.setTextColor(getColor(context, R.color.white))
    }

    override fun onClick(view: View?) {
        when (view) {
            tvEnter -> iQuestRecruit.joinBtnClicked(bindingAdapterPosition)
        }
    }

}