package com.example.nn4wchallenge.database.internal.clothingDatabaseCode

import android.content.Context
import androidx.room.Room
import androidx.work.Data
import com.example.nn4wchallenge.database.external.DataTranslation
import com.example.nn4wchallenge.database.internal.DatabaseCommands
import com.example.nn4wchallenge.imageHandling.RemoveImageHandler
import java.io.File

class ClothingDatabaseHandler(val context : Context) {

    private val commands : DatabaseCommands = DatabaseCommands()
    private val converter : DataTranslation = DataTranslation()

    fun addToDatabase(type : String, season : String, picture : String, redAmount : Int, greenAmount : Int, blueAmount: Int)
    {
        val newClothing = Clothing()
        newClothing.clothingType = type
        newClothing.clothingSeason = season
        newClothing.clothingImageLocation = picture
        newClothing.clothingColorRed = redAmount
        newClothing.clothingColorGreen = greenAmount
        newClothing.clothingColorBlue = blueAmount

        val accessDB = Room.databaseBuilder(context, ClothingDatabase::class.java,
            "user-clothes-database").build()

        accessDB.clothingDao().insert(newClothing)
    }

    fun getFromDataBase() : Data
    {
        val accessDB = Room.databaseBuilder(context, ClothingDatabase::class.java,
            "user-clothes-database").build()

        val foundInDataBase : Array<Clothing> = accessDB.clothingDao().getAll()

        val foundIds : ArrayList<Int?> = ArrayList()
        val foundTypes : ArrayList<String?> = ArrayList()
        val foundSeasons : ArrayList<String?> = ArrayList()
        val foundImages : ArrayList<String?> = ArrayList()
        val foundColour : ArrayList<String?> = ArrayList()

        for (item in foundInDataBase)
        {

                foundIds.add(item.id)
                foundTypes.add(item.clothingType)
                foundSeasons.add(item.clothingSeason)
                foundImages.add(item.clothingImageLocation)

            foundColour.add(converter.rgbToHexString(
                item.clothingColorRed, item.clothingColorGreen, item.clothingColorBlue
            ))

        }

        val ids : IntArray = converter.toIntArray(foundIds)
        val types : Array<String> = converter.toStringArray(foundTypes)
        val season : Array<String> = converter.toStringArray(foundSeasons)
        val image : Array<String> = converter.toStringArray(foundImages)
        val color : Array<String> = converter.toStringArray(foundColour)

        val output : Data = Data.Builder()
            .putIntArray(commands.Clothing_ID, ids)
            .putStringArray(commands.Clothing_type, types)
            .putStringArray(commands.Clothing_season, season)
            .putStringArray(commands.Clothing_picture, image)
            .putStringArray(commands.Clothing_color, color)
            .build()

        return output
    }

    fun updateInDatabase(id : Int, type : String, season : String, picture : String, redAmount : Int, greenAmount : Int, blueAmount: Int)
    {
        val newClothing = Clothing()
        newClothing.id = id
        newClothing.clothingType = type
        newClothing.clothingSeason = season
        newClothing.clothingImageLocation = picture
        newClothing.clothingColorRed = redAmount
        newClothing.clothingColorGreen = greenAmount
        newClothing.clothingColorBlue = blueAmount

        val accessDB = Room.databaseBuilder(context, ClothingDatabase::class.java,
            "user-clothes-database").build()

        accessDB.clothingDao().update(newClothing)
    }

    fun deleteFromDatabase(clothingId : Int)
    {
        val accessDB = Room.databaseBuilder(context, ClothingDatabase::class.java,
            "user-clothes-database").build()

        val imageLocation : String = accessDB.clothingDao().getImageLocation(clothingId)[0]

        val imageRemover = RemoveImageHandler()

        imageRemover.removeImage(imageLocation)

        accessDB.clothingDao().deleteById(clothingId)
    }

}