package com.idle.togeduck.quest.recruit.view

import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.idle.togeduck.R
import com.idle.togeduck.databinding.FragmentQuestRecruitBinding
import com.idle.togeduck.common.Theme
import com.idle.togeduck.event.EventListViewModel
import com.idle.togeduck.quest.recruit.RecruitViewModel
import com.idle.togeduck.quest.recruit.model.Recruit
import com.idle.togeduck.util.TogeDuckItemDecoration
import com.idle.togeduck.util.getColor
import com.idle.togeduck.quest.recruit.view.recruit_rv.IQuestRecruit
import com.idle.togeduck.quest.recruit.view.recruit_rv.QuestRecruitAdapter
import com.idle.togeduck.quest.talk.TalkViewModel
import com.idle.togeduck.quest.talk.model.TalkRoom
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@AndroidEntryPoint
class QuestRecruitFragment : Fragment(), IQuestRecruit {
    private var _binding: FragmentQuestRecruitBinding? = null
    private val binding get() = _binding!!

    private val recruitViewModel: RecruitViewModel by activityViewModels()
    private val talkViewModel: TalkViewModel by activityViewModels()
    private val eventListViewModel: EventListViewModel by activityViewModels()

    private lateinit var questRecruitAdapter: QuestRecruitAdapter


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

        recruitViewModel.recruitList.observe(viewLifecycleOwner){list ->
            questRecruitAdapter.submitList(list)
            setEmptyTheme()
        }
        recruitViewModel.needUpdate.observe(viewLifecycleOwner){check ->
            if(check){
                getRecruitList()
                recruitViewModel.needUpdate.value = false
            }
        }
        getRecruitList()
        setEmptyTheme()
    }

    override fun onResume() {
        super.onResume()
        getRecruitList()
    }
    private fun getRecruitList(){
        eventListViewModel.selectedEvent.value?.let {selectedEvent ->
            CoroutineScope(Dispatchers.IO).launch{
                recruitViewModel.getRecruitList(selectedEvent.eventId, 0, 1000)
            }
        }
    }

    private fun setEmptyTheme(){
        val drawable = ContextCompat.getDrawable(requireContext(), R.drawable.shape_square_circle) as GradientDrawable
        drawable.setColor(ContextCompat.getColor(requireContext(), R.color.white))
        drawable.setStroke(1, ContextCompat.getColor(requireContext(), R.color.gray_bg))
        binding.questShareEmpty.background = drawable
        binding.questShareEmpty.setTextColor(getColor(requireContext(), R.color.gray_text))

        val pastEmptyEventDrawable = ContextCompat.getDrawable(requireContext(), R.drawable.shape_all_round_20) as GradientDrawable
        pastEmptyEventDrawable.setColor(getColor(requireContext(), R.color.gray_bg))
        pastEmptyEventDrawable.setStroke(0, getColor(requireContext(), R.color.gray_bg))
        binding.tvTodayEmptyEvent.background = pastEmptyEventDrawable
        binding.tvTodayEmptyEvent.setTextColor(getColor(requireContext(), R.color.gray_text))

        if(recruitViewModel.recruitList.value == null ||
            recruitViewModel.recruitList.value!!.isEmpty()){
            binding.questShareEmpty.visibility = View.VISIBLE
            binding.tvTodayEmptyEvent.visibility = View.VISIBLE
        }
        else{
            binding.questShareEmpty.visibility = View.GONE
            binding.tvTodayEmptyEvent.visibility = View.GONE
        }
    }

    private fun setRecyclerview() {
        questRecruitAdapter = QuestRecruitAdapter(this, requireContext())

        binding.rvCurrentRecruit.apply {
            addItemDecoration(TogeDuckItemDecoration(10, 0))
            adapter = questRecruitAdapter
            layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        }
    }

    private fun setTheme() {
        binding.tvCurrentRecruit.setTextColor(getColor(requireContext(), Theme.theme.main500))
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun joinBtnClicked(questRecruit: Recruit) {
        talkViewModel.addTalkRoom(TalkRoom(questRecruit.chatId, questRecruit.title, mutableMapOf()))
        talkViewModel.currentChatRoomId.value = questRecruit.chatId
        findNavController().navigate(R.id.action_mapFragment_to_chatRoomFragment)
    }

    override fun removeItem(questRecruit: Recruit) {
        recruitViewModel.removeItem(questRecruit)
    }
}