package com.idle.togeduck.main_map.view

import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.content.ContextCompat
import androidx.core.util.Pair
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.MaterialDatePicker
import com.idle.togeduck.R
import com.idle.togeduck.common.Theme
import com.idle.togeduck.databinding.ComponentTopAppbarBinding
import com.idle.togeduck.databinding.FragmentTopAppbarBinding
import com.idle.togeduck.di.PreferenceModule
import com.idle.togeduck.event.EventListViewModel
import com.idle.togeduck.favorite.FavoriteSettingViewModel
import com.idle.togeduck.history.HistoryViewModel
import com.idle.togeduck.main_map.MapViewModel
import com.idle.togeduck.util.CalcStatusBarSize.getStatusBarHeightToDp
import com.idle.togeduck.util.DpPxUtil.dpToPx
import com.idle.togeduck.util.getColor
import com.idle.togeduck.util.toAlpha
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.datetime.toKotlinLocalDate
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Calendar
import javax.inject.Inject

@AndroidEntryPoint
class TopAppbarFragment : Fragment() {
    private var _binding: FragmentTopAppbarBinding? = null
    private val binding get() = _binding!!

    private var _topAppBarBinding: ComponentTopAppbarBinding? = null
    private val topAppbarBinding get() = _topAppBarBinding!!

//    private var _searchBarBinding: ComponentSearchBarTopAppbarBinding? = null
//    private val searchBarBinding get() = _searchBarBinding!!

    private val mapViewModel: MapViewModel by activityViewModels()
    private val favoriteSettingViewModel: FavoriteSettingViewModel by activityViewModels()
    private val eventListViewModel: EventListViewModel by activityViewModels()
    private val historyViewModel: HistoryViewModel by activityViewModels()

    @Inject
    lateinit var preference: PreferenceModule

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentTopAppbarBinding.inflate(inflater, container, false)
        _topAppBarBinding = binding.cta
//        _searchBarBinding = topAppbarBinding.csb
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setPadding()
        setTheme()
        setIdolProfile()
        setDateRangePicker()
        showSelectCelebrity()

        mapViewModel.pickedDate.observe(viewLifecycleOwner) { data ->
            val dateTimeFormatter = DateTimeFormatter.ofPattern("yy/MM/dd")
            val dateRangeText =
                "${data.first.format(dateTimeFormatter)}-${data.second.format(dateTimeFormatter)}"
            topAppbarBinding.tvDate.text = dateRangeText
            getEventList()
        }

