package com.idle.togeduck.quest.exchange.view

import android.app.Dialog
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.idle.togeduck.R
import com.idle.togeduck.common.Theme
import com.idle.togeduck.databinding.DialogQuestExchangePostBinding
import com.idle.togeduck.databinding.DialogQuestExchangeRequestedBinding
import com.idle.togeduck.event.EventListViewModel
import com.idle.togeduck.favorite.FavoriteSettingViewModel
import com.idle.togeduck.fcm.FCMData
import com.idle.togeduck.quest.exchange.ExchangeViewModel
import com.idle.togeduck.quest.exchange.model.Exchange
import com.idle.togeduck.quest.exchange.model.ExchangeResponse
import com.idle.togeduck.util.MultiPartUtil
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ExchangeRequestedDialogFragment: DialogFragment() {
    private var _binding: DialogQuestExchangeRequestedBinding? = null
    private val binding get() = _binding!!

    private val exchangeViewModel: ExchangeViewModel by activityViewModels()


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
        _binding = DialogQuestExchangeRequestedBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setTheme()

        exchangeViewModel.yourExchangeData.observe(viewLifecycleOwner) { exchange ->
            Glide
                .with(binding.ivYourImg)
                .load(exchange.image)
                .override(700, 700)
                .into(binding.ivYourImg)
        }

        exchangeViewModel.myExchangeData.observe(viewLifecycleOwner) { exchange ->
            Glide
                .with(binding.ivMyImg)
                .load(exchange.image)
                .override(700, 700)
                .into(binding.ivMyImg)
        }

        FCMData.dealId.observe(viewLifecycleOwner) { dealId ->
            binding.btnAccept.setOnClickListener {
                CoroutineScope(Dispatchers.IO).launch {
                exchangeViewModel.acceptExchange(dealId)
                }
                findNavController().navigate(R.id.action_exchangeRequestedDialogFragment_pop)
            }

            binding.btnRefuse.setOnClickListener {
                CoroutineScope(Dispatchers.IO).launch {
                exchangeViewModel.rejectExchange(dealId)
                }
                findNavController().navigate(R.id.action_exchangeRequestedDialogFragment_pop)
            }
        }
    }


    private fun setTheme() {
        val allRoundDrawable = ContextCompat.getDrawable(requireContext(), R.drawable.shape_all_round_10) as GradientDrawable
        allRoundDrawable.setColor(ContextCompat.getColor(requireContext(), Theme.theme.main200))
        allRoundDrawable.setStroke(0, ContextCompat.getColor(requireContext(), Theme.theme.main500))
        binding.llQuestRequestedLayout.background = allRoundDrawable

        val acceptDrawable = ContextCompat.getDrawable(requireContext(), R.drawable.shape_square_circle) as GradientDrawable
        acceptDrawable.setColor(ContextCompat.getColor(requireContext(), R.color.green))
        acceptDrawable.setStroke(0, ContextCompat.getColor(requireContext(), R.color.green))

        val refuseDrawable = ContextCompat.getDrawable(requireContext(), R.drawable.shape_square_circle) as GradientDrawable
        refuseDrawable.setColor(ContextCompat.getColor(requireContext(), R.color.red))
        refuseDrawable.setStroke(0, ContextCompat.getColor(requireContext(), R.color.red))

        binding.btnAccept.background = acceptDrawable
        binding.btnRefuse.background = refuseDrawable
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}