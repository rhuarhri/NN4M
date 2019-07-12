package com.example.nn4wchallenge

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import androidx.palette.graphics.Palette
import androidx.room.Room
import androidx.work.Data
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.example.nn4wchallenge.database.external.onlineDatabase
import com.example.nn4wchallenge.database.internal.clothingDatabaseCode.clothingDatabase
import com.example.nn4wchallenge.database.internal.userDatabaseCode.userDatabase
import java.io.ByteArrayInputStream

class testWorker (appContext: Context, workerParams: WorkerParameters)
    : Worker(appContext, workerParams) {


    override fun doWork(): Result {

        var result = ""

        try{
            result = testUserDB()
        }
        catch(e : Exception)
        {
            result = "user data base error " + e.toString()
        }


        try
        {
            result += ("  " + testClothingDB())
        }
        catch(e : Exception)
        {
            result += " clothing data base error ${e.toString()}"
        }

        try
        {
            result += ("  " + testOnlineDB())
        }
        catch (e : Exception)
        {
            result += " online data base error ${e.toString()}"
        }

        var output : Data = Data.Builder().putString("result", result).build()

        return Result.success(output)

    }

    @Throws (Exception::class)
    private fun testUserDB() : String
    {
        var result = ""

        val accessUserDB = Room.databaseBuilder(applicationContext, userDatabase::class.java,
            "user-info-database").build()

        /*val newUser : user = user()

        newUser.userAge = "abult"
        newUser.userGender = "male"
        newUser.userChestMeasurement = "10"
        newUser.userWaistMeasurement = "10"
        newUser.userShoeSize = 9

        accessUserDB.userDao().insert(newUser)*/

        var inDatabase = accessUserDB.userDao().getAll()


        if (inDatabase.isEmpty())
        {
            result = "data base empty"
        }
        else
        {
            result = inDatabase[0].userGender
        }



        return result

    }

    @Throws (Exception::class)
    private fun testClothingDB() : String
    {
        var result = ""

        val accessClothingDB = Room.databaseBuilder(applicationContext, clothingDatabase::class.java,
            "user-clothes-database").build()

        /*
        var newClothing : clothing = clothing()

        newClothing.clothingColorRed = 255
        newClothing.clothingColorGreen = 0
        newClothing.clothingColorBlue = 0
        newClothing.clothingImageLocation = "test"
        newClothing.clothingSeason = "summer"
        newClothing.clothingType = "dress"

        accessClothingDB.clothingDao().insert(newClothing)*/

        var inDatabase = accessClothingDB.clothingDao().getAll()


        if (inDatabase.isEmpty())
        {
            result = "data base empty"
        }
        else
        {
            result = inDatabase[0].clothingType
        }

        return result
    }

    @Throws (Exception::class)
    private fun testOnlineDB() : String
    {
        var result = ""

        var testOnline = onlineDatabase()

        try {
            result = testOnline.getAvailableClothes()[0].type
        }
        catch (e : Exception)
        {
            result = "online error ${e.toString()}"
        }


        return result
    }
    /*
    @SuppressLint("RestrictedApi")
    override fun doWork(): Result {

        val bitmapByteArray = Data.toByteArray(inputData)
        val bitmap = BitmapFactory.decodeStream(ByteArrayInputStream(bitmapByteArray))

        var picture : Bitmap = bitmap

        //val p = Palette.from(picture).generate()

        //val vibrant = p.vibrantSwatch
// In Kotlin, check for null before accessing properties on the vibrant swatch.
        val titleColor : Int = 11//vibrant!!.titleTextColor

        //testTXT.setTextColor(titleColor)

        var output : Data = Data.Builder().putInt("color", titleColor).build()

        return Result.success(output)
    }*/


}