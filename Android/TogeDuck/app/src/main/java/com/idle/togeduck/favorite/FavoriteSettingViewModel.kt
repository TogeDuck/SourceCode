package com.idle.togeduck.favorite

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.idle.togeduck.common.model.DefaultResponse
import com.idle.togeduck.favorite.model.Celebrity
import com.idle.togeduck.favorite.model.CelebrityRepository
import com.idle.togeduck.favorite.model.FavoriteRepository
import com.idle.togeduck.favorite.model.celebrityResponseToCelebrity
import com.idle.togeduck.favorite.model.celebrityToFavoriteRequest
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import javax.inject.Inject

@HiltViewModel
class FavoriteSettingViewModel @Inject constructor(
    private val favoriteRepository: FavoriteRepository,
    private val celebrityRepository: CelebrityRepository
) : ViewModel() {
    private val _favoriteIdolList = MutableLiveData<List<Celebrity>>()
    val favoriteIdolList: LiveData<List<Celebrity>>
        get() = _favoriteIdolList

    private val _searchIdolList = MutableLiveData<List<Celebrity>>()
    val searchIdolList: LiveData<List<Celebrity>>
        get() = _searchIdolList


    private val _selectedCelebrity = MutableLiveData<Celebrity>()
    val selectedCelebrity: LiveData<Celebrity> get() = _selectedCelebrity

    private val _clickedCelebrity = MutableLiveData<Celebrity>()
    val clickedCelebrity: LiveData<Celebrity> get() = _clickedCelebrity

    init {
        _favoriteIdolList.value = mutableListOf()
        _searchIdolList.value = mutableListOf()

        viewModelScope.launch {
            getFavoriteList()
        }
    }

    private suspend fun getFavoriteList() {
        val responseResult = favoriteRepository.getFavorites()

        if (responseResult.isSuccessful) {
            val body = responseResult.body()!!

            _favoriteIdolList.value = body.data.map { it.celebrityResponseToCelebrity() }
        } else {
            val errorBody = Json.decodeFromString<DefaultResponse>(
                responseResult.errorBody()?.string()!!
            )

            Log.d("로그", "FavoriteSettingViewModel - getFavoriteList() 호출됨 - 응답 실패 - $errorBody")
        }
    }

    suspend fun getCelebrityList(keyword: String) {
        val responseResult = celebrityRepository.getCelebrities(keyword)

        if (responseResult.isSuccessful) {
            val body = responseResult.body()!!

            _searchIdolList.postValue(body.data.map { it.celebrityResponseToCelebrity() })
        } else {
            val errorBody = Json.decodeFromString<DefaultResponse>(
                responseResult.errorBody()?.string()!!
            )

            Log.d("로그", "FavoriteSettingViewModel - getCelebrityList() 호출됨 - 응답 실패 - $errorBody")
        }
    }

    suspend fun addFavoriteIdol(celebrity: Celebrity) {
        val newList = _favoriteIdolList.value!!.toMutableList()
        newList.add(celebrity)
//        _favoriteIdolList.value = newList
        _favoriteIdolList.postValue(newList)

        favoriteRepository.updateFavorites(celebrity.celebrityToFavoriteRequest())
    }

    suspend fun removeMyFavoriteIdol(celebrity: Celebrity) {
        val newList = _favoriteIdolList.value!!.toMutableList()
        newList.remove(celebrity)
//        _favoriteIdolList.value = newList
        _favoriteIdolList.postValue(newList)

        favoriteRepository.updateFavorites(celebrity.celebrityToFavoriteRequest())
    }

    fun clickedCelebrity(position: Int) {
        val newList = _favoriteIdolList.value!!.toMutableList()
        newList.forEach { it.isClicked = false }
        newList[position].isClicked = true
        _favoriteIdolList.value = newList
    }

    fun setClickedCelebrity(celebrity: Celebrity) {
        _clickedCelebrity.value = celebrity
    }

    fun setClickedCelebrity() {
        _clickedCelebrity.value = _selectedCelebrity.value
    }

    fun setSelectedCelebrity() {
        _selectedCelebrity.value = _clickedCelebrity.value
    }
}