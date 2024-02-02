package com.idle.togeduck.quest.exchange.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.idle.togeduck.databinding.FragmentQuestExchangeBinding
import com.idle.togeduck.common.ScreenSize.widthDp
import com.idle.togeduck.quest.exchange.view.exchange_rv.GirdLayoutItemDecoration
import com.idle.togeduck.quest.exchange.view.exchange_rv.IQuestExchangeDetail
import com.idle.togeduck.quest.exchange.view.exchange_rv.QuestExchangeAdapter
import com.idle.togeduck.quest.share.view.share_rv.QuestShareDialog
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
        // Dummy Data
//        questExchangeAdapter.submitList(questExchangeDummyData())
    }

    override fun myQuestExchangeClicked(position: Int) {
//        showQuestExchangeDetailDialog(questExchangeDummyData().get(position))
    }

    fun showQuestExchangeDetailDialog(questExchange: QuestExchange){
        val dialog = QuestShareDialog(QuestShare("title", "content","",100))
        val fragmentManager = childFragmentManager
        dialog.show(fragmentManager, "QuestShareDialog")
    }
}