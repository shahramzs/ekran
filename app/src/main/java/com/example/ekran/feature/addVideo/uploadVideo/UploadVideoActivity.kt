package com.example.ekran.feature.addVideo.uploadVideo

import android.content.Context
import android.database.Cursor
import android.graphics.Bitmap
import android.graphics.Color
import android.media.MediaMetadataRetriever
import android.media.MediaScannerConnection
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.SpinnerAdapter
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.example.ekran.R
import com.example.ekran.base.EkranActivity
import com.example.ekran.base.EkranCompletableObserver
import com.example.ekran.base.EkranSingleObserver
import com.example.ekran.base.TokenContainer
import com.example.ekran.customView.EkranImageView
import com.example.ekran.model.VideoResponse
import com.example.ekran.services.imageLoadingService.ImageLoadingService
import com.example.ekran.services.socketManager.WebSocketManager
import com.example.ekran.utils.UploadStreamRequestBody
import com.example.ekran.utils.bytesIntoHumanReadable
import com.example.ekran.utils.getLocalIpAddress
import com.example.ekran.utils.randomCode
import com.example.ekran.utils.timeFormater
import com.google.android.material.button.MaterialButton
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import com.google.android.material.progressindicator.LinearProgressIndicator
import com.google.android.material.textfield.TextInputEditText
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.plugins.RxJavaPlugins
import io.reactivex.schedulers.Schedulers
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf
import timber.log.Timber
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.util.Calendar


class UploadVideoActivity : EkranActivity() {

    private val uploadVideoViewModel: UploadVideoViewModel by viewModel { parametersOf(intent.extras) }
    private val imageLoadingService: ImageLoadingService by inject()
    val compositeDisposable = CompositeDisposable()


