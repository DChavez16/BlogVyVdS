package com.example.blogvyvds.ui.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.example.blogvyvds.R
import com.example.blogvyvds.databinding.FragmentHomeBinding
import com.example.blogvyvds.databinding.PostItemBinding
import kotlin.random.Random.Default.nextBoolean


class HomeFragment : Fragment(R.layout.fragment_home) {

    private lateinit var binding: FragmentHomeBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentHomeBinding.bind(view)

        loginVerification()
        setButtonListener()
    }

    private fun loginVerification() {
        val logged = nextBoolean()

        Toast.makeText(
            requireContext(),
            "Usuario ${if(logged) "conectado" else "no conectado"}",
            Toast.LENGTH_SHORT
        ).show()

        if(!logged) {
            findNavController().navigate(R.id.action_homeFragment_to_login_navigation)
        }
    }

    private fun setButtonListener() {
        binding.btnCerrarSesion.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_login_navigation)
        }

        binding.btnCrearPublicacion.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_createPostFragment)
        }
    }
}