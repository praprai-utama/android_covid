package com.codemobiles.myapp

import android.Manifest
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.codemobiles.myapp.databinding.ActivityProductFormBinding
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import pl.aprilapps.easyphotopicker.ChooserType
import pl.aprilapps.easyphotopicker.EasyImage

class ProductFormActivity : AppCompatActivity() {

    private lateinit var binding: ActivityProductFormBinding
    private lateinit var easyImage: EasyImage

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityProductFormBinding.inflate(layoutInflater)
        setContentView(binding.root)

        easyImage = EasyImage.Builder(this)
            .setChooserTitle("Pick media")
            .setCopyImagesToPublicGalleryFolder(false)
            .setChooserType(ChooserType.CAMERA_AND_GALLERY)
            .setFolderName("EasyImage sample")
            .allowMultiple(true)
            .build()

        checkRuntimePermission()
        setupEventWidget()
        setupToolbar()
    }

    private fun setupEventWidget() {
        binding.camera.setOnClickListener(View.OnClickListener {
            easyImage.openCameraForImage(this)
        })

        binding.gallery.setOnClickListener(View.OnClickListener {
            easyImage.openGallery(this)
        })

        binding.productSubmit.setOnClickListener(View.OnClickListener {
//            val byteArray: ByteArray? = file?.let { file ->
//                covertByteArray(file)
//            }
//
//            it.isEnabled = false
//
//            upload(
//                binding.productEdittextName.text.toString(),
//                binding.productEdittextPrice.text.toString(),
//                binding.productEdittextStock.text.toString(),
//                byteArray,
//                file?.name
//            )
        })
    }

    private fun setupToolbar() {
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        binding.toolbar.setOnClickListener {
            finish()
        }
    }

    private fun checkRuntimePermission() {
        Dexter.withContext(this)
            .withPermissions(
                Manifest.permission.CAMERA,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            ).withListener(object : MultiplePermissionsListener {
                override fun onPermissionsChecked(report: MultiplePermissionsReport) {
                    if (report.areAllPermissionsGranted()) {
                        //todo
                    } else {
                        finish()
                    }
                }

                override fun onPermissionRationaleShouldBeShown(
                    permissions: List<PermissionRequest?>?,
                    token: PermissionToken?
                ) {
                    token?.continuePermissionRequest()
                }
            }).check()
    }
}