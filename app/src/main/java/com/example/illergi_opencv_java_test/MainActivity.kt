package com.example.illergi_opencv_java_test

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.viewpager.widget.ViewPager

import com.example.illergi_opencv_java_test.utills.MainViewPagerAdapter
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_main.bottomNavigatorBar


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val mainViewPagerAdapter = MainViewPagerAdapter(supportFragmentManager)
        mainViewPager.adapter = mainViewPagerAdapter
        mainViewPager.currentItem = 1
        bottomNavigatorBar.selectedItemId = R.id.camera


        mainViewPager?.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {

            override fun onPageScrollStateChanged(state: Int) {
            }

            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {

            }
            override fun onPageSelected(position: Int) {
                when(position){
                    0 -> {
                        bottomNavigatorBar.selectedItemId = R.id.history
                    }
                    1 -> bottomNavigatorBar.selectedItemId = R.id.camera
                    2 -> bottomNavigatorBar.selectedItemId = R.id.profile
                    else -> bottomNavigatorBar.selectedItemId = R.id.camera
                }

            }

        })

        bottomNavigatorBar.setOnNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.history -> {
                    mainViewPager.currentItem = 0
                }
                R.id.camera -> {
                    mainViewPager.currentItem = 1
                }
                R.id.profile -> {
                    mainViewPager.currentItem = 2
                }
            }
            true
        }


    }
}