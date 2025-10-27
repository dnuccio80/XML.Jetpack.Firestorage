package com.example.xml_jetpackfirestorage.data

import android.net.Uri
import com.google.firebase.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.storage
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

class StorageService @Inject constructor(private val storage: FirebaseStorage) {
    fun getCertificatePath(): String = storage.reference.child("KMP Certificate.pdf").path
    fun uploadBasicImage(uri: Uri) {
        val reference = storage.reference.child(uri.lastPathSegment.orEmpty())
        reference.putFile(uri)
    }

    suspend fun downloadBasicImage(): Uri {
        val reference = storage.reference.child("1000000033")

        return reference.downloadUrl.await()
    }

    suspend fun uploadAndGetImage(uri: Uri, title:String):Uri {

        return suspendCancellableCoroutine { continuation ->

            val reference = if(title.isNotBlank()) storage.reference.child(title) else storage.reference.child(uri.lastPathSegment.orEmpty())
            val uploadTask = reference.putFile(uri)

            uploadTask
                .addOnSuccessListener {
                    reference.downloadUrl
                        .addOnSuccessListener { downloadUri ->
                            continuation.resume(downloadUri)
                        }
                        .addOnFailureListener { e ->
                            if (continuation.isActive) continuation.resumeWithException(e)
                        }
                }
                .addOnFailureListener { e ->
                    continuation.resumeWithException(e)
                }

            continuation.invokeOnCancellation {
                uploadTask.cancel()
            }

        }

    }

}