package com.syracuse.caloriemanager

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.ColorInt
import androidx.core.widget.doAfterTextChanged
import androidx.core.widget.doOnTextChanged
import androidx.navigation.fragment.findNavController
import com.google.android.material.button.MaterialButton
import com.google.android.material.progressindicator.CircularProgressIndicatorSpec
import com.google.android.material.progressindicator.IndeterminateDrawable
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.syracuse.caloriemanager.databinding.FragmentLoginBinding

class LoginFragment : Fragment() {

    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!
    private lateinit var auth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        auth = Firebase.auth
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val loginButton: MaterialButton = binding.btnLogin as MaterialButton
        loginButton.setOnClickListener{

            val email = binding.editEmail.text.toString()
            val password = binding.editPassword.text.toString()

            try {
                auth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(activity!!) { task ->
                        if (task.isSuccessful) {
                            Toast.makeText(activity!!, "Successfully Logged In", Toast.LENGTH_LONG).show()
                            val trackingActivity = Intent(activity, TrackingActivity::class.java)
                            startActivity(trackingActivity)
                            activity?.finish()
                        } else {
                            Toast.makeText(activity!!,
                                task.exception.toString(), Snackbar.LENGTH_LONG).show()
                        }
                    }
            } catch (e: IllegalArgumentException){
                Toast.makeText(activity!!,
                    "Email or Password cannot be empty", Snackbar.LENGTH_LONG).show()
            } catch (e: Exception){
                Toast.makeText(activity!!, e.message.toString(), Snackbar.LENGTH_LONG).show()
            }
        }

        binding.btnToRegister.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_registerFragment)
        }

        attachTextInputListeners()
    }

    private fun attachTextInputListeners() {
        binding.editEmail.doOnTextChanged { text, _, _, _ ->
            if(text?.isEmpty() == true){
                binding.textLayoutEmail.error = "Enter an email"
            } else if (!Patterns.EMAIL_ADDRESS.matcher(text).matches()) {
                binding.textLayoutEmail.error = "Enter a valid email"
            }
        }

        binding.editPassword.doOnTextChanged { text, _, _, _ ->
            if(text?.isEmpty() == true){
                binding.textLayoutPassword.error = "Enter an password"
            } else if (text?.length!! < 8) {
                binding.textLayoutPassword.error = "Password should be at least 8 characters"
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        private const val TAG = "LoginFragment"
    }
}