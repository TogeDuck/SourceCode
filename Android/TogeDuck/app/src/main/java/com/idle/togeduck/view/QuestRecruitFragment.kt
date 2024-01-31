package com.idle.togeduck.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.idle.togeduck.databinding.FragmentQuestRecruitBinding
import com.idle.togeduck.model.QuestShare
import com.idle.togeduck.util.Theme
import com.idle.togeduck.util.TogeDuckItemDecoration
import com.idle.togeduck.util.getColor
import com.idle.togeduck.view.quest_recruit.IQuestRecruit
import com.idle.togeduck.view.quest_recruit.QuestRecruitAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class QuestRecruitFragment : Fragment(), IQuestRecruit {
    private var _binding: FragmentQuestRecruitBinding? = null
    private val binding get() = _binding!!

    private lateinit var questRecruitAdapter: QuestRecruitAdapter

    private val tempList = listOf(
        QuestShare("OO카페 가실 분~", "02/05", "", 0),
        QuestShare("XX카페 가실 분~", "03/06", "", 0),
        QuestShare("ㅁㅁ카페 가실 분~", "05/07", "", 0),
        QuestShare("OO카페 가실 분~", "02/05", "", 0),
        QuestShare("XX카페 가실 분~", "03/06", "", 0),
        QuestShare("ㅁㅁ카페 가실 분~", "05/07", "", 0),
        QuestShare("OO카페 가실 분~", "02/05", "", 0),
        QuestShare("XX카페 가실 분~", "03/06", "", 0),
        QuestShare("ㅁㅁ카페 가실 분~", "05/07", "", 0),
        QuestShare("OO카페 가실 분~", "02/05", "", 0),
        QuestShare("XX카페 가실 분~", "03/06", "", 0),
        QuestShare("ㅁㅁ카페 가실 분~", "05/07", "", 0),
    )

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentQuestRecruitBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setRecyclerview()
        setTheme()

        questRecruitAdapter.submitList(tempList)
    }

    private fun setRecyclerview() {
        questRecruitAdapter = QuestRecruitAdapter(this, requireContext())

        binding.rvCurrentRecruit.apply {
            addItemDecoration(TogeDuckItemDecoration(10, 0))
            adapter = questRecruitAdapter
            layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, true)
                    .apply { stackFromEnd = true }
        }
    }

    private fun setTheme() {
        binding.tvCurrentRecruit.setTextColor(getColor(requireContext(), Theme.theme.main500))
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun joinBtnClicked(position: Int) {
    }
}