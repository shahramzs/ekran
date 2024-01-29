package com.example.ekran.dataSource.categoryDataSource

import com.example.ekran.model.Category
import com.example.ekran.services.http.ApiService
import io.reactivex.Single

class CategoryRemoteDataSource(private val apiService: ApiService):CategoryDataSource {
    override fun getCategory(): Single<List<Category>> = apiService.getCategory()
}