package com.idle.togeduck.quest.share.view

import android.app.Dialog
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.core.content.ContextCompat
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.idle.togeduck.R
import com.idle.togeduck.common.ScreenSize
import com.idle.togeduck.common.Theme
import com.idle.togeduck.databinding.DialogQuestShareBinding
import com.idle.togeduck.quest.share.ShareViewModel
import com.idle.togeduck.util.DpPxUtil
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ShareDialogFragment : DialogFragment() {
    private var _binding: DialogQuestShareBinding? = null
    private val binding get() = _binding!!

    val shareViewModel: ShareViewModel by activityViewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NO_TITLE, R.style.fullscreen_dialog)
        isCancelable = true
    }

    // 들어오고 나갈때 애니메이션 설정
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState)
//        dialog.window!!.attributes.windowAnimations = R.style.dialog_animation
        dialog.window!!.addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)
        return dialog
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DialogQuestShareBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setImgSize()
        setTheme()

        shareViewModel.selectedShare.observe(viewLifecycleOwner) { share ->
            binding.tvTitle.text = share.title
            binding.tvContent.text = share.content

            Glide
                .with(binding.ivMainImg)
                .load(share.image)
                .into(binding.ivMainImg)

            setImgSize()
        }

        binding.llEmptyLayout.setOnClickListener {
            findNavController().navigate(R.id.action_shareDialogFragment_pop)
        }
    }

    private fun setImgSize() {
        val newSize = (DpPxUtil.dpToPx(ScreenSize.heightDp - 360, requireContext()) * 0.5).toInt()
        val layoutParams = binding.ivMainImg.layoutParams as LinearLayout.LayoutParams
        layoutParams.height = newSize
        binding.ivMainImg.scaleType = ImageView.ScaleType.FIT_CENTER
        binding.ivMainImg.layoutParams = layoutParams
    }

    private fun setTheme() {
        val allRoundDrawable = ContextCompat.getDrawable(requireContext(), R.drawable.shape_all_round_10) as GradientDrawable
        allRoundDrawable.setColor(ContextCompat.getColor(requireContext(), Theme.theme.main200))
        allRoundDrawable.setStroke(0, ContextCompat.getColor(requireContext(), Theme.theme.main500))
        binding.llDialogLayout.background = allRoundDrawable

        val squareCircleDrawable = ContextCompat.getDrawable(requireContext(), R.drawable.shape_square_circle) as GradientDrawable
        squareCircleDrawable.setColor(ContextCompat.getColor(requireContext(), Theme.theme.sub400))
        squareCircleDrawable.setStroke(0, ContextCompat.getColor(requireContext(), Theme.theme.main500))

        binding.btnEdit.background = squareCircleDrawable
        binding.btnDelete.background = squareCircleDrawable
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}