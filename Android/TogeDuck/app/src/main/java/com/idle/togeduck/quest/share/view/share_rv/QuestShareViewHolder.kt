package com.idle.togeduck.quest.share.view.share_rv

import android.content.Context
import android.graphics.drawable.GradientDrawable
import android.os.Build
import android.util.Log
import android.view.View
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.idle.togeduck.R
import com.idle.togeduck.databinding.ItemQuestShareBinding
import com.idle.togeduck.common.Theme
import com.idle.togeduck.quest.share.model.Share
import com.idle.togeduck.util.DpPxUtil
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.toJavaLocalDateTime
import java.time.ZoneOffset
import java.util.Timer
import java.util.TimerTask
import kotlin.concurrent.timer


class QuestShareViewHolder (
    binding: ItemQuestShareBinding,
    private var questDetail: IQuestShareDetail
) : RecyclerView.ViewHolder(binding.root), View.OnClickListener{
    // ViewHolder
    // RecyclerView의 아이템을 위한 클래스
    // RecyclerView.ViewHolder 클래스 상속
    // 아이템 뷰의 레이아웃, 데이터바인딩, 이벤트 리스터 관리

    // 생성자
    // bidding : 아이템 뷰의 참조 관리, XML 레이아웃에서 정의된 뷰의 참조를 컴파일 타임에 생성
    // questDetail : 인터페이스 타입 객체, 사용자 상호 작용(터치)에 대한 콜백 처리

    // XML Layout 및 요소들 id로 연결
    private val questShareLayout = binding.itemQuestShare
    private val image = binding.shareImage
    private val title = binding.shareTitle
    private val content = binding.shareContent
    private val time = binding.shareTimer
    private val timeText = binding.timerText

    private var questShare: Share? = null
    private var context: Context? = null

    private var mTimer = Timer()
    var elapsedTimeInSeconds = 0

    // 클래스 초기화 블럭
    // Layout에 OnClickListener 설정
    init {
        questShareLayout.setOnClickListener(this)
    }

    // 데이터 객체를 매개변수로 받아서 해당 데이터로 뷰 컴포넌트 업데이트
    @RequiresApi(Build.VERSION_CODES.O)
    fun binding(questShare: Share, context: Context) {
        this.questShare = questShare
        configurePassedTime(questShare.createdAt)
        setImage(questShare, context)
        setTheme(questShare,context)
        timerTask(questShare,context)
    }

    override fun onClick(v: View?) {
        when(v){
            questShareLayout -> {
                questShare?.let { questDetail.myQuestShareClicked(it) }
            }
        }
    }

    private fun setImage(questShare: Share, context: Context){
        // Image Setting
        Glide
            .with(image)
            .load(questShare.image)
            .thumbnail(
                Glide.with(image).load(questShare.image).override(200,200)
            )
            .transform(CenterCrop(),RoundedCorners(DpPxUtil.dpToPx(10, context)))
            .override(500, 500)
            .into(image)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun configurePassedTime(createdAt: LocalDateTime) {
        val currentTime = java.time.LocalDateTime.now()
        Log.d("시간",currentTime.toString())
        val durationInMillis = currentTime.toInstant(ZoneOffset.UTC).toEpochMilli() - createdAt.toJavaLocalDateTime().toInstant(
            ZoneOffset.UTC).toEpochMilli()
        elapsedTimeInSeconds = (durationInMillis / 1000).toInt()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    @Synchronized
    fun timerTask(questShare: Share, context: Context) {
        val name = "MyTimer"
        val period = 1000L

        mTimer = timer(name = name, period = period){
            elapsedTimeInSeconds++
            Log.d("타이머 진행",elapsedTimeInSeconds.toString())

            if (elapsedTimeInSeconds < 30 * 60 ) {
                setThemeOnUiThread(questShare, context)
            } else {
                mTimer.cancel() // 타이머 종료
            }
        }
    }
    @RequiresApi(Build.VERSION_CODES.O)
    private fun setThemeOnUiThread(questShare: Share, context: Context) {
        questShareLayout.post {
            setTheme(questShare, context)
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun setTheme(questShare: Share, context: Context) {
        // Shape Load
        val roundLarge = ContextCompat.getDrawable(context, R.drawable.shape_all_round_10) as GradientDrawable
        val circle = ContextCompat.getDrawable(context, R.drawable.shape_circle) as GradientDrawable

        // Color Settings
        circle.setColor(ContextCompat.getColor(context, Theme.theme.sub500))
        circle.setStroke(0,0)

        // Time Setting
        when{
            elapsedTimeInSeconds/60 < 5 -> {roundLarge.setColor(ContextCompat.getColor(context, Theme.theme.sub300))
                circle.setColor(ContextCompat.getColor(context, Theme.theme.sub500))
                Log.d("UI 업데이트 1번", (elapsedTimeInSeconds/60).toString())
            }
            elapsedTimeInSeconds/60 < 10 -> {roundLarge.setColor(ContextCompat.getColor(context, Theme.theme.sub200))
                circle.setColor(ContextCompat.getColor(context, Theme.theme.sub400))
                Log.d("UI 업데이트 2번", (elapsedTimeInSeconds/60).toString())
            }
            elapsedTimeInSeconds/60 < 20 -> {roundLarge.setColor(ContextCompat.getColor(context, Theme.theme.sub100))
                circle.setColor(ContextCompat.getColor(context, Theme.theme.sub300))
                Log.d("UI 업데이트 3번", (elapsedTimeInSeconds/60).toString())
            }
            elapsedTimeInSeconds/60 < 30 -> {roundLarge.setColor(ContextCompat.getColor(context, Theme.theme.sub100))
                circle.setColor(ContextCompat.getColor(context, Theme.theme.sub200))
                Log.d("UI 업데이트 4번", (elapsedTimeInSeconds/60).toString())
            }
            else -> {roundLarge.setColor(ContextCompat.getColor(context, Theme.theme.sub100))
                time.visibility = View.INVISIBLE
                timeText.visibility = View.INVISIBLE
                Log.d("UI 업데이트 5번", (elapsedTimeInSeconds/60).toString())
            }
        }

        // To Components
        questShareLayout.background = roundLarge
        time.background = circle
        title.setText(questShare.title)
        content.setText(questShare.content)
        timeText.setText(secondsToString())
    }

    fun secondsToString(): String{
        val minutes = elapsedTimeInSeconds/60
        val seconds = elapsedTimeInSeconds%60
        return String.format("%02d:%02d", minutes, seconds)
    }
}