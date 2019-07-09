package com.example.nn4wchallenge.database.internal

import android.content.Context
import androidx.room.Room
import androidx.work.Data
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.example.nn4wchallenge.database.external.dataTranslation

class GetCartThreadManager(appContext: Context, workerParams: WorkerParameters)
    : Worker(appContext, workerParams)
{
    private val convertToArray : dataTranslation = dataTranslation()

    override fun doWork(): Result {
        val accessCartDB = Room.databaseBuilder(applicationContext, cartDatabase::class.java,
            "cart-database").build()

        val foundInDataBase : Array<cartItem> = accessCartDB.cartDao().getAll()

        val foundIds : ArrayList<String?> = ArrayList()
        val foundNames : ArrayList<String?> = ArrayList()
        val foundPrice : ArrayList<String?> = ArrayList()
        val foundImages : ArrayList<String?> = ArrayList()

        for (item in foundInDataBase)
        {
            foundIds.add(item.id.toString())
            foundNames.add(item.itemName)
            foundPrice.add(item.itemprice.toString())
            foundImages.add(item.itemImage)
        }

        val ids : Array<String> = convertToArray.toStringArray(foundIds)
        val name : Array<String> = convertToArray.toStringArray(foundNames)
        val price : Array<String> = convertToArray.toStringArray(foundPrice)
        val image : Array<String> = convertToArray.toStringArray(foundImages)


        val output : Data = Data.Builder()
            .putStringArray("id", ids)
            .putStringArray("name", name)
            .putStringArray("price", price)
            .putStringArray("image", image)
            .build()

        return Result.success(output)
    }


}