package com.example.illergi_opencv_java_test

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.illergi_opencv_java_test.utills.ErrorText
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.change_password_fragment.view.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [ChangePasswordFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ChangePasswordFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private lateinit var rootView : View

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
       rootView = inflater.inflate(R.layout.change_password_fragment, container, false)
        // Inflate the layout for this fragment
        rootView.saveButton.setOnClickListener(saveOnClickListener)

        rootView.hintNewPassword.addTextChangedListener(textWatcher);
        rootView.hintConfirmNewPass.addTextChangedListener(textWatcher);
        return rootView
    }


    private val saveOnClickListener : View.OnClickListener = View.OnClickListener {
        if(validateData()){
            val newPassword: String = rootView.hintNewPassword.text.toString().trim { it <= ' ' }
            Log.d("TAG", newPassword)
            Firebase.auth.currentUser!!.updatePassword(newPassword)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Log.d("TAG", "User password updated.")
                        FirebaseAuth.getInstance().signOut()
//                        val LoginIntent = Intent(this, LoginActivity::class.java)
//                        LoginIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
//                        startActivity(LoginIntent)
//                        finish()
                    }
                    else{
                        Log.d("TAG", task.exception.toString())
                    }
                }

        }
    }

    private val textWatcher = object : TextWatcher {
        override fun afterTextChanged(s: Editable?) {

            if (s == rootView.hintNewPassword.editableText) {
                validateNewPassword()
            }

            if (s == rootView.hintConfirmNewPass.editableText) {
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

//    private fun validatePassword(): Boolean {
//        rootView.passwordValidateText.text = ""
//        var validateChecker: Boolean = true;
//        val password: String = rootView.hintPassword.text.toString().trim { it <= ' ' }
//
//        if (password.isEmpty()) {
//            rootView.passwordValidateText.text = ErrorText.EMPTY_PASS.text
//            validateChecker = false;
//        }
//        return validateChecker
//    }

    private fun validateNewPassword(): Boolean {
        rootView.newPasswordValidateText.text = ""
        var validateChecker: Boolean = true;
        val newPassword: String = rootView.hintNewPassword.text.toString().trim { it <= ' ' }

        if (newPassword.isEmpty()) {
            rootView.newPasswordValidateText.text = ErrorText.EMPTY_PASS.text
            validateChecker = false;
        }
        return validateChecker
    }

    private fun validateConfirmNewPassword(): Boolean {
        rootView.confirmNewPasswordValidateText.text = ""
        var validateChecker: Boolean = true;
        val newPassword: String = rootView.hintNewPassword.text.toString().trim { it <= ' ' }
        val newPassComfirm: String = rootView.hintConfirmNewPass.text.toString().trim { it <= ' ' }
        if (newPassComfirm.isEmpty()) {
            rootView.confirmNewPasswordValidateText.text = ErrorText.EMPTY_COMFIRMPASS.text
        }
        if (!newPassword.equals(newPassComfirm)) {
            rootView.confirmNewPasswordValidateText.text = ErrorText.PASSWORD_NOT_MATCH.text
        }
        return validateChecker
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment ChangePasswordFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            ChangePasswordFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}