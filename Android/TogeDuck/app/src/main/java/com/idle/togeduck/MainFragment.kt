package com.idle.togeduck

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.idle.togeduck.R
import com.idle.togeduck.databinding.FragmentMainBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainFragment : Fragment() {
    private var _binding: FragmentMainBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMainBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btn0.setOnClickListener {
            findNavController().navigate(R.id.action_mainFragment_to_mapFragment)
        }

        binding.btn1.setOnClickListener {
            findNavController().navigate(R.id.action_mainFragment_to_questTalkFragment)
        }

        binding.btn2.setOnClickListener {
            findNavController().navigate(R.id.action_mainFragment_to_eventDetailFragment)
        }

        binding.btn3.setOnClickListener {
            findNavController().navigate(R.id.action_mainFragment_to_FavoriteSettingFragment)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}