package com.idle.togeduck.my_record.view.my_record_rv

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.idle.togeduck.databinding.ItemMyRecordBinding
import com.idle.togeduck.util.TogeDuckDiffUtil

class MyRecordAdapter(
    private var iMyRecord: IMyRecord,
    private val context: Context
) : ListAdapter<QuestShare, MyRecordViewHolder>(TogeDuckDiffUtil.questShareDiffUtilCallback) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyRecordViewHolder {
        val binding = ItemMyRecordBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return  MyRecordViewHolder(binding, iMyRecord)
    }

    override fun onBindViewHolder(holder: MyRecordViewHolder, position: Int) {
        holder.bind(getItem(position), context)
    }
}