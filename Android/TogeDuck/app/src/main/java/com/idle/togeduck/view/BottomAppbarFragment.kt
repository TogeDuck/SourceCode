package com.idle.togeduck.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.GridLayout
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.idle.togeduck.R
import com.idle.togeduck.databinding.ComponentBottomAppbarBinding
import com.idle.togeduck.databinding.FragmentBottomAppbarBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class BottomAppbarFragment : Fragment() {
    private var _binding: FragmentBottomAppbarBinding? = null
    private val binding get() = _binding!!
    private var _componentBinding: ComponentBottomAppbarBinding? = null
    private val componentBinding get() = _componentBinding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentBottomAppbarBinding.inflate(inflater, container, false)
        _componentBinding = binding.appbar
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        var buttonQuest = componentBinding.buttonQuest

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        _componentBinding = null
    }

    private fun handleFabClick(
        clickedFab: FloatingActionButton,
        hideList: List<FloatingActionButton>
    ) {
        // 클릭된 버튼 이외의 버튼 숨기기
        for (fab in hideList) {
            fab.visibility = View.GONE
        }

        // 클릭된 버튼 보이기
        clickedFab.visibility = View.VISIBLE
    }
}