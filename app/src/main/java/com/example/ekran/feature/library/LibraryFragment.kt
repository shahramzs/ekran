package com.example.ekran.feature.library

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.ekran.R
import com.example.ekran.base.EkranFragment
import com.example.ekran.customView.EkranVideoView
import com.example.ekran.services.videoLoadingService.VideoLoadingService
import org.koin.android.ext.android.inject

class LibraryFragment : EkranFragment() {

    private val videoLoadingService : VideoLoadingService by inject()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_library, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val player =view.findViewById<EkranVideoView>(R.id.player_view)
        val url:String = "https://5f8u2z8mn5qjqvfdxs59z5g6aw8djtnew25.kinguploadf2m15.xyz/Film/2023/The.Little.Mermaid.2023.480p.WEB-DL.PAHE.Farsi.Sub.Film2Media.mkv"

        videoLoadingService.loadMovie(player, url,requireContext())
    }
}