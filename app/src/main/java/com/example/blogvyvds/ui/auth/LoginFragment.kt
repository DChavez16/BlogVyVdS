package com.example.blogvyvds.ui.auth

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.blogvyvds.R
import com.example.blogvyvds.core.*
import com.example.blogvyvds.data.local.AppDatabase
import com.example.blogvyvds.data.local.user.LocalUserDataSource
import com.example.blogvyvds.data.model.User
import com.example.blogvyvds.data.remote.user.RemoteUserDataSource
import com.example.blogvyvds.data.remote.auth.AuthDataSource
import com.example.blogvyvds.databinding.FragmentLoginBinding
import com.example.blogvyvds.domain.auth.AuthRepoImpl
import com.example.blogvyvds.domain.user.UserRepositoryImpl
import com.example.blogvyvds.presentation.auth.AuthViewModel
import com.example.blogvyvds.presentation.auth.AuthViewModelFactory
import com.example.blogvyvds.presentation.user.UserViewModel
import com.example.blogvyvds.presentation.user.UserViewModelFactory


class LoginFragment : Fragment(R.layout.fragment_login) {

    private lateinit var binding: FragmentLoginBinding
    private val authviewmodel by viewModels<AuthViewModel> {
        AuthViewModelFactory(AuthRepoImpl(AuthDataSource()))
    }
    private val userviewmodel by viewModels<UserViewModel> {
        UserViewModelFactory(UserRepositoryImpl(
            LocalUserDataSource(AppDatabase.getDatabase(requireContext()).userDao()),
            RemoteUserDataSource()
        ))
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
        authviewmodel.signIn(email, pass).observe(viewLifecycleOwner) { result ->
            when(result) {
                is Result.Loading -> {
                    binding.progressBar.show()
                    binding.btnIniciarSesion.disable()
                    binding.btnIniciarSesion.isEnabled = false
                }
                is Result.Success -> {
                    binding.progressBar.hide()
                    binding.btnIniciarSesion.enable()

                    getRemoteUserData()
                }
                is Result.Failure -> {
                    binding.progressBar.hide()
                    binding.btnIniciarSesion.enable()
                    Toast.makeText(
                        requireContext(),
                        "Error ${result.exception}",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        }
    }

    private fun getRemoteUserData() {
        userviewmodel.getRemoteUser().observe(viewLifecycleOwner, { result ->
            when(result) {
                is Result.Loading -> {
                    Log.d("RemoteData", "Obteniendo datos del usuario desde el servidor...")
                }
                is Result.Success -> {
                    Log.d("RemoteData", "Datos obtenidos correctamente")
                    saveLocalUserData(result.data)
                }
                is Result.Failure -> {
                    Log.d("RemoteData", "Error al obtener datos: ${result.exception}")
                    Toast.makeText(
                        requireContext(),
                        "Error ${result.exception}",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        })
    }

    private fun saveLocalUserData(user: User) {
        userviewmodel.saveUser(user).observe(viewLifecycleOwner, { result ->
            when(result) {
                is Result.Loading -> {
                    Log.d("LiveData", "Guardando datos...")
                }
                is Result.Success -> {
                    Log.d("LiveData", "Datos guardados")

                    findNavController().navigate(R.id.action_loginFragment_to_main_navigation)
                }
                is Result.Failure -> {
                    Log.d("LiveData", "Error al guardar datos: ${result.exception}")
                    Toast.makeText(
                        requireContext(),
                        "Error ${result.exception}",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        })
    }
}