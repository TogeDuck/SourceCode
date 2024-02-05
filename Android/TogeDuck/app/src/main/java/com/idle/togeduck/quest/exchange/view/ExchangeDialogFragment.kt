package com.idle.togeduck.quest.exchange.view

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
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.idle.togeduck.R
import com.idle.togeduck.common.ScreenSize
import com.idle.togeduck.common.Theme
import com.idle.togeduck.databinding.DialogQuestExchangeBinding
import com.idle.togeduck.quest.exchange.ExchangeViewModel
import com.idle.togeduck.quest.exchange.model.MyExchange
import com.idle.togeduck.quest.exchange.view.my_exchange_rv.IMyExchangeDetail
import com.idle.togeduck.quest.exchange.view.my_exchange_rv.MyExchangeAdapter
import com.idle.togeduck.util.DpPxUtil
import com.idle.togeduck.util.TogeDuckItemDecoration
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ExchangeDialogFragment: DialogFragment(), IMyExchangeDetail {
    private var _binding: DialogQuestExchangeBinding? = null
    private val binding get() = _binding!!

    val exchangeViewModel: ExchangeViewModel by activityViewModels()

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
        _binding = DialogQuestExchangeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setImgSize()
        setTheme()

        exchangeViewModel.selectedExchange.observe(viewLifecycleOwner) { exchange ->
            binding.tvContent.text = exchange.content

            Glide
                .with(binding.ivMainImg)
                .load(exchange.image)
                .into(binding.ivMainImg)

            setImgSize()
        }

        binding.questExchangeDialog.setOnClickListener {
            findNavController().navigate(R.id.action_exchangeDialogFragment_pop)
        }

        // RV Setup
        val recyclerView = binding.questExchangeDialogRv
        val myExchangeAdapter = MyExchangeAdapter(this, requireContext(), exchangeViewModel.mySelectedExchange.value)
        recyclerView.adapter = myExchangeAdapter
        recyclerView.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, true).apply { stackFromEnd = true }
        recyclerView.addItemDecoration(TogeDuckItemDecoration(0,10))

        exchangeViewModel.myExchangeList.observe(viewLifecycleOwner){
            list -> myExchangeAdapter.submitList(list)
        }
        CoroutineScope(Dispatchers.IO).launch {
            exchangeViewModel.getMyExchangeList(0)
        }

        exchangeViewModel.mySelectedExchange.observe(viewLifecycleOwner){
            myExchangeAdapter.notifyDataSetChanged()
        }
    }

    private fun setImgSize() {
        val newSize = (DpPxUtil.dpToPx(ScreenSize.heightDp - 360, requireContext()) * 0.5).toInt()
        val layoutParams = binding.ivMainImg.layoutParams as LinearLayout.LayoutParams
//        layoutParams.width = newSize
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

    override fun myExchangeItemClicked(myExchange: MyExchange) {
        exchangeViewModel.setMySelectedExchange(myExchange)
    }
}