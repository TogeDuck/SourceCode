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
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.idle.togeduck.R
import com.idle.togeduck.databinding.ComponentBottomAppbarBinding
import com.idle.togeduck.databinding.FragmentBottomAppbarBinding
import com.idle.togeduck.util.Theme
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
        setUpBackgroundRoundCorner()
        setUpBackgroundButtonIcon()
        setUpBottomText()
        setUpFloatingButton()
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

    private fun setUpBackgroundRoundCorner(){
        // Bottom Appbar Background
        val upperRoundCorner = ContextCompat.getDrawable(requireContext(), R.drawable.shape_upper_round_25) as GradientDrawable
        upperRoundCorner.setColor(ContextCompat.getColor(requireContext(), Theme.theme.main100))
        val background: ConstraintLayout = componentBinding.navBackground
        background.background = upperRoundCorner
    }

    private fun setUpBackgroundButtonIcon(){
        // Normal Buttons : Icons (Main 500)
        val icQuest: ImageView = componentBinding.icQuest
        val icList: ImageView = componentBinding.icList
        val icChat: ImageView = componentBinding.icChat
        val icMyrecord: ImageView = componentBinding.icMyrecord
        val icons: List<ImageView> = listOf(icQuest, icList, icChat, icMyrecord)
        for (icon in icons){
            icon.setColorFilter(ContextCompat.getColor(requireContext(), Theme.theme.main500))
        }
    }

    private fun setUpBottomText(){
        val textQuest: TextView = componentBinding.textQuest
        val textList: TextView = componentBinding.textList
        val textChat: TextView = componentBinding.textChat
        val textMyRecord: TextView = componentBinding.textMyrecord
        val texts: List<TextView> = listOf(textQuest, textList, textChat, textMyRecord)
        for (text in texts){
            text.setTextColor(ContextCompat.getColor(requireContext(), Theme.theme.main500))
        }
        textQuest.setText("Quest")
        textList.setText("List")
        textChat.setText("MyQuest")
        textMyRecord.setText("MyRecord")
    }

    private fun setUpFloatingButton(){
        // Floating Buttons : Buttons (Main 500)
        val fabQuest: LinearLayout = componentBinding.fabQuest
        val fabList: LinearLayout = componentBinding.fabList
        val fabChat: LinearLayout = componentBinding.fabChat
        val fabMyrecord: LinearLayout = componentBinding.fabMyrecord
        val circle = ContextCompat.getDrawable(requireContext(), R.drawable.shape_circle) as GradientDrawable
        circle.setColor(ContextCompat.getColor(requireContext(), Theme.theme.main500))
        val fabs: List<LinearLayout> = listOf(fabQuest,fabList,fabChat,fabMyrecord)
        for(fab in fabs){
            fab.background = circle;
        }
        componentBinding.buttonQuest.setOnClickListener { handleButtonClick(fabQuest, listOf(fabList, fabChat, fabMyrecord)) }
        componentBinding.buttonList.setOnClickListener { handleButtonClick(fabList, listOf(fabQuest,fabChat,fabMyrecord)) }
        componentBinding.buttonChat.setOnClickListener { handleButtonClick(fabChat, listOf(fabQuest, fabList, fabMyrecord)) }
        componentBinding.buttonMyrecord.setOnClickListener { handleButtonClick(fabMyrecord, listOf(fabQuest, fabList, fabChat)) }
    }
}

