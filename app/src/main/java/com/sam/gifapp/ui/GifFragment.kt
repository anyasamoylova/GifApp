package com.sam.gifapp.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.widget.ViewPager2
import com.sam.gifapp.R
import com.sam.gifapp.model.Gif
import com.sam.gifapp.viewmodels.DataState
import com.sam.gifapp.viewmodels.GifViewModel

class GifFragment : Fragment() {
    companion object {
        const val FRAGMENT_TYPE = "FragmentType"
        const val TYPE_LATEST = 0
        const val TYPE_TOP = 1
        const val TYPE_HOT = 2
    }
    private lateinit var viewPager: ViewPager2
    private lateinit var progressBar: ProgressBar
    private lateinit var llError: LinearLayout
    private lateinit var ivError: ImageView
    private lateinit var tvError: TextView
    private lateinit var btnBack: FrameLayout
    private lateinit var btnForward: FrameLayout
    private lateinit var llButtons: LinearLayout
    private lateinit var adapter: ViewPagerAdapter

    private lateinit var viewModel: GifViewModel

    private var fragmentType = -1

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        val root = inflater.inflate(R.layout.fragment_gif, container, false)

        btnBack = root.findViewById(R.id.btnBack)
        btnForward = root.findViewById(R.id.btnForward)
        llButtons = root.findViewById(R.id.llButtons)
        viewPager = root.findViewById(R.id.vpCards)
        progressBar = root.findViewById(R.id.progressBar)
        llError = root.findViewById(R.id.llError)
        ivError = root.findViewById(R.id.ivError)
        tvError = root.findViewById(R.id.tvError)

        viewModel = ViewModelProvider(requireActivity()).get(GifViewModel::class.java)

        fragmentType = requireArguments().getInt(FRAGMENT_TYPE, -1)

        initAdapter()

        observeGifs()
        //download first portion of gifs
        downloadGifPage()

        //btnBack should not be active if it's first gif
        btnBack.isClickable = false

        //download new gifs if user clicks on btnForward
        btnForward.setOnClickListener {
            downloadGifPage()
            viewPager.currentItem = viewPager.currentItem + 1
            viewModel.increasePosition(fragmentType)
            btnBack.isClickable = true
        }

        btnBack.setOnClickListener {
            viewPager.currentItem = viewPager.currentItem - 1
            viewModel.decreasePosition(fragmentType)
            if (adapter.isFirstPage())
                btnBack.isClickable = false
        }

        llError.setOnClickListener{
            downloadGifPage()
        }

        return root
    }

    private fun handleDataState(dataState: DataState) {
        clearScreen()
        when (dataState) {
            DataState.DOWNLOAD -> {
                progressBar.visibility = View.VISIBLE
                llButtons.visibility = View.VISIBLE
            }
            DataState.SUCCESS -> {
                viewPager.visibility = View.VISIBLE
                llButtons.visibility = View.VISIBLE
            }
            DataState.ERROR, DataState.EMPTY -> {
                llError.visibility = View.VISIBLE
                if (dataState == DataState.ERROR) {
                    ivError.setImageResource(R.drawable.error_internet)
                    tvError.text = "Ooops, something goes wrong, click to try again"
                } else {
                    ivError.setImageResource(R.drawable.error_empty)
                    tvError.text = "Memes are over here :(((("
                }
            }
        }
    }

    private fun clearScreen() {
        viewPager.visibility = View.GONE
        progressBar.visibility = View.GONE
        llError.visibility = View.GONE
        llButtons.visibility = View.GONE
    }

    private fun initAdapter() {
        adapter = ViewPagerAdapter()
        viewPager.adapter = adapter
        when (fragmentType) {
            TYPE_LATEST -> {
                adapter.setGifs(viewModel.latestGifs.value!!)
                viewPager.currentItem = viewModel.positionLatest
            }
            TYPE_TOP -> {
                adapter.setGifs(viewModel.topGifs.value!!)
                viewPager.currentItem = viewModel.positionTop
            }
            TYPE_HOT -> {
                adapter.setGifs(viewModel.hotGifs.value!!)
                viewPager.currentItem = viewModel.positionHot
            }
        }
    }

    private fun observeGifs() {
        val gifsObserver =
            Observer<List<Gif>> { gifs ->
                if (gifs != null) {
                    adapter.setGifs(gifs)
                }
            }
        val stateObserver =
            Observer<DataState> { state ->
                handleDataState(state)
            }
        when (fragmentType) {
            TYPE_LATEST -> {
                viewModel.latestGifs.observe(viewLifecycleOwner, gifsObserver)
                viewModel.dataLatestState.observe(viewLifecycleOwner, stateObserver)
            }
            TYPE_TOP -> {
                viewModel.topGifs.observe(viewLifecycleOwner, gifsObserver)
                viewModel.dataTopState.observe(viewLifecycleOwner, stateObserver)
            }
            TYPE_HOT -> {
                viewModel.hotGifs.observe(viewLifecycleOwner, gifsObserver)
                viewModel.dataHotState.observe(viewLifecycleOwner, stateObserver)
            }
        }
    }

    private fun downloadGifPage() {
        if (adapter.isLastPage()) {
            when (fragmentType) {
                TYPE_LATEST -> viewModel.updateLatestGif()
                TYPE_TOP -> viewModel.updateTopGif()
                TYPE_HOT -> viewModel.updateHotGif()
            }
        }
    }
}