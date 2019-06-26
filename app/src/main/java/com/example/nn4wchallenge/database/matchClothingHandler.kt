package com.example.nn4wchallenge.database

import android.content.Context
import androidx.work.Data
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.example.nn4wchallenge.database.external.onlineDatabase
import com.example.nn4wchallenge.database.external.searchItem

/*
This combines various classes together to create a list of paired clothes
that is shown to the user
 */

class matchClothingHandler (appContext: Context, workerParams: WorkerParameters)
    : Worker(appContext, workerParams) {

    override fun doWork(): Result {

        var message : String = ""

        var jsonOnline : onlineDatabase = onlineDatabase()

        try{
            var result : ArrayList<searchItem> = jsonOnline.getAvailableClothes()

            message = ""+ result.size//testOutput(result)
            //message = jsonOnline.convertStreamToString()

            //if (message == "")
            //{
                //message = "no file"
            //}
        }
        catch(e : Exception)
        {
            message = "json " + e

        }

        var output : Data = Data.Builder().putString("message", message).build()

        return Result.success(output)
    }

    private fun testOutput(input : ArrayList<searchItem>) : String
    {
        var output : String = ""
        var count : Int = 0

        for (item in input)
        {
            count++
            if (item.colour != "")
            {
                output += ("$count: ${item.colour} ")
            }

            if (item.gender != "")
            {
                output += "$count: ${item.gender} "
            }

            if (item.season != "")
            {
                output += "$count: ${item.season} "
            }

            if (item.type != "")
            {
                output += "$count: ${item.type} "
            }

            if (item.maxSize != "")
            {
                output += "$count: ${item.maxSize} "
            }

            if (item.minSize != "")
            {
                output += "$count: ${item.minSize} "
            }

            if (item.imageURL != "")
            {
                output += "$count: image "
            }

            if (item.age != "")
            {
                output += "$count: ${item.age} "
            }

            if (item.descriptionURL != "")
            {
                output += "$count: description "
            }
        }

        return output
    }


}