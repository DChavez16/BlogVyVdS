package com.example.blogvyvds.ui.home

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.blogvyvds.R
import com.example.blogvyvds.core.Result
import com.example.blogvyvds.core.getFileName
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

        binding.imgUserPicture.setOnClickListener {
            getImage()
        }
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
                    Glide.with(requireContext()).load(user.photo_url).centerCrop().into(binding.imgUserPicture)
                    showPosts()
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
                        binding.txtPostContentMessage.text = "No hay publicaciones disponibles"
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

    private fun getImage() {
        val gallery = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        gallery.type = "image/*"
        startActivityForResult(gallery, 0)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(resultCode == Activity.RESULT_OK &&  requestCode == 0) {
            data?.data?.let { updateUserData(it) }
        }
    }

    private fun updateUserData(imageUri: Uri) {
        userviewmodel.updateRemoteUser(user, imageUri).observe(viewLifecycleOwner) { result ->
            when(result) {
                is Result.Loading -> {
                    binding.progressBar.show()
                }
                is Result.Success -> {
                    binding.progressBar.hide()
                    saveLocalUserData(result.data)
                }
                is Result.Failure -> {
                    binding.progressBar.hide()
                    Toast.makeText(
                        requireContext(),
                        "Error: ${result.exception}",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        }
    }

    private fun saveLocalUserData(user: User) {
        userviewmodel.saveUser(user).observe(viewLifecycleOwner, { result ->
            when(result) {
                is Result.Loading -> {
                    Log.d("LiveData", "Guardando datos...")
                }
                is Result.Success -> {
                    Log.d("LiveData", "Datos guardados")

                    showUserData()
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