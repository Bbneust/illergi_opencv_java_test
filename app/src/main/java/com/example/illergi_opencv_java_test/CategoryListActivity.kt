package com.example.illergi_opencv_java_test

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.illergi_opencv_java_test.entities.CategoryData
import com.example.illergi_opencv_java_test.utills.CategoryRecycleViewAdapter
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.allergic_food_1.*

class CategoryListActivity : AppCompatActivity() {

    //    private lateinit var categoryRecyclerView: RecyclerView
    private lateinit var categoryArrayList: ArrayList<CategoryData>
    private lateinit var categoryRecycleViewAdapter : CategoryRecycleViewAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.allergic_food_1)

        categoryRecyclerView.layoutManager = LinearLayoutManager(this)
        categoryRecyclerView.setHasFixedSize(true)

        categoryArrayList = arrayListOf<CategoryData>()

        val db = Firebase.firestore
        val CategoryList = db.collection("categories_list")
        CategoryList.get().addOnSuccessListener { list ->
            for (item in list) {
                val categoryData: CategoryData = item.toObject(CategoryData::class.java)
                categoryArrayList.add(categoryData)
            }
//            categoryRecycleViewAdapter = CategoryRecycleViewAdapter(categoryArrayList)
//            categoryRecyclerView.adapter = categoryRecycleViewAdapter

        }
        hintSearch.setOnQueryTextListener(object: SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                categoryRecycleViewAdapter.filter.filter(newText)
                return false
            }

        })

        bottomNavigatorBar.selectedItemId = R.id.profile
        bottomNavigatorBar.setOnNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.history -> {
                    Toast.makeText(this, "history", Toast.LENGTH_LONG).show()
                    val HistoryIntent = Intent(this, HistoryActivity::class.java)
                    startActivity(HistoryIntent)
                    finish()
                }
                R.id.camera -> {
                    Toast.makeText(this, "camera", Toast.LENGTH_LONG).show()
                    val cameraIntent = Intent(this, CameraActivity::class.java)
                    startActivity(cameraIntent)
                    finish()
                }
                R.id.profile -> {
                    Toast.makeText(this, "profile", Toast.LENGTH_LONG).show()
                    val ProfileIntent = Intent(this, ProfileActivity::class.java)
                    startActivity(ProfileIntent)
                    finish()
                }
            }
            false
        }

        backArrow.setOnClickListener(arrowOnClickListener)
        hintSearch.setOnClickListener(searchViewOnClick)
//        clearButton.setOnClickListener(clearButtonOnClick)


    }

    private val arrowOnClickListener: View.OnClickListener = View.OnClickListener {
        finish()
    }
    private val searchViewOnClick: View.OnClickListener = View.OnClickListener {
        hintSearch.setIconified(false);
    }
    private val clearButtonOnClick: View.OnClickListener = View.OnClickListener {
        hintSearch.setQuery("", false);
    }
}