package com.idle.togeduck.quest.exchange.view

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.idle.togeduck.R
import com.idle.togeduck.databinding.DialogQuestAcceptBinding
import com.idle.togeduck.fcm.FCMData
import com.idle.togeduck.quest.talk.TalkViewModel
import com.idle.togeduck.quest.talk.model.TalkRoom

class ExchangeAcceptDialogFragment: DialogFragment() {
    private var _binding: DialogQuestAcceptBinding? = null
    private val binding get() = _binding!!

    val talkViewModel: TalkViewModel by activityViewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NO_TITLE, R.style.fullscreen_dialog)
        isCancelable = true
    }

    // 들어오고 나갈때 애니메이션 설정
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState)
//        dialog.window!!.attributes.windowAnimations = R.style.dialog_animation
        dialog.window!!.addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)
        return dialog
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DialogQuestAcceptBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.btnBack.setOnClickListener{
            onClick()
        }
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    fun onClick(){
        FCMData.chatId
        if(FCMData.chatId.value != null){
            talkViewModel.addTalkRoom(TalkRoom(FCMData.chatId.value!!, "교환 채팅방", mutableMapOf()))
            talkViewModel.currentChatRoomId.value = FCMData.chatId.value!!
//            findNavController().navigate(R.id.action_eventCloseDialog_pop)
            findNavController().navigate(R.id.action_exchangeAcceptDialogFragment_to_chatRoomFragment)
        }
    }
}