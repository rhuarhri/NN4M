package com.example.nn4wchallenge

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.TextureView
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.camera.core.ImageCapture
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.work.Data
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkInfo
import androidx.work.WorkManager
import com.example.nn4wchallenge.OpenCVCode.CVHandler
import com.example.nn4wchallenge.cameraCode.CameraHandler
import com.example.nn4wchallenge.database.MatchColourHandler
import com.example.nn4wchallenge.database.external.DataTranslation
import com.example.nn4wchallenge.database.internal.AddClothingHandler
import com.example.nn4wchallenge.database.internal.DatabaseCommands
import com.example.nn4wchallenge.database.internal.DatabaseManager
import com.example.nn4wchallenge.fragmentCode.*
import com.example.nn4wchallenge.imageHandling.RemoveImageHandler
import com.example.nn4wchallenge.imageHandling.SaveImageHandler
import com.example.nn4wchallenge.slideShowCode.SlideShowAdapter
import com.example.nn4wchallenge.slideShowCode.SlideShowListener
import org.opencv.android.BaseLoaderCallback
import org.opencv.android.LoaderCallbackInterface
import org.opencv.android.OpenCVLoader
import org.opencv.android.Utils
import org.opencv.core.Mat
import org.opencv.core.Rect
import java.io.File

class MatchActivity : AppCompatActivity(), fromFragment, SlideShowListener {


    private val mLoaderCallback = object : BaseLoaderCallback(this) {
        override fun onManagerConnected(status: Int) {
            when (status) {
                LoaderCallbackInterface.SUCCESS -> {
                    //success
                    openCVHandler = CVHandler()
                }
                else -> {
                    super.onManagerConnected(status)
                }
            }
        }
    }

    private lateinit var openCVHandler : CVHandler

    private lateinit var cameraManger : CameraHandler
    private lateinit var savedImage : SaveImageHandler
    private lateinit var oldFile : String

    private lateinit var totalCostTXT : TextView
    private lateinit var userClothingBTN : Button
    private lateinit var cartBTN : Button
    private lateinit var colourBTN : Button
    private lateinit var cameraBTN : Button
    private lateinit var viewImageBTN : Button
    private lateinit var filterBTN : Button
    private lateinit var flashBTN : Button
    private lateinit var cameraTV : TextureView
    private lateinit var pictureRV : RecyclerView

    //information about products the user can buy
    private lateinit var imageURLList : Array<String?>
    private lateinit var itemDescriptionURLList : Array<String?>
    private lateinit var typeList : Array<String?>
    private lateinit var seasonList : Array<String?>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_match)

        if (!OpenCVLoader.initDebug()) {
            //Internal OpenCV library not found. Using OpenCV Manager for initialization
            OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION_3_4_0, this, mLoaderCallback)
        } else {
            //OpenCV library found inside package. Using it!
            mLoaderCallback.onManagerConnected(LoaderCallbackInterface.SUCCESS)
        }

        totalCostTXT = findViewById(R.id.totalTXT)
        setupTotalPrice()

        cartBTN = findViewById(R.id.cartBTN)
        cartBTN.setOnClickListener {
            val goToViewCartScreen = Intent(applicationContext, ViewCartActivity::class.java)
            startActivity(goToViewCartScreen)
        }

        userClothingBTN = findViewById(R.id.clothingBTN)
        userClothingBTN.setOnClickListener {
            val goToUserClothingDisplayScreen = Intent(applicationContext, UserClothingDisplayActivity::class.java)
            startActivity(goToUserClothingDisplayScreen)
        }

        val accessPermissions = PermissionsHandler(this, applicationContext)
        savedImage = SaveImageHandler(applicationContext)
        oldFile = savedImage.savedPhotoPath

        cameraTV = findViewById(R.id.cameraTV)
        if (accessPermissions.checkCameraPermission()) {

            cameraManger = CameraHandler(this, cameraTV)

            cameraBTN = findViewById(R.id.cameraBTN)

            cameraBTN.setOnClickListener {

                val photoFile = savedImage.getPhotoFile()

                cameraManger.imageCapture.takePicture(photoFile, object : ImageCapture.OnImageSavedListener
                {
                    override fun onImageSaved(file: File) {
                        if (savedImage.savedPhotoPath  != oldFile)
                        {
                            //delete file if already exists
                            val imageRemover: RemoveImageHandler = RemoveImageHandler()
                            imageRemover.removeImage(oldFile)
                        }

                        val openFragment: FragmentTransaction = supportFragmentManager.beginTransaction()
                        openFragment.replace(R.id.saveInfoFRG, SaveImageNotification())
                        openFragment.commit()

                        oldFile = savedImage.savedPhotoPath

                        Toast.makeText(applicationContext, "Image captured", Toast.LENGTH_LONG).show()

                        findColourInImage()

                    }

                    override fun onError(useCaseError: ImageCapture.UseCaseError, message: String, cause: Throwable?) {
                        Toast.makeText(applicationContext, "image capture failed", Toast.LENGTH_SHORT).show()
                    }

                })

            }
        }

        viewImageBTN = findViewById(R.id.viewBTN)
        viewImageBTN.setOnClickListener {
            if (savedImage.savedPhotoPath == "")
            {
                Toast.makeText(applicationContext, "no file path", Toast.LENGTH_LONG).show()
            }
            else if (!File(savedImage.savedPhotoPath).exists())
            {
                Toast.makeText(applicationContext, "no file", Toast.LENGTH_LONG).show()
            }
            else {
                val fragIn : Bundle = Bundle()
                fragIn.putString("file", savedImage.savedPhotoPath)

                val previewImage: CapturedImagePreview = CapturedImagePreview()
                previewImage.arguments = fragIn
                val openFragment: FragmentTransaction = supportFragmentManager.beginTransaction()
                openFragment.replace(R.id.imagePreviewFRG, previewImage)
                openFragment.commit()
            }
        }

        colourBTN = findViewById(R.id.colourBTN)
        colourBTN.setOnClickListener {

            val openFragment : FragmentTransaction = supportFragmentManager.beginTransaction()
            openFragment.replace(R.id.colourPickerFRG, colourPicker())
            openFragment.commit()

        }

        filterBTN = findViewById(R.id.filterBTN)
        filterBTN.setOnClickListener {
            val openFragment : FragmentTransaction = supportFragmentManager.beginTransaction()
            openFragment.replace(R.id.colourPickerFRG, FilterHandler())
            openFragment.commit()
        }

        pictureRV = findViewById(R.id.productRV)

        flashBTN = findViewById(R.id.flashBTN)
        flashBTN.setOnClickListener {


        }

    }


    private fun findColourInImage()
    {
        if (OpenCVLoader.initDebug()) {
            openCVHandler.inputImage(savedImage.savedPhotoPath)
            hexColour = openCVHandler.getColour()
            colourBTN.setBackgroundColor(Color.parseColor("#$hexColour"))
            searchForProduct(hexColour)
        }
        else
        {
            Toast.makeText(applicationContext, "open cv not loaded", Toast.LENGTH_LONG).show()
        }
    }

