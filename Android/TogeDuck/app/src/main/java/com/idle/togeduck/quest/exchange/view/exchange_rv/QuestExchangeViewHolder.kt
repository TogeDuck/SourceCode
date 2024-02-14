package com.idle.togeduck.quest.exchange.view.exchange_rv

import android.content.Context
import android.graphics.drawable.GradientDrawable
import android.os.Build
import android.util.Log
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.idle.togeduck.R
import com.idle.togeduck.databinding.ItemQuestExchangeBinding
import com.idle.togeduck.util.DpPxUtil
import com.idle.togeduck.common.ScreenSize
import com.idle.togeduck.common.Theme
import com.idle.togeduck.quest.exchange.model.Exchange
import com.idle.togeduck.quest.share.model.Share
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.toJavaLocalDateTime
import java.time.ZoneOffset
import java.util.Timer
import kotlin.concurrent.timer

class QuestExchangeViewHolder(
    binding: ItemQuestExchangeBinding,
    private var questExchangeDetail: IQuestExchangeDetail,
) : RecyclerView.ViewHolder(binding.root), View.OnClickListener {

    private val questExchangeLayout = binding.questExchangeItem
    private val image = binding.questExchangeImage
    private val timerText = binding.questExchangeTimerText

    private var questExchange: Exchange? = null

    private var mTimer = Timer()
    var elapsedTimeInSeconds = 1

    init {
        questExchangeLayout.setOnClickListener(this)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun binding(questExchange: Exchange, context: Context, spanCount: Int) {
        this.questExchange = questExchange
        configurePassedTime(questExchange.expiredAt)
        setImage(questExchange, context)
        setTheme(questExchange, context)
        setWidth(context, spanCount)
        timerTask(questExchange, context)
    }

    private fun setWidth(context: Context, spanCount: Int) {
        val newSize = DpPxUtil.dpToPx(ScreenSize.widthDp - 50 - (spanCount - 1) * 10, context) / spanCount
        val layoutParams = questExchangeLayout.layoutParams as ViewGroup.LayoutParams
        layoutParams.width = newSize
        layoutParams.height = newSize
        questExchangeLayout.layoutParams = layoutParams
    }

    override fun onClick(v: View?) {
        when(v){
            questExchangeLayout -> {
                questExchange?.let { questExchangeDetail.myQuestExchangeClicked(it) }
            }
        }
    }

    private fun setImage(questExchange: Exchange, context: Context){
        // Image Setting
        Glide
            .with(image)
            .load(questExchange.image)
            .thumbnail(
                Glide.with(image).load(questExchange.image).override(200,200)
            )
            .transform(CenterCrop(), RoundedCorners(DpPxUtil.dpToPx(15, context)))
            .override(500, 500)
            .into(image)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun configurePassedTime(expiredAt: LocalDateTime) {
        val expiredChanged = expiredAt.toJavaLocalDateTime().plusHours(9)
        val currentTime = java.time.LocalDateTime.now()
        val durationInMillis = expiredChanged.toInstant(ZoneOffset.UTC).toEpochMilli() - currentTime.toInstant(ZoneOffset.UTC).toEpochMilli()
        elapsedTimeInSeconds = (durationInMillis / 1000).toInt()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    @Synchronized
    fun timerTask(questExchange: Exchange, context: Context) {
        val name = "ExchangeTimer"
        val period = 1000L

        mTimer = timer(name = name, period = period){
            elapsedTimeInSeconds--
            if (elapsedTimeInSeconds >= 0) {
                setThemeOnUiThread(questExchange, context)
            } else {
                mTimer.cancel()
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun setThemeOnUiThread(questExchange: Exchange, context: Context) {
        questExchangeLayout.post {
            setTheme(questExchange, context)
        }
    }

    private fun setTheme(questExchange: Exchange, context: Context) {
//        if(elapsedTimeInSeconds <= 0){
//            questExchangeDetail.removeItemFromViewModel(questExchange)
//        }

        val roundSmall =
            ContextCompat.getDrawable(context, R.drawable.shape_all_round_10) as GradientDrawable
        val rectangleCircle =
            ContextCompat.getDrawable(context, R.drawable.shape_square_circle) as GradientDrawable
        roundSmall.setStroke(0, 0)
        rectangleCircle.setStroke(0, 0)
        questExchangeLayout.background = roundSmall

        // Timer Update Logic
        when{
            elapsedTimeInSeconds/60 < 2 -> {
                rectangleCircle.setColor(ContextCompat.getColor(context, Theme.theme.sub200))
            }
            elapsedTimeInSeconds/60 < 5 -> {
                rectangleCircle.setColor(ContextCompat.getColor(context, Theme.theme.sub300))
            }
            elapsedTimeInSeconds/60 < 10 -> {
                rectangleCircle.setColor(ContextCompat.getColor(context, Theme.theme.sub400))
            }
            else -> {
                rectangleCircle.setColor(ContextCompat.getColor(context, Theme.theme.sub500))
            }
        }
        timerText.background = rectangleCircle
        timerText.setText(secondsToString()) // 추후 수정
    }

    fun secondsToString(): String{
        val minutes = elapsedTimeInSeconds/60
        val seconds = elapsedTimeInSeconds%60
        if(minutes>60){
            return "1시간 이상"
        }
        else{
            return String.format("%02d : %02d", minutes, seconds)
        }
    }
}