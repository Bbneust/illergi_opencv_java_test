package com.example.illergi_opencv_java_test

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import kotlinx.android.synthetic.main.edit_profile.*
import kotlinx.android.synthetic.main.edit_profile.backArrow
import kotlinx.android.synthetic.main.edit_profile.bottomNavigatorBar

class EditProfileActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.edit_profile)

        personalInfoLayout.setOnClickListener(personalInfoOnClickListener)
        changePasswordLayout.setOnClickListener(changePasswordOnClickListener)
        backArrow.setOnClickListener(arrowOnClickListener)

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
                    val profileIntent =
                        Intent(this, ProfileActivity::class.java)
                    startActivity(profileIntent)
                }
            }
            false
        }
    }

    private val arrowOnClickListener: View.OnClickListener = View.OnClickListener {
        finish()
    }

    private val personalInfoOnClickListener: View.OnClickListener = View.OnClickListener {
        val personalInfoIntent =
            Intent(this, PersonalInfoActivity::class.java)
        startActivity(personalInfoIntent)
    }

    private val changePasswordOnClickListener: View.OnClickListener = View.OnClickListener {
        val changePasswordIntent =
            Intent(this, ChangePasswordActivity::class.java)
        startActivity(changePasswordIntent)
    }


}