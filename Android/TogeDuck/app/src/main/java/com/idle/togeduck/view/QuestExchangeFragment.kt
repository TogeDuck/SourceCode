package com.idle.togeduck.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.idle.togeduck.databinding.FragmentQuestExchangeBinding
import com.idle.togeduck.model.QuestExchange
import com.idle.togeduck.model.QuestShare
import com.idle.togeduck.util.TogeDuckItemDecoration
import com.idle.togeduck.view.quest_exchange.IQuestExchangeDetail
import com.idle.togeduck.view.quest_exchange.QuestExchangeAdapter
import com.idle.togeduck.view.quest_share.QuestShareDialog
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class QuestExchangeFragment : Fragment(), IQuestExchangeDetail {
    private var _binding: FragmentQuestExchangeBinding? = null
    private val binding get() = _binding!!
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
        questExchangeRecycleView()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun questExchangeRecycleView(){
        val questExchangeRecycleView = binding.questExchangeRecycle
        val questExchangeAdapter = QuestExchangeAdapter(this, requireContext())
        questExchangeRecycleView.adapter = questExchangeAdapter
        questExchangeRecycleView.layoutManager = GridLayoutManager(requireContext(), 3, LinearLayoutManager.VERTICAL, true)
        // 간격 설정
        questExchangeRecycleView.addItemDecoration(TogeDuckItemDecoration(5,15))
        // Dummy Data
        questExchangeAdapter.submitList(questExchangeDummyData())
    }

    private fun questExchangeDummyData(): List<QuestExchange> {
        val exchange1 = QuestExchange("title","",100)
        val exchange2 = QuestExchange("title","",100)
        val exchange3 = QuestExchange("title","",100)
        val exchange4 = QuestExchange("title","",100)
        val exchange5 = QuestExchange("title","",100)
        val exchange6 = QuestExchange("title","",100)
        val exchange7 = QuestExchange("title","",100)
        val exchange8 = QuestExchange("title","",100)
        val exchange9 = QuestExchange("title","",100)
        val exchange10 = QuestExchange("title","",100)
        val exchange11 = QuestExchange("title","",100)
        val exchange12 = QuestExchange("title","",100)
        val exchange13 = QuestExchange("title","",100)
        val exchange14 = QuestExchange("title","",100)
        val exchange15 = QuestExchange("title","",100)
        val exchange16 = QuestExchange("title","",100)

        return listOf(exchange1,
            exchange2,
            exchange3,
            exchange4,
            exchange5,
            exchange6,
            exchange7,
            exchange8,
            exchange9,
            exchange10,
            exchange11,
            exchange12,
            exchange13,
            exchange14,
            exchange15,
            exchange16)
    }

    override fun myQuestExchangeClicked(position: Int) {
        showQuestExchangeDetailDialog(questExchangeDummyData().get(position))
    }

    fun showQuestExchangeDetailDialog(questExchange: QuestExchange){
        val dialog = QuestShareDialog(QuestShare("title", "content","",100))
        val fragmentManager = childFragmentManager
        dialog.show(fragmentManager, "QuestShareDialog")
    }
}