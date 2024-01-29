package com.example.ekran.dataSource.bannerDataSource

import com.example.ekran.model.Banner
import io.reactivex.Single

interface BannerDataSource {
    fun getBanner():Single<List<Banner>>
}