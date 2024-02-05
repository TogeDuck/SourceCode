package com.idle.togeduck.quest.exchange.view.my_exchange_rv

import android.content.Context
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.idle.togeduck.databinding.ItemQuestExchangeMyBinding
import com.idle.togeduck.quest.exchange.model.Exchange

class MyExchangeViewHolder (
    binding: ItemQuestExchangeMyBinding,
    private var iMyExchangeDetail: IMyExchangeDetail
) : RecyclerView.ViewHolder(binding.root), View.OnClickListener{
    private val questExchangeMyLayout = binding.questExchangeMyItem
    private val image = binding.questExchangeMyImage
    private val overlay = binding.questExchangeMyOverlay

    init {
        questExchangeMyLayout.setOnClickListener(this)
    }

    fun binding(questExchange: Exchange, context: Context, spanCount: Int) {
//        this.questExchange = questExchange
//        configurePassedTime(questExchange.expiredAt)
//        setImage(questExchange, context)
//        setTheme(questExchange, context)
//        setWidth(context, spanCount)
//        timerTask(questExchange, context)
    }

    override fun onClick(v: View?) {
        TODO("Not yet implemented")
    }


}