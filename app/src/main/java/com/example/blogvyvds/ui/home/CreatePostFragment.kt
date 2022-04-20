package com.example.blogvyvds.ui.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.blogvyvds.R
import com.example.blogvyvds.core.*
import com.example.blogvyvds.data.remote.post.PostDataSource
import com.example.blogvyvds.databinding.FragmentCreatePostBinding
import com.example.blogvyvds.domain.post.PostRepoImpl
import com.example.blogvyvds.presentation.post.PostViewModel
import com.example.blogvyvds.presentation.post.PostViewModelFactory
import com.google.type.Date
import com.google.type.DateTime
import java.time.LocalDateTime


class CreatePostFragment : Fragment(R.layout.fragment_create_post) {

    private lateinit var binding: FragmentCreatePostBinding
    private val postviewmodel by viewModels<PostViewModel> {
        PostViewModelFactory(PostRepoImpl(PostDataSource()))
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
            uploadPost()
        }
    }

    private fun uploadPost() {
        val username = args.username
        val userImg = args.photoUrl
        val description = binding.txtPostDescription.text.toString()
        // TODO: Permitir subir una imagen al servidor y guardar su url
        val imageUrl = ""
        // TODO: Permitir subir un archivo al servidor y guardar su url
        val fileUrl = ""
        val date = "${LocalDateTime.now().dayOfMonth} - ${LocalDateTime.now().month} - ${LocalDateTime.now().year}"
        val time = "${LocalDateTime.now().hour}:${LocalDateTime.now().minute}"

        if(description.isNotEmpty()) {
            postviewmodel.uploadPost(username, userImg, description, imageUrl, fileUrl, date, time)
                .observe(viewLifecycleOwner) { result ->
                    when(result) {
                        is Result.Loading -> binding.btnPublicar.disable()
                        is Result.Success -> {
                            binding.btnPublicar.enable()
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
        else {
            Toast.makeText(
                requireContext(),
                "Campos vacios",
                Toast.LENGTH_LONG
            ).show()
        }
    }
}