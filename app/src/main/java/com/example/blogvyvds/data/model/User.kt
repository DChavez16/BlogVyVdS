package com.example.blogvyvds.data.model

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.versionedparcelable.VersionedParcelize

@VersionedParcelize
data class User (
    val email: String = "",
    val username: String = "",
    val photo_url: String = ""
)


// Room
@Entity data class UserEntity(
    @PrimaryKey val id: Int = -1,

    @ColumnInfo(name = "email") val email: String = "",
    @ColumnInfo(name = "username") val username: String = "",
    @ColumnInfo(name = "photo_url") val photo_url: String = ""
)

fun List<UserEntity>.toUser(): User {
    return this.first().toUser()
}

fun UserEntity.toUser(): User = User(
    this.email,
    this.username,
    this.photo_url
)

fun User.toUserEntity(): UserEntity = UserEntity(
    -1,
    this.email,
    this.username,
    this.photo_url
)