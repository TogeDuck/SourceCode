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
import com.idle.togeduck.R

import com.idle.togeduck.databinding.FragmentEventListBinding
import com.idle.togeduck.common.Theme
import com.idle.togeduck.event.view.list.list_rv.EventInfo
import com.idle.togeduck.event.view.list.list_rv.EventInfoAdapter
import com.idle.togeduck.event.EventListViewModel
import com.idle.togeduck.event.model.LikeEventRequest
import com.idle.togeduck.event.view.detail.EventDetailFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.datetime.LocalDate

@AndroidEntryPoint
class EventListFragment : Fragment(), EventInfo {
    private var _binding: FragmentEventListBinding? = null
    private val binding get() = _binding!!

    private val eventListViewModel: EventListViewModel by activityViewModels()

    private lateinit var todayEventInfoAdapter: EventInfoAdapter
    private lateinit var upcomingEventInfoAdapter: EventInfoAdapter
    private lateinit var pastEventInfoAdapter: EventInfoAdapter

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

        eventListViewModel.listToday.observe(viewLifecycleOwner){ list ->
             todayEventInfoAdapter.submitList(list)
        }

        eventListViewModel.listUpcoming.observe(viewLifecycleOwner){ list ->
            upcomingEventInfoAdapter.submitList(list)
        }

        eventListViewModel.listPast.observe(viewLifecycleOwner){list ->
            pastEventInfoAdapter.submitList(list)
        }



        //즐겨찾기 리스트
//        CoroutineScope(Dispatchers.IO).launch {
//            eventListViewModel.getLikesList()
//        }

        //즐겨찾기
//        CoroutineScope(Dispatchers.IO).launch {
//            val likeEventRequest = LikeEventRequest(1)
//            eventListViewModel.likeEvent(likeEventRequest)
//        }
//
//        //즐겨찾기 삭제
//        CoroutineScope(Dispatchers.IO).launch {
//            eventListViewModel.unlikeEvent(1)
//        }

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
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun eventClicked(position: Int) {
        TODO("Not yet implemented")
//        val clickedEvent = todayEventInfoAdapter.getItemId((position))
//
//        val intent = Intent(requireContext(), EventDetailFragment::class.java)
//        intent.putExtra("event_id", clickedEvent.eventId)
//        startActivity(intent)


    }

    override fun likeClick(position: Int) {
        TODO("Not yet implemented")
    }
}