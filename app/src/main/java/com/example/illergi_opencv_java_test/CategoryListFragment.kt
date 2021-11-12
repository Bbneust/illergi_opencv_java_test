package com.example.illergi_opencv_java_test

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.illergi_opencv_java_test.entities.CategoryData
import com.example.illergi_opencv_java_test.utills.CategoryRecycleViewAdapter
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.category_list_fragment.view.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [CategoryListFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class CategoryListFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private lateinit var categoryArrayList: ArrayList<CategoryData>
    private lateinit var categoryRecycleViewAdapter : CategoryRecycleViewAdapter
    private lateinit var rootView : View

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }

        categoryArrayList = arrayListOf<CategoryData>()

        val db = Firebase.firestore
        val CategoryList = db.collection("categories_list")
        CategoryList.get().addOnSuccessListener { list ->
            for (item in list) {
                val categoryData: CategoryData = item.toObject(CategoryData::class.java)
                categoryArrayList.add(categoryData)
            }
            categoryRecycleViewAdapter = CategoryRecycleViewAdapter(this,categoryArrayList,FoodListFragment())
            rootView.categoryRecyclerView.adapter = categoryRecycleViewAdapter

        }

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        rootView = inflater.inflate(R.layout.category_list_fragment, container, false)
        rootView.categoryRecyclerView.layoutManager = LinearLayoutManager(rootView.context)
        rootView.categoryRecyclerView.setHasFixedSize(true)
        rootView.backArrow.setOnClickListener(arrowOnClickListener)
        rootView.hintSearch.setOnClickListener(searchViewOnClick)
        rootView.hintSearch.setOnQueryTextListener(object: SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                categoryRecycleViewAdapter.filter.filter(newText)
                return false
            }

        })


        // Inflate the layout for this fragment
        return rootView
    }

    private val arrowOnClickListener: View.OnClickListener = View.OnClickListener {
        activity?.onBackPressed()
    }
    private val searchViewOnClick: View.OnClickListener = View.OnClickListener {
        rootView.hintSearch.isIconified = false;
    }


    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment CategoryListFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            CategoryListFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}