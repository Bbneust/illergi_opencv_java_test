package com.example.illergi_opencv_java_test.utills

import android.os.Build
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import com.example.illergi_opencv_java_test.*

class MainViewPagerAdapter(fragmentManager: FragmentManager):FragmentStatePagerAdapter(fragmentManager) {

    override fun getCount(): Int {
        return 3
    }

    override fun getItemPosition(`object`: Any): Int {
        return POSITION_NONE
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        return super.instantiateItem(container, position)
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun getItem(position: Int): Fragment {
        return when(position){
            0 ->  HistoryFragment.newInstance(null,null)
            1 -> CameraFragment.newInstance(null,null)
            2 -> ProfileFragment.newInstance(null,null)
            else ->  CameraFragment.newInstance(null,null)
        }

    }
}