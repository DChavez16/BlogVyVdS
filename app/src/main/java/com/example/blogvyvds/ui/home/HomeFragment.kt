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
import com.example.blogvyvds.core.hide
import com.example.blogvyvds.core.show
import com.example.blogvyvds.data.local.AppDatabase
import com.example.blogvyvds.data.local.user.LocalUserDataSource
import com.example.blogvyvds.data.model.User
import com.example.blogvyvds.data.remote.post.PostDataSource
import com.example.blogvyvds.data.remote.user.RemoteUserDataSource
import com.example.blogvyvds.databinding.FragmentHomeBinding
import com.example.blogvyvds.domain.post.PostRepoImpl
import com.example.blogvyvds.domain.user.UserRepositoryImpl
import com.example.blogvyvds.presentation.post.PostViewModel
import com.example.blogvyvds.presentation.post.PostViewModelFactory
import com.example.blogvyvds.presentation.user.UserViewModel
import com.example.blogvyvds.presentation.user.UserViewModelFactory
import com.example.blogvyvds.ui.home.adapter.HomeFragmentAdapter
import com.google.firebase.auth.FirebaseAuth


class HomeFragment : Fragment(R.layout.fragment_home) {

    private lateinit var binding: FragmentHomeBinding
    private lateinit var user: User
    private val firebaseAuth by lazy { FirebaseAuth.getInstance() }
    private val userviewmodel by viewModels<UserViewModel> {
        UserViewModelFactory(UserRepositoryImpl(
            LocalUserDataSource(AppDatabase.getDatabase(requireContext()).userDao()),
            RemoteUserDataSource()
        ))
    }
    private val postviewmodel by viewModels<PostViewModel> {
        PostViewModelFactory(PostRepoImpl(PostDataSource()))
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentHomeBinding.bind(view)

        loginVerification()
        setButtonListener()
        showUserData()
        showPosts()
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
            val action = HomeFragmentDirections.actionHomeFragmentToCreatePostFragment(
                user.username,
                user.photo_url,
                FirebaseAuth.getInstance().currentUser?.uid ?: ""
            )

            findNavController().navigate(action)
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
                    user = result.data
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

    private fun showPosts() {
        postviewmodel.getPostList().observe(viewLifecycleOwner) { result ->
            when(result) {
                is Result.Loading -> {
                    binding.progressBar.show()
                }
                is Result.Success -> {
                    binding.progressBar.hide()
                    if(result.data.isNotEmpty()) {
                        binding.RVPosts.adapter = HomeFragmentAdapter(result.data)
                        binding.RVPosts.show()
                    } else {
                        binding.txtPostContentMessage.text = "No hay post disponibles"
                        binding.txtPostContentMessage.show()
                    }
                }
                is Result.Failure -> {
                    binding.progressBar.hide()
                    binding.txtPostContentMessage.text = result.exception.toString()
                    binding.txtPostContentMessage.show()
                }
            }
        }
    }
}