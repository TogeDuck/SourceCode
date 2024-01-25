package com.idle.togeduck.util

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

class TogeDuckItemDecoration(val vertical: Int, val horizontal: Int) : RecyclerView.ItemDecoration() {
    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        super.getItemOffsets(outRect, view, parent, state)
        outRect.set(0, vertical, horizontal * 2, vertical)
    }
}