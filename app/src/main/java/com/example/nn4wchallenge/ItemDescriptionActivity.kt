package com.example.nn4wchallenge

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.*
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.work.Data
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkInfo
import androidx.work.WorkManager
import com.example.nn4wchallenge.database.external.GetItemDescription
import com.example.nn4wchallenge.database.internal.DatabaseCommands
import com.example.nn4wchallenge.database.internal.DatabaseManager
import com.example.nn4wchallenge.imageHandling.RetrieveImageHandler
import com.example.nn4wchallenge.slideShowCode.SlideShowAdapter
import com.example.nn4wchallenge.slideShowCode.SlideShowListener

class ItemDescriptionActivity : AppCompatActivity(), SlideShowListener {
    override fun onItemClick(description : String) {
        //do nothing
    }


    private lateinit var imageHandler : RetrieveImageHandler

    private lateinit var descriptionTXT : TextView
    private lateinit var nameTXT : TextView
    private lateinit var priceTXT : TextView
    private lateinit var reducedPriceTXT : TextView


    private lateinit var pictureRV : RecyclerView

    private lateinit var cartBTN : Button

    private var price : Double = 0.0
    private var itemImage : String = ""
    private var name : String = ""

    private lateinit var imageURLs : Array<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_item_description)

        val descriptionlocation : String = intent.getStringExtra("description")

        imageHandler = RetrieveImageHandler(applicationContext)
        //clothingIV = findViewById(R.id.clothingPicture)
        descriptionTXT = findViewById(R.id.descriptionTXT)
        nameTXT = findViewById(R.id.nameTXT)
        priceTXT = findViewById(R.id.priceTXT)
        reducedPriceTXT = findViewById(R.id.reducedPriceTXT)
        //imageSearchSB = findViewById(R.id.searchPictureSB)
        pictureRV = findViewById(R.id.pictureRV)

        cartBTN = findViewById(R.id.cartBTN)

        if (descriptionlocation == "" || descriptionlocation == "null")
        {
            descriptionTXT.text = "no description"
        }
        else{
            setupDescription(descriptionlocation)
        }

    }

    private fun setupDescription(descriptionURL : String)
    {

        val inputData : Data = Data.Builder().putString("url", descriptionURL).build()

        val getDescriptionWorker = OneTimeWorkRequestBuilder<GetItemDescription>()
            .setInputData(inputData)
            .build()

        WorkManager.getInstance().enqueue(getDescriptionWorker)

        WorkManager.getInstance().getWorkInfoByIdLiveData(getDescriptionWorker.id)
            .observe(this, Observer { workInfo ->
                if (workInfo != null && workInfo.state == WorkInfo.State.SUCCEEDED) {

                    name = workInfo.outputData.getString("name").toString()
                    nameTXT.text = name

                    descriptionTXT.text = workInfo.outputData.getString("description").toString()

                    price = workInfo.outputData.getDouble("cost", 0.0)

                    try {
                        priceTXT.text = "£ " + price.toString()
                    }
                    catch(E : Exception)
                    {
                        Toast.makeText(applicationContext, "error is ${E.toString()}", Toast.LENGTH_LONG).show()
                    }

                    val reduction : Int = workInfo.outputData.getInt("reduction", 0)
                    if (reduction > 0)
                    {
                        reducedPriceTXT.text = "$reduction off"
                    }


                    imageURLs = workInfo.outputData.getStringArray("images") as Array<String>

                    if (imageURLs.isNotEmpty())
                    {
                        itemImage = imageURLs[0]
                        setupRecyclerView(imageURLs)
                    }

                    cartBTN.setOnClickListener {
                        //cart button only usable if data available
                        addToCart()
                    }

                }
                if (workInfo != null && workInfo.state == WorkInfo.State.FAILED)
                {
                    descriptionTXT.text = workInfo.outputData.getString("error").toString()
                }
            })
    }

    private fun setupRecyclerView(images : Array<String>)
    {

        val rvAdapter: RecyclerView.Adapter<*> = SlideShowAdapter(applicationContext, images,null, this)

        pictureRV.apply {

            setHasFixedSize(false)
            layoutManager = LinearLayoutManager(applicationContext, LinearLayoutManager.HORIZONTAL, false)

            adapter = rvAdapter
        }

    }

    private fun addToCart()
    {
        val commands = DatabaseCommands()

        val input : Data = Data.Builder()
            .putString(commands.Cart_DB, commands.Cart_DB)
            .putString(commands.Cart_Add, commands.Cart_Add)
            .putString(commands.Cart_picture, itemImage)
            .putString(commands.Cart_name, name)
            .putDouble(commands.Cart_price, price)
            .build()

        val addToCartWorker = OneTimeWorkRequestBuilder<DatabaseManager>()
            .setInputData(input)
            .build()

        WorkManager.getInstance().enqueue(addToCartWorker)

        goToHomeScreen()
    }

    private fun goToHomeScreen()
    {
        val goTo = Intent(applicationContext, MatchActivity::class.java)
        startActivity(goTo)
    }
}
