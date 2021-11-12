package com.example.illergi_opencv_java_test

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import kotlinx.android.synthetic.main.edit_profile_fragment.view.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [EditProfileFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class EditProfileFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private lateinit var rootView: View

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
        rootView = inflater.inflate(R.layout.edit_profile_fragment, container, false)

        rootView.personalInfoLayout.setOnClickListener(personalInfoOnClickListener)
        rootView.changePasswordLayout.setOnClickListener(changePasswordOnClickListener)
        rootView.backArrow.setOnClickListener(arrowOnClickListener)
        return rootView
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment EditProfileFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            EditProfileFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

    private val arrowOnClickListener: View.OnClickListener = View.OnClickListener {
        activity?.onBackPressed()
    }

    private val personalInfoOnClickListener: View.OnClickListener = View.OnClickListener {
//        val personalInfoIntent =
//            Intent(this, PersonalInfoActivity::class.java)
//        startActivity(personalInfoIntent)
        val personalInfo = PersonalInfoFragment()
        val manager = fragmentManager
        val transaction = manager?.beginTransaction()

        if (transaction != null) {
            transaction. setCustomAnimations(R.anim.slide_in, R.anim.fade_out,R.anim.fade_in, R.anim.slide_out)
            transaction.replace(R.id.editProfileFragmentContainer,personalInfo)
            transaction.addToBackStack(null)
            transaction.commit()
        }
    }

    private val changePasswordOnClickListener: View.OnClickListener = View.OnClickListener {
//        val changePasswordIntent =
//            Intent(this, ChangePasswordActivity::class.java)
//        startActivity(changePasswordIntent)
        val changePassword = ChangePasswordFragment()
        val manager = fragmentManager
        val transaction = manager?.beginTransaction()

        if (transaction != null) {
            transaction. setCustomAnimations(R.anim.slide_in, R.anim.slide_out,R.anim.slide_in, R.anim.slide_out)
            transaction.replace(R.id.editProfileFragmentContainer,changePassword)
            transaction.addToBackStack(null)
            transaction.commit()
        }
    }
}