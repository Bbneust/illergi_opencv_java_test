package com.example.illergi_opencv_java_test

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import kotlinx.android.synthetic.main.manual_2.*
import kotlinx.android.synthetic.main.manual_2.Next
import kotlinx.android.synthetic.main.manual_2.backArrow
import kotlinx.android.synthetic.main.manual_2.bottomNavigatorBar


class Manual2Activity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.manual_2)

        Next.setOnClickListener(nextOnClickListener)
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

    private val nextOnClickListener: View.OnClickListener = View.OnClickListener {
        val manual3Intent = Intent(this@Manual2Activity, Manual3Activity::class.java)
        startActivity(manual3Intent)
    }

    private val backArrowOnClickListener : View.OnClickListener = View.OnClickListener {
        finish()
    }
}