package com.example.ekran.utils

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.TextView
import com.example.ekran.R
import com.example.ekran.model.Videos
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetBehavior.STATE_EXPANDED
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class MainBottomSheet(private val video: Videos): BottomSheetDialogFragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.main_bottom_sheet,container,false)

        val text = view.findViewById<TextView>(R.id.moreOption)
        val standardBottomSheet = view.findViewById<FrameLayout>(R.id.standard_bottom_sheet)
        val standardBottomSheetBehavior = BottomSheetBehavior.from(standardBottomSheet)
        standardBottomSheetBehavior.state = STATE_EXPANDED;

        text.text = video.title
        return view
    }

    companion object {
        const val TAG = "ModalBottomSheet"
    }
}