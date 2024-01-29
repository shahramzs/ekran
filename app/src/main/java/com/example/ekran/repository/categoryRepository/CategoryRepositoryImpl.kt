package com.example.ekran.repository.categoryRepository

import com.example.ekran.dataSource.categoryDataSource.CategoryDataSource
import com.example.ekran.model.Category
import io.reactivex.Single

class CategoryRepositoryImpl(private val categoryRemoteDataSource: CategoryDataSource):CategoryRepository {
    override fun getCategory(): Single<List<Category>> = categoryRemoteDataSource.getCategory()

}