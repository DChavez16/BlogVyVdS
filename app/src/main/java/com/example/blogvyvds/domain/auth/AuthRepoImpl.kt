package com.example.blogvyvds.domain.auth

import com.example.blogvyvds.data.remote.auth.AuthDataSource
import com.google.firebase.auth.FirebaseUser

class AuthRepoImpl(private val dataSource: AuthDataSource): AuthRepo {

    override suspend fun signUp(email: String, password: String, username: String): FirebaseUser? =
        dataSource.signUp(email, password, username)

    override suspend fun signIn(email: String, password: String): FirebaseUser? =
        dataSource.singIn(email, password)
}