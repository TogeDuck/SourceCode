package com.idle.togeduck.history.view

import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.idle.togeduck.R
import com.idle.togeduck.common.Theme
import com.idle.togeduck.databinding.FragmentHistoryBinding
import com.idle.togeduck.di.PreferenceModule
import com.idle.togeduck.event.EventListViewModel
import com.idle.togeduck.favorite.FavoriteSettingViewModel
import com.idle.togeduck.history.HistoryViewModel
import com.idle.togeduck.history.view.history_rv.HistoryAdapter
import com.idle.togeduck.history.view.history_rv.IHistory
import com.idle.togeduck.main_map.MapViewModel
import com.idle.togeduck.main_map.view.MapFragment
import com.idle.togeduck.util.TogeDuckItemDecoration
import com.idle.togeduck.util.getColor
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject


@AndroidEntryPoint
class HistoryFragment : Fragment(), IHistory {
    private var _binding: FragmentHistoryBinding? = null
    private val binding get() = _binding!!

    private lateinit var historyAdapter: HistoryAdapter

    private val historyViewModel: HistoryViewModel by activityViewModels()
    private val eventListViewModel: EventListViewModel by activityViewModels()
    private val mapViewModel: MapViewModel by activityViewModels()
    private val favoriteSettingViewModel: FavoriteSettingViewModel by activityViewModels()

    @Inject
    lateinit var preference: PreferenceModule

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentHistoryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setRecyclerView()
        setTheme()
        getHistoryList()

        historyViewModel.historyList.observe(viewLifecycleOwner) { historyList ->
            Log.d("로그", "HistoryFragment - onViewCreated() 데이터 바뀜")
            historyAdapter.submitList(historyList)
        }

        binding.tvMyThemeContent.text = Theme.theme.name
        binding.tvMyCakeCnt.text = Theme.myCake.toString()

        binding.ivThemeDraw.setOnClickListener {
            binding.ivThemeDraw.isClickable = false
            Handler(Looper.getMainLooper()).postDelayed({
                binding.ivThemeDraw.isClickable = true
            }, 500L)

            if (Theme.myCake > 0) {
                binding.tvMyCakeCnt.text = Theme.myCake.toString()

                CoroutineScope(Dispatchers.IO).launch {
                    preference.setCakeCount(--Theme.myCake)
                }

                var newTheme = Theme.themeList.random()

                while (Theme.theme == newTheme) {
                    newTheme = Theme.themeList.random()
                }

                CoroutineScope(Dispatchers.IO).launch {
                    preference.setTheme(newTheme)
                }
                Theme.theme = newTheme
                binding.tvMyThemeContent.text = newTheme.name

                Toast.makeText(
                    requireContext(),
                    "${Theme.theme.name} 테마를 뽑으셨습니다!",
                    Toast.LENGTH_LONG
                ).show()

                historyViewModel.setNeedRefresh(true)
            } else {
                Toast.makeText(requireContext(), "케잌이 부족하여 테마를 뽑을 수 없습니다", Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }

    override fun onResume() {
        super.onResume()
        Log.d("로그", "HistoryFragment - onResume() 호출됨")
        getHistoryList()
    }

    private fun getHistoryList() {
        CoroutineScope(Dispatchers.IO).launch {
            historyViewModel.getHistoryList(favoriteSettingViewModel.selectedCelebrity.value!!.id)
        }
    }

    private fun setRecyclerView() {
        historyAdapter = HistoryAdapter(this, requireContext())

        binding.rvMyRecord.apply {
            addItemDecoration(TogeDuckItemDecoration(10, 0))
            adapter = historyAdapter
            layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        }
    }

    private fun setTheme() {
        binding.tvMyCakeCnt.setTextColor(getColor(requireContext(), Theme.theme.sub500))

        val circleDrawable = ContextCompat.getDrawable(
            requireContext(),
            com.idle.togeduck.R.drawable.shape_circle
        ) as GradientDrawable
        circleDrawable.setColor(getColor(requireContext(), Theme.theme.main500))
        circleDrawable.setStroke(0, Theme.theme.main500)
        binding.tvMyThemeContent.background = circleDrawable

        val strokeCircleDrawable = ContextCompat.getDrawable(
            requireContext(),
            com.idle.togeduck.R.drawable.shape_circle
        ) as GradientDrawable
        strokeCircleDrawable.setColor(getColor(requireContext(), com.idle.togeduck.R.color.white))
        strokeCircleDrawable.setStroke(4, getColor(requireContext(), Theme.theme.main500))
        binding.ivThemeDraw.background = strokeCircleDrawable

        binding.tvVisited.setTextColor(getColor(requireContext(), Theme.theme.main500))
    }

    override fun recordClicked(position: Int) {
        eventListViewModel.clearList()
        mapViewModel.clearList()

        historyViewModel.setSelectedHistory(historyViewModel.historyList.value!![position])
        (parentFragment as MapFragment).changeViewPagerPage(5)
    }

    override fun onDestroyView() {
        super.onDestroyView()
    }
}