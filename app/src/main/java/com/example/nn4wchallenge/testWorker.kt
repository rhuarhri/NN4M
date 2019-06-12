package com.example.nn4wchallenge

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import androidx.palette.graphics.Palette
import androidx.work.Data
import androidx.work.Worker
import androidx.work.WorkerParameters
import java.io.ByteArrayInputStream

class testWorker (appContext: Context, workerParams: WorkerParameters)
    : Worker(appContext, workerParams) {

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
    }


}