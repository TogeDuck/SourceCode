package com.idle.togeduck.util

import android.annotation.SuppressLint
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.view.Gravity
import android.view.WindowManager.LayoutParams
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.snackbar.Snackbar.SnackbarLayout
import com.idle.togeduck.QuestType
import com.idle.togeduck.R
import com.idle.togeduck.common.Theme
import com.idle.togeduck.databinding.FragmentMainBinding
import com.idle.togeduck.databinding.FragmentMapBinding
import com.idle.togeduck.util.DpPxUtil.dpToPx

object SnackBarFactory {
    @SuppressLint("RestrictedApi", "ShowToast")
    fun show(
        fragment: Fragment,
        bindi,
        type: String,
        time: Int = 2000
    ) {
        val context = fragment.requireContext()

        val snackBar = Snackbar.make(binding.mainLayout, "", time)
        val layoutId = R.layout.snack_bar_add_quest
        val customSnackView = fragment.layoutInflater.inflate(layoutId, null)

        snackBar.view.setBackgroundColor(Color.TRANSPARENT)

        val snackBarLayout = snackBar.view as SnackbarLayout

        snackBarLayout.setPadding(0, 0, 0, dpToPx(130, context))

        val snackBarMainLayout = customSnackView.findViewById<LinearLayout>(R.id.map_quest_alert)
        val snackBarIcon = customSnackView.findViewById<ImageView>(R.id.map_quest_alert_icon)
        val snackBarText = customSnackView.findViewById<TextView>(R.id.map_quest_alert_text)

        val layoutParams = snackBarLayout.layoutParams as FrameLayout.LayoutParams
        layoutParams.width = LayoutParams.WRAP_CONTENT
        layoutParams.height = LayoutParams.WRAP_CONTENT
        layoutParams.gravity = Gravity.CENTER_HORIZONTAL + Gravity.BOTTOM
        snackBarMainLayout.layoutParams = layoutParams

        val squareCircleShape = ContextCompat.getDrawable(context, R.drawable.shape_square_circle) as GradientDrawable
        squareCircleShape.setColor(getColor(context, Theme.theme.main100))
        squareCircleShape.setStroke(0, getColor(context, Theme.theme.main500))

        snackBarMainLayout.background = squareCircleShape

        val circleShape = ContextCompat.getDrawable(context, R.drawable.shape_square_circle) as GradientDrawable
        circleShape.setColor(getColor(context, R.color.green))

        snackBarIcon.background = circleShape
        circleShape.setStroke(0, getColor(context, R.color.green))
        snackBarIcon.setColorFilter(getColor(context, R.color.white))

        when(type) {
            QuestType.SHARE.toString() -> {
                circleShape.setColor(getColor(context, R.color.green))
                snackBarIcon.setImageResource(R.drawable.ic_share)
                snackBarText.text = "새로운 나눔 퀘스트가 등록되었습니다!"
            }
            QuestType.EXCHANGE.toString() -> {
                circleShape.setColor(getColor(context, R.color.yellow))
                snackBarIcon.setImageResource(R.drawable.ic_exchange)
                snackBarText.text = "새로운 교환 퀘스트가 등록되었습니다!"
            }
            else -> {
                circleShape.setColor(getColor(context, R.color.red))
                snackBarIcon.setImageResource(R.drawable.ic_person_white)
                snackBarText.text = "새로운 모집 퀘스트가 등록되었습니다!"
            }
        }

        snackBarLayout.addView(customSnackView, 0)
        snackBar.show()
    }
}