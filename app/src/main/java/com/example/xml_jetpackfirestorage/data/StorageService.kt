package com.example.xml_jetpackfirestorage.data

import com.google.firebase.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.storage
import javax.inject.Inject

class StorageService @Inject constructor(private val storage:FirebaseStorage) {
    fun getCertificatePath(): String = storage.reference.child("KMP Certificate.pdf").path
}