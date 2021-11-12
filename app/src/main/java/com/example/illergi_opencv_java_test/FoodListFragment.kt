package com.example.illergi_opencv_java_test

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import com.example.illergi_opencv_java_test.entities.CategoryData
import com.example.illergi_opencv_java_test.entities.FoodData
import com.example.illergi_opencv_java_test.entities.UserAllergyFood
import com.example.illergi_opencv_java_test.utills.FoodRecycleViewAdapter
import com.google.android.flexbox.AlignItems
import com.google.android.flexbox.FlexDirection
import com.google.android.flexbox.FlexboxLayoutManager
import com.google.android.flexbox.JustifyContent
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.allergic_food_2.*
import kotlinx.android.synthetic.main.food_list_fragment.view.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [FoodListFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class FoodListFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private lateinit var rootView : View

    private lateinit var foodArrayList: ArrayList<FoodData>
    private lateinit var userFoodList: UserAllergyFood
    private lateinit var categoryData: CategoryData
    private val uid: String = FirebaseAuth.getInstance().currentUser!!.uid;
    private lateinit var fieldName: String
    private lateinit var foodRecycleViewAdapter : FoodRecycleViewAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }


//        Log.d("TAG", categoryData.toString());
//        foodArrayList = arrayListOf<FoodData>()


    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        rootView = inflater.inflate(R.layout.food_list_fragment, container, false)

        categoryData  = arguments?.getSerializable("categoryData") as CategoryData;
        Log.d("TAG",categoryData.toString())

        rootView.categoryHeadText.text = categoryData.name
        fieldName = categoryData.name.toString().replace("\\s".toRegex(), "").replaceFirstChar { it.lowercase() }

        val layoutManager = FlexboxLayoutManager(context)
        layoutManager.flexDirection = FlexDirection.ROW
        layoutManager.justifyContent = JustifyContent.CENTER
        layoutManager.alignItems = AlignItems.CENTER
        rootView.foodRecycleView.layoutManager = layoutManager
        rootView.foodRecycleView.setHasFixedSize(true)

        val db = Firebase.firestore
        foodArrayList = arrayListOf<FoodData>()
        db.collection("user_allergy_food").document(uid).get()
            .addOnSuccessListener { foodList ->
                Log.d("TAG", foodList.toString());
                userFoodList = foodList.toObject(UserAllergyFood::class.java)!!
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
                        rootView.foodRecycleView.adapter = foodRecycleViewAdapter
                    }
            }

        // Inflate the layout for this fragment

        rootView.saveButton.setOnClickListener(saveOnClickListener)
        rootView.hintSearch.setOnClickListener(searchViewOnClick)

        rootView.hintSearch.setOnQueryTextListener(object: SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                foodRecycleViewAdapter.filter.filter(newText)
                return false
            }

        })


        return rootView
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
                Toast.makeText(context, "update success", Toast.LENGTH_SHORT).show()
                activity?.onBackPressed()
            }
    }

    private val searchViewOnClick: View.OnClickListener = View.OnClickListener {
        hintSearch.setIconified(false);
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment FoodListFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            FoodListFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}