package com.idle.togeduck.favorite.view

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.content.Context
import android.graphics.drawable.GradientDrawable
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
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
import com.idle.togeduck.R
import com.idle.togeduck.databinding.ComponentSearchBarBinding
import com.idle.togeduck.databinding.FragmentFavoriteSettingBinding
import com.idle.togeduck.util.Theme
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
import kotlinx.coroutines.Job
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

    private val myJob = Job()
    private val myContext get() = Dispatchers.Main + myJob

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

        val animationTime = 300L

        CoroutineScope(myContext).launch {
            // 메모 검색시 검색어 변경이 0.35초 동안 없을시 검색 실행
            launch {
                val editTextFlow = searchBarBinding.etSearch.textChangesToFlow()
                editTextFlow
                    .onEach { text ->
                        // 클리어버튼 및 검색결과 없음 뷰 visibility 설정
                        if (text.isNullOrBlank()) {
                            binding.llSearchResult.animate()
                                .alpha(0f)
                                .setDuration(animationTime)
                                .setListener(object : AnimatorListenerAdapter() {
                                    override fun onAnimationEnd(animation: Animator) {
                                        binding.llSearchResult.visibility = View.GONE
                                    }
                                })
                        }
                    }
                    .debounce(350)
                    .onEach { text ->
                        // api 호출
                        if (!text.isNullOrBlank()) {
                            binding.llSearchResult.apply {
                                alpha = 0f
                                visibility = View.VISIBLE
                                animate()
                                    .alpha(1f)
                                    .setDuration(animationTime)
                                    .setListener(null)
                            }
                        }
                    }
                    .launchIn(this)
            }
        }
    }

    private fun setRecyclerview() {
        myFavoriteAdapter = MyFavoriteAdapter(this, requireContext())
        idolSearchResultAdapter = IdolSearchResultAdapter(this, requireContext())

        binding.rvMyFavorite.apply {
            addItemDecoration(TogeDuckItemDecoration(5, 15))
            adapter = myFavoriteAdapter
            layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, true)
                    .apply { stackFromEnd = true }
        }

        binding.rvIdolSearchResult.apply {
            addItemDecoration(TogeDuckItemDecoration(5, 5))
            adapter = idolSearchResultAdapter
            layoutManager =
                GridLayoutManager(requireContext(), 3, GridLayoutManager.VERTICAL, false)
        }
    }

    override fun myFavoriteRemoveBtnClicked(position: Int) {
        favoriteSettingViewModel.removeMyFavoriteIdol(favoriteSettingViewModel.favoriteIdolList.value!![position])
    }

    override fun idolClicked(position: Int) {
        if (favoriteSettingViewModel.favoriteIdolList.value!!.contains(favoriteSettingViewModel.searchIdolList.value!![position])) {
            favoriteSettingViewModel.removeMyFavoriteIdol(favoriteSettingViewModel.searchIdolList.value!![position])
        } else {
            favoriteSettingViewModel.addFavoriteIdol(favoriteSettingViewModel.searchIdolList.value!![position])
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

}