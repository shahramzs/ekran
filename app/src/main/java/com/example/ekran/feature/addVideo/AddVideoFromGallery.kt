package com.example.ekran.feature.addVideo

import android.content.ContentUris
import android.content.Context
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import com.example.ekran.model.VideosOnGallery


class AddVideoFromGallery {

    companion object {
        fun addVideo(context: Context): MutableList<VideosOnGallery> {

            val videosOnGalleryList = mutableListOf<VideosOnGallery>()

            val collection =
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    MediaStore.Video.Media.getContentUri(
                        MediaStore.VOLUME_EXTERNAL
                    )
                } else {
                    MediaStore.Video.Media.EXTERNAL_CONTENT_URI
                }
            val projection = arrayOf(
                MediaStore.Video.Media._ID,
                MediaStore.Video.Media.DISPLAY_NAME,
                MediaStore.Video.Media.DURATION,
                MediaStore.Video.Media.SIZE,
                MediaStore.Video.Media.RESOLUTION
            )
            val sortOrder = "${MediaStore.Video.Media.DISPLAY_NAME} DESC"

            val query = context.contentResolver.query(
                collection,
                projection,
                null,
                null,
                sortOrder
            )
            query?.use { cursor ->
                // Cache column indices.
                val idColumn = cursor.getColumnIndexOrThrow(MediaStore.Video.Media._ID)
                val nameColumn =
                    cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DISPLAY_NAME)
                val durationColumn =
                    cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DURATION)
                val sizeColumn = cursor.getColumnIndexOrThrow(MediaStore.Video.Media.SIZE)
                val resolutionColumn = cursor.getColumnIndexOrThrow(MediaStore.Video.Media.RESOLUTION)

                while (cursor.moveToNext()) {
                    val id = cursor.getLong(idColumn)
                    val name = cursor.getString(nameColumn)
                    val duration = cursor.getInt(durationColumn)
                    val size = cursor.getInt(sizeColumn)
                    val resolution = cursor.getString(resolutionColumn)
                    val contentUri: Uri = ContentUris.withAppendedId(
                        MediaStore.Video.Media.EXTERNAL_CONTENT_URI,
                        id
                    )

                    videosOnGalleryList += VideosOnGallery(
                        id,
                        contentUri,
                        name,
                        duration,
                        size,
                        resolution
                    )

                }

            }
            return videosOnGalleryList
        }


    }
}