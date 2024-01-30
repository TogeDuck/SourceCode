package com.idle.togeduck.view.quest_recruit

import android.content.Context
import android.graphics.drawable.GradientDrawable
import android.view.View
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.idle.togeduck.R
import com.idle.togeduck.databinding.ItemQuestRecruitBinding
import com.idle.togeduck.model.QuestRecruit
import com.idle.togeduck.model.QuestShare
import com.idle.togeduck.util.Theme
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
    private val cmTimer = binding.cmTimer

    init {
        tvEnter.setOnClickListener(this)
    }

    fun bind(questRecruit: QuestShare, context: Context) {
        val list = listOf(
            R.drawable.common_cupcake1,
            R.drawable.common_cupcake2,
            R.drawable.common_cupcake3,
            R.drawable.common_cupcake4,
            R.drawable.common_cupcake5,
            R.drawable.common_cupcake6,
            R.drawable.common_cupcake7,
            R.drawable.common_cupcake8,
            R.drawable.common_cupcake9,
        )

        ivMainIcon.setImageDrawable(ContextCompat.getDrawable(context, list.random()))
        tvTitle.text = questRecruit.title

        val greenCircleDrawable = ContextCompat.getDrawable(context, R.drawable.shape_circle) as GradientDrawable
        greenCircleDrawable.setColor(getColor(context, R.color.green))
        tvEnter.background = greenCircleDrawable

        val squareCircleDrawable = ContextCompat.getDrawable(context, R.drawable.shape_square_circle) as GradientDrawable
        squareCircleDrawable.setColor(getColor(context, Theme.theme.sub200))
        mainLayout.background = squareCircleDrawable

        tvPersonCnt.text = questRecruit.content
    }

    override fun onClick(view: View?) {
        when (view) {
            tvEnter -> iQuestRecruit.joinBtnClicked(bindingAdapterPosition)
        }
    }

}