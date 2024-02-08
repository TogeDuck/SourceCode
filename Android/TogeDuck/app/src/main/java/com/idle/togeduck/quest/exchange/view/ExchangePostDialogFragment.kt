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
import com.idle.togeduck.quest.exchange.ExchangeViewModel
import com.idle.togeduck.quest.exchange.model.ExchangeRequest
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

    val exchangeViewModel: ExchangeViewModel by activityViewModels()

    private var imgPath: String? = null

    private val pickMedia = registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
        if (uri != null) {
            binding.exchangePostImg.visibility = View.VISIBLE
            binding.exchangePostImg.setImageURI(uri)
            imgPath = MultiPartUtil.uriToFilePath(requireContext(), uri)

        } else {
            Log.d("로그", "EventDetailFragment - pickMedia - 이미지 선택 실패")
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

        binding.llBackground.setOnClickListener{
            findNavController().navigate(R.id.action_exchangePostDialogFragment_pop)
        }

        binding.btnShareCancel.setOnClickListener {
            findNavController().navigate(R.id.action_exchangePostDialogFragment_pop)
        }

        binding.btnSharePost.setOnClickListener {
//            val imgFile = MultiPartUtil.createImagePart(imgPath!!)
//
//            val inputContent = binding.etExchangeInput.text.toString()
////            val contentRequestBody = MultiPartUtil.createRequestBody2(inputContent)
//
//            val inputDuration = binding.npExchangeDuration.value
//
//            CoroutineScope(Dispatchers.IO).launch {
////                exchangeViewModel.postExchange(1, imgFile, contentRequestBody, inputDuration)
//                exchangeViewModel.postExchange(1, imgFile, ExchangeRequest(inputContent, inputDuration))
//            }


//            val imgFile = MultiPartUtil.createImagePart(imgPath!!)
//
//            val inputContent = binding.etExchangeInput.text.toString()
//            val inputDuration = binding.npExchangeDuration.value
//            val exchangeRequest = ExchangeRequest(inputContent, inputDuration)
//            val json = encodeToString(exchangeRequest)
//            //todo.이렇게 하려면 라이브러리 추가해야함..?
//            //todo.재환오빠한테 dto말고 그냥 스트링, 인트 빼서 수정해달라고 부탁....?
//
//            CoroutineScope(Dispatchers.IO).launch {
////                exchangeViewModel.postExchange(1, imgFile, contentRequestBody, inputDuration)
//                exchangeViewModel.postExchange(1, imgFile, ExchangeRequest(inputContent, inputDuration))
//            }


            findNavController().navigate(R.id.action_exchangePostDialogFragment_pop)
            //등록되었습니다 알림? 마이퀘스트 리스트에 추가
        }

        binding.exchangeImgBtn.setOnClickListener{
            binding.exchangeImgBtn.visibility = View.GONE
            pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
        }


    }


    private fun setTheme() {
        binding.npExchangeDuration.apply {
            minValue = 0
            maxValue = 120
        }

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