/*
    private fun findColourInImage()
    {
        if (OpenCVLoader.initDebug()) {

            val input: Data = Data.Builder().putString("location", savedImage.savedPhotoPath).build()

            val colourSearchWorkRequest = OneTimeWorkRequestBuilder<CVHandler>()
                .setInputData(input)
                .build()

            WorkManager.getInstance().enqueue(colourSearchWorkRequest)

            WorkManager.getInstance().getWorkInfoByIdLiveData(colourSearchWorkRequest.id)
                .observe(this, Observer { workInfo ->
                    if (workInfo != null && workInfo.state == WorkInfo.State.SUCCEEDED) {

                        val colour: String? = workInfo.outputData.getString("colour")

                        if (colour != null) {
                            hexColour = colour
                            Toast.makeText(applicationContext, "found colour : ${colour}", Toast.LENGTH_LONG)
                                .show()
                            //colourBTN.setBackgroundColor(Color.parseColor("#$colour"))
                            //searchForProduct(colour)
                        }

                    }

                    if (workInfo != null && workInfo.state == WorkInfo.State.FAILED) {
                        val error: String? = workInfo.outputData.getString("error")

                        Toast.makeText(applicationContext, "search error : ${error.toString()}", Toast.LENGTH_LONG)
                            .show()
                    }
                })

        }
        else
        {
            Toast.makeText(applicationContext, "open cv not loaded", Toast.LENGTH_LONG).show()
        }

    }*/

    private fun searchForProduct(colour : String)
    {

        val input: Data = Data.Builder().putString("colour", colour).build()

        val searchWorkRequest = OneTimeWorkRequestBuilder<MatchColourHandler>()
            .setInputData(input)
            .build()

        WorkManager.getInstance().enqueue(searchWorkRequest)

        WorkManager.getInstance().getWorkInfoByIdLiveData(searchWorkRequest.id)
            .observe(this, Observer { workInfo ->
                if (workInfo != null && workInfo.state == WorkInfo.State.SUCCEEDED) {

                    imageURLList = workInfo.outputData.getStringArray("images")!!
                    itemDescriptionURLList = workInfo.outputData.getStringArray("descriptions")!!
                    typeList = workInfo.outputData.getStringArray("type")!!
                    seasonList = workInfo.outputData.getStringArray("season")!!

                    if (imageURLList.size == 0)
                    {
                        Toast.makeText(applicationContext, "no result found", Toast.LENGTH_LONG).show()
                    }
                    else
                    {
                        setupRecyclerView(imageURLList as Array<String>)
                    }

                }

                if (workInfo != null && workInfo.state == WorkInfo.State.FAILED) {
                    val error: String? = workInfo.outputData.getString("error")

                    Toast.makeText(applicationContext, "search error : ${error.toString()}", Toast.LENGTH_LONG)
                        .show()
                }
            })
    }

    private fun filterResult(type : String, season : String )
    {

        val input : Data = Data.Builder()
            .putStringArray("image", imageURLList)
            .putStringArray("description", itemDescriptionURLList)
            .putStringArray("type", typeList)
            .putStringArray("season", seasonList)
            .putString("userType", type)
            .putString("userSeason", season)
            .build()


        val searchWorkRequest = OneTimeWorkRequestBuilder<MatchColourHandler>()
            .setInputData(input)
            .build()

        WorkManager.getInstance().enqueue(searchWorkRequest)

        WorkManager.getInstance().getWorkInfoByIdLiveData(searchWorkRequest.id)
            .observe(this, Observer { workInfo ->
                if (workInfo != null && workInfo.state == WorkInfo.State.SUCCEEDED) {

                    imageURLList = workInfo.outputData.getStringArray("images")!!
                    itemDescriptionURLList = workInfo.outputData.getStringArray("descriptions")!!
                    typeList = workInfo.outputData.getStringArray("type")!!
                    seasonList = workInfo.outputData.getStringArray("season")!!

                    if (imageURLList.size == 0)
                    {
                        Toast.makeText(applicationContext, "no result found", Toast.LENGTH_LONG).show()
                    }
                    else
                    {
                        setupRecyclerView(imageURLList as Array<String>)
                    }

                }

                if (workInfo != null && workInfo.state == WorkInfo.State.FAILED) {
                    val error: String? = workInfo.outputData.getString("error")

                    Toast.makeText(applicationContext, "search error : ${error.toString()}", Toast.LENGTH_LONG)
                        .show()
                }
            })


    }

    private fun setupTotalPrice()
    {
        val commands = DatabaseCommands()

        val input : Data = Data.Builder()
            .putString(commands.Cart_DB, commands.Cart_DB)
            .putString(commands.Cart_Get_Prices, commands.Cart_Get_Prices)
            .build()

        val getTotalWorker = OneTimeWorkRequestBuilder<DatabaseManager>().setInputData(input).build()


        WorkManager.getInstance().enqueue(getTotalWorker)

        WorkManager.getInstance().getWorkInfoByIdLiveData(getTotalWorker.id).observe(this, Observer {
                workInfo ->

            val total = workInfo.outputData.getDouble(commands.Cart_total_price, 0.0)

            totalCostTXT.text = "total $total"

        })
    }

    private fun setupRecyclerView(images : Array<String>)
    {

        val rvAdapter: RecyclerView.Adapter<*> = SlideShowAdapter(applicationContext, images, this)

        pictureRV.apply {

            setHasFixedSize(false)
            layoutManager = LinearLayoutManager(applicationContext, LinearLayoutManager.HORIZONTAL, false)

            adapter = rvAdapter
        }

    }

