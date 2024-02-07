package com.idle.togeduck.favorite.view

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.content.Context
import android.graphics.drawable.GradientDrawable
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.internal.ViewUtils.hideKeyboard
import com.idle.togeduck.R
import com.idle.togeduck.common.ScreenSize
import com.idle.togeduck.databinding.ComponentSearchBarBinding
import com.idle.togeduck.databinding.FragmentFavoriteSettingBinding
import com.idle.togeduck.common.Theme
import com.idle.togeduck.util.TogeDuckItemDecoration
import com.idle.togeduck.favorite.view.favorite_rv.IIdolSearchResult
import com.idle.togeduck.favorite.view.favorite_rv.IMyFavorite
import com.idle.togeduck.favorite.view.favorite_rv.IdolSearchResultAdapter
import com.idle.togeduck.favorite.view.favorite_rv.MyFavoriteAdapter
import com.idle.togeduck.favorite.FavoriteSettingViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

@AndroidEntryPoint
class FavoriteSettingFragment : Fragment(), IMyFavorite, IIdolSearchResult {
    private var _binding: FragmentFavoriteSettingBinding? = null
    private val binding get() = _binding!!
    private var _searchBarBinding: ComponentSearchBarBinding? = null
    private val searchBarBinding get() = _searchBarBinding!!

    private val favoriteSettingViewModel: FavoriteSettingViewModel by activityViewModels()

