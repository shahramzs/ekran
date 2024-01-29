package com.example.ekran.feature.main.mainFragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.navOptions
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.ekran.R
import com.example.ekran.base.EXTRA_KEY_DATA
import com.example.ekran.base.EkranFragment
import com.example.ekran.feature.main.chip.ChipAdapter
import com.example.ekran.feature.main.videoDetails.VideoDetailsActivity
import com.example.ekran.feature.main.videoList.VideoListAdapter
import com.example.ekran.model.Category
import com.example.ekran.model.Videos
import com.example.ekran.utils.MainBottomSheet
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import timber.log.Timber

class MainFragment : EkranFragment(), VideoListAdapter.VideoOnClickListener {

    private val mainViewModel: MainViewModel by viewModel()
    private val chipAdapter: ChipAdapter by inject()
    private val videoListAdapter: VideoListAdapter by inject()
    private val options = navOptions {
        anim {
            enter = R.anim.fade_in
            exit = R.anim.fade_out
            popEnter = R.anim.fade_in
            popExit = R.anim.fade_out
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_main,container,false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        val chipListRv = view.findViewById<RecyclerView>(R.id.categoryChipRv)
        val videoListRv = view.findViewById<RecyclerView>(R.id.videoListRv)

        chipListRv.layoutManager =
            LinearLayoutManager(requireContext(), RecyclerView.HORIZONTAL, false)
        videoListRv.layoutManager =
            LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)
        chipListRv.adapter = chipAdapter
        videoListRv.adapter = videoListAdapter

        videoListAdapter.videoOnClickListener = this

        mainViewModel.videoLiveData.observe(viewLifecycleOwner) {
            Timber.tag("videos").d(it.toString())
            videoListAdapter.videos = it as ArrayList<Videos>
        }
        mainViewModel.categoryLiveData.observe(viewLifecycleOwner) {
            Timber.tag("category").d(it.toString())
            chipAdapter.category = it as ArrayList<Category>
        }
        mainViewModel.progressBarLiveData.observe(viewLifecycleOwner) {
            setProgressIndicator(it)
        }
        mainViewModel.bannerLiveData.observe(viewLifecycleOwner) {
            Timber.tag("banner").d(it.toString())
        }
    }

    override fun onVideoClick(view:View,video: Videos) {
        startActivity(Intent(requireContext(),VideoDetailsActivity::class.java).apply {
            putExtra(EXTRA_KEY_DATA,video)
        })
    }

    override fun onMoreOptionClick(video: Videos) {
        val mainBottomSheet = MainBottomSheet(video)
        mainBottomSheet.show(parentFragmentManager,MainBottomSheet.TAG)
    }

}