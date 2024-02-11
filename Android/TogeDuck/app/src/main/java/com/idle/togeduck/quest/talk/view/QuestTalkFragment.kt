package com.idle.togeduck.quest.talk.view

import android.graphics.drawable.GradientDrawable
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.idle.togeduck.R
import com.idle.togeduck.common.Theme
import com.idle.togeduck.databinding.ComponentQuestTalkInputBinding
import com.idle.togeduck.databinding.FragmentQuestTalkBinding
import com.idle.togeduck.event.EventListViewModel
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

    private var _componentQuestTalkInputBinding: ComponentQuestTalkInputBinding? = null
    private val componentQuestTalkInputBinding get() = _componentQuestTalkInputBinding!!

    private val talkViewModel: TalkViewModel by activityViewModels()
    private val eventListViewModel: EventListViewModel by activityViewModels()
    private lateinit var questTalkAdapter: QuestTalkAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentQuestTalkBinding.inflate(inflater, container, false)
        _componentQuestTalkInputBinding = binding.compTalkInput
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpQuestTalkRV()
        setTheme()

        talkViewModel.talkList.observe(viewLifecycleOwner) { talkList ->
            questTalkAdapter.submitList(talkList)
        }
        eventListViewModel.selectedEvent.observe(viewLifecycleOwner) {event ->
            talkViewModel.clearTalkList()
        }

        componentQuestTalkInputBinding.talkSend.setOnClickListener {
            // TODO. 여기에 전송 기능 추가하면 됨!!
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

        val inputDrawable = ContextCompat.getDrawable(requireContext(), R.drawable.shape_square_circle) as GradientDrawable
        inputDrawable.setColor(getColor(requireContext(), R.color.white))
        inputDrawable.setStroke(4, getColor(requireContext(), Theme.theme.sub500))
        componentQuestTalkInputBinding.etTalkInputBackground.background = inputDrawable

        val sendDrawable = ContextCompat.getDrawable(requireContext(), R.drawable.shape_all_round_20) as GradientDrawable
        sendDrawable.setColor(getColor(requireContext(), Theme.theme.sub500))
        componentQuestTalkInputBinding.talkSend.background = sendDrawable

        val cursorDrawable =
            ContextCompat.getDrawable(requireContext(), R.drawable.shape_cursor) as GradientDrawable
        cursorDrawable.setColor(getColor(requireContext(), Theme.theme.main500))
        @RequiresApi(Build.VERSION_CODES.Q)
        componentQuestTalkInputBinding.etTalkInput.textCursorDrawable = cursorDrawable
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