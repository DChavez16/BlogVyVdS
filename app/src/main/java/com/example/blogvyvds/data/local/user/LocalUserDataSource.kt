package com.example.blogvyvds.data.local.user

import com.example.blogvyvds.data.model.User
import com.example.blogvyvds.data.model.UserEntity
import com.example.blogvyvds.data.model.toUser

class LocalUserDataSource(private val userDao: UserDao) {

    suspend fun getUser(): User {
        return userDao.getUser().toUser()
    }

    suspend fun saveUser(user: UserEntity) {
        userDao.saveUser(user)
    }
}