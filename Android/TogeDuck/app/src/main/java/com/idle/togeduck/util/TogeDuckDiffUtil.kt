package com.idle.togeduck.util

import androidx.recyclerview.widget.DiffUtil
import com.idle.togeduck.model.FavoriteIdol

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
}