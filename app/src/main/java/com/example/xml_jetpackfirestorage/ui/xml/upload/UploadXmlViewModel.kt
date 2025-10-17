package com.example.xml_jetpackfirestorage.ui.xml.upload

import androidx.lifecycle.ViewModel
import com.example.xml_jetpackfirestorage.data.StorageService
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class UploadXmlViewModel @Inject constructor(private val storageService: StorageService) :ViewModel() {

    fun getPath():String = storageService.getCertificatePath()

}