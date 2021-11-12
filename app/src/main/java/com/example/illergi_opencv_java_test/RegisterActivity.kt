package com.example.illergi_opencv_java_test

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.util.Patterns
import android.view.View
import android.widget.Toast
import com.example.illergi_opencv_java_test.entities.UserAllergyFood
import com.example.illergi_opencv_java_test.utills.ErrorText
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.create_account.*
import maes.tech.intentanim.CustomIntent

class RegisterActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.create_account)

        signInButton.setOnClickListener(signInClickListener);
        signUpButton.setOnClickListener(signUpClickListener);

        hintEmail.addTextChangedListener(textWatcher);
        hintFirstname.addTextChangedListener(textWatcher);
        hintLastname.addTextChangedListener(textWatcher);
        hintPassword.addTextChangedListener(textWatcher);
        hintConfirmpass.addTextChangedListener(textWatcher);
    }
//    override fun onPause() {
//        super.onPause()
//        overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right)
//    }
//
    override fun onBackPressed() {
        //you can do your other onBackPressed logic here..
        val LoginIntent = Intent(this, LoginActivity::class.java)
        LoginIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(LoginIntent)
        CustomIntent.customType(this, "right-to-left");
        //Then just call finish()
//        finish()

    }

    private val textWatcher = object : TextWatcher {
        override fun afterTextChanged(s: Editable?) {
            if (s == hintEmail.editableText) {
                validateEmail()
            }
            if (s == hintPassword.editableText) {
                validatePassword()
            }
            if (s == hintFirstname.editableText) {
                validateFirstname()
            }
            if (s == hintLastname.editableText) {
                validateLastname()
            }
            if (s == hintConfirmpass.editableText) {
                validateConfirmPassword()
            }
        }

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
    }

    private val signUpClickListener: View.OnClickListener = View.OnClickListener {
        if (validateData()) {
            val email: String = hintEmail.text.toString().trim { it <= ' ' }
            val password: String = hintPassword.text.toString().trim { it <= ' ' }
            val firstname: String = hintFirstname.text.toString().trim { it <= ' ' }
            val lastname: String = hintLastname.text.toString().trim { it <= ' ' }
            register(email, password, firstname, lastname)
        }
    }

    private val signInClickListener: View.OnClickListener = View.OnClickListener {
        val loginIntent = Intent(this@RegisterActivity, LoginActivity::class.java)
        loginIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
        startActivity(loginIntent)
        CustomIntent.customType(this, "right-to-left");
        //Then just call finish()
    }

    private fun register(
        inputEmail: String,
        inputPassword: String,
        inputFirstname: String,
        inputLastname: String
    ) {
        FirebaseAuth.getInstance().createUserWithEmailAndPassword(inputEmail, inputPassword)
            .addOnCompleteListener(OnCompleteListener<AuthResult> { task ->
                if (task.isSuccessful) {
//                val user = Firebase.auth.currentUser
                    Firebase.auth.currentUser!!.sendEmailVerification()
                        .addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                Toast.makeText(
                                    this@RegisterActivity,
                                    "Email sent.",
                                    Toast.LENGTH_SHORT
                                ).show()

                            }
                        }
                    val uid: String = FirebaseAuth.getInstance().currentUser!!.uid;
//                 var firebaseUser: FirebaseUser = task.result!!.user!!
                    Firebase.firestore.collection("users").document(uid).set(
                        hashMapOf(
                            "email" to inputEmail,
                            "firstname" to inputFirstname,
                            "lastname" to inputLastname
                        )
                    )
                    createUserFoodAllergyLog()
                    routeToLoginPage()
                    Toast.makeText(
                        this@RegisterActivity,
                        "You are registered successfully.",
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    Toast.makeText(
                        this@RegisterActivity,
                        task.exception!!.message.toString(),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            })
    }

    private fun createUserFoodAllergyLog(){
        val docData = UserAllergyFood()
        val uid: String = FirebaseAuth.getInstance().currentUser!!.uid;
        Firebase.firestore.collection("user_allergy_food").document(uid)
            .set(docData)
            .addOnSuccessListener { Log.d("TAG", "DocumentSnapshot successfully create!") }
            .addOnFailureListener { e -> Log.d("TAG", "Error updating document", e) }
    }

    private fun validateData(): Boolean {
        val emailCheck = validateEmail()
        val passwordCheck = validatePassword()
        val firstnameCheck = validateFirstname()
        val LastnameCheck = validateLastname()
        val confirmPasswordCheck = validateConfirmPassword()
        return emailCheck && passwordCheck && firstnameCheck && LastnameCheck && confirmPasswordCheck
    }

    private fun validateEmail(): Boolean {
        var validateChecker: Boolean = true;
        val email: String = hintEmail.text.toString().trim { it <= ' ' }
        emailValidateText.text = ""
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailValidateText.text = ErrorText.INVALID_EMAIL.text
            validateChecker = false;
        }
        if (email.isEmpty()) {
            emailValidateText.text = ErrorText.EMPTY_EMAIL.text
            validateChecker = false;
        }
        return validateChecker
    }

    private fun validatePassword(): Boolean {
        passwordValidateText.text = ""
        var validateChecker: Boolean = true;
        val password: String = hintPassword.text.toString().trim { it <= ' ' }

        if (password.isEmpty()) {
            passwordValidateText.text = ErrorText.EMPTY_PASS.text
            validateChecker = false;
        }
        return validateChecker
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

    private fun validateConfirmPassword(): Boolean {
        ConfirmpassValidateText.text = ""
        var validateChecker: Boolean = true;
        val password: String = hintPassword.text.toString().trim { it <= ' ' }
        val passComfirm: String = hintConfirmpass.text.toString().trim { it <= ' ' }
        if (passComfirm.isEmpty()) {
            ConfirmpassValidateText.text = ErrorText.EMPTY_COMFIRMPASS.text
            validateChecker = false;
        }
        if (!password.equals(passComfirm)) {
            ConfirmpassValidateText.text = ErrorText.PASSWORD_NOT_MATCH.text
            validateChecker = false;
        }
        return validateChecker
    }

    private fun routeToLoginPage() {
        val LoginIntent = Intent(this@RegisterActivity, LoginActivity::class.java)
        LoginIntent.flags =
            Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(LoginIntent)
        finish()
    }


}
