package com.example.nn4wchallenge

import android.graphics.Bitmap
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.*
import androidx.lifecycle.Observer
import androidx.work.Data
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkInfo
import androidx.work.WorkManager
import com.example.nn4wchallenge.database.external.GetItemDescription
import com.example.nn4wchallenge.database.internal.databaseCommands
import com.example.nn4wchallenge.database.internal.databaseManager
import com.example.nn4wchallenge.imageHandling.retrieveImageHandler
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread

class ItemDescriptionActivity : AppCompatActivity() {

    private lateinit var clothingIV : ImageView
    private lateinit var imageHandler : retrieveImageHandler

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

        imageHandler = retrieveImageHandler(applicationContext)
        clothingIV = findViewById(R.id.clothingPicture)
        descriptionTXT = findViewById(R.id.descriptionTXT)
        nameTXT = findViewById(R.id.nameTXT)
        priceTXT = findViewById(R.id.priceTXT)
        reducedPriceTXT = findViewById(R.id.redudedPriceTXT)
        imageSearchSB = findViewById(R.id.searchPictureSB)
        cartBTN = findViewById(R.id.cartBTN)

        if (descriptionlocation == "" || descriptionlocation == "null")
        {
            descriptionTXT.setText("no description")
        }
        else{
            setupDescription(descriptionlocation)
        }

    }

    private fun setupDescription(descriptionURL : String)
    {

        val inputData : Data = Data.Builder().putString("url", descriptionURL).build()

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
                    Toast.makeText(applicationContext, "price is $price", Toast.LENGTH_LONG).show()
                    try {
                        priceTXT.setText("£ " + price.toString())
                    }
                    catch(E : Exception)
                    {
                        Toast.makeText(applicationContext, "error is ${E.toString()}", Toast.LENGTH_LONG).show()
                    }

                    val reduction : Int = workInfo.outputData.getInt("reduction", 0)
                    if (reduction > 0)
                    {
                        reducedPriceTXT.setText("$reduction off")
                    }


                    imageURLs = workInfo.outputData.getStringArray("images") as Array<String>

                    if (imageURLs.isNotEmpty())
                    {
                        itemImage = imageURLs[0]

                        doAsync{
                           val foundImage: Bitmap =  imageHandler.getBitmapFromURL(imageURLs[0], clothingIV.height, clothingIV.width)
                            
                            uiThread {
                                clothingIV.setImageBitmap(foundImage)
                            }
                        }
                        
                        setupSeekBar()
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

    private fun setupSeekBar()
    {
        imageSearchSB.max = (imageURLs.size - 1)

        imageSearchSB.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener
        {
            override fun onStartTrackingTouch(p0: SeekBar?) {

            }

            override fun onStopTrackingTouch(p0: SeekBar?) {

            }

            override fun onProgressChanged(p0: SeekBar?, position: Int, p2: Boolean) {
                doAsync{
                    val foundImage: Bitmap =imageHandler.getBitmapFromURL(imageURLs[position], clothingIV.height, clothingIV.width)
                    uiThread {
                        clothingIV.setImageBitmap(foundImage)
                    }
                }
            }
        })
    }

    private fun addToCart()
    {
        val commands : databaseCommands = databaseCommands()

        val input : Data = Data.Builder()
            .putString(commands.Cart_DB, commands.Cart_DB)
            .putString(commands.Cart_Add, commands.Cart_Add)
            .putString(commands.Cart_picture, itemImage)
            .putString(commands.Cart_name, name)
            .putDouble(commands.Cart_price, price)
            .build()

        val addToCartWorker = OneTimeWorkRequestBuilder<databaseManager>()
            .setInputData(input)
            .build()

        WorkManager.getInstance().enqueue(addToCartWorker)

    }
}
