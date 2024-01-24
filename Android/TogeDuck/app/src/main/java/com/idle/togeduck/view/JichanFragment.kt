package com.idle.togeduck.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.idle.togeduck.R
import com.idle.togeduck.databinding.FragmentJichanBinding
import com.idle.togeduck.util.Theme
import com.idle.togeduck.util.TogeDuckColor
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class JichanFragment : Fragment() {
    private var _binding: FragmentJichanBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentJichanBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // 기기 설정 테마 값

        Theme.theme = TogeDuckColor.BTS

        binding.textView.setTextColor(ContextCompat.getColor(requireContext(), Theme.theme.color_main_100))
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}