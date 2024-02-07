package com.idle.togeduck.quest.recruit.view.recruit_rv

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.drawable.GradientDrawable
import android.os.Build
import android.view.View
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.idle.togeduck.R
import com.idle.togeduck.common.RandomCupcake
import com.idle.togeduck.databinding.ItemQuestRecruitBinding
import com.idle.togeduck.common.Theme
import com.idle.togeduck.quest.exchange.model.Exchange
import com.idle.togeduck.quest.recruit.model.Recruit
import com.idle.togeduck.util.getColor
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.toJavaLocalDateTime
import java.time.ZoneOffset
import java.util.Timer
import kotlin.concurrent.timer

class QuestRecruitViewHolder(
    binding: ItemQuestRecruitBinding,
    private val iQuestRecruit: IQuestRecruit
) : RecyclerView.ViewHolder(binding.root), View.OnClickListener {
    private val mainLayout = binding.llMainLayout
    private val ivMainIcon = binding.ivMainIcon
    private val tvTitle = binding.tvTitle
    private val ivPersonIcon = binding.ivPersonIcon
    private val tvPersonCnt = binding.tvPersonCnt
    private val tvEnter = binding.tvEnter
    private val tvTimer = binding.tvTimer

    private var questRecruit: Recruit? = null

    private var mTimer = Timer()
    var elapsedTimeInSeconds = 1

    init {
        tvEnter.setOnClickListener(this)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun bind(recruit: Recruit, context: Context) {
        this.questRecruit = recruit
        configurePassedTime(recruit.expiredAt)
        setIcon(context)
        setTheme(recruit, context)
        timerTask(recruit,context)
        tvTitle.text = recruit.title
        tvPersonCnt.text = "${recruit.current} / ${recruit.maximum}"
    }

    private fun setIcon(context: Context){
        val whiteCircleDrawable = ContextCompat.getDrawable(context, R.drawable.shape_circle) as GradientDrawable
        whiteCircleDrawable.setColor(getColor(context, R.color.white))
        whiteCircleDrawable.setStroke(0, getColor(context, Theme.theme.main500))
        ivMainIcon.background = whiteCircleDrawable
        ivMainIcon.setImageDrawable(ContextCompat.getDrawable(context, RandomCupcake.getImage()))
    }

    private fun setTheme(questRecruit: Recruit, context: Context) {
        if(elapsedTimeInSeconds <= 0){
            iQuestRecruit.removeItem(questRecruit)
        }

        val greenCircleDrawable = ContextCompat.getDrawable(context, R.drawable.shape_circle) as GradientDrawable
        greenCircleDrawable.setColor(getColor(context, R.color.green))
        greenCircleDrawable.setStroke(0, getColor(context, Theme.theme.main500))
        tvEnter.background = greenCircleDrawable
        tvEnter.setTextColor(getColor(context, R.color.white))

        val squareCircleDrawable = ContextCompat.getDrawable(context, R.drawable.shape_square_circle) as GradientDrawable
        squareCircleDrawable.setColor(getColor(context, Theme.theme.sub200))
        squareCircleDrawable.setStroke(0, getColor(context, Theme.theme.main500))
        mainLayout.background = squareCircleDrawable

        val main500CircleDrawable = ContextCompat.getDrawable(context, R.drawable.shape_circle) as GradientDrawable
        main500CircleDrawable.setColor(getColor(context, Theme.theme.sub500))
        main500CircleDrawable.setStroke(0, getColor(context, Theme.theme.main500))
        tvTimer.background = main500CircleDrawable
        tvTimer.setTextColor(getColor(context, R.color.white))
        // 타이머 텍스트
        tvTimer.setText(secondsToString())
    }

    override fun onClick(view: View?) {
        when (view) {
            tvEnter -> questRecruit?.let { iQuestRecruit.joinBtnClicked(it) }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun configurePassedTime(expiredAt: LocalDateTime) {
        val currentTime = java.time.LocalDateTime.now()
        val durationInMillis = expiredAt.toJavaLocalDateTime().toInstant(ZoneOffset.UTC).toEpochMilli() - currentTime.toInstant(
            ZoneOffset.UTC).toEpochMilli()
        elapsedTimeInSeconds = (durationInMillis / 1000).toInt()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    @Synchronized
    fun timerTask(questRecruit: Recruit, context: Context) {
        val name = "ExchangeTimer"
        val period = 1000L

        mTimer = timer(name = name, period = period){
            elapsedTimeInSeconds--
            if (elapsedTimeInSeconds >= 0) {
                setThemeOnUiThread(questRecruit, context)
            } else {
                mTimer.cancel()
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun setThemeOnUiThread(questRecruit: Recruit, context: Context) {
        mainLayout.post {
            setTheme(questRecruit, context)
        }
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