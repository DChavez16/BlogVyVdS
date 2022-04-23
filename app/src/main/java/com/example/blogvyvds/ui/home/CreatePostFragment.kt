package com.example.blogvyvds.ui.home

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContract
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
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
    private var imageUri: Uri? = null
    private var fileUri: Uri? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentCreatePostBinding.bind(view)

        setButtonListener()
    }

    private fun setButtonListener() {
        binding.btnAgregarImagen.setOnClickListener {
            getImage()
        }

        binding.btnAgregarArchivo.setOnClickListener {
            getFile()
        }

        binding.btnPublicar.setOnClickListener {
            getintroducedData()
        }
    }

    private fun getImage() {
        val gallery = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        gallery.type = "image/*"
        startActivityForResult(gallery, 0)
    }

    private fun getFile() {
        val fileManager = Intent(Intent.ACTION_GET_CONTENT)
        fileManager.type = "*/*"
        startActivityForResult(fileManager, 1)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(resultCode == RESULT_OK  &&  requestCode == 0) {
            imageUri = data?.data
            Glide.with(requireContext()).load(imageUri).centerCrop().into(binding.imgPreview)
        }

        if(resultCode == RESULT_OK  &&  requestCode == 1) {
            fileUri = data?.data
            binding.txtNombreArchivoPreview.text = fileUri?.getFileName(requireContext())
            binding.LinearLayoutFileDescription.show()
        }
    }

    private fun getintroducedData() {
        val username = args.username
        val userImg = args.photoUrl
        val description = binding.txtPostDescription.text.toString()
        val date = "${LocalDateTime.now().dayOfMonth} - ${LocalDateTime.now().month} - ${LocalDateTime.now().year}"
        val time = "${LocalDateTime.now().hour}:${LocalDateTime.now().minute}"
        val userId = FirebaseAuth.getInstance().currentUser!!.uid
        val imgBool = imageUri != null
        val fileBool = fileUri != null

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
                        imageUri?.let { uploadImage(userId, result.data, it) }
                        fileUri?.let { uploadFile(userId, result.data, it) }
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

    private fun uploadImage(userId: String, postId: String, imageUri: Uri) {
        imageviewmodel.uploadImage(userId, postId, imageUri).observe(viewLifecycleOwner) { result ->
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
    }

    private fun uploadFile(userId: String, postId: String, fileUri: Uri) {
        fileviewmodel.uploadFile(userId, postId, fileUri).observe(viewLifecycleOwner) { result ->
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
    }
}