package com.example.nn4wchallenge.database.internal.clothingDatabaseCode

import android.content.Context
import androidx.room.Room
import androidx.work.Data
import com.example.nn4wchallenge.database.external.dataTranslation
import com.example.nn4wchallenge.database.internal.databaseCommands

class clothingDatabaseHandler(val context : Context) {

    private val commands : databaseCommands = databaseCommands()
    private val convertToArray : dataTranslation = dataTranslation()

    fun addToDatabase(type : String, season : String, picture : String, redAmount : Int, greenAmount : Int, blueAmount: Int)
    {
        val newClothing = clothing()
        newClothing.clothingType = type
        newClothing.clothingSeason = season
        newClothing.clothingImageLocation = picture
        newClothing.clothingColorRed = redAmount
        newClothing.clothingColorGreen = greenAmount
        newClothing.clothingColorBlue = blueAmount

        val accessDB = Room.databaseBuilder(context, clothingDatabase::class.java,
            "user-clothes-database").build()

        accessDB.clothingDao().insert(newClothing)
    }

    fun getFromDataBase() : Data
    {
        val accessDB = Room.databaseBuilder(context, clothingDatabase::class.java,
            "user-clothes-database").build()

        val foundInDataBase : Array<clothing> = accessDB.clothingDao().getAll()

        val foundIds : ArrayList<Int?> = ArrayList()
        val foundTypes : ArrayList<String?> = ArrayList()
        val foundSeasons : ArrayList<String?> = ArrayList()
        val foundImages : ArrayList<String?> = ArrayList()

        for (item in foundInDataBase)
        {

                foundIds.add(item.id)
                foundTypes.add(item.clothingType)
                foundSeasons.add(item.clothingSeason)
                foundImages.add(item.clothingImageLocation)

        }

        val ids : IntArray = convertToArray.toIntArray(foundIds)
        val types : Array<String> = convertToArray.toStringArray(foundTypes)
        val season : Array<String> = convertToArray.toStringArray(foundSeasons)
        val image : Array<String> = convertToArray.toStringArray(foundImages)


        val output : Data = Data.Builder()
            .putIntArray(commands.Clothing_ID, ids)
            .putStringArray(commands.Clothing_type, types)
            .putStringArray(commands.Clothing_season, season)
            .putStringArray(commands.Clothing_picture, image)
            .build()

        return output
    }

    fun deleteFromDatabase(clothingId : Int)
    {
        val accessDB = Room.databaseBuilder(context, clothingDatabase::class.java,
            "user-clothes-database").build()
        accessDB.clothingDao().deleteById(clothingId)
    }

    fun updateInDatabase()
    {

    }
}