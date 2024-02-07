package com.idle.togeduck.main_map.view

import android.graphics.drawable.GradientDrawable
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.core.util.Pair
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.DateValidatorPointBackward
import com.google.android.material.datepicker.DateValidatorPointForward
import com.google.android.material.datepicker.MaterialDatePicker
import com.idle.togeduck.R
import com.idle.togeduck.common.Theme
import com.idle.togeduck.databinding.ComponentSearchBarTopAppbarBinding
import com.idle.togeduck.databinding.ComponentTopAppbarBinding
import com.idle.togeduck.databinding.ComponentTourStartAndQuestPlusBinding
import com.idle.togeduck.databinding.FragmentTopAppbarBinding
import com.idle.togeduck.databinding.FragmentTourStartAndQuestPlusBinding
import com.idle.togeduck.favorite.FavoriteSettingViewModel
import com.idle.togeduck.main_map.MapViewModel
import com.idle.togeduck.util.CalcStatusBarSize.getStatusBarHeightToDp
import com.idle.togeduck.util.DpPxUtil.dpToPx
import com.idle.togeduck.util.getColor
import com.idle.togeduck.util.toAlpha
import dagger.hilt.android.AndroidEntryPoint
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Calendar

@AndroidEntryPoint
class TourStartAndQuestPlusFragment : Fragment() {
    private var _binding: FragmentTourStartAndQuestPlusBinding? = null
    private val binding get() = _binding!!

    private var _tourStartAndQuestPlusBinding: ComponentTourStartAndQuestPlusBinding? = null
    private val tourStartAndQuestPlusBinding get() = _tourStartAndQuestPlusBinding!!

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

        setTheme()
        setPadding()

    }


    private fun setPadding() {

    }

    private fun setTheme() {
        val circle = ContextCompat.getDrawable(requireContext(), R.drawable.shape_circle) as GradientDrawable
        circle.setColor(ContextCompat.getColor(requireContext(), R.color.green))
        circle.setStroke(0,0)

        tourStartAndQuestPlusBinding.tourStartBtn

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        _tourStartAndQuestPlusBinding = null
    }
}