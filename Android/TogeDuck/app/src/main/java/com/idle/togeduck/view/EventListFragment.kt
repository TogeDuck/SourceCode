package com.idle.togeduck.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager

import com.idle.togeduck.databinding.FragmentEventListBinding
import com.idle.togeduck.model.Event
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

        val recyclerView = binding.rvEventToday
        val eventInfoAdapter = EventInfoAdapter(this, requireContext())
        recyclerView.adapter = eventInfoAdapter
        recyclerView.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, true)

        //더미데이터
        eventInfoAdapter.submitList(tempData())
    }

    private fun tempData(): List<Event> {
        val data1 = Event("imageUrl1", "cafe1", "event1", "2024.01.02 ~ 2024.01.06", true)
        val data2 = Event("imageUrl2", "cafe2", "event1", "2024.01.02 ~ 2024.01.06", true)
        val data3 = Event("imageUrl3", "cafe3", "event1", "2024.01.02 ~ 2024.01.06", true)
        val data4 = Event("imageUrl4", "cafe4", "event1", "2024.01.02 ~ 2024.01.06", true)
        val data5 = Event("imageUrl5", "cafe5", "event1", "2024.01.02 ~ 2024.01.06", true)
        val data6 = Event("imageUrl6", "cafe6", "event1", "2024.01.02 ~ 2024.01.06", true)
        return listOf(data1,data2,data3,data4,data5,data6)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}

