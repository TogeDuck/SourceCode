package com.idle.togeduck.quest.share.view.share_rv

import android.content.Context
import android.graphics.drawable.GradientDrawable
import android.view.View
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.idle.togeduck.R
import com.idle.togeduck.databinding.ItemQuestShareBinding
import com.idle.togeduck.common.Theme

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

    // 클래스 초기화 블럭
    // Layout에 OnClickListener 설정
    init {
        questShareLayout.setOnClickListener(this)
    }

    // 데이터 객체를 매개변수로 받아서 해당 데이터로 뷰 컴포넌트 업데이트
    fun binding(questShare: QuestShare, context: Context) {
        setTheme(questShare, context)
    }

    override fun onClick(v: View?) {
        when(v){
            questShareLayout -> {
                questDetail.myQuestShareClicked(bindingAdapterPosition)
            }
        }
    }

    private fun setTheme(questShare: QuestShare, context: Context) {
        val roundSmall = ContextCompat.getDrawable(context, R.drawable.shape_all_round_10) as GradientDrawable
        val roundLarge = ContextCompat.getDrawable(context, R.drawable.shape_all_round_10) as GradientDrawable
        val circle = ContextCompat.getDrawable(context, R.drawable.shape_circle) as GradientDrawable
        roundSmall.setColor(ContextCompat.getColor(context, Theme.theme.sub500)) // 추후 변경
        roundLarge.setColor(ContextCompat.getColor(context, Theme.theme.sub200))
        circle.setColor(ContextCompat.getColor(context, Theme.theme.sub500))
        circle.setStroke(0,0)
        image.background = roundSmall
        questShareLayout.background = roundLarge
        time.background = circle

        title.setText(questShare.title)
        content.setText(questShare.content)
        timeText.setText(questShare.time.toString())
    }
}