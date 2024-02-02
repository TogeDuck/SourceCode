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

//    private val eventListViewModel: EventListViewModel by activityViewModels()

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
}