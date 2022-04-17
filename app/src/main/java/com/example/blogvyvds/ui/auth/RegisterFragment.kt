package com.example.blogvyvds.ui.auth

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.blogvyvds.core.Result
import com.example.blogvyvds.R
import com.example.blogvyvds.core.hide
import com.example.blogvyvds.core.show
import com.example.blogvyvds.data.remote.auth.AuthDataSource
import com.example.blogvyvds.databinding.FragmentRegisterBinding
import com.example.blogvyvds.domain.auth.AuthRepoImpl
import com.example.blogvyvds.presentation.auth.AuthViewModel
import com.example.blogvyvds.presentation.auth.AuthViewModelFactory


class RegisterFragment : Fragment(R.layout.fragment_register) {

    private lateinit var binding: FragmentRegisterBinding
    private val viewmodel by viewModels<AuthViewModel> {
        AuthViewModelFactory(AuthRepoImpl(AuthDataSource()))
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentRegisterBinding.bind(view)

        setButtonListener()
    }

    private fun setButtonListener() {
        binding.btnRegistrar.setOnClickListener {
            signUp()
        }
    }

    private fun signUp() {
        binding.txtRegistrarConfirmarPass.error = null

        val username = binding.txtRegistrarNombre.text.toString()
        val email = binding.txtRegistrarEmail.text.toString()
        val pass = binding.txtRegistrarPass.text.toString()
        val confirmPass = binding.txtRegistrarConfirmarPass.text.toString()

        if(validateIntroducedData(username, email, pass, confirmPass)) createUser(username, email, pass)
    }

    private fun validateIntroducedData(username: String, email: String, pass: String, confirmPass: String): Boolean {
        var validationResult = true

        if(username.isEmpty()) {
            binding.txtRegistrarNombre.error = "Campo vacio"
            validationResult = false
        }

        if(email.isEmpty()) {
            binding.txtRegistrarEmail.error = "Campo vacio"
            validationResult = false
        }

        if(pass.isEmpty()) {
            binding.txtRegistrarPass.error = "Campo vacio"
            validationResult = false
        }

        if(confirmPass.isEmpty() || confirmPass != pass) {
            binding.txtRegistrarConfirmarPass.error = "Las contraseñas no coinciden o el campo esta vacio"
            validationResult = false
        }

        return validationResult
    }

    private fun createUser(username: String, email: String, pass: String) {
        viewmodel.signUp(email, pass, username).observe(viewLifecycleOwner) { result ->
            when(result) {
                is Result.Loading -> {
                    binding.progressBar.show()
                    binding.btnRegistrar.isEnabled = false
                }
                is Result.Success -> {
                    binding.progressBar.hide()
                    findNavController().navigate(R.id.action_registerFragment_to_main_navigation)
                }
                is Result.Failure -> {
                    binding.progressBar.hide()
                    binding.btnRegistrar.isEnabled = true
                    Toast.makeText(
                        requireContext(),
                        "Error: ${result.exception}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }
}