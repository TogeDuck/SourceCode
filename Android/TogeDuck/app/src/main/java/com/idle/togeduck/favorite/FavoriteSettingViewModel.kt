package com.idle.togeduck.favorite

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.idle.togeduck.favorite.model.Celebrity
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class FavoriteSettingViewModel @Inject constructor() : ViewModel() {
    private val _favoriteIdolList = MutableLiveData<List<Celebrity>>()
    val favoriteIdolList: LiveData<List<Celebrity>>
        get() = _favoriteIdolList

    private val _searchIdolList = MutableLiveData<MutableList<Celebrity>>()
    val searchIdolList: LiveData<MutableList<Celebrity>>
        get() = _searchIdolList

    init {
    }

    fun addFavoriteIdol(celebrity: Celebrity) {
    }

    fun removeMyFavoriteIdol(celebrity: Celebrity) {
    }
}