package com.example.ekran.feature.main.sliders

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.ekran.model.Banner

class BannerSliderAdapter(fragment: Fragment, private val banners:List<Banner>) : FragmentStateAdapter(fragment) {

    override fun getItemCount(): Int  = banners.size

    override fun createFragment(position: Int): Fragment = BannerFragment.newInstance(banners[position])
}