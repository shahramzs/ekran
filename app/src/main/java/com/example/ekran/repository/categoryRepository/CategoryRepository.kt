package com.example.ekran.repository.categoryRepository

import com.example.ekran.model.Category
import io.reactivex.Single

interface CategoryRepository {

    fun getCategory():Single<List<Category>>
}