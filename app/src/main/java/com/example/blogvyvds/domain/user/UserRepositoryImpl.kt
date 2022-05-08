package com.example.blogvyvds.domain.user

import android.net.Uri
import android.util.Log
import com.example.blogvyvds.data.local.user.LocalUserDataSource
import com.example.blogvyvds.data.model.User
import com.example.blogvyvds.data.model.toUserEntity
import com.example.blogvyvds.data.remote.user.RemoteUserDataSource

class UserRepositoryImpl(
    private val dataSourceLocal: LocalUserDataSource,
    private val dataSourceRemote: RemoteUserDataSource
    ): UserRepository {

    override suspend fun getUser(): User {
        return dataSourceLocal.getUser()
    }

    override suspend fun saveUser(user: User) {
        dataSourceLocal.saveUser(user.toUserEntity())
    }

    override suspend fun getRemoteUser(): User {
        return dataSourceRemote.getRemoteUser()
    }

    override suspend fun updateRemoteUser(user: User, profilePicUri: Uri): User {
        return dataSourceRemote.updateRemoteUser(user, profilePicUri)
    }
}