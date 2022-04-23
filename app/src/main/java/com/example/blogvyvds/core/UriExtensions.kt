package com.example.blogvyvds.core

import android.annotation.SuppressLint
import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.provider.OpenableColumns
import java.lang.Exception

@SuppressLint("Range")
fun Uri.getFileName(context: Context): String {
    var result: String? = null
    if(this.scheme.equals("content")) {
        val cursor: Cursor? = context.contentResolver.query(this, null, null, null, null)
        try {
            if(cursor != null  &&  cursor.moveToFirst()) {
                result = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME))
            }
        } catch (e: Exception) {
            cursor?.close()
        }
    }
    if(result == null) {
        result = this.path
        val cut = result?.lastIndexOf('/')
        if(cut != -1) {
            if (cut != null) {
                result = result?.substring(cut+1)
            }
        }
    }

    return result ?: ""
}