package com.example.xml_jetpackfirestorage.ui.xml.upload

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.xml_jetpackfirestorage.R
import com.example.xml_jetpackfirestorage.data.StorageService
import com.example.xml_jetpackfirestorage.databinding.ActivityUploadXmlBinding
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class UploadXmlActivity @Inject constructor(): AppCompatActivity() {

    private lateinit var binding : ActivityUploadXmlBinding

    private val viewModel: UploadXmlViewModel by viewModels()

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
        binding.tvTitle.text = viewModel.getPath()
    }

    companion object {
        fun create(context: Context) = Intent(context, UploadXmlActivity::class.java)
    }
}