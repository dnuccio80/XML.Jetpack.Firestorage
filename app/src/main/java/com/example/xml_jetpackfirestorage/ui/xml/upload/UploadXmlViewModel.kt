package com.example.xml_jetpackfirestorage.ui.xml.upload

import android.net.Uri
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.xml_jetpackfirestorage.data.StorageService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class UploadXmlViewModel @Inject constructor(private val storageService: StorageService) :
    ViewModel() {

    fun getPath(): String = storageService.getCertificatePath()

    lateinit var imageUri: Uri

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    fun uploadBasicImage(uri: Uri) {
        storageService.uploadBasicImage(uri)
    }

    fun downloadBasicImage() {
        viewModelScope.launch(Dispatchers.IO) {
            imageUri = storageService.downloadBasicImage()
        }
    }

    fun uploadAndGetImage(uri: Uri, onSuccess: () -> Unit) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                withContext(Dispatchers.IO) { imageUri = storageService.uploadAndGetImage(uri, "") }
                onSuccess()
            } catch (e: Exception) {
                Log.i("Error", e.message.orEmpty())
            }
            _isLoading.value = false
        }
    }

}