package com.idle.togeduck.quest.exchange.view.exchange_rv

import android.content.Context
import android.graphics.drawable.GradientDrawable
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.idle.togeduck.R
import com.idle.togeduck.databinding.ItemQuestExchangeBinding
import com.idle.togeduck.util.DpPxUtil
import com.idle.togeduck.common.ScreenSize
import com.idle.togeduck.common.Theme

class QuestExchangeViewHolder(
    binding: ItemQuestExchangeBinding,
    private var questExchangeDetail: IQuestExchangeDetail,
) : RecyclerView.ViewHolder(binding.root), View.OnClickListener {

    private val questExchangeLayout = binding.questExchangeItem
    private val image = binding.questExchangeImage
    private val titleContainer = binding.questExchangeTitleContainer
    private val title = binding.questExchangeTitle
    private val timerContainer = binding.questExchangeTimerContainer
    private val timerText = binding.questExchangeTimerText

    init {
        questExchangeLayout.setOnClickListener(this)
    }

    fun binding(questExchange: QuestExchange, context: Context, spanCount: Int) {
        setTheme(questExchange, context)
        setWidth(context, spanCount)
    }

    private fun setWidth(context: Context, spanCount: Int) {
        val newSize = DpPxUtil.dpToPx(ScreenSize.widthDp - 50 - (spanCount - 1) * 10, context) / spanCount
        val layoutParams = questExchangeLayout.layoutParams as ViewGroup.LayoutParams
        layoutParams.width = newSize
        layoutParams.height = newSize
        questExchangeLayout.layoutParams = layoutParams
    }

    override fun onClick(v: View?) {
//        when(v){
//            questExchangeLayout -> {
//                questExchangeDetail.myQuestExchangeClicked(bindingAdapterPosition)
//            }
//        }
    }

    private fun setTheme(questExchange: QuestExchange, context: Context) {
        val roundSmall =
            ContextCompat.getDrawable(context, R.drawable.shape_all_round_10) as GradientDrawable
        val rectangleCircle =
            ContextCompat.getDrawable(context, R.drawable.shape_square_circle) as GradientDrawable
        val upperRound =
            ContextCompat.getDrawable(context, R.drawable.shape_upper_round_10) as GradientDrawable
        roundSmall.setStroke(0, 0)
        rectangleCircle.setStroke(0, 0)
        rectangleCircle.setColor(ContextCompat.getColor(context, Theme.theme.sub500))
        upperRound.setStroke(0, 0)
        upperRound.setColor(ContextCompat.getColor(context, Theme.theme.sub200))

        questExchangeLayout.background = roundSmall
        image.background = roundSmall
        image.setBackgroundColor(ContextCompat.getColor(context, Theme.theme.sub200))
        titleContainer.background = upperRound
        timerContainer.background = rectangleCircle

        title.setText(questExchange.title)
        timerText.setText(questExchange.time.toString())
    }
}