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
import com.example.nn4wchallenge.database.internal.userDatabaseCode.User
import com.example.nn4wchallenge.database.internal.userDatabaseCode.UserDatabase

class MatchColourHandler (appContext: Context, workerParams: WorkerParameters)
    : Worker(appContext, workerParams){

    private var availableClothing : ArrayList<SearchItem> = ArrayList()
    private var allUserInfo : Array<User>? = null

    private var searcher : SearchManager = SearchManager()
    private val dataConverter : DataTranslation = DataTranslation()

    private var error : String = ""
    private var imageURLList : ArrayList<String?> = ArrayList()
    private var itemDescriptionURLList : ArrayList<String?> = ArrayList()
    private var typeList : ArrayList<String?> = ArrayList()
    private var seasonList : ArrayList<String?> = ArrayList()

    private lateinit var image : Array<String?>
    private lateinit var description : Array<String?>
    private lateinit var type : Array<String?>
    private lateinit var season : Array<String?>



    private var userClothingSeason : String = ""
    private var userClothingType : String = ""



    override fun doWork(): Result {
        val colour : String? = inputData.getString("colour")


        getOnlineData()
        getUserInformation()

        val userInfo: User = allUserInfo?.get(0)!!

        searcher.setupUserInfo(userInfo)

        if (colour != null)
        {
            try {
                val searchResults = searcher.searchByColour(availableClothing, colour!!)

                for (searchItem in searchResults) {
                    imageURLList.add(searchItem.image)
                    itemDescriptionURLList.add(searchItem.descriptionLocation)
                    typeList.add(searchItem.type)
                    seasonList.add(searchItem.season)
                }

                return setupOutput()
            }
            catch(e : Exception)
            {

                error = "colour failer"
                val output : Data = Data.Builder()
                    .putString("error", error)
                    .build()

                return Result.failure(output)
            }


        }
        else
        {

            if (inputData.getStringArray("image") == null)
            {
                error = "no images"
            }
            else {
                image = inputData.getStringArray("image")!!
            }
            if (inputData.getStringArray("description") == null) {
                error = "no description"
            }
            else {
                description = inputData.getStringArray("description")!!
            }
            if(inputData.getStringArray("type") == null)
            {
                error = "no type"
            }
            else {
                type = inputData.getStringArray("type")!!
            }
            if(inputData.getStringArray("season") == null)
            {
                error = "no season"
            }
            else {
                season = inputData.getStringArray("season")!!
            }

            if(inputData.getString("userType") == null)
            {
                error = "no input type"
            }
            else
            {
                userClothingType = inputData.getString("userType")!!
            }
            if(inputData.getString("userSeason") == null)
            {
                error = "no input season"
            }
            else {
                userClothingSeason = inputData.getString("userSeason")!!
            }

            if (userClothingSeason != "" && userClothingSeason != null && userClothingType != "" && userClothingType != null) {
                val pairs = CreatePairsList()

                val filtedResults = searcher.filterSearchResult(pairs, userClothingType, userClothingSeason)

                for (searchItem in filtedResults) {
                    imageURLList.add(searchItem.image)
                    itemDescriptionURLList.add(searchItem.descriptionLocation)
                    typeList.add(searchItem.type)
                    seasonList.add(searchItem.season)
                }
                return setupOutput()
            }
            else
            {
                error = "no season or type selected"
                val output : Data = Data.Builder()
                    .putString("error", error)
                    .build()
                return Result.failure(output)
            }

        }


    }


    @Throws(Exception::class)
    private fun CreatePairsList() : ArrayList<SearchManager.MatchedPairs>
    {
        val pairsList : ArrayList<SearchManager.MatchedPairs> = ArrayList()
        for ((i, currentImage) in image.withIndex())
        {
            if (currentImage != "" && currentImage != null && description[i] != "" && description[i] != null) {
                val newPair = SearchManager.MatchedPairs()

                var currentType = ""
                var currentSeason = ""

                if (type[i] != "" && type[i] != null)
                {
                    currentType = type[i]!!
                }

                if (season[i] != "" && season[i] != null)
                {
                    currentSeason = season[i]!!
                }

                newPair.createPair(currentImage!!, description[i]!!)

                newPair.addAditionalInfo(currentType, currentSeason)

                pairsList.add(newPair)
            }
        }

        return pairsList
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


    private fun setupOutput() : Result
    {

        val imageArray : Array<String> = dataConverter.toStringArray(imageURLList)
        val descriptionArray : Array<String> = dataConverter.toStringArray(itemDescriptionURLList)
        val typeArray : Array<String> = dataConverter.toStringArray(typeList)
        val seasonArray : Array<String> = dataConverter.toStringArray(seasonList)

        val output : Data = Data.Builder()
            .putString("error", error)
            .putStringArray("images", imageArray)
            .putStringArray("descriptions", descriptionArray)
            .putStringArray("type", typeArray)
            .putStringArray("season", seasonArray)
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