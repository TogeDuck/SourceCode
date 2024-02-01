package com.idle.togeduck.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.idle.togeduck.databinding.FragmentQuestTalkBinding
import com.idle.togeduck.util.TogeDuckItemDecoration
import com.idle.togeduck.view.quest_talk.IQuestTalkDetail
import com.idle.togeduck.view.quest_talk.QuestTalkAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class QuestTalkFragment : Fragment(), IQuestTalkDetail {
    private var _binding: FragmentQuestTalkBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentQuestTalkBinding.inflate(inflater, container, false)
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpQuestTalkRV()
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
    override fun myQuestItemClicked(position: Int) {
        TODO("Not yet implemented")
    }
    fun setUpQuestTalkRV(){
        val recycleView = binding.questTalkRecycle
        val questShareAdapter = QuestTalkAdapter(this, requireContext())
        recycleView.adapter = questShareAdapter
        recycleView.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, true)
        recycleView.addItemDecoration(TogeDuckItemDecoration(15,0))
//        questShareAdapter.submitList(questTalkDummyData())
    }
}