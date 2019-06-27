package com.example.nn4wchallenge.database

import android.content.Context
import androidx.room.Room
import androidx.work.Data
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.example.nn4wchallenge.database.external.onlineDatabase
import com.example.nn4wchallenge.database.external.searchItem
import com.example.nn4wchallenge.database.external.searchManager
import com.example.nn4wchallenge.database.internal.clothing
import com.example.nn4wchallenge.database.internal.clothingDatabase
import com.example.nn4wchallenge.database.internal.user
import com.example.nn4wchallenge.database.internal.userDatabase
import java.util.*
import kotlin.collections.ArrayList

/*
This combines various classes together to create a list of paired clothes
that is shown to the user
 */

class matchClothingHandler (appContext: Context, workerParams: WorkerParameters)
    : Worker(appContext, workerParams) {

    var availableClothing : ArrayList<searchItem> = ArrayList()
    var allUserInfo : ArrayList<user> = ArrayList()
    var allUserClothes : ArrayList<clothing> = ArrayList()

    var searcher : searchManager = searchManager()

    var error : String = ""
    var imageURLList : ArrayList<String> = ArrayList()
    var itemDescriptionURLList : ArrayList<String> = ArrayList()


    override fun doWork(): Result {

        getOnlineData()

        getUserInformation()

        getUserClothesInformation()

        var userInfo : user = allUserInfo[0]

        searcher.setupUserInfo(userInfo)

        for (item in allUserClothes)
        {

            searcher.setupClothingInfo(item)

            var searchResults = searcher.search(availableClothing)

            for (searchItem in searchResults)
            {
                imageURLList.add(searchItem.image)
                itemDescriptionURLList.add(searchItem.descriptionLocation)
            }
        }

        return setupOutput()
    }

    private fun getOnlineData()
    {
        if (availableClothing.isEmpty())
        {
            var jsonOnline : onlineDatabase = onlineDatabase()
            try
            {
                availableClothing =  jsonOnline.getAvailableClothes()
            }
            catch(e: Exception)
            {
                error = "Error in data gathering is $e"
            }
        }
    }

    private fun getUserInformation()
    {
        val accessDB = Room.databaseBuilder(applicationContext, userDatabase::class.java,
            "user-info-database").build()
        allUserInfo = Arrays.asList(accessDB.userDao().getAll()) as ArrayList<user>
    }

    private fun getUserClothesInformation()
    {
        val accessDB = Room.databaseBuilder(applicationContext, clothingDatabase::class.java,
            "user-clothes-database").build()

        allUserClothes = Arrays.asList(accessDB.clothingDao().getAll()) as ArrayList<clothing>
    }


    private fun setupOutput() : Result
    {
        var output : Data = Data.Builder()
            .putString("error", error)
            .putStringArray("images", imageURLList.toArray() as Array<out String>)
            .putStringArray("descriptions", itemDescriptionURLList.toArray() as Array<out String>)
            .build()

        if (error == "")
        {
            return Result.success(output)
        }
        else
        {
            return Result.failure(output)
        }

    }
}