package com.idle.togeduck.view.quest_talk

import android.content.Context
import android.graphics.drawable.GradientDrawable
import android.view.View
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.idle.togeduck.R
import com.idle.togeduck.databinding.ItemQuestTalkMyBinding
import com.idle.togeduck.model.QuestTalk
import com.idle.togeduck.util.Theme

class QuestTalkMyViewHolder (
    binding: ItemQuestTalkMyBinding,
    private var questTalkDetail: IQuestTalkDetail
) : RecyclerView.ViewHolder(binding.root), View.OnClickListener{
    private val questTalkLayout = binding.itemQuestTalkMy
    private val questTalkIconContainer = binding.questTalkMyIconContainer
    private val questTalkContent = binding.questTalkMyContent

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
        roundSquare.setColor(ContextCompat.getColor(context, Theme.theme.main200))
        roundSquare.setStroke(0,0)
        circle.setColor(ContextCompat.getColor(context, Theme.theme.main500))
        circle.setStroke(0,0)
        questTalkContent.background = roundSquare
        questTalkIconContainer.background = circle
        questTalkContent.setText(questTalk.content)
    }
}