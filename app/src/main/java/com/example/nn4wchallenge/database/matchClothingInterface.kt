package com.example.nn4wchallenge.database

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkInfo
import androidx.work.WorkManager

class matchClothingInterface {

    class matchedPairs()
    {
        var image : String = ""
        var descriptionLocation : String = ""

        fun createPair(Image : String, DescriptionLocation : String)
        {
            image = Image
            descriptionLocation = DescriptionLocation
        }
    }

    @Throws (Exception::class)
    public fun search(owner : LifecycleOwner) : ArrayList<matchedPairs>
    {
        var searchResults : ArrayList<matchedPairs> = ArrayList()

        val testWorkRequest = OneTimeWorkRequestBuilder<matchClothingHandler>()
            .build()

        WorkManager.getInstance().enqueue(testWorkRequest)

        WorkManager.getInstance().getWorkInfoByIdLiveData(testWorkRequest.id)
            .observe(owner, Observer { workInfo ->
                if (workInfo != null && workInfo.state == WorkInfo.State.SUCCEEDED) {
                    var clothingImages : Array<String> = workInfo.outputData.getStringArray("images") as Array<String>
                    var clothingDescriptions : Array<String> = workInfo.outputData.getStringArray("descriptions") as Array<String>

                    for (i in 0..clothingImages!!.size)
                    {
                        var newResult : matchedPairs = matchedPairs()
                        newResult.createPair(clothingImages[i], clothingDescriptions[i])

                        searchResults.add(newResult)
                    }
                }

                if (workInfo != null && workInfo.state == WorkInfo.State.FAILED)
                {
                    var error : String? = workInfo.outputData.getString("error")

                    throw Exception(error)
                }
            })
        return searchResults
    }
}