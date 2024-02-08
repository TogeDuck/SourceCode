package com.idle.togeduck.history.view

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.idle.togeduck.R
import com.idle.togeduck.common.RandomCupcake
import com.idle.togeduck.databinding.FragmentHistoryDetailBinding
import com.idle.togeduck.databinding.ItemHistoryBinding
import com.idle.togeduck.history.HistoryViewModel
import com.idle.togeduck.main_map.view.MapFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@AndroidEntryPoint
class HistoryDetailFragment : Fragment() {
    private var _binding: FragmentHistoryDetailBinding? = null
    private val binding get() = _binding!!

    private var _historyDetailCardBinding: ItemHistoryBinding? = null
    private val historyDetailCardBinding get() = _historyDetailCardBinding!!

    private val historyViewModel: HistoryViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentHistoryDetailBinding.inflate(layoutInflater, container, false)
        _historyDetailCardBinding = binding.historyDetailCard

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        historyViewModel.selectedHistory.observe(viewLifecycleOwner) { historyData ->
            historyDetailCardBinding.tvDate.text = historyData.date.toString()
            historyDetailCardBinding.tvMyRecord.text = historyData.historyName
            historyDetailCardBinding.ivMyRecordMainImg.setImageDrawable(
                ContextCompat.getDrawable(
                    requireContext(),
                    RandomCupcake.getImage()
                )
            )

            CoroutineScope(Dispatchers.IO).launch {
                historyViewModel.getHistory(historyData.historyId)
            }
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}