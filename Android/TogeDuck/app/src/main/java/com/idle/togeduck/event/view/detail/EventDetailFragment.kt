package com.idle.togeduck.event.view.detail

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.GradientDrawable
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.idle.togeduck.R
import com.idle.togeduck.common.Theme
import com.idle.togeduck.databinding.ComponentEventReviewInputBinding
import com.idle.togeduck.databinding.FragmentEventDetailBinding
import com.idle.togeduck.event.EventListViewModel
import com.idle.togeduck.event.EventViewModel
import com.idle.togeduck.event.model.Event
import com.idle.togeduck.event.model.LikeEventRequest
import com.idle.togeduck.event.view.detail.detail_rv.EventPosterAdapter
import com.idle.togeduck.event.view.detail.detail_rv.EventReview
import com.idle.togeduck.event.view.detail.detail_rv.EventReviewAdapter
import com.idle.togeduck.favorite.FavoriteSettingViewModel
import com.idle.togeduck.history.HistoryViewModel
import com.idle.togeduck.history.model.HistoryData
import com.idle.togeduck.history.model.HistoryDataResponse
import com.idle.togeduck.main_map.MapViewModel
import com.idle.togeduck.main_map.view.MapFragment
import com.idle.togeduck.util.MultiPartUtil
import com.idle.togeduck.util.TogeDuckItemDecoration
import com.idle.togeduck.util.getColor
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.datetime.LocalDate
import kotlinx.datetime.toKotlinLocalDate

@AndroidEntryPoint
class EventDetailFragment : Fragment(), EventReview {
    private var _binding: FragmentEventDetailBinding? = null
    private val binding get() = _binding!!

    private var _eventReviewInputBinding: ComponentEventReviewInputBinding? = null
    private val eventReviewInputBinding get() = _eventReviewInputBinding!!

    private val eventListViewModel: EventListViewModel by activityViewModels()
    private val eventReviewViewModel: EventViewModel by activityViewModels()
    private val favoriteSettingViewModel: FavoriteSettingViewModel by activityViewModels()
    private val mapViewModel: MapViewModel by activityViewModels()

    private lateinit var eventReviewAdapter: EventReviewAdapter
    private lateinit var eventPosterAdapter: EventPosterAdapter
    private lateinit var event: Event
    private var imgPath: String? = null

    private val pickMedia = registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
        if (uri != null) {
            eventReviewInputBinding.reviewImgThumb.visibility = View.VISIBLE
            eventReviewInputBinding.reviewImgThumb.setImageURI(uri)
            imgPath = MultiPartUtil.uriToFilePath(requireContext(), uri)

        } else {
            Log.d("로그", "EventDetailFragment - pickMedia - 이미지 선택 실패")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentEventDetailBinding.inflate(inflater, container, false)
        _eventReviewInputBinding = binding.compReviewInput
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setTheme()
        setRecyclerView()

        //리스트의 해당 이벤트 정보 가져오기
        eventListViewModel.selectedEvent.observe(viewLifecycleOwner) { event ->
                this.event = event
                binding.cafeNameDetail.text = event.name
                binding.eventPeriodDetail.text = makeDateToString(event.startDate, event.endDate)

                eventReviewViewModel.setSelectedEventId(event.eventId)

                Log.d("로그", "이벤트 디테일 페이지 : ${event}")

                val list = mutableListOf<String>().apply {
                    if (event.image1 != null && event.image1 != "trash") add(event.image1)
                    if (event.image2 != null && event.image2 != "trash" && event.image2 != event.image1) add(event.image2)
                    if (event.image3 != null && event.image3 != "trash" && event.image3 != event.image1 && event.image3 != event.image2) add(event.image3)
                }

                eventPosterAdapter.submitList(list)
                // TODO. image2, image3 처리 필요

                changeLikeImage(event)
                changeVisitImage(event)
        }

        eventReviewViewModel.reviewList.observe(viewLifecycleOwner) { list ->
            eventReviewAdapter.submitList(list)
        }

        binding.bookmarkCheck.setOnClickListener { likeClick(event) }

        eventReviewInputBinding.cameraBtn.setOnClickListener {
            pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
        }

        eventReviewInputBinding.reviewPost.setOnClickListener {
            postReview()
        }

        binding.goBack.setOnClickListener {
            (parentFragment as MapFragment).changeViewPagerPage(1)
        }

    }

    override fun onResume() {
        super.onResume()
        getReviewList()
    }


    private fun getReviewList(){
        if(eventListViewModel.selectedEvent.value != null){
            CoroutineScope(Dispatchers.IO).launch {
                val eventId = eventListViewModel.selectedEvent.value!!.eventId
                eventReviewViewModel.getReviewList(eventId, 0,1000)
                Log.d("로그", "getReviewList 호출됨")
            }
        }
    }

    private fun setRecyclerView(){
        eventReviewAdapter = EventReviewAdapter(this, requireContext())

        binding.rvEventReview.apply {
            addItemDecoration(TogeDuckItemDecoration(10, 0))
            adapter = eventReviewAdapter
            layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        }

        eventPosterAdapter = EventPosterAdapter()

        binding.eventDetailViewpager.adapter = eventPosterAdapter
        binding.eventDetailViewpager.orientation = ViewPager2.ORIENTATION_HORIZONTAL

        binding.eventDetailViewpagerIndicator.attachTo(binding.eventDetailViewpager)
    }

