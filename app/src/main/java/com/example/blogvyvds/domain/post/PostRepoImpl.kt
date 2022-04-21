package com.example.blogvyvds.domain.post

import com.example.blogvyvds.core.Result
import com.example.blogvyvds.data.remote.post.PostDataSource
import com.google.android.gms.tasks.Task

class PostRepoImpl(private val dataSource: PostDataSource): PostRepo {

    override suspend fun uploadPost(
        userName: String,
        userImg: String,
        description: String,
        userId: String,
        date: String,
        time: String,
        imgBool: Boolean,
        fileBool: Boolean
    ): Result<String> =
        dataSource.uploadPost(userName, userImg, description, userId, date, time, imgBool, fileBool)

}