package com.example.blogvyvds.ui.home

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.blogvyvds.R
import com.example.blogvyvds.core.Result
import com.example.blogvyvds.data.local.AppDatabase
import com.example.blogvyvds.data.local.user.LocalUserDataSource
import com.example.blogvyvds.data.remote.user.RemoteUserDataSource
import com.example.blogvyvds.databinding.FragmentHomeBinding
import com.example.blogvyvds.domain.user.UserRepositoryImpl
import com.example.blogvyvds.presentation.user.UserViewModel
import com.example.blogvyvds.presentation.user.UserViewModelFactory
import com.google.firebase.auth.FirebaseAuth


class HomeFragment : Fragment(R.layout.fragment_home) {

    private lateinit var binding: FragmentHomeBinding
    private val firebaseAuth by lazy { FirebaseAuth.getInstance() }
    private val userviewmodel by viewModels<UserViewModel> {
        UserViewModelFactory(UserRepositoryImpl(
            LocalUserDataSource(AppDatabase.getDatabase(requireContext()).userDao()),
            RemoteUserDataSource()
        ))
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentHomeBinding.bind(view)

        loginVerification()
        setButtonListener()
        showUserData()

        // TODO: Mostrar las publicaciones actuales
    }

    private fun loginVerification() {
        if(firebaseAuth.currentUser == null) {
            findNavController().navigate(R.id.action_homeFragment_to_login_navigation)
        }
    }

    private fun setButtonListener() {
        binding.btnCerrarSesion.setOnClickListener {
            firebaseAuth.signOut()

            findNavController().navigate(R.id.action_homeFragment_to_login_navigation)
        }

        binding.btnCrearPublicacion.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_createPostFragment)
        }

        // TODO: Opcion para cambiar la imagen del usuario
    }

    private fun showUserData() {
        userviewmodel.getUser().observe(viewLifecycleOwner, { result ->
            when(result) {
                is Result.Loading -> {
                    Log.d("HomeFragment", "Obteniendo datos del usuario")
                    binding.txtUserName.text = ""
                }
                is Result.Success -> {
                    Log.d("HomeFragment", "Datos del usuario mostrados correctamente")
                    val user = result.data
                    binding.txtUserName.text = user.username
                }
                is Result.Failure -> {
                    Toast.makeText(
                        requireContext(),
                        "Error al obtener perfil: ${result.exception}",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        })
    }
}