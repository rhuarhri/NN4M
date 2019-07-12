package com.example.nn4wchallenge.database.internal

import android.content.Context
import androidx.room.Room
import androidx.work.Data
import androidx.work.Worker
import androidx.work.WorkerParameters

/*
class AddUserThreadManager (appContext: Context, workerParams: WorkerParameters)
    : Worker(appContext, workerParams)
{
    override fun doWork(): Result {

        var newUser : user = user()
        newUser.userGender = inputData.getString("gender").toString()
        newUser.userAge = inputData.getString("age").toString()
        newUser.userChestMeasurement = inputData.getInt("chest",0)
        newUser.userWaistMeasurement = inputData.getInt("waist",0)
        newUser.userShoeSize = inputData.getInt("shoe",0)


        val accessDB = Room.databaseBuilder(applicationContext, userDatabase::class.java,
            "user-info-database").build()

        accessDB.userDao().insert(newUser)

        return Result.success()

    }


}*/