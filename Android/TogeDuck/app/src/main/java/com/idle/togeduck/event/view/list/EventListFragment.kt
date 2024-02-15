package com.idle.togeduck.event.view.list

import android.content.Intent
import android.graphics.drawable.GradientDrawable
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat

import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.ListAdapter
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.idle.togeduck.R

import com.idle.togeduck.databinding.FragmentEventListBinding
import com.idle.togeduck.common.Theme
import com.idle.togeduck.event.view.list.list_rv.EventInfo
import com.idle.togeduck.event.view.list.list_rv.EventInfoAdapter
import com.idle.togeduck.event.EventListViewModel
import com.idle.togeduck.event.model.Event
import com.idle.togeduck.event.model.LikeEventRequest
import com.idle.togeduck.event.view.detail.EventDetailFragment
import com.idle.togeduck.favorite.FavoriteSettingViewModel
import com.idle.togeduck.main_map.MapViewModel
import com.idle.togeduck.main_map.view.MapFragment
import com.idle.togeduck.util.getColor
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.datetime.LocalDate
import kotlinx.datetime.toKotlinLocalDate

@AndroidEntryPoint
class EventListFragment : Fragment(), EventInfo {
    private var _binding: FragmentEventListBinding? = null
    private val binding get() = _binding!!

    private val eventListViewModel: EventListViewModel by activityViewModels()
    private val favoriteSettingViewModel: FavoriteSettingViewModel by activityViewModels()
    private val mapViewModel: MapViewModel by activityViewModels()

    private lateinit var todayEventInfoAdapter: EventInfoAdapter
    private lateinit var upcomingEventInfoAdapter: EventInfoAdapter
    private lateinit var pastEventInfoAdapter: EventInfoAdapter


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        Log.d("로그", "EventListFragment - onCreateView() 호출됨")

        _binding = FragmentEventListBinding.inflate(inflater, container, false)
        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        Log.d("로그", "EventListFragment - onViewCreated() 호출됨")

        setTheme()
        setRecyclerview()

        eventListViewModel.listToday.observe(viewLifecycleOwner){ list ->
             todayEventInfoAdapter.submitList(list)

            if (list.isEmpty()) {
                binding.tvTodayEmptyEvent.visibility = View.VISIBLE
            } else {
                binding.tvTodayEmptyEvent.visibility = View.GONE
            }
        }

        eventListViewModel.listUpcoming.observe(viewLifecycleOwner){ list ->
            upcomingEventInfoAdapter.submitList(list)

            if (list.isEmpty()) {
                binding.tvUpcomingEmptyEvent.visibility = View.VISIBLE
            } else {
                binding.tvUpcomingEmptyEvent.visibility = View.GONE
            }
        }

        eventListViewModel.listPast.observe(viewLifecycleOwner){list ->
            pastEventInfoAdapter.submitList(list)

            if (list.isEmpty()) {
                binding.tvPastEmptyEvent.visibility = View.VISIBLE
            } else {
                binding.tvPastEmptyEvent.visibility = View.GONE
            }
        }

