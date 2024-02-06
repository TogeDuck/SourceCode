package com.idle.togeduck.main_map

import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.idle.togeduck.favorite.model.Celebrity
import com.idle.togeduck.util.NaverItem
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

    private val _peopleMarkerList = MutableLiveData<List<NaverItem>>()
    val peopleMarkerList : MutableLiveData<List<NaverItem>> get() = _peopleMarkerList


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
}