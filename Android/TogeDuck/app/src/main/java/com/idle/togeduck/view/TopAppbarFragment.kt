package com.idle.togeduck.view

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.idle.togeduck.databinding.ComponentTopAppbarBinding
import com.idle.togeduck.databinding.FragmentFavoriteSettingBinding
import com.idle.togeduck.databinding.FragmentTopAppbarBinding
import com.idle.togeduck.util.CalcStatusBarSize.getStatusBarHeightToDp
import com.idle.togeduck.util.DpPxUtil.dpToPx

class TopAppbarFragment : Fragment() {
    private var _binding: FragmentTopAppbarBinding? = null
    private val binding get() = _binding!!

    private var _topAppBarBinding: ComponentTopAppbarBinding? = null
    private val topAppbarBinding get() = _topAppBarBinding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTopAppbarBinding.inflate(inflater, container, false)
        _topAppBarBinding = binding.cta
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val statusDp = getStatusBarHeightToDp(requireContext())

        Log.d("로그", "TopAppbarFragment - onViewCreated() 호출됨 ${statusDp}")

        topAppbarBinding.llTopAppbar.setPadding(
            dpToPx(20, requireContext()),
            dpToPx(statusDp, requireContext()),
            dpToPx(20, requireContext()),
            dpToPx(10, requireContext())
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}