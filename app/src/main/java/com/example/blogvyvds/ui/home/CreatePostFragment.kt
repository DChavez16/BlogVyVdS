package com.example.blogvyvds.ui.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.Toast
import com.example.blogvyvds.R
import com.example.blogvyvds.core.hide
import com.example.blogvyvds.core.isVisible
import com.example.blogvyvds.core.show
import com.example.blogvyvds.databinding.FragmentCreatePostBinding


class CreatePostFragment : Fragment(R.layout.fragment_create_post) {

    private lateinit var binding: FragmentCreatePostBinding

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
            Toast.makeText(
                requireContext(),
                "Post publicado",
                Toast.LENGTH_LONG
            ).show()
        }
    }
}