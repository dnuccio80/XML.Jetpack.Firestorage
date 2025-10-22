package com.example.xml_jetpackfirestorage.ui.xml.upload

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.contract.ActivityResultContracts.TakePicture
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.xml_jetpackfirestorage.R
import com.example.xml_jetpackfirestorage.databinding.ActivityUploadXmlBinding
import com.example.xml_jetpackfirestorage.databinding.SelectPathDialogBinding
import dagger.hilt.android.AndroidEntryPoint
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Objects
import javax.inject.Inject

@AndroidEntryPoint
class UploadXmlActivity @Inject constructor(): AppCompatActivity() {

    private lateinit var binding : ActivityUploadXmlBinding
    private lateinit var selectPathBinding: SelectPathDialogBinding

    private val viewModel: UploadXmlViewModel by viewModels()

    private lateinit var uri: Uri

    private var intentCameraLauncher = registerForActivityResult(TakePicture()) {
        if(it && uri.path?.isNotEmpty() == true) {
            viewModel.uploadBasicImage(uri)
        }
    }

    private var intentGalleryLauncher = registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        uri?.let {
            if(it.path?.isNotEmpty() == true){
                viewModel.uploadBasicImage(uri)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityUploadXmlBinding.inflate(layoutInflater)


        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        initUI()
    }

    private fun initUI() {
        binding.fabImage.setOnClickListener { showSelectorDialog() }

    }

    companion object {
        fun create(context: Context) = Intent(context, UploadXmlActivity::class.java)
    }

    private fun takePhoto(){
        generateUri()
        intentCameraLauncher.launch(uri)
    }

    private fun pickPhotoFromGallery() {
        intentGalleryLauncher.launch("image/*")
    }

    private fun showSelectorDialog() {
        selectPathBinding = SelectPathDialogBinding.inflate(layoutInflater)
        val dialog = AlertDialog.Builder(this).apply {
            setView(selectPathBinding.root)
        }.create()

        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)

        selectPathBinding.btnTakePhoto.setOnClickListener {
            takePhoto()
            dialog.dismiss()
        }
        selectPathBinding.btnSelectFromGallery.setOnClickListener {
            pickPhotoFromGallery()
            dialog.dismiss()
        }

        dialog.show()
    }

    private fun generateUri() {
        uri = FileProvider.getUriForFile(
            Objects.requireNonNull(this),
            "com.example.xml_jetpackfirestorage.provider",
            createFile()
        )
    }

    private fun createFile(): File {
        val name = SimpleDateFormat("yyyyMMdd_hhmmss").format(Date())
        return File.createTempFile(name, ".jpg", externalCacheDir)
    }
}