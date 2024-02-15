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
import com.idle.togeduck.R
import com.idle.togeduck.common.Theme
import com.idle.togeduck.databinding.DialogQuestSharePostBinding
import com.idle.togeduck.event.EventListViewModel
import com.idle.togeduck.favorite.FavoriteSettingViewModel
import com.idle.togeduck.quest.share.ShareViewModel
import com.idle.togeduck.quest.share.model.ShareRequest
import com.idle.togeduck.quest.share.model.toMultipartBody
import com.idle.togeduck.util.MultiPartUtil
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SharePostDialogFragment: DialogFragment() {
    private var _binding: DialogQuestSharePostBinding? = null
    private val binding get() = _binding!!

    val shareViewModel: ShareViewModel by activityViewModels()
    val favoriteSettingViewModel: FavoriteSettingViewModel by activityViewModels()
    val eventListViewModel: EventListViewModel by activityViewModels()

    private var imgPath: String? = null
    private lateinit var eventIds: List<Long>

    private val pickMedia = registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
        if (uri != null) {
            binding.sharePostImg.visibility = View.VISIBLE
            binding.sharePostImg.setImageURI(uri)
            imgPath = MultiPartUtil.uriToFilePath(requireContext(), uri)

        } else {
            Log.d("로그", "SharePostDialogFragment - pickMedia - 이미지 선택 실패")
            binding.shareImgBtn.visibility = View.VISIBLE
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

        eventListViewModel.listToday.observe(viewLifecycleOwner) {event ->
            eventIds = event.map { it.eventId }
        }

        binding.llBackground.setOnClickListener{
            findNavController().navigate(R.id.action_sharePostDialogFragment_pop)
        }

        binding.btnShareCancel.setOnClickListener {
            findNavController().navigate(R.id.action_sharePostDialogFragment_pop)
        }

        binding.btnSharePost.setOnClickListener {
            if(eventListViewModel.selectedEvent.value == null) {
                var toast = Toast.makeText(requireContext(), "이벤트를 선택한 경우에만 퀘스트를 등록할 수 있습니다.", Toast.LENGTH_SHORT)
                toast.show()
                toast = Toast.makeText(requireContext(), "이벤트를 선택 후 나눔글을 작성해주세요", Toast.LENGTH_SHORT)
                toast.show()
            } else {
                val eventId = eventListViewModel.selectedEvent.value!!.eventId
                if(eventId !in eventIds) {
                    var toast = Toast.makeText(requireContext(), "현재 진행중인 이벤트를 선택한 경우에만 퀘스트를 등록할 수 있습니다", Toast.LENGTH_SHORT)
                    toast.show()
                    toast = Toast.makeText(requireContext(), "선택한 이벤트가 과거 혹은 예정된 생일카페인지 확인해 주세요", Toast.LENGTH_SHORT)
                    toast.show()
                } else {
                    val title = binding.etShareTitle.text.toString()
                    val content = binding.etShareContent.text.toString()
                    val duration = 120

                    val shareRequest = ShareRequest(title, content, duration)
                    val shareRequestPart = shareRequest.toMultipartBody()

                    if(title.isNotEmpty() && content.isNotEmpty() && imgPath?.isNotEmpty() == true
                        && favoriteSettingViewModel.selectedCelebrity.value != null
                        && eventListViewModel.selectedEvent.value != null){
                        val shareImg = MultiPartUtil.createImagePart(imgPath!!)

                        CoroutineScope(Dispatchers.IO).launch {
                            Log.d("나눔 등록", "나눔 등록 호출됨")
                            Log.d("shareRequest", "shareRequest : ${shareRequestPart}")
                            shareViewModel.createShare(
                                eventId,
                                shareImg,
                                shareRequestPart,
                                favoriteSettingViewModel.selectedCelebrity.value!!.id)

                            launch(Dispatchers.Main){
                                imgPath = null
                            }
                        }
                    }
                    findNavController().navigate(R.id.action_sharePostDialogFragment_pop)
                }
            }
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