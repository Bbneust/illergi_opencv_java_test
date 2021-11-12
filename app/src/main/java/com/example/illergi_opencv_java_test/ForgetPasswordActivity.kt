package com.example.illergi_opencv_java_test

import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.illergi_opencv_java_test.utills.ErrorText
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.create_account.*
import kotlinx.android.synthetic.main.login.*
import kotlinx.android.synthetic.main.reset_password.*
import kotlinx.android.synthetic.main.reset_password.hintEmail


class ForgetPasswordActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.reset_password)

        Next.setOnClickListener(NextClickListener)
        backArrow.setOnClickListener(backArrowClickListener)
    }

    private val NextClickListener: View.OnClickListener = View.OnClickListener {
        if (validateEmail()) {
            val inputEmail: String = hintEmail.text.toString().trim { it <= ' ' }
            FirebaseAuth.getInstance().sendPasswordResetEmail(inputEmail)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Toast.makeText(
                            this@ForgetPasswordActivity,
                            "Email sent. Please check in email inbox.",
                            Toast.LENGTH_SHORT
                        )
                        Log.d("TAG", "Email sent.")

                    }
                }
        }
    }

    private val backArrowClickListener: View.OnClickListener = View.OnClickListener {
        super.onBackPressed();
    }

    private fun validateEmail(): Boolean {
        resetEmailValidateText.text = ""
        val email: String = hintEmail.text.toString().trim { it <= ' ' }
        var validateChecker: Boolean = true;
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            resetEmailValidateText.text = ErrorText.INVALID_EMAIL.text
            validateChecker = false;
        }
        if (email.isEmpty()) {
            resetEmailValidateText.text =  ErrorText.EMPTY_EMAIL.text
            validateChecker = false;
        }
        return validateChecker;
    }
}