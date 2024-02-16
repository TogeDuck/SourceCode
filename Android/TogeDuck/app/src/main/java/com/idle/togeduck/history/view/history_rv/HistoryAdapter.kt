package com.idle.togeduck.history.view.history_rv

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.idle.togeduck.databinding.ItemHistoryBinding
import com.idle.togeduck.history.model.HistoryData
import com.idle.togeduck.util.TogeDuckDiffUtil

class HistoryAdapter(
    private var iHistory: IHistory,
    private val context: Context
) : ListAdapter<HistoryData, HistoryViewHolder>(TogeDuckDiffUtil.historyDataDiffUtilCallback) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryViewHolder {
        val binding = ItemHistoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return  HistoryViewHolder(binding, iHistory)
    }

    override fun onBindViewHolder(holder: HistoryViewHolder, position: Int) {
        holder.bind(getItem(position), context)
    }
}