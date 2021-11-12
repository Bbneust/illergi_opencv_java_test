package com.example.illergi_opencv_java_test.utills

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.illergi_opencv_java_test.HistoryFragment
import com.example.illergi_opencv_java_test.R
import com.example.illergi_opencv_java_test.ShowResultFragment
import com.example.illergi_opencv_java_test.entities.HistoryData
import com.google.firebase.auth.FirebaseAuth

class HistoryRecycleViewAdapter(private val context: HistoryFragment, private val Histories: ArrayList<HistoryData>):
    RecyclerView.Adapter<HistoryRecycleViewAdapter.HistoryViewholder>() {
//
//    private lateinit var mListener: onItemClickListener
//    interface onItemClickListener {
//        fun onItemClick(position: Int)
//    }
//
//    fun setOnItemClickListener(listener: onItemClickListener){
//        mListener = listener
//    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryViewholder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.history_listview,parent,false)
        return HistoryViewholder(itemView)
    }

    override fun onBindViewHolder(holder: HistoryViewholder, position: Int) {
        val currentItem = Histories[position]
        holder.titleName.text = currentItem.title_name
        holder.date.text = currentItem.timestamp
        Glide.with(context).load(currentItem.image_url).into(holder.image)

        val uid: String = FirebaseAuth.getInstance().currentUser!!.uid;
        holder.itemView.setOnClickListener{
            val firestoreId = uid+"_"+currentItem.image_name
            val showResultFragment: Fragment = ShowResultFragment()
            val args = Bundle()
            args.putSerializable("Key","2")
            args.putSerializable("FirestoreId",firestoreId)
            showResultFragment.arguments = args
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
                transaction.replace(R.id.cropImageFragmentContainer, showResultFragment)
                transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                transaction.addToBackStack(null)
                transaction.commit()
            }
        }
    }

    override fun getItemCount(): Int {
        return Histories.size
    }

    class HistoryViewholder(itemView :View) : RecyclerView.ViewHolder(itemView){



        val titleName :TextView = itemView.findViewById(R.id.titleName)
        val date : TextView = itemView.findViewById(R.id.textDate)
        val image :ImageView = itemView.findViewById(R.id.historyImageView)
//        val categoryLayout : CardView = itemView.findViewById(R.id.cardView)

//        init {
//                itemView.setOnClickListener {
//                    val foodListIntent = Intent(itemView.context,FoodListActivity::class.java)
//                    itemView.context.startActivity(foodListIntent)
//                    listener.onItemClick(adapterPosition)

//                }
//        }

    }

}