        eventListViewModel.isDetailOpen.observe(viewLifecycleOwner){check ->
            Log.d("디테일 페이지 오픈 요청", "요청 수신")
            if(check && eventListViewModel.selectedEvent.value != null){
                toDetailPage()
                eventListViewModel.isDetailOpen.value = false
            }
        }
    }

    override fun onResume() {
        Log.d("로그", "EventListFragment - onResume() 호출됨")
        super.onResume()
        getEventList()
    }

    private fun getEventList(){
        if(favoriteSettingViewModel.selectedCelebrity.value != null){
            CoroutineScope(Dispatchers.IO).launch{
                eventListViewModel.getEventList(
                    favoriteSettingViewModel.selectedCelebrity.value!!.id,
                    mapViewModel.pickedDate.value!!.first.toKotlinLocalDate(),
                    mapViewModel.pickedDate.value!!.second.toKotlinLocalDate())
            }
        }
    }

    override fun onPause() {
        Log.d("로그", "EventListFragment - onPause() 호출됨")
        super.onPause()
    }

    private fun setRecyclerview() {
        todayEventInfoAdapter = EventInfoAdapter(this, requireContext(), 0)
        upcomingEventInfoAdapter = EventInfoAdapter(this, requireContext(), 1)
        pastEventInfoAdapter = EventInfoAdapter(this, requireContext(), 2)

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

    private fun setTheme() {
        binding.tvTitle.setTextColor(ContextCompat.getColor(requireContext(),Theme.theme.main500))
        binding.today.setTextColor(ContextCompat.getColor(requireContext(), Theme.theme.main500))
        binding.upcoming.setTextColor(ContextCompat.getColor(requireContext(), Theme.theme.main300))
        binding.past.setTextColor(ContextCompat.getColor(requireContext(), R.color.gray_bg))

        val todayEmptyEventDrawable = ContextCompat.getDrawable(requireContext(), R.drawable.shape_all_round_20) as GradientDrawable
        todayEmptyEventDrawable.setColor(getColor(requireContext(), Theme.theme.sub400))
        todayEmptyEventDrawable.setStroke(0, getColor(requireContext(), Theme.theme.sub400))
        binding.tvTodayEmptyEvent.background = todayEmptyEventDrawable
        binding.tvTodayEmptyEvent.setTextColor(getColor(requireContext(), R.color.white))

        val upcomingEmptyEventDrawable = ContextCompat.getDrawable(requireContext(), R.drawable.shape_all_round_20) as GradientDrawable
        upcomingEmptyEventDrawable.setColor(getColor(requireContext(), Theme.theme.sub100))
        upcomingEmptyEventDrawable.setStroke(0, getColor(requireContext(), Theme.theme.sub100))
        binding.tvUpcomingEmptyEvent.background = upcomingEmptyEventDrawable
        binding.tvUpcomingEmptyEvent.setTextColor(getColor(requireContext(), Theme.theme.main500))

        val pastEmptyEventDrawable = ContextCompat.getDrawable(requireContext(), R.drawable.shape_all_round_20) as GradientDrawable
        pastEmptyEventDrawable.setColor(getColor(requireContext(), R.color.gray_bg))
        pastEmptyEventDrawable.setStroke(0, getColor(requireContext(), R.color.gray_bg))
        binding.tvPastEmptyEvent.background = pastEmptyEventDrawable
        binding.tvPastEmptyEvent.setTextColor(getColor(requireContext(), R.color.gray_text))
    }

    override fun onDestroyView() {
        Log.d("로그", "EventListFragment - onDestroyView() 호출됨")
        super.onDestroyView()
        _binding = null
    }

    //todo.타입별로 today, upcoming, past 확인하고 그거마다 상세화면 이동
    override fun eventClicked(position: Int, type: Int) {
        if(type == 0) {
            eventListViewModel.setSelectedEvent(eventListViewModel.listToday.value!![position])
            (parentFragment as MapFragment).changeViewPagerPage(2)
            mapViewModel.setBottomSheet(2)
        }else if(type == 1){
            eventListViewModel.setSelectedEvent(eventListViewModel.listUpcoming.value!![position])
            (parentFragment as MapFragment).changeViewPagerPage(2)
            mapViewModel.setBottomSheet(2)
        }else {
            eventListViewModel.setSelectedEvent(eventListViewModel.listPast.value!![position])
            (parentFragment as MapFragment).changeViewPagerPage(2)
            mapViewModel.setBottomSheet(2)
        }
    }

    fun toDetailPage() {
        Log.d("디테일 페이지로","실행")
        CoroutineScope(Dispatchers.Main).launch {
            (parentFragment as MapFragment).changeViewPagerPage(2, false)
            delay(100L)
            mapViewModel.setBottomSheet(2)
        }
    }

    override fun likeClick(position: Int, type: Int) {
        if (type == 0) {
            likeEventPerAdapterType(todayEventInfoAdapter, position)
        }else if (type == 1) {
            likeEventPerAdapterType(upcomingEventInfoAdapter, position)
        }else {
            likeEventPerAdapterType(pastEventInfoAdapter, position)
        }
    }

    private fun likeEventPerAdapterType (adapter: EventInfoAdapter, position: Int) {
        val event = adapter.currentList.get(position)
        event.isStar = !event.isStar

        CoroutineScope(Dispatchers.IO).launch {
            if(event != null){
                if(event.isStar){
                    eventListViewModel.likeEvent(event.eventId)
                }else {
                    eventListViewModel.unlikeEvent(event.eventId)
                }
            }
        }
    }
}