package com.idle.togeduck.quest.share.view

import android.app.Dialog
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.idle.togeduck.QuestType
import com.idle.togeduck.R
import com.idle.togeduck.common.ScreenSize
import com.idle.togeduck.common.Theme
import com.idle.togeduck.databinding.DialogQuestExchangeBinding
import com.idle.togeduck.databinding.DialogQuestExchangePostBinding
import com.idle.togeduck.databinding.DialogQuestRecruitPostBinding
import com.idle.togeduck.databinding.DialogQuestSharePostBinding
import com.idle.togeduck.event.EventListViewModel
import com.idle.togeduck.favorite.FavoriteSettingViewModel
import com.idle.togeduck.network.StompManager
import com.idle.togeduck.quest.exchange.ExchangeViewModel
import com.idle.togeduck.quest.exchange.model.MyExchange
import com.idle.togeduck.quest.exchange.view.my_exchange_rv.IMyExchangeDetail
import com.idle.togeduck.quest.exchange.view.my_exchange_rv.MyExchangeAdapter
import com.idle.togeduck.quest.recruit.RecruitViewModel
import com.idle.togeduck.quest.share.ShareViewModel
import com.idle.togeduck.quest.share.model.ShareRequest
import com.idle.togeduck.quest.share.model.toMultipartBody
import com.idle.togeduck.util.DpPxUtil
import com.idle.togeduck.util.MultiPartUtil
import com.idle.togeduck.util.TogeDuckItemDecoration
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class SharePostDialogFragment: DialogFragment() {
    private var _binding: DialogQuestSharePostBinding? = null
    private val binding get() = _binding!!

    @Inject
    lateinit var stompManager: StompManager

    val shareViewModel: ShareViewModel by activityViewModels()
    private val exchangeViewModel: ExchangeViewModel by activityViewModels()
    private val favoriteSettingViewModel: FavoriteSettingViewModel by activityViewModels()
    private val eventListViewModel: EventListViewModel by activityViewModels()

    private var imgPath: String? = null

    private val pickMedia = registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
        if (uri != null) {
            binding.sharePostImg.visibility = View.VISIBLE
            binding.sharePostImg.setImageURI(uri)
            imgPath = MultiPartUtil.uriToFilePath(requireContext(), uri)

        } else {
            Log.d("로그", "SharePostDialogFragment - pickMedia - 이미지 선택 실패")
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NO_TITLE, R.style.fullscreen_dialog)
        isCancelable = true
    }

    // 들어오고 나갈때 애니메이션 설정
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState)
        dialog.window!!.addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)
        return dialog
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DialogQuestSharePostBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setTheme()

        //todo. 퀘스트 등록할 때 eventId 현재 선택한 eventId로 수정 필요 (지금 임시로 1번으로 해놓음)
//        eventListViewModel.selectedEvent.observe(viewLifecycleOwner) {event ->
//            this.event = event
//        }

        binding.llBackground.setOnClickListener{
            findNavController().navigate(R.id.action_sharePostDialogFragment_pop)
        }

        binding.btnShareCancel.setOnClickListener {
            findNavController().navigate(R.id.action_sharePostDialogFragment_pop)
        }
        //TODO EVENT-001 에러 확인 필요
        binding.btnSharePost.setOnClickListener {
            val title = binding.etShareTitle.text.toString()
            val content = binding.etShareContent.text.toString()
            val duration = 120 // 하 이거 아직도 왜 있냐고

            val shareRequest = ShareRequest(title, content, duration)
            val shareRequestPart = shareRequest.toMultipartBody()

            if(title.isNotEmpty() && content.isNotEmpty() && imgPath?.isNotEmpty() == true){
                val shareImg = MultiPartUtil.createImagePart(imgPath!!)
                if(eventListViewModel.selectedEvent.value != null && favoriteSettingViewModel.selectedCelebrity.value != null){
                    CoroutineScope(Dispatchers.IO).launch {
                        Log.d("나눔 등록", "나눔 등록 호출됨")
                        Log.d("shareRequest", "shareRequest : ${shareRequestPart}")
                        Log.d("나눔 등록 시도",eventListViewModel.selectedEvent.value.toString())
                        shareViewModel.createShare(eventListViewModel.selectedEvent.value!!.eventId, shareImg, shareRequestPart, favoriteSettingViewModel.selectedCelebrity.value!!.id)
                        launch(Dispatchers.Main){
                            imgPath = null
                        }
                    }
                }
            }
            findNavController().navigate(R.id.action_sharePostDialogFragment_pop)
        }

        binding.shareImgBtn.setOnClickListener{
            binding.shareImgBtn.visibility = View.GONE
            pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
        }

    }


    private fun setTheme() {
        val allRoundDrawable = ContextCompat.getDrawable(requireContext(), R.drawable.shape_all_round_10) as GradientDrawable
        allRoundDrawable.setColor(ContextCompat.getColor(requireContext(), Theme.theme.main200))
        allRoundDrawable.setStroke(0, ContextCompat.getColor(requireContext(), Theme.theme.main500))
        binding.llDialogLayout.background = allRoundDrawable

        val squareCircleDrawable = ContextCompat.getDrawable(requireContext(), R.drawable.shape_square_circle) as GradientDrawable
        squareCircleDrawable.setColor(ContextCompat.getColor(requireContext(), Theme.theme.sub400))
        squareCircleDrawable.setStroke(0, ContextCompat.getColor(requireContext(), Theme.theme.main500))

        binding.btnShareCancel.background = squareCircleDrawable
        binding.btnSharePost.background = squareCircleDrawable
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}