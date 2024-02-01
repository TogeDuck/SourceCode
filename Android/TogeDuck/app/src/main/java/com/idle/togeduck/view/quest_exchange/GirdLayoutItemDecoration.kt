package com.idle.togeduck.view.quest_exchange

import android.graphics.Rect
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.idle.togeduck.util.DpPxUtil.dpToPx

class GirdLayoutItemDecoration(private val spacing: Int) : RecyclerView.ItemDecoration() {
    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        val position = parent.getChildAdapterPosition(view)
        val spanCount = (parent.layoutManager as GridLayoutManager).spanCount
        val spacingBetweenItems = spacing * (spanCount - 1) / spanCount

//        if (position % spanCount == 0) {
//            outRect.left = spacing
//            outRect.right = spacingBetweenItems
//        } else {
//            outRect.left = spacingBetweenItems
//            outRect.right = spacing
//        }

        outRect.bottom = spacing
    }
}