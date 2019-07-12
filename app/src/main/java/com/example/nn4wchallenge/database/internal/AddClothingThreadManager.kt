package com.example.nn4wchallenge.database.internal

import android.content.Context
import androidx.room.Room
import androidx.work.Data
import androidx.work.Worker
import androidx.work.WorkerParameters

/*
class AddClothingThreadManager (appContext: Context, workerParams: WorkerParameters)
    : Worker(appContext, workerParams)
{
    override fun doWork(): Result {


        var newClothing : clothing = clothing()
        newClothing.clothingType = inputData.getString("type").toString()
        newClothing.clothingSeason = inputData.getString("season").toString()
        newClothing.clothingImageLocation = inputData.getString("picture").toString()
        newClothing.clothingColorBlue = inputData.getInt("blue",0)
        newClothing.clothingColorGreen = inputData.getInt("green", 0)
        newClothing.clothingColorRed = inputData.getInt("red",0)


        val accessDB = Room.databaseBuilder(applicationContext, clothingDatabase::class.java,
        "user-clothes-database").build()

        accessDB.clothingDao().insert(newClothing)

        return Result.success()
    }


}*/