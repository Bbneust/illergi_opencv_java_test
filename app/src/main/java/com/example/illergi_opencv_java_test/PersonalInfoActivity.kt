package com.example.illergi_opencv_java_test

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.Toast
import com.example.illergi_opencv_java_test.utills.ErrorText
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.personal_info.*
import kotlinx.android.synthetic.main.personal_info.FirstnameValidateText
import kotlinx.android.synthetic.main.personal_info.LastnameValidateText
import kotlinx.android.synthetic.main.personal_info.backArrow
import kotlinx.android.synthetic.main.personal_info.bottomNavigatorBar
import kotlinx.android.synthetic.main.personal_info.hintFirstname
import kotlinx.android.synthetic.main.personal_info.hintLastname

class PersonalInfoActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.personal_info)

        saveButton.setOnClickListener(saveOnClickListener)
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

    private val saveOnClickListener: View.OnClickListener = View.OnClickListener {
        if (validateData()) {
            val firstname: String = hintFirstname.text.toString().trim { it <= ' ' }
            val lastname: String = hintLastname.text.toString().trim { it <= ' ' }
            val uid: String = FirebaseAuth.getInstance().currentUser!!.uid
            Firebase.firestore.collection("users").document(uid).update(
                mapOf(
                    "firstname" to firstname,
                    "lastname" to lastname
                )
            )
            Toast.makeText(this, "Saved", Toast.LENGTH_LONG).show()
        }
    }

    private val textWatcher = object : TextWatcher {
        override fun afterTextChanged(s: Editable?) {
            if (s == hintFirstname.editableText) {
                validateFirstname()
            }
            if (s == hintLastname.editableText) {
                validateLastname()
            }

        }

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
    }

    private fun validateData(): Boolean {
        val firstnameCheck = validateFirstname()
        val LastnameCheck = validateLastname()
        return firstnameCheck && LastnameCheck

    }

    private fun validateFirstname(): Boolean {
        FirstnameValidateText.text = ""
        var validateChecker: Boolean = true;
        val firstname: String = hintFirstname.text.toString().trim { it <= ' ' }
        val nameRegexFormat: Regex = "^[A-Za-z]+$".toRegex();

        if (!nameRegexFormat.matches(firstname)) {
            FirstnameValidateText.text = ErrorText.INVALID_FIRSTNAME.text
            validateChecker = false;
        }
        if (firstname.isEmpty()) {
            FirstnameValidateText.text = ErrorText.EMPTY_FIRSTNAME.text
            validateChecker = false;
        }
        return validateChecker
    }

    private fun validateLastname(): Boolean {
        LastnameValidateText.text = ""
        var validateChecker: Boolean = true;
        val Lastname: String = hintLastname.text.toString().trim { it <= ' ' }
        val nameRegexFormat: Regex = "^[A-Za-z]+$".toRegex();
        if (!nameRegexFormat.matches(Lastname)) {
            LastnameValidateText.text = ErrorText.INVALID_LASTNAME.text
            validateChecker = false;
        }
        if (Lastname.isEmpty()) {
            LastnameValidateText.text = ErrorText.EMPTY_LASTNAME.text
            validateChecker = false;
        }
        return validateChecker
    }


}