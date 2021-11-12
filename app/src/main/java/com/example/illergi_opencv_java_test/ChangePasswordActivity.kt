package com.example.illergi_opencv_java_test

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.Toast
import com.example.illergi_opencv_java_test.utills.ErrorText
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.change_password.*
import kotlinx.android.synthetic.main.change_password.bottomNavigatorBar
import kotlinx.android.synthetic.main.change_password.hintPassword
import kotlinx.android.synthetic.main.change_password.passwordValidateText


class ChangePasswordActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.change_password)

        saveButton.setOnClickListener(saveOnClickListener)
        backArrow.setOnClickListener(arrowOnClickListener)

        hintNewPassword.addTextChangedListener(textWatcher);
        hintConfirmNewPass.addTextChangedListener(textWatcher);

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

    private val saveOnClickListener : View.OnClickListener = View.OnClickListener {
      if(validateData()){
          val newPassword: String = hintNewPassword.text.toString().trim { it <= ' ' }
          Log.d("TAG", newPassword)
          Firebase.auth.currentUser!!.updatePassword(newPassword)
              .addOnCompleteListener { task ->
                  if (task.isSuccessful) {
                      Log.d("TAG", "User password updated.")
                      FirebaseAuth.getInstance().signOut()
                      val LoginIntent = Intent(this, LoginActivity::class.java)
                      LoginIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                      startActivity(LoginIntent)
                      finish()
                  }
                  else{
                      Log.d("TAG", task.exception.toString())
                  }
              }

      }
    }

    private val textWatcher = object : TextWatcher {
        override fun afterTextChanged(s: Editable?) {

            if (s == hintNewPassword.editableText) {
                validateNewPassword()
            }

            if (s == hintConfirmNewPass.editableText) {
                validateConfirmNewPassword()
            }
        }

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
    }

    private fun validateData(): Boolean {
        val newPasswordCheck = validateNewPassword()
        val confirmNewPasswordCheck = validateConfirmNewPassword()
        return newPasswordCheck && confirmNewPasswordCheck
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

    private fun validateNewPassword(): Boolean {
        newPasswordValidateText.text = ""
        var validateChecker: Boolean = true;
        val newPassword: String = hintNewPassword.text.toString().trim { it <= ' ' }

        if (newPassword.isEmpty()) {
            newPasswordValidateText.text = ErrorText.EMPTY_PASS.text
            validateChecker = false;
        }
        return validateChecker
    }

    private fun validateConfirmNewPassword(): Boolean {
        confirmNewPasswordValidateText.text = ""
        var validateChecker: Boolean = true;
        val newPassword: String = hintNewPassword.text.toString().trim { it <= ' ' }
        val newPassComfirm: String = hintConfirmNewPass.text.toString().trim { it <= ' ' }
        if (newPassComfirm.isEmpty()) {
            confirmNewPasswordValidateText.text = ErrorText.EMPTY_COMFIRMPASS.text
        }
        if (!newPassword.equals(newPassComfirm)) {
            confirmNewPasswordValidateText.text = ErrorText.PASSWORD_NOT_MATCH.text
        }
        return validateChecker
    }
}