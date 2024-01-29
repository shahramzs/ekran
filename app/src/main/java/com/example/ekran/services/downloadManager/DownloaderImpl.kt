package com.example.ekran.services.downloadManager

import android.app.DownloadManager
import android.content.Context
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.webkit.MimeTypeMap
import android.widget.Toast
import androidx.annotation.RequiresApi
import java.io.File


class DownloaderImpl() : Downloader {

    @RequiresApi(Build.VERSION_CODES.N)
    override fun download(url: String, context: Context): Long {
        val downloadManager = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            context.getSystemService(DownloadManager::class.java)
        } else {
            context.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
        }

        var downloadId: Long? = null

        val fileName = getFileNameFromUri(url);
        val request = DownloadManager.Request(Uri.parse(url))
            .setMimeType(parseMimeType(url))
            .setDescription("Downloading")
            .setAllowedNetworkTypes(DownloadManager.Request.NETWORK_MOBILE or DownloadManager.Request.NETWORK_WIFI)
            .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
            .setTitle(fileName)
            .setAllowedOverMetered(true)
            .setAllowedOverRoaming(true)
            .setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, fileName)
        downloadId = downloadManager.enqueue(request)
//        progress(context, downloadId, downloadManager)
        return downloadId
    }

    private fun getFileNameFromUri(url: String): String {
        return url.substring(url.lastIndexOf('/') + 1, url.length);
    }

    private fun parseMimeType(url: String): String {
        val file = File(url)
        val map = MimeTypeMap.getSingleton()
        val ext = MimeTypeMap.getFileExtensionFromUrl(file.name)
        var type = map.getMimeTypeFromExtension(ext)
        type = type ?: "*/*"
        return type;
    }

    private fun progress(context: Context, downloadId: Long, downloadManager: DownloadManager) {
        var finishDownload = false
        var progress: Int
        while (!finishDownload) {
            val cursor = downloadManager.query(DownloadManager.Query().setFilterById(downloadId))
            if (cursor.moveToFirst()) {
                val status = cursor.getInt(cursor.getColumnIndexOrThrow(DownloadManager.COLUMN_STATUS))
                when (status) {
                    DownloadManager.STATUS_FAILED -> {
                        finishDownload = true
                    }

                    DownloadManager.STATUS_PAUSED -> {}
                    DownloadManager.STATUS_PENDING -> {}
                    DownloadManager.STATUS_RUNNING -> {
                        val total =
                            cursor.getLong(cursor.getColumnIndexOrThrow(DownloadManager.COLUMN_TOTAL_SIZE_BYTES))
                        if (total >= 0) {
                            val downloaded =
                                cursor.getLong(cursor.getColumnIndexOrThrow(DownloadManager.COLUMN_BYTES_DOWNLOADED_SO_FAR))
                            progress = (downloaded * 100L / total).toInt()

                        }
                    }

                    DownloadManager.STATUS_SUCCESSFUL -> {
                        progress = 100
                        // if you use aysnc task
                        // publishProgress(100);
                        finishDownload = true
                        Toast.makeText(context, "Download Completed", Toast.LENGTH_SHORT)
                            .show()
                    }
                }
            }
        }
    }
}