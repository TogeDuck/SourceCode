package com.idle.togeduck.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.idle.togeduck.databinding.FragmentQuestTalkBinding
import com.idle.togeduck.model.QuestTalk
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
        questShareAdapter.submitList(questTalkDummyData())
    }
    fun questTalkDummyData() : List<QuestTalk> {
        val talk1 = QuestTalk("익명의 오소리","채팅 좋네요")
        val talk2 = QuestTalk("익명의 오리너구리","채팅 좋네요")
        val talk3 = QuestTalk("익명의 사슴","채팅 좋네요")
        val talk4 = QuestTalk("익명의 고양이","채팅 좋네요")
        val talk5 = QuestTalk("익명의 강아지","채팅 좋네요")
        val talk6 = QuestTalk("익명의 소","채팅 좋네요")
        val talk7 = QuestTalk("익명의 최지찬","채팅 좋네요")
        val talk8 = QuestTalk("익명의 푸들","채팅 좋네요")
        val talk9 = QuestTalk("익명의 이지우","채팅 좋네요")
        val talk10 = QuestTalk("익명의 너구리","채팅 좋네요")
        val talk11 = QuestTalk("익명의 오소리","채팅 좋네요")
        val talk12 = QuestTalk("익명의 오리너구리","채팅 좋네요")
        val talk13 = QuestTalk("익명의 사슴","채팅 좋네요")
        val talk14 = QuestTalk("익명의 고양이","채팅 좋네요")
        val talk15 = QuestTalk("익명의 강아지","채팅 좋네요")
        val talk16 = QuestTalk("익명의 소","채팅 좋네요")
        val talk17 = QuestTalk("익명의 최지찬","채팅 좋네요")
        val talk18 = QuestTalk("익명의 푸들","채팅 좋네요")
        val talk19 = QuestTalk("익명의 이지우","채팅 좋네요")
        val talk20 = QuestTalk("익명의 너구리","채팅 좋네요")
        return listOf(talk1,
            talk2,
            talk3,
            talk4,
            talk5,
            talk6,
            talk7,
            talk8,
            talk9,
            talk10,
            talk11,
            talk12,
            talk13,
            talk14,
            talk15,
            talk16,
            talk17,
            talk18,
            talk19,
            talk20)
    }

}