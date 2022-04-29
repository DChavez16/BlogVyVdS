package com.example.blogvyvds.domain.auth

import android.net.Uri
import com.google.firebase.auth.FirebaseUser

interface AuthRepo {
    suspend fun signUp(email: String, password: String, username: String, profilePicUri: Uri): FirebaseUser?
    suspend fun signIn(email: String, password: String): FirebaseUser?
}