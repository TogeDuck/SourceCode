package com.idle.togeduck.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.idle.togeduck.R
import com.idle.togeduck.databinding.FragmentJiwooBinding
import com.idle.togeduck.databinding.FragmentMainBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class JiwooFragment : Fragment() {
    private var _binding: FragmentJiwooBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentJiwooBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

//        binding.jiwoo_btn.setOnClickListener {
//
//        }


    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}