package com.idle.togeduck.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.idle.togeduck.databinding.FragmentQuestShareBinding
import com.idle.togeduck.model.QuestShare
import com.idle.togeduck.util.TogeDuckItemDecoration
import com.idle.togeduck.view.quest_share.IQuestShareDetail
import com.idle.togeduck.view.quest_share.QuestShareDialog
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

        // 간격 설정
        recycleView.addItemDecoration(TogeDuckItemDecoration(15,0))

        // Dummy Data
//        questShareAdapter.submitList(dummyData())
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun myQuestShareClicked(position: Int) {
//        showDialog(dummyData().get(position))
    }

    fun showDialog(questShare: QuestShare) {
        val dialog = QuestShareDialog(questShare)
        val fragmentManager = childFragmentManager
        dialog.show(fragmentManager, "QuestShareDialog")
    }
}