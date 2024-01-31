package com.idle.togeduck.view.quest_talk

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.idle.togeduck.databinding.ItemQuestTalkBinding
import com.idle.togeduck.model.QuestTalk
import com.idle.togeduck.util.TogeDuckDiffUtil

class QuestTalkAdapter (
    private val iQuestTalkDetail: IQuestTalkDetail,
    private val context: Context
) :ListAdapter<QuestTalk, QuestTalkViewHolder>(TogeDuckDiffUtil.questTalkDiffUtilCallback) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): QuestTalkViewHolder {
        val binding = ItemQuestTalkBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return QuestTalkViewHolder(binding, iQuestTalkDetail)
    }

    override fun onBindViewHolder(holder: QuestTalkViewHolder, position: Int) {
        holder.binding(getItem(position),context)
    }
}