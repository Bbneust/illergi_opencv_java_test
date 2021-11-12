package com.example.illergi_opencv_java_test

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewpager.widget.ViewPager
import com.example.illergi_opencv_java_test.utills.ManualViewPagerAdapter
import kotlinx.android.synthetic.main.main_manual_fragment.view.*
import androidx.core.content.ContextCompat


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [MainManualFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class MainManualFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private lateinit var rootView: View

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }


    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.main_manual_fragment, container, false)
        val rec1param = rootView.rec1.layoutParams
        val rec2param = rootView.rec2.layoutParams
        val rec3param = rootView.rec3.layoutParams
        val manualViewPagerAdapter = ManualViewPagerAdapter(childFragmentManager)
        rootView.manualViewPager.adapter = manualViewPagerAdapter
        rootView.manualViewPager.currentItem = 0

        rootView.backArrow.setOnClickListener(backArrowOnClickListener)
        rootView.Next.setOnClickListener(nextOnClickListener)
        rootView.rec1.setOnClickListener { rootView.manualViewPager.currentItem = 0 }
        rootView.rec2.setOnClickListener { rootView.manualViewPager.currentItem = 1 }
        rootView.rec3.setOnClickListener { rootView.manualViewPager.currentItem = 2 }

        rootView.manualViewPager?.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {

            override fun onPageScrollStateChanged(state: Int) {
            }

            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {

            }

            override fun onPageSelected(position: Int) {
                when (position) {
                    0 -> {
                        rootView.Next.text = "NEXT"
                        rootView.backArrow.visibility = View.VISIBLE
                        rootView.rec1.setBackgroundColor(ContextCompat.getColor(context!!, R.color.green_2))
                        rootView.rec2.setBackgroundColor(ContextCompat.getColor(context!!, R.color.textin))
                        rootView.rec3.setBackgroundColor(ContextCompat.getColor(context!!, R.color.textin))
                    }
                    1 -> {
                        rootView.Next.text = "NEXT"
                        rootView.backArrow.visibility = View.VISIBLE
                        rootView.rec1.setBackgroundColor(ContextCompat.getColor(context!!, R.color.textin))
                         rootView.rec2.setBackgroundColor(ContextCompat.getColor(context!!, R.color.green_2))
                        rootView.rec3.setBackgroundColor(ContextCompat.getColor(context!!, R.color.textin))
                    }
                    2 -> {
                        rootView.backArrow.visibility = View.INVISIBLE
                        rootView.Next.text = "FINISH"
                        rootView.rec1.setBackgroundColor(ContextCompat.getColor(context!!, R.color.textin))
                        rootView.rec2.setBackgroundColor(ContextCompat.getColor(context!!, R.color.textin))
                        rootView.rec3.setBackgroundColor(ContextCompat.getColor(context!!, R.color.green_2))
                    }
                    else -> rootView.Next.text = "NEXT"
                }

            }

        })
        return rootView
    }


    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment MainManualFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            MainManualFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

    private val backArrowOnClickListener: View.OnClickListener = View.OnClickListener {
        activity?.onBackPressed()
    }

    private val nextOnClickListener: View.OnClickListener = View.OnClickListener {
        when (rootView.manualViewPager.currentItem) {
            0 -> rootView.manualViewPager.currentItem = 1
            1 -> rootView.manualViewPager.currentItem = 2
            2 -> activity?.onBackPressed()
        }
    }
}