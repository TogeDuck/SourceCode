package com.idle.togeduck.view.quest_share

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.idle.togeduck.databinding.ItemQuestShareBinding
import com.idle.togeduck.model.QuestShare
import com.idle.togeduck.util.TogeDuckDiffUtil

class QuestShareListAdapter (
    private val iquestDetail: IQuestShareDetail,
    private val context: Context
)
    : ListAdapter<QuestShare, QuestShareViewHolder>(TogeDuckDiffUtil.questShareDiffUtilCallback) {

    // ListAdapter ----------------------------------------------------
    // 데이터 리스트, ViewHolder를 매개변수로 받음
    // RecyclerView.Adapter에 비해 데이터 리스트를 편리하게 해주는 클래스
    // <제네릭> : 제네릭 타입 파라미터 정의 (데이터 타입, 뷰홀더 타입)
    // DiffUtil Callback : DiffUtil.ItemCallback을 구현한 객체, 리스트의 아이템이 변경되었을때 업데이트 최적화 위함

    // Adapter ---------------------------------------------------------
    // ListAdapter를 상속 (androidx.recyclerview.widget.ListAdapter 주의)
    // 매개변수 : 인터페이스 객체 -> 터치 콜백, context -> 안드로이드 시스템에서 어플리케이션 환경에 대한 정보 제공

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): QuestShareViewHolder {
        val binding = ItemQuestShareBinding.inflate(LayoutInflater.from(parent.context))
        return QuestShareViewHolder(binding, iquestDetail)
    }

    override fun onBindViewHolder(holder: QuestShareViewHolder, position: Int) {
        holder.binding(getItem(position),context)
    }
}