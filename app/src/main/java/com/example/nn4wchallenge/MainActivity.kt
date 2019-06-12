package com.example.nn4wchallenge

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.work.Data
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkInfo
import androidx.work.WorkManager
import java.io.ByteArrayOutputStream

class MainActivity : AppCompatActivity() {



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)





        //val p = Palette.from(picture).generate()

        //val vibrant = p.vibrantSwatch
// In Kotlin, check for null before accessing properties on the vibrant swatch.
        //val titleColor : Int = vibrant!!.titleTextColor

        //testTXT.setTextColor(titleColor)


        testWorkManager()
    }



    @SuppressLint("RestrictedApi")
    private fun testWorkManager()
    {


        val inputData : Data = Data.fromByteArray(setUpImage())




        val testWorkRequest = OneTimeWorkRequestBuilder<testWorker>()
            .setInputData(inputData)
            .build()

        WorkManager.getInstance().enqueue(testWorkRequest)



        WorkManager.getInstance().getWorkInfoByIdLiveData(testWorkRequest.id)
            .observe(this, Observer { workInfo ->
                if (workInfo != null && workInfo.state == WorkInfo.State.SUCCEEDED) {

                    var output : Int = workInfo.outputData.getInt("color", 0)
                    Toast.makeText(applicationContext, "Thread done and output is $output", Toast.LENGTH_LONG).show()
                }
            })
    }

    private fun setUpImage() : ByteArray
    {
        var picture : Bitmap = BitmapFactory.decodeResource(resources, R.drawable.test_shirt)

        var stream : ByteArrayOutputStream = ByteArrayOutputStream()

        picture.compress(Bitmap.CompressFormat.PNG, 100, stream)

        var rawData : ByteArray = stream.toByteArray()

        return rawData
    }
}
