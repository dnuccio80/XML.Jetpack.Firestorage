package com.example.xml_jetpackfirestorage.ui.compose.upload

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts.TakePicture
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.core.content.FileProvider
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.example.xml_jetpackfirestorage.R
import com.example.xml_jetpackfirestorage.databinding.ActivityUploadComposeBinding
import dagger.hilt.android.AndroidEntryPoint
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Objects

@AndroidEntryPoint
class UploadComposeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityUploadComposeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityUploadComposeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        initUI()
    }

    private fun initUI() {
        binding.composeView.setContent { UploadScreen() }
    }

    companion object {
        fun create(context: Context) = Intent(context, UploadComposeActivity::class.java)
    }

    fun generateUri():Uri {
        return FileProvider.getUriForFile(
            Objects.requireNonNull(this),
            "com.example.xml_jetpackfirestorage.provider",
            createFile()
        )
    }

    private fun createFile(): File {
        val name = SimpleDateFormat("yyyyMMdd_hhmmss").format(Date())
        return File.createTempFile(name, ".jpg", externalCacheDir)
    }

    @Composable
    fun UploadScreen(viewModel: UploadComposeViewModel = hiltViewModel()) {

        var uri: Uri? by remember { mutableStateOf(null) }

        val intentCameraLauncher = rememberLauncherForActivityResult(TakePicture()) {
            if (it && uri?.path?.isNotEmpty() == true) {
                viewModel.uploadBasicImage(uri!!)
            }
        }

        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            FloatingActionButton(
                onClick = {
                    uri = generateUri()
                    intentCameraLauncher.launch(uri!!)
                },
                backgroundColor = colorResource(R.color.green)
            ) {
                Icon(
                    painterResource(R.drawable.ic_camera),
                    contentDescription = "camera button",
                    tint = Color.White
                )
            }
        }
    }

}
