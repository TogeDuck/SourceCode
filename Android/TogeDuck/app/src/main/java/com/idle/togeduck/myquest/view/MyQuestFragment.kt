package com.idle.togeduck.myquest.view

import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.idle.togeduck.R
import com.idle.togeduck.common.Theme
import com.idle.togeduck.databinding.FragmentMyQuestBinding
import com.idle.togeduck.event.EventListViewModel
import com.idle.togeduck.event.view.list.list_rv.EventInfo
import com.idle.togeduck.event.view.list.list_rv.EventInfoAdapter
import com.idle.togeduck.favorite.FavoriteSettingViewModel
import com.idle.togeduck.main_map.MapViewModel
import com.idle.togeduck.main_map.view.MapFragment
import com.idle.togeduck.myquest.MyQuestViewModel
import com.idle.togeduck.myquest.model.MyQuest
import com.idle.togeduck.myquest.view.myquest_rv.IMyQuestDetail
import com.idle.togeduck.myquest.view.myquest_rv.MyQuestAdapter
import com.idle.togeduck.util.TogeDuckItemDecoration
import com.idle.togeduck.util.getColor
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MyQuestFragment : Fragment(), EventInfo {
    private var _binding : FragmentMyQuestBinding? = null
    private val binding get() = _binding!!

    private val eventListViewModel: EventListViewModel by activityViewModels()
    private val mapViewModel: MapViewModel by activityViewModels()

    private lateinit var todayEventInfoAdapter: EventInfoAdapter
    private lateinit var upcomingEventInfoAdapter: EventInfoAdapter
    private lateinit var pastEventInfoAdapter: EventInfoAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMyQuestBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setTheme()
        setRecyclerview()
//        getLikeList()
        eventListViewModel.likeListToday.observe(viewLifecycleOwner){ list ->
            todayEventInfoAdapter.submitList(list)

            if (list.isEmpty()) {
                binding.tvTodayEmptyEvent.visibility = View.VISIBLE
            } else {
                binding.tvTodayEmptyEvent.visibility = View.GONE
            }
        }

        eventListViewModel.likeListUpcoming.observe(viewLifecycleOwner){ list ->
            upcomingEventInfoAdapter.submitList(list)

            if (list.isEmpty()) {
                binding.tvUpcomingEmptyEvent.visibility = View.VISIBLE
            } else {
                binding.tvUpcomingEmptyEvent.visibility = View.GONE
            }
        }

        eventListViewModel.likeListPast.observe(viewLifecycleOwner){list ->
            pastEventInfoAdapter.submitList(list)

            if (list.isEmpty()) {
                binding.tvPastEmptyEvent.visibility = View.VISIBLE
            } else {
                binding.tvPastEmptyEvent.visibility = View.GONE
            }
        }
    }

    override fun onResume() {
        super.onResume()
        getLikeList()
    }
    private fun getLikeList(){
        CoroutineScope(Dispatchers.IO).launch{
            eventListViewModel.getLikesList()
        }
    }
    private fun setTheme() {
        binding.today.setTextColor(ContextCompat.getColor(requireContext(), Theme.theme.main500))
        binding.upcoming.setTextColor(ContextCompat.getColor(requireContext(), Theme.theme.main300))
        binding.past.setTextColor(ContextCompat.getColor(requireContext(), R.color.gray_bg))
        binding.tvTitle.setTextColor(ContextCompat.getColor(requireContext(),Theme.theme.main500))

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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun eventClicked(position: Int, type: Int) {
        if(type == 0) {
            eventListViewModel.setSelectedEvent(eventListViewModel.likeListToday.value!![position])
            (parentFragment as MapFragment).changeViewPagerPage(2)
            mapViewModel.setBottomSheet(2)
        }else if(type == 1){
            eventListViewModel.setSelectedEvent(eventListViewModel.likeListUpcoming.value!![position])
            (parentFragment as MapFragment).changeViewPagerPage(2)
            mapViewModel.setBottomSheet(2)
        }else {
            eventListViewModel.setSelectedEvent(eventListViewModel.likeListPast.value!![position])
            (parentFragment as MapFragment).changeViewPagerPage(2)
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