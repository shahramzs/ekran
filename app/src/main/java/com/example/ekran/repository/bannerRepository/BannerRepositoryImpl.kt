package com.example.ekran.repository.bannerRepository

import com.example.ekran.dataSource.bannerDataSource.BannerDataSource
import com.example.ekran.model.Banner
import io.reactivex.Single

class BannerRepositoryImpl(private val bannerRemoteDataSource: BannerDataSource):BannerRepository {
    override fun getBanner(): Single<List<Banner>> = bannerRemoteDataSource.getBanner()
}