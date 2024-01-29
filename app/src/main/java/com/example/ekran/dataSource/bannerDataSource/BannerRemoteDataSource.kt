package com.example.ekran.dataSource.bannerDataSource

import com.example.ekran.model.Banner
import com.example.ekran.services.http.ApiService
import io.reactivex.Single

class BannerRemoteDataSource(private val apiService: ApiService):BannerDataSource {
    override fun getBanner(): Single<List<Banner>>  = apiService.getBanner()
}