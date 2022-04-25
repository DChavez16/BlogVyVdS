package com.example.blogvyvds.domain.post

import android.content.Context
import android.net.Uri
import com.example.blogvyvds.core.Result
import com.example.blogvyvds.data.model.Post
import com.example.blogvyvds.data.remote.post.PostDataSource
import com.google.android.gms.tasks.Task

class PostRepoImpl(private val dataSource: PostDataSource): PostRepo {

    override suspend fun uploadPost(post: Post, imageUri: Uri?, fileUri: Uri?, context: Context) =
        dataSource.uploadPost(post, imageUri, fileUri, context)

    override suspend fun getPostList(): List<Post> = dataSource.getPostList()
}