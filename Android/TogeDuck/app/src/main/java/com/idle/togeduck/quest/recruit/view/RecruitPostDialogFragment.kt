package com.idle.togeduck.quest.recruit.view

import android.app.Dialog
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.idle.togeduck.R
import com.idle.togeduck.common.ScreenSize
import com.idle.togeduck.common.Theme
import com.idle.togeduck.databinding.DialogQuestExchangeBinding
import com.idle.togeduck.databinding.DialogQuestExchangePostBinding
import com.idle.togeduck.databinding.DialogQuestRecruitPostBinding
import com.idle.togeduck.quest.exchange.ExchangeViewModel
import com.idle.togeduck.quest.exchange.model.MyExchange
import com.idle.togeduck.quest.exchange.view.my_exchange_rv.IMyExchangeDetail
import com.idle.togeduck.quest.exchange.view.my_exchange_rv.MyExchangeAdapter
import com.idle.togeduck.quest.recruit.RecruitViewModel
import com.idle.togeduck.util.DpPxUtil
import com.idle.togeduck.util.TogeDuckItemDecoration
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class RecruitPostDialogFragment: DialogFragment() {
    private var _binding: DialogQuestRecruitPostBinding? = null
    private val binding get() = _binding!!

    val recruitViewModel: RecruitViewModel by activityViewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NO_TITLE, R.style.fullscreen_dialog)
        isCancelable = true
    }

    // 들어오고 나갈때 애니메이션 설정
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState)
        dialog.window!!.addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)
        return dialog
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DialogQuestRecruitPostBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setTheme()

    }


    private fun setTheme() {

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}