package com.example.xml_jetpackfirestorage.ui.compose.upload

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.contract.ActivityResultContracts.GetContent
import androidx.activity.result.contract.ActivityResultContracts.TakePicture
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Text
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
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

    fun generateUri(): Uri {
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

        val intentGalleryLauncher = rememberLauncherForActivityResult(GetContent()) { uri ->
            uri?.let {
                if (it.path?.isNotEmpty() == true) {
                    viewModel.uploadBasicImage(it)
                }
            }
        }

        var showDialog by rememberSaveable { mutableStateOf(false) }

        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            FloatingActionButton(
                onClick = {
                    showDialog = true
                },
                shape = RoundedCornerShape(12.dp),
                backgroundColor = colorResource(R.color.green)
            ) {
                Icon(
                    painterResource(R.drawable.ic_camera),
                    contentDescription = "camera button",
                    tint = Color.White
                )
            }
        }
        SelectorDialog(
            showDialog,
            onDismiss = { showDialog = false },
            onTakePhotoClick = {
                uri = generateUri()
                intentCameraLauncher.launch(uri!!)
                showDialog = false
            },
            onSelectFromGalleryClick = {
                intentGalleryLauncher.launch("image/*")
                showDialog = false
            }
        )
    }

    @Composable
    fun SelectorDialog(
        show: Boolean,
        onDismiss: () -> Unit,
        onTakePhotoClick: () -> Unit,
        onSelectFromGalleryClick: () -> Unit
    ) {
        if (!show) return
        Dialog(
            onDismissRequest = { onDismiss() }
        ) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp),
                shape = RoundedCornerShape(16.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Button (
                        onClick = { onTakePhotoClick() },
                        shape = RoundedCornerShape(4.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = colorResource(R.color.green)
                        ),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Take Photo")
                    }
                    TextButton(
                        onClick = { onSelectFromGalleryClick() },
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = colorResource(R.color.green)
                        ),
                        shape = RoundedCornerShape(4.dp)
                    ) {
                        Text("Select From Gallery")
                    }
                }
            }
        }
    }

}
