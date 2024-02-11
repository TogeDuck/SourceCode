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
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
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
        }

        eventListViewModel.listUpcoming.observe(viewLifecycleOwner){ list ->
            upcomingEventInfoAdapter.submitList(list)
        }

        eventListViewModel.listPast.observe(viewLifecycleOwner){list ->
            pastEventInfoAdapter.submitList(list)
        }

        eventListViewModel.selectedEvent.observe(viewLifecycleOwner){event ->
            Log.d("이벤트 리스트", "Selected event updated: $event")
            if(event != null){
                toDetailPage()
            }
        }

        //todo.즐겨찾기 리스트 호출은 main화면에서 하는 걸로 변경 -> 새로고침은 여기서
//        CoroutineScope(Dispatchers.IO).launch {
//            eventListViewModel.getLikesList()
//
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

    @RequiresApi(Build.VERSION_CODES.O)
    private fun setTheme() {
        binding.today.setTextColor(ContextCompat.getColor(requireContext(), Theme.theme.main500))
        binding.upcoming.setTextColor(ContextCompat.getColor(requireContext(), Theme.theme.main300))
        binding.past.setTextColor(ContextCompat.getColor(requireContext(), R.color.gray_bg))
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
        (parentFragment as MapFragment).changeViewPagerPage(2)
        mapViewModel.setBottomSheet(2)
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
        CoroutineScope(Dispatchers.IO).launch {
            val event = adapter.currentList.get(position)

            if(event != null){
                if(event.isStar){
                    val likeEventRequest = LikeEventRequest(1)
                    eventListViewModel.likeEvent(likeEventRequest)
                    Log.d("log", "eventlistfragment - 즐겨찾기 추가 ")
                }else{
                    eventListViewModel.unlikeEvent(1)
                    Log.d("log", "eventlistfragment - 즐겨찾기 삭제")
                }
            }
        }
    }

}