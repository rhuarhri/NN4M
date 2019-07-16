package com.example.nn4wchallenge.database.internal

import android.content.Context
import android.net.Uri
import android.os.Environment
import androidx.core.content.FileProvider
import androidx.work.Data
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.workDataOf
import com.example.nn4wchallenge.colourMatcherCode.colour
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

class AddClothingHandler (var context : Context) {

    private val commands : databaseCommands = databaseCommands()

    private var savedPhotoPath: String = ""

    //private var newClothingItem : clothing = clothing()

    private var inputData : Data.Builder = Data.Builder()


    private var typeAdded : Boolean = false
    public fun setClothingType(type : String)
    {
        inputData.putString(commands.Clothing_type, type)
        typeAdded = true
    }

    private var colourAdded : Boolean = false
    public fun setClothingColour(redAmount : Int, greenAmount : Int, blueAmount : Int)
    {

        inputData.putInt(commands.Clothing_red_color, redAmount)
        inputData.putInt(commands.Clothing_green_color, greenAmount)
        inputData.putInt(commands.Clothing_blue_color, blueAmount)

        colourAdded = true
    }

    private var seasonAdded : Boolean = false
    public fun setClothingSeason(season : String)
    {
        inputData.putString(commands.Clothing_season, season)
        seasonAdded = true
    }

    private var pictureAdded = false
    public fun setPicture(imagePath : String)
    {
        savedPhotoPath = imagePath
        pictureAdded = true
    }


    public fun saveClothingItem() : String
    {
        val error : String = checkInput()

        if (error == "")
        {
            //run save clothing item thread

            inputData.putString(commands.Clothing_DB, commands.Clothing_DB)
            inputData.putString(commands.Clothing_Add, commands.Clothing_Add)
            val DataForThread : Data = inputData.build()

            val saveClothingThread = OneTimeWorkRequestBuilder<databaseManager>()
                .setInputData(DataForThread)
                .build()
            WorkManager.getInstance().enqueue(saveClothingThread)
        }

        return error
    }

    private fun checkInput() : String
    {
        var error : String = ""

        if (!typeAdded)
        {
            error = "No type selected"
        }

        if (!seasonAdded)
        {
            error = "No season selected"
        }

        if (!colourAdded)
        {
            error = "No colour added"
        }

        if (!pictureAdded)
        {
            error = "No picture"
        }
        else
        {
            inputData.putString(commands.Clothing_picture, savedPhotoPath)
        }


        return error
    }

    /*
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
    }*/

    private fun runUpdateDatabaseThread()
    {

        inputData.putString(commands.Clothing_DB, commands.Clothing_DB)
        inputData.putString(commands.Clothing_Update, commands.Clothing_Update)
        val newClothingItem = inputData.build()
        val addToClothingDatabase = OneTimeWorkRequestBuilder<databaseManager>()
            .setInputData(newClothingItem)
            .build()

        WorkManager.getInstance().enqueue(addToClothingDatabase)
    }
}