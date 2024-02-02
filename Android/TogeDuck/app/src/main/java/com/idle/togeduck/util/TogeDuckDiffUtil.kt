package com.idle.togeduck.util

import androidx.recyclerview.widget.DiffUtil
import com.idle.togeduck.favorite.model.Celebrity
import com.idle.togeduck.my_record.model.HistoryData
import com.idle.togeduck.quest.recruit.model.Recruit
import java.util.Objects

object TogeDuckDiffUtil {
    class DiffUtilCallback(
        private val oldList: List<Any>,
        private val newList: List<Any>
    ) : DiffUtil.Callback() {
        override fun getOldListSize(): Int = oldList.size

        override fun getNewListSize(): Int = newList.size

        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldList[oldItemPosition] == newList[newItemPosition]
        }

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldList[oldItemPosition] == newList[newItemPosition]
        }
    }

    val stringDiffUtilItemCallback = object: DiffUtil.ItemCallback<String>() {
        override fun areContentsTheSame(oldItem: String, newItem: String) = oldItem == newItem

        override fun areItemsTheSame(oldItem: String, newItem: String) = oldItem == newItem
    }

    val recruitDiffUtilCallback = object: DiffUtil.ItemCallback<Recruit>() {
        override fun areItemsTheSame(oldItem: Recruit, newItem: Recruit): Boolean {
            return oldItem.chatId == newItem.chatId
        }

        override fun areContentsTheSame(oldItem: Recruit, newItem: Recruit): Boolean {
            return oldItem == newItem
        }
    }

    val historyDataDiffUtilCallback = object: DiffUtil.ItemCallback<HistoryData>() {
        override fun areItemsTheSame(oldItem: HistoryData, newItem: HistoryData): Boolean {
            return oldItem.historyId == newItem.historyId
        }

        override fun areContentsTheSame(oldItem: HistoryData, newItem: HistoryData): Boolean {
            return oldItem == newItem
        }
    }

    val celebrityDiffUtilCallback = object: DiffUtil.ItemCallback<Celebrity>() {
        override fun areItemsTheSame(oldItem: Celebrity, newItem: Celebrity): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Celebrity, newItem: Celebrity): Boolean {
            return oldItem == newItem
        }
    }
}