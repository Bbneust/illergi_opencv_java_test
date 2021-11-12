package com.example.illergi_opencv_java_test

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import kotlinx.android.synthetic.main.manual_3.*


class Manual3Activity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.manual_3)

        Finish.setOnClickListener(finishOnClickListener)
        backArrow.setOnClickListener(backArrowOnClickListener)

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

    private val finishOnClickListener : View.OnClickListener = View.OnClickListener {
        val profileIntent = Intent(this@Manual3Activity, ProfileActivity::class.java)
        profileIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
        startActivity(profileIntent)
    }

    private val backArrowOnClickListener : View.OnClickListener = View.OnClickListener {
        finish()
    }
}