package com.idle.togeduck.quest.talk.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.idle.togeduck.common.Theme
import com.idle.togeduck.databinding.FragmentQuestTalkBinding
import com.idle.togeduck.quest.talk.TalkViewModel
import com.idle.togeduck.util.TogeDuckItemDecoration
import com.idle.togeduck.quest.talk.view.talk_rv.IQuestTalkDetail
import com.idle.togeduck.quest.talk.view.talk_rv.QuestTalkAdapter
import com.idle.togeduck.util.getColor
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class QuestTalkFragment : Fragment(), IQuestTalkDetail {
    private var _binding: FragmentQuestTalkBinding? = null
    private val binding get() = _binding!!

    private val talkViewModel: TalkViewModel by activityViewModels()
    private lateinit var questTalkAdapter: QuestTalkAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        _binding = FragmentQuestTalkBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpQuestTalkRV()
        setTheme()

        talkViewModel.talkList.observe(viewLifecycleOwner) { talkList ->
            questTalkAdapter.submitList(talkList)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun myQuestItemClicked(position: Int) {
    }

    private fun setTheme() {
        binding.questTalkMainTitle.setTextColor(getColor(requireContext(), Theme.theme.main500))
    }

    private fun setUpQuestTalkRV() {
        val recycleView = binding.questTalkRecycle
        questTalkAdapter = QuestTalkAdapter(this, requireContext())
        recycleView.adapter = questTalkAdapter
        recycleView.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        recycleView.addItemDecoration(TogeDuckItemDecoration(15, 0))
    }
}