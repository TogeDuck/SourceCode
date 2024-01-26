package com.idle.togeduck.view

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.GridLayout
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.idle.togeduck.R
import com.idle.togeduck.databinding.ComponentBottomAppbarBinding
import com.idle.togeduck.databinding.FragmentBottomAppbarBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlin.math.log

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

        val fabQuest: LinearLayout = componentBinding.fabQuest
        val fabList: LinearLayout = componentBinding.fabList
        val fabChat: LinearLayout = componentBinding.fabChat
        val fabMyrecord: LinearLayout = componentBinding.fabMyrecord

        componentBinding.buttonQuest.setOnClickListener { handleButtonClick(fabQuest, listOf(fabList, fabChat, fabMyrecord)) }
        componentBinding.buttonList.setOnClickListener { handleButtonClick(fabList, listOf(fabQuest,fabChat,fabMyrecord)) }
        componentBinding.buttonChat.setOnClickListener { handleButtonClick(fabChat, listOf(fabQuest, fabList, fabMyrecord)) }
        componentBinding.buttonMyrecord.setOnClickListener { handleButtonClick(fabMyrecord, listOf(fabQuest, fabList, fabChat)) }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        _componentBinding = null
    }

    private fun handleButtonClick (
        showFab: LinearLayout,
        hideList: List<LinearLayout>
    ) {
        Log.d("검증", "buttonclick func")
        for (fab in hideList) {
            fab.visibility = View.INVISIBLE
        }
        showFab.visibility = View.VISIBLE
    }
}