    private lateinit var myFavoriteAdapter: MyFavoriteAdapter
    private lateinit var idolSearchResultAdapter: IdolSearchResultAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentFavoriteSettingBinding.inflate(inflater, container, false)
        _searchBarBinding = binding.csb
        return binding.root
    }

    @OptIn(FlowPreview::class)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setRecyclerview()
        setTheme()

        searchBarBinding.etSearch.setOnKeyListener { mView, keyCode, _ ->
            hideKeyboard(mView, keyCode)
        }

        favoriteSettingViewModel.favoriteIdolList.observe(viewLifecycleOwner) { list ->
            myFavoriteAdapter.submitList(list.toList())

            // 추가하면 맨 앞으로 이동
            if (favoriteSettingViewModel.favoriteIdolList.value!!.isNotEmpty()) {
                Handler(Looper.getMainLooper()).postDelayed({
                    binding.rvMyFavorite.smoothScrollToPosition(favoriteSettingViewModel.favoriteIdolList.value!!.lastIndex)
                }, 35)
            }
        }

        favoriteSettingViewModel.searchIdolList.observe(viewLifecycleOwner) { list ->
            idolSearchResultAdapter.submitList(list.toList())
        }

        CoroutineScope(Dispatchers.Main).launch {
            // 메모 검색시 검색어 변경이 0.35초 동안 없을시 검색 실행
            launch {
                val editTextFlow = searchBarBinding.etSearch.textChangesToFlow()
                editTextFlow
                    .onEach { text ->
                        // 클리어버튼 및 검색결과 없음 뷰 visibility 설정
                        if (text.isNullOrBlank()) {
                            binding.llSearchResult.animate()
                                .alpha(0f)
                                .setDuration(ANIMATION_TIME)
                                .setListener(object : AnimatorListenerAdapter() {
                                    override fun onAnimationEnd(animation: Animator) {
                                        binding.llSearchResult.visibility = View.GONE
                                    }
                                })
                        }
                    }
                    .debounce(DEBOUNCE_TIME)
                    .onEach { text ->
                        // api 호출
                        if (!text.isNullOrBlank()) {
                            CoroutineScope(Dispatchers.IO).launch {
                                favoriteSettingViewModel.getCelebrityList(text.toString())
                            }

                            binding.llSearchResult.apply {
                                alpha = 0f
                                visibility = View.VISIBLE
                                animate()
                                    .alpha(1f)
                                    .setDuration(ANIMATION_TIME)
                                    .setListener(null)
                            }
                        }
                    }
                    .launchIn(this)
            }
        }
    }

    private fun setRecyclerview() {
        var spanCount = 1

        while ((ScreenSize.widthDp - 100) / (spanCount + 1) >= 90) {
            spanCount++
        }

        myFavoriteAdapter = MyFavoriteAdapter(this, requireContext())
        idolSearchResultAdapter = IdolSearchResultAdapter(this, requireContext(), spanCount)

        binding.rvMyFavorite.apply {
            addItemDecoration(TogeDuckItemDecoration(5, 10))
            adapter = myFavoriteAdapter
            layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, true)
                    .apply { stackFromEnd = true }
        }

        binding.rvIdolSearchResult.apply {
            addItemDecoration(TogeDuckItemDecoration(5, 5))
            adapter = idolSearchResultAdapter
            layoutManager =
                GridLayoutManager(requireContext(), spanCount, GridLayoutManager.VERTICAL, false)
        }
    }

    override fun myFavoriteRemoveBtnClicked(position: Int) {
        CoroutineScope(Dispatchers.IO).launch {
            favoriteSettingViewModel.removeMyFavoriteIdol(favoriteSettingViewModel.favoriteIdolList.value!![position])
        }
    }

    override fun idolClicked(position: Int) {
        if (favoriteSettingViewModel.favoriteIdolList.value!!.contains(favoriteSettingViewModel.searchIdolList.value!![position])) {
            CoroutineScope(Dispatchers.IO).launch {
                favoriteSettingViewModel.removeMyFavoriteIdol(favoriteSettingViewModel.searchIdolList.value!![position])
            }
        } else {
            CoroutineScope(Dispatchers.IO).launch {
                favoriteSettingViewModel.addFavoriteIdol(favoriteSettingViewModel.searchIdolList.value!![position])
            }
        }
    }

    // 엔터누르면 키보드 내려가고 포커스 사라지는 기능
    private fun hideKeyboard(view: View, keyCode: Int): Boolean {
        if (keyCode == KeyEvent.KEYCODE_ENTER) {
            val inputManager = requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE)
                    as InputMethodManager
            inputManager.hideSoftInputFromWindow(view.windowToken, 0)
            searchBarBinding.etSearch.clearFocus()
            return true
        }
        return false
    }


    private fun setTheme() {
        binding.flFavoriteSetting.background = ContextCompat.getDrawable(requireContext(), Theme.theme.main300)
        binding.tvTitle.setTextColor(ContextCompat.getColor(requireContext(), Theme.theme.main500))
        binding.tvMyFavorite.setTextColor(ContextCompat.getColor(requireContext(), Theme.theme.main500))
        binding.tvIdolSearchResult.setTextColor(ContextCompat.getColor(requireContext(), Theme.theme.main500))

        val circleDrawable = ContextCompat.getDrawable(requireContext(), R.drawable.shape_circle) as GradientDrawable
        circleDrawable.setColor(ContextCompat.getColor(requireContext(), Theme.theme.main100))
        circleDrawable.setStroke(4, ContextCompat.getColor(requireContext(), Theme.theme.main500))

        val squareCircleDrawable = ContextCompat.getDrawable(requireContext(), R.drawable.shape_square_circle) as GradientDrawable
        squareCircleDrawable.setColor(ContextCompat.getColor(requireContext(), Theme.theme.main100))
        squareCircleDrawable.setStroke(4, ContextCompat.getColor(requireContext(), Theme.theme.main500))

        val cursorDrawable = ContextCompat.getDrawable(requireContext(), R.drawable.shape_cursor) as GradientDrawable
        cursorDrawable.setColor(ContextCompat.getColor(requireContext(), Theme.theme.main500))

        @RequiresApi(Build.VERSION_CODES.Q)
        searchBarBinding.etSearch.textCursorDrawable = cursorDrawable
        searchBarBinding.etSearch.background = squareCircleDrawable
        searchBarBinding.etSearch.setTextColor(ContextCompat.getColor(requireContext(), Theme.theme.main500))
        searchBarBinding.etSearch.setHintTextColor(ContextCompat.getColor(requireContext(), Theme.theme.main500))
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        _searchBarBinding = null
    }

    companion object {
        const val ANIMATION_TIME = 300L
        const val DEBOUNCE_TIME = 350L
    }
}