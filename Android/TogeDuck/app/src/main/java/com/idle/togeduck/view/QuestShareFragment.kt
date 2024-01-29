package com.idle.togeduck.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.idle.togeduck.R
import com.idle.togeduck.databinding.ComponentBottomAppbarBinding
import com.idle.togeduck.databinding.FragmentBottomAppbarBinding
import com.idle.togeduck.databinding.FragmentQuestShareBinding
import com.idle.togeduck.model.QuestShare
import com.idle.togeduck.view.quest_share.IQuestShareDetail
import com.idle.togeduck.view.quest_share.QuestShareListAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class QuestShareFragment : Fragment(), IQuestShareDetail {
    private var _binding: FragmentQuestShareBinding? = null
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentQuestShareBinding.inflate(inflater, container, false)
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Adapter 인스턴스 생성 (OnClick 인터페이스 구현체, Context 제공 필요)
        val recycleView = binding.questShareRecycle
        val questShareAdapter = QuestShareListAdapter(this, requireContext())
        recycleView.adapter = questShareAdapter
        recycleView.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, true)

        // Dummy Data
        questShareAdapter.submitList(dummyData())
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun dummyData(): List<QuestShare> {
        val share1 = QuestShare("title1", "content1", "", 100)
        val share2 = QuestShare("title2", "content2", "", 100)
        val share3 = QuestShare("title3", "content3", "", 100)
        val share4 = QuestShare("title4", "content4", "", 100)
        val share5 = QuestShare("title5", "content5", "", 100)
        val share6 = QuestShare("title6", "content6", "", 100)
        val share7 = QuestShare("title7", "content7", "", 100)
        val share8 = QuestShare("title8", "content8", "", 100)
        return listOf(share1, share2, share3, share4, share5, share6, share7, share8)
    }
}