package com.example.nn4wchallenge.database

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkInfo
import androidx.work.WorkManager
import java.util.*
import kotlin.collections.ArrayList

class matchClothingInterface {

    class matchedPairs()
    {
        var image : String = ""
        var descriptionLocation : String = ""
        var userClotingImage : String = ""

        fun createPair(Image : String, DescriptionLocation : String, UserClothing : String)
        {
            image = Image
            descriptionLocation = DescriptionLocation
            userClotingImage = UserClothing
        }
    }


    public fun search() : UUID
    {


        val searchWorkRequest = OneTimeWorkRequestBuilder<matchClothingHandler>()
            .build()

        WorkManager.getInstance().enqueue(searchWorkRequest)


        return searchWorkRequest.id
    }

    @Throws (Exception::class)
    public fun handleOutput(input : WorkInfo) : ArrayList<matchedPairs>
    {
        var searchResults : ArrayList<matchedPairs> = ArrayList()

        if (input != null && input.state == WorkInfo.State.SUCCEEDED) {
            var clothingImages : Array<String> = input.outputData.getStringArray("images") as Array<String>
            var clothingDescriptions : Array<String> = input.outputData.getStringArray("descriptions") as Array<String>
            var userClothingImage : Array<String> = input.outputData.getStringArray("userClothing") as Array<String>

            for (i in 0..clothingImages!!.size)
            {
                var newResult : matchedPairs = matchedPairs()
                newResult.createPair(clothingImages[i], clothingDescriptions[i], userClothingImage[i])

                searchResults.add(newResult)
            }
        }

        if (input != null && input.state == WorkInfo.State.FAILED)
        {
            var error : String? = input.outputData.getString("error")

            throw Exception(error)
        }

        return searchResults
    }
}