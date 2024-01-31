package com.idle.togeduck.view.quest_exchange

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.idle.togeduck.databinding.ItemQuestExchangeBinding
import com.idle.togeduck.model.QuestExchange
import com.idle.togeduck.util.TogeDuckDiffUtil

class QuestExchangeAdapter (
    private val iQuestExchangeDetail: IQuestExchangeDetail,
    private val context: Context
) :ListAdapter<QuestExchange, QuestExchangeViewHolder>(TogeDuckDiffUtil.questExchangeDiffUtilCallback){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): QuestExchangeViewHolder {
        val binding = ItemQuestExchangeBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return QuestExchangeViewHolder(binding, iQuestExchangeDetail)
    }

    override fun onBindViewHolder(holder: QuestExchangeViewHolder, position: Int) {
        holder.binding(getItem(position),context)
    }
}