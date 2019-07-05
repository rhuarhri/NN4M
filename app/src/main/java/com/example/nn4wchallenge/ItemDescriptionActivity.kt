package com.example.nn4wchallenge

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.SeekBar
import android.widget.TextView
import androidx.lifecycle.Observer
import androidx.work.Data
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkInfo
import androidx.work.WorkManager
import com.example.nn4wchallenge.database.external.GetItemDescription
import com.example.nn4wchallenge.database.internal.AddToCartThreadHandler
import com.example.nn4wchallenge.database.internal.cartItem

class ItemDescriptionActivity : AppCompatActivity() {

    private lateinit var clothingIV : ImageView

    private lateinit var descriptionTXT : TextView
    private lateinit var nameTXT : TextView
    private lateinit var priceTXT : TextView
    private lateinit var reducedPriceTXT : TextView

    private lateinit var imageSearchSB : SeekBar

    private lateinit var cartBTN : Button

    private var price : Double = 0.0
    private var itemImage : String = ""
    private var name : String = ""

    private lateinit var imageURLs : Array<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_item_description)

        val descriptionlocation : String = intent.getStringExtra("description")

        clothingIV = findViewById(R.id.clothingPicture)
        descriptionTXT = findViewById(R.id.descriptionTXT)
        nameTXT = findViewById(R.id.nameTXT)
        priceTXT = findViewById(R.id.priceTXT)
        reducedPriceTXT = findViewById(R.id.redudedPriceTXT)
        imageSearchSB = findViewById(R.id.searchPictureSB)
        cartBTN = findViewById(R.id.cartBTN)

        val inputData : Data = Data.Builder().putString("url", descriptionlocation).build()

        val GetDescriptionWorker = OneTimeWorkRequestBuilder<GetItemDescription>()
            .setInputData(inputData)
            .build()

        WorkManager.getInstance().enqueue(GetDescriptionWorker)

        WorkManager.getInstance().getWorkInfoByIdLiveData(GetDescriptionWorker.id)
            .observe(this, Observer { workInfo ->
                if (workInfo != null && workInfo.state == WorkInfo.State.SUCCEEDED) {

                    name = workInfo.outputData.getString("name").toString()
                    nameTXT.setText(name)

                    descriptionTXT.setText(workInfo.outputData.getString("description").toString())

                    price = workInfo.outputData.getDouble("cost", 0.0)
                    priceTXT.setText("$price")

                    val reduction : Int = workInfo.outputData.getInt("reduction", 0)
                    if (reduction > 0)
                    {
                        reducedPriceTXT.setText("$reduction off")
                    }

                    imageURLs = workInfo.outputData.getStringArray("images") as Array<String>

                    if (imageURLs.isNotEmpty())
                    {
                        itemImage = imageURLs[0]
                    }

                    cartBTN.setOnClickListener {
                        //cart button only usable if data available
                        addToCart()
                    }

                }
                if (workInfo != null && workInfo.state == WorkInfo.State.FAILED)
                {
                    descriptionTXT.setText(workInfo.outputData.getString("error").toString())
                }
            })

    }

    private fun addToCart()
    {

        var input : Data = Data.Builder()
            .putString("image", itemImage)
            .putString("name", name)
            .putDouble("price", price)
            .build()

        val addToCartWorker = OneTimeWorkRequestBuilder<AddToCartThreadHandler>()
            .setInputData(input)
            .build()

        WorkManager.getInstance().enqueue(addToCartWorker)

    }
}
