package com.example.blogvyvds.domain.user

import android.net.Uri
import com.example.blogvyvds.data.model.User

interface UserRepository {
    suspend fun getUser(): User
    suspend fun saveUser(user: User)
    suspend fun getRemoteUser(): User
    suspend fun updateRemoteUser(user: User, profilePicUri: Uri): User
}