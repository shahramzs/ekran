package com.example.ekran.utils

import android.content.Context
import net.gotev.uploadservice.data.UploadInfo
import net.gotev.uploadservice.exceptions.UploadError
import net.gotev.uploadservice.exceptions.UserCancelledUploadException
import net.gotev.uploadservice.network.ServerResponse
import net.gotev.uploadservice.observer.request.RequestObserverDelegate
import timber.log.Timber

class GlobalRequestObserverDelegate: RequestObserverDelegate {

    override fun onProgress(context: Context, uploadInfo: UploadInfo) {
        Timber.tag("RECEIVER").e("Progress: %s", uploadInfo)
    }

    override fun onSuccess(
        context: Context,
        uploadInfo: UploadInfo,
        serverResponse: ServerResponse
    ) {
        Timber.tag("RECEIVER").e("Success: %s", serverResponse)
    }

    override fun onError(context: Context, uploadInfo: UploadInfo, exception: Throwable) {
        when (exception) {
            is UserCancelledUploadException -> {
                Timber.tag("RECEIVER").e("Error, user cancelled upload: %s", uploadInfo)
            }

            is UploadError -> {
                Timber.tag("RECEIVER").e("Error, upload error: %s", exception.serverResponse)
            }

            else -> {
                Timber.tag("RECEIVER").e(exception, "Error: %s", uploadInfo)
            }
        }
    }

    override fun onCompleted(context: Context, uploadInfo: UploadInfo) {
        Timber.tag("RECEIVER").e("Completed: %s", uploadInfo)
    }

    override fun onCompletedWhileNotObserving() {
        Timber.tag("RECEIVER").e("Completed while not observing")
    }
}