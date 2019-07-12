package com.example.nn4wchallenge.database.internal

import android.content.Context
import androidx.room.Room
import androidx.work.Data
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.example.nn4wchallenge.database.external.dataTranslation
import com.example.nn4wchallenge.database.internal.clothingDatabaseCode.clothing
import com.example.nn4wchallenge.database.internal.clothingDatabaseCode.clothingDatabase

/*
class GetClothingThreadManager(appContext: Context, workerParams: WorkerParameters)
    : Worker(appContext, workerParams)
{
    private val convertToArray : dataTranslation = dataTranslation()

    override fun doWork(): Result {
        val accessDB = Room.databaseBuilder(applicationContext, clothingDatabase::class.java,
            "user-clothes-database").build()

        val foundInDataBase : Array<clothing> = accessDB.clothingDao().getAll()

        val foundIds : ArrayList<Int?> = ArrayList()
        val foundTypes : ArrayList<String?> = ArrayList()
        val foundSeasons : ArrayList<String?> = ArrayList()
        val foundImages : ArrayList<String?> = ArrayList()

        for (item in foundInDataBase)
        {
            if (item.clothingSeason != null && item.clothingImageLocation != null)
            {
                foundIds.add(item.id)
                foundTypes.add(item.clothingType)
                foundSeasons.add(item.clothingSeason.toString())
                foundImages.add(item.clothingImageLocation.toString())
            }
        }

        val ids : IntArray = convertToArray.toIntArray(foundIds)
        val types : Array<String> = convertToArray.toStringArray(foundTypes)
        val season : Array<String> = convertToArray.toStringArray(foundSeasons)
        val image : Array<String> = convertToArray.toStringArray(foundImages)


        val output : Data = Data.Builder()
            .putIntArray("id", ids)
            .putStringArray("type", types)
            .putStringArray("season", season)
            .putStringArray("image", image)
            .build()

        return Result.success(output)

    }

}*/