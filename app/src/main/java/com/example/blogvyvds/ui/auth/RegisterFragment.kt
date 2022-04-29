package com.example.blogvyvds.ui.auth

import android.net.Uri
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
import com.example.blogvyvds.databinding.FragmentRegisterBinding
import com.example.blogvyvds.domain.auth.AuthRepoImpl
import com.example.blogvyvds.domain.user.UserRepositoryImpl
import com.example.blogvyvds.presentation.auth.AuthViewModel
import com.example.blogvyvds.presentation.auth.AuthViewModelFactory
import com.example.blogvyvds.presentation.user.UserViewModel
import com.example.blogvyvds.presentation.user.UserViewModelFactory


class RegisterFragment : Fragment(R.layout.fragment_register) {

    private lateinit var binding: FragmentRegisterBinding
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
        val defaultProfilePic = Uri.parse("android.resource://com.example.blogvyvds/drawable/img_default_profile_pic")

        if(validateIntroducedData(username, email, pass, confirmPass)) createUser(username, email, pass, defaultProfilePic)
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

    private fun createUser(username: String, email: String, pass: String, profilePicUri: Uri) {
        authviewmodel.signUp(email, pass, username, profilePicUri).observe(viewLifecycleOwner) { result ->
            when(result) {
                is Result.Loading -> {
                    binding.progressBar.show()
                    binding.btnRegistrar.disable()
                }
                is Result.Success -> {
                    binding.progressBar.hide()
                    binding.btnRegistrar.enable()

                    getRemoteUserData()
                }
                is Result.Failure -> {
                    binding.progressBar.hide()
                    binding.btnRegistrar.enable()
                    Toast.makeText(
                        requireContext(),
                        "Error: ${result.exception}",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        }
    }

    private fun getRemoteUserData(): User {
        val user = User()

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
                        "Error: ${result.exception}",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        })

        return user
    }

    private fun saveLocalUserData(user: User) {
        userviewmodel.saveUser(user).observe(viewLifecycleOwner, { result ->
            when(result) {
                is Result.Loading -> {
                    Log.d("LiveData", "Guardando datos...")
                }
                is Result.Success -> {
                    Log.d("LiveData", "Datos guardados")

                    findNavController().navigate(R.id.action_registerFragment_to_main_navigation)
                }
                is Result.Failure -> {
                    Log.d("LiveData", "Error al guardar datos: ${result.exception}")
                    Toast.makeText(
                        requireContext(),
                        "Error: ${result.exception}",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        })
    }
}