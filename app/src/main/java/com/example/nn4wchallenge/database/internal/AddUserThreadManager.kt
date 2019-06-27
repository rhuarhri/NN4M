package com.example.nn4wchallenge.database.internal

import android.content.Context
import androidx.room.Room
import androidx.work.Data
import androidx.work.Worker
import androidx.work.WorkerParameters

class AddUserThreadManager (appContext: Context, workerParams: WorkerParameters)
    : Worker(appContext, workerParams)
{
    override fun doWork(): Result {


        var input : MutableMap<String, Any> = inputData.keyValueMap

        var newUser : user = input["new"] as user

        val accessDB = Room.databaseBuilder(applicationContext, userDatabase::class.java,
            "user-info-database").build()

        accessDB.userDao().insert(newUser)

        return Result.success()

    }


}