package com.idle.togeduck.myquest.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.idle.togeduck.common.Theme
import com.idle.togeduck.databinding.FragmentMyQuestBinding
import com.idle.togeduck.myquest.MyQuestViewModel
import com.idle.togeduck.myquest.model.MyQuest
import com.idle.togeduck.myquest.view.myquest_rv.IMyQuestDetail
import com.idle.togeduck.myquest.view.myquest_rv.MyQuestAdapter
import com.idle.togeduck.util.TogeDuckItemDecoration
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MyQuestFragment : Fragment(), IMyQuestDetail {
    private var _binding : FragmentMyQuestBinding? = null
    private val binding get() = _binding!!
    val myQuestViewModel: MyQuestViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMyQuestBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Adapter 인스턴스 생성 (OnClick 인터페이스 구현체, Context 제공 필요)
        val recycleView = binding.myquestRv
        val myQuestAdapter = MyQuestAdapter(this, requireContext())
        recycleView.adapter = myQuestAdapter
        recycleView.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, true).apply { stackFromEnd = true }
        binding.myqeustText.setTextColor(ContextCompat.getColor(requireContext(), Theme.theme.main500))
        // 간격 설정
        recycleView.addItemDecoration(TogeDuckItemDecoration(15,0))

        myQuestViewModel.myQuestList.observe(viewLifecycleOwner) {list ->
            myQuestAdapter.submitList(list)
        }
        CoroutineScope(Dispatchers.IO).launch {
            myQuestViewModel.getMyQuestList()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun myQuestClicked(myQuest: MyQuest) {
        myQuestViewModel.setSelected(myQuest)
    }
}