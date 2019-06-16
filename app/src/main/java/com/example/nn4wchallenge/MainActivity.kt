package com.example.nn4wchallenge

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
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


    private lateinit var accountBTN : Button
    private lateinit var cartBTN : Button
    private lateinit var likeBTN : Button
    private lateinit var dislikeBTN : Button

    private lateinit var totalCostTXT : TextView

    private lateinit var topIV : ImageView
    private lateinit var bottomIV : ImageView
    /*
    topIV will display clothes that normally are above or on top of
    the clothes displayed in the bottomIV for example shirt will be displayed
    on the topIV and trouser will be displayed in the bottomIV
     */

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        accountBTN = findViewById(R.id.accountBTN)
        accountBTN.setOnClickListener {

            val goToUserClothingDisplayScreen : Intent = Intent(applicationContext, UserClothingDisplayActivity::class.java)
            startActivity(goToUserClothingDisplayScreen)

        }

        cartBTN = findViewById(R.id.cartBTN)
        cartBTN.setOnClickListener {

            val goToViewCartScreen : Intent = Intent(applicationContext, ViewCartActivity::class.java)
            startActivity(goToViewCartScreen)
        }

        likeBTN = findViewById(R.id.likeBTN)
        likeBTN.setOnClickListener {


        }

        dislikeBTN = findViewById(R.id.dislikeBTN)
        dislikeBTN.setOnClickListener {


        }

        totalCostTXT = findViewById(R.id.totalTXT)

        topIV = findViewById(R.id.topIV)

        bottomIV = findViewById(R.id.bottomIV)


        //val p = Palette.from(picture).generate()

        //val vibrant = p.vibrantSwatch
// In Kotlin, check for null before accessing properties on the vibrant swatch.
        //val titleColor : Int = vibrant!!.titleTextColor

        //testTXT.setTextColor(titleColor)


       // testWorkManager()
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
