package com.idle.togeduck.event.view.list.list_rv

interface EventInfo {
    //item 전체 영역 클릭했을 때 해당 상세화면으로 이동
    fun eventClicked(position: Int, type: Int)

    //즐겨찾기 버튼 클릭 시
    fun likeClick(position: Int, type: Int)
}