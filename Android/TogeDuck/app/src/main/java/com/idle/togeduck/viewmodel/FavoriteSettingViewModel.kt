package com.idle.togeduck.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.idle.togeduck.model.FavoriteIdol
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class FavoriteSettingViewModel @Inject constructor() : ViewModel() {
    // 더미 데이터
    private val tempFavoriteIdolList = mutableListOf<FavoriteIdol>()
    private val tempSearchIdolList = mutableListOf(
        FavoriteIdol("https://search.pstatic.net/common?type=b&size=144&expire=1&refresh=true&quality=100&direct=true&src=http%3A%2F%2Fsstatic.naver.net%2Fpeople%2F74%2F202212051555381451.png", "RM"),
        FavoriteIdol("https://search.pstatic.net/common?type=b&size=144&expire=1&refresh=true&quality=100&direct=true&src=http%3A%2F%2Fsstatic.naver.net%2Fpeople%2F88%2F202210251057335601.png", "진"),
        FavoriteIdol("https://search.pstatic.net/common?type=b&size=144&expire=1&refresh=true&quality=100&direct=true&src=http%3A%2F%2Fsstatic.naver.net%2Fpeople%2F90%2F202308222149326631.jpg", "제이홉"),
        FavoriteIdol("https://search.pstatic.net/common?type=b&size=144&expire=1&refresh=true&quality=100&direct=true&src=http%3A%2F%2Fsstatic.naver.net%2Fpeople%2Fportrait%2F202303%2F20230324141336356.jpg", "지민"),
        FavoriteIdol("https://search.pstatic.net/common?type=b&size=144&expire=1&refresh=true&quality=100&direct=true&src=http%3A%2F%2Fsstatic.naver.net%2Fpeople%2FprofileImg%2Fcade00ba-c71f-493e-9c83-be60d716e174.png", "뷔"),
        FavoriteIdol("https://search.pstatic.net/common?type=b&size=144&expire=1&refresh=true&quality=100&direct=true&src=http%3A%2F%2Fsstatic.naver.net%2Fpeople%2FprofileImg%2Ft%2F5f84e775-20f5-4b31-891a-e91a266c7c30.jpg", "정국"),
        FavoriteIdol("https://search.pstatic.net/common?type=b&size=144&expire=1&refresh=true&quality=100&direct=true&src=http%3A%2F%2Fsstatic.naver.net%2Fpeople%2F86%2F202304211711133851.png", "슈가"),
        FavoriteIdol("https://search.pstatic.net/common?type=b&size=144&expire=1&refresh=true&quality=100&direct=true&src=http%3A%2F%2Fsstatic.naver.net%2Fpeople%2F74%2F202212051555381451.png", "RM2"),
        FavoriteIdol("https://search.pstatic.net/common?type=b&size=144&expire=1&refresh=true&quality=100&direct=true&src=http%3A%2F%2Fsstatic.naver.net%2Fpeople%2F88%2F202210251057335601.png", "진2"),
        FavoriteIdol("https://search.pstatic.net/common?type=b&size=144&expire=1&refresh=true&quality=100&direct=true&src=http%3A%2F%2Fsstatic.naver.net%2Fpeople%2F90%2F202308222149326631.jpg", "제이홉2"),
        FavoriteIdol("https://search.pstatic.net/common?type=b&size=144&expire=1&refresh=true&quality=100&direct=true&src=http%3A%2F%2Fsstatic.naver.net%2Fpeople%2Fportrait%2F202303%2F20230324141336356.jpg", "지민2"),
        FavoriteIdol("https://search.pstatic.net/common?type=b&size=144&expire=1&refresh=true&quality=100&direct=true&src=http%3A%2F%2Fsstatic.naver.net%2Fpeople%2FprofileImg%2Fcade00ba-c71f-493e-9c83-be60d716e174.png", "뷔2"),
        FavoriteIdol("https://search.pstatic.net/common?type=b&size=144&expire=1&refresh=true&quality=100&direct=true&src=http%3A%2F%2Fsstatic.naver.net%2Fpeople%2FprofileImg%2Ft%2F5f84e775-20f5-4b31-891a-e91a266c7c30.jpg", "정국2"),
        FavoriteIdol("https://search.pstatic.net/common?type=b&size=144&expire=1&refresh=true&quality=100&direct=true&src=http%3A%2F%2Fsstatic.naver.net%2Fpeople%2F86%2F202304211711133851.png", "슈가2"),
        FavoriteIdol("https://search.pstatic.net/common?type=b&size=144&expire=1&refresh=true&quality=100&direct=true&src=http%3A%2F%2Fsstatic.naver.net%2Fpeople%2F74%2F202212051555381451.png", "RM3"),
        FavoriteIdol("https://search.pstatic.net/common?type=b&size=144&expire=1&refresh=true&quality=100&direct=true&src=http%3A%2F%2Fsstatic.naver.net%2Fpeople%2F88%2F202210251057335601.png", "진3"),
        FavoriteIdol("https://search.pstatic.net/common?type=b&size=144&expire=1&refresh=true&quality=100&direct=true&src=http%3A%2F%2Fsstatic.naver.net%2Fpeople%2F90%2F202308222149326631.jpg", "제이홉3"),
        FavoriteIdol("https://search.pstatic.net/common?type=b&size=144&expire=1&refresh=true&quality=100&direct=true&src=http%3A%2F%2Fsstatic.naver.net%2Fpeople%2Fportrait%2F202303%2F20230324141336356.jpg", "지민3"),
        FavoriteIdol("https://search.pstatic.net/common?type=b&size=144&expire=1&refresh=true&quality=100&direct=true&src=http%3A%2F%2Fsstatic.naver.net%2Fpeople%2FprofileImg%2Fcade00ba-c71f-493e-9c83-be60d716e174.png", "뷔3"),
        FavoriteIdol("https://search.pstatic.net/common?type=b&size=144&expire=1&refresh=true&quality=100&direct=true&src=http%3A%2F%2Fsstatic.naver.net%2Fpeople%2FprofileImg%2Ft%2F5f84e775-20f5-4b31-891a-e91a266c7c30.jpg", "정국3"),
        FavoriteIdol("https://search.pstatic.net/common?type=b&size=144&expire=1&refresh=true&quality=100&direct=true&src=http%3A%2F%2Fsstatic.naver.net%2Fpeople%2F86%2F202304211711133851.png", "슈가3"),
    )

    private val _favoriteIdolList = MutableLiveData<List<FavoriteIdol>>()
    val favoriteIdolList: LiveData<List<FavoriteIdol>>
        get() = _favoriteIdolList

    private val _searchIdolList = MutableLiveData<MutableList<FavoriteIdol>>()
    val searchIdolList: LiveData<MutableList<FavoriteIdol>>
        get() = _searchIdolList

    init {
        _favoriteIdolList.value = tempFavoriteIdolList
        _searchIdolList.value = tempSearchIdolList
    }

    fun addFavoriteIdol(favoriteIdol: FavoriteIdol) {
        tempFavoriteIdolList.add(favoriteIdol)
        _favoriteIdolList.value = tempFavoriteIdolList
    }

    fun removeMyFavoriteIdol(favoriteIdol: FavoriteIdol) {
        tempFavoriteIdolList.remove(favoriteIdol)
        _favoriteIdolList.value = tempFavoriteIdolList
    }
}