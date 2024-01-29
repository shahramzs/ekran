package com.example.ekran.feature.main.sliders

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.ekran.R
import com.example.ekran.base.EXTRA_KEY_DATA
import com.example.ekran.customView.EkranImageView
import com.example.ekran.model.Banner
import com.example.ekran.services.imageLoadingService.ImageLoadingService
import org.koin.android.ext.android.inject

class BannerFragment : Fragment() {

    val imageLoadingService: ImageLoadingService by inject()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val imageView = inflater.inflate(R.layout.fragment_banner, container, false) as EkranImageView
        val banner = requireArguments().getParcelable<Banner>(EXTRA_KEY_DATA)?:throw IllegalStateException("banner can not be null")
        imageLoadingService.load(imageView,banner.banner)
        return imageView
    }

    companion object{
        fun newInstance(banner: Banner): BannerFragment{
           return BannerFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(EXTRA_KEY_DATA,banner)
                }
            }
        }
    }
}