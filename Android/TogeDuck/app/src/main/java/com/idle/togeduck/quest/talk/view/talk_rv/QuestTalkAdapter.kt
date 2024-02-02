package com.idle.togeduck.quest.talk.view.talk_rv

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.idle.togeduck.databinding.ItemQuestTalkBinding
import com.idle.togeduck.databinding.ItemQuestTalkMyBinding
import com.idle.togeduck.quest.talk.model.Talk
import com.idle.togeduck.util.TogeDuckDiffUtil

class QuestTalkAdapter (
    private val iQuestTalkDetail: IQuestTalkDetail,
    private val context: Context,
) :ListAdapter<Talk, RecyclerView.ViewHolder>(TogeDuckDiffUtil.talkDiffUtilCallback) {
    companion object {
        private const val VIEW_TYPE_NORMAL = 1
        private const val VIEW_TYPE_MY = 2
    }
    override fun getItemViewType(position: Int): Int {
        val questTalkItem = getItem(position)
        if (questTalkItem != null) {
            if (questTalkItem.userId == 123) {
                return VIEW_TYPE_MY
            } else {
                return VIEW_TYPE_NORMAL
            }
        }
        return super.getItemViewType(position)
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)

        if (viewType == VIEW_TYPE_NORMAL) {
            val binding = ItemQuestTalkBinding.inflate(inflater, parent, false)
            return QuestTalkViewHolder(binding, iQuestTalkDetail)
        } else if (viewType == VIEW_TYPE_MY) {
            val bindingMy = ItemQuestTalkMyBinding.inflate(inflater, parent, false)
            return QuestTalkMyViewHolder(bindingMy, iQuestTalkDetail)
        } else {
            val binding = ItemQuestTalkBinding.inflate(inflater, parent, false)
            return QuestTalkViewHolder(binding, iQuestTalkDetail)
        }
    }
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is QuestTalkViewHolder) {
            holder.binding(getItem(position), context)
        } else if (holder is QuestTalkMyViewHolder) {
            holder.binding(getItem(position), context)
        }
    }
}