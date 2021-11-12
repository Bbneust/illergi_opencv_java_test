package com.example.illergi_opencv_java_test

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.illergi_opencv_java_test.utills.ErrorText
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.personal_info.*
import kotlinx.android.synthetic.main.personal_info_fragment.view.*


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [PersonalInfoFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class PersonalInfoFragment : Fragment() {
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
        // Inflate the layout for this fragment

        rootView = inflater.inflate(R.layout.personal_info_fragment, container, false)
        rootView.saveButton.setOnClickListener(saveOnClickListener)
        return rootView

    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment PersonalInfoFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            PersonalInfoFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

    private val saveOnClickListener: View.OnClickListener = View.OnClickListener {
        if (validateData()) {
            val firstname: String = rootView.hintFirstname.text.toString().trim { it <= ' ' }
            val lastname: String = rootView.hintLastname.text.toString().trim { it <= ' ' }
            val uid: String = FirebaseAuth.getInstance().currentUser!!.uid
            Firebase.firestore.collection("users").document(uid).update(
                mapOf(
                    "firstname" to firstname,
                    "lastname" to lastname
                )
            )
            Toast.makeText(this.context, "Saved", Toast.LENGTH_LONG).show()
//            rootView.firstnameTopic.text = firstname
//            rootView.lastnameTopic.text = lastname
        }
    }

    private val textWatcher = object : TextWatcher {
        override fun afterTextChanged(s: Editable?) {
            if (s == rootView.hintFirstname.editableText) {
                validateFirstname()
            }
            if (s == rootView.hintLastname.editableText) {
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
        val firstname: String =   rootView.hintFirstname.text.toString().trim { it <= ' ' }
        val nameRegexFormat: Regex = "^[A-Za-z]+$".toRegex();

        if (!nameRegexFormat.matches(firstname)) {
            rootView.FirstnameValidateText.text = ErrorText.INVALID_FIRSTNAME.text
            validateChecker = false;
        }
        if (firstname.isEmpty()) {
            rootView.FirstnameValidateText.text = ErrorText.EMPTY_FIRSTNAME.text
            validateChecker = false;
        }
        return validateChecker
    }

    private fun validateLastname(): Boolean {
        LastnameValidateText.text = ""
        var validateChecker: Boolean = true;
        val Lastname: String =   rootView.hintLastname.text.toString().trim { it <= ' ' }
        val nameRegexFormat: Regex = "^[A-Za-z]+$".toRegex();
        if (!nameRegexFormat.matches(Lastname)) {
            rootView.LastnameValidateText.text = ErrorText.INVALID_LASTNAME.text
            validateChecker = false;
        }
        if (Lastname.isEmpty()) {
            rootView.LastnameValidateText.text = ErrorText.EMPTY_LASTNAME.text
            validateChecker = false;
        }
        return validateChecker
    }
}