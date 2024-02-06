package com.idle.togeduck.event.view.detail.detail_rv

import android.net.Uri
import com.idle.togeduck.event.model.Event

interface EventReview {
    fun changeLikeImage(event: Event)
    fun changeVisitImage(event: Event)
    //즐겨찾기 클릭 이벤트
    fun likeClick(event: Event)
    //방문 클릭 이벤트
    fun visitClick(event: Event)
    fun uriToFilePath(uri: Uri): String
    //리뷰 등록 이벤트
    //리뷰 수정, 삭제 이벤트
    //사진 등록
}