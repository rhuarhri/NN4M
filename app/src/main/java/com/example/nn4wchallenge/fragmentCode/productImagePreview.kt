package com.example.nn4wchallenge.fragmentCode

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.nn4wchallenge.R
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.work.Data
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkInfo
import androidx.work.WorkManager
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.nn4wchallenge.database.external.GetItemDescription
import com.example.nn4wchallenge.database.internal.DatabaseCommands
import com.example.nn4wchallenge.database.internal.DatabaseManager
import com.example.nn4wchallenge.imageHandling.RetrieveImageHandler
import com.example.nn4wchallenge.slideShowCode.SlideShowAdapter
import com.example.nn4wchallenge.slideShowCode.SlideShowListener

class productImagePreview : Fragment(), SlideShowListener {
    override fun onItemClick(description: String) {
        //do nothing
    }

    //private var imageURL : String = "No URL"

    private var description : String = ""
    private var itemImage : String = ""
    private var name : String = ""
    private var price : Double = 0.0

    //private lateinit var previewIV : ImageView

    private lateinit var itemPriceTXT : TextView
    private lateinit var itemReducedPriceTXT : TextView
    private lateinit var itemNameTXT : TextView
    private lateinit var itemDescriptionTXT : TextView

    private lateinit var addToCartBTN : Button

    private lateinit var itemImagesRV : RecyclerView

    private lateinit var callback : fromFragment

    override fun onAttach(activity: Activity) {
        super.onAttach(activity)
        try {
            callback = activity as fromFragment
        }
        catch(e : Exception)
        {
            Toast.makeText(context, "error is ${e.toString()}", Toast.LENGTH_LONG).show()
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        if (arguments != null)
        {
            description = arguments!!.getString("description")!!
            //displayImage()
            setupDescription(description)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.product_preview_layout, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val exitBTN : Button = view.findViewById(R.id.exitBTN)
        exitBTN.setOnClickListener {
            fragmentManager!!.beginTransaction().remove(this).commit()
        }

        /*
        val likeBTN : Button = view.findViewById(R.id.likeBTN)
        likeBTN.setOnClickListener {
            callback.onProductSelected()
        }

        val dislikeBTN : Button = view.findViewById(R.id.disLikeBTN)
        dislikeBTN.setOnClickListener {
            //could possibly more to the next product in the list
        }*/

        //previewIV = view.findViewById(R.id.imagePreviewIV)

        itemNameTXT = view.findViewById(R.id.itemTitleTXT)

        itemDescriptionTXT = view.findViewById(R.id.itemDescriptionTXT)

        itemPriceTXT = view.findViewById(R.id.itemPriceTXT)

        itemReducedPriceTXT = view.findViewById(R.id.itemReducedPriceTXT)

        addToCartBTN = view.findViewById(R.id.addItemToCartBTN)

        itemImagesRV = view.findViewById(R.id.itemImagesRV)
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
                    itemNameTXT.text = name

                    itemDescriptionTXT.text = workInfo.outputData.getString("description").toString()

                    price = workInfo.outputData.getDouble("cost", 0.0)

                    try {
                        itemPriceTXT.text = "£ " + price.toString()
                    }
                    catch(E : Exception)
                    {
                        //Toast.makeText(applicationContext, "error is ${E.toString()}", Toast.LENGTH_LONG).show()
                    }

                    val reduction : Int = workInfo.outputData.getInt("reduction", 0)
                    if (reduction > 0)
                    {
                        itemReducedPriceTXT.text = "$reduction off"
                    }



                    val imageURLs = workInfo.outputData.getStringArray("images") as Array<String>

                    if (imageURLs.isNotEmpty())
                    {
                        itemImage = imageURLs[0]
                        setupRecyclerView(imageURLs)
                    }

                    addToCartBTN.setOnClickListener {
                        addToCart()
                    }
                    /*

                    if (imageURLs.isNotEmpty())
                    {
                        itemImage = imageURLs[0]
                        setupRecyclerView(imageURLs)
                    }

                    cartBTN.setOnClickListener {
                        //cart button only usable if data available
                        addToCart()
                    }*/

                }
                if (workInfo != null && workInfo.state == WorkInfo.State.FAILED)
                {
                    itemDescriptionTXT.text = workInfo.outputData.getString("error").toString()
                }
            })
    }


    private fun setupRecyclerView(images : Array<String>)
    {

        val rvAdapter: RecyclerView.Adapter<*> = SlideShowAdapter(context!!, images,null, this)

        itemImagesRV.apply {

            setHasFixedSize(false)
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)

            adapter = rvAdapter
        }

    }

    /*
    private fun displayImage(imageURL : String)
    {
        if (context != null) {
            val getImage = RetrieveImageHandler(context!!)
            getImage.recyclerViewImageHandler(previewIV, imageURL, false)
        }
    }*/

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

        WorkManager.getInstance().getWorkInfoByIdLiveData(addToCartWorker.id)
            .observe(this, Observer { workInfo ->
                 if (workInfo != null && workInfo.state == WorkInfo.State.SUCCEEDED) {
                     callback.onProductSelected()
                     fragmentManager!!.beginTransaction().remove(this@productImagePreview).commit()
                 }
            })
    }
}