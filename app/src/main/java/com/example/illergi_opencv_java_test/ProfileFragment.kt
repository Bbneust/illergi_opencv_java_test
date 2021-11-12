package com.example.illergi_opencv_java_test


import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.illergi_opencv_java_test.entities.UserData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import android.content.Intent

import kotlinx.android.synthetic.main.profile_fragment.view.*
import maes.tech.intentanim.CustomIntent.customType


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"


/**
 * A simple [Fragment] subclass.
 * Use the [ProfileFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ProfileFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    lateinit var rootView : View

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }

//        getUserName()

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        getUserName()
        rootView = inflater.inflate(R.layout.profile_fragment, container, false)
        rootView.editProfileButton.setOnClickListener(editProfileOnClickListener)
        rootView.checkoutButton.setOnClickListener(checkoutOnClickListener)
        rootView.manualButton.setOnClickListener(manualOnClickListener)
        rootView.signOutButton.setOnClickListener(signOutOnClickListener)
        return rootView
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment ProfileFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String?, param2: String?) =
            ProfileFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

    private val editProfileOnClickListener: View.OnClickListener = View.OnClickListener {
//        val editProfileIntent = Intent(this, EditProfileActivity::class.java)
//        startActivity(editProfileIntent)
        val editProfileFragment = EditProfileFragment()
        val manager = fragmentManager
        val transaction = manager?.beginTransaction()

        if (transaction != null) {
            transaction. setCustomAnimations(R.anim.slide_in, R.anim.slide_out,R.anim.slide_in, R.anim.slide_out)
            transaction.replace(R.id.mainFragmentContainer,editProfileFragment)
            transaction.addToBackStack(null)
            transaction.commit()
        }
    }

    private val checkoutOnClickListener: View.OnClickListener = View.OnClickListener {
//        val CategoryListIntent = Intent(this, CategoryListActivity::class.java)
//        startActivity(CategoryListIntent)
        Toast.makeText(context, "Checkout Allergic food", Toast.LENGTH_SHORT).show()

        val categoryListFragment = CategoryListFragment()
        val manager = fragmentManager
        val transaction = manager?.beginTransaction()

        if (transaction != null) {
            transaction. setCustomAnimations(R.anim.slide_in, R.anim.fade_out,R.anim.fade_in, R.anim.slide_out)
//            transaction. setCustomAnimations(R.anim.slide_in, R.anim.slide_out,R.anim.slide_in, R.anim.slide_out)
            transaction.replace(R.id.mainFragmentContainer,categoryListFragment)
            transaction.addToBackStack(null)
            transaction.commit()
        }
    }

    private val manualOnClickListener: View.OnClickListener = View.OnClickListener {
//        val Manual1Intent = Intent(this, Manual1Activity::class.java)
//        startActivity(Manual1Intent)
        Toast.makeText(context, "Manual", Toast.LENGTH_SHORT).show()

//        val pagerAdapter = ManualViewPagerAdapter(supportFragmentManager)
//        manualViewPager.adapter = pagerAdapter
//        manualViewPager.currentItem = 0
        val mainManualFragment = MainManualFragment()
        val manager = fragmentManager
        val transaction = manager?.beginTransaction()

        if (transaction != null) {
            transaction. setCustomAnimations(R.anim.slide_in, R.anim.slide_out,R.anim.slide_in, R.anim.slide_out)
            transaction.replace(R.id.mainFragmentContainer,mainManualFragment)
            transaction.addToBackStack(null)
            transaction.commit()
        }
    }

    private val signOutOnClickListener: View.OnClickListener = View.OnClickListener {
        FirebaseAuth.getInstance().signOut()
        val LoginIntent = Intent(context, LoginActivity::class.java)
        LoginIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(LoginIntent)
        customType(context,"rotateout-to-rotatein");
        activity?.getFragmentManager()?.popBackStack()

    }

    private fun getUserName() {

        val uid: String = FirebaseAuth.getInstance().currentUser!!.uid;
        val db = Firebase.firestore
        val userData = db.collection("users").document(uid)
        userData.get().addOnSuccessListener { data ->
            if (data != null) {
                val userLoginData: UserData? = data.toObject(UserData::class.java)
                Log.d("TAG", "${userLoginData}");
                Log.d("TAG", "${data.data}");
                rootView.firstnameTopic.text = userLoginData!!.firstname
                rootView.lastnameTopic.text = userLoginData.lastname
                rootView.EmailTopic.text = userLoginData.email
            } else {
                Log.d("TAG", "cannot find user");
            }
        }
    }
}

