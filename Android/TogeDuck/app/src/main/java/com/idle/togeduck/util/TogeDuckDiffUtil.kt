package com.idle.togeduck.util

import androidx.recyclerview.widget.DiffUtil
import com.idle.togeduck.model.Event
import com.idle.togeduck.model.FavoriteIdol
import com.idle.togeduck.model.QuestShare

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

    val favoriteIdolDiffUtilItemCallback = object: DiffUtil.ItemCallback<FavoriteIdol>() {
        override fun areItemsTheSame(oldItem: FavoriteIdol, newItem: FavoriteIdol): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: FavoriteIdol, newItem: FavoriteIdol): Boolean {
            return oldItem == newItem
        }
    }

    val questShareDiffUtilCallback = object : DiffUtil.ItemCallback<QuestShare>(){
        // ListAdapter가 아이템 리스트에서 어떤 변경 사항이 발생했는지 판단, 최소한의 업데이트 수행
        // 두 아이템이 동일한지 비교
        override fun areItemsTheSame(oldItem: QuestShare, newItem: QuestShare): Boolean {
            return oldItem == newItem
        }
        // 두 아이템의 내용이 동일한지 비교
        override fun areContentsTheSame(oldItem: QuestShare, newItem: QuestShare): Boolean {
            return oldItem == newItem
        }
    }

    val eventUtilItemCallback = object: DiffUtil.ItemCallback<Event>(){
        override fun areItemsTheSame(oldItem: Event, newItem: Event): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: Event, newItem: Event): Boolean {
            return oldItem == newItem
        }
    }
}