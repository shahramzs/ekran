package com.example.ekran.utils

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import com.example.ekran.R
import com.google.android.material.progressindicator.LinearProgressIndicator


class SocketProgressDialog() : DialogFragment() {

    private var progress: LinearProgressIndicator? = null
    var text: TextView? = null

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val alertDialog: AlertDialog.Builder = AlertDialog.Builder(requireContext())
        alertDialog.setTitle("تغییر فرمت ویدیو")
        val view: View = layoutInflater.inflate(R.layout.socket_progress, null, false)

        progress = view.findViewById<LinearProgressIndicator>(R.id.socket_progress_indicator)
        text = view.findViewById<TextView>(R.id.socket_progress_text)

//        Timber.tag("dialog").d(message)

//        progress.progress = message?.toInt()!!
//        text.text = message + " % "


        alertDialog.setView(view)
        return alertDialog.create()
    }

    fun fillProgress(message: String?) {

        progress?.progress = message?.toInt()!!
        text?.text = message + " % "

    }
}