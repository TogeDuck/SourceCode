package com.idle.togeduck.quest.recruit.view.recruit_rv

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.idle.togeduck.databinding.ItemQuestRecruitBinding
import com.idle.togeduck.quest.recruit.model.Recruit
import com.idle.togeduck.util.TogeDuckDiffUtil

class QuestRecruitAdapter(
    private var iQuestRecruit: IQuestRecruit,
    private val context: Context
) : ListAdapter<Recruit, QuestRecruitViewHolder>(TogeDuckDiffUtil.recruitDiffUtilCallback) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): QuestRecruitViewHolder {
        val binding = ItemQuestRecruitBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return QuestRecruitViewHolder(binding, iQuestRecruit)
    }

    override fun onBindViewHolder(holder: QuestRecruitViewHolder, position: Int) {
        holder.bind(getItem(position), context)
    }
}