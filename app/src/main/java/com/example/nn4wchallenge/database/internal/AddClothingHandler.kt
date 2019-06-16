package com.example.nn4wchallenge.database.internal

import android.content.Context
import android.net.Uri
import android.os.Environment
import androidx.core.content.FileProvider
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.workDataOf
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

class AddClothingHandler (var context : Context) {

    private var savedPhotoPath: String = ""

    private var newClothingItem : clothing = clothing()

    public fun setClothingType(type : String)
    {
        newClothingItem.clothingType = type
    }

    public fun setClothingColour(redAmount : Int, greenAmount : Int, blueAmount : Int)
    {
        newClothingItem.clothingColorRed = redAmount
        newClothingItem.clothingColorGreen = greenAmount
        newClothingItem.clothingColorBlue = blueAmount
    }

    public fun setClothingSeason(season : String)
    {
        newClothingItem.clothingSeason = season
    }

    public fun setPicture()
    {
        savedPhotoPath = "test"
    }

    public fun saveClothingItem() : String
    {
        var error : String = checkInput()

        if (error == "")
        {
            //run save clothing item thread
        }

        return error
    }

    private fun checkInput() : String
    {
        var error : String = ""

        if (newClothingItem.clothingType == "")
        {
            error = "No type selected"
        }

        if (newClothingItem.clothingSeason == "")
        {
            error = "No season selected"
        }

        if (savedPhotoPath == "")
        {
            error = "No picture"
        }
        else
        {
            newClothingItem.clothingImageLocation = savedPhotoPath
        }


        return error
    }

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
    private fun createImageFile(): File {
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

    private fun runUpdateDatabaseThread()
    {

        val newClothingItem = workDataOf("new" to newClothingItem)
        val addToClothingDatabase = OneTimeWorkRequestBuilder<AddClothingThreadManager>()
            .setInputData(newClothingItem)
            .build()

        WorkManager.getInstance().enqueue(addToClothingDatabase)
    }
}