package com.idle.togeduck.util

import android.annotation.SuppressLint
import android.graphics.Color
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.snackbar.Snackbar.SnackbarLayout
import com.idle.togeduck.R
import com.idle.togeduck.databinding.FragmentMapBinding

class SnackBarFactory {
    @SuppressLint("RestrictedApi")
    fun make(
        fragment: Fragment,
        binding: FragmentMapBinding,
        text: String,
        time: Int = 2000
    ) {
        val snackBar = Snackbar.make(binding.mainLayout, "", time)
        val layoutId = R.layout.snack_bar_add_quest
        val customSnackView = fragment.layoutInflater.inflate(layoutId, null)

        snackBar.view.setBackgroundColor(Color.TRANSPARENT)

        val snackBarLayout = snackBar.view as SnackbarLayout

        snackBarLayout.setPadding(0, 0, 0, 0)

        val snackBarIcon = customSnackView.findViewById<ImageView>(R.id.map_quest_alert_icon)
        val snackBarText = customSnackView.findViewById<TextView>(R.id.map_quest_alert_text)

        snackBarText.text = text
    }
}