package com.example.ekran.utils

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.ekran.R
import com.example.ekran.feature.main.videoDetails.adapters.CommentAdapter
import com.example.ekran.model.Comment
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import org.koin.android.ext.android.inject

class CommentBottomSheet(private val context:Context,private val c: ArrayList<Comment>): BottomSheetDialogFragment() {

    private val commentAdapter: CommentAdapter by inject()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.comment_bottom_sheet,container,false)

        val commentRV = view.findViewById<RecyclerView>(R.id.commentRV)
        commentAdapter.comments = c
        commentRV.layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        commentRV.adapter = commentAdapter

//        Timber.tag("comment").d(c.toString())

        val standardBottomSheet = view.findViewById<FrameLayout>(R.id.standard_comment_bottom_sheet)
        val standardBottomSheetBehavior = BottomSheetBehavior.from(standardBottomSheet)
        standardBottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED;

        return view
    }

    companion object {
        const val TAG = "CommentBottomSheet"
    }
}