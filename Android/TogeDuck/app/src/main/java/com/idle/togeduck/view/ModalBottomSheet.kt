package com.idle.togeduck.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.idle.togeduck.R

class ModalBottomSheet : BottomSheetDialogFragment() {

    var sheetBehavior: BottomSheetBehavior<FrameLayout>? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.component_bottom_sheet, container, false)


        sheetBehavior = BottomSheetBehavior.from(view.findViewById(R.id.bottom_sheet))

        sheetBehavior!!.isHideable = false


//        sheetBehavior?.setBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
//            override fun onStateChanged(bottomSheet: View, newState: Int) {
//                when (newState) {
//                    BottomSheetBehavior.STATE_HIDDEN -> {
//
//                    }
//                    BottomSheetBehavior.STATE_EXPANDED -> {
//
//                    }
//                    BottomSheetBehavior.STATE_COLLAPSED -> {
//
//                    }
//                    BottomSheetBehavior.STATE_DRAGGING -> {
//
//                    }
//                    BottomSheetBehavior.STATE_SETTLING -> {
//
//                    }
//                }
//            }
//
//            override fun onSlide(bottomSheet: View, slideOffset: Float) {
//                //슬라이드
//            }
//        })
        return view
    }


    companion object {
        const val TAG = "ModalBottomSheet"
    }



}