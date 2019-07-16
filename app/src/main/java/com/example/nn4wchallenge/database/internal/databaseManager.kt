package com.example.nn4wchallenge.database.internal

import android.content.Context
import androidx.work.Data
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.example.nn4wchallenge.database.internal.cartDatabaseCode.cartDatabaseHandler
import com.example.nn4wchallenge.database.internal.clothingDatabaseCode.clothingDatabaseHandler
import com.example.nn4wchallenge.database.internal.userDatabaseCode.userDatabaseHandler


/*This class exists to be an interface for the data base.
This makes it easier to maintain as changes made to the
database won't effect the rest fo the app if this class
does not change.
 */
class databaseManager (appContext: Context, workerParams: WorkerParameters)
    : Worker(appContext, workerParams) {

    val commands : databaseCommands = databaseCommands()

    override fun doWork(): Result {

        return if (inputData.getString(commands.Clothing_DB) == commands.Clothing_DB)
        {
            clothingDatabase(applicationContext, inputData)
        }
        else if (inputData.getString(commands.Cart_DB) == commands.Cart_DB)
        {
            cartDatabase(applicationContext, inputData)
        }
        else if (inputData.getString(commands.User_DB) == commands.User_DB)
        {
            userDatabase(applicationContext, inputData)
        }
        else{
            Result.failure()
        }

    }

    private fun clothingDatabase(context: Context, inputData : Data) : Result
    {
        val handler : clothingDatabaseHandler = clothingDatabaseHandler(context)

        if (inputData.getString(commands.Clothing_Add) == commands.Clothing_Add)
        {
            val type : String? = inputData.getString(commands.Clothing_type)
            val season : String? = inputData.getString(commands.Clothing_season)
            val picture : String? = inputData.getString(commands.Clothing_picture)
            val red : Int = inputData.getInt(commands.Clothing_red_color, 0)
            val green : Int = inputData.getInt(commands.Clothing_green_color, 0)
            val blue : Int = inputData.getInt(commands.Clothing_blue_color, 0)
            if (type != null && season != null && picture != null)
            {
                handler.addToDatabase(type, season, picture, red, green, blue)
                return Result.success()
            }
            else
            {
                return Result.failure()
            }
        }
        else if (inputData.getString(commands.Clothing_Get) == commands.Clothing_Get)
        {
            val output = handler.getFromDataBase()
            return Result.success(output)
        }
        else if (inputData.getString(commands.Clothing_Delete) == commands.Clothing_Delete)
        {
            val id : Int = inputData.getInt(commands.Clothing_ID, 0)
            handler.deleteFromDatabase(id)
            return Result.success()
        }
        else
        {
            return Result.failure()
        }


    }

    private fun cartDatabase(context: Context, inputData : Data) : Result
    {
        val handler : cartDatabaseHandler = cartDatabaseHandler(context)

        if (inputData.getString(commands.Cart_Add) == commands.Cart_Add) {
            val name: String? = inputData.getString(commands.Cart_name)
            val price: Double = inputData.getDouble(commands.Cart_price, 0.0)
            val picture: String? = inputData.getString(commands.Cart_picture)
            if (name != null && picture != null) {
                handler.addToDatabase(picture, name, price)
                return Result.success()
            } else {
                return Result.failure()
            }
        }
        else if (inputData.getString(commands.Cart_Get) == commands.Cart_Get)
        {
            val output = handler.getFromDataBase()
            return Result.success(output)
        }
        else if (inputData.getString(commands.Cart_Get_Prices) == commands.Cart_Get_Prices)
        {
            val output = handler.getTotalInDatabase()
            return Result.success(output)
        }
        else if (inputData.getString(commands.Cart_Delete) == commands.Cart_Delete)
        {
            val id : Int = inputData.getInt(commands.Cart_ID, 0)
            handler.deleteFromDatabase(id)
            return Result.success()
        }
        else
        {
            return Result.failure()
        }

    }

    private fun userDatabase(context: Context, inputData : Data) : Result
    {
        val handler : userDatabaseHandler = userDatabaseHandler(context)

        if (inputData.getString(commands.User_Add) == commands.User_Add) {
            val gender : String? = inputData.getString(commands.User_gender)
            val age : String? = inputData.getString(commands.User_age)
            val waist : Int = inputData.getInt(commands.User_waist, 0)
            val chest : Int = inputData.getInt(commands.User_chest, 0)
            val shoe : Int = inputData.getInt(commands.User_shoe_Size, 0)
            if (gender != null && age != null)
            {
                handler.addToDatabase(gender, age, waist, chest, shoe)
                return Result.success()
            }
            else
            {
                return Result.failure()
            }

        }
        else if (inputData.getString(commands.User_Get) == commands.User_Get)
        {
            return Result.success()
        }
        else{
            return Result.failure()
        }
    }


}