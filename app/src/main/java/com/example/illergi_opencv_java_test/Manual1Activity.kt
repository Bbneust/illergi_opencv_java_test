package com.example.illergi_opencv_java_test

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import kotlinx.android.synthetic.main.manual_1.*


class Manual1Activity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.manual_1)

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

    private val nextOnClickListener : View.OnClickListener = View.OnClickListener {
        val manual2Intent = Intent(this@Manual1Activity, Manual2Activity::class.java)
        startActivity(manual2Intent)
    }
    private val backArrowOnClickListener : View.OnClickListener = View.OnClickListener {
       finish()
    }
}