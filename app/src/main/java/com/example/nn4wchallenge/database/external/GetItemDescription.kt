package com.example.nn4wchallenge.database.external

import android.content.Context
import androidx.work.Data
import androidx.work.Worker
import androidx.work.WorkerParameters

class GetItemDescription (appContext: Context, workerParams: WorkerParameters)
    : Worker(appContext, workerParams) {

    private var error = ""

    private var name = ""
    private var description = ""
    private var cost = 0.0
    private var reduction = 0
    private lateinit var images : Array<String>

    override fun doWork(): Result {

        val descriptionLocation : String = inputData.getString("url").toString()

        val getFromOnline = OnlineDatabase()

        try
        {
            val itemDescription = getFromOnline.getItemDescription(descriptionLocation)
            name = itemDescription.name
            description = itemDescription.description
            cost = itemDescription.cost
            reduction = itemDescription.reduction
            val dataConverter = DataTranslation()
            images = dataConverter.toStringArray(itemDescription.images as ArrayList<String?>)
        }
        catch(e : Exception)
        {
            error = "find description error ${e.toString()}"
        }


        return setupOutput()
    }

    private fun setupOutput() : Result {
        val output: Data = Data.Builder()
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