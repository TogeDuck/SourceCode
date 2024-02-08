package com.idle.togeduck.main_map.view

import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.idle.togeduck.R
import com.idle.togeduck.common.Theme
import com.idle.togeduck.databinding.ComponentTourStartAndQuestPlusBinding
import com.idle.togeduck.databinding.FragmentTourStartAndQuestPlusBinding
import com.idle.togeduck.util.CalcStatusBarSize
import com.idle.togeduck.util.DpPxUtil
import com.idle.togeduck.util.getColor
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class TourStartAndQuestPlusFragment : Fragment() {
    private var _binding: FragmentTourStartAndQuestPlusBinding? = null
    private val binding get() = _binding!!

    private var _tourStartAndQuestPlusBinding: ComponentTourStartAndQuestPlusBinding? = null
    private val tourStartAndQuestPlusBinding get() = _tourStartAndQuestPlusBinding!!

    private lateinit var tourCircle: GradientDrawable


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTourStartAndQuestPlusBinding.inflate(inflater, container, false)
        _tourStartAndQuestPlusBinding = binding.tsqp
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setPadding()
        setTheme()

        //클릭하면 색 변경
        tourStartAndQuestPlusBinding.tourStartBtn.setOnClickListener {
            changeTourBtn()
        }

        tourStartAndQuestPlusBinding.questPlusBtn.setOnClickListener {
            tourStartAndQuestPlusBinding.plusToggle.visibility = View.VISIBLE
        }

        tourStartAndQuestPlusBinding.plusExchange.setOnClickListener {
            findNavController().navigate(R.id.action_mapFragment_to_exchangePostDialogFragment)
        }

        tourStartAndQuestPlusBinding.plusShare.setOnClickListener {
            findNavController().navigate(R.id.action_mapFragment_to_sharePostDialogFragment)
        }

        tourStartAndQuestPlusBinding.plusRecruit.setOnClickListener {
            findNavController().navigate(R.id.action_mapFragment_to_recruitPostDialogFragment)
        }


    }

    private fun changeTourBtn() {

        tourCircle.setColor(ContextCompat.getColor(requireContext(), R.color.red))
        tourStartAndQuestPlusBinding.startText.text = "종료"

        //todo. 투어버튼 상태 확인해서 시작 상태이면 종료로, 종료 상태이면 시작으로 바뀌는 거로 수정 필요
    }

    private fun setPadding() {
        tourStartAndQuestPlusBinding.clTourStart.setPadding(
            DpPxUtil.dpToPx(10, requireContext()),
            DpPxUtil.dpToPx(0, requireContext()),
            DpPxUtil.dpToPx(10, requireContext()),
            DpPxUtil.dpToPx(0, requireContext())
        )
    }


    private fun setTheme() {
        tourCircle = ContextCompat.getDrawable(requireContext(), R.drawable.shape_circle) as GradientDrawable
        tourCircle.setColor(ContextCompat.getColor(requireContext(), R.color.green))
        tourCircle.setStroke(0,0)
        tourStartAndQuestPlusBinding.tourStartBtn.background = tourCircle

        val plusCircle = ContextCompat.getDrawable(requireContext(), R.drawable.shape_circle) as GradientDrawable
        plusCircle.setColor(ContextCompat.getColor(requireContext(), Theme.theme.main500))
        plusCircle.setStroke(0,0)
        tourStartAndQuestPlusBinding.questPlusBtn.background = plusCircle

        val exchangeCircle = ContextCompat.getDrawable(requireContext(), R.drawable.shape_circle) as GradientDrawable
        exchangeCircle.setColor(ContextCompat.getColor(requireContext(), R.color.yellow))
        exchangeCircle.setStroke(0,0)
        tourStartAndQuestPlusBinding.plusExchange.background = exchangeCircle
        tourStartAndQuestPlusBinding.plusExchange.setColorFilter(getColor(requireContext(), R.color.white))

        val shareCircle = ContextCompat.getDrawable(requireContext(), R.drawable.shape_circle) as GradientDrawable
        shareCircle.setColor(ContextCompat.getColor(requireContext(), R.color.red))
        shareCircle.setStroke(0,0)
        tourStartAndQuestPlusBinding.plusShare.background = shareCircle
        tourStartAndQuestPlusBinding.plusShare.setColorFilter(getColor(requireContext(), R.color.white))

        val recruitCircle = ContextCompat.getDrawable(requireContext(), R.drawable.shape_circle) as GradientDrawable
        recruitCircle.setColor(ContextCompat.getColor(requireContext(), R.color.green))
        recruitCircle.setStroke(0,0)
        tourStartAndQuestPlusBinding.plusRecruit.background = recruitCircle
        tourStartAndQuestPlusBinding.plusRecruit.setColorFilter(getColor(requireContext(), R.color.white))
    }



    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        _tourStartAndQuestPlusBinding = null
    }
}