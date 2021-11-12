package com.example.illergi_opencv_java_test.utills

import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.illergi_opencv_java_test.CategoryListFragment
import com.example.illergi_opencv_java_test.FoodListActivity
import com.example.illergi_opencv_java_test.R
import com.example.illergi_opencv_java_test.entities.CategoryData
import java.util.*
import kotlin.collections.ArrayList

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.fragment.app.FragmentManager
import com.example.illergi_opencv_java_test.FoodListFragment


class CategoryRecycleViewAdapter(
    private val context: CategoryListFragment,
    private val Categories: ArrayList<CategoryData>,
    private val foodListFragment: FoodListFragment
) :
    RecyclerView.Adapter<CategoryRecycleViewAdapter.CategoryViewholder>(), Filterable {

    var categoriesFilter = ArrayList<CategoryData>()

    init {
        categoriesFilter = Categories as ArrayList<CategoryData>
    }
//
//    private lateinit var mListener: onItemClickListener
//    interface onItemClickListener {
//        fun onItemClick(position: Int)
//    }
//
//    fun setOnItemClickListener(listener: onItemClickListener){
//        mListener = listener
//    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewholder {

        val itemView =
            LayoutInflater.from(parent.context).inflate(R.layout.allergic1_listview, parent, false)
        return CategoryViewholder(itemView)
    }

    override fun onBindViewHolder(holder: CategoryViewholder, position: Int) {
        val currentItem = categoriesFilter[position]
        holder.categoryName.text = currentItem.name

        holder.itemView.setOnClickListener {

//            val foodListIntent = Intent(holder.itemView.getContext(),FoodListActivity::class.java)
//            Log.d("TAG",currentItem.toString());
//            foodListIntent.putExtra("categoryData", currentItem as Serializable)
//            holder.itemView.getContext().startActivity(foodListIntent)
            val fragment: Fragment = foodListFragment
            val args = Bundle()
            args.putSerializable("categoryData", currentItem)
            fragment.setArguments(args)
//            val manager = categoryListFragment.activity?.supportFragmentManager
            val manager: FragmentManager? = context.fragmentManager
            val transaction = manager?.beginTransaction()
            Log.d("TAG", transaction.toString())
            if (transaction != null) {
                transaction.setCustomAnimations(
                    R.anim.slide_in,
                    R.anim.slide_out,
                    R.anim.slide_in,
                    R.anim.slide_out
                )
                transaction.replace(R.id.categoryListFragmentContainer, fragment)
                transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                transaction.addToBackStack(null)
                transaction.commit()
            }

        }
    }

    override fun getItemCount(): Int {
        return categoriesFilter.size
    }

    class CategoryViewholder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val categoryName: TextView = itemView.findViewById(com.example.illergi_opencv_java_test.R.id.categoryName)
        val categoryLayout: CardView = itemView.findViewById(R.id.cardView)

        init {
            itemView.setOnClickListener {
                val foodListIntent = Intent(itemView.context, FoodListActivity::class.java)
                itemView.context.startActivity(foodListIntent)
//                    listener.onItemClick(adapterPosition)

            }
        }

    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val charSearch = constraint.toString()
                categoriesFilter = if (charSearch.isEmpty()) {
                    Categories as ArrayList<CategoryData>
                } else {
                    val resultList = ArrayList<CategoryData>()
                    for (row in Categories) {
                        if (row.name.lowercase(Locale.ROOT).contains(
                                constraint.toString()
                                    .lowercase(Locale.getDefault())
                            )
                        ) {
                            resultList.add(row)
                        }
                    }
                    resultList
                }
                val filterResults = FilterResults()
                filterResults.values = categoriesFilter
                return filterResults
            }

            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                categoriesFilter = results?.values as ArrayList<CategoryData>
                notifyDataSetChanged()
            }
        }
    }

}