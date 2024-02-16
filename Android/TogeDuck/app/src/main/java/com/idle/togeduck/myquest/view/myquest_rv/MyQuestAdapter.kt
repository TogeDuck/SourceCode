package com.idle.togeduck.myquest.view.myquest_rv

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.idle.togeduck.databinding.ItemMyquestBinding
import com.idle.togeduck.myquest.model.MyQuest
import com.idle.togeduck.util.TogeDuckDiffUtil

class MyQuestAdapter (
    private val iMyQuestDetail: IMyQuestDetail,
    private val context: Context
) : ListAdapter<MyQuest, MyQuestViewHolder>(TogeDuckDiffUtil.myQuestDiffUtilCallback){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyQuestViewHolder {
        val binding = ItemMyquestBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyQuestViewHolder(binding, iMyQuestDetail)
    }

    override fun onBindViewHolder(holder: MyQuestViewHolder, position: Int) {
        holder.binding(getItem(position),context)
    }
}