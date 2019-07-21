package com.example.nn4wchallenge.database

import android.content.Context
import androidx.room.Room
import androidx.work.Data
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.example.nn4wchallenge.database.external.DataTranslation
import com.example.nn4wchallenge.database.external.SearchItem
import com.example.nn4wchallenge.database.external.SearchManager
import com.example.nn4wchallenge.database.internal.clothingDatabaseCode.Clothing
import com.example.nn4wchallenge.database.internal.clothingDatabaseCode.ClothingDatabase
import com.example.nn4wchallenge.database.internal.userDatabaseCode.User
import com.example.nn4wchallenge.database.internal.userDatabaseCode.UserDatabase

/*
Go to Quick search activity for more information on what this does
 */

class QuickSearchHandler (appContext: Context, workerParams: WorkerParameters)
    : Worker(appContext, workerParams)
{

    private val dataConverter : DataTranslation = DataTranslation()

    private var availableClothing : ArrayList<SearchItem> = ArrayList()
    private var allUserInfo : Array<User>? = null
    private var allUserClothes : Array<Clothing>? = null

    private var searcher : SearchManager = SearchManager()

    private var userClothingImage : ArrayList<String?> = ArrayList()

    override fun doWork(): Result {

        val type : String? = inputData.getString("type")
        val season : String? = inputData.getString("season")
        val redAmount : Int = inputData.getInt("red", 0)
        val greenAmount : Int = inputData.getInt("green", 0)
        val blueAmount : Int = inputData.getInt("blue", 0)

        getUserInformation()

        getUserClothesInformation()

        val userInfo: User = allUserInfo?.get(0)!!

        searcher.setupUserInfo(userInfo)

        if (type != null && season != null) {
            val searchWithItem = SearchItem()
            searchWithItem.type = type
            searchWithItem.season = season
            val hexColourValue = (dataConverter.rgbToHexString(redAmount, greenAmount, blueAmount))
            /*hexColourValue is going to be converted back into red, green and blue integers that
            where used to make it but it was done this way as to not mess with the searchItem code
            and in turn all classes that rely on it*/
            searchWithItem.colour = hexColourValue
            /*
            The reason that the rest of the values are copies from userInfo is because this information
            is not really necessary as the user will have a already decided that the item will fit them.
             */
            searchWithItem.gender = userInfo.userGender
            searchWithItem.age = userInfo.userAge
            searchWithItem.maxSize = "18"
            searchWithItem.minSize = "5"
            //the user can see the item so these are blank
            searchWithItem.descriptionURL = ""
            searchWithItem.imageURL = ""

            availableClothing.add(searchWithItem)
        }
        else
        {
            userClothingImage.add(type)
            userClothingImage.add(season)
            return setupOutput()
        }


        for (item in allUserClothes!!.iterator()) {

            searcher.setupClothingInfo(item)

            val searchResults = searcher.search(availableClothing)

            for (searchItem in searchResults) {
                userClothingImage.add(item.clothingImageLocation)
            }

        }


        return setupOutput()
        }

    private fun getUserInformation()
    {
        val accessDB = Room.databaseBuilder(applicationContext, UserDatabase::class.java,
            "user-info-database").build()
        allUserInfo = accessDB.userDao().getAll()
    }


    private fun getUserClothesInformation()
    {
        val accessDB = Room.databaseBuilder(applicationContext, ClothingDatabase::class.java,
            "user-clothes-database").build()

        allUserClothes = accessDB.clothingDao().getAll()
    }

    private fun setupOutput() : Result
    {
        val userClothingArray : Array<String> = dataConverter.toStringArray(userClothingImage)

        val output : Data = Data.Builder()
            .putStringArray("userClothing", userClothingArray)
            .build()

            return Result.success(output)

    }

}