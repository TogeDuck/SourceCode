package com.idle.togeduck.util

import com.idle.togeduck.event.model.Event
import com.idle.togeduck.main_map.view.EventKind
import com.naver.maps.geometry.LatLng
import ted.gun0912.clustering.clustering.TedClusterItem
import ted.gun0912.clustering.geometry.TedLatLng

// 네이버 지도 클러스터 데이터 객체
data class NaverItem(var position: LatLng) : TedClusterItem {
    override fun getTedLatLng(): TedLatLng {
        return TedLatLng(position.latitude, position.longitude)
    }

    var title: String? = null
    var snippet: String? = null
    var event: Event? = null
    var eventType:EventKind? = null

    constructor(lat: Double, lng: Double, event: Event, eventType: EventKind) : this(LatLng(lat, lng)) {
        title = null
        snippet = null
        this.event = event
        this.eventType = eventType
    }

    constructor(lat: Double, lng: Double, title: String?, snippet: String?, event: Event?) : this(
        LatLng(lat, lng)
    ) {
        this.title = title
        this.snippet = snippet
        this.event = event
    }
}
