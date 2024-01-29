package com.example.ekran.feature.main.mainFragment

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.ekran.base.EkranSingleObserver
import com.example.ekran.base.EkranViewModel
import com.example.ekran.model.Banner
import com.example.ekran.model.Category
import com.example.ekran.model.Videos
import com.example.ekran.repository.bannerRepository.BannerRepository
import com.example.ekran.repository.categoryRepository.CategoryRepository
import com.example.ekran.repository.videosRepository.VideoRepository
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class MainViewModel(
    private val videoRepository: VideoRepository,
    private val categoryRepository: CategoryRepository,
    private val bannerRepository: BannerRepository
) : EkranViewModel() {

    private val _videoLiveData = MutableLiveData<List<Videos>>()
    val videoLiveData: LiveData<List<Videos>>
        get() = _videoLiveData

    private val _categoryLiveData = MutableLiveData<List<Category>>()
    val categoryLiveData: LiveData<List<Category>>
        get() = _categoryLiveData

    private val _bannerLiveData = MutableLiveData<List<Banner>>()
    val bannerLiveData: LiveData<List<Banner>>
        get() = _bannerLiveData


    init {
        progressBarLiveData.value = true
        videoRepository.getVideos()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doFinally { progressBarLiveData.value = false }
            .subscribe(object : EkranSingleObserver<List<Videos>>(compositeDisposable) {
                override fun onSuccess(t: List<Videos>) {
                    _videoLiveData.value = t
                }
            })

        categoryRepository.getCategory()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : EkranSingleObserver<List<Category>>(compositeDisposable) {
                override fun onSuccess(t: List<Category>) {
                    _categoryLiveData.value = t
                }

            })

        bannerRepository.getBanner()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : EkranSingleObserver<List<Banner>>(compositeDisposable) {
                override fun onSuccess(t: List<Banner>) {
                    _bannerLiveData.value = t
                }

            })
    }
}