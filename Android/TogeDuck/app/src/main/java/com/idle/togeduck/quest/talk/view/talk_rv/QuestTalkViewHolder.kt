package com.idle.togeduck.quest.talk.view.talk_rv

import android.content.Context
import android.graphics.drawable.GradientDrawable
import android.view.View
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.idle.togeduck.R
import com.idle.togeduck.databinding.ItemQuestTalkBinding
import com.idle.togeduck.common.Theme
import com.idle.togeduck.quest.talk.model.Talk

class QuestTalkViewHolder (
    binding: ItemQuestTalkBinding,
    private var questTalkDetail: IQuestTalkDetail
) : RecyclerView.ViewHolder(binding.root), View.OnClickListener{
    private val questTalkLayout = binding.itemQuestTalk
    private val questTalkIconContainer = binding.questTalkIconContainer
    private val questTalkUser = binding.questTalkUser
    private val questTalkContent = binding.questTalkContent

    init {
        questTalkLayout.setOnClickListener(this)
    }

    fun binding(talk: Talk, context: Context) {
        setTheme(talk, context)
    }

    override fun onClick(v: View?) {
        when(v){
            questTalkLayout -> {
                questTalkDetail.myQuestItemClicked(bindingAdapterPosition)
            }
        }
    }

    private fun setTheme(talk: Talk, context: Context) {
        val roundSquare = ContextCompat.getDrawable(context, R.drawable.shape_all_round_20) as GradientDrawable
        val circle = ContextCompat.getDrawable(context, R.drawable.shape_circle) as GradientDrawable
        roundSquare.setColor(ContextCompat.getColor(context, Theme.theme.sub200))
        roundSquare.setStroke(0,0)
        circle.setColor(ContextCompat.getColor(context, Theme.theme.sub500))
        circle.setStroke(0,0)
        questTalkIconContainer.background = circle
        questTalkUser.setText(talk.userId)
        questTalkContent.background = roundSquare
        questTalkContent.text = talk.content
    }
}