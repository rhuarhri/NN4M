package com.example.nn4wchallenge.database.internal.userDatabaseCode

import android.content.Context
import androidx.room.Room
import androidx.work.Data
import com.example.nn4wchallenge.database.internal.databaseCommands

class userDatabaseHandler(val context : Context) {

    private val commands : databaseCommands = databaseCommands()

    fun addToDatabase(gender : String, age : String, waist : Int, chest : Int, shoe : Int)
    {
        val newUser = user()
        newUser.userGender = gender
        newUser.userAge = age
        newUser.userWaistMeasurement = waist
        newUser.userChestMeasurement = chest
        newUser.userShoeSize = shoe

        val accessDB = Room.databaseBuilder(context, userDatabase::class.java,
            "user-info-database").build()

        accessDB.userDao().insert(newUser)
    }

    fun getFromDataBase() : Data
    {
        val accessDB = Room.databaseBuilder(context, userDatabase::class.java,
            "user-info-database").build()

        val foundInDataBase : Array<user> = accessDB.userDao().getAll()

        val gender : String = foundInDataBase[0].userGender
        val age : String = foundInDataBase[0].userAge
        val chestSize : Int = foundInDataBase[0].userChestMeasurement
        val waistSize : Int = foundInDataBase[0].userWaistMeasurement
        val shoeSize : Int = foundInDataBase[0].userShoeSize

        val output : Data = Data.Builder()
            .putString(commands.User_gender, gender)
            .putString(commands.User_age, age)
            .putInt(commands.User_chest, chestSize)
            .putInt(commands.User_waist, waistSize)
            .putInt(commands.User_shoe_Size, shoeSize)
            .build()

        return output

    }

    fun deleteFromDatabase()
    {

    }

    fun updateInDatabase(gender : String, age : String, waist : Int, chest : Int, shoe : Int)
    {

        val newUser = user()
        newUser.id = 0
        newUser.userGender = gender
        newUser.userAge = age
        newUser.userWaistMeasurement = waist
        newUser.userChestMeasurement = chest
        newUser.userShoeSize = shoe

        val accessDB = Room.databaseBuilder(context, userDatabase::class.java,
            "user-info-database").build()

        accessDB.userDao().update(newUser)

    }
}