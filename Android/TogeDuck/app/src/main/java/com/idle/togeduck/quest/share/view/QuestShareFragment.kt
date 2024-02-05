package com.idle.togeduck.quest.share.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.idle.togeduck.databinding.FragmentQuestShareBinding
import com.idle.togeduck.quest.share.ShareViewModel
import com.idle.togeduck.quest.share.model.Share
import com.idle.togeduck.util.TogeDuckItemDecoration
import com.idle.togeduck.quest.share.view.share_rv.IQuestShareDetail
import com.idle.togeduck.quest.share.view.share_rv.QuestShareDialog
import com.idle.togeduck.quest.share.view.share_rv.QuestShareListAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@AndroidEntryPoint
class QuestShareFragment : Fragment(), IQuestShareDetail {
    private var _binding: FragmentQuestShareBinding? = null
    private val binding get() = _binding!!
    val shareViewModel: ShareViewModel by activityViewModels()
//    val eventViewModel: EventViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentQuestShareBinding.inflate(inflater, container, false)
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Adapter 인스턴스 생성 (OnClick 인터페이스 구현체, Context 제공 필요)
        val recycleView = binding.questShareRecycle
        val questShareAdapter = QuestShareListAdapter(this, requireContext())
        recycleView.adapter = questShareAdapter
        recycleView.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, true).apply { stackFromEnd = true }

        // 간격 설정
        recycleView.addItemDecoration(TogeDuckItemDecoration(15,0))

        shareViewModel.shareList.observe(viewLifecycleOwner) {list ->
            questShareAdapter.submitList(list)
        }
        CoroutineScope(Dispatchers.IO).launch {
            shareViewModel.getShareList(0,1,10000)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun myQuestShareClicked(questShare: Share) {
        showDialog(questShare)
    }

    fun showDialog(questShare: Share) {
        val dialog = QuestShareDialog(questShare)
        val fragmentManager = childFragmentManager
        dialog.show(fragmentManager, "QuestShareDialog")
    }
}