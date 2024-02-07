package com.idle.togeduck.util

import androidx.recyclerview.widget.DiffUtil
import com.idle.togeduck.event.model.Event
import com.idle.togeduck.event.model.EventReviewContent
import com.idle.togeduck.favorite.model.Celebrity
import com.idle.togeduck.history.model.HistoryData
import com.idle.togeduck.myquest.model.MyQuest
import com.idle.togeduck.quest.exchange.model.Exchange
import com.idle.togeduck.quest.exchange.model.MyExchange
import com.idle.togeduck.quest.recruit.model.Recruit
import com.idle.togeduck.quest.share.model.Share
import com.idle.togeduck.quest.talk.model.Talk

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

    val exchangeDiffUtilCallback = object: DiffUtil.ItemCallback<Exchange>() {
        override fun areItemsTheSame(oldItem: Exchange, newItem: Exchange): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Exchange, newItem: Exchange): Boolean {
            return oldItem == newItem
        }
    }

    val shareDiffUtilCallback = object: DiffUtil.ItemCallback<Share>() {
        override fun areItemsTheSame(oldItem: Share, newItem: Share): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Share, newItem: Share): Boolean {
            return oldItem == newItem
        }
    }

    val talkDiffUtilCallback = object: DiffUtil.ItemCallback<Talk>() {
        override fun areItemsTheSame(oldItem: Talk, newItem: Talk): Boolean {
            return oldItem.chatId == newItem.chatId
        }

        override fun areContentsTheSame(oldItem: Talk, newItem: Talk): Boolean {
            return oldItem == newItem
        }
    }

    val reviewDiffUtilCallback = object: DiffUtil.ItemCallback<EventReviewContent>() {
        override fun areItemsTheSame(oldItem: EventReviewContent, newItem: EventReviewContent): Boolean {
            return oldItem.reviewId == newItem.reviewId
        }

        override fun areContentsTheSame(oldItem: EventReviewContent, newItem: EventReviewContent): Boolean {
            return oldItem == newItem
        }
    }

    val eventDiffUtilCallback = object: DiffUtil.ItemCallback<Event>() {
        override fun areItemsTheSame(oldItem: Event, newItem: Event): Boolean {
            return oldItem.eventId == newItem.eventId
        }

        override fun areContentsTheSame(oldItem: Event, newItem: Event): Boolean {
            return oldItem == newItem
        }
    }

    val myExchangeDiffUtilCallback = object : DiffUtil.ItemCallback<MyExchange>() {
        override fun areItemsTheSame(oldItem: MyExchange, newItem: MyExchange): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: MyExchange, newItem: MyExchange): Boolean {
            return oldItem == newItem
        }
    }

    val myQuestDiffUtilCallback = object : DiffUtil.ItemCallback<MyQuest>() {
        override fun areItemsTheSame(oldItem: MyQuest, newItem: MyQuest): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: MyQuest, newItem: MyQuest): Boolean {
            return oldItem == newItem
        }
    }

}