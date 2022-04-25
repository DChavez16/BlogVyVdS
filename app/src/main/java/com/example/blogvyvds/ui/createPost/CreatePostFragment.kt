package com.example.blogvyvds.ui.createPost

import android.app.Activity.RESULT_OK
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.example.blogvyvds.R
import com.example.blogvyvds.core.*
import com.example.blogvyvds.data.model.Post
import com.example.blogvyvds.data.remote.post.PostDataSource
import com.example.blogvyvds.databinding.FragmentCreatePostBinding
import com.example.blogvyvds.domain.post.PostRepoImpl
import com.example.blogvyvds.presentation.post.PostViewModel
import com.example.blogvyvds.presentation.post.PostViewModelFactory
import java.time.LocalDateTime


class CreatePostFragment : Fragment(R.layout.fragment_create_post) {

    private lateinit var binding: FragmentCreatePostBinding
    private val postviewmodel by viewModels<PostViewModel> {
        PostViewModelFactory(PostRepoImpl(PostDataSource()))
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
        val userId = args.userId
        val username = args.username
        val userImg = args.photoUrl
        val description = binding.txtPostDescription.text.toString()
        val date = "${LocalDateTime.now().dayOfMonth} - ${LocalDateTime.now().month} - ${LocalDateTime.now().year}"
        val time = "${LocalDateTime.now().hour}:${LocalDateTime.now().minute}"

        val post = Post("", userId, username, userImg, description, null, null, null, null, date, time)

        if(description.isNotEmpty()) {
            uploadPost(post, requireContext())
        }
        else {
            Toast.makeText(
                requireContext(),
                "Agrega una descripcion",
                Toast.LENGTH_LONG
            ).show()
        }
    }

    private fun uploadPost(post: Post, context: Context) {
        postviewmodel.uploadPost(post, imageUri, fileUri, context)
            .observe(viewLifecycleOwner) { result ->
                when(result) {
                    is Result.Loading -> binding.btnPublicar.disable()
                    is Result.Success -> {
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
}