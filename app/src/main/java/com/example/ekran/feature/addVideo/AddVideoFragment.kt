package com.example.ekran.feature.addVideo

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.launch
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.ekran.R
import com.example.ekran.base.EXTRA_KEY_DATA
import com.example.ekran.base.EkranFragment
import com.example.ekran.customView.EkranVideoView
import com.example.ekran.feature.addVideo.adapter.VideosOnGalleryAdapter
import com.example.ekran.feature.addVideo.uploadVideo.UploadVideoActivity
import com.example.ekran.feature.auth.AuthActivity
import com.example.ekran.model.VideosOnGallery
import com.example.ekran.services.videoLoadingService.VideoLoadingService
import com.google.android.material.button.MaterialButton
import dev.marcelpinto.permissionktx.Permission
import dev.marcelpinto.permissionktx.PermissionRational
import dev.marcelpinto.permissionktx.PermissionStatus
import dev.marcelpinto.permissionktx.registerForPermissionResult
import org.koin.android.ext.android.inject


class AddVideoFragment : EkranFragment(), VideosOnGalleryAdapter.VideoOnClickListener {

    private val videosOnGalleryAdapter:VideosOnGalleryAdapter by inject()
    private var videosOnGalleryList = mutableListOf<VideosOnGallery>()
    private val addVideoFromGalleryViewModel:AddVideoFromGalleryViewModel by inject()
    private val videoLoadingService:VideoLoadingService by inject()
    private var video:EkranVideoView?=null

    companion object {
        private const val POSITION_REQUEST_CODE = 1
        private val POSITION_REQUIRED_PERMISSIONS = arrayOf(
            android.Manifest.permission.READ_EXTERNAL_STORAGE,
            android.Manifest.permission.READ_MEDIA_VIDEO,
            android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
            android.Manifest.permission.READ_MEDIA_AUDIO
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_add_video, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        videosOnGalleryAdapter.videoOnClickListener = this

        addVideoFromGalleryViewModel.videoLiveData.observe(viewLifecycleOwner){
            videosOnGalleryList.addAll(it)
        }

        video = view.findViewById<EkranVideoView>(R.id.addVideoView)

        if(videosOnGalleryList.isNotEmpty()) {
            val lastVideo = videosOnGalleryList.last()
            videoLoadingService.defaultLoad(video!!, lastVideo.uri.toString(), requireContext())
        }
        val finePermission = Permission( android.Manifest.permission.READ_EXTERNAL_STORAGE)
        when (val status = finePermission.status) {
            is PermissionStatus.Granted -> displayFiles()
            is PermissionStatus.Revoked -> if (status.rationale == PermissionRational.REQUIRED) {
               Toast.makeText(requireContext(),"Permission Denied.",Toast.LENGTH_LONG).show()
            } else {
                // Do something else
            }
        }

        val locationPermissionRequest =
            registerForPermissionResult( android.Manifest.permission.READ_EXTERNAL_STORAGE) { _ ->
                displayFiles()
            }

        locationPermissionRequest.launch()

        addVideoFromGalleryViewModel.emptyStateLiveData.observe(viewLifecycleOwner){
            if(it.mustShow){
                val emptyState = showEmptyState(R.layout.view_empty_state)
                emptyState?.let{view ->
                    val text = view.findViewById<TextView>(R.id.emptyStateMassageTv)
                    val btn = view.findViewById<MaterialButton>(R.id.emptyStateButton)
                    text.text = getString(it.messageResId)
                    videoLoadingService.release()
                    btn.visibility = if(it.mustShowCallToActionButton) View.VISIBLE else View.GONE
                    btn.setOnClickListener {
                        startActivity(Intent(requireContext(),AuthActivity::class.java))
                    }
                }
            }else{
                val emptyStateRoot = requireActivity().findViewById<View>(R.id.emptyStateRootView)
                emptyStateRoot?.visibility = View.GONE
            }
        }
    }

    override fun onStart() {
        super.onStart()
        addVideoFromGalleryViewModel.refresh(requireContext())
    }

    private fun displayFiles() {
        val listVideo = view?.findViewById<RecyclerView>(R.id.listVideoRV)
        listVideo!!.layoutManager = GridLayoutManager(requireContext(),3)
        listVideo.adapter = videosOnGalleryAdapter
        videosOnGalleryAdapter.videosOnGallery = videosOnGalleryList as ArrayList<VideosOnGallery>
    }

    override fun videoClick(videosOnGallery: VideosOnGallery) {
        if(video != null){
            videoLoadingService.release()
            videoLoadingService.defaultLoad(video!!,videosOnGallery.uri.toString(),requireContext())
        }else{
            videoLoadingService.defaultLoad(video!!,videosOnGallery.uri.toString(),requireContext())
        }
    }

    override fun btnSubmit(videosOnGallery: VideosOnGallery) {
        startActivity(Intent(requireContext(), UploadVideoActivity::class.java).apply {
            putExtra(EXTRA_KEY_DATA,videosOnGallery)
        })
        videoLoadingService.release()
        video == null
    }

}