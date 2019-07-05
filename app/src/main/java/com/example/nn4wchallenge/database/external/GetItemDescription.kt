package com.example.nn4wchallenge.database.external

import android.content.Context
import androidx.work.Data
import androidx.work.Worker
import androidx.work.WorkerParameters

class GetItemDescription (appContext: Context, workerParams: WorkerParameters)
    : Worker(appContext, workerParams) {

    private var error : String = ""

    private var name : String = ""
    private var description : String = ""
    private var cost : Double = 0.0
    private var reduction : Int = 0
    private lateinit var images : Array<String>

    override fun doWork(): Result {

        val descriptionLocation : String = inputData.getString("url").toString()


        val getFromOnline : onlineDatabase = onlineDatabase()

        try
        {
            val itemDescription = getFromOnline.getItemDescription(descriptionLocation)
            name = itemDescription.name
            description = itemDescription.description
            cost = itemDescription.cost
            reduction = itemDescription.reduction
            images = itemDescription.images.toArray() as Array<String>
        }
        catch(e : Exception)
        {
            error = "find description error ${e.toString()}"
        }


        return setupOutput()
    }

    private fun setupOutput() : Result {
        var output: Data = Data.Builder()
            .putString("error", error)
            .putString("name", name)
            .putString("description", description)
            .putDouble("cost", cost)
            .putInt("reduction", reduction)
            .putStringArray("images", images)
            .build()

        if (error == "") {
            return Result.success(output)
        } else {
            return Result.failure(output)
        }
    }

}