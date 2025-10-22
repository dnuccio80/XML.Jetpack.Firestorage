package com.example.xml_jetpackfirestorage.data

import android.net.Uri
import com.google.firebase.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.storage
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class StorageService @Inject constructor(private val storage: FirebaseStorage) {
    fun getCertificatePath(): String = storage.reference.child("KMP Certificate.pdf").path
    fun uploadBasicImage(uri: Uri) {
        val reference = storage.reference.child(uri.lastPathSegment.orEmpty())
        reference.putFile(uri)
    }
    
    suspend fun downloadBasicImage():Uri {
        val reference = storage.reference.child("1000000033")

        return reference.downloadUrl.await()
    }
    
}