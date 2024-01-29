package com.idle.togeduck.view

import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.GridLayout
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.idle.togeduck.R
import com.idle.togeduck.databinding.ComponentBottomAppbarBinding
import com.idle.togeduck.databinding.FragmentBottomAppbarBinding
import com.idle.togeduck.databinding.FragmentEventListBinding
import com.idle.togeduck.util.Theme
import dagger.hilt.android.AndroidEntryPoint
import kotlin.math.log

@AndroidEntryPoint
class EventListFragment : Fragment() {
    private var _binding: FragmentEventListBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentEventListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

//        setThemeColor()
    }


    private fun setThemeColor() {
        //수정
//        val drawable = ContextCompat.getDrawable(requireContext(), R.drawable.shape_circle) as GradientDrawable
//        drawable.setColor(ContextCompat.getColor(requireContext(), Theme.theme.main100))
//        drawable.setStroke(2, ContextCompat.getColor(requireContext(), Theme.theme.main500))

    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

