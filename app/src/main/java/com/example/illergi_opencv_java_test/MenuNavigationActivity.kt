package com.example.illergi_opencv_java_test

import androidx.appcompat.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast

class MenuNavigationActivity : AppCompatActivity() {
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_navigation, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.history->{
                Toast.makeText(this,"history",Toast.LENGTH_LONG).show()
                return true

            }
            R.id.camera ->{
                Toast.makeText(this,"camera",Toast.LENGTH_LONG).show()
//                val cameraIntent = Intent(this@MenuNavigationActivity, CameraPageActivity::class.java)
//                startActivity(cameraIntent)
                return true
            }
            R.id.profile->{
                Toast.makeText(this,"profile",Toast.LENGTH_LONG).show()
                return true

            }
            else->{
                return super.onOptionsItemSelected(item)
            }

        }

    }


//    fun historyMenuOnclick(menu: Menu?) {
////        Intent mainIntent = new Intent(MenuNavigationActivity.this,)
//    }
//
//    fun cameraMenuOnclick(menu: Menu?) {
//        val cameraIntent = Intent(this@MenuNavigationActivity, CameraPageActivity::class.java)
//        startActivity(cameraIntent)
//    }
//
//    fun profileMenuOnclick(menu: Menu?) {
////        Intent mainIntent = new Intent(MenuNavigationActivity.this,)
//    }
}