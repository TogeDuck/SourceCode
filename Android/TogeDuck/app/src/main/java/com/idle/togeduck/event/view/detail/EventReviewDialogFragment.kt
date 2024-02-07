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
import androidx.core.net.toUri
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.idle.togeduck.R
import com.idle.togeduck.common.ScreenSize
import com.idle.togeduck.common.Theme
import com.idle.togeduck.databinding.DialogEventReviewBinding
import com.idle.togeduck.event.EventViewModel
import com.idle.togeduck.util.DpPxUtil
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class EventReviewDialogFragment : DialogFragment() {
    private var _binding: DialogEventReviewBinding? = null
    private val binding get() = _binding!!

    val eventViewModel: EventViewModel by activityViewModels()

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
        _binding = DialogEventReviewBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setImgSize()
        setTheme()

        eventViewModel.selectedImg.observe(viewLifecycleOwner) { uri ->
            binding.selectedImg.setImageURI(uri)

            Glide
                .with(binding.selectedImg)
                .load(uri)
                .into(binding.selectedImg)
        }

        binding.llEmptyLayout.setOnClickListener {
            findNavController().navigate(R.id.action_eventReviewDialogFragment_pop)
        }
    }

    private fun setImgSize() {
        val newSize = (DpPxUtil.dpToPx(ScreenSize.heightDp - 360, requireContext()) * 0.5).toInt()
        val layoutParams = binding.selectedImg.layoutParams as LinearLayout.LayoutParams
        layoutParams.height = newSize
        binding.selectedImg.scaleType = ImageView.ScaleType.FIT_CENTER
        binding.selectedImg.layoutParams = layoutParams
    }

    private fun setTheme() {
        val allRoundDrawable = ContextCompat.getDrawable(requireContext(), R.drawable.shape_all_round_10) as GradientDrawable
        allRoundDrawable.setColor(ContextCompat.getColor(requireContext(), Theme.theme.main200))
        allRoundDrawable.setStroke(0, ContextCompat.getColor(requireContext(), Theme.theme.main500))
        binding.llReviewImgLayout.background = allRoundDrawable
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}