        favoriteSettingViewModel.selectedCelebrity.observe(viewLifecycleOwner) { celebrity ->
            CoroutineScope(Dispatchers.IO).launch {
                val savedCelebrity = runBlocking {
                    preference.getSelectedCelebrity.first()
                }

                if (savedCelebrity == null || (celebrity != null && savedCelebrity.id != celebrity.id)) {
                    preference.setSelectedCelebrity(celebrity)
                }

                val celebrityId =
                    favoriteSettingViewModel.selectedCelebrity.value?.id ?: return@launch
                val (startDate, endDate) = mapViewModel.pickedDate.value ?: return@launch
                eventListViewModel.getEventList(
                    celebrityId,
                    startDate.toKotlinLocalDate(),
                    endDate.toKotlinLocalDate()
                )
                historyViewModel.getHistoryList(celebrityId)
            }

            setIdolProfile()
        }
    }

    private fun getEventList() {
        CoroutineScope(Dispatchers.IO).launch {
            val celebrityId = favoriteSettingViewModel.selectedCelebrity.value?.id ?: return@launch
            val (startDate, endDate) = mapViewModel.pickedDate.value ?: return@launch
            eventListViewModel.getEventList(
                celebrityId,
                startDate.toKotlinLocalDate(),
                endDate.toKotlinLocalDate()
            )
            historyViewModel.getHistoryList(celebrityId)
        }
    }

    private fun showSelectCelebrity() {
        topAppbarBinding.llIdol.setOnClickListener {
            findNavController().navigate(R.id.action_mapFragment_to_selectCelebrityFragment)
            topAppbarBinding.llIdol.isClickable = false

            Handler(Looper.getMainLooper()).postDelayed({
                topAppbarBinding.llIdol.isClickable = true
            }, 500L)
        }
    }

    private fun setDateRangePicker() {
        val calendar = Calendar.getInstance()

        calendar.add(Calendar.YEAR, -10)
        val startYear = calendar.timeInMillis
        calendar.add(Calendar.YEAR, 20)
        val endYear = calendar.timeInMillis

        val constraintsBuilder =
            CalendarConstraints.Builder()
                .setStart(startYear)
                .setEnd(endYear)

        val dateRangePicker = MaterialDatePicker.Builder.dateRangePicker()
            .setTheme(R.style.MyMaterialDatePickerTheme)
            .setSelection(
                Pair(
                    MaterialDatePicker.todayInUtcMilliseconds(),
                    MaterialDatePicker.todayInUtcMilliseconds()
                )
            )
            .setCalendarConstraints(constraintsBuilder.build())
            .setTitleText("날짜를 선택하세요")
            .build()

        val dateRangePickerTag = "dateRangePicker"
        val dateTimeFormatter = DateTimeFormatter.ofPattern("yy/MM/dd")

        dateRangePicker.addOnPositiveButtonClickListener { selection ->
            var instant = Instant.ofEpochMilli(selection.first)
            val startDate = instant.atZone(ZoneId.systemDefault()).toLocalDate()
            instant = Instant.ofEpochMilli(selection.second)
            val endDate = instant.atZone(ZoneId.systemDefault()).toLocalDate()
            val dateRangeText =
                "${startDate.format(dateTimeFormatter)}-${endDate.format(dateTimeFormatter)}"
            topAppbarBinding.tvDate.text = dateRangeText

            mapViewModel.setPickedDate(startDate, endDate)

            // TODO. API 연결
        }

        topAppbarBinding.llDate.setOnClickListener {
            val existingFragment = childFragmentManager.findFragmentByTag(dateRangePickerTag)

            if (existingFragment == null) {
                dateRangePicker.show(childFragmentManager, dateRangePickerTag)
            } else {
                if (existingFragment.isAdded) {
                    (existingFragment as DialogFragment).dismissAllowingStateLoss()
                }
            }
        }
    }

    private fun setPadding() {
        val statusDp = getStatusBarHeightToDp(requireContext())

        topAppbarBinding.llTopAppbar.setPadding(
            dpToPx(20, requireContext()),
            dpToPx(statusDp + 5, requireContext()),
            dpToPx(20, requireContext()),
            dpToPx(10, requireContext())
        )
    }

    private fun setIdolProfile() {
        val image = topAppbarBinding.ivIdolProfile
        val imageUrl = favoriteSettingViewModel.selectedCelebrity.value?.image
        Glide
            .with(image)
            .load(imageUrl)
            .thumbnail(
                Glide.with(image).load(imageUrl).override(80, 80)
            )
            .circleCrop()
            .override(80, 80)
            .into(image)
        topAppbarBinding.tvIdolName.text =
            favoriteSettingViewModel.selectedCelebrity.value?.nickname
    }

    private fun setTheme() {
        val bottomRoundDrawable = ContextCompat.getDrawable(
            requireContext(),
            R.drawable.shape_bottom_round_25
        ) as GradientDrawable
        bottomRoundDrawable.setColor(getColor(requireContext(), Theme.theme.main500))
        bottomRoundDrawable.alpha = 0.6.toAlpha()

        topAppbarBinding.llTopAppbar.background = bottomRoundDrawable

        val yellowCircleDrawable =
            ContextCompat.getDrawable(requireContext(), R.drawable.shape_circle) as GradientDrawable
        yellowCircleDrawable.setColor(getColor(requireContext(), Theme.theme.main100))
        yellowCircleDrawable.setStroke(4, getColor(requireContext(), Theme.theme.sub400))

        topAppbarBinding.ivLogo.background = yellowCircleDrawable

        val main100CircleDrawable =
            ContextCompat.getDrawable(requireContext(), R.drawable.shape_circle) as GradientDrawable
        main100CircleDrawable.setColor(getColor(requireContext(), Theme.theme.main100))
        main100CircleDrawable.setStroke(0, getColor(requireContext(), Theme.theme.main500))

        topAppbarBinding.ivIdolProfile.background = main100CircleDrawable
        topAppbarBinding.ivCalendar.background = main100CircleDrawable
        topAppbarBinding.ivCalendar.setColorFilter(getColor(requireContext(), Theme.theme.main500))

        val squareCircleDrawable = ContextCompat.getDrawable(
            requireContext(),
            R.drawable.shape_square_circle
        ) as GradientDrawable
        squareCircleDrawable.setColor(getColor(requireContext(), Theme.theme.main100))
        squareCircleDrawable.setStroke(4, getColor(requireContext(), Theme.theme.main500))

        val cursorDrawable =
            ContextCompat.getDrawable(requireContext(), R.drawable.shape_cursor) as GradientDrawable
        cursorDrawable.setColor(getColor(requireContext(), Theme.theme.main500))

//        @RequiresApi(Build.VERSION_CODES.Q)
//        searchBarBinding.etSearch.textCursorDrawable = cursorDrawable
//        searchBarBinding.etSearch.background = squareCircleDrawable
//        searchBarBinding.etSearch.setTextColor(getColor(requireContext(), Theme.theme.main500))
//        searchBarBinding.etSearch.setHintTextColor(getColor(requireContext(), Theme.theme.main500))

        topAppbarBinding.tvDate.setTextColor(getColor(requireContext(), Theme.theme.main500))

        val allRoundDrawable = ContextCompat.getDrawable(
            requireContext(),
            R.drawable.shape_all_round_5
        ) as GradientDrawable
        allRoundDrawable.setColor(getColor(requireContext(), Theme.theme.main100))
        allRoundDrawable.setStroke(0, getColor(requireContext(), Theme.theme.main500))
        topAppbarBinding.tvDate.background = allRoundDrawable
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        _topAppBarBinding = null
    }
}