package com.example.nn4wchallenge.imageHandling

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.os.Environment
import androidx.core.content.FileProvider
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

class saveImageHandler (private val context : Context) {

    public var photoLocation : String = ""
    public var savedPhotoPath : String = ""

    @Throws(IOException::class)
    public fun getFileLocation() : Uri? {

        var photoUri : Uri? = null

        val photoFile: File? = createImageFile()

        /*
        val photoFile: File? = try {
            createImageFile()
        } catch (ex: IOException) {
            // Error occurred while creating the File

            null
        }*/
        // Continue only if the File was successfully created
        photoFile?.also {
            val foundPhotoURI: Uri = FileProvider.getUriForFile(
                context,
                "com.example.nn4wchallenge.fileprovider",//"com.example.android.fileprovider",
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
        val prefix : String = "PNG_${timeStamp}_"
        val suffix : String = ".png"
        return File.createTempFile(
            prefix,
            suffix,
            storageDir /* directory */
        ).apply {
            // Save a file: path for use with ACTION_VIEW intents
            savedPhotoPath = absolutePath
        }

    }

    public fun savePhoto(photo : Bitmap)
    {
        val imageFile : File = createImageFile()
        val fileStream : FileOutputStream = FileOutputStream(imageFile)

        photo.compress(Bitmap.CompressFormat.PNG, 85, fileStream)
        fileStream.flush()
        fileStream.close()

    }


    private fun savePhotoLocation(path : String, prefix : String, suffix : String)
    {
        photoLocation = path + prefix + suffix
    }
}