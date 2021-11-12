package com.example.illergi_opencv_java_test

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.illergi_opencv_java_test.entities.UserData
import com.example.illergi_opencv_java_test.utills.ManualViewPagerAdapter
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.profile.*

class ProfileActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.profile)

        getUserName()

        editProfileButton.setOnClickListener(editProfileOnClickListener)
        checkoutButton.setOnClickListener(checkoutOnClickListener)
        manualButton.setOnClickListener(manualOnClickListener)
        signOutButton.setOnClickListener(signOutOnClickListener)
        bottomNavigatorBar.selectedItemId = R.id.profile
        bottomNavigatorBar.setOnNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.history -> {
                    Toast.makeText(this, "history", Toast.LENGTH_LONG).show()
                    val HistoryIntent = Intent(this, HistoryActivity::class.java)
                    startActivity(HistoryIntent)
                }
                R.id.camera -> {
                    Toast.makeText(this, "camera", Toast.LENGTH_LONG).show()
                    val cameraIntent =
                        Intent(this, CameraActivity::class.java)
                    startActivity(cameraIntent)
                }
                R.id.profile -> {
                    Toast.makeText(this, "profile", Toast.LENGTH_LONG).show()
                }
            }
            false
        }
    }

    private val editProfileOnClickListener: View.OnClickListener = View.OnClickListener {
        val editProfileIntent = Intent(this, EditProfileActivity::class.java)
        startActivity(editProfileIntent)
    }

    private val checkoutOnClickListener: View.OnClickListener = View.OnClickListener {
        val CategoryListIntent = Intent(this, CategoryListActivity::class.java)
        startActivity(CategoryListIntent)
        Toast.makeText(applicationContext, "Checkout Allergic food", Toast.LENGTH_SHORT).show()
    }

    private val manualOnClickListener: View.OnClickListener = View.OnClickListener {
        val Manual1Intent = Intent(this, Manual1Activity::class.java)
        startActivity(Manual1Intent)
        Toast.makeText(applicationContext, "Manual", Toast.LENGTH_SHORT).show()

        val pagerAdapter = ManualViewPagerAdapter(supportFragmentManager)
//        manualViewPager.adapter = pagerAdapter
//        manualViewPager.currentItem = 0
    }

    private val signOutOnClickListener: View.OnClickListener = View.OnClickListener {
        FirebaseAuth.getInstance().signOut()
        val LoginIntent = Intent(this, LoginActivity::class.java)
        LoginIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(LoginIntent)
        finish()

    }

    private fun getUserName(){

        val uid: String = FirebaseAuth.getInstance().currentUser!!.uid;
        val db = Firebase.firestore
        val userData = db.collection("users").document(uid)
        userData.get().addOnSuccessListener { data ->
            if (data != null) {
                val userLoginData: UserData? = data.toObject(UserData::class.java)
                Log.d("TAG", "${userLoginData}");
                Log.d("TAG", "${data.data}");
                firstnameTopic.text = userLoginData!!.firstname
                lastnameTopic.text = userLoginData.lastname
                EmailTopic.text = userLoginData.email
            } else {
                Log.d("TAG", "cannot find user");
            }
        }
    }


}