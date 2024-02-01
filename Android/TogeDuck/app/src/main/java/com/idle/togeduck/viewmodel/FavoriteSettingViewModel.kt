package com.idle.togeduck.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.idle.togeduck.model.FavoriteIdol
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class FavoriteSettingViewModel @Inject constructor() : ViewModel() {
    private val _favoriteIdolList = MutableLiveData<List<FavoriteIdol>>()
    val favoriteIdolList: LiveData<List<FavoriteIdol>>
        get() = _favoriteIdolList

    private val _searchIdolList = MutableLiveData<MutableList<FavoriteIdol>>()
    val searchIdolList: LiveData<MutableList<FavoriteIdol>>
        get() = _searchIdolList

    init {
    }

    fun addFavoriteIdol(favoriteIdol: FavoriteIdol) {
    }

    fun removeMyFavoriteIdol(favoriteIdol: FavoriteIdol) {
    }
}