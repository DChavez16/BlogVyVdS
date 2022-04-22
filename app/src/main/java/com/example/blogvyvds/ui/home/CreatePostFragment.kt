package com.example.blogvyvds.ui.home

import android.graphics.Bitmap
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.blogvyvds.R
import com.example.blogvyvds.core.*
import com.example.blogvyvds.data.remote.file.FileDataSource
import com.example.blogvyvds.data.remote.image.ImageDataSource
import com.example.blogvyvds.data.remote.post.PostDataSource
import com.example.blogvyvds.databinding.FragmentCreatePostBinding
import com.example.blogvyvds.domain.file.FileRepoImpl
import com.example.blogvyvds.domain.image.ImageRepoImpl
import com.example.blogvyvds.domain.post.PostRepoImpl
import com.example.blogvyvds.presentation.file.FileViewModel
import com.example.blogvyvds.presentation.file.FileViewModelFactory
import com.example.blogvyvds.presentation.image.ImageViewModel
import com.example.blogvyvds.presentation.image.ImageViewModelFactory
import com.example.blogvyvds.presentation.post.PostViewModel
import com.example.blogvyvds.presentation.post.PostViewModelFactory
import com.google.firebase.auth.FirebaseAuth
import com.google.type.Date
import com.google.type.DateTime
import java.io.File
import java.time.LocalDateTime


class CreatePostFragment : Fragment(R.layout.fragment_create_post) {

    private lateinit var binding: FragmentCreatePostBinding
    private val postviewmodel by viewModels<PostViewModel> {
        PostViewModelFactory(PostRepoImpl(PostDataSource()))
    }
    private val imageviewmodel by viewModels<ImageViewModel> {
        ImageViewModelFactory(ImageRepoImpl(ImageDataSource()))
    }
    private val fileviewmodel by viewModels<FileViewModel> {
        FileViewModelFactory(FileRepoImpl(FileDataSource()))
    }
    private val args by navArgs<CreatePostFragmentArgs>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentCreatePostBinding.bind(view)

        setButtonListener()
    }

    private fun setButtonListener() {
        binding.btnAgregarArchivo.setOnClickListener {
            binding.LinearLayoutFileDescription.let {
                if(!it.isVisible()) {
                    it.show()
                    binding.txtNombreArchivoPreview.text = "Nombre del archivo"
                }
                else {
                    it.hide()
                    binding.txtNombreArchivoPreview.text = ""
                }
            }
        }

        binding.btnAgregarImagen.setOnClickListener {
            if(binding.imgPreview.drawable == null) {
                binding.imgPreview.setImageResource(R.drawable.img_agregar_imagen)
            }
            else binding.imgPreview.setImageDrawable(null)
        }

        binding.btnPublicar.setOnClickListener {
            getintroducedData()
        }
    }

    private fun getintroducedData() {
        val username = args.username
        val userImg = args.photoUrl
        val description = binding.txtPostDescription.text.toString()
        val date = "${LocalDateTime.now().dayOfMonth} - ${LocalDateTime.now().month} - ${LocalDateTime.now().year}"
        val time = "${LocalDateTime.now().hour}:${LocalDateTime.now().minute}"
        val userId = FirebaseAuth.getInstance().currentUser!!.uid
        val imgBool = !binding.btnAgregarImagen.isEnabled
        val fileBool = !binding.btnAgregarArchivo.isEnabled

        if(description.isNotEmpty()) {
            uploadPost(username, userImg, description, userId, date, time, imgBool, fileBool)
        }
        else {
            Toast.makeText(
                requireContext(),
                "Campos vacios",
                Toast.LENGTH_LONG
            ).show()
        }
    }

    private fun uploadPost(username: String, userImg: String, description: String, userId: String, date: String, time: String, imgBool: Boolean, fileBool: Boolean) {
        postviewmodel.uploadPost(username, userImg, description, userId, date, time, imgBool, fileBool)
            .observe(viewLifecycleOwner) { result ->
                when(result) {
                    is Result.Loading -> binding.btnPublicar.disable()
                    is Result.Success -> {
                        if(imgBool) uploadImage(userId, result.data)
                        if(fileBool) uploadFile(userId, result.data)
                        findNavController().popBackStack()
                    }
                    is Result.Failure -> {
                        binding.btnPublicar.enable()
                        Toast.makeText(
                            requireContext(),
                            "Error de conexion: ${result.exception}",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }
            }
    }

    private fun uploadImage(userId: String, postId: String) {
        /*
        imageviewmodel.uploadImage(userId, postId, Bitmap).observe(viewLifecycleOwner) { result ->
            when(result) {
                is Result.Loading -> {
                    Log.d("CreatePostFragment", "Cargando imagen en el servidor...")
                }
                is Result.Success -> {
                    Log.d("CreatePostFragment", "Imagen cargada correctamente")
                }
                is Result.Failure -> {
                    Log.d("CreatePostFragment", "Error al cargar imagen: ${result.exception}")
                }
            }
        }
        */
    }

    private fun uploadFile(userId: String, postId: String) {
        /*
        fileviewmodel.uploadFile(userId, postId, File).observe(viewLifecycleOwner) { result ->
            when(result) {
                is Result.Loading -> {
                    Log.d("CreatePostFragment", "Cargando archivo en el servidor...")
                }
                is Result.Success -> {
                    Log.d("CreatePostFragment", "Archivo cargado en el servidor")
                }
                is Result.Failure -> {
                    Log.d("CreatePostFragment", "Error al cargar archivo: ${result.exception}")
                }
            }
        }
        */
    }
}