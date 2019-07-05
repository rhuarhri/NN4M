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

    private var availableClothing : ArrayList<searchItem> = ArrayList()
    private var allUserInfo : Array<user>? = null
    private var allUserClothes : Array<clothing>? = null

    private var searcher : searchManager = searchManager()

    private var error : String = ""
    private var imageURLList : ArrayList<String> = ArrayList()
    private var itemDescriptionURLList : ArrayList<String> = ArrayList()
    private var userClothingImage : ArrayList<String> = ArrayList()


    override fun doWork(): Result {

        getOnlineData()

        getUserInformation()

        getUserClothesInformation()


            var userInfo: user = allUserInfo?.get(0)!!

            searcher.setupUserInfo(userInfo)


                for (item in allUserClothes!!.iterator()) {

                    searcher.setupClothingInfo(item)

                    var searchResults = searcher.search(availableClothing)


                    for (searchItem in searchResults) {
                        imageURLList.add(searchItem.image)
                        itemDescriptionURLList.add(searchItem.descriptionLocation)
                        userClothingImage.add(item.clothingImageLocation.toString())

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
                error = "Error in data gathering is ${e.toString()}"
            }
        }
    }


    private fun getUserInformation()
    {
        val accessDB = Room.databaseBuilder(applicationContext, userDatabase::class.java,
            "user-info-database").build()
        allUserInfo = accessDB.userDao().getAll()
    }


    private fun getUserClothesInformation()
    {
        val accessDB = Room.databaseBuilder(applicationContext, clothingDatabase::class.java,
            "user-clothes-database").build()

        allUserClothes = accessDB.clothingDao().getAll()
    }


    private fun setupOutput() : Result
    {

        var imageArray : Array<String?> = resultToArray(imageURLList)
        var descriptionArray : Array<String?> = resultToArray(itemDescriptionURLList)
        var userClothingArray : Array<String?> = resultToArray(userClothingImage)

        var output : Data = Data.Builder()
            .putString("error", error)
            .putStringArray("images", imageArray)
            .putStringArray("descriptions", descriptionArray)
            .putStringArray("userClothing", userClothingArray)
            .build()



        if (error == "" || error == null)
        {
            return Result.success(output)
        }
        else
        {
            return Result.failure(output)
        }

        //return Result.success(output)
    }

    //the thread had problems with converting array lists to arrays hence why this exists
    private fun resultToArray(result : ArrayList<String>) : Array<String?>
    {
        var resultArray : Array<String?> = arrayOfNulls(result.size)
        var iterator : Int = 0
        for (item in result)
        {
            resultArray[iterator] = item
        }
        return resultArray
    }
}