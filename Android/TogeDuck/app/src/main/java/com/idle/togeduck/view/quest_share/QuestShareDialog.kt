package com.idle.togeduck.view.quest_share

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.core.content.ContextCompat
import androidx.fragment.app.DialogFragment
import com.idle.togeduck.R
import com.idle.togeduck.databinding.DialogQuestShareBinding
import com.idle.togeduck.util.Theme


class QuestShareDialog(questShare: QuestShare) : DialogFragment() {

    private var _binding: DialogQuestShareBinding? = null
    private val binding get() = _binding!!
    private val questShare = questShare

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = DialogQuestShareBinding.inflate(inflater, container, false)
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Shape Setup
        // ConstraintLayout Background
        val roundCorner = ContextCompat.getDrawable(requireContext(),R.drawable.shape_all_round_10) as GradientDrawable
        roundCorner.setColor(ContextCompat.getColor(requireContext(), Theme.theme.sub100))
        binding.dialogQuestShare.background = roundCorner

        // questShare 데이터 설정
        binding.dialogQuestShareTitle.text = questShare?.title
        binding.dialogQuestShareContent.text = questShare?.content
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


    /*private lateinit var binding: DialogQuestShareBinding
    private val dialog = Dialog(context)

    fun show( questShare: QuestShare ){
        // Dialog Settings
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.dialog_quest_share)
        dialog.window?.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT)
        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)

        // Layout Settings
        val questShareView = dialog.findViewById<ConstraintLayout>(R.id.dialog_quest_share)
        val titleContainer = dialog.findViewById<ImageView>(R.id.dialog_quest_share_title_container)
        val title = dialog.findViewById<TextView>(R.id.dialog_quest_share_title)
        val image = dialog.findViewById<ImageView>(R.id.dialog_quest_share_image)
        val content = dialog.findViewById<TextView>(R.id.dialog_quest_share_content)

        // Shape Setup
        // ConstraintLayout Background
        val roundCorner = ContextCompat.getDrawable(context,R.drawable.shape_all_round_10) as GradientDrawable
        roundCorner.setColor(ContextCompat.getColor(context, Theme.theme.sub100))
        questShareView.background = roundCorner
        // TitleContainer Background
        val roundCornerSmall = ContextCompat.getDrawable(context,R.drawable.shape_upper_round_10) as GradientDrawable
        roundCornerSmall.setColor(ContextCompat.getColor(context, Theme.theme.main300))
        titleContainer.background = roundCornerSmall
        // Image Setop
        image.setBackgroundColor(ContextCompat.getColor(context, Theme.theme.main500))

        // Data Insert
        title.setText(questShare.title)
        content.setText(questShare.content)

        dialog.show()
    }*/
}