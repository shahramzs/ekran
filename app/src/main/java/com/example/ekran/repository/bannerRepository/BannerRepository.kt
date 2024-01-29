package com.example.ekran.repository.bannerRepository

import com.example.ekran.model.Banner
import io.reactivex.Single

interface BannerRepository {

    fun getBanner(): Single<List<Banner>>
}