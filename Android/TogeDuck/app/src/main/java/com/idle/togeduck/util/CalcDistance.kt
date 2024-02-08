package com.idle.togeduck.util

import kotlin.math.asin
import kotlin.math.cos
import kotlin.math.pow
import kotlin.math.sin
import kotlin.math.sqrt

object CalcDistance {
    private const val R = 6372.8 * 1000

    fun getDistance(lat1: Double, lon1: Double, lat2: Double, lon2: Double): Int{
        val dLat = Math.toRadians(lat2 - lat1)
        val dLon = Math.toRadians(lon2 - lon1)
        val a = sin(dLat / 2).pow(2.0) + sin(dLon / 2).pow(2.0) * cos(Math.toRadians(lat1)) * cos(Math.toRadians(lat2))
        val c = 2 * asin(sqrt(a))
        return (R * c).toInt()
    }

    //================================================================
    // 라이브 정보 버튼
    // 시작 조건 ------------------------------------------------------
    // 오늘 날짜로 이벤트 리스트 한번 검색
    // 오늘 날짜로 선택된 아이돌의 생일 카페 리스트가 하나라도 있을 때
    // 기능 ----------------------------------------------------------
    // 웹소켓 이벤트 알림 구독
    // 웹소켓 위치정보 공유 구독
    // 종료 ----------------------------------------------------------
    // 웹소켓 전체 이벤트 구독 종료
    // 웹소켓 위치정보 공유 구독 종료
    //================================================================

    // ===============================================================
    // 투어 시작 버튼
    // 시작 조건 -------------------------------------------------------
    // 오늘 날짜로 이벤트 리스트 한번 검색
    // 오늘 날짜로 선택된 아이돌의 생일 카페 리스트가 하나라도 있을 때
    // 투어 id 가져오는 get 요청
    // 기능 ------------------------------------------------------------
    // 실시간 버튼 on으로 고정 (켜기)
    // 이벤트 리스트 오늘 껄로 고정해서 가져오기
    // 달력 기능 막기
    // 좌표 매번 가져오는 함수 실행
    //=================================================================
    // 실시간 좌표 가져오는 함수
    // 기능 ------------------------------------------------------------
    // 많이 이동했나 너무 많이 이동 했나
    // 좌표와 이벤트 리스트에서 거리를 모두 판단(On)
    // 10m 이내인 이벤트의 리스트 제작, 하나라도 있으면 팝업 -> 방문 체크 기능
    // 실시간 소켓으로 좌표 전송
    //=================================================================
    // 실시간 좌표 수신
    // 기능 ------------------------------------------------------------
    // 내 좌표인지 확인 (guid 확인)
    // 내 좌표가 아닌 경우, 좌표 표시 맵에서 업데이트


    //==================================================================
    // 투어 종료
    // 소켓



}