//from fragment info
    private var hexColour : String = "ff000000"
    override fun onColourSelected(colourHex: String) {
        hexColour = colourHex
        colourBTN.setBackgroundColor(Color.parseColor("#$colourHex"))
        searchForProduct(colourHex)
    }

    private var type = "dress"
    private var season = "summer"
    override fun onFilterChange(type: String, season: String) {
        this.type = type
        this.season = season

        if (type == null || type == "")
        {
            Toast.makeText(applicationContext, "search error : no type", Toast.LENGTH_LONG)
                .show()
        }
        else if (season == null || season == "")
        {
            Toast.makeText(applicationContext, "search error : no season", Toast.LENGTH_LONG)
                .show()
        }
        else {
            filterResult(type, season)
        }
    }

    private lateinit var addManager : AddClothingHandler
    override fun onSaveImageGranted() {
        addManager = AddClothingHandler(applicationContext)
        if (savedImage.savedPhotoPath != "" && File(savedImage.savedPhotoPath).exists())
        {
            addManager.setPicture(savedImage.savedPhotoPath)
            addManager.setClothingType(type)
            addManager.setClothingSeason(season)
            val converter : DataTranslation = DataTranslation()
            converter.stringToRGB(hexColour)
            addManager.setClothingColour(
                converter.redAmount, converter.greenAmount, converter.blueAmount
            )
            addManager.saveClothingItem()
        }

    }

    override fun onProductSelected() {

        val descriptionURL : String? = itemDescriptionURLList[productPosition]

        if (descriptionURL != null && descriptionURL != "") {
            val goToItemDescriptionScreen = Intent(applicationContext, ItemDescriptionActivity::class.java)
            goToItemDescriptionScreen.putExtra("description", descriptionURL)
            startActivity(goToItemDescriptionScreen)
        }
    }

    //from recyclerView
    private var productPosition : Int = 0
    override fun onItemClick(position: Int) {
        Toast.makeText(applicationContext, "position is $position", Toast.LENGTH_LONG).show()
        productPosition = position

        val imageURL : String? = imageURLList[productPosition]

        if(imageURL != null && imageURL != "") {
            val fragIn: Bundle = Bundle()
            fragIn.putString("position", imageURL)


            val previewImage: productImagePreview = productImagePreview()
            previewImage.arguments = fragIn
            val openFragment: FragmentTransaction = supportFragmentManager.beginTransaction()
            openFragment.replace(R.id.imagePreviewFRG, previewImage)
            openFragment.commit()
        }
    }

}
