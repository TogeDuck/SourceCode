package com.idle.togeduck.event.view.list

import android.graphics.drawable.GradientDrawable
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat

import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.idle.togeduck.R

import com.idle.togeduck.databinding.FragmentEventListBinding
import com.idle.togeduck.common.Theme
import com.idle.togeduck.event.view.list.list_rv.EventInfo
import com.idle.togeduck.event.view.list.list_rv.EventInfoAdapter
import com.idle.togeduck.event.EventListViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@AndroidEntryPoint
class EventListFragment : Fragment(), EventInfo {
    private var _binding: FragmentEventListBinding? = null
    private val binding get() = _binding!!

    private val eventListViewModel: EventListViewModel by activityViewModels()

    private lateinit var todayEventInfoAdapter: EventInfoAdapter
    private lateinit var upcomingEventInfoAdapter: EventInfoAdapter
    private lateinit var pastEventInfoAdapter: EventInfoAdapter

    private var todayEventsList: MutableList<Event?> = mutableListOf()
    private var upcomingEventsList: MutableList<Event?> = mutableListOf()
    private var pastEventsList: MutableList<Event?> = mutableListOf()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentEventListBinding.inflate(inflater, container, false)
        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setTheme()
        setRecyclerview()

    }

    private fun setRecyclerview() {
        todayEventInfoAdapter = EventInfoAdapter(this, requireContext())
        upcomingEventInfoAdapter = EventInfoAdapter(this, requireContext())
        pastEventInfoAdapter = EventInfoAdapter(this, requireContext())

        binding.rvEventToday.apply {
            adapter = todayEventInfoAdapter
            layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        }

        binding.rvEventUpcoming.apply {
            adapter = upcomingEventInfoAdapter
            layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        }

        binding.rvEventPast.apply {
            adapter = pastEventInfoAdapter
            layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun setTheme() {
        binding.today.setTextColor(ContextCompat.getColor(requireContext(), Theme.theme.main500))
        binding.upcoming.setTextColor(ContextCompat.getColor(requireContext(), Theme.theme.main300))
        binding.past.setTextColor(ContextCompat.getColor(requireContext(), R.color.gray_bg))

        val todayColor = ContextCompat.getColor(requireContext(), Theme.theme.sub500)
        val upComingColor = ContextCompat.getColor(requireContext(), Theme.theme.sub100)
        val pastColor = ContextCompat.getColor(requireContext(), R.color.gray_bg)

        divideDataByPeriod()

        //더미데이터 넣기
        eventListViewModel.eventList.observe(viewLifecycleOwner){ list ->
            todayEventInfoAdapter.submitList(todayEventsList)
        }
        eventListViewModel.eventList.observe(viewLifecycleOwner){ list ->
            upcomingEventInfoAdapter.submitList(upcomingEventsList)
        }
        eventListViewModel.eventList.observe(viewLifecycleOwner){ list ->
            pastEventInfoAdapter.submitList(pastEventsList)
        }

        //날짜 별 색상 지정..
        val todayEventDrawable = ContextCompat.getDrawable(requireContext(), R.drawable.shape_all_round_20) as GradientDrawable
//        binding.rvEventToday.background = todayEventDrawable
        todayEventDrawable.setColor(todayColor)

    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun divideDataByPeriod() {
        //전체 이벤트 데이터 가져오기
        var tempDataList = eventListViewModel.eventList.value

        //유저가 선택한 날짜
        var dateStart = LocalDate.parse("2024-01-04")
        var dateEnd = LocalDate.parse("2024-01-05")

        //이벤트 기간에 따라 맞는 리스트에 추가
        if (tempDataList != null) {
            for(event in tempDataList){
                val periodSplit = event.eventPeriod.split(" ~ ")
                val start = LocalDate.parse(periodSplit[0], DateTimeFormatter.ofPattern("yyyy.MM.dd"))
                val end = LocalDate.parse(periodSplit[1], DateTimeFormatter.ofPattern("yyyy.MM.dd"))

                val addToList = when {
                    dateEnd.isBefore(start) -> upcomingEventsList.add(event)
                    dateStart.isAfter(end) -> pastEventsList.add(event)
                    else -> todayEventsList.add(event)
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}