package com.idle.togeduck.quest.talk.view

import android.content.Context
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.idle.togeduck.R
import com.idle.togeduck.common.RandomCupcake
import com.idle.togeduck.common.Theme
import com.idle.togeduck.databinding.FragmentChatRoomBinding
import com.idle.togeduck.quest.talk.TalkViewModel
import com.idle.togeduck.quest.talk.view.talk_rv.IQuestTalkDetail
import com.idle.togeduck.quest.talk.view.talk_rv.QuestTalkAdapter
import com.idle.togeduck.util.TogeDuckItemDecoration
import com.idle.togeduck.util.getColor
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ChatRoomFragment : Fragment(), IQuestTalkDetail {
    private var _binding: FragmentChatRoomBinding? = null
    private val binding get() = _binding!!

    private val talkViewModel: TalkViewModel by activityViewModels()
    private lateinit var questTalkAdapter: QuestTalkAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        _binding = FragmentChatRoomBinding.inflate(inflater, container, false)
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
        binding.chatroomMainLayout.setBackgroundColor(ContextCompat.getColor(requireContext(), Theme.theme.sub200))
        // Header
        val bottomRound = ContextCompat.getDrawable(requireContext(),R.drawable.shape_bottom_round_15) as GradientDrawable
        bottomRound.setColor(ContextCompat.getColor(requireContext(), Theme.theme.main500))
        binding.chatroomHeadContainer.background = bottomRound
        // Header Icon
        val whiteCircleDrawable = ContextCompat.getDrawable(requireContext(), R.drawable.shape_circle) as GradientDrawable
        whiteCircleDrawable.setColor(getColor(requireContext(), R.color.white))
        whiteCircleDrawable.setStroke(0, 0)
        binding.chatroomMainIcon.background = whiteCircleDrawable
        binding.chatroomMainIcon.setImageDrawable(ContextCompat.getDrawable(requireContext(), RandomCupcake.getImage()))
        // Header Text
        binding.chatroomTitle.text = "helo"
        // Post Icon
        val squareCircle = ContextCompat.getDrawable(requireContext(), R.drawable.shape_square_circle) as GradientDrawable
        squareCircle.setColor(ContextCompat.getColor(requireContext(), Theme.theme.main500))
        binding.chatroomPost.background = squareCircle
    }

    private fun setUpQuestTalkRV() {
        val recycleView = binding.chatroomRecycle
        questTalkAdapter = QuestTalkAdapter(this, requireContext())
        recycleView.adapter = questTalkAdapter
        recycleView.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        recycleView.addItemDecoration(TogeDuckItemDecoration(15, 0))
    }
}