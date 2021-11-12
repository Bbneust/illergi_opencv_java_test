package com.example.illergi_opencv_java_test.utills

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.RecyclerView
import com.example.illergi_opencv_java_test.R
import com.example.illergi_opencv_java_test.entities.FoodData
import java.util.*
import kotlin.collections.ArrayList

class FoodRecycleViewAdapter (private val foods : ArrayList<FoodData>):
    RecyclerView.Adapter<FoodRecycleViewAdapter.FoodViewholder>() , Filterable {

    var foodsFilter = ArrayList<FoodData>()
    init {
        foodsFilter = foods as ArrayList<FoodData>
    }
//    private lateinit var mListener: onItemClickListener
//    interface onItemClickListener {
//        fun onItemClick(position: Int)
//    }

//    fun setOnItemClickListener(listener: onItemClickListener){
//        mListener = listener
//    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FoodViewholder {

        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.allergic2_listview,parent,false)
        return FoodViewholder(itemView)
    }

    override fun onBindViewHolder(holder: FoodViewholder, position: Int) {
        val currentItem = foodsFilter[position]
        holder.foodName.isSelected = currentItem.is_selected
        holder.foodName.text = currentItem.name

        holder.foodName.setOnClickListener{
            Log.d("TAG",currentItem.toString());
            currentItem.is_selected = !holder.foodName.isSelected
            holder.foodName.isSelected = !holder.foodName.isSelected
        }
    }

    override fun getItemCount(): Int {
        return foodsFilter.size
    }

    class FoodViewholder(itemView :View) : RecyclerView.ViewHolder(itemView){

        val foodName : Button = itemView.findViewById(R.id.foodName)

//        init {
//            itemView.setOnClickListener {
//                val intent = Intent(itemView.context,FoodListActivity::class.java)
//                itemView.context.startActivity(intent)
//                listener.onItemClick(adapterPosition)
//            }
//        }

    }


    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val charSearch = constraint.toString()
                if (charSearch.isEmpty()) {
                    foodsFilter = foods as ArrayList<FoodData>
                } else {
                    val resultList = ArrayList<FoodData>()
                    for (row in foods) {
                        if (row.name.lowercase(Locale.ROOT).contains(constraint.toString()
                                .lowercase(Locale.getDefault()))) {
                            resultList.add(row)
                        }
                    }
                    foodsFilter = resultList
                }
                val filterResults = FilterResults()
                filterResults.values = foodsFilter
                return filterResults
            }

            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                foodsFilter = results?.values as ArrayList<FoodData>
                notifyDataSetChanged()
            }
        }
    }

}