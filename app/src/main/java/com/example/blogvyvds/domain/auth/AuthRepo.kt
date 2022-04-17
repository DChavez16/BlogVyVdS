package com.example.blogvyvds.domain.auth

import com.google.firebase.auth.FirebaseUser

interface AuthRepo {
    suspend fun signUp(email: String, password: String, username: String): FirebaseUser?
    suspend fun signIn(email: String, password: String): FirebaseUser?
}