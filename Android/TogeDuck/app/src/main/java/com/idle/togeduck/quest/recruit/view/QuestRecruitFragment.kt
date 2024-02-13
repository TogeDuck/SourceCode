package com.idle.togeduck.quest.recruit.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
            //TODO 내가 만든 모집인지 확인 후 채팅방 아이디 보관
//            for(recruit in list){
//                if(recruit.){
//                }
//            }
            questRecruitAdapter.submitList(list)
        }
        recruitViewModel.needUpdate.observe(viewLifecycleOwner){check ->
            if(check){
                getRecruitList()
                recruitViewModel.needUpdate.value = false
            }
        }
        getRecruitList()
    }

    override fun onResume() {
        super.onResume()
        getRecruitList()
    }
    private fun getRecruitList(){
        CoroutineScope(Dispatchers.IO).launch{
            if(eventListViewModel.selectedEvent.value != null){
                recruitViewModel.getRecruitList(eventListViewModel.selectedEvent.value!!.eventId, 0, 1000)
            }
        }
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

    override fun joinBtnClicked(questRecruit: Recruit) {
        talkViewModel.addTalkRoom(TalkRoom(questRecruit.chatId, questRecruit.title, mutableMapOf()))
        talkViewModel.currentChatRoomId.value = questRecruit.chatId
        findNavController().navigate(R.id.action_mapFragment_to_chatRoomFragment)
    }

    override fun removeItem(questRecruit: Recruit) {
        recruitViewModel.removeItem(questRecruit)
    }
}