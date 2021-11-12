package com.example.illergi_opencv_java_test

import android.content.Intent
import android.content.Intent.FLAG_ACTIVITY_CLEAR_TOP
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.Toast
import com.example.illergi_opencv_java_test.entities.UserData
import com.example.illergi_opencv_java_test.utills.ErrorText
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.edit_profile_old.*
import kotlinx.android.synthetic.main.edit_profile_old.FirstnameValidateText
import kotlinx.android.synthetic.main.edit_profile_old.LastnameValidateText
import kotlinx.android.synthetic.main.edit_profile_old.hintConfirmpass
import kotlinx.android.synthetic.main.edit_profile_old.hintEmail
import kotlinx.android.synthetic.main.edit_profile_old.hintFirstname
import kotlinx.android.synthetic.main.edit_profile_old.hintLastname
import kotlinx.android.synthetic.main.edit_profile_old.hintPassword


class EditProfileActivity_old : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.edit_profile_old)

        SaveButton.setOnClickListener(saveOnClickListener)
        backArrow.setOnClickListener(arrowOnClickListener)

        hintEmail.addTextChangedListener(textWatcher);
        hintFirstname.addTextChangedListener(textWatcher);
        hintLastname.addTextChangedListener(textWatcher);
        hintPassword.addTextChangedListener(textWatcher);
        hintConfirmpass.addTextChangedListener(textWatcher);

        val uid: String = FirebaseAuth.getInstance().currentUser!!.uid;
        val db = Firebase.firestore
        val userData = db.collection("users").document(uid)
        userData.get().addOnSuccessListener { data ->
            if (data != null) {
                val userLoginData: UserData? = data.toObject(UserData::class.java)
                Log.d("TAG", "${userLoginData}");
                Log.d("TAG", "${data.data}");
                if (userLoginData != null) {
                    hintFirstname.setText("${userLoginData.firstname}")
                    hintLastname.setText("${userLoginData.lastname}")
                }
            } else {
                Log.d("TAG", "cannot find user");
            }
        }

        bottomNavigatorBar.selectedItemId = R.id.profile
        bottomNavigatorBar.setOnNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.history -> {
                    Toast.makeText(this, "history", Toast.LENGTH_LONG).show()
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

    private val textWatcher = object : TextWatcher {
        override fun afterTextChanged(s: Editable?) {
//            if (s == hintEmail.editableText) {
//                validateEmail()
//            }
//            if (s == hintPassword.editableText) {
//                validatePassword()
//            }
            if (s == hintFirstname.editableText) {
                validateFirstname()
            }
            if (s == hintLastname.editableText) {
                validateLastname()
            }
//            if (s == hintConfirmpass.editableText) {
//                validateConfirmPassword()
//            }
        }

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
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

    private val arrowOnClickListener: View.OnClickListener = View.OnClickListener {
        val profileIntent =
            Intent(this, ProfileActivity::class.java)
        profileIntent.flags = FLAG_ACTIVITY_CLEAR_TOP
        startActivity(profileIntent)
        finish()
    }

    private fun validateData(): Boolean {
//        val emailCheck = validateEmail()
//        val passwordCheck = validatePassword()
        val firstnameCheck = validateFirstname()
        val LastnameCheck = validateLastname()
//        val confirmPasswordCheck = validateConfirmPassword()
        return firstnameCheck && LastnameCheck
//        return emailCheck && passwordCheck && firstnameCheck && LastnameCheck && confirmPasswordCheck
    }

//    private fun validateEmail(): Boolean {
//        var validateChecker: Boolean = true;
//        val email: String = hintEmail.text.toString().trim { it <= ' ' }
//        emailValidateText.text = ""
//        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
//            emailValidateText.text = ErrorText.INVALID_EMAIL.text
//            validateChecker = false;
//        }
//        if (email.isEmpty()) {
//            emailValidateText.text = ErrorText.EMPTY_EMAIL.text
//            validateChecker = false;
//        }
//        return validateChecker
//    }

//    private fun validatePassword(): Boolean {
//        passwordValidateText.text = ""
//        var validateChecker: Boolean = true;
//        val password: String = hintPassword.text.toString().trim { it <= ' ' }
//
//        if (password.isEmpty()) {
//            passwordValidateText.text = ErrorText.EMPTY_PASS.text
//            validateChecker = false;
//        }
//        return validateChecker
//    }

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

//    private fun validateConfirmPassword(): Boolean {
//        ConfirmpassValidateText.text = ""
//        var validateChecker: Boolean = true;
//        val password: String = hintPassword.text.toString().trim { it <= ' ' }
//        val passComfirm: String = hintConfirmpass.text.toString().trim { it <= ' ' }
//        if (passComfirm.isEmpty()) {
//            ConfirmpassValidateText.text = ErrorText.EMPTY_COMFIRMPASS.text
//        }
//        if (!password.equals(passComfirm)) {
//            ConfirmpassValidateText.text = ErrorText.PASSWORD_NOT_MATCH.text
//        }
//        return validateChecker
//    }


}