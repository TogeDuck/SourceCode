package com.idle.togeduck.quest.view

import android.content.res.ColorStateList
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.idle.togeduck.R
import com.idle.togeduck.databinding.FragmentQuestBinding
import com.idle.togeduck.common.Theme
import com.idle.togeduck.event.EventListViewModel
import com.idle.togeduck.util.getColor
import com.idle.togeduck.quest.view.quest_rv.QuestPagerAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class QuestFragment : Fragment() {
    private var _binding: FragmentQuestBinding? = null
    private val binding get() = _binding!!

    private lateinit var questPagerAdapter: QuestPagerAdapter
    private val eventListViewModel: EventListViewModel by activityViewModels()

    lateinit var yellowPickDrawable: GradientDrawable
    lateinit var yellowUnPickDrawable: GradientDrawable
    lateinit var redPickDrawable: GradientDrawable
    lateinit var redUnPickDrawable: GradientDrawable
    lateinit var greenPickDrawable: GradientDrawable
    lateinit var greenUnPickDrawable: GradientDrawable
    lateinit var bluePickDrawable: GradientDrawable
    lateinit var blueUnPickDrawable: GradientDrawable

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentQuestBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setTheme()
        initViewPager()
        setBtnClickedListener()
        setEvent()
        changeEvent()

        eventListViewModel.selectedEvent.observe(viewLifecycleOwner){event ->
            changeEvent()
        }
    }

    private fun setBtnClickedListener() {
        binding.llExchange.setOnClickListener {
            binding.viewPager.setCurrentItem(0, false)
            onExchangeClicked(true)
            onShareClicked(false)
            onRecruitClicked(false)
            onTalkClicked(false)
        }

        binding.llShare.setOnClickListener {
            binding.viewPager.setCurrentItem(1, false)
            onExchangeClicked(false)
            onShareClicked(true)
            onRecruitClicked(false)
            onTalkClicked(false)
        }

        binding.llRecruit.setOnClickListener {
            binding.viewPager.setCurrentItem(2, false)
            onExchangeClicked(false)
            onShareClicked(false)
            onRecruitClicked(true)
            onTalkClicked(false)
        }

        binding.llTalk.setOnClickListener {
            binding.viewPager.setCurrentItem(3, false)
            onExchangeClicked(false)
            onShareClicked(false)
            onRecruitClicked(false)
            onTalkClicked(true)
        }
    }

    private fun initViewPager() {
        questPagerAdapter = QuestPagerAdapter(this)
        binding.viewPager.adapter = questPagerAdapter
        binding.viewPager.isUserInputEnabled = false

        binding.viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                when (position) {
                    0 ->  {
                        onExchangeClicked(true)
                        onShareClicked(false)
                        onRecruitClicked(false)
                        onTalkClicked(false)
                    }
                    1 -> {
                        onExchangeClicked(false)
                        onShareClicked(true)
                        onRecruitClicked(false)
                        onTalkClicked(false)
                    }
                    2 -> {
                        onExchangeClicked(false)
                        onShareClicked(false)
                        onRecruitClicked(true)
                        onTalkClicked(false)
                    }
                    3 -> {
                        onExchangeClicked(false)
                        onShareClicked(false)
                        onRecruitClicked(false)
                        onTalkClicked(true)
                    }
                }
            }
        })
    }

    private fun setTheme() {
        yellowPickDrawable = ContextCompat.getDrawable(requireContext(), R.drawable.shape_square_circle) as GradientDrawable
        yellowPickDrawable.setColor(getColor(requireContext(), R.color.yellow))
        yellowPickDrawable.setStroke(0, Theme.theme.main500)

        yellowUnPickDrawable = ContextCompat.getDrawable(requireContext(), R.drawable.shape_square_circle) as GradientDrawable
        yellowUnPickDrawable.setColor(getColor(requireContext(), R.color.white))
        yellowUnPickDrawable.setStroke(4, getColor(requireContext(), R.color.yellow))

        redPickDrawable = ContextCompat.getDrawable(requireContext(), R.drawable.shape_square_circle) as GradientDrawable
        redPickDrawable.setColor(getColor(requireContext(), R.color.red))
        redPickDrawable.setStroke(0, Theme.theme.main500)

        redUnPickDrawable = ContextCompat.getDrawable(requireContext(), R.drawable.shape_square_circle) as GradientDrawable
        redUnPickDrawable.setColor(getColor(requireContext(), R.color.white))
        redUnPickDrawable.setStroke(4, getColor(requireContext(), R.color.red))

        greenPickDrawable = ContextCompat.getDrawable(requireContext(), R.drawable.shape_square_circle) as GradientDrawable
        greenPickDrawable.setColor(getColor(requireContext(), R.color.green))
        greenPickDrawable.setStroke(0, Theme.theme.main500)

        greenUnPickDrawable = ContextCompat.getDrawable(requireContext(), R.drawable.shape_square_circle) as GradientDrawable
        greenUnPickDrawable.setColor(getColor(requireContext(), R.color.white))
        greenUnPickDrawable.setStroke(4, getColor(requireContext(), R.color.green))

        bluePickDrawable = ContextCompat.getDrawable(requireContext(), R.drawable.shape_square_circle) as GradientDrawable
        bluePickDrawable.setColor(getColor(requireContext(), R.color.blue))
        bluePickDrawable.setStroke(0, Theme.theme.main500)

        blueUnPickDrawable = ContextCompat.getDrawable(requireContext(), R.drawable.shape_square_circle) as GradientDrawable
        blueUnPickDrawable.setColor(getColor(requireContext(), R.color.white))
        blueUnPickDrawable.setStroke(4, getColor(requireContext(), R.color.blue))

        onExchangeClicked(true)
        onShareClicked(false)
        onRecruitClicked(false)
        onTalkClicked(false)
    }
    private fun setEvent(){
        val squareCircle = ContextCompat.getDrawable(requireContext(), R.drawable.shape_square_circle) as GradientDrawable
        squareCircle.setColor(getColor(requireContext(), Theme.theme.sub200))
        squareCircle.setStroke(0,0)
        binding.questEventContainer.background = squareCircle
        val squareCircle1 = ContextCompat.getDrawable(requireContext(), R.drawable.shape_square_circle) as GradientDrawable
        squareCircle1.setColor(getColor(requireContext(), R.color.gray_bg))
        squareCircle.setStroke(0,0)
        binding.questEventEmptyText.background = squareCircle1
        val circle = ContextCompat.getDrawable(requireContext(), R.drawable.shape_circle) as GradientDrawable
        circle.setStroke(0,0)
        binding.questEventIcon.background = circle
    }
    private fun changeEvent(){
        if(eventListViewModel.selectedEvent.value != null){
            binding.questEventEmptyText.visibility = View.GONE
            binding.questEventContainer.visibility = View.VISIBLE
            val image = binding.questEventIcon
            eventListViewModel.selectedEvent.value?.image1?.let { imageUrl ->
                Glide.with(image)
                    .load(imageUrl)
                    .circleCrop()
                    .into(image)
            }
            binding.questEventTitle.text = "선택된 이벤트: ${eventListViewModel.selectedEvent.value!!.name}"
        }
        else {
            binding.questEventContainer.visibility = View.GONE
            binding.questEventEmptyText.visibility = View.VISIBLE
        }
    }

    private fun onExchangeClicked(selected: Boolean) {
        if (selected) {
            binding.llExchange.background = yellowPickDrawable
            binding.ivExchange.setColorFilter(getColor(requireContext(), R.color.white))
            binding.tvExchange.setTextColor(getColor(requireContext(), R.color.white))
        } else {
            binding.llExchange.background = yellowUnPickDrawable
            binding.ivExchange.setColorFilter(getColor(requireContext(), R.color.yellow))
            binding.tvExchange.setTextColor(getColor(requireContext(), R.color.yellow))
        }
    }

    private fun onShareClicked(selected: Boolean) {
        if (selected) {
            binding.llShare.background = redPickDrawable
            binding.ivShare.setColorFilter(getColor(requireContext(), R.color.white))
            binding.tvShare.setTextColor(getColor(requireContext(), R.color.white))
        } else {
            binding.llShare.background = redUnPickDrawable
            binding.ivShare.setColorFilter(getColor(requireContext(), R.color.red))
            binding.tvShare.setTextColor(getColor(requireContext(), R.color.red))
        }
    }

    private fun onRecruitClicked(selected: Boolean) {
        if (selected) {
            binding.llRecruit.background = greenPickDrawable
            binding.ivRecruit.setColorFilter(getColor(requireContext(), R.color.white))
            binding.tvRecruit.setTextColor(getColor(requireContext(), R.color.white))
        } else {
            binding.llRecruit.background = greenUnPickDrawable
            binding.ivRecruit.setColorFilter(getColor(requireContext(), R.color.green))
            binding.tvRecruit.setTextColor(getColor(requireContext(), R.color.green))
        }
    }

    private fun onTalkClicked(selected: Boolean) {
        if (selected) {
            binding.llTalk.background = bluePickDrawable
            binding.ivTalk.setColorFilter(getColor(requireContext(), R.color.white))
            binding.tvTalk.setTextColor(getColor(requireContext(), R.color.white))
        } else {
            binding.llTalk.background = blueUnPickDrawable
            binding.ivTalk.setColorFilter(getColor(requireContext(), R.color.blue))
            binding.tvTalk.setTextColor(getColor(requireContext(), R.color.blue))
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}