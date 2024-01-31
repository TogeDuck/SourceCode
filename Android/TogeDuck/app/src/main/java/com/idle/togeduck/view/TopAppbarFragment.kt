package com.idle.togeduck.view

import android.graphics.drawable.GradientDrawable
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.idle.togeduck.R
import com.idle.togeduck.databinding.ComponentSearchBarTopAppbarBinding
import com.idle.togeduck.databinding.ComponentTopAppbarBinding
import com.idle.togeduck.databinding.FragmentTopAppbarBinding
import com.idle.togeduck.util.CalcStatusBarSize.getStatusBarHeightToDp
import com.idle.togeduck.util.DpPxUtil.dpToPx
import com.idle.togeduck.util.Theme
import com.idle.togeduck.util.getColor
import com.idle.togeduck.util.toAlpha
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TopAppbarFragment : Fragment() {
    private var _binding: FragmentTopAppbarBinding? = null
    private val binding get() = _binding!!

    private var _topAppBarBinding: ComponentTopAppbarBinding? = null
    private val topAppbarBinding get() = _topAppBarBinding!!


    private var _searchBarBinding: ComponentSearchBarTopAppbarBinding? = null
    private val searchBarBinding get() = _searchBarBinding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTopAppbarBinding.inflate(inflater, container, false)
        _topAppBarBinding = binding.cta
        _searchBarBinding = topAppbarBinding.csb
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val statusDp = getStatusBarHeightToDp(requireContext())

        topAppbarBinding.llTopAppbar.setPadding(
            dpToPx(20, requireContext()),
            dpToPx(statusDp + 5, requireContext()),
            dpToPx(20, requireContext()),
            dpToPx(10, requireContext())
        )

        setTheme()
    }

    private fun setTheme() {
        val bottomRoundDrawable = ContextCompat.getDrawable(requireContext(), R.drawable.shape_bottom_round_25) as GradientDrawable
        bottomRoundDrawable.setColor(getColor(requireContext(), Theme.theme.main500))
        bottomRoundDrawable.alpha = 0.6.toAlpha()

        topAppbarBinding.llTopAppbar.background = bottomRoundDrawable

        val yellowCircleDrawable = ContextCompat.getDrawable(requireContext(), R.drawable.shape_circle) as GradientDrawable
        yellowCircleDrawable.setColor(getColor(requireContext(), Theme.theme.main100))
        yellowCircleDrawable.setStroke(4, getColor(requireContext(), R.color.yellow))

        topAppbarBinding.ivLogo.background = yellowCircleDrawable
        topAppbarBinding.ivFavorite.background = yellowCircleDrawable

        val main500CircleDrawable = ContextCompat.getDrawable(requireContext(), R.drawable.shape_circle) as GradientDrawable
        main500CircleDrawable.setColor(getColor(requireContext(), Theme.theme.main100))
        main500CircleDrawable.setStroke(4, getColor(requireContext(), Theme.theme.main500))

        searchBarBinding.ivSearch.background = main500CircleDrawable
        searchBarBinding.ivSearch.setColorFilter(getColor(requireContext(), Theme.theme.main500))
        topAppbarBinding.ivIdolProfile.background = main500CircleDrawable
        // TODO. 실제 프로필 사진 적용시 삭제 필요
        topAppbarBinding.ivIdolProfile.setColorFilter(getColor(requireContext(), Theme.theme.main500))
        topAppbarBinding.ivCalendar.background = main500CircleDrawable
        topAppbarBinding.ivCalendar.setColorFilter(getColor(requireContext(), Theme.theme.main500))

        val squareCircleDrawable = ContextCompat.getDrawable(requireContext(), R.drawable.shape_square_circle) as GradientDrawable
        squareCircleDrawable.setColor(getColor(requireContext(), Theme.theme.main100))
        squareCircleDrawable.setStroke(4, getColor(requireContext(), Theme.theme.main500))

        val cursorDrawable = ContextCompat.getDrawable(requireContext(), R.drawable.shape_cursor) as GradientDrawable
        cursorDrawable.setColor(getColor(requireContext(), Theme.theme.main500))

        @RequiresApi(Build.VERSION_CODES.Q)
        searchBarBinding.etSearch.textCursorDrawable = cursorDrawable
        searchBarBinding.etSearch.background = squareCircleDrawable
        searchBarBinding.etSearch.setTextColor(getColor(requireContext(), Theme.theme.main500))
        searchBarBinding.etSearch.setHintTextColor(getColor(requireContext(), Theme.theme.main500))

        topAppbarBinding.tvDate.setTextColor(getColor(requireContext(), Theme.theme.main500))

        val allRoundDrawable = ContextCompat.getDrawable(requireContext(), R.drawable.shape_all_round_5) as GradientDrawable
        allRoundDrawable.setColor(getColor(requireContext(), Theme.theme.main100))
        allRoundDrawable.setStroke(0, getColor(requireContext(), Theme.theme.main500))
        topAppbarBinding.tvDate.background = allRoundDrawable
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        _topAppBarBinding = null
        _searchBarBinding = null
    }
}