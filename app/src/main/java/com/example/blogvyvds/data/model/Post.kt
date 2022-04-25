package com.example.blogvyvds.data.model

data class Post(
    var id: String = "",
    val userId: String = "",
    val userName: String = "",
    val userImg: String = "",
    val description: String = "",
    var imageUrl: String? = null,
    var imageName: String? = null,
    var fileUrl: String? = null,
    var fileName: String? = null,
    val date: String = "",
    val time: String = ""
)