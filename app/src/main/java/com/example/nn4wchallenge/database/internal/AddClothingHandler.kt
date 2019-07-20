package com.example.nn4wchallenge.database.internal

import android.content.Context
import androidx.work.Data
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager

class AddClothingHandler (var context : Context) {

    private val commands : DatabaseCommands = DatabaseCommands()

    private var savedPhotoPath: String = ""

    private var inputData : Data.Builder = Data.Builder()

    private var typeAdded : Boolean = false
    fun setClothingType(type : String)
    {
        inputData.putString(commands.Clothing_type, type)
        typeAdded = true
    }

    private var colourAdded : Boolean = false
    fun setClothingColour(redAmount : Int, greenAmount : Int, blueAmount : Int)
    {

        inputData.putInt(commands.Clothing_red_color, redAmount)
        inputData.putInt(commands.Clothing_green_color, greenAmount)
        inputData.putInt(commands.Clothing_blue_color, blueAmount)

        colourAdded = true
    }

    private var seasonAdded : Boolean = false
    fun setClothingSeason(season : String)
    {
        inputData.putString(commands.Clothing_season, season)
        seasonAdded = true
    }

    private var pictureAdded = false
    fun setPicture(imagePath : String)
    {
        savedPhotoPath = imagePath
        pictureAdded = true
    }

    fun saveClothingItem() : String
    {
        val error : String = checkInput()

        if (error == "")
        {
            //run save clothing item thread

            inputData.putString(commands.Clothing_DB, commands.Clothing_DB)
            inputData.putString(commands.Clothing_Add, commands.Clothing_Add)
            val dataForThread : Data = inputData.build()

            val saveClothingThread = OneTimeWorkRequestBuilder<DatabaseManager>()
                .setInputData(dataForThread)
                .build()
            WorkManager.getInstance().enqueue(saveClothingThread)
        }

        return error
    }

    private fun checkInput() : String
    {
        var error = ""

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

    private fun runUpdateDatabaseThread()
    {

        inputData.putString(commands.Clothing_DB, commands.Clothing_DB)
        inputData.putString(commands.Clothing_Update, commands.Clothing_Update)
        val newClothingItem = inputData.build()
        val addToClothingDatabase = OneTimeWorkRequestBuilder<DatabaseManager>()
            .setInputData(newClothingItem)
            .build()

        WorkManager.getInstance().enqueue(addToClothingDatabase)
    }
}