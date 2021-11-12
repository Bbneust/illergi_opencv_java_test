package com.example.illergi_opencv_java_test

import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.result_safe.*
import kotlinx.android.synthetic.main.result_safe.backArrow
import java.io.File

import com.google.firebase.storage.FirebaseStorage

import android.content.Intent
import android.graphics.Color
import android.text.Editable
import com.bumptech.glide.Glide
import com.example.illergi_opencv_java_test.entities.HistoryData
import com.google.android.material.dialog.MaterialAlertDialogBuilder


import java.sql.Timestamp


class ShowResultActivity : AppCompatActivity() {
    var resultText: String = ""
    var timeStamp: String = ""
    var cropImagePath: String = ""
    var key: String = ""
    private lateinit var historyData: HistoryData

    val db = Firebase.firestore
    val storageRef = FirebaseStorage.getInstance().reference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.result_safe)

        key = intent.extras!!.getString("Key").toString()

        if (key == "1") { // from crop image

            cropImagePath = intent.extras!!.getString("CropImagePath").toString()
            resultText = intent.extras!!.getString("ResultText").toString()

//            val current = LocalDateTime.now()
//            val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
//            timeStamp = current.format(formatter)
            timeStamp = Timestamp(System.currentTimeMillis()).toString()

            val imageFile = File(cropImagePath)
            if (imageFile.exists()) {
                cropImageView.setImageURI(Uri.fromFile(imageFile))
            }
            if (resultText != null) {
                Log.d("TAG", resultText)
            }

            AllergicText.text = "Allergic food found : ${resultText}"
            hintTimeStamp.text = timeStamp
            if(resultText != ""){
                divider.setBackgroundColor(Color.RED)
                Safe.text ="UNSAFE"

            }
        } else if (key == "2") { //from history list
//            crossIcon.setVisibility(View.INVISIBLE);
            val firestoreId = intent.extras!!.getString("FirestoreId").toString()
            db.collection("history").document(firestoreId).get()
                .addOnSuccessListener { history ->
                    historyData = history.toObject(HistoryData::class.java)!!
                    Log.d("TAG", historyData.toString())
                    if (historyData != null) {
                        Glide.with(this).load(historyData.image_url).into(cropImageView)
                        AllergicText.text =
                            "Allergic food found : " + historyData.allergic_food_found
                        titleText.text =
                            Editable.Factory.getInstance().newEditable(historyData.title_name)
                        hintTimeStamp.text = historyData.timestamp

                    }

                }


        }

        backArrow.setOnClickListener {
            finish()

        }

        crossIcon.setOnClickListener {
            if (key == "1") {
                saveToHistory() // to slow
                val cameraIntent =
                    Intent(this, CameraActivity::class.java)
                cameraIntent.flags =
                    Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(cameraIntent)
            }
            if (key == "2") {
                showConfirmDeleteDialog()
            }
        }
    }

    private fun showConfirmDeleteDialog() {
        MaterialAlertDialogBuilder(this)
            .setTitle("Do you want to Delete")
            .setMessage("จะลบม้ายยยย")
            .setNegativeButton("No") { dialog, which ->

            }
            .setPositiveButton("Yes") { dialog, which ->
                val collectionName = historyData.uid+"_"+historyData.image_name
                db.collection("history").document(collectionName)
                    .delete()
                    .addOnSuccessListener {
                        Log.d(
                            "TAG",
                            "DocumentSnapshot successfully deleted!"
                        )
                    }
                    .addOnFailureListener { e ->
                        Log.w(
                            "TAG",
                            "Error deleting document",
                            e
                        )
                    }
                val desertRef = storageRef.child("${historyData.uid}/${historyData.image_name}")

                // Delete the file
                desertRef.delete().addOnSuccessListener {
                    Log.d(
                        "TAG",
                        "file successfully deleted!"
                    )
                }.addOnFailureListener {
                    // Uh-oh, an error occurred!
                }
                val historyIntent = Intent(this,HistoryData::class.java)
                startActivity(historyIntent)
                finish()




            }.show()


    }

    private fun saveToHistory() {

        Toast.makeText(this, "saveToHistory", Toast.LENGTH_SHORT).show()

        val uid: String = FirebaseAuth.getInstance().currentUser!!.uid;
        val titleName: String = titleText.text.toString()

        val savedImageName = intent.extras!!.getString("SavedImageName")
        var firestoreId: String = uid + "_" + savedImageName
        var imageDownloadPath: String = ""
        var file = Uri.fromFile(File(cropImagePath))
//        val uploadTask =  storageRef.getReference("$uid/$savedImageName")
//        val uploadTask = storageRef.putFile(file)

        storageRef.child("$uid/$savedImageName").putFile(file)
            .addOnSuccessListener {

                val result = it.metadata!!.reference!!.downloadUrl;
                Log.d("TAG", result.toString())
                result.addOnSuccessListener {
                    imageDownloadPath = it.toString()
                    Log.d("TAG", imageDownloadPath)

                    Firebase.firestore.collection("history").document(firestoreId).set(
                        hashMapOf(
                            "uid" to uid,
                            "title_name" to titleName,
                            "allergic_food_found" to resultText,
                            "image_name" to savedImageName,
                            "image_url" to imageDownloadPath,
                            "timestamp" to timeStamp
                        )
                    )


                }
            }


    }

}