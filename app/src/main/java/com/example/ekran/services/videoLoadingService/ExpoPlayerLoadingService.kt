package com.example.ekran.services.videoLoadingService

import android.content.Context
import androidx.media3.common.MediaItem
import androidx.media3.common.PlaybackException
import androidx.media3.common.Player
import androidx.media3.common.Tracks
import androidx.media3.common.util.UnstableApi
import androidx.media3.datasource.DataSource
import androidx.media3.datasource.DefaultDataSource
import androidx.media3.datasource.DefaultHttpDataSource
import androidx.media3.datasource.HttpDataSource
import androidx.media3.datasource.cronet.CronetDataSource
import androidx.media3.datasource.cronet.CronetUtil
import androidx.media3.exoplayer.DefaultRenderersFactory
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.exoplayer.RenderersFactory
import androidx.media3.exoplayer.analytics.AnalyticsListener
import androidx.media3.exoplayer.dash.DashMediaSource
import androidx.media3.exoplayer.source.DefaultMediaSourceFactory
import androidx.media3.exoplayer.source.MediaSource
import androidx.media3.exoplayer.source.ProgressiveMediaSource
import androidx.media3.exoplayer.trackselection.DefaultTrackSelector
import androidx.media3.exoplayer.util.EventLogger
import androidx.media3.extractor.DefaultExtractorsFactory
import com.example.ekran.customView.EkranVideoView
import org.chromium.net.CronetEngine
import timber.log.Timber
import java.net.CookieHandler
import java.net.CookieManager
import java.net.CookiePolicy
import java.util.concurrent.Executors

@UnstableApi class ExpoPlayerLoadingService:VideoLoadingService {

    private var player:ExoPlayer? = null

    override fun load(playerView: EkranVideoView, videoUrl: String,context:Context) {

        val dataSourceFactory: DataSource.Factory = DefaultHttpDataSource.Factory()
        val mediaSource: MediaSource = DashMediaSource.Factory(dataSourceFactory).createMediaSource(MediaItem.fromUri(videoUrl))

        player = ExoPlayer.Builder(context).build()
        playerView.player = player
        player!!.setMediaSource(mediaSource)
        player!!.trackSelectionParameters =
            player!!.trackSelectionParameters
            .buildUpon()
            .setMaxVideoSizeSd()
            .setPreferredAudioLanguage("US")
            .build()
        player!!.prepare()
        player!!.play()
    }

    override fun defaultLoad(playerView: EkranVideoView, videoUrl: String, context: Context) {
        player = ExoPlayer.Builder(context).build()
        playerView.player = player
        val mediaItem = MediaItem.fromUri(videoUrl)
        player!!.setMediaItem(mediaItem)
        player!!.prepare()
        player!!.playWhenReady
        player!!.play()
    }

    override fun loadMovie2(playerView: EkranVideoView, videoUrl: String, context: Context) {
        player = ExoPlayer.Builder(context).build()
        playerView.player = player
        val mediaItem = MediaItem.fromUri(videoUrl)
        val dataSourceFactory: DataSource.Factory = DefaultHttpDataSource.Factory()
//        val dataSource = DefaultDataSourceFactory(context, Util.getUserAgent(context,"appname"))
        val mediaSource = ProgressiveMediaSource.Factory(dataSourceFactory).createMediaSource(
            mediaItem)
        player!!.setMediaSource(mediaSource)
        player!!.prepare()
        player!!.playWhenReady
        player!!.play()
    }

    override fun loadMovie(playerView: EkranVideoView, videoUrl: String, context: Context) {

        val renderersFactory = buildRenderersFactory(context, true)
        val mediaSourceFactory =
            DefaultMediaSourceFactory(getDataSourceFactory(context), DefaultExtractorsFactory()) // 1
        val trackSelector = DefaultTrackSelector(context)

        player = ExoPlayer.Builder(context, renderersFactory)
            .setTrackSelector(trackSelector)
            .setMediaSourceFactory(mediaSourceFactory)  // 2
            .build().apply {
                addAnalyticsListener(EventLogger())
                trackSelectionParameters =
                    DefaultTrackSelector.Parameters.getDefaults(context)
//                addListener(exoPlayerListener)
                playWhenReady = false
            }

        playerView.player = player
        val mediaItem = MediaItem.fromUri(videoUrl) // 1
        player?.setMediaItem(mediaItem)  // 2
        player?.prepare() // 3
        player!!.playWhenReady
        player!!.play()


    }


    private fun buildRenderersFactory(
        context: Context, preferExtensionRenderer: Boolean
    ): RenderersFactory {
        val extensionRendererMode = if (preferExtensionRenderer)
            DefaultRenderersFactory.EXTENSION_RENDERER_MODE_PREFER
        else DefaultRenderersFactory.EXTENSION_RENDERER_MODE_ON

        return DefaultRenderersFactory(context.applicationContext)
            .setExtensionRendererMode(extensionRendererMode)
            .setEnableDecoderFallback(true)
    }

    private fun getDataSourceFactory(context: Context): DataSource.Factory = // 3
        DefaultDataSource.Factory(context, getHttpDataSourceFactory(context))

    private fun getHttpDataSourceFactory(context: Context): HttpDataSource.Factory {
        val cronetEngine: CronetEngine? = CronetUtil.buildCronetEngine(context)
        var httpDataSourceFactory: HttpDataSource.Factory? = null

        if (cronetEngine != null) httpDataSourceFactory =
            CronetDataSource.Factory(cronetEngine, Executors.newSingleThreadExecutor())

        if (httpDataSourceFactory == null) {
            // We don't want to use Cronet, or we failed to instantiate a CronetEngine.
            val cookieManager = CookieManager()
            cookieManager.setCookiePolicy(CookiePolicy.ACCEPT_ORIGINAL_SERVER)
            CookieHandler.setDefault(cookieManager)
            httpDataSourceFactory = DefaultHttpDataSource.Factory()
        }
        return httpDataSourceFactory
    }

    override fun release() {
        player?.stop()
        player?.release()

    }

    override fun listener() {
        player?.addAnalyticsListener(
            object : Player.Listener, AnalyticsListener {
                override fun onPlayerError(error: PlaybackException) {
                    val cause = error.cause
                    if (cause is HttpDataSource.HttpDataSourceException) {
                        // An HTTP error occurred.
                        val httpError = cause
                        if (httpError is HttpDataSource.InvalidResponseCodeException) {
                            Timber.tag("expo").e(httpError)
                        } else {
                            Timber.tag("expo").e(httpError)
                        }
                    }
                }
            }
        )

        player?.addListener(
            object: Player.Listener{
                override fun onTracksChanged(tracks: Tracks) {
                    super.onTracksChanged(tracks)
                }
            }
        )
    }

    override fun analytics() {
        player?.addAnalyticsListener(EventLogger())
    }

}