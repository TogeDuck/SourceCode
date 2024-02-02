package com.idle.togeduck.test

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.idle.togeduck.databinding.FragmentJichanBinding
import com.idle.togeduck.util.CalcStatusBarSize.getStatusBarHeightToPx
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

        val statusBarPx = getStatusBarHeightToPx(requireContext())

        binding.mainLayout.setPadding(0, statusBarPx, 0, 0)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}