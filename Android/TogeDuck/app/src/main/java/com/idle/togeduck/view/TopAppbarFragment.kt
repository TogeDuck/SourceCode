package com.idle.togeduck.view

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.idle.togeduck.databinding.FragmentFavoriteSettingBinding
import com.idle.togeduck.databinding.FragmentTopAppbarBinding

class TopAppbarFragment : Fragment() {
    private var _binding: FragmentTopAppbarBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentTopAppbarBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val statusDp = getStatusBarHeight(requireContext())
        binding.cta

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
}