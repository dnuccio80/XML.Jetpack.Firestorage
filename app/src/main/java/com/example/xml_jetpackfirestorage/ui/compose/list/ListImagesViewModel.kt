package com.example.xml_jetpackfirestorage.ui.compose.list

import android.net.Uri
import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.xml_jetpackfirestorage.data.StorageService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import okhttp3.Dispatcher
import javax.inject.Inject

@HiltViewModel
class ListImagesViewModel @Inject constructor(private val storageService: StorageService) : ViewModel() {

    private val _uiState = MutableStateFlow(UIState(false, emptyList()))
    val uiState:StateFlow<UIState> = _uiState

    init {
        getAllImages()
    }

    private fun getAllImages() {
        viewModelScope.launch(Dispatchers.IO) {
            _uiState.value = _uiState.value.copy(isLoading = true)
            _uiState.value = _uiState.value.copy(content = storageService.getAllImages())
            _uiState.value = _uiState.value.copy(isLoading = false)
        }
    }

}

data class UIState(val isLoading: Boolean, val content: List<Uri>)
