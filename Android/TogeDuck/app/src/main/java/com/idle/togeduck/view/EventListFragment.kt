package com.idle.togeduck.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat

import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager

import com.idle.togeduck.databinding.FragmentEventListBinding
import com.idle.togeduck.model.Event
import com.idle.togeduck.util.Theme
import com.idle.togeduck.view.event_list.EventInfo
import com.idle.togeduck.view.event_list.EventInfoAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class EventListFragment : Fragment(), EventInfo {
    private var _binding: FragmentEventListBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentEventListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setTheme()
        setRecyclerview()
    }

    private fun setRecyclerview() {
        val rvToday = binding.rvEventToday
        val rvUpcoming = binding.rvEventUpcoming
        val rvPast = binding.rvEventPast
        val AdapterToday = EventInfoAdapter(this, requireContext())
        val AdapterUpcoming = EventInfoAdapter(this, requireContext())
        val AdapterPast = EventInfoAdapter(this, requireContext())

        rvToday.adapter = AdapterToday
        rvToday.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)

        rvUpcoming.adapter = AdapterUpcoming
        rvUpcoming.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)

        rvPast.adapter = AdapterPast
        rvPast.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        AdapterUpcoming.submitList(tempDataUpcoming())
        AdapterToday.submitList(tempDataToday())
        AdapterUpcoming.submitList(tempDataPast())
    }

    private fun setTheme() {
        binding.today.setTextColor(ContextCompat.getColor(requireContext(), Theme.theme.main500))
        binding.upcoming.setTextColor(ContextCompat.getColor(requireContext(), Theme.theme.main300))
    }

    private fun tempDataToday(): List<Event> {
        val data1 = Event("imageUrl1", "cafe1", "event1", "2024.01.02 ~ 2024.01.06", true)
        val data2 = Event("imageUrl2", "cafe2", "event1", "2024.01.02 ~ 2024.01.06", true)
        val data3 = Event("imageUrl3", "cafe3", "event1", "2024.01.02 ~ 2024.01.06", true)
        val data4 = Event("imageUrl4", "cafe4", "event1", "2024.01.02 ~ 2024.01.06", true)
        val data5 = Event("imageUrl5", "cafe5", "event1", "2024.01.02 ~ 2024.01.06", true)
        val data6 = Event("imageUrl6", "cafe6", "event1", "2024.01.02 ~ 2024.01.06", true)
        return listOf(data1,data2,data3,data4,data5,data6)
    }

    private fun tempDataUpcoming(): List<Event> {
        val data7 = Event("imageUrl7", "cafe7", "event1", "2024.01.02 ~ 2024.01.06", true)
        val data8 = Event("imageUrl8", "cafe8", "event1", "2024.01.02 ~ 2024.01.06", true)
        val data9 = Event("imageUrl9", "cafe9", "event1", "2024.01.02 ~ 2024.01.06", true)
        val data10 = Event("imageUrl10", "cafe10", "event1", "2024.01.02 ~ 2024.01.06", true)
        return listOf(data7,data8,data9,data10)
    }

    private fun tempDataPast(): List<Event> {
        val data11 = Event("imageUrl11", "cafe11", "event1", "2024.01.02 ~ 2024.01.06", true)
        val data12 = Event("imageUrl2", "cafe12", "event1", "2024.01.02 ~ 2024.01.06", true)
        val data13 = Event("imageUrl3", "cafe13", "event1", "2024.01.02 ~ 2024.01.06", true)
        return listOf(data11,data12,data13)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}

