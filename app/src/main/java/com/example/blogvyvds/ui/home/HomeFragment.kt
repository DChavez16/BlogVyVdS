package com.example.blogvyvds.ui.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.example.blogvyvds.R
import com.example.blogvyvds.databinding.FragmentHomeBinding
import com.example.blogvyvds.databinding.PostItemBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlin.random.Random.Default.nextBoolean


class HomeFragment : Fragment(R.layout.fragment_home) {

    private lateinit var binding: FragmentHomeBinding
    private val firebaseAuth by lazy { FirebaseAuth.getInstance() }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentHomeBinding.bind(view)

        loginVerification()
        setButtonListener()

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
}