    private var commentStatus: Spinner? = null
    private var categoryStatus: Spinner? = null
    private var video: Uri? = null
    private lateinit var videoDuration: String
    private lateinit var videoSize: String
    private var tags = ArrayList<String>()
    private lateinit var chipTag: String
    private var hashSet = HashSet<String>()
    private lateinit var tag: TextInputEditText
    lateinit var chipGroup: ChipGroup
    private lateinit var image: Uri
    private lateinit var catt: String
    private lateinit var comment: String
    private lateinit var file: File
    private val IMAGE_DIRECTORY = "/upload_gallery"
    private var webSocket:WebSocketManager ?= null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_upload_video)

        webSocket = WebSocketManager()
        webSocket?.connect()

        RxJavaPlugins.setErrorHandler { e: Throwable? -> }

        val progressBar = findViewById<LinearProgressIndicator>(R.id.progressBarVideoUpload)
        val thumbnail = findViewById<EkranImageView>(R.id.uploadThumbnail)
        val duration = findViewById<TextView>(R.id.uploadDuration)
        val size = findViewById<TextView>(R.id.uploadSize)
        val length = findViewById<TextView>(R.id.uploadLength)
        val token = TokenContainer.token
        val user = TokenContainer.username
        val email = TokenContainer.email
        val code = randomCode(20)
        val ip = getLocalIpAddress()
        val title = findViewById<TextInputEditText>(R.id.tieUploadVideoTitle)
        val description = findViewById<TextInputEditText>(R.id.tieUploadVideoDescription)
        val progressText = findViewById<TextView>(R.id.progressUpload)
        val txtUploading = findViewById<TextView>(R.id.txtUploading)
        tag = findViewById<TextInputEditText>(R.id.tieUploadVideoTag)
        commentStatus = findViewById<Spinner>(R.id.comment_status)
        categoryStatus = findViewById<Spinner>(R.id.category)
        val send = findViewById<MaterialButton>(R.id.uploadVideoSend)
        val cancel = findViewById<MaterialButton>(R.id.uploadVideoCancel)
        chipGroup = findViewById<ChipGroup>(R.id.chipGroupItems)
        setSpinners()

        uploadVideoViewModel.uploadVideoLiveData.observe(this) {
            imageLoadingService.load(thumbnail, it.uri.toString())
            image = it.uri
            duration.text = timeFormater(it.duration.toLong())
            videoDuration = it.duration.toString()
            size.text = bytesIntoHumanReadable(it.size.toLong())
            videoSize = it.size.toString()
            length.text = it.resolution
            video = it.uri
        }

        send.setOnClickListener {

            send.isEnabled = false
            cancel.isEnabled = false
            txtUploading.text = "در حال بارگزاری لطفا صبر کنید..."

            val img = saveImage(createVideoThumb(this, image)!!)
            file = File(img!!)
            val requestBody: RequestBody = file.asRequestBody("*/*".toMediaTypeOrNull())

            uploadVideoViewModel.uploadVideoDetails(
                user!!,
                token!!,
                file.name,
                code,
                ip!!,
                title.text.toString(),
                description.text.toString(),
                tags.toString(),
                catt,
                videoDuration,
                videoSize,
                comment,
                requestBody
            ).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : EkranCompletableObserver(compositeDisposable) {
                    override fun onComplete() {

                        val path = getRealPathFromURI(this@UploadVideoActivity, video)
                        file = File(path!!)

                        val request = UploadStreamRequestBody("video/*", file, onUploadProgress = {
                            Timber.tag("video").d("On upload progress %s", it)
                            runOnUiThread {
                                progressBar.progress = it
                                progressText.text = "$it %"
                            }

                        })

                        uploadVideoViewModel.uploadVideo(token, code, file.name, request)
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(object :
                                EkranSingleObserver<VideoResponse>(compositeDisposable) {
                                override fun onSuccess(t: VideoResponse) {
                                    val videoUrl = t.video.substringAfterLast("/")
                                    webSocket!!.send(videoUrl)

                                }

                            })

                    }

                })

        }

        cancel.setOnClickListener {
            finish()
        }

        tag.addTextChangedListener(
            object : TextWatcher {
                override fun beforeTextChanged(
                    s: CharSequence,
                    start: Int,
                    count: Int,
                    after: Int
                ) {
                }

                override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
                override fun afterTextChanged(s: Editable) {
                    if (s.isNotEmpty() && s[s.length - 1] == ' ') {
                        addNewChip(s.toString(), chipGroup)
                        s.clear()
                    }
                }
            })

    }


    private fun setSpinners() {
        val comments = resources.getStringArray(R.array.comment_status)
        val spinnerAdapter: SpinnerAdapter =
            object : ArrayAdapter<String?>(this, R.layout.spinner_comment, comments) {
                override fun isEnabled(position: Int): Boolean {
                    return position != 0
                }

                override fun getDropDownView(
                    position: Int,
                    convertView: View?,
                    parent: ViewGroup
                ): View {
                    val view = super.getDropDownView(position, convertView, parent)
                    val tv = view as TextView
                    if (position == 0) {
                        // Set the hint text color gray
                        tv.setTextColor(Color.GRAY)
                    } else {
                        tv.setTextColor(Color.BLACK)
                    }
                    return view
                }

                override fun setDropDownViewResource(resource: Int) {
                    super.setDropDownViewResource(R.layout.spinner_comment)
                }
            }
        commentStatus?.adapter = spinnerAdapter
        commentStatus?.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View,
                position: Int,
                id: Long
            ) {
                val selectedItemText = parent.getItemAtPosition(position) as String
                comment = selectedItemText
                // If user change the default selection
                // First item is disable and it is used for hint
                if (position > 0) {
                    // Notify the selected item text
                    Toast.makeText(
                        applicationContext,
                        "Selected : $selectedItemText",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }
        val cat = resources.getStringArray(R.array.category)
        val spinnerAdapter2: SpinnerAdapter =
            object : ArrayAdapter<String?>(this, R.layout.spinner_comment, cat) {
                override fun isEnabled(position: Int): Boolean {
                    return position != 0
                }

                override fun getDropDownView(
                    position: Int,
                    convertView: View?,
                    parent: ViewGroup
                ): View {
                    val view = super.getDropDownView(position, convertView, parent)
                    val tv = view as TextView
                    if (position == 0) {
                        // Set the hint text color gray
                        tv.setTextColor(Color.GRAY)
                    } else {
                        tv.setTextColor(Color.BLACK)
                    }
                    return view
                }

                override fun setDropDownViewResource(resource: Int) {
                    super.setDropDownViewResource(R.layout.spinner_comment)
                }
            }
        categoryStatus?.adapter = spinnerAdapter2
        categoryStatus?.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View,
                position: Int,
                id: Long
            ) {
                val selectedItemText = parent.getItemAtPosition(position) as String
                catt = selectedItemText
                // If user change the default selection
                // First item is disable and it is used for hint
                if (position > 0) {
                    // Notify the selected item text
                    Toast.makeText(
                        applicationContext,
                        "Selected : $selectedItemText",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }
    }

    private fun addNewChip(person: String, chipGroup: ChipGroup) {
        val chip = Chip(this)
        chip.text = person
        chip.chipIcon =
            ContextCompat.getDrawable(applicationContext, R.drawable.tag_24)
        chip.isCloseIconEnabled = true
        chip.isClickable = true
        chip.isCheckable = false
        chipGroup.addView(chip, chipGroup.childCount - 1)
        chip.setOnCloseIconClickListener {
            chipGroup.removeView(chip)
            tags.remove(chip.text.toString())
        }
        for (i in 0 until chipGroup.childCount) {
            chipTag = (chipGroup.getChildAt(i) as Chip).text.toString()
            tags.add(chipTag)
        }
        hashSet.addAll(tags)
        tags.clear()
        tags.addAll(hashSet)
    }

    override fun onDestroy() {
        super.onDestroy()
        compositeDisposable.clear()
        webSocket?.close()
    }

    override fun onResume() {
        super.onResume()
//        webSocket.webSocketConnect(Api.SOCKET_URL)
    }

    private fun getRealPathFromURI(context: Context, contentUri: Uri?): String? {
        var cursor: Cursor? = null
        return try {
            val proj = arrayOf(MediaStore.Images.Media.DATA)
            cursor = context.contentResolver.query(contentUri!!, proj, null, null, null)
            val column_index = cursor!!.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
            cursor.moveToFirst()
            cursor.getString(column_index)
        } finally {
            cursor?.close()
        }
    }

    private fun createVideoThumb(context: Context, uri: Uri): Bitmap? {
        try {
            val mediaMetadataRetriever = MediaMetadataRetriever()
            mediaMetadataRetriever.setDataSource(context, uri)
            return mediaMetadataRetriever.frameAtTime
        } catch (ex: Exception) {
            Toast
                .makeText(context, "Error retrieving bitmap", Toast.LENGTH_SHORT)
                .show()
        }
        return null

    }

    private fun saveImage(myBitmap: Bitmap): String? {
        val bytes = ByteArrayOutputStream()
        myBitmap.compress(Bitmap.CompressFormat.JPEG, 90, bytes)
        val wallpaperDirectory: File = File(
            Environment.getExternalStorageDirectory().toString(), IMAGE_DIRECTORY
        )
        // have the object build the directory structure, if needed.
        if (!wallpaperDirectory.exists()) {
            wallpaperDirectory.mkdirs()
        }
        try {
            val f = File(
                wallpaperDirectory, "${
                    Calendar.getInstance()
                        .timeInMillis
                }.jpg"
            )
            f.createNewFile()
            val fo = FileOutputStream(f)
            fo.write(bytes.toByteArray())
            MediaScannerConnection.scanFile(this, arrayOf(f.path), arrayOf("image/jpeg"), null)
            fo.close()
            Timber.tag("TAG").d("File Saved::--->%s", f.absolutePath)
            return f.absolutePath
        } catch (e1: IOException) {
            e1.printStackTrace()
        }
        return ""
    }



}
