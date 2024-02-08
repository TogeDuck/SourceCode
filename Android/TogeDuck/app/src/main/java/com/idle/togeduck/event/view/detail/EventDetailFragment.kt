package com.idle.togeduck.event.view.detail

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
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
import com.idle.togeduck.util.MultiPartUtil
import com.idle.togeduck.util.TogeDuckItemDecoration
import com.idle.togeduck.util.getColor
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.datetime.LocalDate

@AndroidEntryPoint
class EventDetailFragment : Fragment(), EventReview {
    private var _binding: FragmentEventDetailBinding? = null
    private val binding get() = _binding!!

    private var _eventReviewInputBinding: ComponentEventReviewInputBinding? = null
    private val eventReviewInputBinding get() = _eventReviewInputBinding!!

    private val eventListViewModel: EventListViewModel by activityViewModels()
    private val eventReviewViewModel: EventViewModel by activityViewModels()

    private lateinit var eventReviewAdapter: EventReviewAdapter
    private lateinit var event: Event
    private var imgPath: String? = null

    private lateinit var eventPosterAdapter: EventPosterAdapter


    private val pickMedia = registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
        if (uri != null) {
            eventReviewInputBinding.reviewImgThumb.visibility = View.VISIBLE
            eventReviewInputBinding.reviewImgThumb.setImageURI(uri)
//            imgPath = MultiPartUtil.uriToFilePath(requireContext(), uri)

            val bitmap = BitmapFactory.decodeStream(requireContext().contentResolver.openInputStream(uri))
            imgPath = MultiPartUtil.uriToFilePath2(requireContext(), bitmap, uri)

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

            val list = mutableListOf<String>().apply {
                if (event.image1 != null) add(event.image1)
                if (event.image2 != null) add(event.image2)
                if (event.image3 != null) add(event.image3)
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
        binding.visitCheck.setOnClickListener { visitClick(event) }

        eventReviewInputBinding.cameraBtn.setOnClickListener {
            pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
        }

        eventReviewInputBinding.reviewPost.setOnClickListener {
            postReview()
        }

//        eventReviewInputBinding.etReviewInput.setOnClickListener { mView, keyCode, _ ->
//            hideKeyboard(mView, keyCode)
//        }
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
        eventReviewInputBinding.llInputLayout.background = inputDrawable

        val registDrawable = ContextCompat.getDrawable(requireContext(), R.drawable.shape_all_round_20) as GradientDrawable
        registDrawable.setColor(getColor(requireContext(), Theme.theme.sub500))
        eventReviewInputBinding.reviewPost.background = registDrawable

        binding.eventDetailViewpagerIndicator.setDotIndicatorColor(getColor(requireContext(), Theme.theme.sub500))
        binding.eventDetailViewpagerIndicator.setStrokeDotsIndicatorColor(getColor(requireContext(), Theme.theme.sub500))
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
                val likeEventRequest = LikeEventRequest(1)
                eventListViewModel.likeEvent(likeEventRequest)
                Log.d("log", "eventDetailfragment - 즐겨찾기 추가 ")
            }else {
                eventListViewModel.unlikeEvent(1)
                Log.d("log", "eventDetailfragment - 즐겨찾기 삭제")
            }
        }
    }

    override fun visitClick(event: Event) {
        event.isVisited = !event.isVisited
        changeVisitImage(event)

        //todo.방문 체크 api 추가
//        CoroutineScope(Dispatchers.IO).launch {
//            if(event.isVisited){
//            }
//        }
    }

//    private fun postReview(){
//        val reviewInputText = eventReviewInputBinding.etReviewInput.text.toString()
//
//        if (reviewInputText.isNotEmpty()) {
//            val reviewText = MultiPartUtil.createRequestBody(reviewInputText)
//
//            if(imgPath?.isNotEmpty() == true) {
//                val reviewImg = MultiPartUtil.createImagePart(imgPath!!)
//
//                CoroutineScope(Dispatchers.IO).launch {
//                    Log.d("리뷰 등록", "이미지 있는 리뷰 등록")
//                    eventReviewViewModel.postReview(1, reviewImg, reviewText)
//                    eventReviewViewModel.getReviewList(1, 1, 100)
//
//                    eventReviewInputBinding.etReviewInput.text?.clear()
//
//                    //튕김
////                    eventReviewInputBinding.reviewImgThumb.visibility = View.GONE
////                    imgPath = null
//                }
//            }else {
//                CoroutineScope(Dispatchers.IO).launch {
//                    Log.d("리뷰 등록", "이미지 없는 리뷰 등록")
//                    eventReviewViewModel.postReview(1, null, reviewText)
//                    eventReviewViewModel.getReviewList(1, 1, 100)
//
//                    eventReviewInputBinding.etReviewInput.text?.clear()
//                }
//            }
//        }
//    }

    private fun postReview(){
        val reviewInputText = eventReviewInputBinding.etReviewInput.text.toString()

        if (reviewInputText.isNotEmpty()) {
            val reviewText = MultiPartUtil.createRequestBody(reviewInputText)

            if(imgPath?.isNotEmpty() == true) {
                val reviewImg = MultiPartUtil.createImagePart(imgPath!!)

                CoroutineScope(Dispatchers.IO).launch {
                    Log.d("리뷰 등록", "이미지 있는 리뷰 등록")
                    eventReviewViewModel.postReview(1, reviewImg, reviewText)
                    eventReviewViewModel.getReviewList(1, 1, 100)

                    eventReviewInputBinding.etReviewInput.text?.clear()

                    //튕김
//                    eventReviewInputBinding.reviewImgThumb.visibility = View.GONE
//                    imgPath = null
                }
            }else {
                CoroutineScope(Dispatchers.IO).launch {
                    Log.d("리뷰 등록", "이미지 없는 리뷰 등록")
                    eventReviewViewModel.postReview(1, null, reviewText)
                    eventReviewViewModel.getReviewList(1, 1, 100)

                    eventReviewInputBinding.etReviewInput.text?.clear()
                }
            }
        }
    }




//    private fun hideKeyboard(view: View, keyCode: Int): Boolean {
//        if (keyCode == KeyEvent.KEYCODE_ENTER) {
//            val inputManager = requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE)
//                    as InputMethodManager
//            inputManager.hideSoftInputFromWindow(view.windowToken, 0)
//            eventReviewInputBinding.etReviewInput.clearFocus()
//            return true
//        }
//        return false
//    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        _eventReviewInputBinding = null
    }

}