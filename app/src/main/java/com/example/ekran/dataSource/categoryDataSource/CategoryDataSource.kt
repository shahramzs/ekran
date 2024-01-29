package com.example.ekran.dataSource.categoryDataSource

import com.example.ekran.model.Category
import io.reactivex.Single

interface CategoryDataSource {

    fun getCategory(): Single<List<Category>>
}