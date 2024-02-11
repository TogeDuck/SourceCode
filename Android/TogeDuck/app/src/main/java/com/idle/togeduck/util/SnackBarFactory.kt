package com.idle.togeduck.util

import android.annotation.SuppressLint
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.snackbar.Snackbar.SnackbarLayout
import com.idle.togeduck.R
import com.idle.togeduck.common.Theme
import com.idle.togeduck.databinding.FragmentMainBinding
import com.idle.togeduck.databinding.FragmentMapBinding

object SnackBarFactory {
    @SuppressLint("RestrictedApi", "ShowToast")
    fun show(
        fragment: Fragment,
//        binding: FragmentMapBinding,
        binding: FragmentMainBinding,
        text: String,
        time: Int = 2000
    ) {
        val context = fragment.requireContext()

        val snackBar = Snackbar.make(binding.mainLayout, "", time)
        val layoutId = R.layout.snack_bar_add_quest
        val customSnackView = fragment.layoutInflater.inflate(layoutId, null)

        snackBar.view.setBackgroundColor(Color.TRANSPARENT)

        val snackBarLayout = snackBar.view as SnackbarLayout


        snackBarLayout.setPadding(0, 0, 0, 130)

        val snackBarMainLayout = customSnackView.findViewById<LinearLayout>(R.id.map_quest_alert)
        val snackBarIcon = customSnackView.findViewById<ImageView>(R.id.map_quest_alert_icon)
        val snackBarText = customSnackView.findViewById<TextView>(R.id.map_quest_alert_text)

        val squareCircleShape = ContextCompat.getDrawable(context, R.drawable.shape_square_circle) as GradientDrawable
        squareCircleShape.setColor(getColor(context, Theme.theme.main100))
        squareCircleShape.setStroke(0, getColor(context, Theme.theme.main500))

        snackBarMainLayout.background = squareCircleShape

        val circleShape = ContextCompat.getDrawable(context, R.drawable.shape_square_circle) as GradientDrawable
        circleShape.setColor(getColor(context, R.color.green))
        circleShape.setStroke(0, getColor(context, R.color.green))

        snackBarIcon.background = circleShape
        snackBarIcon.setColorFilter(getColor(context, R.color.white))

        snackBarText.text = text

        snackBarLayout.addView(customSnackView, 0)
        snackBar.show()
    }
}