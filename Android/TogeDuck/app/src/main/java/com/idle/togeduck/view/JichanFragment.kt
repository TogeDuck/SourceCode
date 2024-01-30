package com.idle.togeduck.view

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.idle.togeduck.R
import com.idle.togeduck.databinding.FragmentJichanBinding
import com.idle.togeduck.model.FavoriteIdol
import com.idle.togeduck.util.CalcStatusBarSize
import com.idle.togeduck.util.CalcStatusBarSize.getStatusBarHeightToPx
import com.idle.togeduck.util.Theme
import com.idle.togeduck.util.TogeDuckColor
import com.idle.togeduck.util.TogeDuckItemDecoration
import com.idle.togeduck.view.favorite_setting.IMyFavorite
import com.idle.togeduck.view.favorite_setting.IdolSearchResultAdapter
import com.idle.togeduck.view.favorite_setting.MyFavoriteAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class JichanFragment : Fragment() {
    private var _binding: FragmentJichanBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentJichanBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val statusBarPx = getStatusBarHeightToPx(requireContext())

        binding.mainLayout.setPadding(0, statusBarPx, 0, 0)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}