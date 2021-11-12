package com.example.illergi_opencv_java_test

import android.os.Bundle
import android.util.Log
import android.util.Patterns
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.illergi_opencv_java_test.utills.ErrorText
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.forget_password_fragment.view.*
import kotlinx.android.synthetic.main.reset_password.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [ForgetPasswordFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ForgetPasswordFragment : Fragment() {
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
        rootView = inflater.inflate(R.layout.forget_password_fragment, container, false)
        // Inflate the layout for this fragment
        rootView.Next.setOnClickListener(NextClickListener)
        rootView.backArrow.setOnClickListener(backArrowClickListener)

        return rootView
    }

    private val NextClickListener: View.OnClickListener = View.OnClickListener {
        if (validateEmail()) {
            val inputEmail: String = rootView.hintEmail.text.toString().trim { it <= ' ' }
            FirebaseAuth.getInstance().sendPasswordResetEmail(inputEmail)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Toast.makeText(
                            context,
                            "Email sent. Please check in email inbox.",
                            Toast.LENGTH_SHORT
                        )
                        Log.d("TAG", "Email sent.")

                    }
                }
        }
    }

    private val backArrowClickListener: View.OnClickListener = View.OnClickListener {
        activity?.onBackPressed()
    }

    private fun validateEmail(): Boolean {
        resetEmailValidateText.text = ""
        val email: String = rootView.hintEmail.text.toString().trim { it <= ' ' }
        var validateChecker: Boolean = true;
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            rootView.resetEmailValidateText.text = ErrorText.INVALID_EMAIL.text
            validateChecker = false;
        }
        if (email.isEmpty()) {
            resetEmailValidateText.text =  ErrorText.EMPTY_EMAIL.text
            validateChecker = false;
        }
        return validateChecker;
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment ResetPasswordFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            ForgetPasswordFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}