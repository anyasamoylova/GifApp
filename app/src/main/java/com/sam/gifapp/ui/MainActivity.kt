package com.sam.gifapp.ui

import android.os.Bundle
import android.os.PersistableBundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.tabs.TabLayout
import com.sam.gifapp.R
import com.sam.gifapp.network.GifRepository
import com.sam.gifapp.network.NetworkUtils
import com.sam.gifapp.viewmodels.GifViewModel

class MainActivity : AppCompatActivity() {
    private lateinit var tabLayout: TabLayout

    private lateinit var viewModel: GifViewModel

    private lateinit var latestGifFragment: GifFragment
    private lateinit var topGifFragment: GifFragment
    private lateinit var hotGifFragment: GifFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        tabLayout = findViewById(R.id.tabLayout)
        initFragments()
        viewModel = ViewModelProvider(this).get(GifViewModel::class.java)
        viewModel.repo = GifRepository(this)

        if (viewModel.tabPosition == -1) {
            //activity creates first time
            //set latest fragment as home fragment
            supportFragmentManager.beginTransaction().replace(R.id.container, latestGifFragment).commit()
            viewModel.tabPosition = 0
        } else {
            //set last selected tab
            tabLayout.selectTab(tabLayout.getTabAt(viewModel.tabPosition))
        }

        setTabLayoutListener()
    }

    private fun initFragments() {
        latestGifFragment = GifFragment()
        topGifFragment = GifFragment()
        hotGifFragment = GifFragment()

        latestGifFragment.arguments = Bundle().apply{putInt(GifFragment.FRAGMENT_TYPE, GifFragment.TYPE_LATEST)}
        topGifFragment.arguments = Bundle().apply{putInt(GifFragment.FRAGMENT_TYPE, GifFragment.TYPE_TOP)}
        hotGifFragment.arguments = Bundle().apply{putInt(GifFragment.FRAGMENT_TYPE, GifFragment.TYPE_HOT)}

    }

    private fun setTabLayoutListener() {
        tabLayout.addOnTabSelectedListener(object: TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                if (tab != null) {
                    val ft = supportFragmentManager.beginTransaction()
                    when (tab!!.position) {
                        0 -> ft.replace(R.id.container, latestGifFragment)
                        1 -> ft.replace(R.id.container, topGifFragment)
                        2 -> ft.replace(R.id.container, hotGifFragment)
                    }
                    ft.commit()
                    viewModel.tabPosition = tab!!.position
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {}

            override fun onTabReselected(tab: TabLayout.Tab?) {}

        })
    }
}