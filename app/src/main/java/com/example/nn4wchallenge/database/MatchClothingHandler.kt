package com.example.nn4wchallenge.database

import android.content.Context
import androidx.room.Room
import androidx.work.Data
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.example.nn4wchallenge.database.external.DataTranslation
import com.example.nn4wchallenge.database.external.OnlineDatabase
import com.example.nn4wchallenge.database.external.SearchItem
import com.example.nn4wchallenge.database.external.SearchManager
import com.example.nn4wchallenge.database.internal.clothingDatabaseCode.Clothing
import com.example.nn4wchallenge.database.internal.clothingDatabaseCode.ClothingDatabase
import com.example.nn4wchallenge.database.internal.userDatabaseCode.User
import com.example.nn4wchallenge.database.internal.userDatabaseCode.UserDatabase
import kotlin.collections.ArrayList

/*
This combines various classes together to create a list of paired clothes
that is shown to the user
 */

class MatchClothingHandler (appContext: Context, workerParams: WorkerParameters)
    : Worker(appContext, workerParams) {

    private val dataConverter : DataTranslation = DataTranslation()

    private var availableClothing : ArrayList<SearchItem> = ArrayList()
    private var allUserInfo : Array<User>? = null
    private var allUserClothes : Array<Clothing>? = null

    private var searcher : SearchManager = SearchManager()

    private var error : String = ""
    private var imageURLList : ArrayList<String?> = ArrayList()
    private var itemDescriptionURLList : ArrayList<String?> = ArrayList()
    private var userClothingImage : ArrayList<String?> = ArrayList()


    override fun doWork(): Result {

        getOnlineData()

        getUserInformation()

        getUserClothesInformation()


            val userInfo: User = allUserInfo?.get(0)!!

            searcher.setupUserInfo(userInfo)


                for (item in allUserClothes!!.iterator()) {

                    searcher.setupClothingInfo(item)

                    val searchResults = searcher.search(availableClothing)


                    for (searchItem in searchResults) {
                        imageURLList.add(searchItem.image)
                        itemDescriptionURLList.add(searchItem.descriptionLocation)
                        userClothingImage.add(item.clothingImageLocation)

                    }
                }

        return setupOutput()

    }

    private fun getOnlineData()
    {
        if (availableClothing.isEmpty())
        {
            val jsonOnline = OnlineDatabase()
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

        val imageArray : Array<String> = dataConverter.toStringArray(imageURLList)
        val descriptionArray : Array<String> = dataConverter.toStringArray(itemDescriptionURLList)
        val userClothingArray : Array<String> = dataConverter.toStringArray(userClothingImage)

        val output : Data = Data.Builder()
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

    }


}