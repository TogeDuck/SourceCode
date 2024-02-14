package com.idle.togeduck.fcm

import androidx.lifecycle.MutableLiveData

object FCMData {
    var dealId = MutableLiveData<Long>()
    var isAccept = MutableLiveData<Boolean>(false)
    var isReject = MutableLiveData<Boolean>(false)
}
