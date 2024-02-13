package com.idle.togeduck.history.view.history_rv

import android.content.Context
import android.graphics.drawable.GradientDrawable
import android.view.View
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.idle.togeduck.R
import com.idle.togeduck.common.RandomCupcake
import com.idle.togeduck.common.Theme
import com.idle.togeduck.databinding.ItemHistoryBinding
import com.idle.togeduck.history.model.HistoryData
import com.idle.togeduck.util.getColor

class HistoryViewHolder(
    binding: ItemHistoryBinding,
    private val iHistory: IHistory
) : RecyclerView.ViewHolder(binding.root), View.OnClickListener {
    private val mainLayout = binding.llMainLayout
    private val ivMyRecordMainImg = binding.ivMyRecordMainImg
    private val tvDate = binding.tvDate
    private val tvMyRecord = binding.tvMyRecord

    init {
        mainLayout.setOnClickListener(this)
    }

    fun bind(historyData: HistoryData, context: Context) {
        setTheme(context)

        tvDate.text = historyData.date.toString()
        tvMyRecord.text = historyData.historyId.toString()
        ivMyRecordMainImg.setImageDrawable(ContextCompat.getDrawable(context, RandomCupcake.getImage()))
    }

    private fun setTheme(context: Context) {
        val strokeCircleDrawable = ContextCompat.getDrawable(context, R.drawable.shape_circle) as GradientDrawable
        strokeCircleDrawable.setStroke(4, getColor(context, Theme.theme.sub500))
        ivMyRecordMainImg.background = strokeCircleDrawable

        tvDate.setTextColor(getColor(context, Theme.theme.main500))
        tvMyRecord.setTextColor(getColor(context, R.color.black))

        val squareCircleDrawable = ContextCompat.getDrawable(context, R.drawable.shape_square_circle) as GradientDrawable
        squareCircleDrawable.setColor(getColor(context, Theme.theme.sub200))
        squareCircleDrawable.setStroke(0, getColor(context, Theme.theme.main500))

        mainLayout.background = squareCircleDrawable
    }

    override fun onClick(view: View?) {
        when (view) {
            mainLayout -> iHistory.recordClicked(bindingAdapterPosition)
        }
    }

}