    private fun setTheme(){
        binding.cafeNameDetail.setTextColor(getColor(requireContext(), Theme.theme.main500))
        binding.reviewTitle.setTextColor(getColor(requireContext(), Theme.theme.main500))

        val circleDrawable = ContextCompat.getDrawable(requireContext(), R.drawable.shape_circle) as GradientDrawable
        circleDrawable.setColor(getColor(requireContext(), Theme.theme.sub500))
        circleDrawable.setStroke(0, Theme.theme.sub500)
        eventReviewInputBinding.cameraBtn.background = circleDrawable

        val inputDrawable = ContextCompat.getDrawable(requireContext(), R.drawable.shape_square_circle) as GradientDrawable
        inputDrawable.setColor(getColor(requireContext(), R.color.white))
        inputDrawable.setStroke(4, getColor(requireContext(), Theme.theme.sub500))
        eventReviewInputBinding.etReviewInputBackground.background = inputDrawable

        val registDrawable = ContextCompat.getDrawable(requireContext(), R.drawable.shape_all_round_20) as GradientDrawable
        registDrawable.setColor(getColor(requireContext(), Theme.theme.sub500))
        registDrawable.setStroke(0, Theme.theme.sub500)
        eventReviewInputBinding.reviewPost.background = registDrawable

        binding.eventDetailViewpagerIndicator.setDotIndicatorColor(getColor(requireContext(), Theme.theme.sub500))
        binding.eventDetailViewpagerIndicator.setStrokeDotsIndicatorColor(getColor(requireContext(), Theme.theme.sub500))

        val cursorDrawable =
            ContextCompat.getDrawable(requireContext(), R.drawable.shape_cursor) as GradientDrawable
        cursorDrawable.setColor(getColor(requireContext(), Theme.theme.main500))

        @RequiresApi(Build.VERSION_CODES.Q)
        eventReviewInputBinding.etReviewInput.textCursorDrawable = cursorDrawable
    }

    private fun makeDateToString(startDate: LocalDate, endDate: LocalDate): String{
        return startDate.toString()+" ~ "+endDate.toString()
    }

    override fun changeLikeImage(event: Event) {
        if(event.isStar) {
            Glide
                .with(binding.bookmarkCheck)
                .load(R.drawable.common_cupcake8)
                .override(700,700)
                .into(binding.bookmarkCheck)
        }else {
            Glide
                .with(binding.bookmarkCheck)
                .load(R.drawable.common_cupcake8_empty)
                .override(700,700)
                .into(binding.bookmarkCheck)
        }
    }

    override fun changeVisitImage(event: Event) {
        if(event.isVisited) {
            Glide
                .with(binding.visitCheck)
                .load(R.drawable.common_cupcake5)
                .override(700,700)
                .into(binding.visitCheck)
        }else {
            Glide
                .with(binding.visitCheck)
                .load(R.drawable.common_cupcake5_empty)
                .override(700,700)
                .into(binding.visitCheck)
        }
    }

    override fun likeClick(event: Event) {
        event.isStar = !event.isStar
        changeLikeImage(event)

        CoroutineScope(Dispatchers.IO).launch {
            if(event.isStar) {
                eventListViewModel.likeEvent(event.eventId)
                eventListViewModel.initList()
                eventListViewModel.getEventList(
                    favoriteSettingViewModel.selectedCelebrity.value!!.id,
                    mapViewModel.pickedDate.value!!.first.toKotlinLocalDate(),
                    mapViewModel.pickedDate.value!!.second.toKotlinLocalDate())
            }else {
                eventListViewModel.unlikeEvent(event.eventId)
                eventListViewModel.initList()
                eventListViewModel.getEventList(
                    favoriteSettingViewModel.selectedCelebrity.value!!.id,
                    mapViewModel.pickedDate.value!!.first.toKotlinLocalDate(),
                    mapViewModel.pickedDate.value!!.second.toKotlinLocalDate())
            }
        }
    }


    private fun postReview(){
        val selectedEventId = event.eventId
        val reviewInputText = eventReviewInputBinding.etReviewInput.text.toString()
        if (reviewInputText.isNotEmpty()) {
            val reviewText = MultiPartUtil.createRequestBody(reviewInputText)
            if(imgPath?.isNotEmpty() == true) {
                val reviewImg = MultiPartUtil.createImagePart(imgPath!!)

                CoroutineScope(Dispatchers.IO).launch {
                    Log.d("리뷰 등록", "이미지 있는 리뷰 등록")
                    eventReviewViewModel.postReview(selectedEventId, reviewImg, reviewText)
                    eventReviewViewModel.getReviewList(selectedEventId, 0, 1000)

                    launch(Dispatchers.Main) {
                        eventReviewInputBinding.etReviewInput.text?.clear()
                        eventReviewInputBinding.reviewImgThumb.visibility = View.GONE
                        imgPath = null
                    }
                }
            }else {
                CoroutineScope(Dispatchers.IO).launch {
                    Log.d("리뷰 등록", "이미지 없는 리뷰 등록")
                    eventReviewViewModel.postReview(selectedEventId, null, reviewText)
                    eventReviewViewModel.getReviewList(selectedEventId, 0, 1000)

                    launch(Dispatchers.Main) {
                        eventReviewInputBinding.etReviewInput.text?.clear()
                    }
                }
            }
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        _eventReviewInputBinding = null
    }

}