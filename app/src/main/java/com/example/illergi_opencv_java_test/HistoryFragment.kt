package com.example.illergi_opencv_java_test

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.illergi_opencv_java_test.entities.HistoryData
import com.example.illergi_opencv_java_test.utills.HistoryRecycleViewAdapter
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.history_fragment.*
import kotlinx.android.synthetic.main.history_fragment.view.*


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [HistoryFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class HistoryFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private lateinit var rootView : View

    private lateinit var HistoryArrayList: ArrayList<HistoryData>

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
        rootView = inflater.inflate(R.layout.history_fragment, container, false)

        HistoryArrayList = arrayListOf<HistoryData>()

        val uid: String = FirebaseAuth.getInstance().currentUser!!.uid;
        val db = Firebase.firestore
        val historyList = db.collection("history").whereEqualTo("uid", uid)
        historyList.get().addOnSuccessListener { list ->
            for (item in list) {
                val historyData: HistoryData = item.toObject(HistoryData::class.java)
                Log.d("TAG", historyData.toString())
                HistoryArrayList.add(historyData)
            }
            historyRecycleView.adapter = HistoryRecycleViewAdapter(this,HistoryArrayList)
            Log.d("TAG", HistoryArrayList.size.toString())
            if(HistoryArrayList.size == 0){
//                rootView.noHistoryText.setEnabled(true)
            }else{
                rootView.noHistoryText.text = ""
            }
        }

        rootView.historyRecycleView.layoutManager = LinearLayoutManager(rootView.context)
        rootView.historyRecycleView.setHasFixedSize(true)
        // Inflate the layout for this fragment
        return rootView
    }


    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment HistoryFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String?, param2: String?) =
            HistoryFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}