package com.idle.togeduck.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.idle.togeduck.databinding.ComponentBottomSheetBinding
import com.idle.togeduck.databinding.FragmentBottomSheetBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class BottomSheetFragment : Fragment() {
    private var _binding: FragmentBottomSheetBinding? = null
    private val binding get() = _binding!!
    private var _componentBinding: ComponentBottomSheetBinding? = null
    private val componentBinding get() = _componentBinding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentBottomSheetBinding.inflate(inflater, container, false)
        _componentBinding = binding.bsFragment
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        var sheet = componentBinding.bottomSheet

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        _componentBinding = null
    }
}