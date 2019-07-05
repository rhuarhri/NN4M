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
import com.example.nn4wchallenge.database.matchClothingHandler
import com.example.nn4wchallenge.database.matchClothingInterface
import com.example.nn4wchallenge.imageHandling.retrieveImageHandler
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import java.io.ByteArrayOutputStream

class MainActivity : AppCompatActivity() {

    //available clothing items
    private var clothingImages : Array<String>? = null
    private var clothingDescriptions : Array<String>? = null
    private var userClothingImage : Array<String>? = null
    private var currentClothingItem : Int = 0

    private lateinit var accountBTN : Button
    private lateinit var cartBTN : Button
    private lateinit var likeBTN : Button
    private lateinit var dislikeBTN : Button

    private lateinit var totalCostTXT : TextView


    private lateinit var topIV : ImageView
    private lateinit var bottomIV : ImageView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setupSearchThread()

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

            if (clothingDescriptions != null){
            showItemDescription(clothingDescriptions!![currentClothingItem])
            }
            else{
                Toast.makeText(applicationContext, "no description available", Toast.LENGTH_LONG).show()
            }

        }

        dislikeBTN = findViewById(R.id.dislikeBTN)
        dislikeBTN.setOnClickListener {

            currentClothingItem++
            if (currentClothingItem >= clothingImages!!.size)
            {
                currentClothingItem = 0
            }

            setTopImage(clothingImages?.get(currentClothingItem))
            setBottomImage(userClothingImage?.get(currentClothingItem))

        }

        totalCostTXT = findViewById(R.id.totalTXT)

        topIV = findViewById(R.id.topIV)

        bottomIV = findViewById(R.id.bottomIV)

    }

    private fun showItemDescription(descriptionURL : String)
    {
        val goToItemDescriptionScreen : Intent = Intent(applicationContext, ItemDescriptionActivity::class.java)
        goToItemDescriptionScreen.putExtra("description", descriptionURL)
        startActivity(goToItemDescriptionScreen)
    }

    //this will display the image of the clothing the user can buy
    private fun setTopImage(location : String?)
    {
        doAsync {

            val getImage : retrieveImageHandler = retrieveImageHandler(applicationContext)

            val foundImage : Bitmap = getImage.getBitmapFromURL(location, topIV.height, topIV.width)

            uiThread {
                topIV.setImageBitmap(foundImage)
            }
        }
    }

    //this will display the image of the clothing the user already has
    private fun setBottomImage(location : String?)
    {
        doAsync {

            val getImage : retrieveImageHandler = retrieveImageHandler(applicationContext)

            val foundImage : Bitmap = getImage.getBitmapFromURL(location, bottomIV.height, bottomIV.width)

            uiThread {
                bottomIV.setImageBitmap(foundImage)
            }
        }
    }

    private fun setupSearchThread()
    {
            val searchWorkRequest = OneTimeWorkRequestBuilder<matchClothingHandler>()
                .build()

            WorkManager.getInstance().enqueue(searchWorkRequest)

            WorkManager.getInstance().getWorkInfoByIdLiveData(searchWorkRequest.id)
                .observe(this, Observer { workInfo ->
                    if (workInfo != null && workInfo.state == WorkInfo.State.SUCCEEDED) {
                        //val availableClothesImages : Array<String>? = workInfo.outputData.getStringArray("images")
                        //val availableClothesDescriptions = workInfo.outputData.getStringArray("descriptions")
                        //val ownedClothesImage = workInfo.outputData.getStringArray("userClothing")

                        clothingImages = workInfo.outputData.getStringArray("images")//= availableClothesImages
                        clothingDescriptions = workInfo.outputData.getStringArray("descriptions")// = availableClothesDescriptions
                        userClothingImage = workInfo.outputData.getStringArray("userClothing")//= ownedClothesImage

                        if (clothingImages != null && userClothingImage != null) {
                            setTopImage(clothingImages?.get(currentClothingItem))
                            setBottomImage(userClothingImage?.get(currentClothingItem))
                        }
                        else{
                            Toast.makeText(applicationContext, "no matches found", Toast.LENGTH_LONG).show()
                        }

                    }

                    if (workInfo != null && workInfo.state == WorkInfo.State.FAILED)
                    {
                        var error : String? = workInfo.outputData.getString("error")

                        Toast.makeText(applicationContext, "search error : ${error.toString()}", Toast.LENGTH_LONG).show()
                    }
                })

    }
}
