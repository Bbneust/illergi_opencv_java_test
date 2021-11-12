package com.example.illergi_opencv_java_test.utills

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.example.illergi_opencv_java_test.Manual1Fragment
import com.example.illergi_opencv_java_test.Manual2Fragment
import com.example.illergi_opencv_java_test.Manual3Fragment

class ManualViewPagerAdapter(fragmentManager: FragmentManager):FragmentPagerAdapter(fragmentManager) {
    override fun getCount(): Int {
        return 3
    }


    override fun getItemPosition(`object`: Any): Int {
        return POSITION_NONE
    }

    override fun getItem(position: Int): Fragment {
       return when(position){
            0 ->  Manual1Fragment.newInstance(null,null)
            1 -> Manual2Fragment.newInstance(null,null)
            2 -> Manual3Fragment.newInstance(null,null)
            else ->  Manual1Fragment.newInstance(null,null)
        }

    }
}