package com.example.blogvyvds.data.remote.user

import android.util.Log
import com.example.blogvyvds.data.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class RemoteUserDataSource {

    suspend fun getRemoteUser(): User {
        var user: User
        val userID = FirebaseAuth.getInstance().currentUser?.uid ?: ""

        withContext(Dispatchers.IO) {
            val userQuerySnapshot = FirebaseFirestore
                .getInstance()
                .collection("users")
                .document(userID)
                .get()
                .await()

            user = userQuerySnapshot.toObject(User::class.java)!!
        }

        return user
    }

}