package com.idle.togeduck.quest.recruit.view

import android.annotation.SuppressLint
import android.app.Dialog
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.idle.togeduck.R
import com.idle.togeduck.common.ScreenSize
import com.idle.togeduck.common.Theme
import com.idle.togeduck.databinding.DialogQuestExchangeBinding
import com.idle.togeduck.databinding.DialogQuestExchangePostBinding
import com.idle.togeduck.databinding.DialogQuestRecruitPostBinding
import com.idle.togeduck.event.EventListViewModel
import com.idle.togeduck.event.view.list.list_rv.EventInfo
import com.idle.togeduck.event.view.list.list_rv.EventInfoAdapter
import com.idle.togeduck.favorite.FavoriteSettingViewModel
import com.idle.togeduck.main_map.MapViewModel
import com.idle.togeduck.quest.recruit.RecruitViewModel
import com.idle.togeduck.quest.recruit.model.RecruitRequest
import com.idle.togeduck.quest.talk.TalkViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.datetime.toKotlinLocalDate
import kotlin.math.min

class RecruitPostDialogFragment : DialogFragment() {
    private var _binding: DialogQuestRecruitPostBinding? = null
    private val binding get() = _binding!!

    val recruitViewModel: RecruitViewModel by activityViewModels()

    private val eventListViewModel: EventListViewModel by activityViewModels()
    private val mapViewModel: MapViewModel by activityViewModels()
    private val favoriteSettingViewModel: FavoriteSettingViewModel by activityViewModels()
    private val talkViewModel: TalkViewModel by activityViewModels()

    private lateinit var spinnerAdapter: ArrayAdapter<String>
    private lateinit var eventNames: List<String>
    private lateinit var eventIds: List<Long>
    private lateinit var eventIdsRecruit: List<Long>

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
        _binding = DialogQuestRecruitPostBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setTheme()
        setSpinner()

        eventListViewModel.listToday.observe(viewLifecycleOwner) {event ->
            eventIdsRecruit = event.map { it.eventId }
        }

        CoroutineScope(Dispatchers.IO).launch {
            val celebrityId = favoriteSettingViewModel.selectedCelebrity.value?.id ?: return@launch
            val (startDate, endDate) = mapViewModel.pickedDate.value ?: return@launch
            eventListViewModel.getEventList(
                celebrityId,
                startDate.toKotlinLocalDate(),
                endDate.toKotlinLocalDate()
            )
        }

        binding.llBackground.setOnClickListener {
            findNavController().navigate(R.id.action_recruitPostDialogFragment_pop)
        }

        binding.btnRecruitCancel.setOnClickListener {
            findNavController().navigate(R.id.action_recruitPostDialogFragment_pop)
        }

        binding.btnRecruitPost.setOnClickListener {
            if(eventListViewModel.selectedEvent.value == null) {
                var toast = Toast.makeText(requireContext(), "이벤트를 선택한 경우에만 퀘스트를 등록할 수 있습니다.", Toast.LENGTH_SHORT)
                toast.show()
                toast = Toast.makeText(requireContext(), "이벤트를 선택 후 모집글을 작성해주세요", Toast.LENGTH_SHORT)
                toast.show()
            } else{
                val eventId = eventListViewModel.selectedEvent.value!!.eventId
                if (eventId !in eventIdsRecruit) {
                    var toast = Toast.makeText(
                        requireContext(),
                        "현재 진행중인 이벤트를 선택한 경우에만 퀘스트를 등록할 수 있습니다",
                        Toast.LENGTH_SHORT
                    )
                    toast.show()
                    toast = Toast.makeText(
                        requireContext(),
                        "선택한 이벤트가 과거 혹은 예정된 생일카페인지 확인해 주세요",
                        Toast.LENGTH_SHORT
                    )
                    toast.show()
                } else {
                    val title = binding.etRecruitTitle.text.toString()
                    val maximum = binding.npRecruitPeopleNum.value
                    val duration = binding.npRecruitDuration.value

                    val selectedPosition = binding.spinner.selectedItemPosition
                    val destinationName = eventNames[selectedPosition]
                    val destinationId = eventIds[selectedPosition]

                    val recruitRequest = RecruitRequest(title, destinationId, maximum, duration)
                    if (title.isNotEmpty() && destinationName.isNotEmpty()
                        && maximum > 0 && maximum <= 10
                        && duration > 0 && duration <= 120
                        && eventListViewModel.selectedEvent.value != null
                    ) {
                        CoroutineScope(Dispatchers.IO).launch {
                            Log.d("모집 등록", "모집 등록 호출됨")
                            recruitViewModel.createRecruit(
                                eventId,
                                recruitRequest,
                                favoriteSettingViewModel.selectedCelebrity.value!!.id
                            )
                        }
                    }
                    findNavController().navigate(R.id.action_recruitPostDialogFragment_pop)
                }
            }
        }
    }

    private fun setSpinner() {
        // 오늘의 이벤트 목록을 관찰하고 해당 목록을 spinner에 설정
        eventListViewModel.listToday.observe(viewLifecycleOwner) { event ->
            val eventPairs = event.filter {
                it.eventId != (eventListViewModel.selectedEvent.value?.eventId ?: -1)
            }.map { it.eventId to it.name }
            eventIds = eventPairs.map { it.first }
            eventNames = eventPairs.map { it.second }
            spinnerAdapter =
                ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, eventNames)
            spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            binding.spinner.adapter = spinnerAdapter
        }
    }

    private fun setTheme() {
        binding.npRecruitPeopleNum.apply {
            minValue = 1
            maxValue = 10
        }

        binding.npRecruitDuration.apply {
            minValue = 1
            maxValue = 120
        }


        val allRoundDrawable = ContextCompat.getDrawable(
            requireContext(),
            R.drawable.shape_all_round_10
        ) as GradientDrawable
        allRoundDrawable.setColor(ContextCompat.getColor(requireContext(), Theme.theme.main200))
        allRoundDrawable.setStroke(0, ContextCompat.getColor(requireContext(), Theme.theme.main500))
        binding.llDialogLayout.background = allRoundDrawable

        val squareCircleDrawable = ContextCompat.getDrawable(
            requireContext(),
            R.drawable.shape_square_circle
        ) as GradientDrawable
        squareCircleDrawable.setColor(ContextCompat.getColor(requireContext(), Theme.theme.sub400))
        squareCircleDrawable.setStroke(
            0,
            ContextCompat.getColor(requireContext(), Theme.theme.main500)
        )

        binding.btnRecruitCancel.background = squareCircleDrawable
        binding.btnRecruitPost.background = squareCircleDrawable
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}