package com.idle.togeduck.main_map

import android.util.Log
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.idle.togeduck.event.model.Event
import com.idle.togeduck.favorite.model.Celebrity
import com.idle.togeduck.history.model.Position
import com.idle.togeduck.main_map.view.MessageKind
import com.idle.togeduck.network.Coordinate
import com.idle.togeduck.util.CalcDistance
import com.idle.togeduck.util.NaverItem
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.NaverMap
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

    private val _peopleMarkerList = MutableLiveData<Map<String, NaverItem>>()
    val peopleMarkerList : MutableLiveData<Map<String, NaverItem>> get() = _peopleMarkerList

    private val _isTourStart = MutableLiveData<Boolean>(false)
    val isTourStart get() = _isTourStart

    private val _tourList = MutableLiveData<List<Position>>()
    val tourList: LiveData<List<Position>> get() = _tourList

    private val _closeEvents = MutableLiveData<List<Event>>()
    val closeEvents: LiveData<List<Event>> get() = _closeEvents

    init{
        _peopleMarkerList.value = emptyMap()
    }

    fun initTourList(){
        _tourList.value = emptyList()
    }
    fun initCloseEvnets(){
        _closeEvents.value = emptyList()
    }

    fun addTourRecord(lat:Double, lng:Double): Boolean{
        val currentList = _tourList.value?.toMutableList() ?: mutableListOf()
        val newPosition = Position(lat, lng)

        var hasChanged = false

        if(currentList.isEmpty()){
            currentList.add(newPosition)
            hasChanged = true
        } else {
            val lastPosition = currentList[currentList.size - 1]
            val disFromLastCoor = CalcDistance.getDistance(lastPosition.latitude, lastPosition.longitude, lat, lng)
            // 일정 거리 내인지 판별하는 메서드, 슷자는 조절 필요
            if(3 <= disFromLastCoor && disFromLastCoor <= 10){
                currentList.add(newPosition)
                hasChanged = true
            }
        }
        _tourList.value = currentList
        Log.d("투어 기록 리스트 업데이트", tourList.value.toString())
        return hasChanged
    }

    // 더미 지도 마커 생성 코드
    fun getItems(naverMap: NaverMap, num: Int) {
        val bounds = naverMap.contentBounds
        _markerList.value = mutableListOf<NaverItem>().apply {
            repeat(num) {
                val temp = NaverItem(
                    (bounds.northLatitude - bounds.southLatitude) * Math.random() + bounds.southLatitude,
                    (bounds.eastLongitude - bounds.westLongitude) * Math.random() + bounds.westLongitude
                )

                add(temp)
            }
        }
    }

    fun addItem(naverMap: NaverMap, num: Int) {
        val list = _markerList.value?.toMutableList() ?: mutableListOf()
        val bounds = naverMap.contentBounds

        repeat(num) {
            val temp = NaverItem(
                (bounds.northLatitude - bounds.southLatitude) * Math.random() + bounds.southLatitude,
                (bounds.eastLongitude - bounds.westLongitude) * Math.random() + bounds.westLongitude
            )

            list.add(temp)
        }

        _markerList.postValue(list)
    }

    fun setPickedDate(startDate: LocalDate, endDate: LocalDate) {
        _pickedDate.value = startDate to endDate
    }

    fun updatePeopleMarker(coordinate: Coordinate, type:String){
        val updateMap = _peopleMarkerList.value?.toMutableMap() ?: mutableMapOf()
        when(type){
            MessageKind.MESSAGE.toString() -> {
                updateMap[coordinate.userId] = NaverItem(coordinate.latitude, coordinate.longitude)
            }
            MessageKind.LEAVE.toString() -> {
                updateMap.remove(coordinate.userId)
            }
        }
        _peopleMarkerList.postValue(updateMap)
    }

    fun clearList() {
        _peopleMarkerList.value = mapOf()
    }

    fun setTourStatus(isStart: Boolean) {
        _isTourStart.value = isStart
    }
}