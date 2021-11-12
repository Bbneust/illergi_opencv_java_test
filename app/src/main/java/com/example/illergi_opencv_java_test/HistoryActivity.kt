package com.example.illergi_opencv_java_test

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.illergi_opencv_java_test.entities.HistoryData

class HistoryActivity : AppCompatActivity() {

    private lateinit var HistoryArrayList: ArrayList<HistoryData>
    private lateinit var context:Context

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.history)

//        historyRecycleView.layoutManager = LinearLayoutManager(this)
//        historyRecycleView.setHasFixedSize(true)
//
//        HistoryArrayList = arrayListOf<HistoryData>()
//
//        val uid: String = FirebaseAuth.getInstance().currentUser!!.uid;
//        val db = Firebase.firestore
//        val historyList = db.collection("history").whereEqualTo("uid", uid)
//        historyList.get().addOnSuccessListener { list ->
//            for (item in list) {
//                val historyData: HistoryData = item.toObject(HistoryData::class.java)
//                Log.d("TAG", historyData.toString())
//                HistoryArrayList.add(historyData)
//            }
////            historyRecycleView.adapter = HistoryRecycleViewAdapter(this,HistoryArrayList)
//
//        }
//
//
//        bottomNavigatorBar.selectedItemId = R.id.history
//        bottomNavigatorBar.setOnNavigationItemSelectedListener {
//            when (it.itemId) {
//                R.id.history -> {
//                    //now on this page
//                    Toast.makeText(this, "history", Toast.LENGTH_LONG).show()
//                }
//                R.id.camera -> {
//                    Toast.makeText(this, "camera", Toast.LENGTH_LONG).show()
//                    val cameraIntent = Intent(this, CameraActivity::class.java)
//                    startActivity(cameraIntent)
//                    finish()
//                }
//                R.id.profile -> {
//                    Toast.makeText(this, "profile", Toast.LENGTH_LONG).show()
//                    val ProfileIntent = Intent(this, ProfileActivity::class.java)
//                    startActivity(ProfileIntent)
//                    finish()
//                }
//            }
//            false
//        }
    }
}