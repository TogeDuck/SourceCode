package com.idle.togeduck.quest.exchange.view

import android.app.Dialog
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.idle.togeduck.R
import com.idle.togeduck.common.ScreenSize
import com.idle.togeduck.common.Theme
import com.idle.togeduck.databinding.DialogQuestExchangeBinding
import com.idle.togeduck.databinding.DialogQuestExchangePostBinding
import com.idle.togeduck.event.EventListViewModel
import com.idle.togeduck.event.model.Event
import com.idle.togeduck.quest.exchange.ExchangeViewModel
import com.idle.togeduck.quest.exchange.model.ExchangeReq
import com.idle.togeduck.quest.exchange.model.ExchangeRequest
import com.idle.togeduck.quest.exchange.model.toMultipartBody
import com.idle.togeduck.util.DpPxUtil
import com.idle.togeduck.util.MultiPartUtil
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.Json.Default.encodeToString
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File

class ExchangePostDialogFragment: DialogFragment() {
    private var _binding: DialogQuestExchangePostBinding? = null
    private val binding get() = _binding!!

    private val eventListViewModel: EventListViewModel by activityViewModels()
    private val exchangeViewModel: ExchangeViewModel by activityViewModels()

//    private lateinit var event: Event
    private var imgPath: String? = null

    private val pickMedia = registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
        if (uri != null) {
            binding.exchangePostImg.visibility = View.VISIBLE
            binding.exchangePostImg.setImageURI(uri)
            imgPath = MultiPartUtil.uriToFilePath(requireContext(), uri)

        } else {
            Log.d("로그", "ExchangePostDialogFragment - pickMedia - 이미지 선택 실패")
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
        _binding = DialogQuestExchangePostBinding.inflate(inflater, container, false)
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
            findNavController().navigate(R.id.action_exchangePostDialogFragment_pop)
        }

        binding.btnExchangeCancel.setOnClickListener {
            findNavController().navigate(R.id.action_exchangePostDialogFragment_pop)
        }

        binding.btnExchangePost.setOnClickListener {
//            val eventId = event.eventId
            val content = binding.etExchangeInput.text.toString()
            val duration = binding.npExchangeDuration.value

            val exchangeRequest = ExchangeRequest(content, duration)
            val exchangeRequestPart = exchangeRequest.toMultipartBody()

            if(content.isNotEmpty() && duration > 0 && duration < 61){

                if(imgPath?.isNotEmpty() == true){
                    val exchaneImg = MultiPartUtil.createImagePart(imgPath!!)

                    CoroutineScope(Dispatchers.IO).launch {
                        Log.d("교환 등록", "교환 등록 호출됨")
                        Log.d("tradeRequest", "exchangeRequest : ${exchangeRequestPart}")
                        exchangeViewModel.postExchange(3, exchaneImg, exchangeRequestPart)

                        launch(Dispatchers.Main) {
                            imgPath = null
                        }
                    }
                }
            }

            //todo. 퀘스트 리스트로 이동?
            //등록되었습니다 알림?
            findNavController().navigate(R.id.action_exchangePostDialogFragment_pop)
        }

        binding.exchangeImgBtn.setOnClickListener{
            binding.exchangeImgBtn.visibility = View.GONE
            pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
        }


    }


    private fun setTheme() {
        binding.npExchangeDuration.apply {
            minValue = 1
            maxValue = 60
        }

        val allRoundDrawable = ContextCompat.getDrawable(requireContext(), R.drawable.shape_all_round_10) as GradientDrawable
        allRoundDrawable.setColor(ContextCompat.getColor(requireContext(), Theme.theme.main200))
        allRoundDrawable.setStroke(0, ContextCompat.getColor(requireContext(), Theme.theme.main500))
        binding.llDialogLayout.background = allRoundDrawable

        val squareCircleDrawable = ContextCompat.getDrawable(requireContext(), R.drawable.shape_square_circle) as GradientDrawable
        squareCircleDrawable.setColor(ContextCompat.getColor(requireContext(), Theme.theme.sub400))
        squareCircleDrawable.setStroke(0, ContextCompat.getColor(requireContext(), Theme.theme.main500))

        binding.btnExchangeCancel.background = squareCircleDrawable
        binding.btnExchangePost.background = squareCircleDrawable

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}