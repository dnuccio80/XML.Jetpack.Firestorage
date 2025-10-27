package com.example.xml_jetpackfirestorage.ui.compose.upload

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.xml_jetpackfirestorage.data.StorageService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UploadComposeViewModel @Inject constructor(private val storageService: StorageService) :
    ViewModel() {

    private val _imageUri = MutableStateFlow<Uri?>(null)
    val imageUri: StateFlow<Uri?> = _imageUri

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    fun uploadBasicImage(uri: Uri) {
        storageService.uploadBasicImage(uri)
    }

    fun downloadBasicImage() {
        viewModelScope.launch(Dispatchers.IO) {
            _imageUri.value = storageService.downloadBasicImage()
        }
    }

    fun uploadAndGetImage(uri: Uri, title:String) {
        viewModelScope.launch(Dispatchers.IO) {
            _isLoading.value = true
            _imageUri.value = null
            _imageUri.value = storageService.uploadAndGetImage(uri, title)
            _isLoading.value = false
        }
    }

}