package com.idle.togeduck.view.quest_share

import android.app.Dialog
import android.graphics.drawable.GradientDrawable
import android.view.Window
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.idle.togeduck.R
import com.idle.togeduck.databinding.DialogQuestShareBinding
import com.idle.togeduck.model.QuestShare
import com.idle.togeduck.util.Theme

class QuestShareDialog(private val context: AppCompatActivity) {
    private lateinit var binding: DialogQuestShareBinding
    private val dialog = Dialog(context)

    fun show( questShare: QuestShare ){
        binding = DialogQuestShareBinding.inflate(context.layoutInflater)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(binding.root)

        // Shape Setup
        val roundCorner = ContextCompat.getDrawable(context,R.drawable.shape_all_round_10) as GradientDrawable
        binding.dialogQuestShare.background = roundCorner
        binding.dialogQuestShareTitleContainer.setBackgroundColor(Theme.theme.main300)
        binding.dialogQuestShare.setBackgroundColor(Theme.theme.sub200)

        // Data Insert
        binding.dialogQuestShareTitle.setText(questShare.title)
        binding.dialogQuestShareContent.setText(questShare.content)

        dialog.show()
    }
}