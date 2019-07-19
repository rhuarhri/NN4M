package com.example.nn4wchallenge.database.internal.cartDatabaseCode

import android.content.Context
import androidx.room.Room
import androidx.work.Data
import com.example.nn4wchallenge.database.external.dataTranslation
import com.example.nn4wchallenge.database.internal.databaseCommands

class cartDatabaseHandler(val context : Context) {

    private val commands : databaseCommands = databaseCommands()

    fun addToDatabase(picture : String, name : String, price : Double)
    {
        val newCartItem = cartItem()
        newCartItem.itemImage = picture
        newCartItem.itemName = name
        newCartItem.itemprice = price

        val accessDB = Room.databaseBuilder(context, cartDatabase::class.java,
            "cart-database").build()

        accessDB.cartDao().insert(newCartItem)
    }

    fun getFromDataBase() : Data
    {
        val convertToArray = dataTranslation()

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

    fun deleteFromDatabase(clothingID : Int)
    {

        val accessCartDB = Room.databaseBuilder(context, cartDatabase::class.java,
            "cart-database").build()

        accessCartDB.cartDao().deleteById(clothingID)

    }

    fun updateInDatabase()
    {

    }

    fun getTotalInDatabase() : Data
    {

        val accessCartDB = Room.databaseBuilder(context, cartDatabase::class.java,
            "cart-database").build()
        val foundInDataBase : Array<Double> = accessCartDB.cartDao().getPriceList()

        var totalPrice : Double = 0.0

        for (price in foundInDataBase)
        {
            totalPrice += price
        }

        val output : Data = Data.Builder().putDouble(commands.Cart_total_price, totalPrice).build()

        return output
    }

    fun getSpecificItemInDatabase(id : Int) : Data
    {
        val accessCartDB = Room.databaseBuilder(context, cartDatabase::class.java,
            "cart-database").build()
        val foundInDataBase : cartItem = accessCartDB.cartDao().getItem(id)[0]


        val output : Data = Data.Builder()
            .putInt(commands.Cart_ID, foundInDataBase.id)
            .putString(commands.Cart_name, foundInDataBase.itemName)
            .putDouble(commands.Cart_price, foundInDataBase.itemprice)
            .putString(commands.Cart_picture, foundInDataBase.itemImage)
            .build()

        return output


    }

}