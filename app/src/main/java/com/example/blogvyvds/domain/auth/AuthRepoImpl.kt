package com.example.blogvyvds.domain.auth

import android.net.Uri
import com.example.blogvyvds.data.remote.auth.AuthDataSource
import com.google.firebase.auth.FirebaseUser

class AuthRepoImpl(private val dataSource: AuthDataSource): AuthRepo {

    override suspend fun signUp(email: String, password: String, username: String, profilePicUri: Uri): FirebaseUser? =
        dataSource.signUp(email, password, username, profilePicUri)

    override suspend fun signIn(email: String, password: String): FirebaseUser? =
        dataSource.singIn(email, password)
}