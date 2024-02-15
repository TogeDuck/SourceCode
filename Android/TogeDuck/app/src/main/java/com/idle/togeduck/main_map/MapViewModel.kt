package com.idle.togeduck.main_map

import android.util.Log
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.idle.togeduck.MessageKind
import com.idle.togeduck.event.model.Event
import com.idle.togeduck.favorite.model.Celebrity
import com.idle.togeduck.history.model.Position
import com.idle.togeduck.network.Coordinate
import com.idle.togeduck.network.QuestAlert
import com.idle.togeduck.util.CalcDistance
import com.idle.togeduck.util.DpPxUtil
import com.idle.togeduck.util.NaverItem
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.NaverMap
import com.naver.maps.map.overlay.Marker
import com.naver.maps.map.overlay.OverlayImage
import dagger.hilt.android.lifecycle.HiltViewModel
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class MapViewModel @Inject constructor(

) : ViewModel() {
    private val _markerList = MutableLiveData<List<NaverItem>>()
    val markerList: LiveData<List<NaverItem>> get() = _markerList

    private val _pickedDate = MutableLiveData<Pair<LocalDate, LocalDate>>()
    val pickedDate: LiveData<Pair<LocalDate, LocalDate>> get() = _pickedDate

    val peopleMarkerList = MutableLiveData<Map<String, Marker>>()

    var tourList = mutableListOf<Position>()

    private val _closeEvents = MutableLiveData<List<Event>>()
    val closeEvents: LiveData<List<Event>> get() = _closeEvents

    var peopleMarkerOverlay: OverlayImage? = null
    var markerSize: Int = 20

    var peopleNum: MutableLiveData<Int> = MutableLiveData(0)

    var naverMap:NaverMap? = null

    var bottomSheetState: MutableLiveData<Int> = MutableLiveData(0)
    var isQuestAlert = MutableLiveData<QuestAlert>()
    var eventList = mutableListOf<Event>()
    var isCloseDialogOpen = false
    var isTourStart = false
    var visitedEvent = mutableListOf<Long>()

    fun initPeopleMarkerImage(image: OverlayImage){
        this.peopleMarkerOverlay = image
    }

    fun initTourList(){
        tourList = mutableListOf()
    }
    fun initCloseEvnets(){
        _closeEvents.value = emptyList()
    }

    fun addTourRecord(lat:Double, lng:Double): Boolean{
        Log.d("로그", "MapViewModel - addTourRecord() 호출됨 ${lat} / ${lng}")
        val currentList = tourList.toMutableList()
        val newPosition = Position(lat, lng)

        var hasChanged = false

        if(currentList.isEmpty()){
            currentList.add(newPosition)
            hasChanged = true
            Log.d("투어 기록 리스트 업데이트", tourList.toString())
        } else {
            val lastPosition = currentList[currentList.size - 1]
            val disFromLastCoor = CalcDistance.getDistance(lastPosition.latitude, lastPosition.longitude, lat, lng)
            // 일정 거리 내인지 판별하는 메서드, 슷자는 조절 필요
            if(disFromLastCoor in 5..30){
                currentList.add(newPosition)
                hasChanged = true
                Log.d("투어 기록 리스트 업데이트", tourList.toString())
            }
        }
        tourList = currentList
        return hasChanged
    }

    fun setBottomSheet(state:Int){
        bottomSheetState.value = state
    }
    fun setPickedDate(startDate: LocalDate, endDate: LocalDate) {
        _pickedDate.value = startDate to endDate
    }
    fun updatePeopleNum(updatePeople: Int){
        peopleNum.postValue(updatePeople)
    }
    fun updateMarkerSize() {
        peopleMarkerList.value?.let { peopleMarkers ->
            for ((_, marker) in peopleMarkers) {
                marker?.let {
                    it.width = markerSize
                    it.height = markerSize
                }
            }
        }
    }

    fun clearList() {
        peopleMarkerList.value = mutableMapOf()
    }
}