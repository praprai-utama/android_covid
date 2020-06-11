package com.codemobiles.myapp

import android.Manifest
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.codemobiles.myapp.databinding.ActivityProductFormBinding
import com.codemobiles.myapp.services.APIClient
import com.codemobiles.myapp.services.APIService
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.ResponseBody
import pl.aprilapps.easyphotopicker.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.BufferedInputStream
import java.io.File
import java.io.FileInputStream
import java.io.IOException

class ProductFormActivity : AppCompatActivity() {

    private var file: File? = null
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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        easyImage.handleActivityResult(
            requestCode,
            resultCode,
            data,
            this,
            object : DefaultCallback() {
                override fun onMediaFilesPicked(
                    imageFiles: Array<MediaFile>,
                    source: MediaSource
                ) {
                    onPhotosReturned(imageFiles)
                }

                override fun onImagePickerError(
                    error: Throwable,
                    source: MediaSource
                ) {
                    //Some error handling
                    error.printStackTrace()
                }

                override fun onCanceled(source: MediaSource) {
                    //Not necessary to remove any files manually anymore
                }
            })
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()
            return true
        }
        return true
    }


    private fun onPhotosReturned(returnedPhotos: Array<MediaFile>) {
        val imagesFiles: List<MediaFile> = ArrayList(listOf(*returnedPhotos))
        file = imagesFiles[0].file
        binding.productImageview.visibility = View.VISIBLE
        Glide.with(applicationContext).load(file).into(binding.productImageview)

        binding.photoLayout.gravity = Gravity.END
        binding.photoLayout.setPadding(0, 12, 12, 0)
    }

    private fun setupEventWidget() {
        binding.camera.setOnClickListener(View.OnClickListener {
            easyImage.openCameraForImage(this)
        })

        binding.gallery.setOnClickListener(View.OnClickListener {
            easyImage.openGallery(this)
        })

        binding.productSubmit.setOnClickListener(View.OnClickListener {
            val byteArray: ByteArray? = file?.let { file ->
                covertByteArray(file)
            }

            it.isEnabled = false

            upload(
                binding.productEdittextName.text.toString(),
                binding.productEdittextPrice.text.toString(),
                binding.productEdittextStock.text.toString(),
                byteArray,
                file?.name
            )
        })
    }

    private fun upload(name: String, price: String, stock: String,
                       byteArray: ByteArray?, fileName: String?) {


        // Sent Message
        val bodyText = HashMap<String, RequestBody>().apply {
                val mediaType = MediaType.parse("text/plain")
                this["name"] =
                    RequestBody.create(mediaType, if (name.isEmpty()) "-" else name)
                this["price"] =
                    RequestBody.create(mediaType, if (price.isEmpty()) "0" else price)
                this["stock"] =
                    RequestBody.create(mediaType, if (stock.isEmpty()) "0" else stock)
        }

        // Send Image
        val bodyImage: MultipartBody.Part? = byteArray?.let {
            val mediaType = MediaType.parse("image/png")
            val reqFile = RequestBody.create(mediaType, byteArray)
            MultipartBody.Part.createFormData("photo", fileName, reqFile)
        }



        val call: Call<ResponseBody> = APIClient.getClient().create(APIService::class.java).addProduct(bodyText, bodyImage)

        Log.d("cm_network", call.request().url().toString())

        // object expression
        call.enqueue(object : Callback<ResponseBody> {
            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                Toast.makeText(applicationContext, t.message, Toast.LENGTH_SHORT).show()
                binding.productSubmit.isEnabled = true
            }

            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                if(response.isSuccessful){
                    finish()
                }else{
                    Toast.makeText(applicationContext, "upload failure", Toast.LENGTH_SHORT).show()
                }
                binding.productSubmit.isEnabled = true
            }
        })
    }

    private fun setupToolbar() {
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
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

    private fun covertByteArray(file: File): ByteArray {
        val size = file.length().toInt()
        val bytes = ByteArray(size)
        try {
            val buf =
                BufferedInputStream(FileInputStream(file))
            buf.read(bytes, 0, bytes.size)
            buf.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return bytes
    }


}