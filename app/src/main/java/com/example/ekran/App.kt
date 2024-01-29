package com.example.ekran

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.SharedPreferences
import android.os.Build
import android.os.Bundle
import androidx.media3.common.util.UnstableApi
import com.example.ekran.dataSource.authDataSource.AuthLocalDataSource
import com.example.ekran.dataSource.authDataSource.AuthRemoteDataSource
import com.example.ekran.dataSource.bannerDataSource.BannerRemoteDataSource
import com.example.ekran.dataSource.categoryDataSource.CategoryRemoteDataSource
import com.example.ekran.dataSource.commentDataSource.CommentRemoteDataSource
import com.example.ekran.dataSource.subscriberDataSource.SubscriberRemoteDataSource
import com.example.ekran.dataSource.videoDataSource.VideoLocalDataSource
import com.example.ekran.dataSource.videoDataSource.VideoRemoteDataSource
import com.example.ekran.feature.addVideo.AddVideoFromGalleryViewModel
import com.example.ekran.feature.addVideo.adapter.VideosOnGalleryAdapter
import com.example.ekran.feature.addVideo.uploadVideo.UploadVideoViewModel
import com.example.ekran.feature.auth.AuthViewModel
import com.example.ekran.feature.main.chip.ChipAdapter
import com.example.ekran.feature.main.mainFragment.MainViewModel
import com.example.ekran.feature.main.videoDetails.VideoDetailsViewModel
import com.example.ekran.feature.main.videoDetails.adapters.CommentAdapter
import com.example.ekran.feature.main.videoDetails.adapters.VideoSameAdapter
import com.example.ekran.feature.main.videoDetails.adapters.VideoSimilarAdapter
import com.example.ekran.feature.main.videoList.VideoListAdapter
import com.example.ekran.feature.subscription.SubscriptionViewModel
import com.example.ekran.feature.subscription.adapter.SubscriptionAdapter
import com.example.ekran.repository.authRepository.AuthRepository
import com.example.ekran.repository.authRepository.AuthRepositoryImpl
import com.example.ekran.repository.bannerRepository.BannerRepository
import com.example.ekran.repository.bannerRepository.BannerRepositoryImpl
import com.example.ekran.repository.categoryRepository.CategoryRepository
import com.example.ekran.repository.categoryRepository.CategoryRepositoryImpl
import com.example.ekran.repository.commentRepository.CommentRepository
import com.example.ekran.repository.commentRepository.CommentRepositoryImpl
import com.example.ekran.repository.subscriberRepository.SubscriberRepository
import com.example.ekran.repository.subscriberRepository.SubscriberRepositoryImpl
import com.example.ekran.repository.videosRepository.VideoRepository
import com.example.ekran.repository.videosRepository.VideoRepositoryImpl
import com.example.ekran.services.downloadManager.Downloader
import com.example.ekran.services.downloadManager.DownloaderImpl
import com.example.ekran.services.http.ApiService
import com.example.ekran.services.http.ApiServiceUploadVideo
import com.example.ekran.services.http.CreateApiServiceUploadVideo
import com.example.ekran.services.http.createApiServiceInstance
import com.example.ekran.services.imageLoadingService.FrescoImageLoadingService
import com.example.ekran.services.imageLoadingService.ImageLoadingService
import com.example.ekran.services.socketManager.SocketManagerService
import com.example.ekran.services.socketManager.WebSocketManager
import com.example.ekran.services.videoLoadingService.ExpoPlayerLoadingService
import com.example.ekran.services.videoLoadingService.VideoLoadingService
import com.example.ekran.utils.CustomPlaceholdersProcessor
import com.example.ekran.utils.GlobalRequestObserverDelegate
import com.facebook.drawee.backends.pipeline.Fresco
import com.facebook.imagepipeline.core.ImagePipelineConfig
import net.gotev.uploadservice.BuildConfig
import net.gotev.uploadservice.UploadServiceConfig
import net.gotev.uploadservice.data.RetryPolicyConfig
import net.gotev.uploadservice.observer.request.GlobalRequestObserver
import org.koin.android.ext.android.get
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.context.startKoin
import org.koin.dsl.module
import timber.log.Timber


@UnstableApi class App : Application() {

    companion object {
        const val notificationChannelID = "uploadVideoChannel"
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= 26) {
            val channel = NotificationChannel(
                notificationChannelID,
                "uploadVideo Channel",
                NotificationManager.IMPORTANCE_LOW
            )
            val manager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            manager.createNotificationChannel(channel)
        }
    }

    override fun onCreate() {
        super.onCreate()
        createNotificationChannel()

        UploadServiceConfig.initialize(
            context = this,
            defaultNotificationChannel = notificationChannelID,
            debug = BuildConfig.DEBUG
        )
        UploadServiceConfig.retryPolicy = RetryPolicyConfig(
            initialWaitTimeSeconds = 1,
            maxWaitTimeSeconds = 10,
            multiplier = 2,
            defaultMaxRetries = 3
        )
        UploadServiceConfig.placeholdersProcessor = CustomPlaceholdersProcessor()
        GlobalRequestObserver(this, GlobalRequestObserverDelegate())
        Timber.plant(Timber.DebugTree())

        val config: ImagePipelineConfig = ImagePipelineConfig.newBuilder(this)
            .setDownsampleEnabled(true)
            .build()
        Fresco.initialize(this, config)


        val appModules = module {

            single<ApiService> { createApiServiceInstance() }
            single<ApiServiceUploadVideo> { CreateApiServiceUploadVideo() }
            single<ImageLoadingService>{FrescoImageLoadingService()}
            single<VideoLoadingService>{ExpoPlayerLoadingService()}
            single<SharedPreferences> { this@App.getSharedPreferences("app_settings", MODE_PRIVATE) }
            single<SocketManagerService>{WebSocketManager()}
            single<Downloader>{ DownloaderImpl() }

            factory<VideoRepository>{VideoRepositoryImpl(VideoRemoteDataSource(get()),VideoLocalDataSource())}
            factory<CategoryRepository>{CategoryRepositoryImpl(CategoryRemoteDataSource(get()))}
            factory<BannerRepository> {BannerRepositoryImpl(BannerRemoteDataSource(get())) }
            factory { VideoListAdapter(get()) }
            factory { ChipAdapter() }
            factory<SubscriberRepository> {SubscriberRepositoryImpl(
                SubscriberRemoteDataSource(get())
            )}
            factory<CommentRepository> {CommentRepositoryImpl(CommentRemoteDataSource(get()))  }
            factory { VideoSameAdapter(get()) }
            factory { VideoSimilarAdapter(get()) }
            factory{VideosOnGalleryAdapter(get())}
            factory<AuthRepository> {AuthRepositoryImpl(AuthRemoteDataSource(get()),
                AuthLocalDataSource((get()))
            ) }
            factory {CommentAdapter(get()) }
            factory{SubscriptionAdapter(get())}

            viewModel { MainViewModel(get(), get(), get()) }
            viewModel {(bundle: Bundle) -> VideoDetailsViewModel(bundle,get(),get(),get()) }
            viewModel{AddVideoFromGalleryViewModel(get())}
            viewModel{(bundle:Bundle) -> UploadVideoViewModel(bundle,get())}
            viewModel{AuthViewModel(get())}
            viewModel{SubscriptionViewModel(get())}
        }

        startKoin {
            androidLogger()
            androidContext(this@App)
            modules(appModules)
        }

        val authRepository:AuthRepository = get()
        authRepository.loadToken()
        authRepository.loadUsername()
        authRepository.loadEmail()
    }

}