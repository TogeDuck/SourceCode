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
import java.lang.Exception
import javax.inject.Inject

@HiltViewModel
class FavoriteSettingViewModel @Inject constructor(
    private val favoriteRepository: FavoriteRepository,
    private val celebrityRepository: CelebrityRepository
) : ViewModel() {
    private val _favoriteIdolList = MutableLiveData<List<Celebrity>>(listOf())
    val favoriteIdolList: LiveData<List<Celebrity>>
        get() = _favoriteIdolList

    private val _tempFavoriteIdolList = MutableLiveData<List<Celebrity>>(listOf())
    val tempFavoriteIdolList: LiveData<List<Celebrity>>
        get() = _tempFavoriteIdolList

    private val _searchIdolList = MutableLiveData<List<Celebrity>>(listOf())
    val searchIdolList: LiveData<List<Celebrity>>
        get() = _searchIdolList

    private val _selectedCelebrity = MutableLiveData<Celebrity>()
    val selectedCelebrity: LiveData<Celebrity> get() = _selectedCelebrity

    private val _clickedCelebrity = MutableLiveData<Celebrity>()
    val clickedCelebrity: LiveData<Celebrity> get() = _clickedCelebrity

    init {
        viewModelScope.launch {
            getFavoriteList()
        }
    }

     suspend fun getFavoriteList() :Boolean{
         var result = false
         try {
             val responseResult = favoriteRepository.getFavorites()
             if (responseResult.isSuccessful) {
                 val body = responseResult.body()!!

                 Log.d("로그", "FavoriteSettingViewModel - getFavoriteList() / ${body.data}")

                 _favoriteIdolList.value = body.data.map { celebrityResponse ->
                    val celebrity = celebrityResponse.celebrityResponseToCelebrity()
                     if (selectedCelebrity.value != null && selectedCelebrity.value!!.id == celebrity.id) {
                         celebrity.isSelected = true
                         syncClickedCelebrity()
                         setClickedCelebrity(celebrity)
                     }
                     celebrity
                 }
                 result = true
             } else {
                 val errorBody = Json.decodeFromString<DefaultResponse>(
                     responseResult.errorBody()?.string()!!
                 )
                 Log.d(
                     "로그",
                     "FavoriteSettingViewModel - getFavoriteList() 호출됨 - 응답 실패 - $errorBody"
                 )
                 result = false
             }
         }
        catch (e: Exception){
            return result
        }
         return result
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
        _favoriteIdolList.postValue(newList)

        _tempFavoriteIdolList.postValue(
            tempFavoriteIdolList.value!!.toMutableList().apply { add(celebrity) }
        )

        val responseResult = favoriteRepository.updateFavorites(celebrity.celebrityToFavoriteRequest())

        if (responseResult.isSuccessful) {
            val body = responseResult.body()!!

            Log.d("로그", "FavoriteSettingViewModel - addFavoriteIdol() 응답 성공 / ${body}")
        } else {
            val errorBody = Json.decodeFromString<DefaultResponse>(
                responseResult.errorBody()?.string()!!
            )

            Log.d("로그", "FavoriteSettingViewModel - addFavoriteIdol() 응답 실패 / ${errorBody}")
        }
    }

    suspend fun removeMyFavoriteIdol(celebrity: Celebrity) {
        val newList = _favoriteIdolList.value!!.toMutableList()
        newList.remove(celebrity)
        _favoriteIdolList.postValue(newList)

        _tempFavoriteIdolList.postValue(
            _tempFavoriteIdolList.value!!.toMutableList().apply { remove(celebrity) }
        )

        val responseResult = favoriteRepository.updateFavorites(celebrity.celebrityToFavoriteRequest())

        if (responseResult.isSuccessful) {
            val body = responseResult.body()!!

            Log.d("로그", "FavoriteSettingViewModel - removeMyFavoriteIdol() 응답 성공 / ${body}")
        } else {
            val errorBody = Json.decodeFromString<DefaultResponse>(
                responseResult.errorBody()?.string()!!
            )

            Log.d("로그", "FavoriteSettingViewModel - addFavoriteIdol() 응답 실패 / ${errorBody}")
        }
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

    fun syncClickedCelebrity() {
        _clickedCelebrity.value = _selectedCelebrity.value
    }

    fun setSelectedCelebrity(celebrity: Celebrity) {
        _selectedCelebrity.value = celebrity
    }

    fun completeBtnClicked() {
        _tempFavoriteIdolList.value = listOf()
    }
}