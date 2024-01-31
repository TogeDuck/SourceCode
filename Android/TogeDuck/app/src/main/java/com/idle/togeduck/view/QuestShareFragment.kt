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
        questShareAdapter.submitList(dummyData())
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun dummyData(): List<QuestShare> {
        val share1 = QuestShare("title1", "ddddddd", "", 100)
        val share2 = QuestShare("title2", "conkwjefoisodpisjdlkjlvkjslkdjflskdjflskdjfconkwjefoisodpisjdlkjlvkjslkdjflskdjflskdjflksjdflksjdlfksjdlfkjsdlkfjtent2conkwjefoisodpisjdlkjlvkjslkdjflskdjflskdjflksjdflksjdlfksjdlfkjsdlkfjtent2conkwjefoisodpisjdlkjlvkjslkdjflskdjflskdjflksjdflksjdlfksjdlfkjsdlkfjtent2conkwjefoisodpisjdlkjlvkjslkdjflskdjflskdjflksjdflksjdlfksjdlfkjsdlkfjtent2conkwjefoisodpisjdlkjlvkjslkdjflskdjflskdjflksjdflksjdlfksjdlfkjsdlkfjtent2conkwjefoisodpisjdlkjlvkjslkdjflskdjflskdjflksjdflksjdlfksjdlfkjsdlkfjtent2conkwjefoisodpisjdlkjlvkjslkdjflskdjflskdjflksjdflksjdlfksjdlfkjsdlkfjtent2conkwjefoisodpisjdlkjlvkjslkdjflskdjflskdjflksjdflksjdlfksjdlfkjsdlkfjtent2conkwjefoisodpisjdlkjlvkjslkdjflskdjflskdjflksjdflksjdlfksjdlfkjsdlkfjtent2lksjdflksjdlfksjdlfkjsdlkfjtent2", "", 100)
        val share3 = QuestShare("title3", "ss", "", 100)
        val share4 = QuestShare("title4", "consdfweopfiwpeofiposidfpoisdpofispdofispodfipsodifpsodifpsodifposdifpsodifposdtent3", "", 100)
        val share5 = QuestShare("title5", "consdfweopfiwpeofiposidfpoisdpofispdofispodfipsodifpsodifpsodifposdifpsodifposdtent3", "", 100)
        val share6 = QuestShare("title6", "content6", "", 100)
        val share7 = QuestShare("title7", "consdfweopfiwpeofiposidfpoisdpofispdofispodfipsodifpsodifpsodifposdifpsodifposdtent3", "", 100)
        val share8 = QuestShare("title8", "content8", "", 100)
        val share9 = QuestShare("title1", "conssefsdfsadfasdfasdfasdfasdfasdfasdfasdfasdfasdfsadfasdfasdfastent1", "", 100)
        val share10 = QuestShare("title2", "conkwjefoisodpisjdlkjlvkjslkdjflskdjflskdjflksjdflksjdlfksjdlfkjsdlkfjtent2", "", 100)
        val share11 = QuestShare("title3", "ss", "", 100)
        val share12 = QuestShare("title4", "consdfweopfiwpeofiposidfpoisdpofispdofispodfipsodifpsodifpsodifposdifpsodifposdtent3", "", 100)
        val share13 = QuestShare("title5", "consdfweopfiwpeofiposidfpoisdpofispdofispodfipsodifpsodifpsodifposdifpsodifposdtent3", "", 100)
        val share14 = QuestShare("title6", "content6", "", 100)
        val share15 = QuestShare("title7", "consdfweopfiwpeofiposidfpoisdpofispdofispodfipsodifpsodifpsodifposdifpsodifposdtent3", "", 100)
        val share16 = QuestShare("title8", "content8", "", 100)
        return listOf(share1, share2, share3, share4, share5, share6, share7, share8, share9, share10, share11, share12, share13, share14, share15, share16)
    }

    override fun myQuestShareClicked(position: Int) {
        showDialog(dummyData().get(position))
    }

    fun showDialog(questShare: QuestShare) {
        val dialog = QuestShareDialog(questShare)
        val fragmentManager = childFragmentManager
        dialog.show(fragmentManager, "QuestShareDialog")
    }
}