package com.example.nn4wchallenge.database.internal.cartDatabaseCode

import android.content.Context
import androidx.room.Room
import androidx.work.Data
import com.example.nn4wchallenge.database.external.dataTranslation
import com.example.nn4wchallenge.database.internal.databaseCommands

class cartDatabaseHandler(val context : Context) {

    private val commands : databaseCommands = databaseCommands()

    public fun addToDatabase(picture : String, name : String, price : Double)
    {
        val newCartItem : cartItem = cartItem()
        newCartItem.itemImage = picture
        newCartItem.itemName = name
        newCartItem.itemprice = price

        val accessDB = Room.databaseBuilder(context, cartDatabase::class.java,
            "cart-database").build()

        accessDB.cartDao().insert(newCartItem)
    }

    public fun getFromDataBase() : Data
    {
        val convertToArray : dataTranslation = dataTranslation()

        val accessCartDB = Room.databaseBuilder(context, cartDatabase::class.java,
            "cart-database").build()
        val foundInDataBase : Array<cartItem> = accessCartDB.cartDao().getAll()

        val foundIds : ArrayList<Int?> = ArrayList()
        val foundNames : ArrayList<String?> = ArrayList()
        val foundPrice : ArrayList<Double?> = ArrayList()
        val foundImages : ArrayList<String?> = ArrayList()

        for (item in foundInDataBase)
        {
            foundIds.add(item.id)
            foundNames.add(item.itemName)
            foundPrice.add(item.itemprice)
            foundImages.add(item.itemImage)
        }

        val ids : IntArray = convertToArray.toIntArray(foundIds)
        val name : Array<String> = convertToArray.toStringArray(foundNames)
        val price : DoubleArray = convertToArray.toDoubleArray(foundPrice)
        val image : Array<String> = convertToArray.toStringArray(foundImages)


        val output : Data = Data.Builder()
            .putIntArray(commands.Cart_ID, ids)
            .putStringArray(commands.Cart_name, name)
            .putDoubleArray(commands.Cart_price, price)
            .putStringArray(commands.Cart_picture, image)
            .build()

        return output

    }

    public fun deleteFromDatabase()
    {

    }

    public fun updateInDatabase()
    {

    }

}