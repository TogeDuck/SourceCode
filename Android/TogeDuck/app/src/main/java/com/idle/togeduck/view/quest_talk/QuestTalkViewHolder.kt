package com.idle.togeduck.view.quest_talk

import android.content.Context
import android.graphics.drawable.GradientDrawable
import android.view.View
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.idle.togeduck.R
import com.idle.togeduck.databinding.ItemQuestTalkBinding
import com.idle.togeduck.model.QuestTalk
import com.idle.togeduck.util.Theme

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

    fun binding(questTalk: QuestTalk, context: Context) {
        setTheme(questTalk, context)
    }

    override fun onClick(v: View?) {
        when(v){
            questTalkLayout -> {
                questTalkDetail.myQuestItemClicked(bindingAdapterPosition)
            }
        }
    }

    private fun setTheme(questTalk: QuestTalk, context: Context) {
        val roundSquare = ContextCompat.getDrawable(context, R.drawable.shape_all_round_20) as GradientDrawable
        val circle = ContextCompat.getDrawable(context, R.drawable.shape_circle) as GradientDrawable
        roundSquare.setColor(ContextCompat.getColor(context, Theme.theme.sub200))
        roundSquare.setStroke(0,0)
        circle.setColor(ContextCompat.getColor(context, Theme.theme.sub500))
        circle.setStroke(0,0)
        questTalkIconContainer.background = circle
        questTalkUser.setText(questTalk.user)
        questTalkContent.background = roundSquare
        questTalkContent.setText(questTalk.content)
    }
}