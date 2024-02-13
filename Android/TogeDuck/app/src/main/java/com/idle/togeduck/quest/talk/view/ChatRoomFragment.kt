package com.idle.togeduck.quest.talk.view

import android.content.Context
import android.graphics.drawable.GradientDrawable
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowInsets
import android.view.WindowManager
import androidx.annotation.RequiresApi
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.MediatorLiveData
import androidx.recyclerview.widget.LinearLayoutManager
import com.idle.togeduck.MainViewModel
import com.idle.togeduck.R
import com.idle.togeduck.common.RandomCupcake
import com.idle.togeduck.common.Theme
import com.idle.togeduck.databinding.ComponentQuestTalkInputBinding
import com.idle.togeduck.databinding.FragmentChatRoomBinding
import com.idle.togeduck.favorite.FavoriteSettingViewModel
import com.idle.togeduck.network.StompManager
import com.idle.togeduck.quest.talk.TalkViewModel
import com.idle.togeduck.quest.talk.model.Talk
import com.idle.togeduck.quest.talk.view.talk_rv.IQuestTalkDetail
import com.idle.togeduck.quest.talk.view.talk_rv.QuestTalkAdapter
import com.idle.togeduck.util.CalcStatusBarSize
import com.idle.togeduck.util.DpPxUtil
import com.idle.togeduck.util.TogeDuckItemDecoration
import com.idle.togeduck.util.getColor
import com.idle.togeduck.util.toAlpha
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class ChatRoomFragment : Fragment(), IQuestTalkDetail {
    private var _binding: FragmentChatRoomBinding? = null
    private val binding get() = _binding!!

    private var _componentChatInputBinding: ComponentQuestTalkInputBinding? = null
    private val componentChatInputBinding get() = _componentChatInputBinding!!

    @Inject
    lateinit var stompManager: StompManager

    private val talkViewModel: TalkViewModel by activityViewModels()
    private val mainViewModel: MainViewModel by activityViewModels()
    private val favoriteSettingViewModel: FavoriteSettingViewModel by activityViewModels()
    private lateinit var questTalkAdapter: QuestTalkAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentChatRoomBinding.inflate(inflater, container, false)
        _componentChatInputBinding = binding.compChatInput
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpQuestTalkRV()
        setTheme()
        setLayout()

        componentChatInputBinding.talkSend.setOnClickListener {
            sendChat()
        }

        val filteredTalkList = MediatorLiveData<List<Talk>>()
        filteredTalkList.addSource(talkViewModel.currentChatRoomId) { chatRoomId ->
            talkViewModel.chatRoomTalkList.value?.let { chatRoomTalkMap ->
                filteredTalkList.value = chatRoomTalkMap[chatRoomId] ?: emptyList()
            }
        }
        filteredTalkList.addSource(talkViewModel.chatRoomTalkList) { chatRoomTalkMap ->
            talkViewModel.currentChatRoomId.value?.let { chatRoomId ->
                filteredTalkList.value = chatRoomTalkMap[chatRoomId] ?: emptyList()
            }
        }
        filteredTalkList.observe(viewLifecycleOwner) { talkList ->
            Log.d("채팅 리스트 observer 업데이트", "업데이트")
            questTalkAdapter.submitList(talkList ?: emptyList())

            if (talkList.isNotEmpty()) {
                binding.chatroomRecycle.scrollToPosition(talkList.lastIndex)
            }
        }
    }

    fun sendChat() {
        val message = componentChatInputBinding.etTalkInput.text.toString()
        if (message.isNotEmpty()
            && talkViewModel.currentChatRoomId != null
            && mainViewModel.guid != null
            && favoriteSettingViewModel.selectedCelebrity.value != null
        ) {
            stompManager.sendChat(
                talkViewModel.currentChatRoomId.value!!,
                mainViewModel.guid!!,
                message,
                favoriteSettingViewModel.selectedCelebrity.value!!.id
            )
            componentChatInputBinding.etTalkInput.text?.clear()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun myQuestItemClicked(position: Int) {
    }

    private fun setLayout() {
        val statusDp = CalcStatusBarSize.getStatusBarHeightToDp(requireContext())

        binding.chatroomHeadContainer.setPadding(
            DpPxUtil.dpToPx(15, requireContext()),
            DpPxUtil.dpToPx(statusDp + 15, requireContext()),
            DpPxUtil.dpToPx(15, requireContext()),
            DpPxUtil.dpToPx(15, requireContext())
        )

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            binding.root.setOnApplyWindowInsetsListener { _, windowInsets ->
                val imeHeight = windowInsets.getInsets(WindowInsets.Type.ime()).bottom
                binding.root.setPadding(0, 0, 0, imeHeight)
                windowInsets
            }
        } else {
            requireActivity().window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)
        }
    }

    private fun setTheme() {
        binding.chatroomMainLayout.setBackgroundColor(
            ContextCompat.getColor(
                requireContext(),
                Theme.theme.sub200
            )
        )
        // Header
        val bottomRound = ContextCompat.getDrawable(
            requireContext(),
            R.drawable.shape_bottom_round_25
        ) as GradientDrawable
        bottomRound.setColor(ContextCompat.getColor(requireContext(), Theme.theme.main500))
        bottomRound.alpha = 0.6.toAlpha()
        binding.chatroomHeadContainer.background = bottomRound
        // Header Icon
        val whiteCircleDrawable =
            ContextCompat.getDrawable(requireContext(), R.drawable.shape_circle) as GradientDrawable
        whiteCircleDrawable.setColor(getColor(requireContext(), R.color.white))
        whiteCircleDrawable.setStroke(0, 0)
        binding.chatroomMainIcon.background = whiteCircleDrawable
        binding.chatroomMainIcon.setImageDrawable(
            ContextCompat.getDrawable(
                requireContext(),
                RandomCupcake.getImage()
            )
        )
        // Header Text
        binding.chatroomTitle.text =
            talkViewModel.chatRoomList.value?.get(talkViewModel.currentChatRoomId.value)?.title
                ?: "익명의 채팅방"        // Post Icon

        val inputDrawable = ContextCompat.getDrawable(
            requireContext(),
            R.drawable.shape_square_circle
        ) as GradientDrawable
        inputDrawable.setColor(getColor(requireContext(), R.color.white))
        inputDrawable.setStroke(4, getColor(requireContext(), Theme.theme.sub500))
        componentChatInputBinding.etTalkInputBackground.background = inputDrawable

        val sendDrawable = ContextCompat.getDrawable(
            requireContext(),
            R.drawable.shape_all_round_20
        ) as GradientDrawable
        sendDrawable.setColor(getColor(requireContext(), Theme.theme.sub500))
        componentChatInputBinding.talkSend.background = sendDrawable

        val cursorDrawable =
            ContextCompat.getDrawable(requireContext(), R.drawable.shape_cursor) as GradientDrawable
        cursorDrawable.setColor(getColor(requireContext(), Theme.theme.main500))
        @RequiresApi(Build.VERSION_CODES.Q)
        componentChatInputBinding.etTalkInput.textCursorDrawable = cursorDrawable
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