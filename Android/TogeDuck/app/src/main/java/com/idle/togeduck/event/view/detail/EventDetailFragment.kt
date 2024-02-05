package com.idle.togeduck.event.view.detail

import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.idle.togeduck.R
import com.idle.togeduck.databinding.ComponentEventReviewInputBinding
import com.idle.togeduck.databinding.FragmentEventDetailBinding
import com.idle.togeduck.common.Theme
import com.idle.togeduck.event.EventListViewModel
import com.idle.togeduck.event.EventViewModel
import com.idle.togeduck.event.model.Event
import com.idle.togeduck.event.model.LikeEventRequest
import com.idle.togeduck.util.TogeDuckItemDecoration
import com.idle.togeduck.util.getColor
import com.idle.togeduck.event.view.detail.detail_rv.EventReview
import com.idle.togeduck.event.view.detail.detail_rv.EventReviewAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.datetime.LocalDate
import java.io.File

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
        eventListViewModel.selectedEvent.observe(viewLifecycleOwner){ event ->
            this.event = event
            binding.cafeNameDetail.text = event.name
            binding.eventNameDetail.text = event.description
            binding.eventPeriodDetail.text = makeDateToString(event.startDate, event.endDate)

            Glide
                .with(binding.eventPosterDetail)
                .load(event.imgUrl)
                .override(1000,1000)
                .into(binding.eventPosterDetail)

            changeLikeImage(event)
            changeVisitImage(event)
        }

        eventReviewViewModel.reviewList.observe(viewLifecycleOwner){ list ->
            eventReviewAdapter.submitList(list)
        }

//        CoroutineScope(Dispatchers.IO).launch {
//            eventReviewViewModel.postReview(1, , "리뷰1")
//        }

//        CoroutineScope(Dispatchers.IO).launch {
//            eventReviewViewModel.deleteReview(1,1)
//        }


        binding.bookmarkCheck.setOnClickListener { likeClick(event) }
        binding.visitCheck.setOnClickListener { visitClick(event) }




    }

    //이미지 파일 등록
//    private fun postReview(){
//
//
//    }

    private fun setRecyclerView(){
        eventReviewAdapter = EventReviewAdapter(this, requireContext())

        binding.rvEventReview.apply {
            addItemDecoration(TogeDuckItemDecoration(10, 0))
            adapter = eventReviewAdapter
            layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        }
    }

    private fun setTheme(){
        binding.cafeNameDetail.setTextColor(getColor(requireContext(), Theme.theme.main500))
        binding.reviewTitle.setTextColor(getColor(requireContext(), Theme.theme.main500))

        val circleDrawable = ContextCompat.getDrawable(requireContext(), R.drawable.shape_circle) as GradientDrawable
        circleDrawable.setColor(getColor(requireContext(), Theme.theme.sub500))
        eventReviewInputBinding.plusEventReviewInput.background = circleDrawable

        val inputDrawable = ContextCompat.getDrawable(requireContext(), R.drawable.shape_square_circle) as GradientDrawable
        inputDrawable.setColor(getColor(requireContext(), R.color.white))
        inputDrawable.setStroke(4, getColor(requireContext(), Theme.theme.sub500))
        eventReviewInputBinding.etReviewInput.background = inputDrawable

        val registDrawable = ContextCompat.getDrawable(requireContext(), R.drawable.shape_all_round_20) as GradientDrawable
        registDrawable.setColor(getColor(requireContext(), Theme.theme.sub500))
        eventReviewInputBinding.reviewReg.background = registDrawable
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
            if(event.isStar){
                val likeEventRequest = LikeEventRequest(1)
                eventListViewModel.likeEvent(likeEventRequest)
                Log.d("log", "eventDetailfragment - 즐겨찾기 추가 ")
            }else{
                eventListViewModel.unlikeEvent(1)
                Log.d("log", "eventDetailfragment - 즐겨찾기 삭제")
            }
        }
    }

    override fun visitClick(event: Event) {
        event.isVisited = !event.isVisited

        changeVisitImage(event)

        //todo.방문체크 api 없음
//        CoroutineScope(Dispatchers.IO).launch {
//            if(event.isVisited){
//
//            }
//        }
    }



    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        _eventReviewInputBinding = null
    }

}