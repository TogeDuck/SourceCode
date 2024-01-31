package com.idle.togeduck.view

import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.idle.togeduck.R
import com.idle.togeduck.databinding.FragmentMyRecordBinding
import com.idle.togeduck.model.QuestShare
import com.idle.togeduck.util.Theme
import com.idle.togeduck.util.TogeDuckItemDecoration
import com.idle.togeduck.util.getColor
import com.idle.togeduck.view.my_record.IMyRecord
import com.idle.togeduck.view.my_record.MyRecordAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MyRecordFragment : Fragment(), IMyRecord {
    private var _binding: FragmentMyRecordBinding? = null
    private val binding get() = _binding!!

    private lateinit var myRecordAdapter: MyRecordAdapter

    private val tempList = listOf(
        QuestShare("2024/01/31", "방탄소년단 뷔 생일 카페를 투어했어요!", "https://search.pstatic.net/common?type=b&size=144&expire=1&refresh=true&quality=100&direct=true&src=http%3A%2F%2Fsstatic.naver.net%2Fpeople%2F74%2F202212051555381451.png", 0),
        QuestShare("2024/01/30", "방탄소년단 뷔 생일 카페를 투어했어요!", "https://search.pstatic.net/common?type=b&size=144&expire=1&refresh=true&quality=100&direct=true&src=http%3A%2F%2Fsstatic.naver.net%2Fpeople%2F74%2F202212051555381451.png", 0),
        QuestShare("2024/01/29", "방탄소년단 뷔 생일 카페를 투어했어요!", "https://search.pstatic.net/common?type=b&size=144&expire=1&refresh=true&quality=100&direct=true&src=http%3A%2F%2Fsstatic.naver.net%2Fpeople%2F74%2F202212051555381451.png", 0),
        QuestShare("2024/01/28", "방탄소년단 뷔 생일 카페를 투어했어요!", "https://search.pstatic.net/common?type=b&size=144&expire=1&refresh=true&quality=100&direct=true&src=http%3A%2F%2Fsstatic.naver.net%2Fpeople%2F74%2F202212051555381451.png", 0),
        QuestShare("2024/01/27", "방탄소년단 뷔 생일 카페를 투어했어요!", "https://search.pstatic.net/common?type=b&size=144&expire=1&refresh=true&quality=100&direct=true&src=http%3A%2F%2Fsstatic.naver.net%2Fpeople%2F74%2F202212051555381451.png", 0),
        QuestShare("2024/01/26", "방탄소년단 뷔 생일 카페를 투어했어요!", "https://search.pstatic.net/common?type=b&size=144&expire=1&refresh=true&quality=100&direct=true&src=http%3A%2F%2Fsstatic.naver.net%2Fpeople%2F74%2F202212051555381451.png", 0),
        QuestShare("2024/01/25", "방탄소년단 뷔 생일 카페를 투어했어요!", "https://search.pstatic.net/common?type=b&size=144&expire=1&refresh=true&quality=100&direct=true&src=http%3A%2F%2Fsstatic.naver.net%2Fpeople%2F74%2F202212051555381451.png", 0),
        QuestShare("2024/01/24", "방탄소년단 뷔 생일 카페를 투어했어요!", "https://search.pstatic.net/common?type=b&size=144&expire=1&refresh=true&quality=100&direct=true&src=http%3A%2F%2Fsstatic.naver.net%2Fpeople%2F74%2F202212051555381451.png", 0),
        QuestShare("2024/01/23", "방탄소년단 뷔 생일 카페를 투어했어요!", "https://search.pstatic.net/common?type=b&size=144&expire=1&refresh=true&quality=100&direct=true&src=http%3A%2F%2Fsstatic.naver.net%2Fpeople%2F74%2F202212051555381451.png", 0),
        QuestShare("2024/01/22", "방탄소년단 뷔 생일 카페를 투어했어요!", "https://search.pstatic.net/common?type=b&size=144&expire=1&refresh=true&quality=100&direct=true&src=http%3A%2F%2Fsstatic.naver.net%2Fpeople%2F74%2F202212051555381451.png", 0),
        QuestShare("2024/01/21", "방탄소년단 뷔 생일 카페를 투어했어요!", "https://search.pstatic.net/common?type=b&size=144&expire=1&refresh=true&quality=100&direct=true&src=http%3A%2F%2Fsstatic.naver.net%2Fpeople%2F74%2F202212051555381451.png", 0),
        QuestShare("2024/01/20", "방탄소년단 뷔 생일 카페를 투어했어요!", "https://search.pstatic.net/common?type=b&size=144&expire=1&refresh=true&quality=100&direct=true&src=http%3A%2F%2Fsstatic.naver.net%2Fpeople%2F74%2F202212051555381451.png", 0),
    )

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMyRecordBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setRecyclerView()
        setTheme()

        myRecordAdapter.submitList(tempList)
    }

    private fun setRecyclerView() {
        myRecordAdapter = MyRecordAdapter(this, requireContext())

        binding.rvMyRecord.apply {
            addItemDecoration(TogeDuckItemDecoration(10, 0))
            adapter = myRecordAdapter
            layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, true)
                    .apply { stackFromEnd = true }
        }
    }

    private fun setTheme() {
        binding.tvMyCakeCnt.setTextColor(getColor(requireContext(), Theme.theme.sub500))

        val circleDrawable = ContextCompat.getDrawable(requireContext(), R.drawable.shape_circle) as GradientDrawable
        circleDrawable.setColor(getColor(requireContext(), Theme.theme.main500))
        circleDrawable.setStroke(0, Theme.theme.main500)
        binding.tvMyThemeContent.background = circleDrawable

        val strokeCircleDrawable = ContextCompat.getDrawable(requireContext(), R.drawable.shape_circle) as GradientDrawable
        strokeCircleDrawable.setColor(getColor(requireContext(), R.color.white))
        strokeCircleDrawable.setStroke(4, getColor(requireContext(), Theme.theme.main500))
        binding.ivThemeDraw.background = strokeCircleDrawable

        binding.tvVisited.setTextColor(getColor(requireContext(), Theme.theme.main500))
    }

    override fun recordClicked(position: Int) {
        Log.d("로그", "MyRecordFragment - recordClicked() 호출됨 $position")
    }

    override fun onDestroyView() {
        super.onDestroyView()
    }
}