package com.example.blogvyvds.domain.post

import com.example.blogvyvds.core.Result
import com.example.blogvyvds.data.remote.post.PostDataSource
import com.google.android.gms.tasks.Task

class PostRepoImpl(private val dataSource: PostDataSource): PostRepo {

    override suspend fun uploadPost(
        userName: String,
        userImg: String,
        description: String,
        imageUrl: String,
        fileUrl: String,
        date: String,
        time: String
    ): Result<Task<Void>> =
        dataSource.uploadPost(userName, userImg, description, imageUrl, fileUrl, date, time)

}