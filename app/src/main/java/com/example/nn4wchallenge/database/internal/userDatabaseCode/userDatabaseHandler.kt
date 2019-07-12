package com.example.nn4wchallenge.database.internal.userDatabaseCode

import android.content.Context
import androidx.room.Room

class userDatabaseHandler(val context : Context) {

    public fun addToDatabase(gender : String, age : String, waist : Int, chest : Int, shoe : Int)
    {
        val newUser : user = user()
        newUser.userGender = gender
        newUser.userAge = age
        newUser.userWaistMeasurement = waist
        newUser.userChestMeasurement = chest
        newUser.userShoeSize = shoe

        val accessDB = Room.databaseBuilder(context, userDatabase::class.java,
            "user-info-database").build()

        accessDB.userDao().insert(newUser)
    }

    public fun getFromDataBase()
    {

    }

    public fun deleteFromDatabase()
    {

    }

    public fun updateInDatabase()
    {

    }
}