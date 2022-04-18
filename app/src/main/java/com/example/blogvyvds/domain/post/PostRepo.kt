package com.example.blogvyvds.domain.post

import com.example.blogvyvds.core.Result
import com.google.android.gms.tasks.Task

interface PostRepo {
    suspend fun uploadPost(
        userName: String,
        userImg: String,
        description: String,
        imageUrl: String,
        fileUrl: String,
        date: String,
        time: String
    ): Result<Task<Void>>
}