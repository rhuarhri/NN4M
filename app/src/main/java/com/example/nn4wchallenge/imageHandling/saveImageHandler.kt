package com.example.nn4wchallenge.imageHandling

import android.content.Context
import android.net.Uri
import android.os.Environment
import androidx.core.content.FileProvider
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

class saveImageHandler (private val context : Context) {

    public var savedPhotoPath : String = ""

    public fun getFileLocation() : Uri? {

        var photoUri : Uri? = null

        val photoFile: File? = try {
            createImageFile()
        } catch (ex: IOException) {
            // Error occurred while creating the File

            null
        }
        // Continue only if the File was successfully created
        photoFile?.also {
            val foundPhotoURI: Uri = FileProvider.getUriForFile(
                context,
                "com.example.android.fileprovider",
                it
            )

            photoUri = foundPhotoURI
        }

        return photoUri
    }


    @Throws(IOException::class)
    public fun createImageFile(): File {
        // Create an image file name
        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val storageDir: File = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile(
            "JPEG_${timeStamp}_", /* prefix */
            ".jpg", /* suffix */
            storageDir /* directory */
        ).apply {
            // Save a file: path for use with ACTION_VIEW intents
            savedPhotoPath = absolutePath
        }
    }
}