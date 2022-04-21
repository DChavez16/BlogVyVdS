package com.example.blogvyvds.domain.post

import com.example.blogvyvds.core.Result
import com.google.android.gms.tasks.Task

interface PostRepo {
    suspend fun uploadPost(
        userName: String,
        userImg: String,
        description: String,
        userId: String,
        date: String,
        time: String,
        imgBool: Boolean,
        fileBool: Boolean
    ): Result<String>
}