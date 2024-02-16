package com.idle.togeduck.event.view.detail.detail_rv

import android.net.Uri
import com.idle.togeduck.event.model.Event

interface EventReview {
    fun changeLikeImage(event: Event)
    fun changeVisitImage(event: Event)
    fun likeClick(event: Event)
}