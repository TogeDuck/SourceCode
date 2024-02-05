package com.idle.togeduck.quest.exchange.view.my_exchange_rv

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.idle.togeduck.databinding.ItemQuestExchangeMyBinding
import com.idle.togeduck.quest.exchange.model.MyExchange
import com.idle.togeduck.util.TogeDuckDiffUtil

class MyExchangeAdapter (
    private val iMyExchangeDetail: IMyExchangeDetail,
    private val context: Context,
    private val selectedMyExchange: MyExchange?
) :ListAdapter<MyExchange, MyExchangeViewHolder>(TogeDuckDiffUtil.myExchangeDiffUtilCallback){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyExchangeViewHolder {
        val binding = ItemQuestExchangeMyBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyExchangeViewHolder(binding, iMyExchangeDetail)
    }

    override fun onBindViewHolder(holder: MyExchangeViewHolder, position: Int) {
        val currentExchange = getItem(position)
        var isSelected = false
        if(selectedMyExchange != null && currentExchange.id == selectedMyExchange.id){
            isSelected = true
        }
        holder.binding(getItem(position), context, isSelected)
    }
}