package com.example.blogvyvds.ui.auth

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.blogvyvds.R
import com.example.blogvyvds.core.Result
import com.example.blogvyvds.core.hide
import com.example.blogvyvds.core.show
import com.example.blogvyvds.data.remote.auth.AuthDataSource
import com.example.blogvyvds.databinding.FragmentLoginBinding
import com.example.blogvyvds.domain.auth.AuthRepoImpl
import com.example.blogvyvds.presentation.auth.AuthViewModel
import com.example.blogvyvds.presentation.auth.AuthViewModelFactory


class LoginFragment : Fragment(R.layout.fragment_login) {

    private lateinit var binding: FragmentLoginBinding
    private val viewmodel by viewModels<AuthViewModel> {
        AuthViewModelFactory(AuthRepoImpl(AuthDataSource()))
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentLoginBinding.bind(view)

        setButtonListener()
    }

    private fun setButtonListener() {
        binding.btnIniciarSesion.setOnClickListener {
            logUser()
        }

        binding.btnIrARegistro.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_registerFragment)
        }
    }

    private fun logUser() {
        val email = binding.txtEmail.text.toString()
        val pass = binding.txtPassword.text.toString()

        if(validateIntroducedData(email, pass)) signIn(email, pass)
    }

    private fun validateIntroducedData(email: String, pass: String): Boolean {
        var validationResult = true

        if(email.isEmpty()) {
            binding.txtEmail.error = "Campo vacio"
            validationResult = false
        }

        if(pass.isEmpty()) {
            binding.txtPassword.error = "Campo vacio"
            validationResult = false
        }

        return validationResult
    }

    private fun signIn(email: String, pass: String) {
        viewmodel.signIn(email, pass).observe(viewLifecycleOwner) { result ->
            when(result) {
                is Result.Loading -> {
                    binding.progressBar.show()
                    binding.btnIniciarSesion.isEnabled = false
                }
                is Result.Success -> {
                    binding.progressBar.hide()
                    findNavController().navigate(R.id.action_loginFragment_to_main_navigation)
                }
                is Result.Failure -> {
                    binding.progressBar.hide()
                    Toast.makeText(
                        requireContext(),
                        "Error ${result.exception}",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        }
    }
}