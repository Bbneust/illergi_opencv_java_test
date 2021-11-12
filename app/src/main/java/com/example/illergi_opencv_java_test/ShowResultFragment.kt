package com.example.illergi_opencv_java_test

import android.content.Context.*
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.text.Editable
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import com.bumptech.glide.Glide
import com.example.illergi_opencv_java_test.entities.HistoryData
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.synthetic.main.show_result_fragment.view.*
import java.io.File
import java.sql.Timestamp


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [ShowResultFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ShowResultFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private var resultText: String = ""
    private var timeStamp: String = ""
    private var cropImagePath: String = ""
    private var key: String = ""
    private lateinit var historyData: HistoryData

    lateinit var rootView: View

    val db = Firebase.firestore
    val storageRef = FirebaseStorage.getInstance().reference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        key = arguments?.getString("Key").toString()

        rootView = inflater.inflate(R.layout.show_result_fragment, container, false)

        if (key == "1") { // from crop image
            rootView.crossIcon.setBackgroundResource(R.drawable.ic_baseline_clear);
            cropImagePath = arguments?.getString("CropImagePath").toString()
            resultText = arguments?.getString("ResultText").toString()
//            val current = LocalDateTime.now()
//            val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
//            timeStamp = current.format(formatter)
            timeStamp = Timestamp(System.currentTimeMillis()).toString()

            val imageFile = File(cropImagePath)
            if (imageFile.exists()) {
                rootView.cropImageView.setImageURI(Uri.fromFile(imageFile))
            }
            if (resultText != null) {
                Log.d("TAG", resultText)
            }

            rootView.hintTimeStamp.text = timeStamp
            if (resultText != "") {
                rootView.divider.setBackgroundColor(Color.RED)
                rootView.Safe.text = "UNSAFE"
                rootView.AllergicText.text =
                    "Allergic food found : " + resultText


            } else {
                rootView.AllergicText.text = "Allergic food Not found"
            }
        } else if (key == "2") { //from history list
//            rootView.saveToHistoryIcon.visibility = View.INVISIBLE;
            rootView.crossIcon.setBackgroundResource(R.drawable.icon_trash);
            val firestoreId = arguments?.getString("FirestoreId").toString()
            db.collection("history").document(firestoreId).get()
                .addOnSuccessListener { history ->
                    historyData = history.toObject(HistoryData::class.java)!!
                    Log.d("TAG", historyData.toString())
                    if (historyData != null) {
                        Glide.with(this).load(historyData.image_url).into(rootView.cropImageView)
                        if (historyData.allergic_food_found != "") {
                            rootView.AllergicText.text =
                                "Allergic food found : " + historyData.allergic_food_found
                        } else {
                            rootView.AllergicText.text = "Allergic food Not found"
                        }
                        rootView.titleText.text =
                            Editable.Factory.getInstance().newEditable(historyData.title_name)
                        rootView.hintTimeStamp.text = historyData.timestamp
                    }

                }
        }
        rootView.titleText.isEnabled = false

        rootView.backArrow.setOnClickListener(arrowOnClickListener)
        rootView.crossIcon.setOnClickListener(CrossOnClickListener)
        rootView.iconEdit.setOnClickListener(iconEditOnClickListener)
//        rootView.saveToHistoryIcon.setOnClickListener(iconSaveOnClickListener)


        return rootView
    }

    private val iconEditOnClickListener: View.OnClickListener = View.OnClickListener {
        if (rootView.titleText.isEnabled) {
            rootView.titleText.clearFocus()
            rootView.titleText.isEnabled = false
        } else {
            rootView.titleText.isEnabled = true
            rootView.titleText.isFocusable = true
            rootView.titleText.setSelectAllOnFocus(true)
//            rootView.titleText.isFocusableInTouchMode = true
            rootView.titleText.requestFocus()
            val inputMethodManager =
                activity?.getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager?
            inputMethodManager!!.toggleSoftInputFromWindow(
                rootView.titleText.getApplicationWindowToken(),
                InputMethodManager.SHOW_FORCED, 0
            )
            activity?.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN)
//        rootView.titleText.requestFocus()
//        if( rootView.titleText.requestFocus()) {
//            activity?.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
//        }
        }
    }
    private val iconSaveOnClickListener: View.OnClickListener = View.OnClickListener {
        saveToHistory()
    }
    private val arrowOnClickListener: View.OnClickListener = View.OnClickListener {
        activity?.onBackPressed()
    }

    private val CrossOnClickListener: View.OnClickListener = View.OnClickListener {
        if (key == "1") {
            saveToHistory()
            activity?.onBackPressed()
            activity?.onBackPressed()
            // to slow
//                val cameraIntent =
//                    Intent(this, CameraActivity::class.java)
//                cameraIntent.flags =
//                    Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
//                startActivity(cameraIntent)
        }
        if (key == "2") {
            showConfirmDeleteDialog()
        }
    }

    private fun showConfirmDeleteDialog() {
        this.context?.let {
            MaterialAlertDialogBuilder(it)
                .setTitle("Do you want to Delete")
                .setMessage("Do you confirm to delete this history ?")
                .setNegativeButton("No") { dialog, which ->
                }
                .setPositiveButton("Yes") { dialog, which ->
                    val collectionName = historyData.uid + "_" + historyData.image_name
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
//                    val historyIntent = Intent(this, HistoryData::class.java)
//                    startActivity(historyIntent)
                    activity?.onBackPressed()
//                    val historyFragment: HistoryFragment
//                    fragmentManager?.let { it1 ->
//                        ManualViewPagerAdapter(
//                            it1
//                        )
//                    }?.notifyDataSetChanged();


                }.show()
        }
    }

    private fun saveToHistory() {
        Toast.makeText(context, "saveToHistory", Toast.LENGTH_SHORT).show()

        val uid: String = FirebaseAuth.getInstance().currentUser!!.uid
        val titleName: String = rootView.titleText.text.toString()
        val savedImageName = arguments?.getString("SavedImageName")
        val firestoreId: String = uid + "_" + savedImageName
        var imageDownloadPath: String = ""
        val file = Uri.fromFile(File(cropImagePath))
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

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment ShowResultFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            ShowResultFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}