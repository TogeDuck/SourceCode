package com.idle.togeduck.view

import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.idle.togeduck.R
import com.idle.togeduck.databinding.ComponentEventReviewInputBinding
import com.idle.togeduck.databinding.FragmentEventDetailBinding
import com.idle.togeduck.model.Event
import com.idle.togeduck.model.Review
import com.idle.togeduck.util.Theme
import com.idle.togeduck.util.TogeDuckItemDecoration
import com.idle.togeduck.util.getColor
import com.idle.togeduck.view.event_detail.EventReview
import com.idle.togeduck.view.event_detail.EventReviewAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class EventDetailFragment : Fragment(), EventReview{
    private var _binding: FragmentEventDetailBinding? = null
    private val binding get() = _binding!!

    private var _eventReviewInputBinding: ComponentEventReviewInputBinding? = null

    private val eventReviewInputBinding get() = _eventReviewInputBinding!!

    private lateinit var eventReviewAdapter: EventReviewAdapter

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

//        eventReviewAdapter.submitList(tempData)
    }

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
        binding.reviewTitle.setTextColor(getColor(requireContext(),Theme.theme.main500))

        val circleDrawable = ContextCompat.getDrawable(requireContext(), R.drawable.shape_circle) as GradientDrawable
        circleDrawable.setColor(getColor(requireContext(), Theme.theme.sub500))
        eventReviewInputBinding.plusEventReviewInput.background = circleDrawable

        val inputDrawable = ContextCompat.getDrawable(requireContext(), R.drawable.shape_square_circle) as GradientDrawable
        inputDrawable.setColor(getColor(requireContext(), R.color.white))
        inputDrawable.setStroke(4, getColor(requireContext(),Theme.theme.sub500))
        eventReviewInputBinding.etReviewInput.background = inputDrawable

        val registDrawable = ContextCompat.getDrawable(requireContext(), R.drawable.shape_all_round_20) as GradientDrawable
        registDrawable.setColor(getColor(requireContext(), Theme.theme.sub500))
        eventReviewInputBinding.reviewReg.background = registDrawable
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        _eventReviewInputBinding = null
    }
}