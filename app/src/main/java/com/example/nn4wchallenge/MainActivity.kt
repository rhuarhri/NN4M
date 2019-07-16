package com.example.nn4wchallenge


import android.content.Intent
import android.graphics.Bitmap
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.work.Data
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkInfo
import androidx.work.WorkManager
import com.example.nn4wchallenge.database.internal.databaseCommands
import com.example.nn4wchallenge.database.internal.databaseManager
import com.example.nn4wchallenge.database.matchClothingHandler
import com.example.nn4wchallenge.imageHandling.retrieveImageHandler
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread

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

        totalCostTXT = findViewById(R.id.totalTXT)
        setupTotalPrice()

        topIV = findViewById(R.id.topIV)

        bottomIV = findViewById(R.id.bottomIV)

        setupSearchThread()

        accountBTN = findViewById(R.id.accountBTN)
        accountBTN.setOnClickListener {

            val goToUserClothingDisplayScreen = Intent(applicationContext, UserClothingDisplayActivity::class.java)
            startActivity(goToUserClothingDisplayScreen)

        }

        cartBTN = findViewById(R.id.cartBTN)
        cartBTN.setOnClickListener {

            val goToViewCartScreen = Intent(applicationContext, ViewCartActivity::class.java)
            startActivity(goToViewCartScreen)
        }

        likeBTN = findViewById(R.id.likeBTN)
        likeBTN.setOnClickListener {

            if (clothingDescriptions!![currentClothingItem] != null){
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



    }

    private fun showItemDescription(descriptionURL : String)
    {
        val goToItemDescriptionScreen = Intent(applicationContext, ItemDescriptionActivity::class.java)
        goToItemDescriptionScreen.putExtra("description", descriptionURL)
        startActivity(goToItemDescriptionScreen)
    }

    //this will display the image of the clothing the user can buy
    private fun setTopImage(location : String?)
    {
        doAsync {

            val getImage = retrieveImageHandler(applicationContext)

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

            val getImage = retrieveImageHandler(applicationContext)

            val foundImage : Bitmap = getImage.getBitmapFromFile(location, bottomIV.height, bottomIV.width)

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

                        clothingImages = workInfo.outputData.getStringArray("images")
                        clothingDescriptions = workInfo.outputData.getStringArray("descriptions")
                        userClothingImage = workInfo.outputData.getStringArray("userClothing")

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
                        val error : String? = workInfo.outputData.getString("error")

                        Toast.makeText(applicationContext, "search error : ${error.toString()}", Toast.LENGTH_LONG).show()
                    }
                })

    }

    private fun setupTotalPrice()
    {
        val commands = databaseCommands()

        val input : Data = Data.Builder()
            .putString(commands.Cart_DB, commands.Cart_DB)
            .putString(commands.Cart_Get_Prices, commands.Cart_Get_Prices)
            .build()

        val getTotalWorker = OneTimeWorkRequestBuilder<databaseManager>().setInputData(input).build()


        WorkManager.getInstance().enqueue(getTotalWorker)

        WorkManager.getInstance().getWorkInfoByIdLiveData(getTotalWorker.id).observe(this, Observer {
                workInfo ->

            val total = workInfo.outputData.getDouble(commands.Cart_total_price, 0.0)

            totalCostTXT.text = "total $total"

        })
    }
}
