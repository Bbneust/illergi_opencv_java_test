package com.example.illergi_opencv_java_test

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import com.example.illergi_opencv_java_test.entities.CategoryData
import com.example.illergi_opencv_java_test.entities.FoodData
import com.example.illergi_opencv_java_test.entities.UserAllergyFood
import com.example.illergi_opencv_java_test.utills.FoodRecycleViewAdapter
import com.google.android.flexbox.FlexboxLayoutManager
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.allergic_food_2.*
import kotlinx.android.synthetic.main.allergic_food_2.backArrow
import kotlinx.android.synthetic.main.allergic_food_2.bottomNavigatorBar
import com.google.android.flexbox.AlignItems

import com.google.android.flexbox.JustifyContent

import com.google.android.flexbox.FlexDirection
import com.google.firebase.auth.FirebaseAuth


class FoodListActivity : AppCompatActivity() {

    private lateinit var foodArrayList: ArrayList<FoodData>
    private lateinit var userFoodList: UserAllergyFood
    private lateinit var categoryData: CategoryData
    private val uid: String = FirebaseAuth.getInstance().currentUser!!.uid;
    private lateinit var fieldName: String
    private lateinit var foodRecycleViewAdapter : FoodRecycleViewAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.allergic_food_2)

        val layoutManager = FlexboxLayoutManager(this)
        layoutManager.flexDirection = FlexDirection.ROW
        layoutManager.justifyContent = JustifyContent.CENTER
        layoutManager.alignItems = AlignItems.CENTER
        foodRecycleView.layoutManager = layoutManager

//        foodRecycleView.layoutManager = FlexboxLayoutManager(this)
        foodRecycleView.setHasFixedSize(true)


        categoryData = intent.extras?.get("categoryData") as CategoryData
        Log.d("TAG", categoryData.toString());
        fieldName = categoryData.name.toString().replace("\\s".toRegex(), "").replaceFirstChar { it.lowercase() }
        foodArrayList = arrayListOf<FoodData>()

        categoryHeadText.text = categoryData.name
        val db = Firebase.firestore

        db.collection("user_allergy_food").document(uid).get()
            .addOnSuccessListener { foodList ->
                    userFoodList = foodList.toObject(UserAllergyFood::class.java)!!
                    Log.d("TAG", foodList.toString());
//                Log.d("TAG", userFoodList.food_list.toString());

                    db.collection("food_list").whereEqualTo("category_id", categoryData.id).get()
                        .addOnSuccessListener { foodList ->
                            Log.d("TAG", foodList.size().toString())
                            for (item in foodList) {
                                val documentId: String = item.id
                                val foodData: FoodData = item.toObject(FoodData::class.java)
                                foodData.id = documentId
                                val getUserFoodList: List<String>? = userFoodList.get(fieldName)
                                Log.d("TAG", getUserFoodList.toString())
                                if (getUserFoodList != null) {
                                    if (getUserFoodList.contains(documentId) == true) {
                                        foodData.is_selected = true
                                    }
                                }
                                Log.d("TAG", foodData.name)
                                foodArrayList.add(foodData)
                            }
                            foodRecycleViewAdapter = FoodRecycleViewAdapter(foodArrayList)
                            foodRecycleView.adapter = foodRecycleViewAdapter
                        }
            }

        hintSearch.setOnQueryTextListener(object: SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                foodRecycleViewAdapter.filter.filter(newText)
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
        saveButton.setOnClickListener(saveOnClickListener)
        hintSearch.setOnClickListener(searchViewOnClick)
//        clearButton.setOnClickListener(clearButtonOnClick)
    }

    private val saveOnClickListener: View.OnClickListener = View.OnClickListener {
        val foodListFilter = foodArrayList.filter { foodData -> foodData.is_selected.equals(true) }
        val foodListUpdate: ArrayList<String> = arrayListOf<String>()
        foodListFilter.forEach { foodData ->
            foodListUpdate.add(foodData.id)
        }
        Firebase.firestore.collection("user_allergy_food").document(uid)
            .update(fieldName, foodListUpdate)
            .addOnSuccessListener {
                Toast.makeText(this, "update success", Toast.LENGTH_SHORT).show()
                finish()
            }
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

