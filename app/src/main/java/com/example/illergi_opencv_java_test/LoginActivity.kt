package com.example.illergi_opencv_java_test

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.util.Patterns
import android.view.View
import android.widget.Toast
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.example.illergi_opencv_java_test.utills.ErrorText
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.login.*
import kotlinx.android.synthetic.main.reset_password.*
import maes.tech.intentanim.CustomIntent.customType

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login)
        checkUserLogin()

        signUpButton.setOnClickListener(signUpClickListener)
        signInButton.setOnClickListener(signInClickListener)
        forgotPassButton.setOnClickListener(forgetPasswordClickListener)

    }


    private val signInClickListener: View.OnClickListener = View.OnClickListener {
        if (validateData()) {
            val email: String = TextEmailAddress.text.toString().trim { it <= ' ' }
            val password: String = TextPassword.text.toString().trim { it <= ' ' }

            FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        if (Firebase.auth.currentUser!!.isEmailVerified) {
//                            var firebaseUser: FirebaseUser = task.result!!.user!!
                            val MainActivityIntent =
                                Intent(this@LoginActivity, MainActivity::class.java)
                            MainActivityIntent.flags =
                                Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                            startActivity(MainActivityIntent)
                            customType(this, "fadein-to-fadeout");
                            finish()
                            Toast.makeText(
                                this@LoginActivity,
                                "You are login in successfully.",
                                Toast.LENGTH_SHORT
                            ).show()

                        } else {
                            ValidateText.text = "please verify email"
                            Toast.makeText(
                                this@LoginActivity,
                                "pls verify email",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    } else {
                        ValidateText.text = task.exception!!.message.toString()
                        Toast.makeText(
                            this@LoginActivity,
                            task.exception!!.message.toString(),
                            Toast.LENGTH_SHORT
                        ).show()
                    }

                }
        }

    }

    private val signUpClickListener: View.OnClickListener = View.OnClickListener {
        val signUpIntent = Intent(this@LoginActivity, RegisterActivity::class.java)
        signUpIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
        startActivity(signUpIntent)
        customType(this, "left-to-right");
    }

    private val forgetPasswordClickListener: View.OnClickListener = View.OnClickListener {
//        val forgetPassIntent = Intent(this@LoginActivity, ForgetPasswordActivity::class.java)
//        forgetPassIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
//        startActivity(forgetPassIntent)

//        val forgetPassIntent = Intent(this, ForgetPasswordActivity::class.java)
//        val options = ActivityOptions.makeCustomAnimation(this, R.anim.slide_in,  R.anim.slide_out)
//        this.startActivity(forgetPassIntent, options.toBundle())

        val manager: FragmentManager = supportFragmentManager
        val transaction: FragmentTransaction = manager.beginTransaction()
        transaction.setCustomAnimations(
            R.anim.slide_in,
            R.anim.slide_out,
            R.anim.slide_in,
            R.anim.slide_out
        )
        transaction.replace(R.id.forgetPasswordFragmentContainer, ForgetPasswordFragment());
        transaction.addToBackStack(null)
        transaction.commit()
    }

    private fun checkUserLogin() {
        val user = FirebaseAuth.getInstance().currentUser;
        if (user != null) {
            if (Firebase.auth.currentUser!!.isEmailVerified) {
                // User is signed in
                val MainActivityIntent =
                    Intent(this@LoginActivity, MainActivity::class.java)
                MainActivityIntent.flags =
                    Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(MainActivityIntent)
                customType(this, "fadein-to-fadeout");
                finish()
            }
        }
    }

    private fun validateData(): Boolean {
        ValidateText.text = ""
        val email: String = TextEmailAddress.text.toString().trim { it <= ' ' }
        val password: String = TextPassword.text.toString().trim { it <= ' ' }

        var validateChecker: Boolean = true;

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            ValidateText.text = ErrorText.INVALID_EMAIL.text
            validateChecker = false;
        }
        if (email.isEmpty()) {
            ValidateText.text = ErrorText.EMPTY_EMAIL.text
            validateChecker = false;
        }

        if (password.isEmpty()) {
            ValidateText.text = ErrorText.EMPTY_PASS.text
            validateChecker = false;
        }

        if (email.isEmpty() && password.isEmpty()) {
            ValidateText.text = ErrorText.EMPTY_EMAIL_PASSWORD.text
        }

        return validateChecker;
    }

}



