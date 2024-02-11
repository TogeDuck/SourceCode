package com.idle.togeduck.quest.exchange.view

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.idle.togeduck.R
import com.idle.togeduck.databinding.FragmentQuestExchangeBinding
import com.idle.togeduck.common.ScreenSize.widthDp
import com.idle.togeduck.common.Theme
import com.idle.togeduck.event.EventListViewModel
import com.idle.togeduck.quest.exchange.ExchangeViewModel
import com.idle.togeduck.quest.exchange.model.Exchange
import com.idle.togeduck.quest.exchange.view.exchange_rv.GirdLayoutItemDecoration
import com.idle.togeduck.quest.exchange.view.exchange_rv.IQuestExchangeDetail
import com.idle.togeduck.quest.exchange.view.exchange_rv.QuestExchangeAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@AndroidEntryPoint
class QuestExchangeFragment : Fragment(), IQuestExchangeDetail {
    private var _binding: FragmentQuestExchangeBinding? = null
    private val binding get() = _binding!!
    val exchangeViewModel: ExchangeViewModel by activityViewModels()
    val eventListViewModel: EventListViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentQuestExchangeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        var spanCount = 1

        while (widthDp / (spanCount + 1) >= 110) {
            spanCount++
        }

        val questExchangeRecycleView = binding.questExchangeRecycle
        val questExchangeAdapter = QuestExchangeAdapter(this, requireContext(), spanCount)
        questExchangeRecycleView.adapter = questExchangeAdapter
        questExchangeRecycleView.layoutManager = GridLayoutManager(requireContext(), spanCount, LinearLayoutManager.VERTICAL, false)
        // 간격 설정
        questExchangeRecycleView.addItemDecoration(GirdLayoutItemDecoration(20))
        binding.tvCurrentExchange.setTextColor(ContextCompat.getColor(requireContext(),Theme.theme.main500))

        exchangeViewModel.exchangeList.observe(viewLifecycleOwner) {list ->
            questExchangeAdapter.submitList(list)
        }
        exchangeViewModel.needUpdate.observe(viewLifecycleOwner){check ->
            if(check){
                getExchangeList()
                exchangeViewModel.needUpdate.value = false
            }
        }

        getExchangeList()
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onResume() {
        super.onResume()
        getExchangeList()
    }
    fun getExchangeList(){
        eventListViewModel.selectedEvent.value?.let { selectedEvent ->
            CoroutineScope(Dispatchers.IO).launch {
                exchangeViewModel.getExchangeList(selectedEvent.eventId, 0, 1000)
            }
        }
    }

    override fun myQuestExchangeClicked(questExchange: Exchange) {
        showQuestExchangeDetailDialog(questExchange)
    }

    override fun removeItemFromViewModel(questExchange: Exchange) {
       exchangeViewModel.removeItemFromList(questExchange)
    }

    fun showQuestExchangeDetailDialog(questExchange: Exchange){
        exchangeViewModel.setSelectedExchange(questExchange)
        findNavController().navigate(R.id.action_mapFragment_to_exchangeDialogFragment)
    }
}