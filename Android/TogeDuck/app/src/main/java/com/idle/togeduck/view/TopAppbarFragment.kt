package com.idle.togeduck.view

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.idle.togeduck.databinding.ComponentTopAppbarBinding
import com.idle.togeduck.databinding.FragmentFavoriteSettingBinding
import com.idle.togeduck.databinding.FragmentTopAppbarBinding

class TopAppbarFragment : Fragment() {
    private var _binding: FragmentTopAppbarBinding? = null
    private val binding get() = _binding!!

    private var _topAppBarBinding: ComponentTopAppbarBinding? = null
    private val topAppbarBinding get() = _topAppBarBinding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTopAppbarBinding.inflate(inflater, container, false)
        _topAppBarBinding = binding.cta
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val statusDp = getStatusBarHeight(requireContext())

        Log.d("로그", "TopAppbarFragment - onViewCreated() 호출됨 ${statusDp}")

        topAppbarBinding.llTopAppbar.setPadding(dpToPx(20), dpToPx(statusDp), dpToPx(20), dpToPx(10))
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    @SuppressLint("InternalInsetResource", "DiscouragedApi")
    fun getStatusBarHeight(context: Context) : Int {
        var statusBarHeight = 0
        val resourceId: Int = context.resources.getIdentifier("status_bar_height", "dimen", "android")

        if (resourceId > 0) statusBarHeight = context.resources.getDimensionPixelSize(resourceId)

        return pxTodp(statusBarHeight, context).toInt()
    }

    fun pxTodp(px: Int, context: Context): Float {
        return px / context.resources.displayMetrics.density
    }

    fun dpToPx(dp: Int): Int {
        val scale = resources.displayMetrics.density
        return (dp * scale + 0.5f).toInt()
    }
}